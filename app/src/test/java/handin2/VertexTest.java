package handin2;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

//this test class was used under the development of Vertices

public class VertexTest {

    //Confrims that all the expected Vertices acual is in the vertexMap
    @Test
    void addVertexTest() throws Exception {

        Model model = new Model("data/vertexTest.osm.zip");

        Long[] actualArray = new Long[model.vertexMap.size()];
        Long counter = 1L;
        for (int i = 0; i < model.vertexMap.size(); i++) {
            boolean nullpointer = true;
            while (nullpointer) {
                boolean beenCatched = false;
                try {
                    model.vertexMap.get(counter).getVertexID();
                } catch (NullPointerException e) {
                    beenCatched = true;
                }
                if (beenCatched) {
                    counter++;
                } else {
                    nullpointer = false;
                }
            }
            actualArray[i] = model.vertexMap.get(counter).getVertexID();
            counter++;
        }

        Long[] expectedArray = new Long[] { 1L, 3L, 5L, 6L, 9L, 10L, 11L, 13L, 18L };
        assertArrayEquals(expectedArray, actualArray);
    }

    // Was used to confirm that the logic to fill the vertesMap with someething
    // (hopefully vertices) was working
    @Test
    void isVertexMapNotEmpty() throws Exception {
        Model model = new Model("data/vertexTest.osm.zip");
        assertTrue(!model.vertexMap.isEmpty());
    }

    // Used to confirm the suspected amount of vertices that should be "registered"
    @Test
    void sizeOfVertexMap() throws Exception {
        Model model = new Model("data/vertexTest.osm.zip");
        assertEquals(9, model.vertexMap.size());
    }

    // Used to verify single vertoces in the vertexMap
    @Test
    void lookAtOneVertexInVertexMap() throws Exception {
        Model model = new Model("data/vertexTest.osm.zip");
        assertEquals(18L, model.vertexMap.get(18L).getVertexID());
    }
}