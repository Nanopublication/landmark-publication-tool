package org.biosemantics.landmark.controller;

import info.aduna.iteration.CloseableIteration;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.Charsets;
import org.biosemantics.landmark.model.LandmarkPublication;
import org.biosemantics.landmark.triplestore.LandmarkException;
import org.biosemantics.landmark.triplestore.StoreManager;
import org.openrdf.query.BindingSet;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.impl.EmptyBindingSet;
import org.openrdf.query.parser.ParsedQuery;
import org.openrdf.query.parser.sparql.SPARQLParser;
import org.openrdf.sail.SailConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.base.Objects;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class NanopubController {

    @Inject
    @Named("nativeStore")
    private StoreManager landmarkStore;
    private static final Logger LOG = LoggerFactory
            .getLogger(NanopubController.class);
    // @Inject
    // @Named("autoCompleteStore")
    // private StoreManager autoCompleteStore = null;
    @Inject
    @Named("baseURI")
    private String baseURI;


    /*
     * @Inject
     * 
     * @Named("tableQuerySingleResource") private Resource tableQuerySingle;
     */
    public NanopubController() {
        // TODO Auto-generated constructor stub
    }

    @RequestMapping(value = "/nanopub/{nanopubid}")
    public ModelAndView showNanopubView(
            @PathVariable("nanopubid") final String id,
            HttpServletResponse response) throws IOException {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("nanopub");

        try {
            LandmarkPublication pub = landmarkStore.retrieveLandmarkPublication(id);

            LOG.info(Objects.toStringHelper(pub)
                    .add("subject", pub.getSubjectUri())
                    .add("object", pub.getObjectUri())
                    .add("subjectLabel", pub.getSubjectLabel())
                    .add("objectLabel", pub.getObjectLabel())
                    .add("author", pub.getAuthor())
                    .add("id", pub.getId())
                    .add("discoveryType", pub.getDiscoveryType())
                    .toString());
            mav.addObject("publication", pub);

        }
        catch (LandmarkException ex) {
            // TODO pass on error message to the user
            LOG.error(ex.getMessage(), ex);
        }

        return mav;
    }

    @RequestMapping(value = "/nanopubList")
    public ModelAndView showNanopubView(HttpServletResponse response)
            throws IOException {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("nanopubList");
        try {
            Set<LandmarkPublication> publicationsSet = landmarkStore.listLandmarkPublications();
            mav.addObject("publicationsSet", publicationsSet);
        }
        catch (LandmarkException ex) {
            LOG.error(ex.getMessage(), ex);
            // TODO pass on error message to the user
        }
        return mav;
    }   
    
    @RequestMapping(value = "/rdf/{id}", method = RequestMethod.GET)

    public ModelAndView getFile(@PathVariable String id, HttpServletResponse response) 
            throws LandmarkException {
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("nanopubrdf");
        
             
        LandmarkPublication landmarkpub = 
                landmarkStore.retrieveLandmarkPublicationTrig(id);   
        mav.addObject("landmarkpub", landmarkpub);
        return mav;
    
       

}
}
