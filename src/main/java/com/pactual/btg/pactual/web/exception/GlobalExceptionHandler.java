package com.pactual.btg.pactual.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
class GlobalExceptionHandler {

    static class NotFoundException extends RuntimeException { public NotFoundException(String m){ super(m);} }
    static class InsufficientBalanceException extends RuntimeException { public InsufficientBalanceException(String m){ super(m);} }
    static class MinimumAmountException extends RuntimeException { public MinimumAmountException(String m){ super(m);} }
    static class InvalidStateException extends RuntimeException { public InvalidStateException(String m){ super(m);} }


    @ExceptionHandler(NotFoundException.class)
    ProblemDetail handleNotFound(NotFoundException ex){
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        pd.setTitle("Recurso no encontrado");
        pd.setDetail(ex.getMessage());
        return pd;
    }


    @ExceptionHandler(InsufficientBalanceException.class)
    ProblemDetail handleInsufficient(InsufficientBalanceException ex){
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        pd.setTitle("Saldo insuficiente");
        pd.setDetail(ex.getMessage());
        return pd;
    }


    @ExceptionHandler(MinimumAmountException.class)
    ProblemDetail handleMinimum(MinimumAmountException ex){
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setTitle("Monto inferior al mínimo del fondo");
        pd.setDetail(ex.getMessage());
        return pd;
    }


    @ExceptionHandler(InvalidStateException.class)
    ProblemDetail handleInvalidState(InvalidStateException ex){
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);
        pd.setTitle("Estado inválido");
        pd.setDetail(ex.getMessage());
        return pd;
    }


    @ExceptionHandler(Exception.class)
    ProblemDetail handleGeneric(Exception ex){
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        pd.setTitle("Error interno");
        pd.setDetail(ex.getMessage());
        return pd;
    }
}
