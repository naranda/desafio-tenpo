package cl.tenpo.desafio.ex;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ClaveException extends RuntimeException {

    public ClaveException() {
        super(String.format("No se pudo generar la clave del usuario"));
    }
}
