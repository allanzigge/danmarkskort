package handin2;

import org.junit.jupiter.api.Test;
import com.google.common.annotations.VisibleForTesting;
import static org.junit.jupiter.api.Assertions.*;
import java.io.FileInputStream;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import java.util.*;


public class NNSearchTest {



    
    @Test
    public void nearestTest1() { //Identical rectangles
        RTree rtree = new RTree();
        Node node1 = new Node(5.0f,3.0f,1);
        Node node2 = new Node(4.0f,4.0f,2);
        ArrayList<Node> list1 = new ArrayList();
        list1.add(node1);
        list1.add(node2);
        Way way1 = new Way(list1);
        rtree.insert(way1);

        Node node3 = new Node(12.0f,4.0f,3);
        Node node4 = new Node(4.0f,12.0f,4);
        ArrayList<Node> list2 = new ArrayList();
        list2.add(node3);
        list2.add(node4);
        Way way2 = new Way(list2);
        rtree.insert(way2);
        
        Node node5 = new Node(12.0f,7.0f,5);
        Node node6 = new Node(11.0f,8.0f,6);
        Node node7 = new Node(12.0f,9.0f,7);
        Node node8 = new Node(13.0f,8.0f,8);
        ArrayList<Node> list3 = new ArrayList();
        list3.add(node5);
        list3.add(node6);
        list3.add(node7);
        list3.add(node8);
        Way way3 = new Way(list3);
        rtree.insert(way3);

        float[] searchPoint = new float[]{5.0f,5.0f};

        assertEquals(way1, rtree.NNSearch(searchPoint));
    }

    @Test
    public void nearestTest2() { //Identical rectangles
        RTree rtree = new RTree();
        Way way1 = new Way( new ArrayList<>() {{add(new Node(15f,1f,1)); add(new Node(14f,2f,1));}});
        Way way2 = new Way( new ArrayList<>() {{add(new Node(10f,2f,1)); add(new Node(9f,3f,1));}});
        Way way3 = new Way( new ArrayList<>() {{add(new Node(7f,15f,1)); add(new Node(6f,15f,1));}});
        Way way4 = new Way( new ArrayList<>() {{add(new Node(4f,2f,1)); add(new Node(3f,2f,1));}});
        Way way5 = new Way( new ArrayList<>() {{add(new Node(2f,1f,1)); add(new Node(1f,2f,1));}});
        Way way6 = new Way( new ArrayList<>() {{add(new Node(15f,13f,1)); add(new Node(14f,14f,1));}});
        Way way7 = new Way( new ArrayList<>() {{add(new Node(7f,5f,1)); add(new Node(6f,4f,1));}});
        Way way8 = new Way( new ArrayList<>() {{add(new Node(3f,8f,1)); add(new Node(3f,9f,1));}});
        Way way9 = new Way( new ArrayList<>() {{add(new Node(15f,18f,1)); add(new Node(14f,18f,1));}});
        Way way10 = new Way( new ArrayList<>() {{add(new Node(11f,13f,1)); add(new Node(10f,13f,1));}});
        rtree.insert(way1);
        rtree.insert(way2);
        rtree.insert(way3);
        rtree.insert(way4);
        rtree.insert(way5);
        rtree.insert(way6);
        rtree.insert(way7);
        rtree.insert(way8);
        rtree.insert(way9);
        rtree.insert(way10);

        float[] searchPoint1 = new float[]{17f,2f};
        assertEquals(way1,rtree.NNSearch(searchPoint1));

        float[] searchPoint2 = new float[]{9f,2f};
        assertEquals(way2,rtree.NNSearch(searchPoint2));

        float[] searchPoint3 = new float[]{7f,16f};
        assertEquals(way3,rtree.NNSearch(searchPoint3));

        float[] searchPoint4 = new float[]{4f,3f};
        assertEquals(way4,rtree.NNSearch(searchPoint4));

        float[] searchPoint5 = new float[]{1f,5f};
        assertEquals(way5,rtree.NNSearch(searchPoint5));

        float[] searchPoint6 = new float[]{16f,14f};
        assertEquals(way6,rtree.NNSearch(searchPoint6));

        float[] searchPoint8 = new float[]{2f,9f};
        assertEquals(way8,rtree.NNSearch(searchPoint8));

        float[] searchPoint9 = new float[]{15f,17f};
        assertEquals(way9,rtree.NNSearch(searchPoint9));

        float[] searchPoint10 = new float[]{10f,11f};
        assertEquals(way10,rtree.NNSearch(searchPoint10));

        // float[] searchPoint11 = new float[]{100f,5.9f};
        // assertEquals(way5,rtree.NNSearch(searchPoint11));

        // float[] searchPoint12 = new float[]{1f,7f};
        // assertEquals(way5,rtree.NNSearch(searchPoint12));


        
    }
}
