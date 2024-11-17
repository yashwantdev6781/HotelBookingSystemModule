package com.ums.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ums.entity.AppUser;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JWTService {

    @Value("${jwt.algorithm.key}")
    private String algorithmkey;
    @Value("${jwt.issuer}")
    private String issuer;
    @Value("${jwt.expiryDuration}")
    private int expirytime;
    private Algorithm algorithm;
    private final static String USER_NAME="name";

    @PostConstruct
    public void postconstruct(){
        algorithm = Algorithm.HMAC256(algorithmkey);
    }
    public String generateToken(AppUser user){
        return JWT.create().withClaim(USER_NAME,user.getName())
                .withExpiresAt(new Date(System.currentTimeMillis()+expirytime))
                .withIssuer(issuer)
                .sign(algorithm);
    }
    public String getUserName(String token){
        DecodedJWT decodedJwt = JWT.require(algorithm).withIssuer(issuer).build().verify(token);
        return  decodedJwt.getClaim(USER_NAME).asString();
    }


}
