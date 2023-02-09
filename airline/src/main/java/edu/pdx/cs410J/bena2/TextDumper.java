package edu.pdx.cs410J.bena2;

import edu.pdx.cs410J.AirlineDumper;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.*;

/**
 * The TextDumper class handles outputting the provided airline via comma seperated as well
 * as error checking output. The TextDumper class implements the AirlineDumper class,
 * thus it implements the dump method, which is of void return type and takes an airline reference.
 * <ul>
 *     <li> Writer: Handles writing out character data to the desired writer </li>
 * </ul>
 */
public class TextDumper implements AirlineDumper<Airline> {
  private final Writer writer;

  /**
   * TextDumper acts as the primary constructor for the TextDumper class. The Writer field is
   * set to the value passed in.
   * @param writer a Writer holding the outputstream used to write to the file.
   */
  public TextDumper(Writer writer) {
    this.writer = writer;
  }

  /**
   * dump Dumps the airline and flight to the outputstream referenced by the writer. Implements the
   * dump method of the AirlineDumper interface.
   * @param airline the Airline to be "dumped" to the file.
   * @throws IllegalArgumentException Thrown if any output issues are encountered while dumping.
   */
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

}


