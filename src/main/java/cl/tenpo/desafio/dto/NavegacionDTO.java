package cl.tenpo.desafio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NavegacionDTO {
    String usuario;
    String method;
    String url;
    Integer status;
    String response;
}
