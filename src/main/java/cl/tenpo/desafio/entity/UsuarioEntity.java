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
@Table(name = "usuario")
public class UsuarioEntity {
    @Column(name = "id_usuario", nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long usuarioId;

    @Column(name = "nombre", length = 20, nullable = false)
    private String nombre;

    @Column(name = "apellido", length = 20, nullable = false)
    private String apellido;

    @Column(name = "email", length = 100, nullable = false)
    private String email;

    @Column(name = "clave", length = 100, nullable = false)
    private String clave;

    @Column(name = "fecha_registro", nullable = false, length = 6, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false)
    private LocalDateTime fechaRegistro;

}
