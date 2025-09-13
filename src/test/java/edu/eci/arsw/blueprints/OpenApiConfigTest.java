package edu.eci.arsw.blueprints;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import edu.eci.arsw.blueprints.config.OpenApiConfig;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

class OpenApiConfigTest {

    /*
     * Test to ensure that the OpenAPI bean is correctly configured.
     */
    @Test
    void testApiBeanNotNullAndContainsInfo() {
        OpenApiConfig config = new OpenApiConfig();
        OpenAPI api = config.api();

        assertNotNull(api);
        assertNotNull(api.getInfo());
        Info info = api.getInfo();

        assertEquals("ARSW Blueprints API", info.getTitle());
        assertEquals("v1", info.getVersion());
        assertTrue(info.getDescription().contains("Blueprints Laboratory"));
    }
}
