package com.project.backend.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.backend.dbAccess.User;
import com.project.backend.dbAccess.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.FilterChain;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class DbRestController<authorizationHeader> {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @RequestMapping(value = "/createUser", produces = "application/json", method = RequestMethod.POST)
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User u1 = userRepository.findByUserName(user.getUserName());
        if (u1 == null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            return new ResponseEntity("{\"response\": \"User Created Successfully!\"}", HttpStatus.CREATED);
        }
        return new ResponseEntity("{\"response\": \"User Already Exists!\"}", HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @RequestMapping(value = "/getUser", produces = "application/json", method = RequestMethod.GET)
    public ResponseEntity<User> getUser(HttpServletRequest request, HttpServletResponse response) {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        String token = authorizationHeader.substring("Bearer ".length());
        Algorithm algorithm = Algorithm.HMAC256("qwerty".getBytes());
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = jwtVerifier.verify(token);
        String user = decodedJWT.getClaim("sub").asString();
        User u1 = userRepository.findByUserName(user);
        Map<String, String> res = new HashMap();
        res.put("user", u1.getUserName());
        res.put("accBalance", Integer.toString(u1.getAccBalance()));
        return new ResponseEntity(res, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/credit", produces = "application/json", method = RequestMethod.POST)
    public void credit(HttpServletRequest request, HttpServletResponse response, @RequestBody User u) {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        String token = authorizationHeader.substring("Bearer ".length());
        Algorithm algorithm = Algorithm.HMAC256("qwerty".getBytes());
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = jwtVerifier.verify(token);
        String user = decodedJWT.getClaim("sub").asString();
        User u1 = userRepository.findByUserName(user);
        u1.setAccBalance(u.getAccBalance() + u1.getAccBalance());
        userRepository.save(u1);
    }

    @RequestMapping(value = "/debit", produces = "application/json", method = RequestMethod.POST)
    public void debit(HttpServletRequest request, HttpServletResponse response, @RequestBody User u) {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        String token = authorizationHeader.substring("Bearer ".length());
        Algorithm algorithm = Algorithm.HMAC256("qwerty".getBytes());
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = jwtVerifier.verify(token);
        String user = decodedJWT.getClaim("sub").asString();
        User u1 = userRepository.findByUserName(user);
        u1.setAccBalance(u1.getAccBalance() - u.getAccBalance());
        userRepository.save(u1);
    }

    @RequestMapping(value = "/getRole", produces = "application/json", method = RequestMethod.GET)
    public ResponseEntity<User> getRole(HttpServletRequest request, HttpServletResponse response) {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        String token = authorizationHeader.substring("Bearer ".length());
        Algorithm algorithm = Algorithm.HMAC256("qwerty".getBytes());
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = jwtVerifier.verify(token);
        String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
        Map<String, String> res = new HashMap();
        res.put("role", roles[0]);
        return new ResponseEntity(res, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/refreshToken", produces = "application/json", method = RequestMethod.GET)
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refreshToken = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("qwerty".getBytes());
                JWTVerifier jwtVerifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = jwtVerifier.verify(refreshToken);
                String userName = decodedJWT.getSubject();
                User user = userRepository.findByUserName(userName);
                List<String> roles = new ArrayList<>();
                roles.add(user.getRole());
                String accessToken = JWT.create()
                        .withSubject(user.getUserName())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", roles)
                        .sign(algorithm);
                //response.setHeader("accessToken", accessToken);
                //response.setHeader("refreshToken", refreshToken);
                Map<String, String> tokens = new HashMap<>();
                tokens.put("accessToken", accessToken);
                tokens.put("refreshToken", refreshToken);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            } catch (Exception exception) {
                response.setHeader("error", exception.getMessage());
                response.setStatus(FORBIDDEN.value());
                Map<String, String> tokens = new HashMap<>();
                tokens.put("error", exception.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            }
        } else {
            throw new RuntimeException("Refresh Token is Missing!");
        }
    }

}
