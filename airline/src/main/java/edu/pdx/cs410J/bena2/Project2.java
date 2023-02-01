package edu.pdx.cs410J.bena2;

import edu.pdx.cs410J.ParserException;

import java.io.*;
import java.util.*;

/**
 * The main class for the CS410J airline Project2
 */
public class Project2 extends CommandLineParser {
    /**
     * Main method for  CS410J Project 2, parses the command line for airline/flight data
     * and program options. All errors cause the program to exit and an error message to be printed
     * to standard error.
     * @param args an Array of Strings hold user supplied input from the command line
     */

  public static void main(String[] args) {
      Project2 test = new Project2();
      String[] operations = {"-README", "-print", "-textFile"};
      boolean print = false;
      ArrayList<String> args_list = new ArrayList<>();
      Set<String> options_list = new HashSet<>();
      String fName = null;
      File file = null;

      fName = splitOptionsAndArgs(args, args_list, options_list);

      if(args_list.isEmpty() && options_list.isEmpty()) {
          System.err.println(missingArguments);
          printUsage(0);
          return;
      }
      
      if(options_list.contains(operations[0])) {
          printREADME(0);
          return;
      }
      

      if(options_list.contains(operations[1])) {
          print = true;
          options_list.remove(operations[1]);
      }

      if(options_list.contains(operations[2]))
      {
          file = fileSet(options_list, fName);
          if(file == null)
              return;
      }

      if(invalidOptions(options_list))
          return;

      test.execution( print, args_list, fName, file);
  }

    /**
     * invalidOptions validates that no options remain, outputs invalid options to standard error.
     * @param options_list the Set of options.
     * @return returns true if there are any invalid options and false if not.
     */
    protected static boolean invalidOptions(Set<String> options_list) {

        if(options_list.isEmpty())
            return false;

        System.err.print("Invalid Options: ");
        for(String option: options_list)
        {
            System.err.print(option+" ");
        }
        System.err.println("\nPlease see README for further instructions on valid command line" +
                " options");
        return true;
    }

    /**
     * fileSet if the file name provided is null the method outputs an error to standard error.
     * Otherwise, a new File is instantiated with the File path.
     * @param optionsList a Set of options provided by the user.
     * @param fName the file path.
     * @return the instantiated file.
     */
    private static File fileSet(Set<String> optionsList, String fName) {

        if (fName == null) {
            System.err.println("Command Line: -textFile option selected but no file path provided");
            return null;
        }

        optionsList.remove("-textFile");

        File toReturn = new File(fName);

        try {

            if (!toReturn.exists())
                return toReturn;
            if (!toReturn.isFile())
            {
                System.err.println("Error Command Line: File Path Provided Is Not A Valid File");
                return null;
            }
            if(!toReturn.canRead())
            {
                System.err.println("Error Command Line: File Path Provided Is Not Readable");
                return null;
            }
            if(!toReturn.canWrite())
            {
                System.err.println("Error Command Line: File Path Provided Is Not Writeable");
                return null;
            }
        }
        catch(SecurityException ex)
        {
            System.err.println("Error Command Line: Do not have security level to access file");
            return null;
        }

        return toReturn;
    }


    /**
     * execution handles the primary execution of the program, parsing the file, creating the new
     * flight, and outputting the airline.
     * @param print a Boolean indicating whether the latest flight should be output.
     * @param args_list  a List of arguments used to create the airline/file.
     * @param fName the file path.
     * @param file the file to be read from/writen too.
     */
    protected void execution(boolean print, ArrayList<String> args_list, String fName, File file) {
        try{
            if(file != null )
                parseFile(file);
  
            createAirlineAndFlight(args_list);
  
           if(file != null)
               dumpFile(file);
  
            if(print)
                printFlight();
        }
        catch(IllegalArgumentException ex){
            System.err.println("Error Command Line: "+ex.getMessage()+"\n" +
                    "Please see README for further instructions.");
        }
        catch (ParserException ex)
        {
            System.err.println("File Error:"+ ex.getMessage());
        }
        catch(IOException ex)
        {
            System.err.println("Error Command Line: Invalid file path "+ fName +ex.getMessage());
        }
    }

}