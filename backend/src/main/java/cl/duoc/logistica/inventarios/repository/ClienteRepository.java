package cl.duoc.logistica.inventarios.repository;

import cl.duoc.logistica.inventarios.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    List<Cliente> findByActivoTrueOrderByIdAsc();
}