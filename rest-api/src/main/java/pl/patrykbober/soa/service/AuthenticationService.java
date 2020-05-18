package pl.patrykbober.soa.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.NoArgsConstructor;
import pl.patrykbober.soa.exception.RestException;

import javax.ejb.Stateless;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Stateless
@NoArgsConstructor
public class AuthenticationService {

    public String login(String login, String password) {
        boolean isValid = authenticate(login, password);

        if (!isValid) {
            throw new RestException("Invalid user or password");
        }

        try {
            return issueToken(login);
        } catch (Exception e) {
            throw new RestException("Exception occurred while authenticate");
        }
    }

    private String issueToken(String login) {
        Algorithm algorithm = Algorithm.HMAC256("qgeZAWWxQn4uLHZbOvAg");

        return JWT.create()
                .withSubject(login)
                .withIssuer("auth0")
                .withIssuedAt(new Date())
                .withExpiresAt(Date.from(LocalDateTime.now().plusMinutes(15).atZone(ZoneId.systemDefault()).toInstant()))
                .sign(algorithm);
    }

    private boolean authenticate(String login, String password) {
        return "testUser".equals(login) && "testPassword".equals(password);
    }

}
