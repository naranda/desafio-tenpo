package cl.tenpo.desafio.controller;

import cl.tenpo.desafio.dto.ResultadoDTO;
import cl.tenpo.desafio.dto.SumaRequestDTO;
import cl.tenpo.desafio.service.OperacionService;
import cl.tenpo.desafio.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/operacion")
public class OperacionController {

    @Autowired
    private OperacionService operacionService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    @PostMapping(value = "/suma", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultadoDTO> suma(HttpServletRequest request, @RequestBody @Valid SumaRequestDTO sumaRequestDTO) {

        String usuario = jwtTokenUtil.getUsernameFromToken(jwtTokenUtil.getToken(request));

        return new ResponseEntity<>(operacionService.suma(usuario, sumaRequestDTO), HttpStatus.OK);

    }
}
