package com.sb.controller;

import com.sb.model.JWTRequest;
import com.sb.model.JWTResponse;
import com.sb.service.UserService;
import com.sb.utility.JWTUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
//@RequestMapping(value ="api/jwt")
public class JWTController {

    @Autowired
    private JWTUtility jwtUtility;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    public UserService userService;
    @GetMapping("/")
    public String hello(){
        return "Welcome to Spring boot JWT demo";
    }

    @PostMapping("/authenticate")
    public JWTResponse authenticate(@RequestBody JWTRequest jwtRequest) throws Exception {
        System.out.println("user name.......................>"+jwtRequest.getUserName());
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    jwtRequest.getUserName(), jwtRequest.getPassword()
            ));
        }catch (BadCredentialsException e){
            throw new Exception("BAD Credentials",e);
        }

        final UserDetails userDetails= userService.loadUserByUsername(jwtRequest.getUserName());
        String token= jwtUtility.generateToken(userDetails);
        return new JWTResponse(token);
    }
}
