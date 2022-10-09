package cl.tenpo.desafio.controller;

import cl.tenpo.desafio.dto.LoginRequestDTO;
import cl.tenpo.desafio.dto.LoginResponseDTO;
import cl.tenpo.desafio.dto.SignUpRequestDTO;
import cl.tenpo.desafio.security.JwtAuthenticationEntryPoint;
import cl.tenpo.desafio.security.JwtTokenUtil;
import cl.tenpo.desafio.security.JwtUserDetailsService;
import cl.tenpo.desafio.service.LoginService;
import cl.tenpo.desafio.service.NavegacionService;
import cl.tenpo.desafio.service.SignUpService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@WebMvcTest(LoginController.class)
class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoginService loginService;

    @MockBean
    private SignUpService signUpService;

    @MockBean
    private JwtUserDetailsService jwtUserDetailsService;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @MockBean
    private NavegacionService navegacionService;

    @MockBean
    private JmsTemplate jmsTemplate;

    @Test
    void signUpOKTest() throws Exception {

        var request = new SignUpRequestDTO();
        request.setNombre("Nicolas");
        request.setApellido("Aranda");
        request.setEmail("nicolas.aranda@gmail.com");
        request.setClave("welcome1");

        doNothing().when(signUpService).signUp(request);

        var requestJson = dtoToString(request);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/login/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void signUpAtributoObligatorioFaltanteErrorTest() throws Exception {

        var request = new SignUpRequestDTO();
        request.setNombre("Nicolas");
        request.setApellido("Aranda");
        request.setEmail("nicolas.aranda@gmail.com");
        // request.setClave("welcome1");


        var requestJson = dtoToString(request);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/login/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void loginOKTest() throws Exception {
        var request = new LoginRequestDTO();
        request.setEmail("nicolas.aranda@gmail.com");
        request.setClave("welcome1");

        var response = new LoginResponseDTO();
        response.setToken("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJuaWNvbGFzLmFyYW5kYUBnbWFpbC5jb20iLCJleHAiOjE2NjUyOTI3NjMsImlhdCI6MTY2NTI3NDc2M30.Bjp6Ya6j5GO2R9er2lRYYbywCQpP_P22bZsiqBzHbkmH5Em1t0AAD_j1aR8MyyLj3vLrj7DLhGuJE1d5UDwIfg");

        when(loginService.login(request)).thenReturn(response);

        var requestJson = dtoToString(request);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/login/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void loginAtributoObligatorioFaltanteErrorTest() throws Exception {
        var request = new LoginRequestDTO();
        request.setEmail("nicolas.aranda@gmail.com");

        var response = new LoginResponseDTO();
        response.setToken("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJuaWNvbGFzLmFyYW5kYUBnbWFpbC5jb20iLCJleHAiOjE2NjUyOTI3NjMsImlhdCI6MTY2NTI3NDc2M30.Bjp6Ya6j5GO2R9er2lRYYbywCQpP_P22bZsiqBzHbkmH5Em1t0AAD_j1aR8MyyLj3vLrj7DLhGuJE1d5UDwIfg");

        when(loginService.login(request)).thenReturn(response);

        var requestJson = dtoToString(request);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/login/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    private <T> String dtoToString(T dto) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        try {
            return ow.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
        }
        return null;
    }
}
