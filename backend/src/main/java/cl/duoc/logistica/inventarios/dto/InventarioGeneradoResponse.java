package cl.duoc.logistica.inventarios.dto;

import java.time.LocalDateTime;

public record InventarioGeneradoResponse(
        Long historialId,
        LocalDateTime fecha,
        ClienteResponse clienteCiclico,
        ClienteResponse clienteAleatorio
) {
}