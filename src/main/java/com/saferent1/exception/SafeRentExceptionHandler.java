package com.saferent1.exception;

import com.saferent1.exception.message.ApiResponseError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.naming.AuthenticationException;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice // för att hantera det centrala undantaget
public class SafeRentExceptionHandler extends ResponseEntityExceptionHandler {

    // MITT SYFTE: Jag utökade det för att skapa ett anpassat undantagssystem,
    // för att ovirride de undantag som kan komma och för att ge svaret i den struktur jag vill ha.


    Logger logger = LoggerFactory.getLogger(SafeRentExceptionHandler.class);


    private ResponseEntity<Object> buildResponseEntity(ApiResponseError error) {
        logger.error(error.getMessage());
        return new ResponseEntity<>(error, error.getStatus());
    }

    @ExceptionHandler(ConflictException.class)
    protected ResponseEntity<Object> handleConflictException(
            ConflictException ex, WebRequest request) {

        ApiResponseError error = new ApiResponseError(HttpStatus.CONFLICT,
                ex.getMessage(), request.getDescription(false));
        return buildResponseEntity(error);
    }


    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<Object> handleAccesDeniedException(
            AccessDeniedException ex, WebRequest request) {

        ApiResponseError error = new ApiResponseError(HttpStatus.FORBIDDEN,
                ex.getMessage(),
                request.getDescription(false));
        return buildResponseEntity(error);
    }



    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<Object> handleAuthenticationException(
            AuthenticationException ex, WebRequest request) {

        ApiResponseError error = new ApiResponseError(HttpStatus.BAD_REQUEST,
                ex.getMessage(), request.getDescription(false));
        return buildResponseEntity(error);
    }



    @ExceptionHandler(ResourceNotFoundException.class)
    protected ResponseEntity<Object> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {

        ApiResponseError error = new ApiResponseError(HttpStatus.NOT_FOUND,
                ex.getMessage(),
                request.getDescription(false));
        return buildResponseEntity(error);
    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        List<String> errors = ex.getBindingResult().getFieldErrors().
                stream().
                map(e -> e.getDefaultMessage()).
                collect(Collectors.toList());

        ApiResponseError error = new ApiResponseError(HttpStatus.BAD_REQUEST,
                errors.get(0).toString(),
                request.getDescription(false));


        return buildResponseEntity(error);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        ApiResponseError error = new ApiResponseError(HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                request.getDescription(false));


        return buildResponseEntity(error);
    }

    @Override
    protected ResponseEntity<Object> handleConversionNotSupported(ConversionNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        ApiResponseError error = new ApiResponseError(HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getMessage(),
                request.getDescription(false));


        return buildResponseEntity(error);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApiResponseError error = new ApiResponseError(HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                request.getDescription(false));


        return buildResponseEntity(error);
    }


    // Vi kommer att ha följande binära generiska undantagsfångare. Om vi skrev upp dem, om vi tog ner resten, skrev vi ner dem;
    // Metoderna skulle inte ha någon betydelse eftersom överordnade klasser skulle fånga alla undantag och de skulle ogiltigförklaras. Så dessa två borde vara längst ner.
    // Ordningen mellan de två bör vara följande: Körtid högst upp under undantag. Så först fadern och sedan farfar.

    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<Object> hadnleRuntimeException(RuntimeException ex,
                                                            WebRequest request) {
        ApiResponseError error = new ApiResponseError(HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getMessage(),
                request.getDescription(false));
        return buildResponseEntity(error);
    }


    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleGeneralException(Exception ex, WebRequest request) {
        ApiResponseError error = new ApiResponseError(HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getMessage(), request.getDescription(false));
        return buildResponseEntity(error);
    }


}
