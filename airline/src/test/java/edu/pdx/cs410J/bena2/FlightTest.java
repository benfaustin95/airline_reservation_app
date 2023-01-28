package edu.pdx.cs410J.bena2;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Flight} class.
 * You'll need to update these unit tests as you build out you program.
 */
public class FlightTest {

    @Test
    void initializeFlightStrings() {

        assertThat("Flight 1 departs src at 01/01/2023 10:39 arrives dsn at 01/02/2023 02:50",
                equalTo(getValidFlight().toString()));
    }

    @Test
    void initializeFlightDate() {

        Flight test = null;

        try {
            test = new Flight("1", "src", "dsn",
                    new SimpleDateFormat("MM/dd/yyyy HH:mm").parse("1/1/2023 10:39"),
                    new SimpleDateFormat("MM/dd/yyyy HH:mm").parse("1/2/2023 2:50"));
        } catch (IllegalArgumentException | ParseException ex) {
            fail(ex.getMessage());
        }

        assertThat("Flight 1 departs src at 01/01/2023 10:39 arrives dsn at 01/02/2023 02:50",equalTo(test.toString()));
    }
    @Test
    void testGoodFNumber(){

        assertThat(1,equalTo(getValidFlight().getNumber()));

    }

    @Test
    void testBadFNumber() {
        assertThrows(IllegalArgumentException.class, () -> new Flight("123f",
                "src", "dsn", "1/1/2023", "10:39", "1/2/2023", "2:50"));

        assertThrows(IllegalArgumentException.class, () -> new Flight("",
                "src", "dsn", "1/1/2023", "10:39", "1/2/2023", "2:50"));

        assertThrows(IllegalArgumentException.class, () -> new Flight("0",
                "src", "dsn", "1/1/2023", "10:39", "1/2/2023", "2:50"));

        assertThrows(IllegalArgumentException.class, () -> new Flight("-1",
                "src", "dsn", "1/1/2023", "10:39", "1/2/2023", "2:50"));

        assertThrows(IllegalArgumentException.class, () -> new Flight("f",
                "src", "dsn", "1/1/2023", "10:39", "1/2/2023", "2:50"));

        assertThrows(IllegalArgumentException.class, () -> new Flight(null,
                "src", "dsn", "1/1/2023", "10:39", "1/2/2023", "2:50"));
    }

    @Test
    void testSource(){

        assertThat("src", equalTo(getValidFlight().getSource()));
    }

