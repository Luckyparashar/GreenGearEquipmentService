package com.greengear.equipment.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.AllArgsConstructor;

@Configuration // to declare config class - to declare spring beans - @Bean)
@EnableWebSecurity // to customize spring security
@EnableMethodSecurity // to enable method level annotations
//(@PreAuthorize , @PostAuthorize..) to specify  authorization rules
@AllArgsConstructor
public class SecurityConfiguration {
	// depcy - password encoder
	
	private final CustomJwtFilter customJwtFilter;
	private final JwtAuthEntryPoint jwtAuthEntryPoint;
	
	/*
	 * configure spring bean to customize spring security filter chain disable CSRF
	 * protection - session creation policy - stateless - disable form login based
	 * authentication - enable basic authentication scheme , for REST clients
	 */
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		// 1. Disable CSRF protection
		http.csrf(csrf -> csrf.disable());
		// 2. Authenticate any request
		http.authorizeHttpRequests(request ->
		// 3.permit all - swagger , view all restaurants , user signin , sign up....
		request.requestMatchers("/swagger-ui/**", "/v**/api-docs/**" ).permitAll()
		//  /error - public
			.requestMatchers("/error").permitAll()
		//  Front end React In Flight requesta - allo
		.requestMatchers(HttpMethod.OPTIONS).permitAll()
		// restaurants - GET - to get all restaurants - no authentication
				.requestMatchers(HttpMethod.GET, "/ms2/category/{name}").permitAll()
				.requestMatchers(HttpMethod.GET, "/ms2/equipment/{id}/avl").permitAll()
				.requestMatchers(HttpMethod.GET, "/ms2/equipment").permitAll()
				 .requestMatchers("/ms2/equipment/{id}/image").permitAll()
				.requestMatchers(HttpMethod.POST, "/ms2/equipment/{id}").hasRole("OWNER")
				.requestMatchers(HttpMethod.GET, "/ms2/equipment/myequipment").hasRole("OWNER")
				.requestMatchers(HttpMethod.GET, "/ms2/equipment/{id}").hasRole("RENTER")
		.anyRequest().authenticated());
		
		// 5. set session creation policy - stateless
		http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		// 6. add custom JWT filter before -UserNamePasswordAuthFilter
		http.addFilterBefore(customJwtFilter, UsernamePasswordAuthenticationFilter.class);
		// 7. Customize error code of SC 401 | SC 403, in case of authentication failure
		http.exceptionHandling(ex -> 
		ex.authenticationEntryPoint(jwtAuthEntryPoint));
		return http.build();
	}

	// configure a spring to return Spring security authentication manager
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration mgr) throws Exception {
		return mgr.getAuthenticationManager();
	}


}
