package cl.tenpo.desafio.controller;

import cl.tenpo.desafio.dto.PaginacionHistorialDTO;
import cl.tenpo.desafio.service.NavegacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HistorialController {

    @Autowired
    private NavegacionService navegacionService;

    @GetMapping("/historial")
    public ResponseEntity<PaginacionHistorialDTO> listarHistorial(@RequestParam(name = "numPagina", required = false, defaultValue = "1") int numPagina,
                                                                  @RequestParam(name = "cantidadElementos", required = false, defaultValue = "15") int cantidadElementos) {

        return new ResponseEntity<>(navegacionService.listarHistorial(numPagina, cantidadElementos), HttpStatus.OK);
    }
}
