package edu.eci.arsw.blueprints;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import edu.eci.arsw.blueprints.filters.IdentityFilter;
import edu.eci.arsw.blueprints.filters.RedundancyFilter;
import edu.eci.arsw.blueprints.filters.UndersamplingFilter;
import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;

class FiltersTest {

    /* 
     * Test to ensure that the IdentityFilter returns the same blueprint.
     */
    @Test
    void testIdentityFilterReturnsSameBlueprint() {
        Blueprint bp = new Blueprint("john", "test", List.of(new Point(1, 1)));
        IdentityFilter f = new IdentityFilter();
        Blueprint result = f.apply(bp);
        assertEquals(bp, result);
    }

    /* 
     * Test to ensure that the RedundancyFilter removes consecutive duplicate points.
     */
    @Test
    void testRedundancyFilterRemovesConsecutiveDuplicates() {
        Blueprint bp = new Blueprint("john", "dup",
                List.of(new Point(1, 1), new Point(1, 1), new Point(2, 2)));
        RedundancyFilter f = new RedundancyFilter();
        Blueprint result = f.apply(bp);
        assertEquals(2, result.getPoints().size());
    }

    /* 
     * Test to ensure that the UndersamplingFilter keeps every other point.
     */
    @Test
    void testUndersamplingFilterKeepsEveryOtherPoint() {
        Blueprint bp = new Blueprint("john", "sample",
                List.of(new Point(1, 1), new Point(2, 2), new Point(3, 3), new Point(4, 4)));
        UndersamplingFilter f = new UndersamplingFilter();
        Blueprint result = f.apply(bp);
        assertEquals(2, result.getPoints().size());
        assertEquals(new Point(1, 1), result.getPoints().get(0));
        assertEquals(new Point(3, 3), result.getPoints().get(1));
    }
}
