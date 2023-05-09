package handin2;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import handin2.Objects.Node;

public class NodeTest {

    @Test
    public void getLatTest() {

            Node nd = new Node(0, 0, 0);
            
            float expected = 0;
            float actual = nd.getLat();

            assertEquals(expected, actual);
       }

    @Test
    public void getLonTest() {

            Node nd = new Node(0, 0, 0);
            
            float expected = 0;
            float actual = nd.getLat();

            assertEquals(expected, actual);
    }

    @Test
    public void isNodeEqualTest() {

            Node nd0 = new Node(0, 0, 0);
            Node nd1 = new Node(0, 0, 0);
            
            boolean expected = nd0.isNodeEqual(nd1);
            boolean actual = true;

            assertEquals(expected, actual);
    }
}
