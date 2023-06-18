package com.example.backendsupportapp.constant;

public class SecurityConstant {

    public static final long EXPIRATION_TIME = 86400;//1 day expressed in milliseconds
    public static final String TOKEN_PREFIX = "Bearer ";//means when ever you receive a token starting with "Bearer " you accept it
    public static final String JWT_TOKEN_HEADER  = "Jwt-Token";
    public static final String TOKEN_CANNOT_BE_VERIFIED  = "Token cannot be verified";
    public static final String ALPHA_CENTAURI_LLC  = "Alpha CENTAURI LLC";
    public static final String  ALPHA_CENTAURI_ADMINISTRATION  = "User Management";
    public static final String  AUTHORITIES = "authorities";

    public static final String FORBIDDEN_MESSAGE = "YOU need to log to access this page";

    public static final String ACCESS_DENIED_MESSAGE = "You do not have permission to access this page";
    public static final String OPTIONS_HTTP_METHOD = "OPTIONS";
    //public static final String[] PUBLIC_URLS = {"/user/login", "/user/register", "/user/resetpassword/**", "/user/image/**"};
    public static final String[] PUBLIC_URLS = {"**"};




}
