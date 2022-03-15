package net.therap.mealsystem.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.therap.mealsystem.dto.AuthenticationRequest;
import net.therap.mealsystem.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author sheikh.ishrak
 * @since 10/03/2022
 */
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Autowired
    JwtUtil jwtUtil;

    private final AuthenticationManager authenticationManager;

    public AuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        try {
            AuthenticationRequest authenticationRequest = new ObjectMapper().readValue(
                    request.getInputStream(), AuthenticationRequest.class);

            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getUsernameOrEmail(), authenticationRequest.getPassword()));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        String token = JwtUtil.generateToken(authResult);

        response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
    }
}
