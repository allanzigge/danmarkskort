package handin2;

import org.junit.jupiter.api.Test;


import handin2.Pathfinding.Edge;
import handin2.Pathfinding.Pathfinder;

import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class PathFinderTest {
    String filename = "src/test/java/handin2/pathFinderTestFile.osm.zip";

    @Test
    public void pathFinderSearch1() throws Exception {
        Model model = new Model(filename);
        Pathfinder pathfinder = new Pathfinder(model.vertexMap);
        List<Edge> result = pathfinder.findPathCar(model.vertexMap.get(Long.parseLong("2")),
                model.vertexMap.get(Long.parseLong("7")));
        String resultString = "";
        for (Edge edge : result) {
            resultString = resultString + edge.getRoad().getWayId() + " ";
        }
        String expected = "12 121 1112 1011 910 89 78 ";

        assertEquals(expected, resultString);
    }

}