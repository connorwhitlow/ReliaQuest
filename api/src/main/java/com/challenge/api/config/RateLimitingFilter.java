package com.challenge.api.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RateLimitingFilter implements Filter {

    // Maps to store request counts per IP address
    private final Map<String, AtomicInteger> apiRequestCounts = new ConcurrentHashMap<>();
    private final Map<String, AtomicInteger> authRequestCounts = new ConcurrentHashMap<>();
    
    // Rate limits
    private static final int MAX_API_REQUESTS_PER_MINUTE = 30;  // More lenient for API calls
    private static final int MAX_AUTH_REQUESTS_PER_MINUTE = 5;  // Stricter for auth attempts
    
    // Scheduler for periodic cleanup
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String clientIp = httpRequest.getRemoteAddr();
        String requestPath = httpRequest.getRequestURI();
        
        // Check if this is an authentication request
        boolean isAuthRequest = requestPath.contains("/api/v1/auth/");
        
        // Choose appropriate counter and limit
        Map<String, AtomicInteger> requestCounts = isAuthRequest ? authRequestCounts : apiRequestCounts;
        int maxRequests = isAuthRequest ? MAX_AUTH_REQUESTS_PER_MINUTE : MAX_API_REQUESTS_PER_MINUTE;
        
        // Initialize and increment request count
        requestCounts.putIfAbsent(clientIp, new AtomicInteger(0));
        int requests = requestCounts.get(clientIp).incrementAndGet();

        // Check if limit exceeded
        if (requests > maxRequests) {
            httpResponse.setStatus(429);
            String message = isAuthRequest ? 
                "Too many login attempts. Please try again later." :
                "Too many API requests. Please try again later.";
            httpResponse.getWriter().write(message);
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Schedule periodic cleanup of request counts
        scheduler.scheduleAtFixedRate(this::resetRequestCounts, 1, 1, TimeUnit.MINUTES);
    }

    @Override
    public void destroy() {
        scheduler.shutdown();
    }

    private void resetRequestCounts() {
        apiRequestCounts.clear();
        authRequestCounts.clear();
    }
} 