package edu.pdx.cs410J.bena2;

import edu.pdx.cs410J.ParserException;

import java.io.*;
import java.util.*;

/**
 * The main class for the CS410J airline Project2
 */
public class Project4 extends CommandLineParser {
    /**
     * Main method for  CS410J Project 2, parses the command line for airline/flight data
     * and program options. All errors cause the program to exit and an error message to be printed
     * to standard error.
     * @param args an Array of Strings hold user supplied input from the command line
     */

  public static void main(String[] args) {
      Project4 test = new Project4();
      ArrayList<String> args_list = new ArrayList<>();
      Set<String> options_list = new HashSet<>();
      String[] fNames;
      File [] file = new File[2];
      print = stdOut = false;
      int parserType = 0;

      fNames = splitOptionsAndArgs(args, args_list, options_list);

      if(args_list.isEmpty() && options_list.isEmpty()) {
          System.err.println(missingArguments);
          printUsage(0);
          return;
      }

      if(options_list.contains(operations[2]) && options_list.contains(operations[4]))
      {
          System.err.println("Options "+operations[2]+" and "+operations[4]+" can not be exercised" +
                  " together. \nPlease see README for further instructions");
          return;
      }

      if(fNames[0] != null && fNames[0].equals(fNames[1]))
      {
          System.err.println("File path for (-textFile or -xmlFile) and -pretty options can not be the same\n" +
                  "Please see README for further instruction");
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

      if(options_list.contains(operations[2]) || options_list.contains(operations[4]))
      {
          parserType = (options_list.contains(operations[2])?0:1);
          file[0] = fileSet(options_list, fNames[0],(parserType==0?operations[2]:operations[4]));
          if(file[0] == null)
              return;
      }

      if(options_list.contains(operations[3]))
      {
          file[1] = fileSet(options_list, fNames[1], operations[3]);
          if(file[1] == null && !stdOut)
              return;
      }

      if(invalidOptions(options_list))
          return;

      test.execution(args_list, file,parserType);
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
    protected static File fileSet(Set<String> optionsList, String fName, String option) {

        if (fName == null) {
            System.err.println("Command Line: "+option+
                    " option selected but no file path provided");
            return null;
        }

        optionsList.remove(option);

        if(option.equals(operations[3]) && fName.equals("-")) {
            stdOut = true;
            return null;
        }

        return fileValidation(fName, option, 0);
    }

    protected static File fileValidation(String fName, String option, int type) {

        File toReturn = new File(fName);

        try {

            if (!toReturn.exists() && type == 0)
                return toReturn;
            if(!toReturn.exists())
            {
                System.err.println("Error Command Line: File Path Provided for option "+option+" "+
                "does not exist, therefor can not be converted to XML");
                return null;
            }
            if (!toReturn.isFile())
            {
                System.err.println("Error Command Line: File Path Provided for option "+option+" " +
                        "Is Not A Valid File");
                return null;
            }
            if(!toReturn.canRead())
            {
                System.err.println("Error Command Line: File Path Provided for option "+option+" "
                        +"Is Not Readable");
                return null;
            }
            if(!toReturn.canWrite())
            {
                System.err.println("Error Command Line: File Path Provided for option "+option+" "
                        +" Is Not Writeable");
                return null;
            }
            return toReturn;
        }
        catch(SecurityException ex)
        {
            System.err.println("Error Command Line: Do not have security level to access file provided" +
                    " for option "+option);
            return null;
        }

    }


    /**
     * execution handles the primary execution of the program, parsing the file, creating the new
     * flight, and outputting the airline.
     * @param args_list  a List of arguments used to create the airline/file.
     * @param file the array of Files to be read from/written too.
     */
    protected void execution(ArrayList<String> args_list, File[] file, int type) {
        try{
            if(file[0] != null)
                parseFile(file[0], type);

            Flight toPrint = createAirlineAndFlight(args_list);
  
           if(file[0] != null)
               dumpFile(file[0], type);

           if(file[1] != null || stdOut)
               prettyPrintFile(file[1]);
  
            if(print)
                System.out.println(toPrint.toString());
        }
        catch(IllegalArgumentException ex){
            System.err.println("Error Command Line: "+ex.getMessage()+"\n" +
                    "Please see README for further instructions.");
        }
        catch (ParserException ex)
        {
            System.err.println("File Error "+ ex.getMessage());
        }
        catch(IOException ex)
        {
            System.err.println("Error Command Line: Invalid file path "+ ex.getMessage());
        }
    }

}