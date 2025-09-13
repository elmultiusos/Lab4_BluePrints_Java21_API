package edu.eci.arsw.blueprints;

import java.util.Set;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.eci.arsw.blueprints.controllers.BlueprintsAPIController;
import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.services.BlueprintsServices;

@WebMvcTest(BlueprintsAPIController.class)
class BlueprintsAPIControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BlueprintsServices services;

    @Autowired
    private ObjectMapper objectMapper;

    /* 
     * Prueba el endpoint GET /blueprints para obtener todos los blueprints.
     * Verifica que la respuesta tenga un código de estado 200 y que el cuerpo
     * contenga la clave "code" con valor 200.
     */
    @Test
    void testGetAllBlueprintsReturns200() throws Exception {
        Blueprint bp = new Blueprint("john", "house",
                java.util.List.of(new Point(0, 0), new Point(1, 1)));

        when(services.getAllBlueprints()).thenReturn(Set.of(bp));

        mockMvc.perform(get("/api/v1/blueprints"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data[0].author").value("john"))
                .andExpect(jsonPath("$.data[0].name").value("house"));
    }

    /* 
     * Prueba el endpoint POST /blueprints para agregar un nuevo blueprint.
     * Verifica que la respuesta tenga un código de estado 201 y que el cuerpo
     * contenga la clave "code" con valor 201.
     */
    @Test
    void testAddNewBlueprintReturns201() throws Exception {
        BlueprintsAPIController.NewBlueprintRequest req
                = new BlueprintsAPIController.NewBlueprintRequest("john", "garage",
                        java.util.List.of(new Point(5, 5)));

        mockMvc.perform(post("/api/v1/blueprints")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value(201));
    }
}
