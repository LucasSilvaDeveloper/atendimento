package br.com.base.project.config;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.format.number.NumberStyleFormatter;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebConfig implements WebMvcConfigurer{

	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addFormatterForFieldType(BigDecimal.class, new NumberStyleFormatter("#,##0.00"));
		registry.addFormatterForFieldType(Integer.class, new NumberStyleFormatter("#,##0"));
	}
	
	@Bean
	FormattingConversionService conversionService() {
		DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();
		
		DateTimeFormatterRegistrar dateTimeFormatterRegistrar = new DateTimeFormatterRegistrar();
		dateTimeFormatterRegistrar.setDateFormatter(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		dateTimeFormatterRegistrar.registerFormatters(conversionService);

		return conversionService;
	}
	
}
