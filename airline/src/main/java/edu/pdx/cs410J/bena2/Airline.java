package edu.pdx.cs410J.bena2;

import edu.pdx.cs410J.AbstractAirline;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Collection;

public class Airline extends AbstractAirline<Flight> {
    private String name;
    Collection<Flight>  roster;

    private Airline(){
        this.name = "";
        roster = new ArrayList<Flight>();
    }
    public Airline(String name) throws IllegalArgumentException{
        this();
        this.name = validateName(name);
    }

    public Airline(String name, Flight flight) throws IllegalArgumentException
    {
        this(name);
        addFlight(flight);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void addFlight(Flight flight) throws IllegalArgumentException {
        roster.add(new Flight(flight));
    }

    @Override
    public Collection<Flight> getFlights() {
        return roster;
    }

    public static String validateName(String name) throws IllegalArgumentException{
        if(name == null || name.isEmpty())
            throw new IllegalArgumentException("Airline Name = " +name + " must not be empty");
        return name;
    }
}
