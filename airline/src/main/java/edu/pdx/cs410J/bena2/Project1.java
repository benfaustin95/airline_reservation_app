package edu.pdx.cs410J.bena2;

import com.google.common.annotations.VisibleForTesting;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.*;

/**
 * The main class for the CS410J airline Project
 */
public class Project1 {

    protected Airline airline;
  @VisibleForTesting static boolean isValidDateAndTime(String dateAndTime) {
    return true;
  }

  public static void main(String[] args) {
      Project1 test = new Project1();
      String operations[] = {"-README", "-printme"};
      List<String> args_list = Arrays.asList(args);

      if(args_list.isEmpty()){
          System.err.println("Missing command line arguments");
          //print read me
          return;
      }

      if(!args_list.contains(operations[0]) && !args_list.contains(operations[1]))
      {
          System.err.println("Missing command line options, please see README for instructions");
          //print README
          return;
      }

      if(args_list.contains(operations[0])){
          System.out.println("WILL PRINT README");
          return;
      }

      if(args.length != 9)
      {
          System.err.println("Number of Command line arguments = " +
                  args.length + " is incorrect");
          return;
      }


      try{
          test.createAirlineAndFlight(Arrays.copyOfRange(args, 1, args.length));
          test.printFlight();
      }
      catch(IllegalArgumentException ex){
          System.out.println(ex);
      }

  }
  public void createAirlineAndFlight(String [] flightData) throws IllegalArgumentException {

        airline = new Airline(flightData[0], new Flight(flightData[1], flightData[2], flightData[5],flightData[3],
                flightData[4],flightData[6], flightData[7]));
  }

  public void printFlight()
  {
      Collection<Flight> temp= airline.getFlights();

      if(temp.isEmpty())
          throw new IllegalArgumentException("Airline Roster = Empty must create a flight in order to print information");

      if(temp.iterator().hasNext())
        System.out.println(temp.iterator().next().toString());
  }

}