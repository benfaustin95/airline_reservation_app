package edu.pdx.cs410J.bena2;

import edu.pdx.cs410J.AbstractFlight;

public class Flight extends AbstractFlight {

  protected int flightNumber;
  protected String source;
  protected String destination;
  protected String departureDate;
  protected String departureTime;
  protected String arrivalDate;
  protected String arrivalTime;

  public Flight()
  {

  }

  public Flight(String source, String destination, String departureDate, String departureTime, String arrivalDate, String arrivalTime)
  {
    this.source = source;
    this.destination = destination;
    this.departureDate = departureDate;
    this.departureTime = departureTime;
    this.arrivalDate = arrivalDate;
    this.arrivalTime = arrivalTime;
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
  public String getDepartureString() {
    return departureDate + " " + departureTime;
  }

  @Override
  public String getDestination() {
    return destination;
  }

  @Override
  public String getArrivalString() {
    return arrivalDate + " " + arrivalTime;
  }
}
