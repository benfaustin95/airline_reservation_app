package edu.pdx.cs410J.bena2;

import com.google.common.annotations.VisibleForTesting;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;

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

    //what to check
    //number of args is 0, 1, or >=9

    if(args.length == 0 || args[0].equals("-README")){
      //print readme
    }

    if(!args[0].equals("-printme") || args.length != 9){
      //invalid options
      //exit
    }

   try{
       test.createAirlineAndFlight(args);
       test.printAirlineAndFlight();
   }
   catch(IllegalArgumentException ex){
        System.out.println(ex);
   }

  }
  public void createAirlineAndFlight(String [] args) throws IllegalArgumentException {
        String name = validateName(args[1]);
        int number = validateNumber(args[2]);
        String source = validateLocation(args[3]);
        Date departure = validateDateAndTime(args[4],args[5]);
        String destination = validateLocation(args[6]);
        Date arrival = validateDateAndTime(args[7],args[8]);

        airline = new Airline(name, new Flight(number,source,destination, departure, arrival));
  }

  public void printAirlineAndFlight()
  {
      airline.toString();
  }

  public static String validateName(String name) throws IllegalArgumentException{
      if(name.isEmpty())
          throw new IllegalArgumentException("Airline Name = " +name + " must not be empty");
      return name;
  }

  public static int validateNumber(String number) throws IllegalArgumentException {
      Integer rnumber = null;

      try {
          rnumber = Integer.parseInt(number);
      }
      catch(NumberFormatException ex){
          throw new IllegalArgumentException("Flight Number= " + number + " must be numeric");
      }
      return rnumber;
  }

  public static String validateLocation(String location) throws IllegalArgumentException {
      if(location.length()!= 3 || !location.matches("[A-za-z]{3}"))
          throw new IllegalArgumentException("location = " + location + " must be three alphabetic letters");
      return location;
  }

  public static Date validateDateAndTime(String date, String time) throws IllegalArgumentException
  {
      DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm");
      Date rdate = null;
      if(!date.matches("[01]?[0-9]\\/[01]?[0-9]\\/\\d{4}"))
          throw new IllegalArgumentException("Date = "+date+" must be in mm/dd/yyyy");
      if(!time.matches("[0-2]?[0-9]:[0-5][0-9]"))
          throw new IllegalArgumentException("Time = "+time
                  +" must be in hh:mm (24 hour time");
      try {
          rdate = df.parse(date + " " + time);
      }
      catch (ParseException ex)
      {
          throw new IllegalArgumentException("Date= "+ date + " " + time +
                  " must be in format mm/dd/yyyy hh:mm (24 hour time");
      }

      return rdate;
  }

}