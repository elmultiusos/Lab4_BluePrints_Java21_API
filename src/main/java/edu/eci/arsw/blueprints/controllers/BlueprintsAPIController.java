package edu.eci.arsw.blueprints.controllers;

import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import edu.eci.arsw.blueprints.services.BlueprintsServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping("/api/v1/blueprints")
public class BlueprintsAPIController {

    private final BlueprintsServices services;

    public BlueprintsAPIController(BlueprintsServices services) {
        this.services = services;
    }

    // GET /blueprints
    @GetMapping
    @Operation(summary = "Obtener todos los blueprints", description = "Retorna todos los blueprints registrados")
    @ApiResponse(responseCode = "200", description = "Consulta exitosa")
    public ResponseEntity<edu.eci.arsw.blueprints.model.ApiResponse<Set<Blueprint>>> getAll() {
        Set<Blueprint> data = services.getAllBlueprints();
        edu.eci.arsw.blueprints.model.ApiResponse<Set<Blueprint>> response = new edu.eci.arsw.blueprints.model.ApiResponse<>(200, "execute ok", data);
        return ResponseEntity.ok(response);
    }

    // GET /blueprints/{author}
    @GetMapping("/{author}")
    @Operation(summary = "Obtener blueprints por autor", description = "Retorna los blueprints de un autor específico")
    @ApiResponse(responseCode = "200", description = "Consulta exitosa")
    @ApiResponse(responseCode = "404", description = "Autor no encontrado")
    public ResponseEntity<edu.eci.arsw.blueprints.model.ApiResponse<?>> byAuthor(@PathVariable String author) {
        try {
            Set<Blueprint> data = services.getBlueprintsByAuthor(author);
            edu.eci.arsw.blueprints.model.ApiResponse<Set<Blueprint>> response = new edu.eci.arsw.blueprints.model.ApiResponse<>(200, "execute ok", data);
            return ResponseEntity.ok(response);
        } catch (BlueprintNotFoundException e) {
            edu.eci.arsw.blueprints.model.ApiResponse<String> response = new edu.eci.arsw.blueprints.model.ApiResponse<>(404, e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    // GET /blueprints/{author}/{bpname}
    @GetMapping("/{author}/{bpname}")
    @Operation(summary = "Obtener blueprint por autor y nombre", description = "Retorna un blueprint específico de un autor")
    @ApiResponse(responseCode = "200", description = "Consulta exitosa")
    @ApiResponse(responseCode = "404", description = "Blueprint no encontrado")
    public ResponseEntity<edu.eci.arsw.blueprints.model.ApiResponse<?>> byAuthorAndName(@PathVariable String author, @PathVariable String bpname) {
        try {
            Blueprint data = services.getBlueprint(author, bpname);
            edu.eci.arsw.blueprints.model.ApiResponse<Blueprint> response = new edu.eci.arsw.blueprints.model.ApiResponse<>(200, "execute ok", data);
            return ResponseEntity.ok(response);
        } catch (BlueprintNotFoundException e) {
            edu.eci.arsw.blueprints.model.ApiResponse<String> response = new edu.eci.arsw.blueprints.model.ApiResponse<>(404, e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    // POST /blueprints
    @PostMapping
    @Operation(summary = "Crear un nuevo blueprint", description = "Agrega un blueprint al sistema")
    @ApiResponse(responseCode = "201", description = "Blueprint creado")
    @ApiResponse(responseCode = "400", description = "Datos inválidos")
    public ResponseEntity<edu.eci.arsw.blueprints.model.ApiResponse<?>> add(@Valid @RequestBody NewBlueprintRequest req) {
        try {
            Blueprint bp = new Blueprint(req.author(), req.name(), req.points());
            services.addNewBlueprint(bp);
            edu.eci.arsw.blueprints.model.ApiResponse<String> response = new edu.eci.arsw.blueprints.model.ApiResponse<>(201, "created", null);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (BlueprintPersistenceException e) {
            edu.eci.arsw.blueprints.model.ApiResponse<String> response = new edu.eci.arsw.blueprints.model.ApiResponse<>(400, e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    // PUT /blueprints/{author}/{bpname}/points
    @PutMapping("/{author}/{bpname}/points")
    @Operation(summary = "Agregar punto a blueprint", description = "Agrega un punto a un blueprint existente")
    @ApiResponse(responseCode = "202", description = "Punto agregado")
    @ApiResponse(responseCode = "404", description = "Blueprint no encontrado")
    public ResponseEntity<edu.eci.arsw.blueprints.model.ApiResponse<?>> addPoint(@PathVariable String author, @PathVariable String bpname,
            @RequestBody Point p) {
        try {
            services.addPoint(author, bpname, p.x(), p.y());
            edu.eci.arsw.blueprints.model.ApiResponse<String> response = new edu.eci.arsw.blueprints.model.ApiResponse<>(202, "accepted", null);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        } catch (BlueprintNotFoundException e) {
            edu.eci.arsw.blueprints.model.ApiResponse<String> response = new edu.eci.arsw.blueprints.model.ApiResponse<>(404, e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    public record NewBlueprintRequest(
            @NotBlank String author,
            @NotBlank String name,
            @Valid java.util.List<Point> points
            ) {

    }
}
