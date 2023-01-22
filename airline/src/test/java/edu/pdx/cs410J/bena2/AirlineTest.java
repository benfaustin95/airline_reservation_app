package edu.pdx.cs410J.bena2;

import org.junit.jupiter.api.Test;

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
       assertThrows(IllegalArgumentException.class, ()->new Airline("name", null));
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
    public void testAddNullFlight(){

        assertThrows(IllegalArgumentException.class, () -> getValidAirline().addFlight(null));
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