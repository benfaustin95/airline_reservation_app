package edu.pdx.cs410J.bena2;

import edu.pdx.cs410J.AirlineDumper;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.*;

public class TextDumper implements AirlineDumper<Airline> {
  private final Writer writer;

  public TextDumper(Writer writer) {
    this.writer = writer;
  }

  @Override
  public void dump(Airline airline) throws IllegalArgumentException {

    if (airline == null)
      throw new IllegalArgumentException("No Airline is available to dump to file");

    try (PrintWriter pw = new PrintWriter(writer)) {
      Iterator<Flight> toDump = airline.getFlights().iterator();
      String name = airline.getName();

      if(!toDump.hasNext()) {
        pw.println(name);
        pw.flush();
      }
      while (toDump.hasNext()) {
        pw.println(name + "," + toDump.next().getDump());
        pw.flush();
      }
    }
  }


  public void dumpAirport(Airport airport) throws IllegalArgumentException
  {
    if(airport == null)
      throw new IllegalArgumentException("Null Airport can not be written to file");

    for (Airline airline : airport.getAirlines()) {
      dump(airline);
    }
  }

}


