package cl.duoc.logistica.inventarios.service;

import cl.duoc.logistica.inventarios.dto.ClienteRequest;
import cl.duoc.logistica.inventarios.dto.ClienteResponse;
import cl.duoc.logistica.inventarios.exception.ClienteDuplicadoException;
import cl.duoc.logistica.inventarios.model.Cliente;
import cl.duoc.logistica.inventarios.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Transactional
    public ClienteResponse registrarCliente(ClienteRequest request) {
        if (clienteRepository.existsById(request.id())) {
            throw new ClienteDuplicadoException(request.id());
        }

        Cliente cliente = new Cliente(request.id(), request.nombre(), true);
        cliente = clienteRepository.save(cliente);

        return ClienteResponse.fromEntity(cliente);
    }
}