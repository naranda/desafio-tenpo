package cl.tenpo.desafio.ex;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class LoginException extends RuntimeException {

    public LoginException() {
        super(String.format("Usuario o Clave incorrecto"));
    }
}
