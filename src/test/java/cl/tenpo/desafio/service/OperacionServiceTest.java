package cl.tenpo.desafio.service;

import cl.tenpo.desafio.adapter.ServicioExternoAdapter;
import cl.tenpo.desafio.dto.PorcentajeResponseDTO;
import cl.tenpo.desafio.dto.ResultadoDTO;
import cl.tenpo.desafio.dto.SumaRequestDTO;
import cl.tenpo.desafio.ex.BadRequestException;
import cl.tenpo.desafio.util.Util;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OperacionServiceTest {
    @Mock
    private ServicioExternoAdapter servicioExternoAdapter;

    @Mock
    private NavegacionService navegacionService;

    @Mock
    private Util util;

    @InjectMocks
    private OperacionService operacionService;

    @Mock
    private JmsTemplate jmsTemplate;

    @BeforeEach
    public void initEach() {
        ReflectionTestUtils.setField(operacionService, "uriExterna", "http://localhost:3000/porcentaje");
    }

    @Test
    void sumaOkTest() {

        String usuario = "ignacia@gmail.com";
        var request = new SumaRequestDTO();
        request.setNumero1(10.0);
        request.setNumero2(20.0);

        var response = new PorcentajeResponseDTO();
        response.setPorcentaje(50);

        when(servicioExternoAdapter.obtenerPorcentaje()).thenReturn(response);

        String s = dtoToString(response);
        when(util.dtoToString(response)).thenReturn(s);

        ResultadoDTO suma = operacionService.suma(usuario, request);

        Assertions.assertThat(suma.getResultado()).isEqualTo(45);
    }

    @Test
    void sumaErrorTest() {
        String usuario = "ignacia@gmail.com";
        var request = new SumaRequestDTO();
        request.setNumero1(10.0);
        request.setNumero2(20.0);

        when(servicioExternoAdapter.obtenerPorcentaje()).thenReturn(null);

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> operacionService.suma(usuario, request))
                .withNoCause();
    }

    public <T> String dtoToString(T dto) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        try {
            return ow.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
        }
        return null;
    }


}
