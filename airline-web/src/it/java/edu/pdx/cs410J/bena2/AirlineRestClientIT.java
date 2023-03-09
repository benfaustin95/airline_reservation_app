package edu.pdx.cs410J.bena2;

import edu.pdx.cs410J.ParserException;
import edu.pdx.cs410J.web.HttpRequestHelper;
import org.junit.jupiter.api.MethodOrderer.MethodName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test that tests the REST calls made by {@link AirlineRestClient}
 */
@TestMethodOrder(MethodName.class)
class AirlineRestClientIT {
  private static final String HOSTNAME = "localhost";
  private static final String PORT = System.getProperty("http.port", "8080");

  private AirlineRestClient newAirlineRestClient() {
    int port = Integer.parseInt(PORT);
    return new AirlineRestClient(HOSTNAME, port);
  }

  @Test
  void test0GetAirlineNoAirlines() {
    try {
      AirlineRestClient client = newAirlineRestClient();
      client.removeAllAirlines();
      Exception ex = assertThrows(HttpRequestHelper.RestException.class, () -> client.getAirline("test"));
      assertThat(ex.getMessage(), containsString("Airline test does"));
    } catch (Exception ex) {
      fail(ex.getMessage());
    }
  }

  @Test
  void test1AddAirline(){
    try {
      AirlineRestClient client = newAirlineRestClient();
      client.addFlight("test", FlightTest.getValidFlight());
      Airline airline = client.getAirline("test");

      assertNotNull(airline);
      assertThat(airline.getFlights().size(), equalTo(1));
    } catch (Exception ex) {
      fail(ex.getMessage());
    }
  }

  @Test
  void test2AddFlight(){
    try {
      AirlineRestClient client = newAirlineRestClient();
      client.addFlight("test", FlightTest.getValidFlight());
      Airline airline = client.getAirline("test");

      assertNotNull(airline);
      assertThat(airline.getFlights().size(), equalTo(2));
    } catch (Exception ex) {
      fail(ex.getMessage());
    }
  }

  @Test
  void test3AddFlightInvalidName(){
    try {
      AirlineRestClient client = newAirlineRestClient();
      client.addFlight("test airline2", FlightTest.getValidFlight());
      Airline airline = client.getAirline("test airline2");

      assertNotNull(airline);
      assertThat(airline.getFlights().size(), equalTo(1));
    } catch (Exception ex) {
      fail(ex.getMessage());
    }
  }
  @Test
  void test4getAirline() {
    try {
      AirlineRestClient client = newAirlineRestClient();
      Airline airline = client.getAirline("test");

      assertNotNull(airline);
      assertThat(airline.getFlights().size(), equalTo(2));
    } catch (Exception ex) {
      fail(ex.getMessage());
    }
  }

  @Test
  void test5getAirline() {
    try {
      AirlineRestClient client = newAirlineRestClient();
      Airline airline = client.getAirline("test","pdx","sea");

      assertNotNull(airline);
      assertThat(airline.getFlights().size(), equalTo(2));
    } catch (Exception ex) {
      fail(ex.getMessage());
    }
  }

  @Test
  void test6getAirlineInvalidName() {
    try {
      AirlineRestClient client = newAirlineRestClient();
      Exception ex = assertThrows(HttpRequestHelper.RestException.class, () -> client.getAirline("test3"));
      assertThat(ex.getMessage(), containsString("Airline test3 does"));
    } catch (Exception ex) {
      fail(ex.getMessage());
    }
  }

  @Test
  void test7getAirlineInvalidSrc() {
    try {
      AirlineRestClient client = newAirlineRestClient();
      Exception ex = assertThrows(HttpRequestHelper.RestException.class, () -> client.getAirline("test","SRC","sea"));
      assertThat(ex.getMessage(), containsString("Source Location SRC is invalid, must be a known airport."));
    } catch (Exception ex) {
      fail(ex.getMessage());
    }
  }
}
