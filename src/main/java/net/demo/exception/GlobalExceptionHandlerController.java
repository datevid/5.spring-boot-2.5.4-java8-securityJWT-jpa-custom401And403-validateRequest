package net.demo.exception;

import java.io.IOException;

import net.demo.entity.ResponseApi;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandlerController {

    //401 and 403(UNAUTHORIZED and FORBIDDEN)
    @ExceptionHandler(ExceptionAuthSecurity.class)
    public ResponseEntity handleExceptionAuthSecurity(ExceptionAuthSecurity e) throws IOException {
        System.out.println("ExceptionAuthSecurity");
        ResponseApi response = new ResponseApi();
        response.setStatus(false);
        response.setMessage(e.getMessage());
        return ResponseEntity.status(e.getHttpStatus().value()).body(response);
    }


    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity handleAccessDeniedException(AccessDeniedException e) throws IOException {
        System.out.println("AccessDeniedException");
        ResponseApi response = new ResponseApi();
        response.setStatus(false);
        response.setMessage(e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);

    }

    // Manejar la excepción personalizada para error de servidor y no mostrar información de DB o servidor
    @ExceptionHandler(Exception.class)
    public ResponseEntity handleException500(Exception e) {
        System.out.println(e.getMessage());
        e.printStackTrace();
        ResponseApi response = new ResponseApi();
        response.setStatus(false);
        response.setMessage("Error en el servidor.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

}
