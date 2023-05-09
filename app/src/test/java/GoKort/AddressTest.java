package GoKort;


import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import GoKort.Pathfinding.Address;

public class AddressTest {

    @Test
    public void getLatTest() {

            Address adr0 = new Address(0, 20, null, null, null, null, null);
            
            double expected = 0;
            double actual = adr0.getLat();

            assertEquals(expected, actual);
       }
    
       @Test
       public void getLonTest() {
           
            Address adr1 = new Address(20, 0, null, null, null, null, null);
            
            double expected = 0;
            double actual = adr1.getLon();
    
            assertEquals(expected, actual);
        }

        @Test
        public void getStreetTest() {
            
            Address adr2 = new Address(0, 0, "Klerkegade", null, null, null, null);
            
            String expected = "Klerkegade";
            String actual = adr2.getStreet();

            assertEquals(expected, actual);
        }

        @Test
        public void getAdrNumTest() {
            
            Address adr3 = new Address(0, 0, null, "5", null, null, null);
            
            String expected = "5";
            String actual = adr3.getAdrNum();

            assertEquals(expected, actual);
        }

        @Test
        public void getAdrLetTest() {
            
            Address adr4 = new Address(0, 0, null, null, "A", null, null);
            
            String expected = "A";
            String actual = adr4.getAdrLet();

            assertEquals(expected, actual);
        }

        @Test
        public void getPostcodeTest() {
            
            Address adr5 = new Address(0, 0, null, null, null, "1304", null);
            
            String expected = "1304";
            String actual = adr5.getPostcode();

            assertEquals(expected, actual);
        }

        @Test
        public void getCity() {
            
            Address adr6 = new Address(0, 0, null, null, null, null, "København");
            
            String expected = "København";
            String actual = adr6.getCity();

            assertEquals(expected, actual);
        }

        @Test
        public void OveridedToStringTest0() {
            
            Address adr7 = new Address(0, 0, "Klerkegade", "5", "A", "1304", "København");
            
            String expected = "Klerkegade 5A, 1304 København";
            String actual = adr7.toString();

            assertEquals(expected, actual);
        }

        @Test
        public void OveridedToStringTest1() {
            
            Address adr8 = new Address(0, 0, "Klerkegade", "5", "", "1304", "København");
            
            String expected = "Klerkegade 5, 1304 København";
            String actual = adr8.toString();

            assertEquals(expected, actual);
        }

        @Test
        public void OveridedToStringTest2() {
            
            Address adr8 = new Address(0, 0, "Klerkegade", "5", "A", "", "København");
            
            String expected = "Klerkegade 5A, København";
            String actual = adr8.toString();

            assertEquals(expected, actual);
        }

        @Test
        public void OveridedToStringTest3() {
            
            Address adr9 = new Address(0, 0, "Klerkegade", "5", "", "", "København");
            
            String expected = "Klerkegade 5, København";
            String actual = adr9.toString();

            assertEquals(expected, actual);
        }

        @Test
        public void OveridedToStringTest4() {
            
            Address adr10 = new Address(0, 0, "Klerkegade", "", "", "1304", "København");
            
            String expected = "Klerkegade, 1304 København";
            String actual = adr10.toString();

            assertEquals(expected, actual);
        }

        @Test
        public void OveridedToStringTest5() {
            
            Address adr10 = new Address(0, 0, "Klerkegade", "", "", "", "København");
            
            String expected = "Klerkegade, København";
            String actual = adr10.toString();

            assertEquals(expected, actual);
        }
}
