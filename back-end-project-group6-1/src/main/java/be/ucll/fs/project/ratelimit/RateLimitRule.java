package be.ucll.fs.project.ratelimit;


public record RateLimitRule(String name, int maxRequests, long windowSeconds) {}
