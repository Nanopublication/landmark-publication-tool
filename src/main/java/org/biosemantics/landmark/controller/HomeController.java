package org.biosemantics.landmark.controller;

import com.google.common.base.Objects;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.biosemantics.landmark.model.LandmarkPublication;
import org.biosemantics.landmark.triplestore.LandmarkException;
import org.biosemantics.landmark.triplestore.StoreManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class HomeController {

    @Inject
    @Named("nativeStore")
    private StoreManager landmarkStore;        
    
    private static final Logger LOG = LoggerFactory.getLogger(HomeController.class);
    
//    @Inject
//    @Named("autoCompleteStore")
//    private StoreManager autoCompleteStore = null;
    
//    @Inject
//    @Named("landmarkTemplateResource")
//    private Resource landmarkTemplate;
//    
//    @Inject
//    @Named("baseURI")
//    private String baseURI;
//    
//    @Inject
//    @Named("tableQueryResource")
//    private Resource tableQuery;
//    
//    private final String LANDMARKID_VAR = "LANDMARKID"; 
//        
//    private static final String GET_MAX_ID_QUERY = 
//            "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>\n" +
//                    "select ?id where {?x skos:notation ?id} ORDER BY DESC (?id)"; 

    public HomeController() {
        //default constructor
    }
        
    @RequestMapping({"/", "/home"})
    public ModelAndView showHomePage(HttpServletResponse response) throws IOException {
        
        return new ModelAndView("home", "landmarkPublication", new LandmarkPublication());
    }       

    @RequestMapping("/landmark/autocomplete/{query}")
    @ResponseBody
    public List<String> autocomplete(@PathVariable("query") final String q,
            final HttpServletResponse response) {
        List<String> landmarkResults = new ArrayList<String>();
/* Temporarily disable autocomplete until we have a proper autocomplete db set up
        try {
            LOG.info("Get connection to the landmark store");
            final SailConnection landmarkSC = landmarkStore.getSailConnection();
            //TODO: add regex filter to this query
            final String LANDMARK_COMPLETES = "select distinct ?y where { ?x <http://www.nanopub.org/todoinstitution> ?y }";
            landmarkResults = getSingleQueryResult(LANDMARK_COMPLETES, landmarkSC, "y", 5);
            LOG.info("Got completions from history, count: #" + landmarkResults.size());
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        try {
            LOG.info("Get connection to the autocomplete store");
            final SailConnection autoComplSC = autoCompleteStore.getSailConnection();
            final String EXTRA_COMPLETES = "select distinct ?z where {?z a <http://dbpedia.org/ontology/School> FILTER regex(str(?z), \"" + q + "\", \"i\")}";
            List<String> autoCompleteResults = getSingleQueryResult(EXTRA_COMPLETES, autoComplSC, "z", 5);
            LOG.info("Got completions from autocompleteStore, count: #" + autoCompleteResults.size());

            landmarkResults.addAll(autoCompleteResults);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
*/
        return landmarkResults;
    }


    @RequestMapping(value={"/", "/home"},method=RequestMethod.POST) // Spring in Action 3e. p.187
    public String addLandmarkPublicationFromForm(
            @Valid @ModelAttribute("landmarkPublication") LandmarkPublication pub, 
            BindingResult bindingResult) {
        
        if (bindingResult.hasErrors()) {
        	for (ObjectError e: bindingResult.getAllErrors()) {
        		LOG.error(e.toString());
        	}
            return "home"; 
        }
        
        
        LOG.info(Objects.toStringHelper(pub)
                .add("subjectUri", pub.getSubjectUri())
                .add("objectUri", pub.getObjectUri())
                .add("subjectLabel", pub.getSubjectLabel())
                .add("objectLabel", pub.getObjectLabel())
                .add("author", pub.getAuthor())
                .add("id", pub.getId())
                .add("discoveryType", pub.getDiscoveryType())
                .add("publicationDate", pub.getPublicationDate())
                .toString());
        
        String nanopubid = "";
        try {
			LandmarkPublication lmp = this.landmarkStore.createLandmarkPublication(pub);
			nanopubid = lmp.getId();
        } catch (LandmarkException e) {
			// TODO Auto-generated catch block
			LOG.error("Error adding Landmark publication", e);
		}
        
        String redirect = "redirect:/nanopub/" + nanopubid;
        LOG.info("Redirecting to: " + redirect);
        return redirect;
    }
    
    /*
     * TODO: error handling, currently throws all exceptions
     */
    @RequestMapping(value = "/landmark/submit",method=RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED) // Spring in Action, 3ed. p.286
    @ResponseBody
    public void submit(
            //            @RequestParam("autosub") final String autosub,
            //            @RequestParam("autoobj") final String autoobj,
            @RequestParam("sub") final String sub,
            @RequestParam("obj") final String obj,
            @RequestParam(value = "rid", required = false) final String rid,
            @RequestParam(value = "pmid", required = false) final String pmid,
            @RequestParam(value = "doi", required = false) final String doi,
            @RequestParam("pdate") final String pdate,
            @RequestParam("inst") final String inst,
            final HttpServletResponse response) {
    	
    	
        //http://127.0.0.1:8181/npvalidator/validator/landmark?autoobj=a&autosub=b&obj=c&sub=d&rid=e&pmid=f&doi=g&pdate=h
        // Initialize nanopub store.. is there a proper initializer for controller?
        
        //return 0;
    }
    
}
