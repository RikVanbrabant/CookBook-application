package be.ucll.fs.project.service;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.stereotype.Service;

@Service
public class InputSecurityService {
    private static final int MAX_TEXT_LENGTH = 120;

    public String cleanPlainText(String value, String fieldName) {
        if (value == null) {
            return null;
        }

        String cleaned = Jsoup.clean(value.trim(), Safelist.none());
        cleaned = cleaned.replaceAll("[\\p{Cntrl}&&[^\\r\\n\\t]]", "");

        if (cleaned.isBlank()) {
            throw new IllegalArgumentException(fieldName + " may not be empty after sanitizing.");
        }
        if (cleaned.length() > MAX_TEXT_LENGTH) {
            throw new IllegalArgumentException(fieldName + " is too long.");
        }
        return cleaned;
    }
}