package handin2;

import org.junit.jupiter.api.Test;

import handin2.DataStructures.RTree;
import handin2.Objects.Node;
import handin2.Objects.Way;

import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class RTreeTest {

    @Test
    public void isOverLappingTest1() { // Identical rectangles
        RTree rtree = new RTree();
        Node node1 = new Node(55.0f, 14.0f, 1);
        Node node2 = new Node(54.0f, 16.0f, 2);
        Node node3 = new Node(56.0f, 15.0f, 3);
        Node node4 = new Node(55.0f, 15.0f, 4);
        ArrayList<Node> list = new ArrayList<>();
        list.add(node1);
        list.add(node2);
        list.add(node3);
        list.add(node4);
        Way way = new Way(list);

        ArrayList<Way> expected = new ArrayList<>();
        expected.add(way);

        float[] searchField = new float[] { 55.0f, 56.0f, 14.0f, 15.0f };

        rtree.insert(way);

        assertEquals(expected, rtree.search(searchField));

    }

    // Test 2-7 tests rectangles which are identical on Lons, but different versions
    // of overlaps on lats.
    @Test
    public void isOverLappingTest2() { // Lon identical. -1-1
        RTree rtree = new RTree();
        Node node1 = new Node(55.0f, 14.0f, 1);
        Node node2 = new Node(54.0f, 16.0f, 2);
        Node node3 = new Node(56.0f, 15.0f, 3);
        Node node4 = new Node(55.0f, 15.0f, 4);
        ArrayList<Node> list = new ArrayList<>();
        list.add(node1);
        list.add(node2);
        list.add(node3);
        list.add(node4);
        Way way = new Way(list);

        ArrayList<Way> expected = new ArrayList<>();

        float[] searchField = new float[] { 53.0f, 53.5f, 14.0f, 15.0f };

        rtree.insert(way);

        assertEquals(expected, rtree.search(searchField));

    }

    @Test
    public void isOverLappingTest3() { // Lon identical. -1 0
        RTree rtree = new RTree();
        Node node1 = new Node(55.0f, 14.0f, 1);
        Node node2 = new Node(54.0f, 16.0f, 2);
        Node node3 = new Node(56.0f, 15.0f, 3);
        Node node4 = new Node(55.0f, 15.0f, 4);
        ArrayList<Node> list = new ArrayList<>();
        list.add(node1);
        list.add(node2);
        list.add(node3);
        list.add(node4);
        Way way = new Way(list);

        ArrayList<Way> expected = new ArrayList<>();
        expected.add(way);

        float[] searchField = new float[] { 53.0f, 55.5f, 14.0f, 15.0f };

        rtree.insert(way);

        assertEquals(expected, rtree.search(searchField));

    }

    @Test
    public void isOverLappingTest4() { // Lon identical. 0 0
        RTree rtree = new RTree();
        Node node1 = new Node(55.0f, 14.0f, 1);
        Node node2 = new Node(54.0f, 16.0f, 2);
        Node node3 = new Node(56.0f, 15.0f, 3);
        Node node4 = new Node(55.0f, 15.0f, 4);
        ArrayList<Node> list = new ArrayList<>();
        list.add(node1);
        list.add(node2);
        list.add(node3);
        list.add(node4);
        Way way = new Way(list);

        ArrayList<Way> expected = new ArrayList<>();
        expected.add(way);
        float[] searchField = new float[] { 55.3f, 55.5f, 14.0f, 15.0f };
        rtree.insert(way);

        assertEquals(expected, rtree.search(searchField));
    }

    @Test
    public void isOverLappingTest5() { // Lon identical. 0 1
        RTree rtree = new RTree();
        Node node1 = new Node(55.0f, 14.0f, 1);
        Node node2 = new Node(54.0f, 16.0f, 2);
        Node node3 = new Node(56.0f, 15.0f, 3);
        Node node4 = new Node(55.0f, 15.0f, 4);
        ArrayList<Node> list = new ArrayList<>();
        list.add(node1);
        list.add(node2);
        list.add(node3);
        list.add(node4);
        Way way = new Way(list);

        ArrayList<Way> expected = new ArrayList<>();
        expected.add(way);
        float[] searchField = new float[] { 55.3f, 56.5f, 14.0f, 15.0f };
        rtree.insert(way);

        assertEquals(expected, rtree.search(searchField));
    }

    @Test
    public void isOverLappingTest6() { // Lon identical. 11
        RTree rtree = new RTree();
        Node node1 = new Node(55.0f, 14.0f, 1);
        Node node2 = new Node(54.0f, 16.0f, 2);
        Node node3 = new Node(56.0f, 15.0f, 3);
        Node node4 = new Node(55.0f, 15.0f, 4);
        ArrayList<Node> list = new ArrayList<>();
        list.add(node1);
        list.add(node2);
        list.add(node3);
        list.add(node4);
        Way way = new Way(list);

        ArrayList<Way> expected = new ArrayList<>();
        float[] searchField = new float[] { 56.3f, 56.5f, 14.0f, 15.0f };
        rtree.insert(way);

        assertEquals(expected, rtree.search(searchField));
    }

    @Test
    public void isOverLappingTest7() { // Lons identical. -1 1
        RTree rtree = new RTree();
        Node node1 = new Node(55.0f, 14.0f, 1);
        Node node2 = new Node(54.0f, 16.0f, 2);
        Node node3 = new Node(56.0f, 15.0f, 3);
        Node node4 = new Node(55.0f, 15.0f, 4);
        ArrayList<Node> list = new ArrayList<>();
        list.add(node1);
        list.add(node2);
        list.add(node3);
        list.add(node4);
        Way way = new Way(list);

        ArrayList<Way> expected = new ArrayList<>();
        expected.add(way);
        float[] searchField = new float[] { 53.3f, 56.5f, 14.0f, 15.0f };
        rtree.insert(way);

        assertEquals(expected, rtree.search(searchField));
    }

    // Test 8-13 tests rectangles which are identical on Lats, but different
    // versions of overlaps on lons.
    @Test
    public void isOverLappingTest8() { // Lats identical. -1-1
        RTree rtree = new RTree();
        Node node1 = new Node(55.0f, 14.0f, 1);
        Node node2 = new Node(54.0f, 16.0f, 2);
        Node node3 = new Node(56.0f, 15.0f, 3);
        Node node4 = new Node(55.0f, 15.0f, 4);
        ArrayList<Node> list = new ArrayList<>();
        list.add(node1);
        list.add(node2);
        list.add(node3);
        list.add(node4);
        Way way = new Way(list);

        ArrayList<Way> expected = new ArrayList<>();

        float[] searchField = new float[] { 55.0f, 56.0f, 12.0f, 13.0f };

        rtree.insert(way);

        assertEquals(expected, rtree.search(searchField));

    }

    @Test
    public void isOverLappingTest9() { // Lats identical. -1 0
        RTree rtree = new RTree();
        Node node1 = new Node(55.0f, 14.0f, 1);
        Node node2 = new Node(54.0f, 16.0f, 2);
        Node node3 = new Node(56.0f, 15.0f, 3);
        Node node4 = new Node(55.0f, 15.0f, 4);
        ArrayList<Node> list = new ArrayList<>();
        list.add(node1);
        list.add(node2);
        list.add(node3);
        list.add(node4);
        Way way = new Way(list);

        ArrayList<Way> expected = new ArrayList<>();
        expected.add(way);

        float[] searchField = new float[] { 55.0f, 56.0f, 12.0f, 15.0f };

        rtree.insert(way);

        assertEquals(expected, rtree.search(searchField));

    }

    @Test
    public void isOverLappingTest10() { // Lats identical. 0 0
        RTree rtree = new RTree();
        Node node1 = new Node(55.0f, 14.0f, 1);
        Node node2 = new Node(54.0f, 16.0f, 2);
        Node node3 = new Node(56.0f, 15.0f, 3);
        Node node4 = new Node(55.0f, 15.0f, 4);
        ArrayList<Node> list = new ArrayList<>();
        list.add(node1);
        list.add(node2);
        list.add(node3);
        list.add(node4);
        Way way = new Way(list);

        ArrayList<Way> expected = new ArrayList<>();
        expected.add(way);
        float[] searchField = new float[] { 55.0f, 56.0f, 15.0f, 15.5f };
        rtree.insert(way);

        assertEquals(expected, rtree.search(searchField));
    }

    @Test
    public void isOverLappingTest11() { // Lats identical. 0 1
        RTree rtree = new RTree();
        Node node1 = new Node(55.0f, 14.0f, 1);
        Node node2 = new Node(54.0f, 16.0f, 2);
        Node node3 = new Node(56.0f, 15.0f, 3);
        Node node4 = new Node(55.0f, 15.0f, 4);
        ArrayList<Node> list = new ArrayList<>();
        list.add(node1);
        list.add(node2);
        list.add(node3);
        list.add(node4);
        Way way = new Way(list);

        ArrayList<Way> expected = new ArrayList<>();
        expected.add(way);
        float[] searchField = new float[] { 55.0f, 56.0f, 15.0f, 17.0f };
        rtree.insert(way);

        assertEquals(expected, rtree.search(searchField));
    }

    @Test
    public void isOverLappingTest12() { // Lats identical. 11
        RTree rtree = new RTree();
        Node node1 = new Node(55.0f, 14.0f, 1);
        Node node2 = new Node(54.0f, 16.0f, 2);
        Node node3 = new Node(56.0f, 15.0f, 3);
        Node node4 = new Node(55.0f, 15.0f, 4);
        ArrayList<Node> list = new ArrayList<>();
        list.add(node1);
        list.add(node2);
        list.add(node3);
        list.add(node4);
        Way way = new Way(list);

        ArrayList<Way> expected = new ArrayList<>();
        float[] searchField = new float[] { 55.0f, 56.0f, 17.0f, 18.0f };
        rtree.insert(way);

        assertEquals(expected, rtree.search(searchField));
    }

    @Test
    public void isOverLappingTest13() { // Lats identical. -1 1
        RTree rtree = new RTree();
        Node node1 = new Node(55.0f, 14.0f, 1);
        Node node2 = new Node(54.0f, 16.0f, 2);
        Node node3 = new Node(56.0f, 15.0f, 3);
        Node node4 = new Node(55.0f, 15.0f, 4);
        ArrayList<Node> list = new ArrayList<>();
        list.add(node1);
        list.add(node2);
        list.add(node3);
        list.add(node4);
        Way way = new Way(list);

        ArrayList<Way> expected = new ArrayList<>();
        expected.add(way);
        float[] searchField = new float[] { 55.0f, 56.0f, 13.0f, 17.0f };
        rtree.insert(way);

        assertEquals(expected, rtree.search(searchField));
    }
}