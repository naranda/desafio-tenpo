package cl.tenpo.desafio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginacionHistorialDTO {

    private long totalPagina;
    private long pagina;
    private long totalElementos;
    private List<HistorialDTO> historial;
}
