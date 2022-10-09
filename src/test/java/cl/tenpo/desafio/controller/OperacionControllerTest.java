package cl.tenpo.desafio.controller;

import cl.tenpo.desafio.dto.ResultadoDTO;
import cl.tenpo.desafio.dto.SumaRequestDTO;
import cl.tenpo.desafio.security.JwtAuthenticationEntryPoint;
import cl.tenpo.desafio.security.JwtTokenUtil;
import cl.tenpo.desafio.security.JwtUserDetailsService;
import cl.tenpo.desafio.service.NavegacionService;
import cl.tenpo.desafio.service.OperacionService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(OperacionController.class)
class OperacionControllerTest {

    @MockBean
    private OperacionService operacionService;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtUserDetailsService jwtUserDetailsService;

    @MockBean
    private NavegacionService navegacionService;

    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @MockBean
    private JmsTemplate jmsTemplate;

    @Test
    void sumaOKTest() throws Exception {

        String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJuaWNvbGFzLmFyYW5kYUBnbWFpbC5jb20iLCJleHAiOjE2NjUyOTI3NjMsImlhdCI6MTY2NTI3NDc2M30.Bjp6Ya6j5GO2R9er2lRYYbywCQpP_P22bZsiqBzHbkmH5Em1t0AAD_j1aR8MyyLj3vLrj7DLhGuJE1d5UDwIfg";
        when(jwtTokenUtil.getToken(any())).thenReturn(token);

        String usuario = "nicolas.aranda@gmail.com";
        when(jwtTokenUtil.getUsernameFromToken(any())).thenReturn(usuario);

        var sumaRequestDTO = new SumaRequestDTO();
        sumaRequestDTO.setNumero1(20.0);
        sumaRequestDTO.setNumero2(30.0);

        var sumaResponse = new ResultadoDTO();
        sumaResponse.setResultado(40.0);
        when(operacionService.suma(usuario, sumaRequestDTO)).thenReturn(sumaResponse);

        var requestJson = dtoToString(sumaRequestDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/suma")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

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
