package cl.tenpo.desafio.adapter;

import cl.tenpo.desafio.dto.PorcentajeResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;

import java.time.Duration;

@Component
@Slf4j
public class ServicioExternoAdapter {

    @Value("${endpoint.porcentaje}")
    private String uriExterna;

    @Value("${request.max.intentos:3}")
    private Integer maxIntentos;

    @Value("${request.delay:2}")
    private Integer delay;

    protected WebClient webClient() {
        return WebClient.builder()
                .baseUrl(uriExterna)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    private PorcentajeResponseDTO consumirPorcentaje() {
        PorcentajeResponseDTO response = null;
        try {
            log.debug("Consultando a servicio externo por porcentaje....");
            response = webClient()
                    .get()
                    .retrieve()
                    .bodyToMono(PorcentajeResponseDTO.class)
                    .retryWhen(Retry.fixedDelay(maxIntentos, Duration.ofSeconds(delay)))
                    .block();

            log.debug("Respuesta servicio porcentaje: {}", response.toString());


        } catch (Exception e) {
            log.error("Se produjo un error al obtener el porcentaje... {}", e.getMessage());
        }
        return response;
    }

    @Cacheable(value = "porcentaje", unless = "#result==null")
    public PorcentajeResponseDTO obtenerPorcentaje() {

        return consumirPorcentaje();
    }
}
