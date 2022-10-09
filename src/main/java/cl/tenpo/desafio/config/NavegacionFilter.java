package cl.tenpo.desafio.config;

import cl.tenpo.desafio.dto.NavegacionDTO;
import cl.tenpo.desafio.security.JwtTokenUtil;
import cl.tenpo.desafio.service.NavegacionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;

@Component
@Slf4j
public class NavegacionFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private NavegacionService navegacionService;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Value(("${cola.jms}"))
    private String colaJms;

    private String getStringValue(byte[] contentAsByteArray, String characterEncoding) {
        try {
            return new String(contentAsByteArray, 0, contentAsByteArray.length, characterEncoding);
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage());
        }
        return "";
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        filterChain.doFilter(requestWrapper, responseWrapper);

        String requestBody = getStringValue(requestWrapper.getContentAsByteArray(),
                request.getCharacterEncoding());
        String responseBody = getStringValue(responseWrapper.getContentAsByteArray(),
                response.getCharacterEncoding());


        log.debug("Preparando datos historicos para guardar");
        responseWrapper.copyBodyToResponse();

        String usuario = (request.getHeader(jwtTokenUtil.AUTHORIZATION) == null || request.getHeader(jwtTokenUtil.AUTHORIZATION).isEmpty()) ? "anonimo" : jwtTokenUtil.getUsernameFromToken(jwtTokenUtil.getToken(request));

        NavegacionDTO navegacionDTO = new NavegacionDTO();
        navegacionDTO.setStatus(response.getStatus());
        navegacionDTO.setResponse(responseBody);
        navegacionDTO.setUrl(request.getRequestURI());
        navegacionDTO.setMethod(request.getMethod());
        navegacionDTO.setUsuario(usuario);

        log.debug("Preparando mensaje desde NavegacionFilter para dejarlo en la cola...");

        jmsTemplate.convertAndSend(colaJms, navegacionDTO);
        log.debug("Fecha de mensaje en la cola: {}", LocalDateTime.now().toString());
    }
}
