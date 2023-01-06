package com.seris.bassein.service.security.config;

import com.seris.bassein.service.security.service.JwtUserDetailsService;
import com.seris.bassein.util.Constants;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.log4j.Log4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@Component
@Log4j
public class JwtRequestFilter extends OncePerRequestFilter {

    @Resource
    private JwtUserDetailsService jwtUserDetailsService;
    @Resource
    private JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String requestTokenHeader = request.getHeader("Authorization");
        Cookie cookie = request.getCookies() == null ? null : Arrays.stream(request.getCookies()).filter(f -> f.getName().equals("token")).findFirst().orElse(null);

        String username = null;
        String jwtToken = null;
        if (cookie != null) {
            jwtToken = cookie.getValue();
            try {
                username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                System.out.println("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                System.out.println("JWT Token has expired");
            }
        } else {
            if (Arrays.stream(Constants.httpSecurityUrlAntMatchers).noneMatch(fx ->
                    request.getRequestURI().replaceAll(" http://localhost:3000", "").startsWith(fx.replaceAll("\\**", ""))))
                logger.warn("JWT Token not started Bearer string " + request.getRequestURI() + " " + requestTokenHeader);
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);

            if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        chain.doFilter(request, response);
    }
}
