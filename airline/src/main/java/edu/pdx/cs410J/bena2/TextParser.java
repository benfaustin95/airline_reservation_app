package edu.pdx.cs410J.bena2;

import com.sun.source.tree.TryTree;
import edu.pdx.cs410J.AirlineParser;
import edu.pdx.cs410J.ParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * A skeletal implementation of the <code>TextParser</code> class for Project 2.
 */
public class TextParser implements AirlineParser<Airline> {
  private final Reader reader;

  public TextParser(Reader reader) {
    this.reader = reader;
  }

  public Airport parseAirport() throws ParserException, IllegalArgumentException {
    Airport toReturn = new Airport("temp_name");
    int counter = 0;

    try (BufferedReader br = new BufferedReader(this.reader)) {
      String line;
      ArrayList<String> field_list = null;
      Airline toAdd = null;

      while ((line = br.readLine()) != null) {
        field_list = splitLine(line);
        ++counter;

        if(toAdd != null && toAdd.equals(field_list.get(0)))
          toAdd.addFlight(parseFlight(field_list));
        else if((toAdd = toReturn.getAirline(field_list.get(0))) == null)
          toReturn.addAirline(parseAirline(field_list));
        else
          toAdd.addFlight(parseFlight(field_list));

      }
    } catch (IOException | IllegalArgumentException ex) {
          throw new ParserException("Failure line "+counter,ex);
    }

    return toReturn;
  }

  protected Airline parseAirline(ArrayList<String> arg_list) throws IllegalArgumentException
  {
    return new Airline(arg_list.get(0),new Flight(arg_list.get(1),arg_list.get(2),
            arg_list.get(5),arg_list.get(3),arg_list.get(4),arg_list.get(6),arg_list.get(7)));
  }


  protected Flight parseFlight(ArrayList<String> arg_list) throws IllegalArgumentException
  {
      return new Flight(arg_list.get(1),arg_list.get(2),arg_list.get(5),arg_list.get(3),
              arg_list.get(4),arg_list.get(6),arg_list.get(7));
  }

  private static ArrayList<String> splitLine(String line) throws IllegalArgumentException{

    StringTokenizer temp = new StringTokenizer(line, ",");
    ArrayList<String> arg_list = new ArrayList<>(8);

    while(temp.hasMoreTokens())
    {
      arg_list.add(temp.nextToken());
    }

    if(arg_list.size()!=8)
      throw new IllegalArgumentException("bad arguments");
    return arg_list;
  }
  @Override
  public Airline parse() throws ParserException {

    /*String line;
    Airline airline = null;
    int counter = 0;


    try (BufferedReader br = new BufferedReader(this.reader)) {

      while ((line = br.readLine()) != null) {
        ++counter;
        if (airline == null)
          airline = parseAirline(line);
        else
          airline.addFlight(parseFlight(airline.getName(), line));
      }
    }
    catch (IOException | IllegalArgumentException e) {
      throw new ParserException("While parsing airline text line " + counter, e);
    }
   catch(ParserException ex)
   {
     return airline;
   }
    return airline;*/
    return null;
  }

  /*
    buf reader in parseAirport
    read arguments (splitline)
    check name if airport contains airline with name add flight to airline
    else create airline and instantiate with flight
    loop until line is null
   */
}
