package org.biosemantics.landmark.triplestore;

import java.util.Set;

import org.biosemantics.landmark.model.LandmarkPublication;


/**
 * Interface of a landmark publication store.
 * 
 * @author evanderhorst
 * @author m. Thomson
 *
 */
public interface StoreManager {
	
	
	/**
	 * Creates a new landmark publication in the store.
	 * 
	 * @param lm the new landmark publication to be stored. Note: lm should have an ID of -1.
	 * @return the new landmark publication with valid ID. 
	 */
	public LandmarkPublication createLandmarkPublication(final LandmarkPublication lm) throws LandmarkException;
	
	
	/**
	 * Retrieves the landmark publication with the specified ID.
	 * 
	 * @param ID the string identifier of the landmark publication.
	 * @return the specified landmark publication, or null if it doesn't exist.
	 */
	public LandmarkPublication retrieveLandmarkPublication(final String ID) throws LandmarkException;
	
	
	/**
	 * Retrieves the set of all landmark publications in the store.
	 * 
	 * @return the set of all landmark publications.
	 * @throws LandmarkException 
	 */
	public Set<LandmarkPublication> listLandmarkPublications() throws LandmarkException;
	
	
	/**
	 * Closes the StoreManager and the underlying Repository.
	 * 
	 * @throws Exception
	 */
	public void close() throws Exception;


	public abstract void setBaseUri(String baseUri);


	public abstract String getBaseUri(); 
        
        public LandmarkPublication retrieveLandmarkPublicationTrig (final String ID) throws LandmarkException;

}
