package be.ucll.fs.project.ratelimit;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RateLimitInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(RateLimitInterceptor.class);

    private static final RateLimitRule LOGIN_RULE =
            new RateLimitRule("login", 5, 900);


    private static final RateLimitRule SIGNUP_RULE =
            new RateLimitRule("signup", 10, 3600);


    private static final RateLimitRule FORGOT_PASSWORD_RULE =
            new RateLimitRule("forgot-password", 10, 3600);


    private static final RateLimitRule RESET_PASSWORD_RULE =
            new RateLimitRule("reset-password", 5, 900);


    private static final RateLimitRule URL_VALIDATE_RULE =
            new RateLimitRule("url-validate", 20, 60);



    private final ConcurrentHashMap<String, RateLimitBucket> buckets =
            new ConcurrentHashMap<>();


    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws IOException {

        String path   = request.getRequestURI();
        String method = request.getMethod();
        RateLimitRule rule = resolveRule(path, method);

        if (rule == null) {
            return true;
        }

        String clientIp  = extractClientIp(request);
        String bucketKey = rule.name() + ":" + clientIp;

        RateLimitBucket bucket = buckets.computeIfAbsent(
                bucketKey,
                k -> new RateLimitBucket(rule.maxRequests(), rule.windowSeconds())
        );

        if (bucket.tryConsume()) {
            return true;
        }

        long retryAfter = bucket.secondsUntilRefill();
        log.warn("Rate limit exceeded: rule={} ip={} retryAfter={}s",
                rule.name(), clientIp, retryAfter);

        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setHeader("Retry-After", String.valueOf(retryAfter));

        String body = buildErrorJson(retryAfter);
        response.getWriter().write(body);
        response.getWriter().flush();
        return false;
    }

    private RateLimitRule resolveRule(String path, String method) {
        if (!"POST".equalsIgnoreCase(method)) {
            return null;
        }
        return switch (path) {
            case "/users/login"            -> LOGIN_RULE;
            case "/users/signup"           -> SIGNUP_RULE;
            case "/users/forgot-password"  -> FORGOT_PASSWORD_RULE;
            case "/users/reset-password"   -> RESET_PASSWORD_RULE;
            case "/security/url-validate"  -> URL_VALIDATE_RULE;
            default                        -> null;
        };
    }


    private String extractClientIp(HttpServletRequest request) {
        String xff = request.getHeader("X-Forwarded-For");
        if (xff != null && !xff.isBlank()) {
            return xff.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }


    private String buildErrorJson(long retryAfterSeconds) {
        return String.format(
                "{\"status\":\"rate_limit_exceeded\"," +
                "\"message\":\"Too many requests. Please try again in %d seconds.\"," +
                "\"retryAfterSeconds\":%d}",
                retryAfterSeconds, retryAfterSeconds);
    }
}
