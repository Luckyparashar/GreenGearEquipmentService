package com.greengear.equipment;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;

import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication

public class GreanGearEquipment {

	public static void main(String[] args) {
		SpringApplication.run(GreanGearEquipment.class, args);
	}
	
	//for mapping the Entities to Dto and viceversa
	@Bean
	ModelMapper modelMapper() {
		System.out.println("creating model mapper");
		ModelMapper mapper= new ModelMapper();
		//to transfer only properties matching by name 
		mapper.getConfiguration()
		.setMatchingStrategy(MatchingStrategies.STRICT)
		//transfer not null props
		.setPropertyCondition(Conditions.isNotNull());
		return mapper;
	}
	@Bean
	@LoadBalanced
	RestTemplate restTemplate() 
	{
		return new RestTemplate();
	}
	
//	@Bean
//	 CommonsMultipartResolver multipartResolver() {
//	    CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
//	    multipartResolver.setMaxUploadSize(10000000); // 10MB
//	    return multipartResolver;
//	}


}
