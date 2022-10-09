package cl.tenpo.desafio.service;

import cl.tenpo.desafio.dto.HistorialDTO;
import cl.tenpo.desafio.dto.NavegacionDTO;
import cl.tenpo.desafio.dto.PaginacionHistorialDTO;
import cl.tenpo.desafio.entity.HistorialEntity;
import cl.tenpo.desafio.ex.BadRequestException;
import cl.tenpo.desafio.repository.HistorialRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NavegacionServiceTest {
    @Mock
    private HistorialRepository historialRepository;

    @InjectMocks
    private NavegacionService navegacionService;

    @Test
    void registrarNavegacionOKTest() {

        String usuario = "ignacia@gmail.com";
        String method = "POST";
        String uri = "/login";
        int status = 200;
        String response = "algo";

        HistorialEntity historialEntity = new HistorialEntity();
        historialEntity.setUsuario(usuario);
        historialEntity.setMethod(method);
        historialEntity.setStatus(status);
        historialEntity.setUrl(uri);
        historialEntity.setResultado(response);


        when(historialRepository.save(any(HistorialEntity.class))).thenReturn(historialEntity);

        NavegacionDTO navegacionDTO = new NavegacionDTO();
        navegacionDTO.setUsuario(usuario);
        navegacionDTO.setResponse(response);
        navegacionDTO.setMethod(method);
        navegacionDTO.setUrl(uri);
        navegacionDTO.setStatus(status);

        navegacionService.registrarNavegacion(navegacionDTO);

        Assertions.assertThat(usuario).isEqualTo(historialEntity.getUsuario());
    }

    @Test
    void listarHistorialOKTest() {

        int numPagina = 1;
        int cantidadElementos = 15;

        Pageable pageable = PageRequest.of(numPagina - 1, cantidadElementos, Sort.by("fechaRegistro").descending());

        var historialDTO = new HistorialDTO();
        historialDTO.setId(1l);
        historialDTO.setUrl("/login");
        historialDTO.setFechaRegistro(LocalDateTime.now());
        historialDTO.setMethod("POST");
        historialDTO.setUsuario("ignacia@gmail.com");
        historialDTO.setResultado("123456");
        historialDTO.setStatus(200);

        HistorialEntity historialEntity = new HistorialEntity();
        historialEntity.setHistorialId(1l);
        historialEntity.setUsuario("ignacia@gmail.com");
        historialEntity.setStatus(200);
        historialEntity.setMethod("POST");
        historialEntity.setUrl("/login");
        historialEntity.setFechaRegistro(LocalDateTime.now());

        List<HistorialEntity> lista = Arrays.asList(historialEntity);

        Page<HistorialEntity> pagedResponse = new PageImpl(lista);

        when(historialRepository.findAll(pageable)).thenReturn(pagedResponse);

        PaginacionHistorialDTO paginacionHistorialDTO = navegacionService.listarHistorial(numPagina, cantidadElementos);

        Assertions.assertThat(paginacionHistorialDTO.getHistorial().get(0).getId()).isEqualTo(historialEntity.getHistorialId());
    }

    @Test
    void listarHistorialNumPaginaErrorTest() {

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> navegacionService.listarHistorial(-1, 10))
                .withNoCause();
    }

    @Test
    void listarHistorialcantidadElementosErrorTest() {

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> navegacionService.listarHistorial(1, -10))
                .withNoCause();
    }

}
