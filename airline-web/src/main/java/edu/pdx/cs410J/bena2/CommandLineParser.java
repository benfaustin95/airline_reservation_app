package edu.pdx.cs410J.bena2;

import edu.pdx.cs410J.AirlineDumper;
import edu.pdx.cs410J.AirlineParser;
import edu.pdx.cs410J.AirportNames;
import edu.pdx.cs410J.ParserException;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * the CommandLineParser class maintains the functionality implemented in the Project1 class
 * outlined in Project One.
 */
public class CommandLineParser {

    //Standard Error message  for missing arguments
    public static final String missingArguments = "Missing command line arguments, please see " +
            "below usage information";
    protected Airline airline;
    // holds list of args
    protected static final String [] ARG_TYPE = {"Airline Name", "Flight Number", "Source Location",
            "Departure Date", "Departure Time", "Departure am/pm marker","Destination Location", "Arrival Date",
            "Arrival Time", "Arrival am/pm marker"};
    protected static final String[] operations = {"-README", "-print", "-host","-port","-search"};
    protected static boolean print = false, search = false;

    /**
     * createAirlineAndFlight if the airline field is null the method instantiates a new airline
     * object and adds a new flight to the airline. Both the flight and airline are created using
     * data passed in via an ArrayList of strings. If the airline field is not null the airline
     * name is confirmed to match what is stored in the argument list and a flight is added with
     * the remainder of the flight data.
     * @param flightData An ArrayList of Strings that holds the data needed to instantiate the flight
     *                   and airline objects.
     * @throws IllegalArgumentException Throws an IllegalArgumentException if the name of the airline
     *                                  does not match what is in flightData. An IllegalArgumentException
     *                                  can also be thrown by the Airline or Flight constructor.
     */
    public Flight createAirlineAndFlight(ArrayList<String> flightData) throws IllegalArgumentException {

        correctNumberOfArguments(flightData);

        Flight toAdd = new Flight(flightData.get(1), flightData.get(2),flightData.get(6),
                flightData.get(3), flightData.get(4),flightData.get(5), flightData.get(8),
                flightData.get(7), flightData.get(9));
        Airline airline = new Airline(flightData.get(0), toAdd);
        return toAdd;
    }

    /**
     * correctNumberOfArguments ensures that the correct number of arguments are present in the
     * Array list provided.
     * @param flightData the ArrayList to be validated.
     * @throws IllegalArgumentException Thrown if the number of arguments stored in the list is invalid.
     */
    protected static void correctNumberOfArguments(ArrayList<String> flightData) throws IllegalArgumentException{
        if(flightData.size() < 10)
            throw new IllegalArgumentException(toFewArguments(flightData));

        if(flightData.size() > 10)
            throw new IllegalArgumentException(toManyArguments(flightData, 10));
    }


    /**
     * printREADME prints out the README stored in the resources folder.
     * @param output Signals whether the README should be printed to standard error (1) or standard out (!=1)
     */
    protected static void printREADME(int output)
    {
        String line;

        try (InputStream readme = CommandLineParser.class.getResourceAsStream("README.txt")) {
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
            System.err.println("README not available");
        }

    }

    /**
     * printUsage prints out the Usage stored in the resources folder.
     * @param output Signals whether the README should be printed to standard error (1) or standard out (!=1)
     */
    protected static void printUsage(int output)
    {
        String line;

        try (InputStream readme = CommandLineParser.class.getResourceAsStream("CommandLineUsage.txt")) {
            if (readme == null)
                throw new IOException("CommandLineUsage not available");
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
            System.err.println("CommandLineUsage not available");
        }

    }

    /**
     * Project1 serves as the default constructor for the Project1 class, sets the airline field to
     * null.
     */
    public CommandLineParser()
    {
        airline = null;
    }

    /**
     * toFewArguments builds the error message to be thrown if too few arguments are provided by the
     * user.
     * @param args the list of Arguments provided.
     * @return the String holding the error message.
     */
    public static String toFewArguments(ArrayList<String> args)
    {
        StringBuilder error = new StringBuilder();
        int size = args.size();
        error.append("Arguments Provided - ");
        for(int i =0; i<size; ++i)
        {
            error.append(ARG_TYPE[i]).append(" ").append(args.get(i)).append(" ");
            if(i != size-1)
                error.append(", ");
        }
        error.append("\nArguments Missing - ");
        for(int i =size; i<10; ++i)
        {
            error.append(ARG_TYPE[i]).append(" ");
            if(i!=9)
                error.append(", ");
        }
        return error.toString();
    }

    /**
     * toManyArguments builds the error message to be thrown if too many arguments are provided by the
     * user.
     * @param args the list of Arguments provided.
     * @return the String holding the error message.
     */
    public static String toManyArguments(ArrayList<String> args, int expected)
    {
        StringBuilder error = new StringBuilder();
        int size = args.size();

        for(int i = expected; i<size; ++i)
        {
            error.append("Extra Argument - ").append(args.get(i));
            if(i!=(size-1))
                error.append(",");

        }
        return error.toString();
    }


    /**
     * prettyPrintFile handles outputting the formatted textual representation of the airline data.
     * If the file is null the airline information is printed via standard out. If the file is not
     * null dumpFile is called with type set to 1.
     * @throws IOException thrown if the file can not be written to.
     * @throws IllegalArgumentException thrown if the airline is null.
     */
   protected void prettyPrintFile(String src, String dest) throws IllegalArgumentException {
       if(airline.getFlights().size()==0)
           throw new IllegalArgumentException(String.format("%s contains no direct flights " +
                   "between %s(%s) and %s(%s)",airline.getName(), src, AirportNames.getName(src.toUpperCase()),
                   dest, AirportNames.getName(dest.toUpperCase())));
       PrettyPrinter printer = new PrettyPrinter(System.out);
       printer.dump(airline,src, dest);
   }

    /**
     * splitOptionsAndArgs parses the command line into arguments meant for
     * creating an airline and/or flight and the command line options.
     * @param args the String array to be parsed.
     * @param argsList the List meant to hold the arguments.
     * @param optionsList The List meant to hold the options.
     */
    public static void splitOptionsAndArgs(String[] args, ArrayList<String> argsList,
                                             HashMap<String,String> optionsList) {

        for(int i = 0; i<args.length; ++i) {
            if (((args[i].equals(operations[2])) || args[i].equals(operations[3]))) {
                if (i + 1 < args.length && !args[i + 1].startsWith("-")) {
                    optionsList.put(args[i++], args[i]);
                }
                else
                    optionsList.put(args[i], null);
                continue;
            }
            if (args[i].startsWith("-"))
                optionsList.put(args[i], null);
            else
                argsList.add(args[i]);
        }
    }

    protected static boolean validateSearchArguments(ArrayList<String> argsList) throws IllegalArgumentException{
        int size = argsList.size();

        if(size == 0)
            throw new IllegalArgumentException("No -search arguments provided for search option");
        if(size == 2)
            throw  new IllegalArgumentException("-search argument missing: Destination");
        if(size>3)
            throw new IllegalArgumentException(toManyArguments(argsList, 3));
        if(size == 1)
            return true;

        Flight.validateLocation(argsList.get(1),0);
        Flight.validateLocation(argsList.get(2),1);

        return false;
    }
}