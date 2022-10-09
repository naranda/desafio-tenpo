package cl.tenpo.desafio.service;

import cl.tenpo.desafio.dto.LoginRequestDTO;
import cl.tenpo.desafio.dto.LoginResponseDTO;
import cl.tenpo.desafio.entity.UsuarioEntity;
import cl.tenpo.desafio.ex.ClaveException;
import cl.tenpo.desafio.ex.LoginException;
import cl.tenpo.desafio.repository.UsuarioRepository;
import cl.tenpo.desafio.security.JwtTokenUtil;
import cl.tenpo.desafio.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Service
@Slf4j
public class LoginService {
    @Autowired
    private Util util;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UserDetailsService jwtInMemoryUserDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {

        String clave = null;
        try {
            clave = util.generarClave(loginRequestDTO.getClave().toLowerCase());
        } catch (NoSuchAlgorithmException e) {
            log.error("Se produjo un error al validar la clave del usuario");
            throw new ClaveException();
        }


        Optional<UsuarioEntity> usuarioEntityOptional = usuarioRepository.findByEmailAndClave(loginRequestDTO.getEmail().toLowerCase(), clave);


        if (usuarioEntityOptional.isEmpty()) {
            log.error("Verificar Usuario o Clave");
            throw new LoginException();
        }

        UserDetails userDetails = jwtInMemoryUserDetailsService.loadUserByUsername(loginRequestDTO.getEmail().toLowerCase());

        String token = jwtTokenUtil.generateToken(userDetails);

        LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
        loginResponseDTO.setToken(token);

        return loginResponseDTO;

    }
}
