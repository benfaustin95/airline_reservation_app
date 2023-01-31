package edu.pdx.cs410J.bena2;

import com.google.common.annotations.VisibleForTesting;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * The main class for the CS410J airline Project
 */
public class Project1 {

    //Standard Error message  for missing arguments
    public static final String missingArguments = "Missing command line arguments, please see following README "
            + " for further instruction on valid command line arguments.";
    //Standard Error message  for to few arguments
    public static final String fewArguments = "To few arguments to create a flight, please see README " +
            "for further instruction on valid command line arguments.";
    //Standard Error message  for to many arguments
    public static final String manyArguments = "To many arguments to create a flight, please see README " +
            "for further instruction on valid command line arguments.";
    // references the airline created by the user
    protected Airline airline;
    // holds list of args
    protected static final String [] ARG_TYPE = {"Airline Name", "Flight Number", "Source Location",
    "Departure Date", "Departure Time", "Destination Location", "Arrival Date", "Arrival Time"};

    /**
     * createAirlineAndFlight instantiates a new airline object and adds a new flight to the airline.
     * Both the flight and airline are created using data passed in via an ArrayList of strings.
     * @param flightData An ArrayList of Strings that holds the data needed to instantiate the flight
     *                   and airline objects.
     * @throws IllegalArgumentException Throws an IllegalArgumentException if the number  of arguments
     *                                  does not match the required number. An IllegalArgumentException
     *                                  can also be thrown by the Airline or Flight constructor.
     */
  public void createAirlineAndFlight(ArrayList<String> flightData) throws IllegalArgumentException {

      correctNumberOfArguments(flightData);

      airline = new Airline(flightData.get(0), new Flight(flightData.get(1), flightData.get(2), flightData.get(5), flightData.get(3),
              flightData.get(4), flightData.get(6), flightData.get(7)));
  }

    protected static void correctNumberOfArguments(ArrayList<String> flightData) {
        if(flightData.size() < 8)
            throw new IllegalArgumentException(toFewArguments(flightData));

        if(flightData.size() > 8)
            throw new IllegalArgumentException(toManyArguments(flightData));
    }


    /**
     * printFlight prints the flight information of the (only) flight stored in the airlines roster.
     * @throws IllegalArgumentException Throws an IllegalArgumentException if hte airline is empty,
     *                                  airline roster is empty, or the flight s null.
     */
    public void printFlight() throws IllegalArgumentException
  {
      if(airline == null)
          throw new IllegalArgumentException("No Airline has been created");

      Collection<Flight> temp= airline.getFlights();

      if(temp.iterator().hasNext())
        System.out.println(temp.iterator().next().toString());
  }

    /**
     * printREADME prints out the README stored in the resources folder.
     * @param output Signals whether the README should be printed to standard error (1) or standard out (!=1)
     * @throws IOException Throws an IOException if the README file is not an available resource.
     */
  protected void printREADME(int output) throws IOException
  {
      String line;

      try (InputStream readme = Project1.class.getResourceAsStream("README.txt")) {
          if (readme == null)
              throw new IOException("README not available");
          BufferedReader reader = new BufferedReader(new InputStreamReader(readme));
          while((line=reader.readLine()) != null)
          {
              if(output == 1)
                  System.err.println(line);
              else
                System.out.println(line);
          }
      }
      catch(IOException ex)
      {
          throw new IOException("README not available");
      }

  }

    /**
     * Project1 serves as the default constructor for the Project1 class, sets the airline field to
     * null.
     */
  public Project1()
  {
    airline = null;
  }

  public static String toFewArguments(ArrayList<String> args)
  {
     StringBuilder error = new StringBuilder();
     int size = args.size();

     for(int i =size; i<8; ++i)
     {
         error.append("Missing ").append(ARG_TYPE[i]);
         if(i!=7)
             error.append(",");
     }

     return error.toString();
  }

  public static String toManyArguments(ArrayList<String> args) throws IllegalArgumentException
  {
      StringBuilder error = new StringBuilder();
      int size = args.size();

      for(int i = 8; i<size; ++i)
      {
          error.append("Extra Argument - ").append(args.get(i));
          if(i!=(size-1))
              error.append(",");

      }
      return error.toString();
  }

}