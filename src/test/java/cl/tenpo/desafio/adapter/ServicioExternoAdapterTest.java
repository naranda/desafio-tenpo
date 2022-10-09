package cl.tenpo.desafio.adapter;

import cl.tenpo.desafio.dto.PorcentajeResponseDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

@ExtendWith(MockitoExtension.class)
@Slf4j
public class ServicioExternoAdapterTest {

    @InjectMocks
    private ServicioExternoAdapter servicioExternoAdapter;
    private MockWebServer mockWebServer;

    @BeforeEach
    public void inicializa() throws IOException {
        ReflectionTestUtils.setField(servicioExternoAdapter, "maxIntentos", 3);
        ReflectionTestUtils.setField(servicioExternoAdapter, "delay", 2);

        this.mockWebServer = new MockWebServer();
        this.mockWebServer.start();
        ReflectionTestUtils.setField(servicioExternoAdapter, "uriExterna", mockWebServer.url("/").toString());
    }

    @AfterEach
    public void finaliza() throws IOException {
        this.mockWebServer.shutdown();
    }

    @Test
    void servicvioExternoPorcentajeOKTest() throws Exception {
        PorcentajeResponseDTO responseDTO = new PorcentajeResponseDTO();
        responseDTO.setPorcentaje(30);

        var mockResponse = new MockResponse()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .setBody(dtoToString(responseDTO))
                .setResponseCode(HttpStatus.OK.value());

        mockWebServer.enqueue(mockResponse);

        var response = servicioExternoAdapter.obtenerPorcentaje();
        assertThat(response).extracting(PorcentajeResponseDTO::getPorcentaje).isEqualTo(30);


        var recordedRequest = mockWebServer.takeRequest();
        assertEquals("GET", recordedRequest.getMethod());
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
