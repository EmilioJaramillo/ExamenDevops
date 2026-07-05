package cl.duoc.logistica.inventarios.exception;

public class ClienteDuplicadoException extends RuntimeException {

    public ClienteDuplicadoException(Long id) {
        super("Ya existe un cliente registrado con id " + id);
    }
}