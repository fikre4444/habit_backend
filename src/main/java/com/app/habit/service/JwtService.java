package com.app.habit.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.app.habit.domain.User;
import com.app.habit.repository.UserRepository;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtService {

  @Autowired
  private UserRepository userRepository;

  @Value("${security.secret.key}")
  private String secretKey;

  public JwtService() {
    // this creates a dynamically generated secret key which then changes during
    // restart of server.
    // try {
    // KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
    // SecretKey sk = keyGen.generateKey();
    // secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
    // } catch (NoSuchAlgorithmException e) {
    // e.printStackTrace();
    // }
  }

  public String generateToken(String username) {
    final long ONE_DAY = 1000 * 60 * 60 * 24;
    final long ONE_MINUTE = 1000 * 60;
    Map<String, Object> claims = new HashMap<>();

    // this is to add the roles of the person
    Optional<User> user = userRepository.findByUsername(username);
    if (user.isPresent()) {
      claims.put("roles", user.get().getAuthorities().stream()
          .map(grantedAuthority -> grantedAuthority.getAuthority()).collect(Collectors.toList()));
    }

    return Jwts.builder()
        .claims()
        .add(claims)
        .subject(username)
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + (ONE_DAY)))
        .and()
        .signWith(getKey())
        .compact();
  }

  // might have to refactor later
  public String generateTokenForPasswordReset(String username) {
    final int FIFTEEN_MINUTES = 1000 * 60 * 15;
    Map<String, Object> claims = new HashMap<>();
    Optional<User> user = userRepository.findByUsername(username);
    if (!user.isPresent()) {
      return null;
    }
    claims.put("roles", user.get().getAuthorities().stream()
        .map(grantedAuthority -> grantedAuthority.getAuthority()).collect(Collectors.toList()));
    return Jwts.builder()
        .claims()
        .add(claims)
        .subject(username)
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + (FIFTEEN_MINUTES)))
        .and()
        .signWith(getKey())
        .compact();
  }

  public String generateTokenForCompanyFiller() {
    String username = "company_filler";
    final long ONE_DAY = 1000 * 60 * 60 * 24;
    Map<String, Object> claims = new HashMap<>();
    Optional<User> user = userRepository.findByUsername(username);
    if (!user.isPresent()) {
      return null;
    }
    claims.put("roles", user.get().getAuthorities().stream()
        .map(grantedAuthority -> grantedAuthority.getAuthority()).collect(Collectors.toList()));
    return Jwts.builder()
        .claims()
        .add(claims)
        .subject(username)
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + (ONE_DAY)))
        .and()
        .signWith(getKey())
        .compact();
  }

  private SecretKey getKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  public String extractUserName(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
    final Claims claims = extractAllClaims(token);
    return claimResolver.apply(claims);
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parser()
        .verifyWith(getKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  public boolean validateToken(String token, UserDetails userDetails) {
    final String userName = extractUserName(token);
    return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
  }

  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  private Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

}
