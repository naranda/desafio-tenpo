package cl.tenpo.desafio.controller;

import cl.tenpo.desafio.security.JwtAuthenticationEntryPoint;
import cl.tenpo.desafio.dto.HistorialDTO;
import cl.tenpo.desafio.dto.PaginacionHistorialDTO;
import cl.tenpo.desafio.security.JwtUserDetailsService;
import cl.tenpo.desafio.service.NavegacionService;
import cl.tenpo.desafio.security.JwtTokenUtil;
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

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(HistorialController.class)
class HistorialControllerTest {

    @MockBean
    private NavegacionService navegacionService;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtUserDetailsService jwtUserDetailsService;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @MockBean
    private JmsTemplate jmsTemplate;

    @Test
    void listarHistorialOKTest() throws Exception {
        String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJuaWNvbGFzLmFyYW5kYUBnbWFpbC5jb20iLCJleHAiOjE2NjUyOTI3NjMsImlhdCI6MTY2NTI3NDc2M30.Bjp6Ya6j5GO2R9er2lRYYbywCQpP_P22bZsiqBzHbkmH5Em1t0AAD_j1aR8MyyLj3vLrj7DLhGuJE1d5UDwIfg";


        var historialDTO = new HistorialDTO();
        historialDTO.setId(1);
        historialDTO.setResultado("");
        historialDTO.setMethod("POST");
        historialDTO.setUsuario("anonimo");
        historialDTO.setUrl("/api/login/signup");
        historialDTO.setFechaRegistro(LocalDateTime.now());
        historialDTO.setStatus(201);

        var historialDTO2 = new HistorialDTO();
        historialDTO2.setId(1);
        historialDTO2.setResultado("{\"token\":\"eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJuaWNvbGFzLmFyYW5kYUBnbWFpbC5jb20iLCJleHAiOjE2NjUyMTcxMDksImlhdCI6MTY2NTE5OTEwOX0.L0Or81ullTgkaC_R50HTA0naP92aFUw-AfSPm4gk1yGVK7iYzz2_m9KDl2Ru-2gXDV1KDSVz8Qn_SLu0fR9dQA\"}");
        historialDTO2.setMethod("POST");
        historialDTO2.setUsuario("anonimo");
        historialDTO2.setUrl("/api/login/");
        historialDTO2.setFechaRegistro(LocalDateTime.now());
        historialDTO2.setStatus(200);

        List<HistorialDTO> historialDTOS = Arrays.asList(historialDTO, historialDTO2);

        var response = new PaginacionHistorialDTO();
        response.setHistorial(historialDTOS);
        response.setTotalPagina(1);
        response.setTotalElementos(2);
        response.setPagina(0);


        when(navegacionService.listarHistorial(1, 15)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/historial?numPagina=1&cantidadElementos=15")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

    }

}
