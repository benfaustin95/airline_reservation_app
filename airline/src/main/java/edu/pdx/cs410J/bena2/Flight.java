package edu.pdx.cs410J.bena2;

import edu.pdx.cs410J.AbstractFlight;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;

/**
 * The Flight class holds the data related to a Flight instance. The Flight class overrides all
 * abstract methods present in the Abstract Flight class. In addition, the class provides validation
 * methods to ensure that invalid data is not stored within the fields of the class. The state
 * information of a flight includes.
 * <ul>
 *     <li> Flight Number: The unique flight number </li>
 *     <li> Source: The location from which the flight departs </li>
 *     <li> Destination: The location where the flight lands </li>
 *     <li> Departure: The Date and Time the flight departs </li>
 *     <li> Arrival: The Date and Time the flight lands </li>
 * </ul>
 */
public class Flight extends AbstractFlight implements Cloneable{

  protected int flightNumber;
  protected String source;
  protected String destination;
  protected Date departure;
  protected Date arrival;

  /**
   * Flight serves as the default constructor for the Flight class
   */
  private Flight()
  {

  }


  /**
   * Flight serves as the constructor for the Flight class. The Flight constructor sets all fields to
   * value of the corresponding parameter.
   * @param flightNumber  A String holding the flight number
   * @param source        A String holding the three letter source location identifier.
   * @param destination   A String holding the three letter destination location identifier.
   * @param departureDate A String holding the flight's departure date
   * @param departureTime A String holding the flight's departure time
   * @param arrivalDate   A String holding the flight's arrival date
   * @param arrivalTime   A String holding the flight's arrival time
   * @throws IllegalArgumentException Not thrown within the function but rather the methods called by
   *                                  the constructor and not caught.
   */
  public Flight(String flightNumber, String source, String destination, String departureDate,
                String departureTime, String arrivalDate, String arrivalTime)
          throws IllegalArgumentException
  {
    this();
    this.flightNumber = validateNumber(flightNumber);
    this.source = validateLocation(source,0);
    this.destination = validateLocation(destination,1);
    this.departure = validateDateAndTime(departureDate,departureTime,0);
    this.arrival = validateDateAndTime(arrivalDate, arrivalTime,1);
    validateOrder(departure,arrival);
  }

  /**
   * Flight serves as the constructor for the Flight class. The Flight constructor sets all fields to
   * the value fo the corresponding parameter.
   * @param flightNumber  A String holding the flight number
   * @param source        A String holding the three letter source location identifier.
   * @param destination   A String holding the three letter destination location identifier.
   * @param departureDate A Date holding the flight's departure date and time.
   * @param arrivalDate   A Date holding the flight's arrival date and time.
   * @throws IllegalArgumentException Not thrown by the constructor but rather by methods called by
   *                                  the constructor and not caught.
   */
  public Flight(String flightNumber, String source, String destination, Date departureDate,
                Date arrivalDate) throws IllegalArgumentException
  {

    this.flightNumber = validateNumber(flightNumber);
    this.source = validateLocation(source,0);
    this.destination = validateLocation(destination,1);
    validateOrder(departureDate,arrivalDate);
    this.departure = new Date(departureDate.getTime());
    this.arrival = new Date(arrivalDate.getTime());

  }

  /**
   * Flight serves as the copy constructor for the Flight class. The Flight copy constructor makes a
   * deep copy of all fields in the source flight.
   * @param flight A Flight object to be copied
   * @throws IllegalArgumentException Thrown if the flight reference supplied is null
   */
  public Flight(Flight flight) throws IllegalArgumentException {

    if(flight == null)
      throw new IllegalArgumentException("Flight object can not be " +
              "instantiated with a copy of a null Flight reference");
    this.flightNumber = flight.flightNumber;
    this.source = flight.source;
    this.destination = flight.destination;
    this.departure = flight.departure;
    this.arrival = flight.arrival;

  }

  /**
   * getNumber overrides the Abstract Flight's getNumber method, returns the flight's
   * unique identification number
   * @return an Integer
   */
  @Override
  public int getNumber() {
    return flightNumber;
  }

  /**
   * getSource overrides the Abstract Flight's getSource method, returns a reference to
   * the Flight's source.
   * @return  A reference to the flight's source String.
   */
  @Override
  public String getSource() {
    return source;
  }

  /**
   * getDestination overrides the Abstract Flight's getDestination method, returns a reference to
   * the Flight's destination.
   * @return A reference to the flight's destination string.
   */
  @Override
  public String getDestination() {
    return destination;
  }

  /**
   * getDeparture overrides the Abstract Flight's getDeparture method, returns a reference to the
   * Flight's departure Date instance.
   * @return A reference to the departure Date instance.
   */
  @Override
  public Date getDeparture() {
    return this.departure;
  }

  /**
   * getArrival overrides the Abstract Flight's getArrival method, returns a reference to the
   * Flight's arrival Date instance.
   * @return A reference to the arrival Date instance.
   */
  @Override
  public Date getArrival() {
    return this.arrival;
  }

  /**
   * getArrivalString overrides the Abstract Flight's getArrivalString method, returns a
   * reference to the Flight's arrival Date instance represented by a String.
   * @return A reference to a String representation of the arrival Date instance.
   */
  @Override
  public String getArrivalString(){

    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");

    return dateFormat.format(arrival);
  }

  /**
   * getDepartureString overrides the Abstract Flight's getDepartureString method, returns a
   * reference to the Flight's departure Date instance represented by a String.
   * @return A reference to a String representation of the departure Date instance.
   */
  @Override
  public String getDepartureString(){

    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");

    return dateFormat.format(departure);
  }

