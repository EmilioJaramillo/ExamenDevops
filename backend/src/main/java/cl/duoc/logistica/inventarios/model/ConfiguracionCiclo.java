package cl.duoc.logistica.inventarios.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Tabla singleton: siempre se usa el registro con id = 1 como puntero del ciclo actual.
 */
@Entity
@Table(name = "configuracion_ciclo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConfiguracionCiclo {

    public static final Long SINGLETON_ID = 1L;

    @Id
    private Long id;

    @Column(name = "ultimo_cliente_id", nullable = false)
    private Long ultimoClienteId;
}