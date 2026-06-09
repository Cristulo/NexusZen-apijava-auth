package com.nexuszen.auth.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
    String activeRole = request.getHeader("X-User-Active-Role");
    String rolesHeader = request.getHeader("X-User-Roles");

    System.out.println("BFF FILTER EXECUTING FOR: " + request.getRequestURI());
    System.out.println("X-User-Id HEADER: " + userId);
    System.out.println("X-User-Active-Role HEADER: " + activeRole);

    if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      List<SimpleGrantedAuthority> authorities = new ArrayList<>();

      if (activeRole != null && !activeRole.trim().isEmpty()) {
        authorities.add(new SimpleGrantedAuthority(activeRole.trim()));
      } else if (rolesHeader != null && !rolesHeader.trim().isEmpty()) {
        Arrays.stream(rolesHeader.split(","))
            .map(String::trim)
            .filter(r -> !r.isEmpty())
            .map(SimpleGrantedAuthority::new)
            .forEach(authorities::add);
      } else {
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
      }

      System.out.println("SETTING SECURITY CONTEXT FOR: " + userId
          + " WITH AUTHORITIES: " + authorities);

      UsernamePasswordAuthenticationToken authToken =
          new UsernamePasswordAuthenticationToken(userId, null, authorities);

      authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
      SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    filterChain.doFilter(request, response);
  }
}
