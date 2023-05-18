package com.saferent1.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice // för att hantera det centrala undantaget
public class SafeRentExceptionHandler extends ResponseEntityExceptionHandler {

    // MITT SYFTE: Jag utökade det för att skapa ett anpassat undantagssystem,
    // för att ovirride de undantag som kan komma och för att ge svaret i den struktur jag vill ha.


}
