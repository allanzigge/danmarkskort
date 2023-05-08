package handin2;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

class relationTest {
   Node n1 = new Node(0, 0, 1);
   Node n2 = new Node(0, 8, 2);
   Node n3 = new Node(8, 8, 3);
   Node n4 = new Node(8, 0, 4);
   Node n5 = new Node(1, 1, 5);
   Node n6 = new Node(1, 2, 6);
   Node n7 = new Node(4, 2, 7);
   Node n8 = new Node(4, 1, 8);
   Node n9 = new Node(3, 4, 9);
   Node n10 = new Node(3, 7, 10);
   Node n11 = new Node(6, 7, 11);
   Node n12 = new Node(6, 4, 12);

   // We ran into an infinite while loop, and this test was to test if our
   // iteration had help the problem
   @Test
   void OuterListCreateLoop() {

      // dthe first
      ArrayList<Node> listNode1 = new ArrayList<>();
      listNode1.add(n1);
      listNode1.add(n2);
      listNode1.add(n3);

      // second way
      ArrayList<Node> listNode2 = new ArrayList<>();
      // node n4 is missing here to envoke the inifite while loop (relation linje 54)
      listNode2.add(n8);
      listNode2.add(n7);
      listNode2.add(n1);

      // third way
      ArrayList<Node> listNode3 = new ArrayList<>();
      listNode3.add(n3);
      listNode3.add(n11);
      listNode3.add(n4);

      ArrayList<List<Node>> outerList = new ArrayList<>();
      outerList.add(listNode1);
      outerList.add(listNode3);
      outerList.add(listNode2);

      assertTimeout(Duration.ofSeconds(7), () -> {
      });
   }

   @Test
   void twoWaysShouldBecomeOne() {

      // den ene way
      ArrayList<Node> listNode1 = new ArrayList<>();
      listNode1.add(n1);
      listNode1.add(n2);
      listNode1.add(n3);

      // den anden way
      ArrayList<Node> listNode2 = new ArrayList<>();
      listNode2.add(n3);
      listNode2.add(n4);
      listNode2.add(n1);

      ArrayList<List<Node>> outerList = new ArrayList<>();
      outerList.add(listNode1);
      outerList.add(listNode2);

      // forventet liste af noder
      ArrayList<Node> expectedNodeList = new ArrayList<>();
      expectedNodeList.add(n1);
      expectedNodeList.add(n2);
      expectedNodeList.add(n3);
      expectedNodeList.add(n3);
      expectedNodeList.add(n4);
      expectedNodeList.add(n1);

      String expectedString = "";
      String actualString = "";

      for(Node node : expectedNodeList){
         expectedString = expectedString + node.toString();
      }

      Relation rel = new Relation(outerList, new ArrayList<List<Node>>(), "hej");

      for(Node node : rel.relationTrace){
         actualString = actualString + node.toString();
      }
      assertEquals(expectedString, actualString);
   }

   @Test
   void twoWaysShouldBecomeOneReverse() {
      // den ene way
      ArrayList<Node> listNode1 = new ArrayList<>();
      listNode1.add(n1);
      listNode1.add(n2);
      listNode1.add(n3);

      // den anden way, som vender den 'forkerte' vej
      ArrayList<Node> listNode2 = new ArrayList<>();
      listNode2.add(n1);
      listNode2.add(n4);
      listNode2.add(n3);

      // den samlede liste af de to ways
      ArrayList<List<Node>> outerList = new ArrayList<>();
      outerList.add(listNode1);
      outerList.add(listNode2);

      // forventet liste af noder
      ArrayList<Node> expectedNodeList = new ArrayList<>();
      expectedNodeList.add(n1);
      expectedNodeList.add(n2);
      expectedNodeList.add(n3);
      expectedNodeList.add(n3);
      expectedNodeList.add(n4);
      expectedNodeList.add(n1);
      String expectedString = "";
      String actualString = "";

      for(Node node : expectedNodeList){
         expectedString = expectedString + node.toString();
      }
      Relation rel = new Relation(outerList, new ArrayList<List<Node>>(), "hej");
      for(Node node : rel.relationTrace){
         actualString = actualString + node.toString();
      }
      assertEquals(expectedString, actualString);
   }

