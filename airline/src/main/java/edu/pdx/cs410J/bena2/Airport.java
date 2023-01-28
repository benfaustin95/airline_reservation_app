package edu.pdx.cs410J.bena2;

import java.util.*;

public class Airport {

    protected Collection<Airline> airlines;
    protected String name;

    protected Airport()
    {
        name = "";
        airlines = new LinkedList<>();
    }

    public Airport(String name)
    {
        this();
        this.name = name;
    }

    public Airport(String name, Airline airline) {
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

    public void addAirline(Airline airline) {
        airlines.add(airline.clone());
    }

    @Override
    public boolean equals(Object object)
    {
        if(super.equals(object))
            return true;
        if(!(object instanceof Airport))
            return false;
        return this.name.equals(((Airport) object).name);
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
