package edu.eci.arsw.blueprints;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.services.BlueprintsServices;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BlueprintsIntegrationTest {

    @Autowired
    private BlueprintsServices services;
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String url(String path) {
        return "http://localhost:" + port + "/api/v1/blueprints" + path;
    }

    /* 
     * Verifica que el contexto de Spring se cargue correctamente y que el
     * servicio de blueprints no sea nulo.
     */
    @Test
    void contextLoads() {
        assertThat(services).isNotNull();
        assertThat(services.getAllBlueprints()).isNotEmpty();
    }

    /* 
     * Prueba el endpoint GET /blueprints para obtener todos los blueprints.
     * Verifica que la respuesta tenga un código de estado 200 y que el cuerpo
     * contenga la clave "code" con valor 200.
     */
    @Test
    void testGetAllBlueprints() {
        ResponseEntity<Map> response
                = restTemplate.getForEntity(url(""), Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsKey("code");
        assertThat(response.getBody().get("code")).isEqualTo(200);
    }

    /* 
     * Prueba el endpoint POST /blueprints para crear un nuevo blueprint.
     * Verifica que la respuesta tenga un código de estado 201 y que el cuerpo
     * contenga la clave "code" con valor 201.
     */
    @Test
    void testCreateNewBlueprint() {
        Blueprint bp = new Blueprint("john", "labTest",
                List.of(new Point(10, 10), new Point(20, 20)));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Blueprint> request = new HttpEntity<>(bp, headers);

        ResponseEntity<Map> response
                = restTemplate.postForEntity(url(""), request, Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().get("code")).isEqualTo(201);
    }
}
