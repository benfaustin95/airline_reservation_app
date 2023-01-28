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
    Airport test = getValidAirport();
    String text = null;

    try (StringWriter sw = new StringWriter())
    {
      TextDumper dumper = new TextDumper(sw);
      dumper.dumpAirport(test);
      text = sw.toString();
    } catch (IOException | IllegalArgumentException e) {
      fail(e.getMessage());
    }
    assertThat(text, containsString("name,1,src"));
  }


  @Test
  void canParseTextWrittenByTextDumper() throws IOException, ParserException {
    Airport test = getValidAirport();

    File textFile = new File("airline.txt");
    TextDumper dumper = new TextDumper(new FileWriter(textFile));
    dumper.dumpAirport(test);

   TextParser parser = new TextParser(new FileReader(textFile));
    Airport test2 = parser.parseAirport();
    assertThat(test.toString(), equalTo(test2.toString()));
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

  public static Airport getValidAirport()
  {
      Airport test = new Airport("temp_name", AirlineTest.getValidAirline());
      test.addAirline(new Airline("name2", FlightTest.getValidFlight()));
      return test;
  }

}
