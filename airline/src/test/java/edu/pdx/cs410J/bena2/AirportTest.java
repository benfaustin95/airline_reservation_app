package edu.pdx.cs410J.bena2;


import org.junit.jupiter.api.Test;
import org.w3c.dom.Text;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class AirportTest {

    @Test
    public void testAirportInitName()
    {
        Airport test = null;
        try{
            test = new Airport("name");
        }
        catch(IllegalArgumentException ex)
        {
            fail(ex.getMessage());
        }

        assertThat(test.getName(), containsString("name"));

    }
    @Test
    public void testAirportInit()
    {
        Airport test = null;
        try{
            test = new Airport("name",AirlineTest.getValidAirline());
        }
        catch(IllegalArgumentException ex)
        {
            fail(ex.getMessage());
        }

        assertThat(test.getName(), containsString("name"));
        assertThat(test.getAirlines().size(),equalTo(1));
    }

    @Test
    public void testAirportCopyConstructor()
    {
        Airport test = null;

        try {
            test = TextDumperTest.getValidAirport();
            Airport copy = new Airport(test);

            assertFalse(copy.getAirlines() == test.getAirlines());
            assertTrue(copy.equals(test));
            assertTrue(copy.getAirlines().size() == 2);
        }
        catch(IllegalArgumentException ex)
        {
            fail(ex.getMessage());
        }
    }

    @Test
    public void testGetName()
    {
        try {
            Airport test = TextDumperTest.getValidAirport();
            assertThat(test.getName(), equalTo("temp_name"));
        }
        catch (IllegalArgumentException ex)
        {
            fail(ex.getMessage());
        }
    }

    @Test
    public void testToString()
    {
        try {
            Airport test = TextDumperTest.getValidAirport();
            assertThat(test.toString(), equalTo("Airport temp_name has 2 airlines"));
        }
        catch (IllegalArgumentException ex)
        {
            fail(ex.getMessage());
        }
    }

    @Test
    public void testGetAirlines()
    {
        try {
            Airport test = TextDumperTest.getValidAirport();
            assertThat(test.getAirlines().size(), equalTo(2));
        } catch (IllegalArgumentException ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void testAddAirline()
    {
        try {
            Airport test = TextDumperTest.getValidAirport();
            Airline toAdd = AirlineTest.getValidAirline();

            assertThat(test.getAirlines().size(), equalTo(2));
            test.addAirline(toAdd);

            assertThat(test.getAirlines().size(),equalTo(3));
        }
        catch (IllegalArgumentException ex)
        {
            fail(ex.getMessage());
        }
    }


    @Test
    public void testEqualsSameReferenceEquality()
    {
        Airport test = TextDumperTest.getValidAirport();

        assertTrue(test.equals(test));
        assertTrue(test == test);
    }

    @Test
    public void testEqualsLogicalEquivalence()
    {
        Airport test = TextDumperTest.getValidAirport();
        Airport clone = TextDumperTest.getValidAirport();

        assertTrue(test.equals(clone));
        assertFalse(test == clone);
    }

    @Test
    public void testEqualsNotEquivalent()
    {
        Airport test = TextDumperTest.getValidAirport();
        Airport clone = new Airport("name");

        assertFalse(test == clone);
        assertFalse(test.equals(clone));
    }


    @Test
    public void testEqualsDifferentClass()
    {
        Airport test = TextDumperTest.getValidAirport();

        assertFalse(test.equals(" "));
        assertFalse(test.equals(new Object()));
    }

    @Test
    public void testEqualsName()
    {
        Airport test = TextDumperTest.getValidAirport();

        assertTrue(test.equals("temp_name"));
    }
    @Test
    public void testRemoveAirline()
    {
        try {
            Airport test = TextDumperTest.getValidAirport();

            assertTrue(test.removeAirline("name"));
            assertTrue(test.removeAirline("name2"));
            assertThat(test.getAirlines().size(), equalTo(0));
        }
        catch(IllegalArgumentException ex)
        {
            fail(ex.getMessage());
        }
    }

    @Test
    public void testGetAirline()
    {
        try{
            Airport test = TextDumperTest.getValidAirport();
            Airline toGet = null;

            toGet = test.getAirline("name");

            assertThat(toGet.getName(), equalTo("name"));

            toGet = test.getAirline("badName");

            assertNull(toGet);
        }
        catch (IllegalArgumentException ex)
        {
            fail(ex);
        }
    }
}
