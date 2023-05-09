package handin2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import handin2.DataStructures.TST;
import handin2.Pathfinding.Address;

import org.junit.jupiter.api.BeforeAll;

public class TSTTest {

    static Address adr0;
    static Address adr1;
    static Address adr2;
    static TST<Address> tst;


    @BeforeAll
    public static void setUp() {
        tst =  new TST<Address>();
        adr0 = new Address
            (0, 0, "Rønnevej"
            , "1", ""
            , "3720", "Aakirkeby");
        adr1 = new Address
        (0, 0, "Rønnevej"
        , "1", "Q"
        , "3720", "Aakirkeby");
        adr2 = new Address
            (0, 0, "Rønnevej"
            , "2", "A"
            , "3720", "Aakirkeby");

        tst.put("Rønnevej 1, 3720 Aakirkeby", adr0);
        tst.put("Rønnevej 1Q, 3720 Aakirkeby", adr1);
        tst.put("Rønnevej 2A, 3720 Aakirkeby", adr2);
    }

    @Test
    public void putValueTest() {
    
        int actual = tst.size();
        int expected = 3;
    
        assertEquals(expected, actual);
    }

    @Test
    public void getValueTest() {


        String actual = tst.get("Rønnevej 1, 3720 Aakirkeby").toString();
        String expected = "Rønnevej 1, 3720 Aakirkeby";

        assertEquals(expected, actual);
    }

    @Test
    public void TSTContainsTest() {

        boolean actual = tst.contains("Rønnevej 2A, 3720 Aakirkeby");
        boolean expected = true;
        assertEquals(expected, actual);
    }
}
