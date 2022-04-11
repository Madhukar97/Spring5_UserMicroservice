package com.fundoo.utils;

import java.util.HashMap;
import java.util.Map;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Verification;

public class JwtToken {

	private final String SECRET_KEY = "secret";

	public String createToken (String username, String id) {
		Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);

		Map<String, Object> payloadClaims = new HashMap<>();
		payloadClaims.put("username", username);
		payloadClaims.put("id", id);

		String token = JWT.create()
			        .withPayload(payloadClaims)
			        .sign(algorithm);

		return token;
	}

	public String decodeTokenUsername(String token) {
		String id;
		String username;
	           //for verification algorithm
	           Verification verification = null;
		try {
		verification = JWT.require(Algorithm.HMAC256(SECRET_KEY));
		} catch (IllegalArgumentException  e) {
			e.printStackTrace();
		}
		JWTVerifier jwtverifier=verification.build();
		//to decode token
		DecodedJWT decodedjwt=jwtverifier.verify(token);

		Map<String, Claim> claims = decodedjwt.getClaims();
		Claim claim = claims.get("id");
		id=claim.asString();
		Claim claim2 = claims.get("username");
		username = claim2.asString();
		return username;
	}

	public String decodeTokenUserId(String token) {
		String id;
		String username;
	           //for verification algorithm
	           Verification verification = null;
		try {
		verification = JWT.require(Algorithm.HMAC256(SECRET_KEY));
		} catch (IllegalArgumentException  e) {
			e.printStackTrace();
		}
		JWTVerifier jwtverifier=verification.build();
		//to decode token
		DecodedJWT decodedjwt=jwtverifier.verify(token);

		Map<String, Claim> claims = decodedjwt.getClaims();
		Claim claim = claims.get("id");
		id=claim.asString();
		Claim claim2 = claims.get("username");
		username = claim2.asString();
		return id;
	}

}
