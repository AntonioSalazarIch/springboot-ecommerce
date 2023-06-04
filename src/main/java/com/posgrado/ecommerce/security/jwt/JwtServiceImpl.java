package com.posgrado.ecommerce.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.posgrado.ecommerce.entity.User;
import com.posgrado.ecommerce.security.UserDetailService;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtServiceImpl implements JwtService {

  @Value("${spring.jwt.secret-key}")
  private String secretKey;
  private UserDetailService userDetailService;

  public JwtServiceImpl(UserDetailService userDetailService) {
    this.userDetailService = userDetailService;
  }

  @Override
  public String createToken(User user) {
    return JWT.create()
        .withSubject(user.getEmail())
        .withClaim("role", user.getRole().getName())
        .withIssuedAt(new Date())
        .withExpiresAt(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(30)))
        .sign(Algorithm.HMAC256(secretKey));
  }

  @Override
  public User decodeToken(String token) {
    JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secretKey)).build();
    DecodedJWT decodedJWT = verifier.verify(token);
    String email = decodedJWT.getSubject();
    return (User) userDetailService.loadUserByUsername(email);
  }
}
