package edu.pdx.cs410J.bena2;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class AirlineTest{

    @Test
    public void testInitializeAirlineOnlyName()
    {
        Airline test = null;

        try{
            test = new Airline("name");
        }
        catch (IllegalArgumentException ex)
        {
            fail(ex.getMessage());
        }

        assertThat(test.getName(), is(equalTo("name")));
    }

    @Test
    public void testInitializeAirlineNameFlight()
    {
        Airline test = null;
        Flight test_flight = FlightTest.getValidFlight();

        try{
            test = new Airline("name",test_flight);
        }
        catch (IllegalArgumentException ex)
        {
            fail(ex.getMessage());
        }

        assertThat(test.toString(), is(equalTo("name with 1 flights")));
    }

    @Test
    public void testNameGood()
    {
        Airline test = null;

        try{
            test = new Airline("goodName");
        }
        catch (IllegalArgumentException ex)
        {
            fail(ex.getMessage());
        }
        assertThat(test.getName(), is(equalTo("goodName")));
    }

    @Test
    public void testNameBad()
    {
        assertThrows(IllegalArgumentException.class,
                () ->new Airline("", FlightTest.getValidFlight()));

        assertThrows(IllegalArgumentException.class,
                () ->new Airline(null, FlightTest.getValidFlight()));
    }

    @Test
    public void testInitializeWithNullFlight()
    {
       Airline test =  new Airline("name", null);
        assertEquals(0, test.getFlights().size());
    }

    @Test
    public void testGetName()
    {
        Airline test = getValidAirline();

        assertThat(test.getName(), is(equalTo("name")));
    }

    @Test
    public void testAddFlight()
    {
        Airline test = null;

        try{
            test = new Airline("name");
        }
        catch (IllegalArgumentException ex)
        {
            fail(ex.getMessage());
        }

        try{
            test.addFlight(FlightTest.getValidFlight());
        }
        catch(IllegalArgumentException ex)
        {
            fail(ex.getMessage());
        }

        assertThat(test.getFlights().size(), equalTo(1));
    }



    @Test
    public void testGetFlights()
    {
        assertThat(getValidAirline().getFlights().size(), equalTo(1));
    }

    @Test
    public void testGetFlightsEmpty()
    {
       assertThat(new Airline("name").getFlights().size(), equalTo(0));
    }

    @Test
    public void testCloneSameDate()
    {
        Airline test = getValidAirline();
        Airline clone = test.clone();

        assertEquals(test, clone);
        assertNotSame(test.getFlights(), clone.getFlights());
        assertNotSame(test, clone);
    }

    @Test
    public void testCloneChangeData()
    {
        Airline test = getValidAirline();
        Airline clone = test.clone();

        clone.addFlight(FlightTest.getValidFlight());

        assertTrue(test.equals(clone));
        assertFalse(test.getFlights().size() == clone.getFlights().size());
        assertFalse(test.getFlights() == clone.getFlights());
    }

    @Test
    public void testEqualsSameReferenceEquality()
    {
        Airline test = getValidAirline();

        assertTrue(test.equals(test));
        assertTrue(test == test);
    }

    @Test
    public void testEqualsLogicalEquivalence()
    {
       Airline test = getValidAirline();
       Airline clone = getValidAirline();

       assertTrue(test.equals(clone));
       assertFalse(test == clone);
    }

    @Test
    public void testEqualsNotEquivalent()
    {
        Airline test = getValidAirline();
        Airline clone = new Airline("name2");

        assertFalse(test == clone);
        assertFalse(test.equals(clone));
    }


    @Test
    public void testEqualsDifferentClass()
    {
        Airline test = getValidAirline();

        assertFalse(test.equals(new ArrayList<String>()));
    }

    @Test
    public void testEqualsNameEqual()
    {
        Airline test = getValidAirline();

        assertTrue(test.equals("name"));
    }

    @Test
    public void testEqualsNameNotEqual()
    {
        Airline test = getValidAirline();

        assertFalse(test.equals("name2"));
    }

    @Test
    public void testRemoveFlight()
    {
        Airline test = getValidAirline();
        Flight flight = FlightTest.getValidFlight();

        assertTrue(test.removeFlight(flight));
        assertTrue(test.getFlights().isEmpty());
    }

    @Test
    public void testRemoveFlightThatDoesNotExist()
    {
        Airline test = getValidAirline();
        Flight flight = new Flight("2","pdx","sea","1/1/2023", "10:39","am" , "2:40", "1/2/2023","pm");

        assertFalse(test.removeFlight(flight));
    }

    @Test
    public void testRemoveNullFlight()
    {
        Airline test = getValidAirline();

        assertFalse(test.removeFlight(null));
    }

    @Test
    public void testCopyConstructor()
    {
        Airline test = getValidAirline();
        Airline copy = new Airline(test);

        assertTrue(test.equals(copy));
    }

    @Test
    public void testOrdering()
    {
        Airline test = getValidAirline();
        addManyFlightsToAirline(test);

        Iterator<Flight> current = test.getFlights().iterator();

        for(int i=4; i>0; --i)
        {
            assertThat(current.next().getNumber(), equalTo(i));
        }
    }

    @Test
    public void testHashCode()
    {
        assertThat(getValidAirline().hashCode(), equalTo(getValidAirline().toString().hashCode()));
    }
    public static void addManyFlightsToAirline(Airline test) {
        test.addFlight(new Flight("2", "LAX","SEA","1/1/2023","10:23", "am","10:32","1/1/2023","pm"));
        test.addFlight(new Flight("3", "JFK","SEA","1/1/2023","10:23", "am",
                "10:32","1/1/2023", "pm"));
        test.addFlight(new Flight("4", "JFK","SEA","12/1/2022","10:23", "am",
                "10:32","1/1/2023", "pm"));
    }

    @Test
    public void testContainsAirport(){
        Airline airline = getValidAirline();

        assertThat(airline.containsAirport("SEA"), equalTo(true));
        assertThat(airline.containsAirport("JFk"), equalTo(false));
    }

    @Test
    public void testConstructorMatchingSourceAndDestination(){
        Airline airline = getValidAirline();

        airline.addFlight(new Flight("123","JFk","SEA","1/1/2023 10:32 PM", "1/1/2023 10:53 PM"));

        assertThat(new Airline(airline, "JFk", "SEA").getFlights().size(), equalTo(1));
    }

    public static Airline getValidAirline()
    {
       Airline test = null;

       try{
           test = new Airline("name", FlightTest.getValidFlight());
       }
       catch (IllegalArgumentException ex)
       {
           fail(ex.getMessage());
       }
       return test;
    }



}