package be.ucll.fs.project.controller;

import be.ucll.fs.project.exception.CoursesException;
import be.ucll.fs.project.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionHandlers {

    private static final Logger appLogger = LoggerFactory.getLogger(ExceptionHandlers.class);
    private static final Logger securityLogger = LoggerFactory.getLogger("SECURITY_AUDIT");

    @ExceptionHandler(CoursesException.class)
    public ProblemDetail handleCoursesException(CoursesException ex) {
        appLogger.info("Domeinfout opgetreden: {}", ex.getMessage());
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problemDetail.setType(URI.create("https://courses-app.ucll.be/errors/domain-error"));
        problemDetail.setTitle("Domein Fout");
        return problemDetail;
    }

    @ExceptionHandler(NotFoundException.class)
    public ProblemDetail handleNotFoundException(NotFoundException ex) {
        appLogger.info("Bron niet gevonden: {}", ex.getMessage());
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setType(URI.create("https://courses-app.ucll.be/errors/not-found"));
        problemDetail.setTitle("Bron niet gevonden");
        return problemDetail;
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ProblemDetail handleBadCredentialsException(BadCredentialsException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, "Ongeldige gebruikersnaam of wachtwoord.");
        problemDetail.setType(URI.create("https://courses-app.ucll.be/errors/unauthorized"));
        problemDetail.setTitle("Authenticatie mislukt");
        return problemDetail;
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail handleAccessDeniedException(AccessDeniedException ex) {
        // Schrijf de overtreding weg in de security log
        securityLogger.warn("event=access_denied, message=Toegang geweigerd voor een resource. Gebruiker heeft onvoldoende rechten.");

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, "Je hebt geen rechten om deze actie uit te voeren.");
        problemDetail.setType(URI.create("https://courses-app.ucll.be/errors/forbidden"));
        problemDetail.setTitle("Toegang geweigerd");
        return problemDetail;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationException(MethodArgumentNotValidException ex) {
        String errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        appLogger.warn("Validatiefout in request: {}", errors);

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, errors);
        problemDetail.setType(URI.create("https://courses-app.ucll.be/errors/validation-error"));
        problemDetail.setTitle("Validatie mislukt");
        return problemDetail;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneralException(Exception ex) {
        appLogger.error("Onverwachte interne server fout opgetreden: ", ex);

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, 
            "Er is een onverwachte fout opgetreden. Probeer het later opnieuw.");
        problemDetail.setType(URI.create("https://courses-app.ucll.be/errors/internal-server-error"));
        problemDetail.setTitle("Interne server fout");
        return problemDetail;
    }
}