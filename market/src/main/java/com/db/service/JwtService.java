package com.db.service;

import com.db.exception.JwtServiceException;
import io.jsonwebtoken.Claims;
import javax.servlet.http.HttpServletRequest;

public interface JwtService {
    String createServiceRequestToken();

    Claims validateAccessTokenAndGetClaims(String token) throws JwtServiceException;

    int getUserId(Claims claims);

    String getRole(Claims claims);

    String getAccessTokenFromRequest(HttpServletRequest request) throws JwtServiceException;
}