   @Test
   void emptyList() {

      // an empty outerList
      ArrayList<List<Node>> outerList = new ArrayList<>();

      Relation rel = new Relation(outerList, new ArrayList<List<Node>>(), "hej");

      // The resault equals null, since the solution to parsing an empty outerlist is
      // to
      // catch the IndexOutOfBoundsException and make relationTrace = null (Relation
      // line 24-28)
      assertTrue(rel.relationTrace == null);
   }

   @Test
   void oneInnerList() {

      // den ene way
      ArrayList<Node> listNode1 = new ArrayList<>();
      listNode1.add(n1);
      listNode1.add(n2);
      listNode1.add(n3);

      // Den anden way
      ArrayList<Node> listNode2 = new ArrayList<>();
      listNode2.add(n3);
      listNode2.add(n4);
      listNode2.add(n1);

      // Way der bruges som inner
      ArrayList<Node> listNode3 = new ArrayList<>();
      listNode3.add(n5);
      listNode3.add(n6);
      listNode3.add(n7);
      listNode3.add(n8);
      listNode3.add(n5);

      // Liste af outerways
      ArrayList<List<Node>> outerList = new ArrayList<>();
      outerList.add(listNode1);
      outerList.add(listNode2);

      // Liste af innerways
      ArrayList<List<Node>> innerList = new ArrayList<>();
      innerList.add(listNode3);

      // Forventet liste af noder
      ArrayList<Node> expectedNodeList = new ArrayList<>();
      expectedNodeList.add(n1);
      expectedNodeList.add(n2);
      expectedNodeList.add(n3);
      expectedNodeList.add(n3);
      expectedNodeList.add(n4);
      expectedNodeList.add(n1);
      expectedNodeList.add(n5);
      expectedNodeList.add(n6);
      expectedNodeList.add(n7);
      expectedNodeList.add(n8);
      expectedNodeList.add(n5);
      expectedNodeList.add(n1);
      
      String expectedString = "";
      String actualString = "";

      for(Node node : expectedNodeList){
         expectedString = expectedString + node.toString();
      }


      Relation rel = new Relation(outerList, innerList, "hej");

      for(Node node : rel.relationTrace){
         actualString = actualString + node.toString();
      }
      assertEquals(expectedString, actualString);
   }

   @Test
   void twoInnerLists() {

      // den ene way
      ArrayList<Node> listNode1 = new ArrayList<>();
      listNode1.add(n1);
      listNode1.add(n2);
      listNode1.add(n3);

      // Den anden way
      ArrayList<Node> listNode2 = new ArrayList<>();
      listNode2.add(n3);
      listNode2.add(n4);
      listNode2.add(n1);

      // Way der bruges som inner
      ArrayList<Node> listNode3 = new ArrayList<>();
      listNode3.add(n5);
      listNode3.add(n6);
      listNode3.add(n7);
      listNode3.add(n8);
      listNode3.add(n5);

      // Anden Way der bruegs som inner
      ArrayList<Node> listNode4 = new ArrayList<>();
      listNode4.add(n9);
      listNode4.add(n10);
      listNode4.add(n11);
      listNode4.add(n12);
      listNode4.add(n9);

      // Liste af outerways
      ArrayList<List<Node>> outerList = new ArrayList<>();
      outerList.add(listNode1);
      outerList.add(listNode2);

      // Liste af innerways
      ArrayList<List<Node>> innerList = new ArrayList<>();
      innerList.add(listNode3);
      innerList.add(listNode4);

      // Forventet liste af noder
      ArrayList<Node> expectedNodeList = new ArrayList<>();
      expectedNodeList.add(n1);
      expectedNodeList.add(n2);
      expectedNodeList.add(n3);
      expectedNodeList.add(n3);
      expectedNodeList.add(n4);
      expectedNodeList.add(n1);
      expectedNodeList.add(n5);
      expectedNodeList.add(n6);
      expectedNodeList.add(n7);
      expectedNodeList.add(n8);
      expectedNodeList.add(n5);
      expectedNodeList.add(n1);
      expectedNodeList.add(n9);
      expectedNodeList.add(n10);
      expectedNodeList.add(n11);
      expectedNodeList.add(n12);
      expectedNodeList.add(n9);
      expectedNodeList.add(n1);

      String expectedString = "";
      String actualString = "";

      for(Node node : expectedNodeList){
         expectedString = expectedString + node.toString();
      }

      Relation rel = new Relation(outerList, innerList, "hej");

      for(Node node : rel.relationTrace){
         actualString = actualString + node.toString();
      }
      assertEquals(expectedString, actualString);
   }

}
