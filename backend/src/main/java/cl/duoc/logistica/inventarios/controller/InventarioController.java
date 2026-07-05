package cl.duoc.logistica.inventarios.controller;

import cl.duoc.logistica.inventarios.dto.HistorialResponse;
import cl.duoc.logistica.inventarios.dto.InventarioGeneradoResponse;
import cl.duoc.logistica.inventarios.service.InventarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/inventarios")
public class InventarioController {

    private final InventarioService inventarioService;

    public InventarioController(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    @PostMapping("/generar")
    public ResponseEntity<InventarioGeneradoResponse> generar() {
        InventarioGeneradoResponse response = inventarioService.generarInventarioDelDia();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<HistorialResponse>> obtenerUltimos() {
        return ResponseEntity.ok(inventarioService.obtenerUltimos10());
    }
}