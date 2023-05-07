package handin2;

import handin2.Address;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class HighwayTest {

    //setProperty() is above the maxspeed! Could effect route finding

    @Test
    public void setPropertyTest0() {

        Node n0 = new Node(0,0,0);
        Node n1 = new Node(1,1,0);
        Node n2 = new Node(2.55555555555f,2.555555555555f,0);
        Node n3 = new Node(2.6666666666f,2.66666666666f,0);

        ArrayList<Node> nodes = new ArrayList<Node>();
        nodes.add(n0);
        nodes.add(n1);
        nodes.add(n2);
        nodes.add(n3);

        Highway h = new Highway(nodes, true, true, "", "Adelgade", "motorway", false, "");

        assertEquals(100000, h.showAtZoom);
        assertEquals(110, h.maxSpeed);
        assertEquals(4, h.thickness);
    }

    @Test
    public void setPropertyTest1() {
        ArrayList<Node> nodes = new ArrayList<Node>();

        Highway h = new Highway(nodes, true, true, "null", "Adelgade", "trunk", false, "");

        assertEquals(100000, h.showAtZoom);
        assertEquals(90, h.maxSpeed);
        assertEquals(4, h.thickness);
    }

    @Test
    public void setPropertyTest2() {
        ArrayList<Node> nodes = new ArrayList<Node>();

        Highway h = new Highway(nodes, true, true, "", "Adelgade", "primary", false, "");

        assertEquals(10000, h.showAtZoom);
        assertEquals(80, h.maxSpeed);
        assertEquals(3, h.thickness);
    }

    @Test
    public void setPropertyTest3() {
        ArrayList<Node> nodes = new ArrayList<Node>();

        Highway h = new Highway(nodes, true, true, "", "Adelgade", "secondary", false, "");

        assertEquals(10000, h.showAtZoom);
        assertEquals(80, h.maxSpeed);
        assertEquals (2.5, h.thickness);
    }

    @Test
    public void setPropertyTest4() {
        ArrayList<Node> nodes = new ArrayList<Node>();

        Highway h = new Highway(nodes, true, true, "", "Adelgade", "tertiary", false, "");

        assertEquals(5000, h.showAtZoom);
        assertEquals(80, h.maxSpeed);
        assertEquals(2, h.thickness);
    }

    @Test
    public void setPropertyTest5() {
        ArrayList<Node> nodes = new ArrayList<Node>();

        Highway h = new Highway(nodes, true, true, "", "Adelgade", "unclassified", false, "");

        assertEquals(2000, h.showAtZoom);
        assertEquals(50, h.maxSpeed);
        assertEquals(1, h.thickness);
    }

    @Test
    public void setPropertyTest6() {
        ArrayList<Node> nodes = new ArrayList<Node>();

        Highway h = new Highway(nodes, true, true, "", "Adelgade", "residential", false, "");

        assertEquals(500, h.showAtZoom);
        assertEquals(50, h.maxSpeed);
        assertEquals(1, h.thickness);
    }

    @Test
    public void setPropertyTest7() {
        ArrayList<Node> nodes = new ArrayList<Node>();

        Highway h = new Highway(nodes, true, true, "", "Adelgade", "other", false, "");

        assertEquals(500, h.showAtZoom);
        assertEquals(50, h.maxSpeed);
        assertEquals(1f, h.thickness);
    }

    @Test
    public void constructorTryCatchTest() {
        ArrayList<Node> nodes = new ArrayList<Node>();

        Highway h = new Highway(nodes, true, true, "200", "Adelgade", "other", false, "");

        assertEquals(500, h.showAtZoom);
        assertEquals(200, h.maxSpeed);
        assertEquals(1f, h.thickness);
    }

}