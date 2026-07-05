package cl.duoc.logistica.inventarios.dto;

import cl.duoc.logistica.inventarios.model.Cliente;

public record ClienteResponse(Long id, String nombre, Boolean activo) {

    public static ClienteResponse fromEntity(Cliente cliente) {
        return new ClienteResponse(cliente.getId(), cliente.getNombre(), cliente.getActivo());
    }
}