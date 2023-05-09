package GoKort;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;

import GoKort.Objects.Node;
import GoKort.Objects.Way;


public class WayTest {

    Way w0;
    Way w1;

    Node n0 = new Node(0,0,0);
    Node n1 = new Node(1,1,0);
    Node n2 = new Node(2.55555555555f,2.555555555555f,0);
    Node n3 = new Node(2.6666666666f,2.66666666666f,0);

    @Test
    public void overridedEqualsTest0() {
   
        ArrayList<Node> nodes0 = new ArrayList<Node>();
        ArrayList<Node> nodes1 = new ArrayList<Node>();

        nodes0.add(n1);
        nodes1.add(n1);

        w0 = new Way(nodes0);
        w1 = new Way(nodes1);

        boolean expected = true;
        boolean actual = w0.equals(w1);

        assertEquals(expected, actual);
    }

    @Test
    public void overridedEqualsTest1() {

        ArrayList<Node> nodes = new ArrayList<Node>();

        nodes.add(n0);
        nodes.add(n1);

        w0 = new Way(nodes);
        w1 = new Way(nodes);

        boolean expected = true;
        boolean actual = w0.equals(w0);

        assertEquals(expected, actual);
       }


    @Test
    public void overridedEqualsTest2() {
   
        ArrayList<Node> nodes0 = new ArrayList<Node>();
        ArrayList<Node> nodes1 = new ArrayList<Node>();

        nodes0.add(n0);
        nodes0.add(n0);
        nodes1.add(n1);

        w0 = new Way(nodes0);
        w1 = new Way(nodes1);

        boolean expected = false;
        boolean actual = w0.equals(w1);

        assertEquals(expected, actual);
    }

    @Test
    public void overridedEqualsTest3() {
   
        ArrayList<Node> nodes0 = new ArrayList<Node>();
        

        nodes0.add(n0);
        nodes0.add(n0);

        w0 = new Way(nodes0);

        boolean expected = false;
        boolean actual = w0.equals(n0);

        assertEquals(expected, actual);
    }

    @Test
    public void overridedEqualsTest4() {
   
        ArrayList<Node> nodes0 = new ArrayList<Node>();
        ArrayList<Node> nodes1 = new ArrayList<Node>();

        nodes0.add(n2);
        nodes1.add(n3);

        w0 = new Way(nodes0);
        w1 = new Way(nodes1);

        boolean expected = false;
        boolean actual = w0.equals(w1);

        assertEquals(expected, actual);
    }

}