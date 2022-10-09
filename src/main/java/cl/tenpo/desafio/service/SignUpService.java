package cl.tenpo.desafio.service;

import cl.tenpo.desafio.dto.SignUpRequestDTO;
import cl.tenpo.desafio.entity.UsuarioEntity;
import cl.tenpo.desafio.ex.ClaveException;
import cl.tenpo.desafio.ex.UsuarioExistException;
import cl.tenpo.desafio.repository.UsuarioRepository;
import cl.tenpo.desafio.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Service
@Slf4j
public class SignUpService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private Util util;

    public void signUp(SignUpRequestDTO signUpRequestDTO) {

        Optional<UsuarioEntity> usuarioEntityOptional = usuarioRepository.findByEmail(signUpRequestDTO.getEmail().toLowerCase());

        if (usuarioEntityOptional.isPresent()) {
            log.error("Ya existe usuario registrado");
            throw new UsuarioExistException();
        }

        UsuarioEntity usuarioEntity = new UsuarioEntity();
        usuarioEntity.setApellido(signUpRequestDTO.getApellido());
        usuarioEntity.setNombre(signUpRequestDTO.getNombre());
        usuarioEntity.setEmail(signUpRequestDTO.getEmail().toLowerCase());

        try {
            usuarioEntity.setClave(util.generarClave(signUpRequestDTO.getClave().toLowerCase()));
        } catch (NoSuchAlgorithmException e) {
            log.error("Se produjo un error al generar clave");
            throw new ClaveException();
        }

        usuarioRepository.save(usuarioEntity);
    }

}
