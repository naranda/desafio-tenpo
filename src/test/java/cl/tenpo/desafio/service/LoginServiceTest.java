package cl.tenpo.desafio.service;

import cl.tenpo.desafio.dto.LoginRequestDTO;
import cl.tenpo.desafio.dto.LoginResponseDTO;
import cl.tenpo.desafio.entity.UsuarioEntity;
import cl.tenpo.desafio.ex.ClaveException;
import cl.tenpo.desafio.ex.LoginException;
import cl.tenpo.desafio.repository.UsuarioRepository;
import cl.tenpo.desafio.security.JwtTokenUtil;
import cl.tenpo.desafio.util.Util;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @InjectMocks
    private LoginService loginService;

    @Mock
    private Util util;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private UserDetailsService jwtInMemoryUserDetailsService;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private UserDetails userDetails;

    @Test
    void loginOKTest() throws NoSuchAlgorithmException {

        var request = new LoginRequestDTO();
        request.setEmail("ignacia@gmail.com");
        request.setClave("welcome1");

        String clave = "123456";

        when(util.generarClave(request.getClave())).thenReturn(clave);


        UsuarioEntity usuarioEntity = new UsuarioEntity();
        usuarioEntity.setClave(clave);
        usuarioEntity.setNombre("Ignacia");
        usuarioEntity.setApellido("A");
        usuarioEntity.setEmail("ignacia@gmail.com");
        usuarioEntity.setFechaRegistro(LocalDateTime.now());
        usuarioEntity.setUsuarioId(1l);
        Optional<UsuarioEntity> usuarioEntityOptional = Optional.of(usuarioEntity);
        when(usuarioRepository.findByEmailAndClave(request.getEmail(), clave)).thenReturn(usuarioEntityOptional);

        when(jwtInMemoryUserDetailsService.loadUserByUsername(request.getEmail().toLowerCase())).thenReturn(userDetails);

        String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJpZ25hY2lhQGdtYWlsLmNvbSIsImV4cCI6MTY2NTE4ODEwOSwiaWF0IjoxNjY1MTcwMTA5fQ.lDhIaEQprouYwpHW5jo0qmlfFjcO0ie4dR-r0acveK4Yeux-As_bcn8cPWd8qEFrKnqKv82yhGTj2lfD60QC7w";

        when(jwtTokenUtil.generateToken(userDetails)).thenReturn(token);


        LoginResponseDTO response = loginService.login(request);

        Assertions.assertThat(response.getToken()).isEqualTo(token);

    }

    @Test
    void loginCredencialesErrorTest() throws NoSuchAlgorithmException {
        var request = new LoginRequestDTO();
        request.setEmail("ignacia@gmail.com");
        request.setClave("qwerty");

        String clave = "vcxzsd";

        when(util.generarClave(request.getClave())).thenReturn(clave);

        Optional<UsuarioEntity> usuarioEntityOptional = Optional.ofNullable(null);
        when(usuarioRepository.findByEmailAndClave(request.getEmail(), clave)).thenReturn(usuarioEntityOptional);

        assertThatExceptionOfType(LoginException.class)
                .isThrownBy(() -> loginService.login(request))
                .withNoCause();
    }


    @Test
    void loginGenerarClaveErrorTest() throws NoSuchAlgorithmException {

        var request = new LoginRequestDTO();
        request.setEmail("ignacia@gmail.com");
        request.setClave("error");
        when(util.generarClave(anyString())).thenThrow(new NoSuchAlgorithmException());

        assertThatExceptionOfType(ClaveException.class)
                .isThrownBy(() -> loginService.login(request))
                .withNoCause();

    }
}
