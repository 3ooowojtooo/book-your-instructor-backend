package com.quary.bookyourinstructor.configuration.security.filter;

import bookyourinstructor.usecase.authentication.jwt.JwtClaimExtractor;
import bookyourinstructor.usecase.authentication.jwt.JwtValidator;
import com.quary.bookyourinstructor.model.authentication.exception.ExpiredJwtException;
import com.quary.bookyourinstructor.model.authentication.exception.InvalidJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static java.util.Objects.isNull;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_TOKEN_PREFIX = "Bearer ";

    private final JwtValidator jwtValidator;
    private final JwtClaimExtractor jwtClaimExtractor;
    private final UserDetailsService userDetailsService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        final String authorizationHeader = getAuthorizationHeader(request);
        return isNull(authorizationHeader) || !authorizationHeader.startsWith(BEARER_TOKEN_PREFIX);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String authorizationHeader = getAuthorizationHeader(request);
        final String jwtToken = extractJwtFromAuthorizationHeader(authorizationHeader);
        final Optional<String> subjectOptional = extractSubject(jwtToken);
        if (subjectOptional.isPresent()) {
            final String subject = subjectOptional.get();
            if (jwtValidator.isTokenValid(jwtToken, subject) && securityContextHasNoAuthentication()) {
                final UserDetails userDetails = loadUserDetails(subject);
                setAuthenticationInSecurityContext(userDetails, request);
                filterChain.doFilter(request, response);
            }
        } else {
            response.setStatus(401);
        }
    }

    private Optional<String> extractSubject(String jwtToken) {
        try {
            return Optional.of(jwtClaimExtractor.extractSubject(jwtToken));
        } catch (InvalidJwtException | ExpiredJwtException ex) {
            log.warn("Invalid JWT token passed in Bearer authorization header", ex);
            return Optional.empty();
        }
    }

    private UserDetails loadUserDetails(final String subject) {
        return userDetailsService.loadUserByUsername(subject);
    }

    private void setAuthenticationInSecurityContext(final UserDetails userDetails, final HttpServletRequest request) {
        final UsernamePasswordAuthenticationToken authentication = createAuthentication(userDetails);
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private static String getAuthorizationHeader(final HttpServletRequest request) {
        return request.getHeader(AUTHORIZATION_HEADER);
    }

    private static UsernamePasswordAuthenticationToken createAuthentication(final UserDetails userDetails) {
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    private static String extractJwtFromAuthorizationHeader(final String authorizationHeader) {
        return authorizationHeader.substring(BEARER_TOKEN_PREFIX.length());
    }

    private static boolean securityContextHasNoAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication() == null;
    }
}
