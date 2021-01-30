package co.com.worldoffice.shoppingcart.service.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ComponentException extends Exception {

    private final HttpStatus status;

    public ComponentException(HttpStatus status) {
        this.status = status;
    }

    public ComponentException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public ComponentException(String message, Throwable cause, HttpStatus status) {
        super(message, cause);
        this.status = status;
    }

    public ComponentException(Throwable cause, HttpStatus status) {
        super(cause);
        this.status = status;
    }

    public ComponentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, HttpStatus status) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.status = status;
    }
}
