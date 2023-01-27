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
  public void dump(Airline airline) {

    if(airline == null)
        throw new IllegalArgumentException("No airline");

    Iterator<Flight> toDump = airline.getFlights().iterator();
    String name = airline.getName();

    try (
      PrintWriter pw = new PrintWriter(this.writer)
      ) {

      while(toDump.hasNext())
      {
        pw.println(name+","+toDump.next().getDump());
      }
    }
  }

}


