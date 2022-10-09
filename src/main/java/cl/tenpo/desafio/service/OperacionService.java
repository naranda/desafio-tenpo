package cl.tenpo.desafio.service;

import cl.tenpo.desafio.adapter.ServicioExternoAdapter;
import cl.tenpo.desafio.dto.NavegacionDTO;
import cl.tenpo.desafio.dto.PorcentajeResponseDTO;
import cl.tenpo.desafio.dto.ResultadoDTO;
import cl.tenpo.desafio.dto.SumaRequestDTO;
import cl.tenpo.desafio.ex.BadRequestException;
import cl.tenpo.desafio.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class OperacionService {

    @Autowired
    private ServicioExternoAdapter servicioExternoAdapter;

    @Autowired
    private NavegacionService navegacionService;

    @Autowired
    private Util util;

    @Value("${endpoint.porcentaje}")
    private String uriExterna;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Value("${cola.jms}")
    private String colaJms;

    private static final int FACTOR = 100;

    public ResultadoDTO suma(String usuario, SumaRequestDTO request) {

        PorcentajeResponseDTO porcentajeResponseDTO = servicioExternoAdapter.obtenerPorcentaje();

        if (porcentajeResponseDTO == null || porcentajeResponseDTO.getPorcentaje() == null) {
            throw new BadRequestException("No se pudo obtener el porcentaje del servicio externo");
        }


        double num1 = request.getNumero1();
        double num2 = request.getNumero2();
        Integer porcentaje = porcentajeResponseDTO.getPorcentaje();
        double suma1 = num1 + num2;
        log.debug("Valor 1: {}", num1);
        log.debug("Valor2: {}", num2);
        log.debug("Suma : {}", suma1);
        log.debug("Porcentaje devuelto: {}", porcentaje);

        double valorDelPorcentaje = (suma1 * porcentaje) / FACTOR;
        log.debug("Valor del porcentaje: {}", valorDelPorcentaje);

        double resultado = suma1 + valorDelPorcentaje;

        log.debug("Resultado: {}", resultado);

        ResultadoDTO resultadoDTO = new ResultadoDTO();
        resultadoDTO.setResultado(resultado);

        NavegacionDTO navegacionDTO = new NavegacionDTO();
        navegacionDTO.setStatus(HttpStatus.OK.value());
        navegacionDTO.setResponse(util.dtoToString(porcentajeResponseDTO));
        navegacionDTO.setUrl(uriExterna);
        navegacionDTO.setMethod("CACHE");
        navegacionDTO.setUsuario(usuario);

        log.debug("Preparando mensaje desde OperacionService para dejarlo en la cola...");
        jmsTemplate.convertAndSend(colaJms, navegacionDTO);
        log.debug("Fecha de mensaje en la cola: {}", LocalDateTime.now().toString());

        return resultadoDTO;
    }

}
