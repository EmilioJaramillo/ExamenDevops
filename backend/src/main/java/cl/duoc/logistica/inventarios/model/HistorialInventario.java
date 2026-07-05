package cl.duoc.logistica.inventarios.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "historial_inventario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HistorialInventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(name = "cliente_ciclico_id", nullable = false)
    private Long clienteCiclicoId;

    @Column(name = "cliente_aleatorio_id", nullable = false)
    private Long clienteAleatorioId;
}