package edu.pdx.cs410J.bena2;

import edu.pdx.cs410J.ParserException;

import java.io.*;
import java.util.*;

/**
 * The main class for the CS410J airline Project
 */
public class Project2 extends Project1 {
    /**
     * Main method for  CS410J Project 1, parses the command line for airline/flight data
     * and program options. All errors cause the program to exit and an error message to be printed
     * to standard error.
     * @param args an Array of Strings hold user supplied input from the command line
     */
  public static void main(String[] args) {
      // Holds airline and will be used to create and print the airline
      Project2 test = new Project2();
      // Valid command line operations
      String[] operations = {"-README", "-print", "-textFile"};
      // Will signal to print the flight if flipped to true
      boolean print = false;
      // Will hold the argument list (args)
      ArrayList<String> args_list = new ArrayList<>();
      Set<String> options_list = new HashSet<>();
      String fName = null;
      File file = null;


      try{
          fName = splitOptionsAndArgs(args, args_list, options_list);
      }
      catch(IllegalArgumentException ex)
      {
          System.err.println("Command Line: " + ex.getMessage());
          System.err.println("Please see README for further instructions");
          return;
      }

      // If no arguments on command line through exception and print readme to standard error
      if(args_list.isEmpty() && options_list.isEmpty()){
          System.err.println(missingArguments);
          //print out not concise command line ussage
      }

      // If argument list contains -README then readme is printed via standard out and program is
      // exited
      if(options_list.contains(operations[0])){

          options_list.remove(operations[0]);
          try {
              test.printREADME(0);
          }
          catch(IOException ex)
          {
              System.err.println(ex.getMessage());
          }
          return;
      }

      // If argument list contains -print print is flipped to true and the argument is removed from
      // the list
      if(options_list.contains(operations[1])) {
          print = true;
          options_list.remove(operations[1]);
      }

      // IF options list contains -textFile then read in from file
      if(options_list.contains(operations[2]))
      {
          file = new File(fName);
          options_list.remove(operations[2]);
      }

      if(!options_list.isEmpty())
      {
          System.err.print("Invalid Options: ");
          for(String option: options_list)
          {
              System.err.print(option+" ");
          }
          System.err.println("Please see read me for further instructions on valid command line" +
                  " options");
          return;
      }

      // createAirline and Flight is called and at this point the arg_list should contain only
      // airline and flight information. If the airline/flight is successfully instantiated then
      // and print is flipped on print flight is called.
      try{
          if(file != null )
              test.parseFile(file);

          correctNumberOfArguments(args_list);

          if (test.airline == null)
              test.createAirlineAndFlight(args_list);
          else
              test.addFlight(args_list);

         if(file != null)
             test.dumpFile(file);

          if(print)
              test.printFlight();
      }
      catch(IllegalArgumentException ex){
          System.err.println("Error Command Line: "+ex.getMessage()+"\n" +
                  "Please see README for further instructions.");
      }
  }

    protected void dumpFile(File file) {

      try(FileWriter fw = new FileWriter(file)) {

          TextDumper dumper = new TextDumper(fw);
          dumper.dump(airline);

      } catch (IOException e) {
          throw new RuntimeException(e);
      }
    }

    protected void parseFile(File file) throws IllegalArgumentException{

      try(FileReader fr = new FileReader(file)) {

         TextParser parser = new TextParser(fr);
         airline = parser.parse();

      } catch (FileNotFoundException e) {
          //nothing
      } catch (IOException e) {
          throw new RuntimeException(e);
      } catch (ParserException e) {
          throw new IllegalArgumentException(e.getMessage());
      }
    }

    protected void addFlight(ArrayList<String> argsList) throws IllegalArgumentException{

          if(!airline.getName().equals(argsList.get(0)))
              throw new IllegalArgumentException("Airline ("+airline.getName()+") provided in file " +
                      "does not match Airline ("+argsList.get(0)+") provided on command line");
          airline.addFlight(new Flight(argsList.get(1), argsList.get(2),argsList.get(5),
                  argsList.get(3), argsList.get(4),argsList.get(6), argsList.get(7)));
    }

    public static String splitOptionsAndArgs(String[] args, ArrayList<String> argsList,
                                             Set<String> optionsList) throws IllegalArgumentException {
      String toReturn = null;

      for(int i = 0; i<args.length; ++i)
      {
          if(args[i].equals("-textFile") && toReturn == null)
          {
              if(i+1 < args.length && !args[i+1].startsWith("-")) {
                  optionsList.add(args[i++]);
                  toReturn = args[i];
                  continue;
              }
              throw new IllegalArgumentException("-textFile option selected but no file path provided");
          }

          if(args[i].startsWith("-"))
              optionsList.add(args[i]);
          else
              argsList.add(args[i]);
      }

      return toReturn;
    }


}