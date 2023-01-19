package edu.pdx.cs410J.bena2;

import org.junit.jupiter.api.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Flight} class.
 *
 * You'll need to update these unit tests as you build out you program.
 */
public class FlightTest {

  @Test
  void initializeFlightStrings() {

    Flight test = null;

    try {
      test = new Flight(1,"source", "destination","1/1/23",
              "10:39","1/1/23", "2:50");
    } catch (ParseException e) {
      System.out.println("Bad date");
      System.exit(1);
    }

    assertThat("destination",equalTo(test.getDestination()));
  }

 @Test
  void initializeFlightDate() {

    Flight test = null;

    try {
      test = new Flight(1, "source", "destination",
              new SimpleDateFormat("MM/dd/yyyy HH:mm").parse("1/1/23 10:39"),
              new SimpleDateFormat("MM/dd/yyyy HH:mm").parse("1/1/23 2:20"));
    } catch (ParseException e) {
      System.out.println("Bad date");
      System.exit(1);
    }

    assertThat("destination", equalTo(test.getDestination()));
  }
  @Test
  void testFNumber(){
    Flight test = null;
    try {
        test = new Flight(1,"source", "destination","1/1/23",
                "10:39","1/1/23", "2:50");
    } catch (ParseException e) {
      System.out.println("Bad date");
      System.exit(1);
    }
    assertThat(1,equalTo(test.getNumber()));
  }
  @Test
  void testSource(){
    Flight test = null;
    try {
        test = new Flight(1,"source", "destination","1/1/23",
                "10:39","1/1/23", "2:50");
    } catch (ParseException e) {
      System.out.println("Bad date");
      System.exit(1);
    }
    assertThat("source", equalTo(test.getSource()));
  }

  @Test
  void testDestination(){
    Flight test = null;
    try {
        test = new Flight(1,"source", "destination","1/1/23",
                "10:39","1/1/23", "2:50");
    } catch (ParseException e) {
      System.out.println("Bad date");
      System.exit(1);
    }
    assertThat("destination", equalTo(test.getDestination()));
  }

  @Test
  void testDepartureString()
  {
      Flight test = null;
      //need to allow for equal comparaison without the 0 for strings
      String departureDate = "01/01/2023", departureTime = "10:39";
      try {
          test = new Flight(1,"source", "destination", departureDate,
                  departureTime,"1/1/23", "2:50");
      } catch (ParseException e) {
          System.out.println("Bad date");
          System.exit(1);
      }
      assertThat(departureDate+" "+departureTime,equalTo(test.getDepartureString()));
  }

  @Test
  void testDepartureDate()
  {
        Flight test= null;
        //need to allow for equal comparaison without the 0 for strings
        Date test_departure = null, test_arrival = null;

        try {
            test_departure = new SimpleDateFormat("MM/dd/yyyy HH:mm").parse("1/1/2023 10:39");
            test_arrival = new SimpleDateFormat("MM/dd/yyyy HH:mm").parse("1/1/2023 1:11");

            test = new Flight(1,"source", "destination", test_departure,
                    test_arrival);

        } catch (ParseException e) {
            System.out.println("Bad date");
            System.exit(1);
        }
        assertThat(0, equalTo(test_departure.compareTo(test.getDeparture())));
  }

    @Test
    void testArrivalString() {
        Flight test = null;
        //need to allow for equal comparaison without the 0 for strings
        String arrivalDate = "01/01/2023", arrivalTime = "10:39";
        try {
            test = new Flight(1, "source", "destination", "1/1/23",
                    "2:50", arrivalDate, arrivalTime);
        } catch (ParseException e) {
            System.out.println("Bad date");
            System.exit(1);
        }
        assertThat(arrivalDate + " " + arrivalTime, equalTo(test.getArrivalString()));
    }

    @Test
    void testArrivalDate()
    {
        Flight test= null;
        //need to allow for equal comparaison without the 0 for strings
        Date test_departure = null, test_arrival = null;

        try {
            test_departure = new SimpleDateFormat("MM/dd/yyyy HH:mm").parse("1/1/2023 10:39");
            test_arrival = new SimpleDateFormat("MM/dd/yyyy HH:mm").parse("1/1/2023 1:11");

            test = new Flight(1,"source", "destination", test_departure,
                    test_arrival);

        } catch (ParseException e) {
            System.out.println("Bad date");
            System.exit(1);
        }
        assertThat(0, equalTo(test_arrival.compareTo(test.getArrival())));
    }

}
