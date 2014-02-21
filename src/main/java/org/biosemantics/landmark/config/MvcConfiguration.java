package org.biosemantics.landmark.config;

import java.io.IOException;

import javax.inject.Inject;
import org.biosemantics.landmark.triplestore.StoreManager;
import org.biosemantics.landmark.triplestore.StoreManagerImpl;
import org.openrdf.repository.RepositoryException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@PropertySource("classpath:app.properties")
@ComponentScan(basePackages = "org.biosemantics.landmark")
@EnableWebMvc
public class MvcConfiguration extends WebMvcConfigurerAdapter {

	@Inject
	private Environment env;

	@Bean
	public ViewResolver getViewResolver() {
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setPrefix("/WEB-INF/views/");
		resolver.setSuffix(".jsp");
		return resolver;
	}

	@Bean(destroyMethod = "close", name = "nativeStore")
	public StoreManager getNativeStore() throws RepositoryException,
			IOException {
		String storeLocation = env.getProperty("store.location");
		StoreManager mngr = new StoreManagerImpl(storeLocation);
		mngr.setBaseUri(getBaseURI());
		
		return mngr;
	}

	// @Bean (destroyMethod="close", name="autoCompleteStore")
	// public StoreManagerImpl getAutoCompleteStoreManager() {
	// return new
	// StoreManagerImpl(env.getProperty("autocompletestore.location"));
	// }

	@Bean(name = "baseURI")
	public String getBaseURI() {
		return env.getProperty("base.URI");
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
                registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
	}

}
