package edu.pdx.cs410J.bena2;

import edu.pdx.cs410J.ParserException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

public class TextDumperTest {

  @Test
  void airlineNameIsDumpedInTextFormat() {
    Airline test = AirlineTest.getValidAirline();
    String text = null;

    try (StringWriter sw = new StringWriter())
    {
      for(int i =0; i<10; ++i)
      {
        test.addFlight(FlightTest.getValidFlight());
      }

      TextDumper dumper = new TextDumper(sw);
      dumper.dump(test);
      text = sw.toString();
    } catch (IOException | IllegalArgumentException e) {
      fail(e.getMessage());
    }
    assertThat(text.length(), equalTo(539));
  }


  @Test
  void canParseTextWrittenByTextDumper(@TempDir File dir) throws IOException, ParserException {
    Airline test = AirlineTest.getValidAirline();

    File textFile = new File(dir,"airline.txt");
    TextDumper dumper = new TextDumper(new FileWriter(textFile));
    dumper.dump(test);

   TextParser parser = new TextParser(new FileReader(textFile));
    Airline test2 = parser.parse();
    assertThat(test.toString(), equalTo(test2.toString()));
  }

  @Test
  void canDumpIntoFile(@TempDir File dir)
  {
    Airline test, test2;
    File file = new File(dir, "test.txt");
    File file2 = new File(dir, "test2.txt");
    try(FileWriter fw= new FileWriter(file))
    {
      test = AirlineTest.getValidAirline();
      TextDumper dumper = new TextDumper(fw);
      dumper.dump(test);

      FileReader fr = new FileReader(file);
      TextParser parser = new TextParser(fr);
      test2 = parser.parse();
      fr.close();

      FileWriter fw2 = new FileWriter(file2);
      TextDumper dumper2 = new TextDumper(fw2);
      dumper2.dump(test2);
      fw2.close();

      assertThat(file.length(), equalTo(file2.length()));
    }
    catch(IOException | ParserException ex) {
        fail(ex.getMessage());
    }
  }

  @Test
  void testNullAirlineWrittenError(@TempDir File dir)
  {
    try {
      TextDumper dumper = new TextDumper(new FileWriter(new File(dir, "test.txt")));
      assertThrows(IllegalArgumentException.class, () -> dumper.dump(null));
    } catch (IOException e) {
      fail(e.getMessage());
    }
  }

  @Test
  void testPrintingAirlineNoFlights(@TempDir File dir)
  {
    Airline test = new Airline("name");
    File file = new File(dir, "airline.txt");
    try(FileWriter fw = new FileWriter(file)) {
      TextDumper dumper = new TextDumper(fw);
      dumper.dump(test);

      assertThat((int) file.length(), equalTo(5));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

  }

}
