package edu.pdx.cs410J.bena2;

import edu.pdx.cs410J.ParserException;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class TextDumperTest {

  @Test
  void airlineNameIsDumpedInTextFormat() {
    String airlineName = "Test Airline";
    Airline airline = new Airline(airlineName,FlightTest.getValidFlight());

    StringWriter sw = new StringWriter();
    TextDumper dumper = new TextDumper(sw);
    dumper.dump(airline);

    String text = sw.toString();
    assertThat(text, containsString(airlineName));
  }


  @Test
  void canParseTextWrittenByTextDumper(@TempDir File tempDir) throws IOException, ParserException {
    String airlineName = "Test Airline";
    Airline airline = new Airline(airlineName,FlightTest.getValidFlight());

    File textFile = new File(tempDir, "airline.txt");
    TextDumper dumper = new TextDumper(new FileWriter(textFile));
    dumper.dump(airline);

    TextParser parser = new TextParser(new FileReader(textFile));
    Airline read = parser.parse();
    assertThat(read.getName(), equalTo(airlineName));
  }

  @Test
  void canDumpIntoFile()
  {
    Airline test = null;

    try(FileWriter file = new FileWriter("test.txt"))
    {
      test = AirlineTest.getValidAirline();
      TextDumper dumper = new TextDumper(file);
      dumper.dump(test);
    }
    catch(IOException ex) {

    }
    assertTrue(true);
  }

  @Test
  void canParseIntoAirline()
  {
    Airport test = new Airport("temp_name");
    try(FileReader file = new FileReader("test.txt")) {

      TextParser parser = new TextParser(file);
      test = parser.parseAirport();
    }
    catch(IOException |ParserException ex)
    {
        fail(ex.getMessage());
    }

    assertThat(test.getAirlines().size(), equalTo(1));
    Airline temp = test.getAirline("name");
    for(Flight flight: temp.getFlights())
    {
      System.out.println(flight.toString());
    }
  }
}
