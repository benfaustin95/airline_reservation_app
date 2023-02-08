package edu.pdx.cs410J.bena2;

import edu.pdx.cs410J.ParserException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

public class TextParserTest {

  @Test
  void validTextFileCanBeParsed() {
    try (InputStream resource = getClass().getResourceAsStream("valid-airline.txt")) {
      assertThat(resource, notNullValue());
      TextParser parser = new TextParser(new InputStreamReader(resource));
      Airline airline = parser.parse();
      assertThat(airline.getName(), equalTo("Test Airline"));
      assertThat(airline.getFlights().size(), equalTo(9));
    } catch (ParserException ex) {
      fail(ex.getMessage());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void testMultipleAirlinesOneFile()
  {
    String name = "invalid-MultipleAirlines-airline.txt";
    invalidFile(name);
  }


  @Test
  void testMissingArguments()
  {
    String name = "invalid-MissingArguments-airline.txt";
    invalidFile(name);
  }

  @Test
  void testExtraArguments()
  {
    String name = "invalid-ExtraArguments-airline.txt";
    invalidFile(name);
  }

  @Test
  void testInvalidArgumentsArguments()
  {
    String name = "invalid-InvalidDate-airline.txt";
    invalidFile(name);

    name = "invalid-InvalidTime-airline.txt";
    invalidFile(name);

    name = "invalid-InvalidLocation-airline.txt";
    invalidFile(name);

    name = "invalid-InvalidFlightNumber-airline.txt";
    invalidFile(name);

    name = "invalid-InvalidName-airline.txt";
    invalidFile(name);

    name = "invalid-BadAMMarker-airline.txt";
    invalidFile(name);

    name = "invalid-BadPMMarker-airline.txt";
    invalidFile(name);

    name = "invalid-BadLocation-airline.txt";
    invalidFile(name);

    name = "invalid-MissingAMMarker-airline.txt";
    invalidFile(name);
  }

  @Test
  void invalidTextFileThrowsParserException() {
    InputStream resource = getClass().getResourceAsStream("empty-airline.txt");
    assertThat(resource, notNullValue());

    TextParser parser = new TextParser(new InputStreamReader(resource));
    assertThrows(ParserException.class, parser::parse);
  }

  @Test
  void testParseAirlineNoFlights()
  {
    String name = "valid-AirlineNoFlights-airline.txt";
    try(InputStream resource = getClass().getResourceAsStream(name))
    {
      assertNotNull(resource);
      TextParser parser = new TextParser(new InputStreamReader(resource));
      Airline test = parser.parse();

      assertThat(test.getName(), equalTo("Test Airline"));
      assertThat(test.getFlights().size(), equalTo(0));

    } catch (IOException | ParserException e) {
      fail(e.getMessage());
    }
  }

  @Test
  void testParseAirlineNoFlightThenFlights()
  {
    String name = "valid-AirlineNoFlightsThenFlights-airline.txt";
    try(InputStream resource = getClass().getResourceAsStream(name))
    {
      assertNotNull(resource);
      TextParser parser = new TextParser(new InputStreamReader(resource));
      Airline test = parser.parse();

      assertThat(test.getName(), equalTo("Test Airline"));
      assertThat(test.getFlights().size(), equalTo(5));

    } catch (IOException | ParserException e) {
      fail(e.getMessage());
    }
  }

  @Test
  void testMultipleAirlinesOneFileOneAirlineNoFlight()
  {
    String name = "invalid-MultipleAirlinesOneNoFlight-airline.txt";
    invalidFile(name);
  }

  @Test
  void testParseAirlineFlightsThenNoFlight()
  {
    String name = "valid-AirlineFlightsThenNoFlights-airline.txt";
    try(InputStream resource = getClass().getResourceAsStream(name))
    {
      assertNotNull(resource);
      TextParser parser = new TextParser(new InputStreamReader(resource));
      Airline test = parser.parse();

      assertThat(test.getName(), equalTo("Test Airline"));
      assertThat(test.getFlights().size(), equalTo(5));

    } catch (IOException | ParserException e) {
      fail(e.getMessage());
    }
  }
  private void invalidFile(String name) {
    try(InputStream resource = getClass().getResourceAsStream(name)){
      assertNotNull(resource);
      TextParser parser = new TextParser((new InputStreamReader(resource)));
      assertThrows(ParserException.class,parser::parse);
    }
    catch (IOException ex)
    {
      fail(ex.getMessage());
    }
  }
}