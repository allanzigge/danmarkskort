package handin2;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;
import java.util.zip.ZipInputStream;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class Model implements Serializable {
    List<Edge> route = new ArrayList<>();
    RTree edgeTreeBike = new RTree();
    RTree edgeTreeCar = new RTree();
    RTree bigRoadRTree = new RTree();
    RTree mediumRoadRTree = new RTree();
    RTree smallRoadRtree = new RTree();
    RTree firstLayerRTree = new RTree();
    RTree secondLayerRTree = new RTree();
    RTree thirdLayerRTree = new RTree();
    RTree fourthLayerRTree = new RTree();
    RTree fithLayerRTree = new RTree();
    Pathfinder pathfinder;

    List<Way> testList = new ArrayList<>();
    List<Way> testListMBR = new ArrayList<>();

    // Parsing sets, to determin whitch node objects we wish to create, and which
    // ways and realtions we wish to draw.
    Set<Long> addressNodeRef = new HashSet<>();
    Set<Long> bulidingNodeRef = new HashSet<>();
    Set<Long> highwayNodeRef = new HashSet<>();
    Set<Long> placeNodeRef = new HashSet<>();
    Set<Long> landueNodeRef = new HashSet<>();
    Set<Long> naturalNodeRef = new HashSet<>();
    Set<String> placeWayRef = new HashSet<>();
    Set<String> landuseWayRef = new HashSet<>();
    Set<String> naturalWayRef = new HashSet<>();
    Set<String> buildingWayRef = new HashSet<>();
    Set<String> highwayWayRef = new HashSet<>();
    Set<String> placeRelationRef = new HashSet<>();
    Set<String> landuseRelationRef = new HashSet<>();
    Set<String> naturalRelationRef = new HashSet<>();
    Set<String> buildingRelationRef = new HashSet<>();
    List<String> memberRefs = new ArrayList<>();
    List<Long> ndRefs = new ArrayList<>();
    String relationId = null;
    boolean parseNd = true;
    boolean parseMember = true;
    boolean parsingHighways = false;

    String nearestRoad;

    // Global attributes
    int counter = 0;
    TST<Set<Address>> addresses = new TST<Set<Address>>();

    float minlat, maxlat, minlon, maxlon;

    // Attributes needed for helper-methods
    Map<Long, Node> id2node = new HashMap<Long, Node>();
    long nodeID = 0L;
    String wayId = null;
    ArrayList<Node> way = new ArrayList<Node>();
    String roadName = null;
    boolean isOneWay = false;
    String maxSpeed = null;
    Boolean place = false;
    Boolean building = false;
    Boolean highway = false;
    Boolean landuse = false;
    Boolean natural = false;
    String v = null;

    // relation fields
    List<List<Node>> outerList = new ArrayList<List<Node>>();
    List<List<Node>> innerList = new ArrayList<List<Node>>();
    Boolean hasOuter = false;
    Boolean relation = false;
    Map<String, ArrayList<Node>> id2way = new HashMap<String, ArrayList<Node>>();

    String street = "";
    String adrNum = "";
    String adrLet = "";
    String postcode = "";
    String city = "";

    // fields for graph
    List<Highway> highways = new ArrayList<>(); // used to make edges in the graph
    HashMap<Long, Integer> checkedNodes = new HashMap<>(); // A hashmap that stores which nodes the vertexParser has
                                                           // incountered and how many times.
    HashMap<Long, Vertex> vertexMap = new HashMap<>(); // The map of all Vertices incountered so far, with id ref to its
                                                       // Node

    static Model load(String filename) throws FileNotFoundException, IOException, ClassNotFoundException,
            XMLStreamException, FactoryConfigurationError {
        if (filename.endsWith(".obj")) {
            try (var in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filename)))) {
                return (Model) in.readObject();
            }
        }
        return new Model(filename);
    }

    public Model(String filename)
            throws XMLStreamException, FactoryConfigurationError, IOException, ClassNotFoundException {
        if (filename.endsWith(".osm.zip")) {
            parseZIP(filename);
        } else if (filename.endsWith(".osm")) {
            parseOSM(filename);
        }
        if (!filename.endsWith(".obj")) {
            // save(filename + ".obj");
        }

    }

    void save(String filename) throws FileNotFoundException, IOException {
        try (var out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(this);
        }
    }

    // =====================================================================================================================================================
    // by parsing multible times we ease the load of objects created at once, by
    // clearing sets, id2node and id2way every time we go throug the parser.
    private void parseZIP(String filename) throws IOException, XMLStreamException, FactoryConfigurationError {
        var time = java.lang.System.currentTimeMillis() / 1000;
        var input = new ZipInputStream(new FileInputStream(filename));
        input.getNextEntry();
        parseOSMWays(input);

        input = new ZipInputStream(new FileInputStream(filename));
        input.getNextEntry();
        parseOSMNodes(input);

        input = new ZipInputStream(new FileInputStream(filename));
        input.getNextEntry();
        // Parses all the bulidings
        parseOSM(input, bulidingNodeRef, buildingWayRef, buildingRelationRef);
        bulidingNodeRef.clear();
        buildingWayRef.clear();
        buildingRelationRef.clear();

        input = new ZipInputStream(new FileInputStream(filename));
        input.getNextEntry();
        // Parses all islands, islets and peninsula
        parseOSM(input, placeNodeRef, placeWayRef, placeRelationRef);
        placeNodeRef.clear();
        placeWayRef.clear();
        placeRelationRef.clear();

        input = new ZipInputStream(new FileInputStream(filename));
        input.getNextEntry();
        // Pases all the naturals
        parseOSM(input, naturalNodeRef, naturalWayRef, naturalRelationRef);
        naturalNodeRef.clear();
        naturalWayRef.clear();
        naturalRelationRef.clear();

        input = new ZipInputStream(new FileInputStream(filename));
        input.getNextEntry();
        // Pases all the landuses
        parseOSM(input, landueNodeRef, landuseWayRef, landuseRelationRef);
        landueNodeRef.clear();
        landuseWayRef.clear();
        landuseRelationRef.clear();

        input = new ZipInputStream(new FileInputStream(filename));
        input.getNextEntry();
        // Parses all the highways and vertices
        parsingHighways = true;
        parseOSM(input, highwayNodeRef, highwayWayRef, new HashSet<String>());
        parsingHighways = false;
        highwayNodeRef.clear();
        highwayWayRef.clear();
        System.out.print("it took :");
        System.out.print((java.lang.System.currentTimeMillis() / 1000) - time);
        System.out.print("s to parse " + filename);
    }

    // =====================================================================================================================================================
    // by parsing multible times we ease the load of objects created at once, by
    // clearing sets, id2node and id2way every time we go throug the parser.
    private void parseOSM(String filename) throws FileNotFoundException, XMLStreamException, FactoryConfigurationError {
        var time = java.lang.System.currentTimeMillis() / 1000;
        FileInputStream inputStream = new FileInputStream(filename);
        parseOSMWays(inputStream);
        inputStream = new FileInputStream(filename);
        parseOSMNodes(inputStream);
        inputStream = new FileInputStream(filename);
        // Parses all the bulidings
        parseOSM(inputStream, bulidingNodeRef, buildingWayRef, buildingRelationRef);
        bulidingNodeRef.clear();
        buildingWayRef.clear();
        buildingRelationRef.clear();

        inputStream = new FileInputStream(filename);
        // Parses all islands, islets and peninsula
        parseOSM(inputStream, placeNodeRef, placeWayRef, placeRelationRef);
        placeNodeRef.clear();
        placeWayRef.clear();
        placeRelationRef.clear();

        inputStream = new FileInputStream(filename);
        // Pases all the naturals
        parseOSM(inputStream, naturalNodeRef, naturalWayRef, naturalRelationRef);
        naturalNodeRef.clear();
        naturalWayRef.clear();
        naturalRelationRef.clear();

        inputStream = new FileInputStream(filename);
        // Pases all the landuses
        parseOSM(inputStream, landueNodeRef, landuseWayRef, landuseRelationRef);
        landueNodeRef.clear();
        landuseWayRef.clear();
        landuseRelationRef.clear();

        inputStream = new FileInputStream(filename);
        // Parses all the highways and vertices
        parsingHighways = true;
        parseOSM(inputStream, highwayNodeRef, highwayWayRef, new HashSet<String>());
        parsingHighways = false;
        highwayNodeRef.clear();
        highwayWayRef.clear();
        System.out.print("it took :");
        System.out.print((java.lang.System.currentTimeMillis() / 1000) - time);
        System.out.print("s to parse " + filename);

    }

    // =====================================================================================================================================================
    // the first parsing: finds all the wayRefs necerssary to create the relations
    // we wish to create, based on there member refs.
    private void parseOSMWays(InputStream inputStream)
            throws FileNotFoundException, XMLStreamException, FactoryConfigurationError {
        var input = XMLInputFactory.newInstance().createXMLStreamReader(new InputStreamReader(inputStream));
        String name;
        while (input.hasNext()) {
            var tagKind = input.next();
            if (tagKind == XMLStreamConstants.START_ELEMENT) { // Reads XML-start elements
                name = input.getLocalName().intern();
                if (name == "bounds") {
                    boundsParser(input);
                } else if (name == "tag") {
                    reltaionTagParser(input);
                } else if (name == "relation") {
                    getRefsRelation(input, "start");
                } else if (name == "member" && relation) {
                    getRefsMembers(input);
                } else if (name == "node") {
                    nodeID = Long.parseLong(input.getAttributeValue(null, "id"));
                }
            } else if (tagKind == XMLStreamConstants.END_ELEMENT) { // Reads xml-end elements
                name = input.getLocalName().intern();
                if (name == "relation" && hasOuter) {
                    getRefsRelation(input, "end");
                } else if (name == "node" && !street.isEmpty()) {
                    addressNodeRef.add(nodeID);
                    street = "";
                }
            }

        }
    }

    // Is only used in parseOSMWays, cause we are only interested in the tags
    // regarding relations
    private void reltaionTagParser(XMLStreamReader input) {
        var k = input.getAttributeValue(null, "k").intern();
        if (k.equals("building")) {
            building = true;
        } else if (k.equals("landuse")) {
            v = input.getAttributeValue(null, "v");
            if (landuseParser(v)) {
                landuse = true;
            }
        } else if (k.equals("natural")) {
            v = input.getAttributeValue(null, "v");
            if (v.equals("peninsula")) {
                place = true;
            } else if (naturalParser(v)) {
                natural = true;
            }
        } else if (k.equals("place")) {
            v = input.getAttributeValue(null, "v");
            if (v.equals("islet")) {
                place = true;
            } else if (v.equals("island")) {
                place = true;
            }
        } else if (k.equals("addr:street")) {
            v = input.getAttributeValue(null, "v");
            street = v;
        }
    }

    // Finds the coordinates (bounds) for the map file.
    private void boundsParser(XMLStreamReader input) {
        minlat = Float.parseFloat(input.getAttributeValue(null, "minlat"));
        maxlat = Float.parseFloat(input.getAttributeValue(null, "maxlat"));
        minlon = Float.parseFloat(input.getAttributeValue(null, "minlon"));
        maxlon = Float.parseFloat(input.getAttributeValue(null, "maxlon"));
    }

    // fills up a tempoary List of the member references, which, if we are
    // interested in the realtion that contains the members, wwill be filled in an
    // appropriate set containing its references
    private void getRefsMembers(XMLStreamReader input) {
        if (input.getAttributeValue(null, "type").equals("way")) {
            if (input.getAttributeValue(null, "role").equals("outer")) {
                String ref = input.getAttributeValue(null, "ref");
                hasOuter = true;
                memberRefs.add(ref);

            } else if (input.getAttributeValue(null, "role").equals("inner")) {
                String ref = input.getAttributeValue(null, "ref");
                memberRefs.add(ref);
            }
        }
    }

    // is used to get the references of the relations we wish to draw
    private void getRefsRelation(XMLStreamReader input, String element) {
        if (element.equals("start")) {
            memberRefs.clear();

            place = false;
            relation = true;
            hasOuter = false;
            building = false;
            landuse = false;
            natural = false;
            relationId = input.getAttributeValue(null, "id");
        } else if (element.equals("end")) {
            // If we meet a boolean which is true, we save the relation ID in an appropriate
            // "relatinRef" set, and empty the "memberRefs" List into a correntsponding
            // appropriate "wayRef" set
            if (place) {

                placeRelationRef.add(relationId);
                for (String ref : memberRefs) {
                    placeWayRef.add(ref);
                }
            } else if (landuse) {
                landuseRelationRef.add(relationId);
                for (String ref : memberRefs) {
                    landuseWayRef.add(ref);
                }
            } else if (natural) {
                naturalRelationRef.add(relationId);
                for (String ref : memberRefs) {
                    naturalWayRef.add(ref);
                }
            } else if (building) {
                buildingRelationRef.add(relationId);
                for (String ref : memberRefs) {
                    buildingWayRef.add(ref);
                }
            }
            relation = false;
        }
    }

    // =====================================================================================================================================================
    // the second parsing: finds all the nodeRefs necerssary to create the ways
    // we wish to create, based on memberRefs and booleans from the wayTagParser.
    private void parseOSMNodes(InputStream inputStream)
            throws FileNotFoundException, XMLStreamException, FactoryConfigurationError {
        var input = XMLInputFactory.newInstance().createXMLStreamReader(new InputStreamReader(inputStream));

        String name;
        while (input.hasNext()) {
            var tagKind = input.next();
            if (tagKind == XMLStreamConstants.START_ELEMENT) { // Reads XML-start elements
                name = input.getLocalName().intern();
                if (name == "way") {
                    getWaysRefs(input, "start");
                } else if (name == "tag") {
                    wayTagParser(input);
                } else if (name == "nd") {
                    getNdRefs(input);
                } else if (name == "node") {
                    nodeID = Long.parseLong(input.getAttributeValue(null, "id"));
                    if (addressNodeRef.contains(nodeID)) {
                        nodeParser(input);
                    }

                }
            } else if (tagKind == XMLStreamConstants.END_ELEMENT) {
                name = input.getLocalName().intern();
                if (name == "way") {
                    getWaysRefs(input, "end");
                } else if (name == "node"
                        && addressNodeRef.contains(nodeID)) {
                    addressParser();
                }
            }
        }
        id2node.clear();
    }

    // Is only used in parseOSMNodes, cause we are only interested in the tags
    // regarding ways
    private void wayTagParser(XMLStreamReader input) {
        var k = input.getAttributeValue(null, "k").intern();
        if (k.equals("highway")) {
            highway = true;
        } else if (k.equals("building")) {
            building = true;
        } else if (k.equals("landuse")) {
            v = input.getAttributeValue(null, "v");
            if (landuseParser(v)) {
                landuse = true;
            }
        } else if (k.equals("natural")) {
            v = input.getAttributeValue(null, "v");
            if (v.equals("peninsula")) {
                place = true;
            } else if (naturalParser(v)) {
                natural = true;
            }
        } else if (k.equals("place")) {
            v = input.getAttributeValue(null, "v");
            if (v.equals("islet")) {
                place = true;
            } else if (v.equals("island")) {
                place = true;
            }
        } else if (k.equals("addr:street")) {
            v = input.getAttributeValue(null, "v");
            street = v;
        } else if (k.equals("addr:housenumber")) {
            v = input.getAttributeValue(null, "v");
            var res = v.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
            if (res.length == 1) {
                adrNum = res[0];
            }
            if (res.length == 2) {
                adrNum = res[0];
                adrLet = res[1];
            }
        } else if (k.equals("addr:postcode")) {
            v = input.getAttributeValue(null, "v");
            postcode = v;
        } else if (k.equals("addr:city")) {
            v = input.getAttributeValue(null, "v");
            city = v;
        }
    }

    // adds Nd to ndRefs
    private void getNdRefs(XMLStreamReader input) {
        var ref = Long.parseLong(input.getAttributeValue(null, "ref"));
        ndRefs.add(ref);
    }

    // We add the way refrences to the apropiate sets, if the way ref is in af set,
    // created under relation/member parsing, and if the wayTagParser has set a
    // boolean true
    private void getWaysRefs(XMLStreamReader input, String element) {
        if (element.equals("start")) {
            ndRefs.clear();
            place = false;
            building = false;
            highway = false;
            landuse = false;
            natural = false;
            wayId = input.getAttributeValue(null, "id");
        } else if (element.equals("end")) {
            if (buildingWayRef.contains(wayId)) {
                for (Long ref : ndRefs) {
                    bulidingNodeRef.add(ref);
                }
            }
            if (highwayWayRef.contains(wayId)) {
                for (Long ref : ndRefs) {
                    highwayNodeRef.add(ref);
                }
            }
            if (placeWayRef.contains(wayId)) {
                for (Long ref : ndRefs) {
                    placeNodeRef.add(ref);
                }
            }
            if (landuseWayRef.contains(wayId)) {
                for (Long ref : ndRefs) {
                    landueNodeRef.add(ref);
                }
            }
            if (naturalWayRef.contains(wayId)) {
                for (Long ref : ndRefs) {
                    naturalNodeRef.add(ref);
                }
            }
            if (building) {
                buildingWayRef.add(wayId);
                for (Long ref : ndRefs) {
                    bulidingNodeRef.add(ref);
                }
            } else if (highway) {
                highwayWayRef.add(wayId);
                for (Long ref : ndRefs) {
                    highwayNodeRef.add(ref);
                }
            } else if (place) {
                placeWayRef.add(wayId);
                for (Long ref : ndRefs) {
                    placeNodeRef.add(ref);
                }
            } else if (landuse) {
                landuseWayRef.add(wayId);
                for (Long ref : ndRefs) {
                    landueNodeRef.add(ref);
                }
            } else if (natural) {
                naturalWayRef.add(wayId);
                for (Long ref : ndRefs) {
                    naturalNodeRef.add(ref);
                }
            }
        }
    }

    // parses all adresses from the XML document
    private void addressParser() {

        Address adr = new Address(id2node.get(nodeID).getLat(), id2node.get(nodeID).getLon(), street, adrNum, adrLet,
                postcode, city);
        String key = street.toLowerCase() + " " + adrNum + adrLet.toLowerCase();
        var value = addresses.get(key);

        if (addresses.get(key) != null) {
            value.add(adr);
            addresses.put(key, value);
        } else {
            value = new HashSet<>();
            value.add(adr);
            addresses.put(key, value);
        }

        street = "";
        adrNum = "";
        adrLet = "";
        postcode = "";
        city = "";
    }

    // =====================================================================================================================================================
    // The thrid parsing: Parses OSM files, by filter given as sets, utilizing
    // helpermethods.
    private void parseOSM(InputStream inputStream, Set<Long> nodeRef, Set<String> wayRef, Set<String> relationRef)
            throws FileNotFoundException, XMLStreamException, FactoryConfigurationError {
        var input = XMLInputFactory.newInstance().createXMLStreamReader(new InputStreamReader(inputStream));
        String name;
        while (input.hasNext()) { // Runs as long as there are XML elements to parse.
            var tagKind = input.next();
            if (tagKind == XMLStreamConstants.START_ELEMENT) { // Reads XML-start elements
                name = input.getLocalName().intern();
                if (name == "node" && nodeRef.contains(Long.parseLong(input.getAttributeValue(null, "id")))) {
                    // We dont want to make a Node object if the nodeId isn't in the nodeRef set.
                    nodeParser(input);
                    nodeRef.remove(Long.parseLong(input.getAttributeValue(null, "id")));
                } else if (name == "way") {
                    wayId = input.getAttributeValue(null, "id");
                    if (wayRef.contains(wayId)) {
                        wayParser(input, "start");
                    } else {
                        // We assure by using these booleans that we don't engage whit the ndParser and
                        // the tagParser, if the wayRef set doesn't contain this way.
                        parseNd = false;

                    }
                } else if (name == "tag") {
                    tagParser(input);
                } else if (name == "nd" && parseNd) {
                    ndParser(input);
                } else if (name == "relation") {
                    relationId = input.getAttributeValue(null, "id");
                    if (relationRef.contains(relationId)) {
                        relationParser(input, "start");
                    } else {
                        // We assure by using these booleans that we don't engage whit the memberParser
                        // and tagParser, if the relationRef set doesn't contain this relation.
                        parseMember = false;

                    }
                } else if (name == "member" && relation && parseMember) {
                    memberParser(input);
                }
            } else if (tagKind == XMLStreamConstants.END_ELEMENT) { // Reads xml-end elements
                name = input.getLocalName().intern(); // returns the "name"(type) of a given xml-element
                if (name == "way") {
                    if (wayRef.contains(wayId)) {
                        wayParser(input, "end");
                    } else {
                        parseNd = true;
                    }
                } else if (name == "relation") {
                    if (relationRef.contains(relationId) && hasOuter) {
                        relationParser(input, "end");
                    } else {
                        parseMember = true;
                    }
                }
            }
            if (highway) {
                pathfinder = new Pathfinder(vertexMap);
            }
        }
        // After the parsing of all highways, the highway list is filled and we call the
        // edgeParser methode.
        if (!highways.isEmpty()) {
            checkedNodes.clear();
            edgeParser();
        }
        // To free up space in the heap space, we clear the maps which holds the Node
        // obejcts, since they now is stored as Ways
        id2node.clear();
        id2way.clear();
    }

    // Updates attributes (booleans m.m.) related to parsed ways and relations.
    private void tagParser(XMLStreamReader input) {
        var k = input.getAttributeValue(null, "k").intern();
        if (k.equals("highway")) {
            v = input.getAttributeValue(null, "v").intern();
            highway = true;
        } else if (k.equals("name")) {
            roadName = input.getAttributeValue(null, "v");
        } else if (k.equals("oneway")) {
            if (v.equals("yes")) {
                isOneWay = true;
            }
        } else if (k.equals("junction")) {
            isOneWay = true;
        } else if (k.equals("maxspeed")) {
            maxSpeed = input.getAttributeValue(null, "v");
        } else if (k.equals("building")) {
            building = true;
        } else if (k.equals("landuse")) {
            v = input.getAttributeValue(null, "v");
            if (landuseParser(v)) {
                landuse = true;
            }
        } else if (k.equals("natural")) {
            v = input.getAttributeValue(null, "v");
            if (v.equals("peninsula")) {
                place = true;
            } else if (naturalParser(v)) {
                natural = true;
            }
        } else if (k.equals("place")) {
            v = input.getAttributeValue(null, "v");
            if (v.equals("islet")) {
                place = true;
            } else if (v.equals("island")) {
                place = true;
            }
        }
    }

    // nd is an element containing a ref to a node.
    // Parses all nd's in a given way, and adds these to way(List<Node>)
    private void ndParser(XMLStreamReader input) {
        var ref = Long.parseLong(input.getAttributeValue(null, "ref"));
        var node = id2node.get(ref);
        way.add(node);
    }

    // Parses all nodes from XMLfile and maps these with their id's.
    private void nodeParser(XMLStreamReader input) {
        nodeID = Long.parseLong(input.getAttributeValue(null, "id"));
        var lat = Float.parseFloat(input.getAttributeValue(null, "lat"));
        var lon = Float.parseFloat(input.getAttributeValue(null, "lon"));
        id2node.put(nodeID, new Node(lat, lon, nodeID));
    }

    // Parsing of ways from XMLfile
    private void wayParser(XMLStreamReader input, String element) {

        // Resets all attributes related to way.
        if (element.equals("start")) {
            way.clear();
            roadName = null;
            isOneWay = false;
            maxSpeed = null;
            place = false;
            building = false;
            highway = false;
            landuse = false;
            natural = false;

            // Creates ways(object) from the list<Node> and stores these in R-trees
            // categories
        } else if (element.equals("end")) {
            id2way.put(wayId, new ArrayList<Node>(way)); // maps all ways with their ref's. To be used by relations.

            if (building) {
                fithLayerRTree.insert(new Building(way));
            } else if (highway) {

                // When we incounter a highway, we want to run it through the vertexParser.
                Highway highway = new Highway(way, maxSpeed, roadName, v, isOneWay, wayId);
                if (parsingHighways) {
                    vertexParser(way);
                    highways.add(highway);
                }
                if (bigRoad(v)) {
                    bigRoadRTree.insert(highway);
                    mediumRoadRTree.insert(highway);
                    smallRoadRtree.insert(highway);
                } else if (mediumRoad(v)) {
                    mediumRoadRTree.insert(highway);
                    smallRoadRtree.insert(highway);
                } else {
                    smallRoadRtree.insert(highway);
                }

            } else if (landuse) {
                secondLayerRTree.insert(new Landuse(way, v));
            } else if (natural && !v.equals("coastline")) {
                thirdLayerRTree.insert(new Natural(way, v));
            } else if (place) {
                firstLayerRTree.insert(new Natural(way, "island"));
            }
        }
    }

    // Checks to see if relations have inner- and outer-lists. If so, updates the
    // global attributes innerlist and outerlist, to be used later in the creation
    // of relations.
    private void memberParser(XMLStreamReader input) {
        if (input.getAttributeValue(null, "type").equals("way")) {
            if (input.getAttributeValue(null, "role").equals("outer")) {
                String ref = input.getAttributeValue(null, "ref");
                hasOuter = true;
                if (id2way.get(ref) != null) {
                    outerList.add(id2way.get(ref));
                }

            } else if (input.getAttributeValue(null, "role").equals("inner")) {
                String ref = input.getAttributeValue(null, "ref");
                if (id2way.get(ref) != null) {
                    innerList.add(id2way.get(ref));
                }
            }
        }
    }

    // Parses relations
    private void relationParser(XMLStreamReader input, String element) {
        // Resets all atributs related to relations
        if (element.equals("start")) {
            outerList.clear();
            innerList.clear();

            place = false;
            relation = true;
            hasOuter = false;
            building = false;
            highway = false;
            landuse = false;
            natural = false;
            // Creates relations, and stores them in different lists ("categories"), based
            // on their attributes.
        } else if (element.equals("end")) {
            if (place && !relationId.equals("11734020")) {
                firstLayerRTree.insert(new Natural(
                        new Relation(new ArrayList<List<Node>>(outerList), new ArrayList<List<Node>>(innerList), v)
                                .getRelationTrace(),
                        "island"));
            } else if (landuse && !place) {
                secondLayerRTree.insert(new Landuse(
                        new Relation(new ArrayList<List<Node>>(outerList), new ArrayList<List<Node>>(innerList), v)
                                .getRelationTrace(),
                        v));
            } else if (natural && !v.equals("coastline")) {
                thirdLayerRTree.insert(new Natural(
                        new Relation(new ArrayList<List<Node>>(outerList), new ArrayList<List<Node>>(innerList), v)
                                .getRelationTrace(),
                        v));
            } else if (building) {
                fithLayerRTree.insert(new Building(
                        new Relation(new ArrayList<List<Node>>(outerList), new ArrayList<List<Node>>(innerList), v)
                                .getRelationTrace(),
                        v));
            }
            relation = false;
        }
    }

    // =====================================================================================================================================================
    // road helper methodes

    // helper methode used to determinant the "size" of the road
    private boolean bigRoad(String roadType) {
        if (roadType.equals("motorway") || roadType.equals("motorway_link")) {
            return true;
        }

        else if (roadType.equals("trunk") || roadType.equals("trunk_link")) {
            return true;
        }

        else if (roadType.equals("primary") || roadType.equals("primary_link")) {
            return true;
        }

        else {
            return false;
        }
    }

    // helper methode used to determinant the "size" of the road
    private boolean mediumRoad(String roadType) {
        if (roadType.equals("secondary") || roadType.equals("secondary_link")) {
            return true;
        } else if (roadType.equals("tertiary") || roadType.equals("tertiary_link")) {
            return true;
        } else {
            return false;
        }
    }

    // =====================================================================================================================================================
    // helper methodes for creating vertices and edges for the A* implementation

    // This methode detects how many Vertices there is on a highway, and updates the
    // vertexMap
    public void vertexParser(List<Node> nodes) {

        for (int i = 0; i < nodes.size(); i++) {
            Long nodeID = nodes.get(i).getID();
            if (checkedNodes.get(nodeID) == null) {
                if (i == 0 || i == nodes.size() - 1) {
                    checkedNodes.put(nodeID, 1); // updates the map, since this is a new node
                    Vertex vertex = new Vertex(nodes.get(i));
                    vertexMap.put(nodeID, vertex); // since its a new end node, we make a new Vertex and updates the
                                                   // vertexMap
                } else {
                    checkedNodes.put(nodeID, 1); // updates the node map, since this is a new node
                }
            } else if (checkedNodes.get(nodeID) > 1) {
                checkedNodes.put(nodeID, (checkedNodes.get(nodeID) + 1)); // increse how many times the node has been
                                                                          // incountered in the map
            } else if (checkedNodes.get(nodeID) == 1) {
                checkedNodes.put(nodeID, (checkedNodes.get(nodeID) + 1));
                if (vertexMap.containsKey(nodeID)) { // We need to see if the vertex exist, in the case that the first
                                                     // encounter with the node had been an end node.
                } else {
                    Vertex vertex = new Vertex(nodes.get(i));
                    vertexMap.put(nodeID, vertex); // since the node has been encountered twice, is it a intersection,
                                                   // and we make a new Vertex and updates the vertexMap
                }
            }
        }
    }

    // runs throug the list of highways, and the vertexMap contains one og it nodes,
    // makes it af start or end node of a vertex.
    private void edgeParser() {
        for (Highway highway : highways) {
            Vertex startVertex = null;
            Vertex endVertex = null;
            double cost = 0.0;
            ArrayList<Node> way = new ArrayList<>();
            Node lastNode = null;
            for (Node node : id2way.get(highway.getWayId())) {
                if (startVertex == null) {
                    if (vertexMap.containsKey(node.getID())) {
                        startVertex = vertexMap.get(node.getID());
                        way.add(startVertex);
                        lastNode = startVertex;
                    }
                } else {
                    if (lastNode != null) {
                        cost = cost + lastNode.distToVertex(node);
                    }
                    way.add(node);
                    lastNode = node;

                    if (vertexMap.keySet().contains(node.getID())) {

                        endVertex = vertexMap.get(node.getID());
                        way.add(endVertex);
                    }
                }
                if (endVertex != null && startVertex != null) {
                    if (!highway.isOneWay()) {
                        Edge startVertexEdge = new Edge(startVertex.nodeID, endVertex.nodeID, highway,
                                highway.isDriveable(), highway.isBikeable(), cost,
                                new ArrayList<Node>(way));
                        startVertex.addNeigbor(startVertexEdge);
                        Edge endVertexEdge = new Edge(endVertex.nodeID, startVertex.nodeID, highway,
                                highway.isDriveable(), highway.isBikeable(), cost,
                                new ArrayList<Node>(way));
                        endVertex.addNeigbor(endVertexEdge);
                        if (highway.isDriveable()) {
                            edgeTreeCar.insert(endVertexEdge);
                            edgeTreeCar.insert(startVertexEdge);
                        }
                        if (highway.isBikeable()) {
                            edgeTreeBike.insert(endVertexEdge);
                            edgeTreeBike.insert(startVertexEdge);
                        }

                        startVertex = endVertex;
                        endVertex = null;
                        lastNode = startVertex;
                        cost = 0.0;
                        way.clear();
                        way.add(startVertex);
                    } else {
                        Edge startVertexEdge = new Edge(startVertex.nodeID, endVertex.nodeID, highway,
                                highway.isDriveable(), highway.isBikeable(), cost,
                                new ArrayList<Node>(way));

                        startVertex.addNeigbor(startVertexEdge);
                        if (highway.isDriveable()) {
                            edgeTreeCar.insert(startVertexEdge);
                        }
                        if (highway.isBikeable()) {
                            edgeTreeBike.insert(startVertexEdge);
                        }
                        startVertex = endVertex;
                        endVertex = null;
                        lastNode = startVertex;
                        cost = 0.0;
                        way.clear();
                        way.add(startVertex);
                    }
                }
            }
        }
    }

    // =====================================================================================================================================================
    // TagParser helper methodes

    // helper methode used by the tagparseres do determinate the boolean state when
    // meeting a landuse tag
    private boolean landuseParser(String element) {
        switch (element) {
            case "basin":
                return true;
            case "reservoir":
                return true;
            case "salt_pond":
                return true;
            case "aquaculture":
                return true;

            case "meadow":
                return true;
            case "orchard":
                return true;
            case "plant_nursery":
                return true;
            case "vineyard":
                return true;
            case "grass":
                return true;
            case "village_green":
                return true;
            case "residential":
                return true;

            case "forest":
                return true;
            case "cemetery":
                return true;

            case "farmyard":
                return false;
            case "greenhouse_horticulture":
                return false;
            case "farmland":
                return false;
            case "military":
                return false;
            case "port":
                return false;

            default:
                return false;
        }
    }

    // helper methode used by the tagparseres do determinate the boolean state when
    // meeting a natural tag
    private boolean naturalParser(String element) {
        switch (element) {
            case "coastline":
                return false;
            case "cliff":
                return false;
            case "tree_row":
                return false;
            case "bay":
                return false;
            case "strait":
                return false;
            default:
                return true;
        }
    }
}
