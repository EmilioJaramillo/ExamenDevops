package cl.duoc.logistica.inventarios.dto;

import java.time.LocalDateTime;

public record HistorialResponse(
        Long id,
        LocalDateTime fecha,
        Long clienteCiclicoId,
        String clienteCiclicoNombre,
        Long clienteAleatorioId,
        String clienteAleatorioNombre
) {
}