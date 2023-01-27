package edu.pdx.cs410J.bena2;

import java.util.*;

public class Airport {

    protected Collection<Airline> airlines;
    protected String name;

    public Airport()
    {
        name = "";
        airlines = new LinkedList<>();
    }

    public Airport(String name)
    {
        this();
        this.name = name;
    }

    public Airport(String name, Airline airline) throws CloneNotSupportedException {
        this(name);
        addAirline(airline);
    }

    public Airport(Airport airport) throws CloneNotSupportedException {
       this(airport.name);

       for(Airline airline: airport.airlines)
       {
           this.airlines.add(airline.clone());
       }

    }

    public String getName()
    {
        return this.name;
    }

    public Collection<Airline> getAirlines()
    {
        return this.airlines;
    }

    public void addAirline(Airline airline) throws CloneNotSupportedException {
        airlines.add(new Airline(airline));
    }

    @Override
    public boolean equals(Object object)
    {
        if(this == object)
            return true;
        if(this.getClass() != object.getClass())
            return false;
        if(this.name.equals(((Airport)object).name))
            return true;
        return false;
    }

    public boolean removeAirline(Airline airline)
    {
        if(airline == null)
            return false;

        return airlines.remove(airline);
    }

    public Airline getAirline(String aName) {

        Iterator<Airline> temp = airlines.iterator();
        Airline toReturn = null;

        if(name == null)
            return null;

        while(temp.hasNext())
        {
            toReturn = temp.next();
            if(toReturn.equals(aName))
                return toReturn;
        }

        return null;
    }
}
