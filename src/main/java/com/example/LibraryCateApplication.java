package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication //x dire che è un app con spring..
@EnableSwagger2
public class LibraryCateApplication {

	@Bean //corrisponde a <bean> in xml
	public Docket swaggerSpringMvcConfiguration() {
		return new Docket(DocumentationType.SWAGGER_2)//crea un nuovo docket (interfaccia x spring mvc e swagger) (sto usando swagger2)
				.select()//inizializza e ritorna ..?
				.apis(RequestHandlerSelectors.basePackage(this.getClass().getPackage().getName()))//nome del package
				.paths(PathSelectors.any())
				.build();
	}

	public static void main(String[] args) {
        SpringApplication.run(LibraryCateApplication.class, args);//lancia l app con gli args indicati (se ci sono)
	}
}