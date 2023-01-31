package edu.pdx.cs410J.bena2;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.*;
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
       assertTrue(test.getFlights().size()==0);
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

        assertTrue(test.equals(clone));
        assertTrue(test.getFlights() != clone.getFlights());
        assertFalse(test == clone);
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
        Flight flight = new Flight("2","src","dsw","1/1/2023", "10:39","1/2/2023","2:40");

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
    public void testGetLastFlight()
    {
        Airline test = getValidAirline();
        test.addFlight(new Flight("2","srx","dsn","1/1/2023","10:20","1/1/2023","19:48"));
        assertTrue(test.getLastFlight().getNumber()==2);

        Airline test2 = new Airline("name");
        assertThrows(NullPointerException.class, test2::getLastFlight);
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