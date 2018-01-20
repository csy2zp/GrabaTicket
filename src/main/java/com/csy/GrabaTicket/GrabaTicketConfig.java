package com.csy.GrabaTicket;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class GrabaTicketConfig extends WebMvcConfigurerAdapter {

	@Value("${resources.image.directory:/GrabaTicket/image/}")
	private String imageDir;

	@Value("${resources.image.directory.uri:/download}")
	private String imageDirURI;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler(imageDirURI + "/**").addResourceLocations("file:" + imageDir + "/");
		registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
		super.addResourceHandlers(registry);
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/**");
		super.addInterceptors(registry);
	}
}
