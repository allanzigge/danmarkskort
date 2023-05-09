package handin2;

import java.io.Serializable;

public class Address implements Serializable {
    private float lat, lon;
    private String street, adrNum, adrLet, postcode, city;

    public Address(float lat, float lon, String street, String adrNum,
            String adrLet, String postcode, String city) {
        this.lat = lat;
        this.lon = lon;
        this.street = street;
        this.adrNum = adrNum;
        this.adrLet = adrLet;
        this.postcode = postcode;
        this.city = city;
    }

    public float getLat() {
        return lat;
    }

    public float getLon() {
        return lon;
    }

    public String getStreet() {
        return street;
    }

    public String getAdrNum() {
        return adrNum;
    }

    public String getAdrLet() {
        return adrLet;
    }

    public String getPostcode() {
        return postcode;
    }

    public String getCity() {
        return city;
    }

    //Prints the address in the format addresses are usually written in.
    //This is done by checking what parts of the address are available and then placing a comma accordingly
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
}