  /**
   * validateNumber validates that the String passed in is a valid Flight Number. The Flight Number
   * must be a valid integer.
   * @param number The String to be tested.
   * @return An Integer holding the validated flight number.
   * @throws IllegalArgumentException Thrown if the String supplied is not a valid flight number.
   */
  public static int validateNumber(String number) throws IllegalArgumentException {
    Integer rnumber = null;
    if(number == null)
      throw  new IllegalArgumentException("null flight number not accepted");
    try {
      rnumber = Integer.parseInt(number.trim());
      if(rnumber <= 0)
        throw new IllegalArgumentException("Flight Number " + number + " is invalid, must be greater than zero.");
    }
    catch(NumberFormatException ex){
      throw new IllegalArgumentException("Flight Number " + number + " is invalid, must be numeric.");
    }
    return rnumber;
  }

  /**
   * validateLocation validates that the String passed in is a valid location. The location must
   * be three alphabetic characters.
   * @param location The location String to be tested.
   * @return A reference to the validated location String.
   * @throws IllegalArgumentException Thrown if the string supplied is not a valid location.
   */
  public static String validateLocation(String location, int type) throws IllegalArgumentException {
    if(location == null)
      throw new IllegalArgumentException("Location can not be null");

    location = location.trim();

    if(location.length()!= 3 || !location.matches("[A-za-z]{3}"))
      throw new IllegalArgumentException((type == 0 ? "Source ": "Destination ")+"Location " +
              location + " is invalid, format must be three alphabetic letters.");
    return location;
  }

  /**
   * validateDateAndTime validates that the Strings passed in are valid Date and Time representations.
   * The Date must be in the following format: MM/dd/YYYY
   * The time must be in the following format: HH:mm (24 hour time)
   * @param date A String holding the date to be tested.
   * @param time A String holding the time to be tested.
   * @return A reference to a Date object containing the valid date and time.
   * @throws IllegalArgumentException Thrown if either the date or time passed in is not valid.
   */
  public static Date validateDateAndTime(String date, String time, int type) throws IllegalArgumentException
  {
    DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm");
    df.setLenient(false);
    Date rdate = null;

    if(date == null || time == null)
      throw new IllegalArgumentException("Null arguments are not accepted");

    date = date.trim();
    time = time.trim();

    if(!date.matches("(0?[1-9]|1[0-2])\\/(0?[1-9]|1[0-9]|2[0-9]|3[01])\\/\\d{4}"))
      throw new IllegalArgumentException((type == 0 ? "Departure ": "Arrival ")+"date "+date+" " +
              "is invalid, date must be in format mm/dd/yyyy");
    if(!time.matches("([01]?[0-9]|2[0-4]):[0-5][0-9]"))
      throw new IllegalArgumentException((type == 0 ? "Departure ": "Arrival ")+"time "+time
              +" is invalid, time must be in format hh:mm (24 hour time)");
    try {
      rdate = df.parse(date + " " + time);
    }
    catch (ParseException ex)
    {
      throw new IllegalArgumentException((type == 0 ? "Departure ": "Arrival ") +"date and time "+
              date + " " + time + " is invalid, date must be in format MM/dd/yyyy hh:mm (24 hour time) " +
              "and exist");
    }

    return rdate;
  }

  /**
   * validateOrder ensures that the departure date occurs before the arrival date.
   * @param departure the Date representation of departure
   * @param arrival the Date representation of arrival
   * @throws IllegalArgumentException Thrown if arrival occurs on or before departure.
   */
  public static void validateOrder(Date departure, Date arrival) throws IllegalArgumentException
  {
    SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm");
    if(!departure.before(arrival))
      throw new IllegalArgumentException("Arrival ("+df.format(arrival)+") can not occur before or" +
              " in conjunction with Departure ("+df.format(departure)+")");
  }


  /**
   * getDump returns a representation of the Flight instance as a String in the required file output
   * format
   * @return a String representation of the Flight object ready for output to a file.
   */
  public String getDump()
  {
    return getNumber() + "," +
            getSource() + ',' +
            dateDump(getDepartureString()) + ',' +
            getDestination() + "," +
            dateDump(getArrivalString());
  }

  /**
   *  dateDump creates a new string that deliminates date and time with a comma rather than a space.
   * @param date the Date string representation to be modified.
   * @return a reference to the new string holding the modified date.
   */
  protected  static String dateDump(String date)
  {
    return date.replace(' ', ',');
  }

  /**
   * clone overrides the clone method present in the Object super class. The implementation
   * calls the clone method of the object class, then makes a deep copy of mutable objects.
   * @return a Flight that is a deep copy of the current Flight instance.
   */
  @Override
  protected Flight clone()
  {
    Flight clone = null;

    try {
      clone = (Flight) super.clone();
      clone.departure = new Date(departure.getTime());
      clone.arrival = new Date(arrival.getTime());
    }
    catch (CloneNotSupportedException ex)
    {
      ex.printStackTrace();
    }
    return clone;
  }

  /**
   * equals overrides the equals method present in the object super class. The implementation calls
   * the clone method to determine if both references reference the same object. If the clone method
   * returns false the implementation then compares the class type, if both references are of the
   * same class type then the flight number is compared to determine absolute equality.
   * @param toCompare an Object reference to be compared to the current flight instance.
   * @return true or false depending on the equality.
   */
  @Override
  public boolean equals(Object toCompare)
  {
    if(super.equals(toCompare))
      return true;
    if(!(toCompare instanceof Flight))
      return false;
    return flightNumber == ((Flight)toCompare).flightNumber;
  }

}
