package cl.duoc.logistica.inventarios.service;

import cl.duoc.logistica.inventarios.dto.ClienteResponse;
import cl.duoc.logistica.inventarios.dto.HistorialResponse;
import cl.duoc.logistica.inventarios.dto.InventarioGeneradoResponse;
import cl.duoc.logistica.inventarios.exception.NegocioException;
import cl.duoc.logistica.inventarios.model.Cliente;
import cl.duoc.logistica.inventarios.model.ConfiguracionCiclo;
import cl.duoc.logistica.inventarios.model.HistorialInventario;
import cl.duoc.logistica.inventarios.repository.ClienteRepository;
import cl.duoc.logistica.inventarios.repository.ConfiguracionCicloRepository;
import cl.duoc.logistica.inventarios.repository.HistorialInventarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class InventarioService {

    private final ClienteRepository clienteRepository;
    private final HistorialInventarioRepository historialRepository;
    private final ConfiguracionCicloRepository configuracionCicloRepository;
    private final Random random = new Random();

    public InventarioService(ClienteRepository clienteRepository,
                              HistorialInventarioRepository historialRepository,
                              ConfiguracionCicloRepository configuracionCicloRepository) {
        this.clienteRepository = clienteRepository;
        this.historialRepository = historialRepository;
        this.configuracionCicloRepository = configuracionCicloRepository;
    }

    @Transactional
    public InventarioGeneradoResponse generarInventarioDelDia() {
        List<Cliente> activos = clienteRepository.findByActivoTrueOrderByIdAsc();

        if (activos.isEmpty()) {
            throw new NegocioException("No hay clientes activos para generar el inventario");
        }
        if (activos.size() < 2) {
            throw new NegocioException("Se requieren al menos 2 clientes activos: uno ciclico y uno para la auditoria aleatoria");
        }

        ConfiguracionCiclo configuracion = obtenerOCrearConfiguracion();

        Cliente clienteCiclico = seleccionarSiguienteCiclico(activos, configuracion.getUltimoClienteId());

        Cliente clienteAleatorio = seleccionarAleatorioDistinto(activos, clienteCiclico);

        HistorialInventario historial = new HistorialInventario();
        historial.setFecha(LocalDateTime.now());
        historial.setClienteCiclicoId(clienteCiclico.getId());
        historial.setClienteAleatorioId(clienteAleatorio.getId());
        historial = historialRepository.save(historial);

        configuracion.setUltimoClienteId(clienteCiclico.getId());
        configuracionCicloRepository.save(configuracion);

        return new InventarioGeneradoResponse(
                historial.getId(),
                historial.getFecha(),
                ClienteResponse.fromEntity(clienteCiclico),
                ClienteResponse.fromEntity(clienteAleatorio)
        );
    }

    @Transactional(readOnly = true)
    public List<HistorialResponse> obtenerUltimos10() {
        List<HistorialInventario> historiales = historialRepository.findTop10ByOrderByFechaDesc();

        List<Long> idsClientes = historiales.stream()
                .flatMap(h -> java.util.stream.Stream.of(h.getClienteCiclicoId(), h.getClienteAleatorioId()))
                .distinct()
                .collect(Collectors.toList());

        Map<Long, String> nombresPorId = clienteRepository.findAllById(idsClientes).stream()
                .collect(Collectors.toMap(Cliente::getId, Cliente::getNombre));

        return historiales.stream()
                .map(h -> new HistorialResponse(
                        h.getId(),
                        h.getFecha(),
                        h.getClienteCiclicoId(),
                        nombresPorId.getOrDefault(h.getClienteCiclicoId(), "Cliente eliminado"),
                        h.getClienteAleatorioId(),
                        nombresPorId.getOrDefault(h.getClienteAleatorioId(), "Cliente eliminado")
                ))
                .collect(Collectors.toList());
    }

    private ConfiguracionCiclo obtenerOCrearConfiguracion() {
        return configuracionCicloRepository.findById(ConfiguracionCiclo.SINGLETON_ID)
                .orElseGet(() -> {
                    ConfiguracionCiclo nueva = new ConfiguracionCiclo(ConfiguracionCiclo.SINGLETON_ID, 0L);
                    return configuracionCicloRepository.save(nueva);
                });
    }

    /**
     * Avanza al siguiente cliente activo con id mayor al ultimo procesado.
     * Si no existe (se llego al final de la lista), reinicia al de menor id (wrap-around).
     */
    private Cliente seleccionarSiguienteCiclico(List<Cliente> activosOrdenados, Long ultimoClienteId) {
        return activosOrdenados.stream()
                .filter(cliente -> cliente.getId() > ultimoClienteId)
                .findFirst()
                .orElse(activosOrdenados.get(0));
    }

    private Cliente seleccionarAleatorioDistinto(List<Cliente> activos, Cliente clienteCiclico) {
        List<Cliente> candidatos = activos.stream()
                .filter(cliente -> !cliente.getId().equals(clienteCiclico.getId()))
                .collect(Collectors.toList());

        return candidatos.get(random.nextInt(candidatos.size()));
    }
}