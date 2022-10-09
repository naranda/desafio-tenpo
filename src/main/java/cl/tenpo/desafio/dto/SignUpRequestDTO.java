package cl.tenpo.desafio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequestDTO {

    @NotBlank(message = "Debe ingresar el email del usuario")
    @NotNull(message = "Debe ingresar el email del usuario")
    private String email;
    @NotBlank(message = "Debe ingresar el nombre del usuario")
    @NotNull(message = "Debe ingresar el nombre del usuario")
    private String nombre;
    @NotBlank(message = "Debe ingresar el apellido del usuario")
    @NotNull(message = "Debe ingresar el apellido del usuario")
    private String apellido;
    @NotBlank(message = "Debe ingresar la clave del usuario")
    @NotNull(message = "Debe ingresar la clave del usuario")
    private String clave;

}
