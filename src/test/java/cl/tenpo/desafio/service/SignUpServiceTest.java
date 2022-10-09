package cl.tenpo.desafio.service;

import cl.tenpo.desafio.dto.SignUpRequestDTO;
import cl.tenpo.desafio.entity.UsuarioEntity;
import cl.tenpo.desafio.ex.ClaveException;
import cl.tenpo.desafio.ex.UsuarioExistException;
import cl.tenpo.desafio.repository.UsuarioRepository;
import cl.tenpo.desafio.util.Util;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SignUpServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private Util util;

    @InjectMocks
    private SignUpService signUpService;

    @Test
    void signUpOKTest() throws NoSuchAlgorithmException {

        var request = new SignUpRequestDTO();

        request.setEmail("nicolas.aranda@gmail.com");
        request.setApellido("Aranda");
        request.setNombre("Nicolas");
        request.setClave("welcome1");

        Optional<UsuarioEntity> usuarioEntityOptional = Optional.ofNullable(null);

        when(usuarioRepository.findByEmail(request.getEmail().toLowerCase())).thenReturn(usuarioEntityOptional);

        UsuarioEntity usuarioEntity = new UsuarioEntity();
        usuarioEntity.setApellido(request.getApellido());
        usuarioEntity.setNombre(request.getNombre());
        usuarioEntity.setEmail(request.getEmail().toLowerCase());

        String claveCifrada = "qwertyu";
        when(util.generarClave(request.getClave().toLowerCase())).thenReturn(claveCifrada);

        signUpService.signUp(request);
    }

    @Test
    void signUpExisteUsuarioErrorTest() {
        var request = new SignUpRequestDTO();
        request.setEmail("nicolas.aranda@gmail.com");
        request.setApellido("Aranda");
        request.setNombre("Nicolas");
        request.setClave("welcome1");

        UsuarioEntity usuarioEntity = new UsuarioEntity();
        usuarioEntity.setApellido(request.getApellido());
        usuarioEntity.setNombre(request.getNombre());
        usuarioEntity.setEmail(request.getEmail().toLowerCase());

        Optional<UsuarioEntity> usuarioEntityOptional = Optional.of(usuarioEntity);

        when(usuarioRepository.findByEmail(request.getEmail().toLowerCase())).thenReturn(usuarioEntityOptional);

        assertThatExceptionOfType(UsuarioExistException.class)
                .isThrownBy(() -> signUpService.signUp(request))
                .withNoCause();
    }

    @Test
    void signUpnGenerarClaveErrorTest() throws NoSuchAlgorithmException {

        var request = new SignUpRequestDTO();
        request.setEmail("nicolas.aranda@gmail.com");
        request.setApellido("Aranda");
        request.setNombre("Nicolas");
        request.setClave("welcome1");
        when(util.generarClave(anyString())).thenThrow(new NoSuchAlgorithmException());

        Optional<UsuarioEntity> usuarioEntityOptional = Optional.ofNullable(null);

        when(usuarioRepository.findByEmail(request.getEmail().toLowerCase())).thenReturn(usuarioEntityOptional);

        assertThatExceptionOfType(ClaveException.class)
                .isThrownBy(() -> signUpService.signUp(request))
                .withNoCause();
    }
}