    @Test
    void testBadSource() {
        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "srcw", "dsn", "1/1/2023", "10:39", "1/2/2023", "2:50"));

        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "", "dsn", "1/1/2023", "10:39", "1/2/2023", "2:50"));

        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                null, "dsn", "1/1/2023", "10:39", "1/2/2023", "2:50"));

        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "sr", "dsn", "1/1/2023", "10:39", "1/2/2023", "2:50"));

        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "s", "dsn", "1/1/2023", "10:39", "1/2/2023", "2:50"));

        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "s12", "dsn", "1/1/2023", "10:39", "1/2/2023", "2:50"));

    }

    @Test
    void testDestination(){

        assertThat("dsn", equalTo(getValidFlight().getDestination()));
    }

    @Test
    void testBadDestination() {
        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "src", "dsnbad", "1/1/2023", "10:39", "1/2/2023", "2:50"));
    }

    @Test
    void testDepartureString()
    {
        assertThat("01/01/2023 10:39",equalTo(getValidFlight().getDepartureString()));
    }

    @Test
    void testDepartureDate()
    {
        Flight test= null;
        Date test_departure = null, test_arrival = null;

        try {
            test_departure = new SimpleDateFormat("MM/dd/yyyy HH:mm").parse("1/1/2023 10:39");
            test_arrival = new SimpleDateFormat("MM/dd/yyyy HH:mm").parse("1/2/2023 1:11");

            test = new Flight("1","src", "dsn", test_departure,
                    test_arrival);

        } catch (ParseException | IllegalArgumentException ex) {
            fail(ex.getMessage());
        }
        assertThat(0, equalTo(test_departure.compareTo(test.getDeparture())));
    }

    @Test
    void testArrivalString() {

        assertThat("01/02/2023 02:50", equalTo(getValidFlight().getArrivalString()));
    }

    @Test
    void testArrivalDate()
    {
        Flight test= null;
        Date test_departure = null, test_arrival = null;

        try {
            test_departure = new SimpleDateFormat("MM/dd/yyyy HH:mm").parse("1/1/2023 10:39");
            test_arrival = new SimpleDateFormat("MM/dd/yyyy HH:mm").parse("1/2/2023 1:11");

            test = new Flight("1","src", "dsn", test_departure,
                    test_arrival);

        } catch (ParseException | IllegalArgumentException ex) {
            fail(ex);
        }
        assertThat(0, equalTo(test_arrival.compareTo(test.getArrival())));
    }

    @Test
    void testInvalidYearString() {
        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "src", "dsn", "1/1/223", "10:39", "1/2/2023", "2:50"));
        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "src", "dsn", "1/1/20234", "10:39", "1/2/2023", "2:50"));
        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "src", "dsn", "1/1/-2023", "10:39", "1/2/2023", "2:50"));
        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "src", "dsn", "1/1/2f23", "10:39", "1/2/2023", "2:50"));
        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "src", "dsn", "1/1/0000", "10:39", "1/2/2023", "2:50"));
        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "src", "dsn", "1/1/23", "10:39", "1/2/2023", "2:50"));
    }

    @Test
    void testInvalidMonthString() {
        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "src", "dsn", "21/1/2023", "10:39", "1/2/2023", "2:50"));
        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "src", "dsn", "-12/1/2023", "10:39", "1/2/2023", "2:50"));
        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "src", "dsn", "0/1/2023", "10:39", "1/2/2023", "2:50"));
        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "src", "dsn", "123/1/2023", "10:39", "1/2/2023", "2:50"));
        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "src", "dsn", "/1/2023", "10:39", "1/2/2023", "2:50"));
        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "src", "dsn", "13/1/2023", "10:39", "1/2/2023", "2:50"));
    }

    @Test
    void testInvalidDayString() {
        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "src", "dsn", "12/32/2023", "10:39", "1/2/2023", "2:50"));
        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "src", "dsn", "12/-1/2023", "10:39", "1/2/2023", "2:50"));
        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "src", "dsn", "12/0/2023", "10:39", "1/2/2023", "2:50"));
        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "src", "dsn", "12/123/2023", "10:39", "1/2/2023", "2:50"));
        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "src", "dsn", "2/31/2023", "10:39", "1/2/2023", "2:50"));
        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "src", "dsn", null, "10:39", "1/2/2023", "2:50"));
    }

    @Test
    void testInvalidHourString() {
        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "src", "dsn", "12/12/2023", "25:39", "1/2/2023", "2:50"));
        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "src", "dsn", "12/12/2023", "-1:39", "1/2/2023", "2:50"));
        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "src", "dsn", "12/12/2023", "100:39", "1/2/2023", "2:50"));
        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "src", "dsn", "12/12/2023", ":39", "1/2/2023", "2:50"));
        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "src", "dsn", "12/12/2023", "99:39", "1/2/2023", "2:50"));
        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "src", "dsn", "12/12/2023", null, "1/2/2023", "2:50"));
    }

    @Test
    void testInvalidMinuteString() {
        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "src", "dsn", "12/12/2023", "12:60", "1/2/2023", "2:50"));
        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "src", "dsn", "12/12/2023", "12:-1", "1/2/2023", "2:50"));
        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "src", "dsn", "12/12/2023", "12:390", "1/2/2023", "2:50"));
        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "src", "dsn", "12/12/2023", "12:0", "1/2/2023", "2:50"));
        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "src", "dsn", "12/12/2023", "12:", "1/2/2023", "2:50"));
    }

    @Test
    void testArrivalBeforeDeparture()
    {
        assertThrows(IllegalArgumentException.class, () -> new Flight("123", "src",
                "dsn", "12/12/2023", "12:13", "12/11/2023", "12:13"));

        assertThrows(IllegalArgumentException.class, () -> new Flight("123", "src",
                "dsn", "12/11/2023", "12:14", "12/11/2023", "12:13"));
    }

    @Test
    void testDump()
    {
        Flight test = getValidFlight();

        assertThat(test.getDump(), equalTo("1,src,01/01/2023,10:39,dsn,01/02/2023,02:50"));
    }

    @Test
    void testDateDump()
    {
        String test = getValidFlight().getDepartureString();

        assertThat(Flight.dateDump(test), equalTo("01/01/2023,10:39"));
    }

    @Test
    void testCloneSameData()
    {
       Flight test = getValidFlight();
       Flight clone = test.clone();

       assertTrue(clone.equals(test));
        assertNotSame(clone.getDeparture(), test.getDeparture());
    }

    @Test
    void testEqualsSameObject()
    {
        Flight test = getValidFlight();

        assertTrue(test.equals(test));
    }

    @Test
    void testEqualsSameState()
    {
        Flight test = getValidFlight();
        Flight test2 = getValidFlight();
        assertTrue(test.equals(test2));
    }

    @Test
    void testEqualsFailure()
    {
        Flight test = new Flight("32", "srd","dsn", "1/1/2023", "10:23","1/2/2023","2:54");
        Flight test2 = getValidFlight();

        assertFalse(test.equals(test2));
    }

    @Test
    void testEqualsDifferentClass()
    {
        Flight test = getValidFlight();

        assertFalse(test.equals(" "));
    }
    protected static Flight getValidFlight() {
        Flight test = null;
        try {
            test = new Flight("1", "src", "dsn", "1/1/2023",
                    "10:39", "1/2/2023", "2:50");
        }
        catch (IllegalArgumentException ex) {
            fail(ex.getMessage());
        }
        return test;
    }

}
