package cl.tenpo.desafio.service;

import cl.tenpo.desafio.dto.HistorialDTO;
import cl.tenpo.desafio.dto.NavegacionDTO;
import cl.tenpo.desafio.dto.PaginacionHistorialDTO;
import cl.tenpo.desafio.entity.HistorialEntity;
import cl.tenpo.desafio.ex.BadRequestException;
import cl.tenpo.desafio.repository.HistorialRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
@Slf4j
public class NavegacionService {

    @Autowired
    private HistorialRepository historialRepository;

    private static final int PAGINA_MINIMA = 1;

    private static final int MINIMA_CANTIDAD_ELEMENTOS = 1;

    @JmsListener(destination = "navegacionQueue", containerFactory = "myFactory")
    public void registrarNavegacion(NavegacionDTO navegacionDTO) {

        //Codigo para verificar que registro de historico no afecte en las respuesta de los servicios
        /*try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }*/

        log.debug("Consumiendo mensaje de la cola: {}", LocalDateTime.now().toString());
        log.debug("Mensaje consumido: {}", navegacionDTO.toString());

        HistorialEntity historialEntity = new HistorialEntity();
        historialEntity.setUsuario(navegacionDTO.getUsuario().toLowerCase());
        historialEntity.setMethod(navegacionDTO.getMethod());
        historialEntity.setStatus(navegacionDTO.getStatus());
        historialEntity.setUrl(navegacionDTO.getUrl());
        historialEntity.setResultado(navegacionDTO.getResponse());

        log.debug("registrando historico....");
        historialRepository.save(historialEntity);
    }


    public PaginacionHistorialDTO listarHistorial(int numPagina, int cantidadElementos) {

        if (numPagina < PAGINA_MINIMA) {
            throw new BadRequestException("Numero de pagina debe ser minimo " + PAGINA_MINIMA);
        }

        if (cantidadElementos < MINIMA_CANTIDAD_ELEMENTOS) {
            throw new BadRequestException("Cantidad de elementos debe ser minimo " + MINIMA_CANTIDAD_ELEMENTOS);
        }

        numPagina = numPagina - PAGINA_MINIMA;

        Pageable pageable = PageRequest.of(numPagina, cantidadElementos, Sort.by("fechaRegistro").descending());

        Page<HistorialEntity> historialEntities = historialRepository.findAll(pageable);

        PaginacionHistorialDTO paginacionHistorialDTO = new PaginacionHistorialDTO();
        paginacionHistorialDTO.setPagina(historialEntities.getNumber());
        paginacionHistorialDTO.setTotalElementos(historialEntities.getTotalElements());
        paginacionHistorialDTO.setTotalPagina(historialEntities.getTotalPages());
        paginacionHistorialDTO.setHistorial(historialEntities.stream().map(h -> {
            HistorialDTO historialDTO = new HistorialDTO();
            historialDTO.setId(h.getHistorialId());
            historialDTO.setUrl(h.getUrl());
            historialDTO.setUsuario(h.getUsuario());
            historialDTO.setMethod(h.getMethod());
            historialDTO.setStatus(h.getStatus());
            historialDTO.setFechaRegistro(h.getFechaRegistro());

            historialDTO.setResultado(h.getResultado());

            return historialDTO;
        }).collect(Collectors.toList()));

        return paginacionHistorialDTO;
    }
}
