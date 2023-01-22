package edu.pdx.cs410J.bena2;

import edu.pdx.cs410J.AbstractFlight;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;

public class Flight extends AbstractFlight {

  protected int flightNumber;
  protected String source;
  protected String destination;
  protected Date departure;
  protected Date arrival;

  private Flight()
  {

  }


  public Flight(String flightNumber, String source, String destination, String departureDate,
                String departureTime, String arrivalDate, String arrivalTime)
          throws IllegalArgumentException
  {
    this.flightNumber = validateNumber(flightNumber);
    this.source = validateLocation(source);
    this.destination = validateLocation(destination);
    this.departure = validateDateAndTime(departureDate,departureTime);
    this.arrival = validateDateAndTime(arrivalDate, arrivalTime);

  }
  public Flight(String flightNumber, String source, String destination, Date departureDate,
                Date arrivalDate) throws IllegalArgumentException
  {

    this.flightNumber = validateNumber(flightNumber);
    this.source = validateLocation(source);
    this.destination = validateLocation(destination);
    this.departure = new Date(departureDate.getTime());
    this.arrival = new Date(arrivalDate.getTime());

  }

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
  @Override
  public int getNumber() {
    return flightNumber;
  }

  @Override
  public String getSource() {
    return source;
  }

  @Override
  public String getDestination() {
    return destination;
  }

  @Override
  public Date getDeparture() {
    return this.departure;
  }

  @Override
  public Date getArrival() {
    return this.arrival;
  }

  @Override
  public String getArrivalString(){

    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");

    return dateFormat.format(arrival);
  }

  @Override
  public String getDepartureString(){

    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");

    return dateFormat.format(departure);
  }
  public static int validateNumber(String number) throws IllegalArgumentException {
    Integer rnumber = null;
    if(number == null)
      throw  new IllegalArgumentException("null flight number not accepted");
    try {
      rnumber = Integer.parseInt(number);
      if(rnumber <= 0)
        throw new IllegalArgumentException("Flight Number= " + number + " must be greater than zero");
    }
    catch(NumberFormatException ex){
      throw new IllegalArgumentException("Flight Number= " + number + " must be numeric");
    }
    return rnumber;
  }

  public static String validateLocation(String location) throws IllegalArgumentException {
    if(location == null || location.length()!= 3 || !location.matches("[A-za-z]{3}"))
      throw new IllegalArgumentException("location = " + location + " must be three alphabetic letters");
    return location;
  }

  public static Date validateDateAndTime(String date, String time) throws IllegalArgumentException
  {
    DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm");
    df.setLenient(false);
    Date rdate = null;
    if(date == null || time == null)
      throw new IllegalArgumentException("Null arguments are not accepted");
    if(!date.matches("(0?[1-9]|1[0-2])\\/(0?[1-9]|1[0-9]|2[0-9]|3[01])\\/\\d{4}"))
      throw new IllegalArgumentException("Date = "+date+" must be in mm/dd/yyyy");
    if(!time.matches("([01]?[0-9]|2[0-4]):[0-5][0-9]"))
      throw new IllegalArgumentException("Time = "+time
              +" must be in hh:mm (24 hour time");
    try {
      rdate = df.parse(date + " " + time);
    }
    catch (IllegalArgumentException ex)
    {
      throw new IllegalArgumentException("Date ="+ date + " "+time+" must exist");
    }
    catch (ParseException ex)
    {
      throw new IllegalArgumentException("Date= "+ date + " " + time +
              " must be in format mm/dd/yyyy hh:mm (24 hour time");
    }

    return rdate;
  }
}
