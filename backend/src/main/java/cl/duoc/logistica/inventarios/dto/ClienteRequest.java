package cl.duoc.logistica.inventarios.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ClienteRequest(

        @NotNull(message = "El id del cliente es obligatorio")
        Long id,

        @NotBlank(message = "El nombre del cliente es obligatorio")
        String nombre
) {
}