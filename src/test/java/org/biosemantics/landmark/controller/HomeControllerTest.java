/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.biosemantics.landmark.controller;

import com.google.common.io.Files;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.Charsets;
import org.biosemantics.landmark.config.MvcConfiguration;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;


/**
 *
 * @author reinout
 */
@ContextConfiguration(classes = {MvcConfiguration.class})
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class HomeControllerTest {
    
    @Inject
    private WebApplicationContext wac;
    
    //see http://static.springsource.org/spring/docs/3.2.x/spring-framework-reference/html/testing.html#spring-mvc-test-framework
    private MockMvc mockMvc;
    
    public HomeControllerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of showHomePage method, of class HomeController.
     */
    @Test
    @Ignore
    public void testShowHomePage() throws Exception {
        System.out.println("showHomePage");
        HttpServletResponse response = null;
        HomeController instance = new HomeController();
        ModelAndView expResult = null;
        ModelAndView result = instance.showHomePage(response);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }



    /**
     * Test of submit method, of class HomeController.
     */
    @Test
    @Ignore
    public void testSubmit() {
        System.out.println("submit");
        String sub = "";
        String obj = "";
        String rid = "";
        String pmid = "";
        String doi = "";
        String pdate = "";
        String inst = "";
        HttpServletResponse response = null;
        HomeController instance = new HomeController();
        instance.submit(sub, obj, rid, pmid, doi, pdate, inst, response);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
