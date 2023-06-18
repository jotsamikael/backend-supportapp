package com.example.backendsupportapp.controller;

import com.example.backendsupportapp.Service.UserService;
import com.example.backendsupportapp.constant.SecurityConstant;
import com.example.backendsupportapp.domain.User;
import com.example.backendsupportapp.domain.UserPrincipal;
import com.example.backendsupportapp.exception.ExceptionHandling;
import com.example.backendsupportapp.exception.domain.EmailExistException;
import com.example.backendsupportapp.exception.domain.UserNameExistException;
import com.example.backendsupportapp.exception.domain.UserNotFoundException;
import com.example.backendsupportapp.utility.JWTTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(path={"/","/api/v1/user"})
public class UserController extends ExceptionHandling {

    private AuthenticationManager authenticationManager;

    private UserService userService;
    private JWTTokenProvider jwtTokenProvider;

    @Autowired
    public UserController(AuthenticationManager authenticationManager, UserService userService, JWTTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) throws UserNotFoundException, UserNameExistException, EmailExistException {
        User newUser = userService.register(user.getFirstName(), user.getLastName(), user.getUserName(), user.getEmail());
        return new ResponseEntity<>(newUser, OK);
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User user) {
        System.out.println("touched0");
        authenticate(user.getUserName(), user.getPassword()); //raises an exception if authentication fails
        System.out.println("touched1");
        User loginUser = userService.findUserByUserName(user.getUserName());
        System.out.println("touched2");
        UserPrincipal userPrincipal = new UserPrincipal(loginUser);
        System.out.println("touched3");
        HttpHeaders jwtHeader = getJwtHeader(userPrincipal);
        System.out.println("touched4");
        return new ResponseEntity<>(loginUser, jwtHeader, OK);
    }

    private HttpHeaders getJwtHeader(UserPrincipal user) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(SecurityConstant.JWT_TOKEN_HEADER, jwtTokenProvider.generateJwtToken(user));
        return headers;
    }

    private void authenticate(String userName, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, password));
    }
}
