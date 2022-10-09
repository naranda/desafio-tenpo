package cl.tenpo.desafio.ex;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class UsuarioExistException extends RuntimeException {

    public UsuarioExistException() {
        super(String.format("El usuario ya se encuentra registrado"));
    }
}
