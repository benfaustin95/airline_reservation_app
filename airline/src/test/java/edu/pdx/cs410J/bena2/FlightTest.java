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

        assertThat("Flight 1 departs PDX at 01/01/2023 10:39 AM arrives SEA at 01/02/2023 02:50 PM",
                equalTo(getValidFlight().toString()));
    }

    @Test
    void initializeFlightDate() {

        Flight test = null;

        try {
            test = new Flight("1", "pdx", "SEA",
                    new SimpleDateFormat("MM/dd/yyyy hh:mm aa").parse("1/1/2023 10:39 AM"),
                    new SimpleDateFormat("MM/dd/yyyy hh:mm aa").parse("1/2/2023 2:50 PM"));
        } catch (IllegalArgumentException | ParseException ex) {
            fail(ex.getMessage());
        }

        assertThat("Flight 1 departs PDX at 01/01/2023 10:39 AM arrives SEA at 01/02/2023 02:50 PM",equalTo(test.toString()));

    }
    @Test
    void testGoodFNumber(){

        assertThat(1,equalTo(getValidFlight().getNumber()));

    }

    @Test
    void testBadFNumber() {
        assertThrows(IllegalArgumentException.class, () -> new Flight("123f",
                "PDX", "SEA", "1/1/2023", "10:39","am", "2:50", "1/2/2023","pm"));

        assertThrows(IllegalArgumentException.class, () -> new Flight("",
                "PDX", "SEA", "1/1/2023", "10:39","am", "2:50", "1/2/2023","pm"));


        assertThrows(IllegalArgumentException.class, () -> new Flight("0",
                "PDX", "SEA", "1/1/2023", "10:39","am", "2:50", "1/2/2023","pm"));

        assertThrows(IllegalArgumentException.class, () -> new Flight("-1",
                "PDX", "SEA", "1/1/2023", "10:39","am", "2:50", "1/2/2023","pm"));

        assertThrows(IllegalArgumentException.class, () -> new Flight("f",
                "PDX", "SEA", "1/1/2023", "10:39","am", "2:50", "1/2/2023","pm"));

        assertThrows(IllegalArgumentException.class, () -> new Flight(null,
                "PDX", "SEA", "1/1/2023", "10:39","am", "2:50", "1/2/2023","pm"));

        assertThrows(IllegalArgumentException.class, () -> new Flight("12.12",
                "PDX", "SEA", "1/1/2023", "10:39","am", "2:50", "1/2/2023","pm"));
    }

    @Test
    void testSource(){

        assertThat("PDX", equalTo(getValidFlight().getSource()));
    }

    @Test
    void testBadSource() {
        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "PDXw", "dsn", "1/1/2023", "10:39","am", "2:50", "1/2/2023","pm"));

        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "", "dsn", "1/1/2023", "10:39","am", "2:50", "1/2/2023","pm"));

        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                null, "dsn", "1/1/2023", "10:39","am", "2:50", "1/2/2023","pm"));

        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "sr", "dsn", "1/1/2023", "10:39","am", "2:50", "1/2/2023","pm"));

        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "s", "dsn", "1/1/2023", "10:39","am", "2:50", "1/2/2023","pm"));

        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "s12", "dsn", "1/1/2023", "10:39","am", "2:50", "1/2/2023","pm"));

        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "src", "dsn", "1/1/2023", "10:39","am", "2:50", "1/2/2023","pm"));
    }

    @Test
    void testDestination(){

        assertThat("SEA", equalTo(getValidFlight().getDestination()));
    }

    @Test
    void testBadDestination() {
        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "SEA", "dsnbad", "1/1/2023", "10:39","am", "2:50", "1/2/2023","am"));

        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "SEA", "DSN", "1/1/2023", "10:39","am", "2:50", "1/2/2023","am"));
    }

    @Test
    void testDepartureString()
    {
        assertThat("01/01/2023 10:39 AM",equalTo(getValidFlight().getDepartureString()));
    }

    @Test
    void testDepartureDate()
    {
        Flight test= null;
        Date test_departure = null, test_arrival = null;

        try {
            test_departure = new SimpleDateFormat("MM/dd/yyyy hh:mm aa").parse("1/1/2023 10:39 am");
            test_arrival = new SimpleDateFormat("MM/dd/yyyy hh:mm aa").parse("1/2/2023 1:11 pm");

            test = new Flight("1","PDX", "SEA", test_departure,
                    test_arrival);

        } catch (ParseException | IllegalArgumentException ex) {
            fail(ex.getMessage());
        }
        assertThat(0, equalTo(test_departure.compareTo(test.getDeparture())));
    }

    @Test
    void testArrivalString() {

        assertThat("01/02/2023 02:50 PM", equalTo(getValidFlight().getArrivalString()));
    }

    @Test
    void testArrivalDate()
    {
        Flight test= null;
        Date test_departure = null, test_arrival = null;

        try {
            test_departure = new SimpleDateFormat("MM/dd/yyyy hh:mm aa").parse("1/1/2023 10:39 am");
            test_arrival = new SimpleDateFormat("MM/dd/yyyy hh:mm aa").parse("1/2/2023 1:11 am");

            test = new Flight("1","PDX", "SEA", test_departure,
                    test_arrival);

        } catch (ParseException | IllegalArgumentException ex) {
            fail(ex);
        }
        assertThat(0, equalTo(test_arrival.compareTo(test.getArrival())));
    }

    @Test
    void testInvalidYearString() {
        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "PDX", "dsn", "1/1/223", "10:39","am", "2:50", "1/2/2023","pm"));
        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "PDX", "SEA", "1/1/20234", "10:39","am", "2:50", "1/2/2023","pm"));
        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "PDX", "SEA", "1/1/-2023", "10:39","am", "2:50", "1/2/2023","pm"));
        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "PDX", "SEA", "1/1/2f23", "10:39","am", "2:50", "1/2/2023","pm"));
        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "PDX", "SEA", "1/1/0000", "10:39","am", "2:50", "1/2/2023","pm"));
        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "PDX", "SEA", "1/1/23", "10:39","am", "2:50", "1/2/2023","pm"));
    }

    @Test
    void testInvalidMonthString() {
        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "PDX", "SEA", "21/1/2023", "10:39","am", "2:50", "1/2/2023","pm"));
        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "PDX", "SEA", "-12/1/2023", "10:39","am", "2:50", "1/2/2023","pm"));
        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "PDX", "SEA", "0/1/2023", "10:39","am", "2:50", "1/2/2023","pm"));
        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "PDX", "SEA", "123/1/2023", "10:39","am", "2:50", "1/2/2023","pm"));
        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "PDX", "SEA", "/1/2023", "10:39","am", "2:50", "1/2/2023","pm"));
        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "PDX", "SEA", "13/1/2023", "10:39","am", "2:50", "1/2/2023","pm"));
        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "PDX", "SEA", "12/1/2023", "10:39","am", "2:50", "2/32/2023","pm"));
        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "PDX", "SEA", "12/1/2023", "10:39","am", "2:50", "2/30/2023","pm"));
    }

    @Test
    void testInvalidDayString() {
        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "PDX", "SEA", "12/32/2023", "10:39","am", "2:50", "1/2/2023","pm"));
        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "PDX", "SEA", "12/-1/2023", "10:39","am", "2:50", "1/2/2023","pm"));
        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "PDX", "SEA", "12/0/2023", "10:39","am", "2:50", "1/2/2023","pm"));
        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "PDX", "SEA", "12/123/2023", "10:39","am", "2:50", "1/2/2023","pm"));
        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "PDX", "SEA", "2/31/2023", "10:39","am", "2:50", "1/2/2023","pm"));
        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "PDX", "SEA", null, "10:39","am", "2:50", "1/2/2023","pm"));
    }

    @Test
    void testInvalidHourString() {
        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "PDX", "SEA", "12/12/2023", "25:39","am", "2:50", "1/2/2023","pm"));
        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "PDX", "SEA", "12/12/2023", "-1:39","am", "2:50", "1/2/2023","pm"));
        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "PDX", "SEA", "12/12/2023", "100:39","am", "2:50", "1/2/2023","pm"));
        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "PDX", "SEA", "12/12/2023", ":39","am", "2:50", "1/2/2023","pm"));
        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "PDX", "SEA", "12/12/2023", "99:39","am", "2:50", "1/2/2023","pm"));
        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "PDX", "SEA", "12/12/2023", null,"am", "2:50", "1/2/2023","pm"));
        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "PDX", "SEA", "12/12/2023", "13:12","am", "2:50", "1/2/2023","pm"));
    }

    @Test
    void testInvalidMinuteString() {
        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "PDX", "SEA", "12/12/2022", "12:60","am", "2:50", "1/2/2023","pm"));
        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "PDX", "SEA", "12/12/2022", "12:-1","am", "2:50", "1/2/2023","pm"));
        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "PDX", "SEA", "12/12/2022", "12:390","am", "2:50", "1/2/2023","pm"));
        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "PDX", "SEA", "12/12/2022", "12:0","am", "2:50", "1/2/2023","pm"));
        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "PDX", "SEA", "12/12/2022", "12:","am", "2:50", "1/2/2023","pm"));
        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "PDX", "SEA", "12/12/2022", "12:0","am", "2:50", "1/2/2023","pm"));
    }

    @Test
    void testInvalidAMPMString() {
        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "PDX", "SEA", "12/12/2022", "12:50","cm", "2:50", "1/2/2023","pm"));
        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "PDX", "SEA", "12/12/2022", "12:21","ac", "2:50", "1/2/2023","pm"));
        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "PDX", "SEA", "12/12/2022", "12:30","pa", "2:50", "1/2/2023","pm"));
        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "PDX", "SEA", "12/12/2022", "12:01","m", "2:50", "1/2/2023","pm"));
        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "PDX", "SEA", "12/12/2022", "12:11","a", "2:50", "1/2/2023","pm"));
        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "PDX", "SEA", "12/12/2022", "12:10","p", "2:50", "1/2/2023","pm"));
        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "PDX", "SEA", "12/12/2022", "12:10","", "2:50", "1/2/2023","pm"));
        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "PDX", "SEA", "12/12/2022", "12:10",null, "2:50", "1/2/2023","pm"));
        assertThrows(IllegalArgumentException.class, () -> new Flight("123",
                "PDX", "SEA", "12/12/2022", "13:10","pm", "2:50", "1/2/2023","pm"));

    }

    @Test
    void testArrivalBeforeDeparture()
    {
        assertThrows(IllegalArgumentException.class, () -> new Flight("123", "PDX",
                "SEA", "12/12/2023", "12:13","pm", "12:13", "12/11/2023","pm"));

        assertThrows(IllegalArgumentException.class, () -> new Flight("123", "PDX",
                "SEA", "12/11/2023", "12:14","pm", "12:13", "12/11/2023","pm"));
    }

    @Test
    void testDump()
    {
        Flight test = getValidFlight();

        assertThat(test.getDump(), equalTo("1,PDX,01/01/2023,10:39,AM,SEA,01/02/2023,02:50,PM"));
    }

    @Test
    void testPrettyDump()
    {
        Flight test = getValidFlight();

        assertThat(test.getPrettyDump(), equalTo("Flight 1 Departs from Portland, OR at " +
                "10:39 AM on Sunday the 01 of January 2023 and arrives in Seattle, WA at 02:50 PM " +
                "on Monday the 02 of January 2023 lasting 1691 minutes"));
    }

    @Test
    void testDateDump()
    {
        String test = getValidFlight().getDepartureString();

        assertThat(Flight.dateDump(test), equalTo("01/01/2023,10:39,AM"));
    }

    @Test
    void testCloneSameData()
    {
       Flight test = getValidFlight();
       Flight clone = test.clone();

       assertTrue(clone.equals(test));
       assertNotSame(test,clone);
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
        Flight test = new Flight("32", "PDX","SEA", "1/1/2023", "10:23","am", "2:54", "1/2/2023","pm");
        Flight test2 = getValidFlight();

        assertFalse(test.equals(test2));
    }

    @Test
    void testEqualsDifferentClass()
    {
        Flight test = getValidFlight();

        assertFalse(test.equals(" "));
    }


    @Test
    void testCopyConstructor()
    {
        Flight test = getValidFlight();
        Flight copy = new Flight(test);

        assertTrue(test.equals(copy));
        assertThrows(IllegalArgumentException.class, ()->new Flight(null));
    }

    @Test
    void testGetPrettyDump()
    {

        assertThat(getValidFlight().getPrettyDump(), equalTo(validFlightDump));
    }
    protected static Flight getValidFlight() {
        Flight test = null;
        try {
            test = new Flight("1", "PDX", "SEA", "1/1/2023",
                    "10:39","am" , "2:50", "1/2/2023","pm" );
        }
        catch (IllegalArgumentException ex) {
            fail(ex.getMessage());
        }
        return test;
    }

    public static final String validFlightDump = "Flight 1 Departs from Portland," +
            " OR at 10:39 AM on Sunday the 01 of January 2023 and arrives in Seattle, WA at 02:50 " +
            "PM on Monday the 02 of January 2023 lasting 1691 minutes";
}
