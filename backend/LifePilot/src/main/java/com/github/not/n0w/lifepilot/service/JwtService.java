package com.github.not.n0w.lifepilot.service;

import org.springframework.security.core.userdetails.UserDetails;


public interface JwtService {
    public String generateToken(UserDetails userDetails, Long userId);
    public String extractUsername(String token);
    public boolean isTokenValid(String token, UserDetails userDetails);
    public Long extractUserId(String token);
    public boolean isTokenValid(String token);
    public String generateRefreshToken(UserDetails userDetails, Long userId);
}
