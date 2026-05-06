package be.ucll.fs.project.service;

import org.springframework.stereotype.Service;

import java.net.IDN;
import java.net.InetAddress;
import java.net.URI;
import java.util.List;
import java.util.Locale;

@Service
public class UrlValidationService {
    private static final List<String> ALLOWED_SCHEMES = List.of("http", "https");

    public ValidationResult validate(String rawUrl) {
        if (rawUrl == null || rawUrl.isBlank()) {
            return ValidationResult.reject("URL is required.");
        }

        final URI uri;
        try {
            uri = URI.create(rawUrl.trim());
        } catch (IllegalArgumentException ex) {
            return ValidationResult.reject("URL is not syntactically valid.");
        }

        String scheme = uri.getScheme();
        if (scheme == null || !ALLOWED_SCHEMES.contains(scheme.toLowerCase(Locale.ROOT))) {
            return ValidationResult.reject("Only http and https URLs are allowed.");
        }

        if (uri.getUserInfo() != null) {
            return ValidationResult.reject("Credentials in URLs are not allowed.");
        }

        String host = uri.getHost();
        if (host == null || host.isBlank()) {
            return ValidationResult.reject("URL must contain a hostname.");
        }

        String asciiHost = IDN.toASCII(host).toLowerCase(Locale.ROOT);
        if (asciiHost.equals("localhost") || asciiHost.endsWith(".localhost")) {
            return ValidationResult.reject("Localhost targets are not allowed.");
        }

        int port = uri.getPort();
        if (port != -1 && port != 80 && port != 443) {
            return ValidationResult.reject("Only ports 80 and 443 are allowed.");
        }

        try {
            InetAddress[] addresses = InetAddress.getAllByName(asciiHost);
            for (InetAddress address : addresses) {
                if (isBlockedAddress(address)) {
                    return ValidationResult.reject("URL resolves to an internal or unsafe IP address.");
                }
            }
        } catch (Exception ex) {
            return ValidationResult.reject("Hostname could not be resolved safely.");
        }

        return ValidationResult.allow("URL passed SSRF validation.");
    }

    private boolean isBlockedAddress(InetAddress address) {
        return address.isAnyLocalAddress()
                || address.isLoopbackAddress()
                || address.isLinkLocalAddress()
                || address.isSiteLocalAddress()
                || address.isMulticastAddress();
    }

    public record ValidationResult(boolean allowed, String reason) {
        static ValidationResult allow(String reason) { return new ValidationResult(true, reason); }
        static ValidationResult reject(String reason) { return new ValidationResult(false, reason); }
    }
}
