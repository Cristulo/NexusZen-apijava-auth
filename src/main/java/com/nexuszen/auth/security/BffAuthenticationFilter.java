package com.nexuszen.auth.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class BffAuthenticationFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    String userId = request.getHeader("X-User-Id");
    System.out.println("BFF FILTER EXECUTING FOR: " + request.getRequestURI());
    System.out.println("X-User-Id HEADER: " + userId);
    
    if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      System.out.println("SETTING SECURITY CONTEXT FOR: " + userId);
      UsernamePasswordAuthenticationToken authToken =
          new UsernamePasswordAuthenticationToken(userId, null, Collections.emptyList());
      
      authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
      SecurityContextHolder.getContext().setAuthentication(authToken);
    }
    
    filterChain.doFilter(request, response);
  }
}
