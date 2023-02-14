package edu.pdx.cs410J.bena2;

import edu.pdx.cs410J.AirlineParser;
import edu.pdx.cs410J.ParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * The TextParser class handles parsing provided text files into an airline as well as error checking
 * the data stored in the provided file. The TextParser class implements the AirlineParser class,
 * thus it implements the parse method, which returns an airline reference.
 * <ul>
 *     <li> Reader: Handles reading in character data from the file </li>
 * </ul>
 */
public class TextParser implements AirlineParser<Airline> {
  // The reader object which will be used to instantiate the buffered reader which
  // actually parses the data from the file.
  private final Reader reader;

  /**
   * TextParser acts as the primary constructor for the TextParser class. The Reader field is
   * set to the value passed in.
   * @param reader a Reader holding the inputstream to be parsed.
   */
  public TextParser(Reader reader) {
    this.reader = reader;
  }

  /**
   * parse Parses the airline and flights from the inputstream referenced by the reader. Implements
   * the parse method of the AirlineParser interface.
   * @return an Airline reference holding the flights and airline stored in the file.
   * @throws ParserException Thrown if any formatting or data validation issues are encountered
   *                          when parsing the file.
   */
  @Override
  public Airline parse() throws ParserException {

    Airline airline = null;
    int counter = 0;


    try (BufferedReader br = new BufferedReader(this.reader)) {
      String line;
      ArrayList<String> field_list;

      while ((line = br.readLine()) != null) {
        ++counter;
        field_list = splitLine(line);
        if (airline == null)
          airline = parseAirline(field_list);
        else
          airline.addFlight(parseFlight(airline.getName(), field_list));
      }
    }
    catch (IOException | IllegalArgumentException e) {
      throw new ParserException("While parsing file line " + counter +": "+e.getMessage(), e);
    }

    if(counter == 0)
      throw new ParserException("File is empty, file provided must have contents or not yet" +
              "exist");
    return airline;
  }

  /**
   * parseAirline takes a list of arguments required for instantiating an Airline object. If there is
   * only one argument in the list an Airline with no flights is instantiated, otherwise an airline
   * with a flight is instantiated.
   * @param arg_list an ArrayList of Strings containing the Airline and Flight data.
   * @return an Airline reference, referencing the created Airline.
   * @throws IllegalArgumentException Exception not thrown in the method but rather in the methods
   *                                  invoked by the method and the error is pased to the calling
   *                                  routine.
   */
  protected Airline parseAirline(ArrayList<String> arg_list) throws IllegalArgumentException
  {
    if(arg_list.size() == 1)
      return new Airline(arg_list.get(0));

    return new Airline(arg_list.get(0),new Flight(arg_list.get(1),arg_list.get(2),
            arg_list.get(6),arg_list.get(3),arg_list.get(4),arg_list.get(5) , arg_list.get(8), arg_list.get(7),arg_list.get(9)));
  }


  /**
   * parseFlight takes a list of arguments required for instantiating a Flight object. Confirms that
   * the airline name matches the name provided by the argument list, then instantiates a flight
   * object with the data stored in the argument list.
   * @param aName a String holding the airline name to be confirmed.
   * @param arg_list an ArrayList of Strings containing the Airline and Flight data.
   * @return an Airline reference, referencing the created Airline.
   * @throws IllegalArgumentException Exception not thrown in the method but rather in the methods
   *                                  invoked by the method and the error is pased to the calling
   *                                  routine.
   */
  protected Flight parseFlight(String aName, ArrayList<String> arg_list) throws IllegalArgumentException
  {
    if(!aName.equals(arg_list.get(0)))
        throw new IllegalArgumentException("Second Airline (Name " + arg_list.get(0) + ") identified in file. Please" +
                " make sure each file contains data for only one airline");
    if(arg_list.size() != 10)
      return null;
    return new Flight(arg_list.get(1),arg_list.get(2),arg_list.get(6),arg_list.get(3),
            arg_list.get(4), arg_list.get(5), arg_list.get(8), arg_list.get(7), arg_list.get(9));
  }

  /**
   * splitLine tokenizes the line provided and returns an ArrayList of Strings containing the data
   * used to create an airline and/or flight.
   * @param line a String referencing the line to be tokenized.
   * @return a ArrayList of Strings holding the parsed data.
   * @throws IllegalArgumentException Thrown if the data parsed from the line doesn't
   *                                  contain the correct number of arguments.
   */
  private static ArrayList<String> splitLine(String line) throws IllegalArgumentException {

    StringTokenizer temp = new StringTokenizer(line, ",");
    ArrayList<String> arg_list = new ArrayList<>(10);

    while(temp.hasMoreTokens())
    {
      arg_list.add(temp.nextToken());
    }

    if(arg_list.size() < 10 && arg_list.size()>1 || arg_list.isEmpty())
      throw new IllegalArgumentException(CommandLineParser.toFewArguments(arg_list));
    else if(arg_list.size() > 10)
     throw new IllegalArgumentException(CommandLineParser.toManyArguments(arg_list));

    return arg_list;
  }


}
