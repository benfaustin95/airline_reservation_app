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

  @Override
  public Airline parse() throws ParserException {

    Airline airline = null;
    int counter = 0;


    try (BufferedReader br = new BufferedReader(this.reader)) {
      String line;
      ArrayList<String> field_list = null;

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
      throw new ParserException("While parsing file line " + counter +":\n "+e.getMessage(), e);
    }

    if(counter == 0)
      throw new ParserException("File is empty, file provided must have contents or not yet" +
              "exist");
    return airline;
  }

  protected Airline parseAirline(ArrayList<String> arg_list) throws IllegalArgumentException
  {
    if(arg_list.size() == 1)
      return new Airline(arg_list.get(0));

    return new Airline(arg_list.get(0),new Flight(arg_list.get(1),arg_list.get(2),
            arg_list.get(5),arg_list.get(3),arg_list.get(4),arg_list.get(6),arg_list.get(7)));
  }


  protected Flight parseFlight(ArrayList<String> arg_list) throws IllegalArgumentException
  {
      return new Flight(arg_list.get(1),arg_list.get(2),arg_list.get(5),arg_list.get(3),
              arg_list.get(4),arg_list.get(6),arg_list.get(7));
  }
  protected Flight parseFlight(String aName, ArrayList<String> arg_list) throws IllegalArgumentException
  {
    if(!aName.equals(arg_list.get(0)))
        throw new IllegalArgumentException("Second Airline (Name " + arg_list.get(0) + ") identified in file. Please" +
                " make sure each file contains data for only one airline");
    if(arg_list.size() != 8)
      return null;
    return new Flight(arg_list.get(1),arg_list.get(2),arg_list.get(5),arg_list.get(3),
            arg_list.get(4),arg_list.get(6),arg_list.get(7));
  }

  private static ArrayList<String> splitLine(String line) throws IllegalArgumentException {

    StringTokenizer temp = new StringTokenizer(line, ",");
    ArrayList<String> arg_list = new ArrayList<>(8);

    while(temp.hasMoreTokens())
    {
      arg_list.add(temp.nextToken());
    }

    if(arg_list.size() < 8 && arg_list.size()>1)
      throw new IllegalArgumentException(Project1.toFewArguments(arg_list));
    else if(arg_list.size() > 8)
     throw new IllegalArgumentException(Project1.toManyArguments(arg_list));

    return arg_list;
  }

  public Airport parseAirport() throws ParserException {
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
}
