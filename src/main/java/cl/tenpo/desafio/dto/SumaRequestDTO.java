package cl.tenpo.desafio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SumaRequestDTO {

    @NotNull(message = "Debe ingresar el valor para numero1")
    private Double numero1;

    @NotNull(message = "Debe ingresar el valor para numero2")
    private Double numero2;
}
