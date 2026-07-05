package cl.duoc.logistica.inventarios.repository;

import cl.duoc.logistica.inventarios.model.HistorialInventario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistorialInventarioRepository extends JpaRepository<HistorialInventario, Long> {

    List<HistorialInventario> findTop10ByOrderByFechaDesc();
}