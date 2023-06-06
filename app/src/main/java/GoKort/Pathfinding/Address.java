package GoKort.Pathfinding;

import GoKort.Objects.Node;
import java.io.Serializable;

public class Address extends Node implements Serializable {
    private String street, adrNum, adrLet, postcode, city;

    public Address(float lat, float lon, long id, String street, String adrNum,
            String adrLet, String postcode, String city) {
        super(lat,lon,id);
        this.street = street;
        this.adrNum = adrNum;
        this.adrLet = adrLet;
        this.postcode = postcode;
        this.city = city;
    }

    // Prints the address in the format addresses are usually written in.
    // This is done by checking what parts of the address are available and then
    // placing a comma accordingly
    @Override
    public String toString() {
        boolean hasLet = !adrLet.isEmpty();
        boolean hasNum = !adrNum.isEmpty();
        boolean hasPostcode = !postcode.isEmpty();

        if (hasNum && hasLet && hasPostcode) {
            return (street + " " + adrNum + adrLet + ", " + postcode + " " + city);
        } else if (hasNum && hasLet && !hasPostcode) {
            return (street + " " + adrNum + adrLet + ", " + city);
        } else if (hasNum && !hasLet && !hasPostcode) {
            return (street + " " + adrNum + ", " + city);
        } else if (!hasNum && !hasLet && hasPostcode) {
            return (street + ", " + postcode + " " + city);
        } else if (hasNum && !hasLet && hasPostcode) {
            return (street + " " + adrNum + ", " + postcode + " " + city);
        } else {
            return (street + ", " + city);
        }
    }

    // Getter og setter
    public float getLat() {
        return lat;
    }

    public float getLon() {
        return lon;
    }

}
