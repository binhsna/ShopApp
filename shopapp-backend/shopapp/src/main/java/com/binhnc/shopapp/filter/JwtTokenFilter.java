package com.binhnc.shopapp.filter;

import com.binhnc.shopapp.component.JwtTokenUtils;
import com.binhnc.shopapp.model.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Bộ lọc cho mỗi request
@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
    @Value("${api.prefix}")
    private String apiPrefix;
    private final UserDetailsService userDetailsService;
    private final JwtTokenUtils jwtTokenUtils;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        try {
            // Những request không cần token
            if (isBypassToken(request)) {
                filterChain.doFilter(request, response); // Enable bypass
                return;
            }
            final String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                return;
            }
            final String token = authHeader.substring(7);
            final String phoneNumber = jwtTokenUtils.extractPhoneNumber(token);
            if (phoneNumber != null
                    && SecurityContextHolder.getContext().getAuthentication() == null) {
                User userDetails = (User) userDetailsService.loadUserByUsername(phoneNumber);
                if (jwtTokenUtils.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        }
    }

    private boolean isBypassToken(@NonNull HttpServletRequest request) {
        // Các request được cho phép đi qua
        final List<Pair<String, String>> bypassTokens = Arrays.asList(
                // Health check request, không yêu cầu JWT token
                Pair.of(String.format("%s/health-check/health", apiPrefix), "GET"),
                Pair.of(String.format("%s/actuator/**", apiPrefix), "GET"),

                Pair.of(String.format("%s/roles", apiPrefix), "GET"),
                Pair.of(String.format("%s/products", apiPrefix), "GET"),
                Pair.of(String.format("%s/categories", apiPrefix), "GET"),
                Pair.of(String.format("%s/users/register", apiPrefix), "POST"),
                Pair.of(String.format("%s/users/login", apiPrefix), "POST"),

                // Swagger
                Pair.of("/api-docs", "GET"),
                Pair.of("/api-docs/**", "GET"),
                Pair.of("/swagger-resources", "GET"),
                Pair.of("/swagger-resources/**", "GET"),
                Pair.of("/configuration/ui", "GET"),
                Pair.of("/configuration/security", "GET"),
                Pair.of("/swagger-ui/**", "GET"),
                Pair.of("/swagger-ui.html", "GET"),
                Pair.of("/swagger-ui/index.html", "GET")
        );

        String requestPath = request.getServletPath();
        String requestMethod = request.getMethod();
        if (requestPath.startsWith(String.format("%s/orders", apiPrefix))
                && requestMethod.equals("GET")) {
            if (requestPath.matches(apiPrefix + "/orders/\\d+")) {
                return true;
            }
            if (requestPath.equals(String.format("/%s/orders", apiPrefix))) {
                return true;
            }
        }
        for (Pair<String, String> bypassToken : bypassTokens) {
            String tokenPath = bypassToken.getFirst();
            String tokenMethod = bypassToken.getSecond();
            if (tokenPath.contains("**")) {
                String regexPath = tokenPath.replace("**", ".*");
                Pattern pattern = Pattern.compile(regexPath);
                Matcher matcher = pattern.matcher(requestPath);
                if (matcher.matches() && requestMethod.equals(tokenMethod)) {
                    return true;
                }
            } else if (requestPath.equals(tokenPath) && requestMethod.equals(tokenMethod)) {
                return true;
            }
          /*
           if (request.getServletPath().contains(bypassToken.getFirst()) &&
                    request.getMethod().equals(bypassToken.getSecond())) {
                return true;
            }
          */
        }
        return false;
    }
}
