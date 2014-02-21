/**
 * 
 */
package org.biosemantics.landmark.triplestore;

import java.text.SimpleDateFormat;
import static org.junit.Assert.*;

import java.util.Set;

import org.biosemantics.landmark.model.LandmarkPublication;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;


/**
 * @author evanderhorst
 *
 */
@Ignore
public class StoreManagerImplTest {
	
	@Rule 
	public TemporaryFolder tempFolder = new TemporaryFolder();
	
    private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	private StoreManager storeManagerImpl;
    private LandmarkPublication singletonLMP;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
        // Set bogus LMP:
		this.singletonLMP = new LandmarkPublication();
        this.singletonLMP.setAuthor("Eelke");
        this.singletonLMP.setCuratorId("Mark");        
        this.singletonLMP.setDetails("See below");
        this.singletonLMP.setDiscoveryType("Analytical");
        this.singletonLMP.setExperimentType("Binding Study");        
        this.singletonLMP.setClaimDate("2013-06-07");
        this.singletonLMP.setInstitution("LUMC"); 
        this.singletonLMP.setObjectUri("e78ae519-4b42-4dd2-985e-a52bdf831190");
        this.singletonLMP.setObjectLabel("beta-Endorphin Receptor");
        this.singletonLMP.setSubjectUri("d2358aee-db99-47bf-939c-61ceecfede31");
        this.singletonLMP.setSubjectLabel("beta-Endorphin");
        this.singletonLMP.setPublicationDate(DATE_FORMAT.parse("2013-06-07"));
        this.singletonLMP.setPubmedId("BlaBla");
        
        // Set the StoreManagerImpl fixture:
        this.storeManagerImpl = new StoreManagerImpl(tempFolder.getRoot().getAbsolutePath());
        this.storeManagerImpl.setBaseUri("http://nanopub.org");
	}
    
 
	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		this.storeManagerImpl.close();
		
		this.storeManagerImpl = null;
		this.singletonLMP = null;
	}


	
	/**
	 * Test method for {@link org.biosemantics.landmark.triplestore.StoreManagerImpl#createLandmarkPublication(org.biosemantics.landmark.model.LandmarkPublication)}.
	 * @throws LandmarkException 
	 */
	@Test
	public void testCreateLandmarkPublication() throws Exception  {
		LandmarkPublication lmp = storeManagerImpl.createLandmarkPublication(this.singletonLMP);
		
		assertEquals("Nanopub ID not equal", "1", lmp.getId());
	}
	

	/**
	 * Test method for {@link org.biosemantics.landmark.triplestore.StoreManagerImpl#retrieveLandmarkPublication(java.lang.String)}.
	 * @throws LandmarkException
	 */
	@Test        
	public void testRetrieveLandmarkPublication() throws Exception {
		LandmarkPublication lmpA = storeManagerImpl.createLandmarkPublication(this.singletonLMP);
		
		assertEquals("Nanopub ID not equal", "1", lmpA.getId());
		
		LandmarkPublication lmpB = storeManagerImpl.retrieveLandmarkPublication("1");
	}
	

	/**
	 * Test method for {@link org.biosemantics.landmark.triplestore.StoreManagerImpl#listLandmarkPublications()}.
	 */
	@Test
	public void testListLandmarkPublications() throws Exception {
		storeManagerImpl.createLandmarkPublication(this.singletonLMP);
		
		Set<LandmarkPublication> set = storeManagerImpl.listLandmarkPublications();
		
		assertEquals("Number of landmark publications != 1", 1, set.size());
		assertEquals("Nanopub ID not equal", "1", set.iterator().next().getId());
	}

}
