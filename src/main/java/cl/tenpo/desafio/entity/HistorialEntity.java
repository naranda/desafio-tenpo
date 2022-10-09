package cl.tenpo.desafio.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@org.hibernate.annotations.Proxy(lazy = false)
@Table(name = "historial")
public class HistorialEntity {

    @Column(name = "id_historial", nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long historialId;

    @Column(name = "fecha_registro", nullable = false, length = 6, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false)
    private LocalDateTime fechaRegistro;

    @Column(name = "email", length = 50, nullable = false)
    private String usuario;

    @Column(name = "method", length = 20)
    private String method;

    @Column(name = "status")
    private Integer status;

    @Column(name = "url", length = 100)
    private String url;

    @Column(name = "response", columnDefinition = "TEXT")
    private String resultado;
}
