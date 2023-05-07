package handin2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class RTree implements Serializable {
    Boolean isEmpty = true;
    private int dims = 2; // dimentions
    private Node root;
    private int M = 50; //Maximum amount of children pr. Node
    private int m = (M*40)/100;
    private float[] nearPoint;
    private Map<String, ArrayList<handin2.Node>> id2way;

    public RTree() {
        root = new Node(true);
    }

    // Helper class used for testing
    public void setMax(int M) {
        this.M = M;
        this.m = (M*40)/100;
    }


    // Helper class
    private class Node implements Serializable {

        private boolean isLeaf;
        private List<Node> children;
        private float[] mbr = new float[dims * 2]; // Minimum bound rectangle of either node or object. (minLat, minLon,
                                                   // maxLat,maxLon)
        private Node parrent;

        public Node(boolean isLeaf) {
            this.isLeaf = isLeaf;
            this.children = new LinkedList<Node>();
            mbrCalculator();
        }

        public void mbrCalculator() {
            float[] mbr = new float[dims * 2];
            float minLon = Float.MAX_VALUE;
            float minLat = Float.MAX_VALUE;
            float maxLon = Float.MIN_VALUE;
            float maxLat = Float.MIN_VALUE;

            for (Node node : children) {
                if (node.mbr[0] < minLat) {
                    minLat = node.mbr[0];
                }
                if (node.mbr[1] < minLon) {
                    minLon = node.mbr[1];
                }
                if (node.mbr[2] > maxLat) {
                    maxLat = node.mbr[2];
                }
                if (node.mbr[3] > maxLon) {
                    maxLon = node.mbr[3];
                }
            }
            mbr[0] = minLat;
            mbr[1] = minLon;
            mbr[2] = maxLat;
            mbr[3] = maxLon;

            this.mbr = mbr;
        }

        public void setParrent(Node node) {
            parrent = node;
        }

        public void add(Node entry) {
            children.add(entry);
            mbrCalculator();
        }

        public int size() {
            return children.size();
        }

        public void clear() {
            children.clear();
        }

        // Searches for the objects in the leafs, that is included in the given
        // seachfield (window)
        private List<Way> nodeSearcher(float[] searchField) {
            List<Way> searchResult = new ArrayList<Way>();
            if (!isLeaf) {
                for (Node node : children) {
                    if (node.isOverlapping(searchField)) {
                        for (Way object : node.nodeSearcher(searchField)) {
                            searchResult.add(object);
                        }
                    }
                }
            } else {
                for (Node entry : children) {
                    if (entry.isOverlapping(searchField)) {
                        Entry e = (Entry) entry;
                        searchResult.add(e.getObject());
                    }
                }
            }
            return searchResult;

        }

        private List<Way> mbrSearcher(float[] searchField) {
            List<Way> searchResult = new ArrayList<Way>();
            if (!isLeaf) {
                for (Node node : children) {
                    if (node.isOverlapping(searchField)) {
                        for (Way object : node.mbrSearcher(searchField)) {
                            searchResult.add(object);
                        }
                    }
                }
            } else {

                // Creates a way based on the MBR of the leafNode.
                Way way = new Way(new ArrayList<>() {
                    {
                        add(new handin2.Node(mbr[0], mbr[1], 1));
                        add(new handin2.Node(mbr[2], mbr[1], 1));
                        add(new handin2.Node(mbr[2], mbr[3], 1));
                        add(new handin2.Node(mbr[0], mbr[3], 1));
                        add(new handin2.Node(mbr[0], mbr[1], 1));
                    }
                });
                searchResult.add(way);
            }

            return searchResult;
        }

        // Checkes if the MBR on the node or object is overlapping the searchfield
        private boolean isOverlapping(float[] searchField) {
            var isOverlappingLon = true;
            var isOverlappingLat = true;

            if (searchField[2] < mbr[0]) {
                isOverlappingLat = false;
            }
            if ((searchField[3]) < mbr[1]) {
                isOverlappingLon = false;
            }
            if (searchField[0] > mbr[2]) {
                isOverlappingLat = false;
            }
            if ((searchField[1]) > mbr[3]) {
                isOverlappingLon = false;
            }

            if (isOverlappingLon && isOverlappingLat) {
                return true;
            } else {
                return false;
            }

        }

        // Calculate how much the noded has to grow to include a new node/object
        public float MBRarealDif(Node node) {
            float minLat;
            float minLon;
            float maxLat;
            float maxLon;

            if (this.mbr[0] < node.mbr[0]) {
                minLat = this.mbr[0];
            } else {
                minLat = node.mbr[0];
            }
            if (this.mbr[1] < node.mbr[1]) {
                minLon = this.mbr[1];
            } else {
                minLon = node.mbr[1];
            }
            if (this.mbr[2] > node.mbr[2]) {
                maxLat = this.mbr[2];
            } else {
                maxLat = node.mbr[2];
            }
            if (this.mbr[3] > node.mbr[3]) {
                maxLon = this.mbr[3];
            } else {
                maxLon = node.mbr[3];
            }
            return ((maxLon - minLon) * (maxLat - minLat))
                    - ((this.mbr[3] - this.mbr[1]) * (this.mbr[2] - this.mbr[0]));

        }

    }

    // Helper class Entry, which holds an refrence to the MBR of its children and a
    // refrence to an object
    private class Entry extends Node {
        private Way object;

        public Entry(boolean isLeaf, Way object) {
            super(false);
            this.object = object;

            super.mbr[0] = object.getMinLat();
            super.mbr[1] = object.getMinLon();
            super.mbr[2] = object.getMaxLat();
            super.mbr[3] = object.getMaxLon();

        }

        public Way getObject() {
            return object;
        }

    }

    public List<Way> search(float[] searchField) {
        return root.nodeSearcher(new float[] { searchField[0], searchField[2], searchField[1], searchField[3] });
    }

    public List<Way> searchLeafMBR(float[] searchField) {
        return root.mbrSearcher(new float[] { searchField[0], searchField[2], searchField[1], searchField[3] });
    }

    // Metode for inserting an Object to the tree
    public void insert(Way way) {
        isEmpty = false;
        Entry entry = new Entry(false, way);
        Node leaf = chooseLeaf(entry);
        entry.setParrent(leaf);
        if (leaf.size() < M) {
            leaf.add(entry);
            adjustTree(leaf);
        } else {
            leaf.add(entry);
            Node[] splittedNodes = enchantedSplitNode(leaf);
            splittedNodes[1].isLeaf = true;
            adjustTreeSplit(splittedNodes);
        }
    }

    // Adujst all node MBR
    private void adjustTree(Node leaf) {

        if (root != leaf) {
            leaf.mbrCalculator();
            adjustTree(leaf.parrent);
        } else {
            leaf.mbrCalculator();
        }
    }

    // Adujsts node MBR in case of a split.
    private void adjustTreeSplit(Node[] splittedNodes) {
        if (root == splittedNodes[0]) {
            root = new Node(false);
            for (Node node : splittedNodes) {
                root.add(node);
                node.setParrent(root);
            }
            root.mbrCalculator();
        } else {
            for (Node node : splittedNodes) {
                node.mbrCalculator();
            }
            Node parrent = splittedNodes[0].parrent;
            if (parrent.size() < M) {
                parrent.add(splittedNodes[1]);
                adjustTree(parrent);
            } else {
                parrent.add(splittedNodes[1]);
                Node[] newNodes = enchantedSplitNode(parrent);
                adjustTreeSplit(newNodes);
            }

        }
    }

    // Metode called, if a node has M+1 objects or nodes.
    private Node[] enchantedSplitNode(Node leaf) {

        Node[] seeds = linearPickSeed(leaf);
        Node[] newNodes = new Node[] { leaf, new Node(false) };
        List<Node> listOfEntries = new LinkedList<>(leaf.children);
        leaf.clear();
        newNodes[0].add(seeds[0]);
        newNodes[1].add(seeds[1]);
        newNodes[1].setParrent(newNodes[0].parrent);
        listOfEntries.remove(seeds[0]);
        listOfEntries.remove(seeds[1]);

        // Fills each node with m amount of children. (minimum amount)

        for(Node node : newNodes){
            for(int i = 0 ; i < m-1; i++){
                float minDefAreal = Float.MAX_VALUE;
                Node best = null;
                for (Node entry : listOfEntries) {
                    if (node.MBRarealDif(entry) < minDefAreal) {
                        minDefAreal = node.MBRarealDif(entry);
                        best = entry;
                    }
                }
                
                listOfEntries.remove(best);
                node.add(best);
            }
        }

        for (Node entry : listOfEntries) {
            float minDefAreal = Float.MAX_VALUE;
            Node best = null;
            for (Node node : newNodes) {
                if (node.MBRarealDif(entry) < minDefAreal) {
                    minDefAreal = node.MBRarealDif(entry);
                    best = node;
                }
            }
            best.add(entry);
        }

        return newNodes;

    }

    // This metode chooses the two seeds that splits the node.
    // This is the linare timecomplex implementation
    private Node[] linearPickSeed(Node leaf) {
        // Longitude dimension
        float bigMinLon = Float.MIN_VALUE; // The biggest minimum longitude
        float smallMinLon = Float.MAX_VALUE; // The smallest minimum longitude
        Node bigMinLonNode = null; // Holds the node/entry which had the biggest minimum longtude
        float smallMaxLon = Float.MAX_VALUE; // The smallest maximum longitude
        float bigMaxLon = Float.MIN_VALUE; // The biggest maximum longitude
        Node smallMaxLonNode = null; // Holds the Node/entry which had the smallest maximum longitude
        float lonSeperation; // the diffrence on biggest minimum longitude and the smallest maximum longitude

        // Latitude dimension
        float bigMinLat = Float.MIN_VALUE; // The biggest minimum latitude
        float smallMinLat = Float.MAX_VALUE; // The smallest minimum latitude
        Node bigMinLatNode = null; // Holds the node/entry which had the biggest minimum latitude
        float smallMaxLat = Float.MAX_VALUE; // The smallest maximum latitude
        float bigMaxLat = Float.MAX_VALUE; // The biggest maximum latitude
        Node smallMaxLatNode = null; // Holds the node/entry which had the smallest maximum latitude
        float latSeperation; // the diffrence on biggest minimum latitude and the smallest maximum latitude

        for (Node node : leaf.children) {
            if (node.mbr[0] > bigMinLat) {
                bigMinLat = node.mbr[0];
                bigMinLatNode = node;
            }
            if (node.mbr[0] < smallMinLat) {
                smallMinLat = node.mbr[0];
            }
            if (node.mbr[1] > bigMinLon) {
                bigMinLon = node.mbr[1];
                bigMinLonNode = node;
            }
            if (node.mbr[1] < smallMinLon) {
                smallMinLon = node.mbr[1];
            }
            if (node.mbr[2] < smallMaxLat) {
                smallMaxLat = node.mbr[2];
                smallMaxLatNode = node;
            }
            if (node.mbr[2] > bigMaxLat) {
                bigMaxLat = node.mbr[2];
            }
            if (node.mbr[3] < smallMaxLon) {
                smallMaxLon = node.mbr[3];
                smallMaxLonNode = node;
            }
            if (node.mbr[3] > bigMaxLon) {
                bigMaxLon = node.mbr[3];
            }
        }

        lonSeperation = (Math.abs(bigMinLon - smallMaxLon)) / (bigMaxLon - smallMinLon);
        latSeperation = (Math.abs(bigMinLat - smallMaxLat)) / (bigMaxLat - smallMinLat);

        Node[] seeds = new Node[2];
        if (lonSeperation > latSeperation) {
            seeds[0] = bigMinLonNode;
            seeds[1] = smallMaxLonNode;
        } else {
            seeds[0] = bigMinLatNode;
            seeds[1] = smallMaxLatNode;
        }

        return seeds;
    }

    // This methode chooses which leaf the object should be added to, by finding the
    // MBR that has to grow the last to include the object.
    public Node chooseLeaf(Entry insertedEntry) {
        Node n = root;

        while (!n.isLeaf) {
            float minMBRarealDif = Float.MAX_VALUE;
            for (Node node : n.children) {
                var MBRarealDif = node.MBRarealDif(insertedEntry);
                if (MBRarealDif <= minMBRarealDif) {
                    minMBRarealDif = MBRarealDif;
                    n = node;
                }
            }
        }
        return n;
    }

    public handin2.Node NearestNodeSearch(float[] point) {
        this.nearPoint = point;
        return nearestNodeSearchNode(root);
    }

    public handin2.Node nearestNodeSearchNode(Node node) {
        float nearestDist = Float.MAX_VALUE;
        handin2.Node nearestNode = null;

       if(node.isLeaf) {
            for(Node entry : node.children) {
                Entry e = (Entry) entry;
                if(e.getObject().distanceToPoint(nearPoint) < nearestDist) {
                    handin2.Node nearest = null;
                    Highway h = (Highway) e.getObject();
                    float localbest = Float.MAX_VALUE;
                    for(handin2.Node n : id2way.get(h.getWayId())) {
                        double dist = (Math.pow(Math.abs((nearPoint[0]-(n.lat))),2.0) + Math.pow(Math.abs((nearPoint[1]-n.lon)),2.0));
                        if(dist < localbest) {
                            localbest = (float )dist;
                            nearest = n;
                        }
                    }
                    nearestNode = nearest;
                    nearestDist = localbest;
                }
            }
        } else {
            List<Node> abl = new ArrayList<Node>();
            for(Node child : node.children) {
                abl.add(child);
            }
            abl.sort(new AblSort());
            abl = downWardPrune(abl, nearPoint);
            for(Node child : abl) {
                if(nearestNeighborSearch(child).distanceToPoint(nearPoint)<nearestDist) {
                    nearestNode = nearestNodeSearchNode(child);
                    nearestDist = (float) (Math.pow(Math.abs((nearPoint[0]-(nearestNode.lat))),2.0) + Math.pow(Math.abs((nearPoint[1]-nearestNode.lon)),2.0));
                    abl = downWardPrune(abl, nearPoint);
                }   

            }
        }
        return nearestNode;
    }
    

    public Way NNSearch(float[] point) {
        this.nearPoint = point;
        return nearestNeighborSearch(root);
 
    }

    //Helper class
    private Way nearestNeighborSearch(Node node) {
        float nearestDist = Float.MAX_VALUE;
        Way nearestWay = null;

       if(node.isLeaf) {
            for(Node entry : node.children) {
                Entry e = (Entry) entry;
                if(e.getObject().distanceToPoint(nearPoint) < nearestDist) {
                    nearestWay = e.getObject();
                    nearestDist = e.getObject().distanceToPoint(nearPoint);
                }
            }
        } else {
            List<Node> abl = new ArrayList<Node>();
            for(Node child : node.children) {
                abl.add(child);
            }
            abl.sort(new AblSort());
            abl = downWardPrune(abl, nearPoint);
            for(Node child : abl) {
                if(nearestNeighborSearch(child).distanceToPoint(nearPoint)<nearestDist) {
                    nearestWay = nearestNeighborSearch(child);
                    nearestDist = nearestWay.distanceToPoint(nearPoint);
                    abl = downWardPrune(abl, nearPoint);
                }   

            }
        }
        return nearestWay;
    }

    private List<Node> downWardPrune(List<Node> abl, float[] point) {
        float minMinMaxDist = Float.MAX_VALUE;
        List<Node> aux = new ArrayList<>(abl);
        for(Node node : aux) {
            if(minMaxDist(point, node.mbr) < minMinMaxDist) {
                minMinMaxDist = minMaxDist(point,node.mbr);
            }
        }
        for(Node node : abl) {
            if(minDist(point, node.mbr) > minMinMaxDist) {
                aux.remove(node);
            }
        }
        return aux;
    }

    //Comparator that sorts nodes based on their minMaxDist to searchpoint
    private class AblSort implements Comparator<Node> {
        @Override
        public int compare(Node n1, Node n2) {
            return Float.compare(minMaxDist(nearPoint,n1.mbr),minMaxDist(nearPoint, n2.mbr));
        }
    }

    private float minDist(float[] point, float[] mbr) {
        float rlat;
        if(point[0] < mbr[0]) {
            rlat = mbr[0];
        } else if(point[0] > mbr[2]) {
            rlat = mbr[2];
        } else {
            rlat = point[0];
        }

        float rlon;
        if(point[1] < mbr[1]) {
            rlon = mbr[1];
        } else if(point[1] > mbr[3]) {
            rlon = mbr[3];
        } else {
            rlon = point[1];
        }

        return (float) (Math.pow((Math.abs(point[0]-rlat)),2.0) + (Math.pow((Math.abs(point[1]-rlon)),2.0)));


    }

    //in mbr point1 = min,min point2 = max,max 
    //For both format is lat,lon
    private float minMaxDist(float[] point, float[] mbr) {
        float latDist;
        float lonDist;

        //lat dist
        float rmlat;
        float rnlon;
        if(point[0] <= ((mbr[0]+mbr[2])/2)) {
            rmlat = mbr[0];
        } else {
            rmlat = mbr[2];
        }
        if(point[1] >= ((mbr[1]+mbr[3])/2)) {
            rnlon = mbr[1];
        } else {
            rnlon = mbr[3];
        }
        latDist = (float) (Math.pow((Math.abs(point[0]-rmlat)),2.0) + (Math.pow((Math.abs(point[1]-rnlon)),2.0)));

        //lon dist
        float rmlon;
        float rnlat;
        if(point[1] <= ((mbr[1]+mbr[3])/2)) {
            rmlon = mbr[1];
        } else {
            rmlon = mbr[3];
        }
        if(point[0] >= ((mbr[0]+mbr[2])/2)) {
            rnlat = mbr[0];
        } else {
            rnlat = mbr[2];
        }
        lonDist = (float) (Math.pow((Math.abs(point[1]-rmlon)),2.0) + (Math.pow((Math.abs(point[0]-rnlat)),2.0)));
        return Math.min(latDist, lonDist);
    }

    
    
}
