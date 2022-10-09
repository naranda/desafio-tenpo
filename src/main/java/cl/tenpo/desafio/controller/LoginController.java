package cl.tenpo.desafio.controller;

import cl.tenpo.desafio.dto.LoginRequestDTO;
import cl.tenpo.desafio.dto.LoginResponseDTO;
import cl.tenpo.desafio.dto.SignUpRequestDTO;
import cl.tenpo.desafio.service.LoginService;
import cl.tenpo.desafio.service.SignUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private SignUpService signUpService;

    @Autowired
    private LoginService loginService;

    @PostMapping(value = "/signup", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> signUp(@RequestBody @Valid SignUpRequestDTO signUpRequestDTO) {

        signUpService.signUp(signUpRequestDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    @PostMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO loginRequestDTO) {

        return new ResponseEntity<>(loginService.login(loginRequestDTO), HttpStatus.OK);
    }
}
