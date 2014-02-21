package org.biosemantics.landmark;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JSONP filter implementation until Spring provides an easy way to handle JSONP requests.
 * @author <a href="mailto:kees.burger@nbic.nl">Kees Burger</a>
 */
public class JsonpFilter implements Filter {
    /**
     * Name of the parameter that defines the callback function.
     */
    protected static final String PARAMETER_CALLBACK = "callback";
    /**
     * Content type for JSONP responses, as per RFC4329.
     * @see <a href="http://www.rfc-editor.org/rfc/rfc4329.txt">RDF4329</a>
     */
    protected static final String CONTENT_TYPE = "application/javascript";
    /**
     * Logger instance.
     */
    private static final Logger logger = LoggerFactory.getLogger(JsonpFilter.class);
    
    @Override
    public void init(final FilterConfig filterConfig) throws ServletException { }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest httpReq = (HttpServletRequest)request;
        
        final String callbackParameter = httpReq.getParameter(PARAMETER_CALLBACK);
        if (callbackParameter != null) {
            logger.debug("Using value \"{}\" for the jsonp callback", callbackParameter);
            
            final HttpServletResponse httpRes = (HttpServletResponse)response;
            final OutputStream out = httpRes.getOutputStream();
            final JsonpResponseWrapper wrapper = new JsonpResponseWrapper(httpRes);
            
            chain.doFilter(httpReq, wrapper);
            
            wrapper.setContentType(CONTENT_TYPE);
            
            out.write(callbackParameter.getBytes());
            out.write("(".getBytes());
            out.write(wrapper.getData());
            out.write(");".getBytes());
            out.close();
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() { }
    
    /**
     * Wrapper class for the servlet response.
     */
    protected class JsonpResponseWrapper extends HttpServletResponseWrapper {
        /**
         * Outputstream for the wrapped response.
         */
        protected ByteArrayOutputStream outputStream;
        
        /**
         * Initializes the outputstream and parent class with the given response.
         * @param response the wrapped response.
         */
        public JsonpResponseWrapper(final HttpServletResponse response) {
            super(response);
            outputStream = new ByteArrayOutputStream();
        }
        
        /**
         * Retrieves the wrapped data.
         * @return the concent of the {@link #outputStream}.
         */
        public byte[] getData() {
            return outputStream.toByteArray();
        }
        
        @Override
        public PrintWriter getWriter() throws IOException {
            return new PrintWriter(outputStream, true);
        }
        
        @Override
        public ServletOutputStream getOutputStream() throws IOException {
            return new ServletOutputStream() {
                @Override
                public void write(final int b) throws IOException {
                    outputStream.write(b);
                }
            };
        }
    }
}
