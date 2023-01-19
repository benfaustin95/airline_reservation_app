package edu.pdx.cs410J.bena2;

import edu.pdx.cs410J.AbstractAirline;

import java.util.ArrayList;
import java.util.Collection;

public class Airline extends AbstractAirline<Flight> {
  private final String name;
  Collection<Flight>  roster;

  public Airline(String name) {
    this.name = name;
    roster = new ArrayList<Flight>();
  }

 public Airline(String name, Flight flight)
 {
   this.name = name;
   roster = new ArrayList<Flight>();
   roster.add(flight);
 }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public void addFlight(Flight flight) {
    roster.add(flight);
  }

  @Override
  public Collection<Flight> getFlights() {
    return roster;
  }
}
