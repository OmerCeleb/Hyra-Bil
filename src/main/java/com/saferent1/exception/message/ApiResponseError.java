package com.saferent1.exception.message;

//Vi kunde ha gjort GETTER-SETTERarna på lombok, men vi skrev dem manuellt så att vi kan se arrangemangen i detalj.

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ApiResponseError {

    // MITT SYFTE: Skapa huvudmallen för custom felmeddelanden

    private HttpStatus status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime timestamp;

    private String message;

    private String requestURI;

    //Const;
    private ApiResponseError() {
        timestamp = LocalDateTime.now();

    }

    public ApiResponseError(HttpStatus status) {
        this();
        this.message = "Unexpected Error";
        this.status = status;
    }

    public ApiResponseError(HttpStatus status, String message, String requestURI) {
        this(status);
        this.message = message;
        this.requestURI = requestURI;
    }


    // Getter - Setter
    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    // public void setTimestamp(LocalDateTime timestamp) { ---->Vi vill inte att tiden ska ställa in
    //   this.timestamp = timestamp;
    //}

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRequestURI() {
        return requestURI;
    }

    public void setRequestURI(String requestURI) {
        this.requestURI = requestURI;
    }
}



