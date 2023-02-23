package edu.pdx.cs410J.bena2;

import edu.pdx.cs410J.AbstractAirline;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * The Airline class holds the data related to an airline instance, including the airline name and
 * roster (collection of flights). The airline class overrides all abstract methods present in the
 * abstract airline class. In addition, the class provides validation methods to ensure that invalid
 * data is not stored within the fields of the class. The state information of an airline includes:
 * <ul>
 *     <li> Name : A String holding the name of the airline</li>
 *     <li> Roster: A collection of Flight instances related to the airline</li>
 * </ul>
 */
public class Airline extends AbstractAirline<Flight> implements Cloneable{
    // The name of the airline
    private String name;
    // The roster of flights belonging to the airline
    ArrayList<Flight>  roster;

    /**
     * Airline serves as the default constructor for the Airline class, sets all fields to their
     * zero equivalent value. Can not be used to instantiate an airline object outside the scope of the
     * airline class.
     */
    private Airline(){
        this.name = "";
        roster = new ArrayList<>();
    }

    /**
     * Airline serves as the constructor for teh Airline class, calls the default constructor then
     * sets the airlines name to the value of the string passed in. Is invoked if an airline needs
     * to be created with a name and no flights.
     * @param name A String containing the name of the airline should be set to.
     * @throws IllegalArgumentException Not thrown within the constructor but rather within
     *                                  validate name and not caught as the error message should
     *                                  be passed to calling routine.
     */
    public Airline(String name) throws IllegalArgumentException{
        this();
        this.name = validateName(name);
    }

    /**
     * Airline serves as the copy constructor for the Airline class, the implementation
     * creates a deep copy of the Airline object passed in.
     * @param airline the Airline object to be copied.
     * @throws IllegalArgumentException Not thrown within the constructor but rather by methods
     *                                  invoked within the constructor and not caught as the error
     *                                  should be passed to the calling routine.
     */
    public Airline(Airline airline) throws IllegalArgumentException{
        this(airline.name);

        for (Flight flight : airline.roster) {
            this.roster.add(new Flight(flight));
        }
    }

    /**
     * Airline serves as the constructor for the Airline class, calls the single argument constructor
     * and calls addFight passing in the Flight object that should be added to the airline.
     * @param name A String containing the name of the airline.
     * @param flight An instance of a flight that should be added to the airlines roster.
     * @throws IllegalArgumentException Not thrown within constructor but rather by methods invoked within
     *                                  the constructor and not caught.
     */
    public Airline(String name, Flight flight) throws IllegalArgumentException
    {
        this(name);
        addFlight(flight);
    }

    /**
     * getName overrides the get name method in the AbstractAirline class, returns a reference to the
     * airline's name.
     * @return A reference to the String containing the airline's name.
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * addFlight overrides the addFlight method in the AbstractAirline class, copies the flight instance supplied into the airlines roster.
     * @param flight A Flight instance holding referencing the flight to be copied into the airline's
     *               roster.
     * @throws IllegalArgumentException Not thrown within function but rather by methods invoked by
     *                                  the function and not caught.
     */
    @Override
    public void addFlight(Flight flight) throws IllegalArgumentException {
        if(flight == null)
            return;
        roster.add(new Flight(flight));

    }

    /**
     * getFlights overrides the addFlight method in the AbstractAirline class, returns reference to the airline's roster.
     * @return A reference to the collection of flights stored in the airline
     */
    @Override
    public Collection<Flight> getFlights() {
        Collections.sort(roster);
        return roster;
    }

    /**
     * validateName validates that the String passed is a valid name.
     * @param name The String object to be validated.
     * @return A reference to the valid String.
     * @throws IllegalArgumentException Thrown if the string is not a valid name.
     */
    public static String validateName(String name) throws IllegalArgumentException{
        if(name == null || name.isEmpty() || name.isBlank())
            throw new IllegalArgumentException("Name " +name + " is invalid, must not be empty.");
        return name.trim();
    }


    /**
     * clone overrides the clone method present in the Object super class. The implementation
     * calls the clone method of the object class, then makes a deep copy of mutable objects.
     * @return an Airline that is a deep copy of the current Airline instance.
     */
    @Override
    protected Airline clone()
    {
        Airline clone = null;

        try {
            clone = (Airline) super.clone();
            clone.roster = new ArrayList<>();
            for(Flight flight: roster)
            {
                clone.roster.add(flight.clone());
            }
        }
        catch (CloneNotSupportedException ex)
        {
            ex.printStackTrace();
        }

        return clone;
    }

    /**
     * equals overrides the equals method present in the object super class. The implementation calls
     * the equals method of the super class to determine whether the references reference the same
     * identity. If the super class method returns false the implementation then compares the class
     * type then the airline name is compared to determine absolute equality.
     * @param object the Object whose equality is to be compared.
     * @return true if the objects are logically equivalent and false if not.
     */
    @Override
    public boolean equals(Object object)
    {
        if(super.equals(object))
            return true;
        if(!(object instanceof Airline))
            return false;
        return this.name.equals(((Airline)object).name);
    }

    /**
     * equals overloads the equals method to take a String to determine equality based on the unique
     * airline name.
     * @param toCompare the String representation of the name to compare.
     * @return true if the airline name matches the supplied argument and false if not.
     */
    public boolean equals(String toCompare)
    {
        return toCompare.equals(name);
    }

    /**
     * removeFlight removes a flight from the airlines roster
     * @param flight the flight object to be removed
     * @return true if the object exists and was removed and false if the object does not exist.
     */
    public boolean removeFlight(Flight flight)
    {
        if(flight == null)
            return false;

        return roster.remove(flight);
    }

    /**
     * hashCode utilizes the toString method to calculate a unique hashcode for the Airline. The
     * method is overridden for consistency as the Airline method also overrides the equals method.
     * @return returns an integer containing the unique hashcode.
     */
    @Override
    public int hashCode()
    {
        return toString().hashCode();
    }

    /**
     * containsAirport determines whether an airport is within the airlines flight roster.
     * @param s a String referencing the name of the airport to be checked.
     * @return true if the airport is present false if not.
     */
    public boolean containsAirport(String s) {

        for(Flight flight: roster)
        {
            if(flight.getSource().equalsIgnoreCase(s)||flight.getDestination().equalsIgnoreCase(s))
                return true;
        }
        return false;
    }
}
