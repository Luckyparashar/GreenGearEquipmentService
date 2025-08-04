package com.greengear.equipment.Security;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.greengear.equipment.Dto.CustomUserPrincipal;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Component //to declare spring bean

public class JwtUtils {
//inject the props in JWT Utils class for creating n validation of JWT
	/*
	 * @Value => injection of a value (<constr-arg name n value : xml tags) arg - Spring
	 * expression Lang - SpEL
	 * // example of value injected as dependency , using SpEL
	 * (Spring Expression Language)
	 */
	@Value("${jwt.secret.key}") 	
	private String jwtSecret;

	@Value("${jwt.expiration.time}")
	private int jwtExpirationMs;

	private SecretKey key;//=> represents symmetric key

	@PostConstruct
	public void init() {
		
		/*create secret key instance from  Keys class
		 * Keys - builder of Secret key
		 * Create a Secret Key using HMAC-SHA256 encryption algo.
		 */		
		key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
	}

	
	// this method will be invoked by our custom JWT filter
	public CustomUserPrincipal getUserNameFromJwtToken(Claims claims) {
		Long userId = claims.get("userId", Long.class);
		String username = claims.getSubject();
		return new CustomUserPrincipal(username, userId);
	}

	// this method will be invoked by our custom JWT filter
	public Claims validateJwtToken(String jwtToken) {
		// try {
		Claims claims = Jwts.parser()

				.verifyWith(key) // sets the SAME secret key for JWT signature verification
				.build()

				// rets the JWT parser set with the Key
				.parseSignedClaims(jwtToken) // rets JWT with Claims added in the body
				.getPayload();// => JWT valid , rets the Claims(payload)
		/*
		 * parseClaimsJws - throws:UnsupportedJwtException -if the JWT body | payload
		 * does not represent any Claims JWSMalformedJwtException - if the JWT body |
		 * payload is not a valid JWSSignatureException - if the JWT signature
		 * validation fails ExpiredJwtException - if the specified JWT is expired
		 * IllegalArgumentException - if the JWT claims body | payload is null or empty
		 * or only whitespace
		 */
		return claims;
	}

	private List<String> getAuthoritiesInString(Collection<? extends GrantedAuthority> authorities) {
		return authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
	}

	// this method will be invoked by our custom JWT filter to get list of granted
	// authorities n store it in auth token
	public List<GrantedAuthority> getAuthoritiesFromClaims(Claims claims) {

		List<String> authorityNamesFromJwt = 
				(List<String>) claims.get("authorities");
		List<GrantedAuthority> authorities = 
				authorityNamesFromJwt.stream()
				.map(role -> new SimpleGrantedAuthority("ROLE_" + role))
				.collect(Collectors.toList());		

		authorities.forEach(System.out::println);
		return authorities;
	}

	

	public Authentication populateAuthenticationTokenFromJWT(String jwt) {
		// validate JWT n retrieve JWT body (claims)
		Claims payloadClaims = validateJwtToken(jwt);
		// get user name from the claims
		CustomUserPrincipal details = getUserNameFromJwtToken(payloadClaims);
		// get granted authorities as a custom claim
		List<GrantedAuthority> authorities = getAuthoritiesFromClaims(payloadClaims);
			// add user name/email , null:password granted authorities in Authentication object
		UsernamePasswordAuthenticationToken token = 
				new UsernamePasswordAuthenticationToken(details, null, authorities);
//		UsernamePasswordAuthenticationToken token = 
//				new UsernamePasswordAuthenticationToken(new User(email,"", authorities), null, authorities);
//	
		System.out.println("is authenticated " + token.isAuthenticated());// true
		return token;

	}

}
