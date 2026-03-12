package com.soap;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;

// RBAC: role checking
@WebService
public interface AuthService {

    @WebMethod
    String registerUser(String username, String password);

    @WebMethod
    String loginUser(String username, String password);

    @WebMethod
    boolean validateToken(String token);

    @WebMethod
    String getUserRole(String token);

    @WebMethod
    String registerAdmin(String username, String password);
}