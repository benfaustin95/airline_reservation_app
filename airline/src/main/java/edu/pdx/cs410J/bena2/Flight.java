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

  public Flight()
  {

  }

  public Flight(int flightNumber, String source, String destination, String departureDate,
                String departureTime, String arrivalDate, String arrivalTime)
                throws ParseException
  {
    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");

    this.flightNumber = flightNumber;
    this.source = source;
    this.destination = destination;
    this.departure = dateFormat.parse(departureDate+" "+departureTime);
    this.arrival = dateFormat.parse(arrivalDate+" "+arrivalTime);

  }

  public Flight(int flightNumber, String source, String destination, Date departureDate,
                Date arrivalDate)
  {

    this.flightNumber = flightNumber;
    this.source = source;
    this.destination = destination;
    this.departure = new Date(departureDate.getTime());
    this.arrival = new Date(arrivalDate.getTime());

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
}
