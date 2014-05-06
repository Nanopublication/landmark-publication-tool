package org.biosemantics.landmark.triplestore;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Formatter;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;


import org.apache.commons.io.Charsets;
import org.biosemantics.landmark.model.LandmarkPublication;
import org.openrdf.model.Literal;
import org.openrdf.model.impl.ValueFactoryImpl;
import org.openrdf.model.vocabulary.XMLSchema;
import org.openrdf.query.BindingSet;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.query.algebra.evaluation.function.datetime.Now;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.repository.sparql.SPARQLRepository;
import org.openrdf.rio.RDFFormat;
import org.openrdf.sail.Sail;
import org.openrdf.sail.memory.MemoryStore;
import org.openrdf.sail.nativerdf.NativeStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.Resources;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import org.nanopub.MalformedNanopubException;
import org.nanopub.Nanopub;
import org.nanopub.NanopubImpl;
import org.nanopub.NanopubUtils;
import org.openrdf.model.URI;
import org.openrdf.model.impl.URIImpl;
import org.openrdf.rio.RDFHandlerException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class StoreManagerImpl implements StoreManager {

    private static Logger LOG = LoggerFactory.getLogger(StoreManagerImpl.class);
    private final String landmarkTemplate;
    private final String tableQuery;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private String baseUri;
    private final String LANDMARKID_VAR = "LANDMARKID";
    private static final String GET_MAX_ID_QUERY = "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>\n"
            + "select ?id where {?x skos:notation ?id} ORDER BY DESC (?id)";
    private Repository repository;
    private RepositoryConnection repositoryConnection; // for SPARQL queries
    private String outputFile;

    public StoreManagerImpl() throws IOException {
        this.tableQuery = Resources.toString(getClass().getResource("landmark-query.sparql"), Charsets.UTF_8);
        this.landmarkTemplate = Resources.toString(getClass().getResource("landmark-template.rdf"), Charsets.UTF_8);
        Sail store = new MemoryStore();

        this.repository = new SailRepository(store);
    }

    // TODO: destructor with sync and cleanup
    public StoreManagerImpl(final String location) throws RepositoryException, IOException {
        this.tableQuery = Resources.toString(getClass().getResource("landmark-query.sparql"), Charsets.UTF_8);
        this.landmarkTemplate = Resources.toString(getClass().getResource("landmark-template.rdf"), Charsets.UTF_8);
        File storeLoc = new File(location);
        Sail store = new NativeStore(storeLoc);

        //this.repository = new SailRepository(store);
        this.repository = new SPARQLRepository("http://agraph.biosemantics.org/repositories/landmarks");
        this.repository.initialize();
    }

    @Override
    public String getBaseUri() {
        return baseUri;
    }

    @Override
    public void setBaseUri(String baseUri) {
        this.baseUri = baseUri;
    }

    private RepositoryConnection getRepositoryConnection() throws Exception {
        if (repositoryConnection == null || !repositoryConnection.isOpen()) {
            repositoryConnection = repository.getConnection();
        }

        return repositoryConnection;
    }

    @Override
    public void close() throws Exception {
        try {
            if ((repositoryConnection != null) && repositoryConnection.isOpen()) {
                repositoryConnection.close();
            }
        }
        catch (RepositoryException exc) {
            LOG.error("Error closing repository connection!");
            throw (exc);
        }
        finally {
            repositoryConnection = null;

            try {
                if (repository != null) {
                    repository.shutDown();
                }
            }
            catch (Exception exc2) {
                LOG.error("Error closing repository!");
                throw (exc2);
            }
            finally {
                repository = null;
            }
        }
    }

    @Override
    public LandmarkPublication createLandmarkPublication(
            final LandmarkPublication pub) throws LandmarkException {
        RepositoryConnection conn = null;

        try { // calling container will not catch exceptions (or it sends
            // exception directly as response?)
            // so using catch-all here
            conn = getRepositoryConnection();

            // Get the ID for the next landmark nanopub
            TupleQuery id_query = conn.prepareTupleQuery(QueryLanguage.SPARQL,
                    GET_MAX_ID_QUERY, baseUri);
            TupleQueryResult tq_result = id_query.evaluate();

            String nextId = "1";

            if (tq_result.hasNext()) {

                nextId = String.valueOf(1 + Integer.parseInt(tq_result.next()
                        .getValue("id").stringValue()));
            }
            LOG.info("Landmark will be submitted with ID: " + nextId);
            pub.setId(nextId);

            // Creation date:
            Literal l = new Now().evaluate(new ValueFactoryImpl());
            LOG.info("Landmark will be submitted with DateTime: " + l.stringValue());
            pub.setClaimDate(l.stringValue());

            // Use template to build landmark nanopub
            StringBuilder sb = new StringBuilder();
            Formatter formatter = new Formatter(sb, Locale.US);
            String format = landmarkTemplate.replaceAll(LANDMARKID_VAR, nextId);
            formatter.format(format,
                    pub.getSubjectUri(), pub.getObjectUri(),
                    pub.getSubjectUri(), pub.getSubjectLabel(),
                    pub.getObjectUri(), pub.getObjectLabel(),
                    pub.getPubmedId(), pub.getAuthor(),
                    pub.getExperimentType(), pub.getDiscoveryType(),
                    DATE_FORMAT.format(pub.getPublicationDate()),
                    pub.getAuthor(),
                    pub.getClaimDate(), pub.getCuratorId(),
                    pub.getDetails(), pub.getInstitution());

            LOG.info("Submitting nanopub with content: \n" + sb.toString());
            // Store landmark nanopub
            Reader r = new StringReader(sb.toString());
            formatter.close();
            conn.add(r, baseUri, RDFFormat.TRIG); // baseURI allowed null for
            // httprepository

            // myCon.commit();
        }
        catch (Exception e) {
            LOG.error("Error creating Landmark publication!");
            throw (new LandmarkException(e));
        }
        finally {
            if (conn != null) {
                try {
                    conn.close();
                }
                catch (Exception e) {
                    LOG.error("Error closing repository connection!");
                    throw (new LandmarkException(e));
                }
            }
        }

        return pub;
    }

    private static LandmarkPublication bindingSetToLMP(BindingSet row) throws ParseException {
        LandmarkPublication pub = new LandmarkPublication();
        
        if (row.hasBinding("np")) {
            pub.setNanoPublicationURL("" + row.getBinding("np").getValue().stringValue());
        }

        if (row.hasBinding("subjectUri")) {
            pub.setSubjectUri("" + row.getBinding("subjectUri").getValue().stringValue());
        }

        if (row.hasBinding("objectUri")) {
            pub.setObjectUri("" + row.getBinding("objectUri").getValue().stringValue());
        }

        if (row.hasBinding("subjectLabel")) {
            pub.setSubjectLabel("" + row.getBinding("subjectLabel").getValue().stringValue());
        }

        if (row.hasBinding("objectLabel")) {
            pub.setObjectLabel("" + row.getBinding("objectLabel").getValue().stringValue());
        }

        if (row.hasBinding("claimDate")) { // TODO: parse this one to a Date as well
            pub.setClaimDate("" + row.getBinding("claimDate").getValue().stringValue());
        }

        if (row.hasBinding("id")) { // ? id?
            pub.setId("" + row.getBinding("id").getValue().stringValue());
        }

        if (row.hasBinding("pubMedID")) {
            pub.setPubmedId("" + row.getBinding("pubMedID").getValue().stringValue());
        }

        if (row.hasBinding("author")) {
            pub.setAuthor("" + row.getBinding("author").getValue().stringValue());
        }

        if (row.hasBinding("date")) {
            pub.setPublicationDate(DATE_FORMAT.parse(row.getBinding("date").getValue().stringValue()));
        }

        if (row.hasBinding("curatorId")) {
            pub.setCuratorId("" + row.getBinding("curatorId").getValue().stringValue());
        }

        if (row.hasBinding("inst")) { // ?
            pub.setInstitution("" + row.getBinding("inst").getValue().stringValue());
        }

        if (row.hasBinding("experimentType")) {
            pub.setExperimentType(""
                    + row.getBinding("experimentType").getValue().stringValue());
        }

        if (row.hasBinding("discoveryType")) {
            pub.setDiscoveryType(""
                    + row.getBinding("discoveryType").getValue().stringValue());
        }

        if (row.hasBinding("details")) {
            pub.setDetails("" + row.getBinding("details").getValue().stringValue());
        }

        return pub;
    }

    @Override
    public LandmarkPublication retrieveLandmarkPublication(final String id)
            throws LandmarkException {

        // return this.singletonLMP;
        // TODO Auto-generated method stub
        LandmarkPublication pub = new LandmarkPublication();

        RepositoryConnection conn = null;
        try {
            conn = getRepositoryConnection();

            // Get the ID for the next landmark nanopub
            TupleQuery query = conn.prepareTupleQuery(QueryLanguage.SPARQL,
                    tableQuery, baseUri);
            Literal idLiteral = (new ValueFactoryImpl()).createLiteral(id, XMLSchema.NON_NEGATIVE_INTEGER);
            LOG.info("?id bound to " + idLiteral.toString());
            query.setBinding("id", idLiteral);
            TupleQueryResult queryResults = query.evaluate();

            if (queryResults.hasNext()) {
                BindingSet row = queryResults.next();

                pub = bindingSetToLMP(row);
            }            

        }
        catch (Exception e) {
            LOG.error("Error retrieving LMP!");
            throw (new LandmarkException(e));
        }
        finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            }
            catch (Exception e) {
                LOG.error("Error closing repository connection!");
                throw (new LandmarkException(e));
            }
        }
        
        
        
        
        

        return pub;
    }
    
    
    
    public LandmarkPublication retrieveLandmarkPublicationTrig(final String id)
            throws LandmarkException {

        // return this.singletonLMP;
        // TODO Auto-generated method stub
        LandmarkPublication pub = new LandmarkPublication();

       
        try {
                     
            
            String npURLStr = "http://localhost:8080/landmark-"
                    + "publication-tool/nanopub/"+id;
                     
                URI npURI = new URIImpl(npURLStr);

                Nanopub np = new NanopubImpl(repository, npURI);

                String npTRIG = nanopublicationTRIG(np);
                npTRIG = replaceURL(npTRIG, npURLStr, id);
                //LOG.info(npTRIG);
                //writeNanopublicationToFile(np);
                pub.setNanoPublicationTrigString(npTRIG);
            

        }
        catch (Exception e) {
            LOG.error("Error retrieving LMP!");
            throw (new LandmarkException(e));
        }
        
        
        
        
        
        

        return pub;
    }

    @Override
    public Set<LandmarkPublication> listLandmarkPublications()
            throws LandmarkException {
        // return Collections.singleton(this.singletonLMP);
        Set<LandmarkPublication> resultSet = new HashSet<LandmarkPublication>();

        RepositoryConnection conn = null;
        try {
            conn = getRepositoryConnection();

            // Get the list of LMPs:
            TupleQuery query = conn.prepareTupleQuery(QueryLanguage.SPARQL,
                    tableQuery, baseUri);
            TupleQueryResult queryResults = query.evaluate();

            while (queryResults.hasNext()) {
                BindingSet row = queryResults.next();

                LandmarkPublication pub = bindingSetToLMP(row);

                resultSet.add(pub);
            }

        }
        catch (Exception e) {
            LOG.error("Error retrieving landmark publication list!");
            throw (new LandmarkException(e));
        }
        finally {
            if (conn != null) {
                try {
                    conn.close();
                }
                catch (Exception e) {
                    LOG.error("Error closing repository connection!");
                    throw (new LandmarkException(e));
                }
            }
        }

        return resultSet;
    }
    
    
    private String nanopublicationTRIG (Nanopub np) {
        String npTrig = null;
        
        ByteArrayOutputStream npOutStream = new ByteArrayOutputStream();
        try {
            NanopubUtils.writeToStream(np, npOutStream, RDFFormat.TRIG);
            npTrig = new String(npOutStream.toByteArray(), "UTF-8"); 		
        } catch (RDFHandlerException | UnsupportedEncodingException ex) {
            LOG.error("Error creating nanopublication TRIG");           
        } 
        
        
        return npTrig;
    }
    
    private void writeNanopublicationToFile (Nanopub np) {
        
        try {
            File file = new File(this.outputFile);
            FileOutputStream fop = new FileOutputStream(file);
        
            NanopubUtils.writeToStream(np, fop, RDFFormat.TRIG);      
            fop.close();           
        } catch (RDFHandlerException | FileNotFoundException  ex) {
            LOG.error("Error creating nanopublication trig file");           
        }
        catch (IOException ex) {
            LOG.error("Error creating nanopublication trig file"); 
        } 
    }
    
    public String replaceURL (String nanopub, String nanopubURI, String id) {
    
        String landmark = "<"+nanopubURI+"#landmark_"+id+">";
        String publication = "<"+nanopubURI+"#publication_"+id+">";
        String assertion = "<"+nanopubURI+"#assertion>";
        String provenance = "<"+nanopubURI+"#provenance>";
        String publicationInfo = "<"+nanopubURI+"#publicationInfo>";
        String thisPrefix = "@prefix this: <"+nanopubURI+">";
        
        String newBase = "@base <>."+"\n@prefix : <#>";
        String newLandmark = ":landmark_"+id;
        String newPublication = ":publication_"+id;
        String newAssertion = ":assertion";
        String newProvenance = ":provenance";
        String newPublicationInfo = ":publicationInfo";
        
        nanopub = nanopub.replace(thisPrefix, newBase);
        nanopub = nanopub.replace("this:", "<>");
        
        nanopub = nanopub.replace(landmark, newLandmark);
        nanopub = nanopub.replace(publication, newPublication);
        nanopub = nanopub.replace(assertion, newAssertion);
        nanopub = nanopub.replace(provenance, newProvenance);
        nanopub = nanopub.replace(publicationInfo, newPublicationInfo);
        
        return nanopub;
        
    
    }
}
