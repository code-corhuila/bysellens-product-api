package com.bysellens.common;
import java.time.Instant;import java.util.List;import javax.crypto.spec.SecretKeySpec;import com.nimbusds.jose.jwk.source.ImmutableSecret;import org.springframework.stereotype.Component;import org.springframework.security.oauth2.jwt.*;import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
@Component
public class Tokens {
 private final JwtEncoder encoder;
 public Tokens(SecretKeySpec key){encoder=new NimbusJwtEncoder(new ImmutableSecret<>(key));}
 public String issue(String email,String role){var now=Instant.now();var claims=JwtClaimsSet.builder().issuer("bysellens").subject(email).issuedAt(now).expiresAt(now.plusSeconds(1800)).claim("rol",List.of(role)).build();return encoder.encode(JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(),claims)).getTokenValue();}
}