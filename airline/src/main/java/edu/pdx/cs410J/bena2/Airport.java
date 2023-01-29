package edu.pdx.cs410J.bena2;

import java.util.*;

public class Airport {

    protected Collection<Airline> airlines;
    protected String name;

    /**
     * Airport serves as the default constructor for the Airport class, the implementation
     * initializes all fields to their zero equivalent value. The default constructor is
     * protected, thus can not be used to instantiate am Airport object outside the scope of
     * the Airport class.
     */
    protected Airport()
    {
        name = "";
        airlines = new LinkedList<>();
    }

    /**
     * Airport serves as a constructor for the Airport class, the implementation
     * calls the default constructor (outlined above) then sets the name field to reference
     * the String passed in. It is invoked when an Airport is initialized with no Airlines.
     * Is public and can thus be used to instantiate an Airport.
     * @param name a String reference to the name the Airport should be set to.
     * @throws IllegalArgumentException Not thrown within the constructor but rather
     *                                  by the validateName method and not caught as
     *                                  error message should be passed to the calling
     *                                  routine.
     */
    public Airport(String name) throws IllegalArgumentException
    {
        this();
        this.name = Airline.validateName(name);
    }

    /**
     * Airport serves as a constructor for the Airport class, the implementation
     * calls the  constructor (outlined above) and passes the name argument provided.
     * The implementation then calls the addAirline method with the airline argument
     * provided.
     * @param name  a String reference to the name the Airport should be set to.
     * @param airline A reference to the Airline object that should be added to the
     *                airport.
     * @throws IllegalArgumentException Not thrown within the constructor but rather
     *                                  the Airport constructor called within the
     *                                  method and not caught as error message should
     *                                  be passed to the calling routine.
     */
    public Airport(String name, Airline airline) throws IllegalArgumentException{
        this(name);
        addAirline(airline);
    }

    /**
     * Airport serves the copy constructor for the Airport class, the implementation
     * creates a deep copy of the Airport object passed in.
     * @param airport the Airport object to be copied
     * @throws IllegalArgumentException Not thrown within the constructor but rather in methods
     *                                  invoked by the constructor, error is thrown to calling
     *                                  routine.
     */
    public Airport(Airport airport) throws IllegalArgumentException {
       this(airport.name);
       for(Airline airline: airport.airlines)
       {
           this.airlines.add(new Airline(airline));
       }

    }

    /**
     * getName returns a reference to the Airport's name.
     * @return a String reference to the Airport's name.
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * toString concatenates and returns String representation of the Airport instance.
     * @return a String representation of the Airport instance.
     */
    public String toString()
    {
        return "Airport " + getName()+" has " + getAirlines().size() + " airlines";
    }

    /**
     * getAirlines returns a reference to the collection of Airlines stored within the Airport.
     * @return a reference to a Collection of airlines.
     */
    public Collection<Airline> getAirlines()
    {
        return this.airlines;
    }

    /**
     * addAirline adds the Airline object passed in to the collection of Airline's stored
     * within the Airport.
     * @param airline a reference to the airline to be stored.
     * @throws IllegalArgumentException Not thrown by the method but rather by methods invoked by
     *                                  the methods, error is passed to the calling routine.
     */
    public void addAirline(Airline airline) throws IllegalArgumentException {
        airlines.add(new Airline(airline));
    }

    /**
     * equals overrides the equals methods present in the object super class. The implementation calls
     * the equals method of the super class which determines the reference equality. If the objects are
     * not reference equal, the implementation compares the class type and finally determines absolute
     * equality by comparing the names of both objects.
     * @param object a reference to the Object to be compared.
     * @return true if the objects are logically equivalent, false if not.
     */
    @Override
    public boolean equals(Object object)
    {
        if(super.equals(object))
            return true;
        if(object instanceof String && ((String)object).equals(name))
            return true;
        if(!(object instanceof Airport))
            return false;
        return this.name.equals(((Airport) object).name);
    }

    /**
     * removeAirline removes the Airline provided from the Airport if the airline exists withing the
     * airport.
     * @param aName a reference to a String containing the name of the Airline to be removed.
     * @return true if the Airline has been removed false if it was not present within the Airport.
     */
    public boolean removeAirline(String aName)
    {
        if(aName == null)
            return false;

        return airlines.removeIf(n ->(n.equals(aName)));
    }

    /**
     * getAirline searches the Airport to identify if an airline with the name provided is present
     * within the Airport. If the Airline is present a reference to the object is returned, otherwise
     * a null reference is returned.
     * @param aName a String referencing the name to be searched.
     * @return a reference to the Airline identified, or a null reference.
     */
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
