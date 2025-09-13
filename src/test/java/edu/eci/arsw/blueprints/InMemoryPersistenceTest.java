package edu.eci.arsw.blueprints;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import edu.eci.arsw.blueprints.persistence.InMemoryBlueprintPersistence;

class InMemoryPersistenceTest {

    private final InMemoryBlueprintPersistence repo = new InMemoryBlueprintPersistence();

    @Test
    void saveBlueprint_existing_throwsException() {
        Blueprint bp = new Blueprint("john", "house", List.of(new Point(1, 1)));
        assertThrows(BlueprintPersistenceException.class, () -> repo.saveBlueprint(bp));
    }

    @Test
    void getBlueprint_nonExisting_throwsException() {
        assertThrows(BlueprintNotFoundException.class,
                () -> repo.getBlueprint("ghost", "none"));
    }

    @Test
    void getBlueprintsByAuthor_nonExisting_throwsException() {
        assertThrows(BlueprintNotFoundException.class,
                () -> repo.getBlueprintsByAuthor("nobody"));
    }

    @Test
    void addPoint_nonExisting_throwsException() {
        assertThrows(BlueprintNotFoundException.class,
                () -> repo.addPoint("ghost", "none", 5, 5));
    }
}
