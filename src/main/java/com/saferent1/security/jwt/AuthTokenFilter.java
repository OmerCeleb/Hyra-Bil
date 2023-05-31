package com.saferent1.security.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired // Jag behöver data om användaren, men eftersom vi är i säkerhetsskiktet
    private UserDetailsService userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException,
            IOException {


        // Vi kommer att få JwtToken i begäran. Jag undrar om denna jwt-token är min JWTtoken, det är därför vi måste skaffa jwttoken först
        //jwt-token kommer inbäddad i begäran

        String jwtToken = parseJwt(request);

        try {
            if (jwtToken != null && jwtUtils.validateJwtToken(jwtToken)) {
                String email = jwtUtils.getEmailFromToken(jwtToken);
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                //!!! Vi skickar den validerade användarinformationen till SecurityContext.
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails,
                                null,
                                userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        } catch (Exception e) {
            logger.error("Användare hittades inte{} ", e.getMessage());
        }
        filterChain.doFilter(request, response);

    }


    //Denna metod kommer bara att användas i den här klassen för att få jwt-token, så jag gjorde den privat,
    // returvärdet är jwt-token, så det är en sträng
    private String parseJwt(HttpServletRequest request) {
        String header = request.getHeader("Autherization");
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }


    // Här kommer jag att specificera den del som jag inte vill filtrera.
    // Jag kommer att skriva den del som jag inte vill ha med i säkerheten
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        return antPathMatcher.match("/register",request.getServletPath()) ||
                antPathMatcher.match("/login", request.getServletPath());
    }
}
