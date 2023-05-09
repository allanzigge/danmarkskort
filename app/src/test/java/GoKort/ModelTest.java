package GoKort;

import org.junit.jupiter.api.Test;


import GoKort.Objects.Node;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

public class ModelTest {
    String filename = "src/test/java/handin2/modelTestFile.osm";

    @Test
    public void testParseOSM() throws Exception {
        Model model = new Model(filename);
        assertEquals(1.0, model.getMaxlat(), 0.0);
        assertEquals(0.0, model.getMinlat(), 0.0);
        assertEquals(1.0, model.maxlon, 0.0);
        assertEquals(0.0, model.getMinlon(), 0.0);
        //assertEquals(1, model.ways.size());
        // assertEquals(0, model.landuseRelations.size());
        // assertEquals(0, model.natRelations.size());
        List<Node> wayNodes = model.id2way.get("2");
        assertNotNull(wayNodes);
    }
    
}
