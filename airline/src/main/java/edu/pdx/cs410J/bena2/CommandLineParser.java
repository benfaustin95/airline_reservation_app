package edu.pdx.cs410J.bena2;

import edu.pdx.cs410J.AirlineDumper;
import edu.pdx.cs410J.ParserException;

import java.io.*;
import java.util.*;

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
    protected static final String[] operations = {"-README", "-print", "-textFile","-pretty"};
    protected static boolean print = false, stdOut = false;

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

        if(airline == null)
            airline = new Airline(flightData.get(0),toAdd);

        else if(!airline.getName().equals(flightData.get(0)))
            throw new IllegalArgumentException("Airline ("+airline.getName()+") provided in file " +
                    "does not match Airline ("+flightData.get(0)+") provided on command line");

        else airline.addFlight(toAdd);

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
            throw new IllegalArgumentException(toManyArguments(flightData));
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
    public static String toManyArguments(ArrayList<String> args)
    {
        StringBuilder error = new StringBuilder();
        int size = args.size();

        for(int i = 10; i<size; ++i)
        {
            error.append("Extra Argument - ").append(args.get(i));
            if(i!=(size-1))
                error.append(",");

        }
        return error.toString();
    }

    /**
     * dumpFile handles dumping the airline data out to the file provided.
     * @param file the File to be written to.
     * @throws IOException Thrown if the file is not valid
     * @throws IllegalArgumentException Thrown if the output fails.
     */
    protected void dumpFile(File file, int type) throws IOException, IllegalArgumentException{
        AirlineDumper<Airline> dumper;

        try(FileWriter fw = new FileWriter(file)) {
            if(type == 0)
                dumper = new TextDumper(fw);
            else
                dumper = new PrettyPrinter(fw);

            dumper.dump(airline);

        } catch (IOException e) {
            throw new IOException(file.getPath() +" unable to write");
        }
    }

    /**
     * prettyPrintFile handles outputting the formatted textual representation of the airline data.
     * If the file is null the airline information is printed via standard out. If the file is not
     * null dumpFile is called with type set to 1.
     * @param file the File to be written to.
     * @throws IOException thrown if the file can not be written to.
     * @throws IllegalArgumentException thrown if the airline is null.
     */
   protected void prettyPrintFile(File file) throws IOException, IllegalArgumentException {

       if(file == null && stdOut) {
           PrettyPrinter printer = new PrettyPrinter(System.out);
           printer.dump(airline);
           return;
       }

       dumpFile(file, 1);


   }

    /**
     * parseFile handles parsing the airline data from the provided file.
     * @param file the File to read from.
     * @throws ParserException Thrown if the parser runs into any format or data issues when parsing
     *                         the file.
     * @throws IOException Thrown if the file provided can not be read from.
     */
    protected void parseFile(File file) throws ParserException, IOException{

        try(FileReader fr = new FileReader(file)) {

            TextParser parser = new TextParser(fr);
            airline = parser.parse();

        } catch (FileNotFoundException e) {
            return;
        } catch (IOException e) {
            throw new IOException(file.getPath()+" Unable to read");
        }
    }

    /**
     * splitOptionsAndArgs parses the command line into arguments meant for
     * creating an airline and/or flight and the command line options.
     * @param args the String array to be parsed.
     * @param argsList the List meant to hold the arguments.
     * @param optionsList The List meant to hold the options.
     * @return the Name of the file parsed from args.
     */
    public static String [] splitOptionsAndArgs(String[] args, ArrayList<String> argsList,
                                             Set<String> optionsList) {
        String [] toReturn = new String[2];
        toReturn[0] = null;
        toReturn[1] = null;

        for(int i = 0; i<args.length; ++i)
        {
            if(args[i].equals("-textFile") && toReturn[0]== null)
            {
                optionsList.add(args[i]);
                if(i+1 < args.length && !args[i+1].startsWith("-")) {
                    toReturn[0] = args[++i];
                }
                continue;
            }

            if(args[i].equals("-pretty") && toReturn[1] == null)
            {
                optionsList.add(args[i]);
                if(i+1 < args.length && (!args[i+1].startsWith("-") || args[i+1].equals("-"))) {
                    toReturn[1] = args[++i];
                }
                continue;
            }
            if(args[i].startsWith("-"))
                optionsList.add(args[i]);
            else
                argsList.add(args[i]);
        }
        return toReturn;
    }
}