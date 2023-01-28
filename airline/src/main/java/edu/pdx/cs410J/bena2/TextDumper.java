package edu.pdx.cs410J.bena2;

import edu.pdx.cs410J.AirlineDumper;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.*;

/**
 * A skeletal implementation of the <code>TextDumper</code> class for Project 2.
 */
public class TextDumper implements AirlineDumper<Airline> {
  private final Writer writer;

  public TextDumper(Writer writer) {
    this.writer = writer;
  }

  @Override
  public void dump(Airline airline) throws IllegalArgumentException {
  }

  public void dump(Airline airline, PrintWriter pw) throws IllegalArgumentException {

    if(airline == null)
      throw new IllegalArgumentException("No airline");

    Iterator<Flight> toDump = airline.getFlights().iterator();
    String name = airline.getName();

      while(toDump.hasNext())
      {
        pw.println(name+","+toDump.next().getDump());
        pw.flush();
      }
    }


  public void dumpAirport(Airport airport) throws IllegalArgumentException
  {
    if(airport == null)
      throw new IllegalArgumentException("Null Airport can not be written to file");
    try (PrintWriter pw = new PrintWriter(this.writer)) {
      for (Airline airline : airport.getAirlines()) {
        dump(airline,pw);
      }
    }
  }

}


