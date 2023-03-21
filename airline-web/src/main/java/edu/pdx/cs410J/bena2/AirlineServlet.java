package edu.pdx.cs410J.bena2;

import com.google.common.annotations.VisibleForTesting;
import edu.pdx.cs410J.AirportNames;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.IllformedLocaleException;
import java.util.Map;

/**
 * This servlet ultimately provides a REST API for working with an
 * <code>Airline</code>.  However, in its current state, it is an example
 * of how to use HTTP and Java servlets to store simple dictionary of words
 * and their definitions.
 */
public class AirlineServlet extends HttpServlet {
    static final String AIRLINE_PARAMETER = "airline";
    static final String SRC_PARAMETER = "src";
    static final String DST_PARAMETER ="dest";
    static final String FNUMBER_PARAMETER = "flightNumber";
    static final String DEPART_PARAMETER = "depart";
    static final String ARRIVE_PARAMETER = "arrive";

    private final Map<String, Airline> airport = new HashMap<>();

  /**
   * Handles an HTTP GET request from a client by writing the definition of the
   * word specified in the "word" HTTP parameter to the HTTP response.  If the
   * "word" parameter is not specified, all of the entries in the dictionary
   * are written to the HTTP response.
   */
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
  {
      String airline = getParameter(AIRLINE_PARAMETER, request);
      String src = getParameter(SRC_PARAMETER, request);
      String dst = getParameter(DST_PARAMETER, request);

      response.setContentType( "text/plain" );

      switch (validateGetParameters(airline, src, dst, response)){
          case 1:
              writeFlights(response, 1, airline);
              break;
          case 2:
              writeFlights(response, 2, airline, src, dst);
              break;
          case 4:
              writeAllAirlines(response);
              break;
          default:
              break;
      }
      return;
  }

    /**
     * Writes all airlines in "airport" to response.
     * @param response the HttpServletResponse to be written too.
     * @throws IOException thrown if an error with Server.
     */
    protected void writeAllAirlines(HttpServletResponse response) throws IOException {

      if(airport.isEmpty()) {
          response.sendError(HttpServletResponse.SC_NOT_FOUND, "No airlines currently stored " +
                  "in program");
          return;
      }

      for(String name: airport.keySet()){
          writeFlights(response, 1, name);
      }

    }

    /**
     * dumps desired airline to response writer.
     * @param response the HttpServletResponse to be written too.
     * @param i the type of execution.
     * @param strings the search arguments.
     * @throws IOException thrown if an error with Server.
     */
    private void writeFlights(HttpServletResponse response, int i, String ... strings) throws IOException {
        XMLDumper dumper = new XMLDumper(response.getWriter());
        Airline airline = airport.get(strings[0]);

        if(airline == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Airline "+strings[0]+" does not " +
                    "exist");
            return;
        }

        if(i == 1){
            dumper.dump(airline);
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        airline = new Airline(airline, strings[1], strings[2]);

        if(airline.getFlights().size() == 0) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND,String.format("%s contains no direct flights " +
                            "between %s(%s) and %s(%s)",airline.getName(), strings[1], AirportNames.getName(strings[1].toUpperCase()),
                    strings[2], AirportNames.getName(strings[2].toUpperCase())));
            return;
        }
        dumper.dump(airline);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    /**
     * validates that the provided get parameters are valid.
     * @param airline the airline name.
     * @param src     the desired source.
     * @param dst     the desired destination.
     * @param response the response error should be sent to if encoountered.
     * @return         returns int holding execution type as determined by arguments.
     * @throws IOException thrown if an error with Server.
     */


    /**
   * Handles an HTTP POST request by building flight from parameters and either adding to existing
     * airline or creating new airline.
     * @param response the response to be written to.
     * @param request  the request to be read from.
   */
  @Override
  protected void doPost( HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
      response.setContentType( "text/plain" );

      String aName = getParameter(AIRLINE_PARAMETER, request);
      String fnumber = getParameter(FNUMBER_PARAMETER, request);
      String src = getParameter(SRC_PARAMETER, request);
      String depart = getParameter(DEPART_PARAMETER, request);
      String dst = getParameter(DST_PARAMETER, request);
      String arrive = getParameter(ARRIVE_PARAMETER, request);

      try{
          Airline airline = airport.get(aName);
          Flight flight = new Flight(fnumber, src, dst, depart, arrive);
          if(airline == null)
              airport.put(aName, new Airline(aName, flight));
          else
            airline.addFlight(flight);

          PrintWriter pw = response.getWriter();
          pw.println(Messages.addedFlight(airport.get(aName), flight));
          pw.flush();
          response.setStatus( HttpServletResponse.SC_OK);
      }catch(IllegalArgumentException ex){
          response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
      }
  }

  /**
   * Handles an HTTP DELETE request by removing all airlines .  This
   * behavior is exposed for testing purposes only.
   * @param request  the request to be read from.
   * @param response the response to be written to.
   */
  @Override
  protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
      response.setContentType("text/plain");

      this.airport.clear();

      PrintWriter pw = response.getWriter();
      pw.println(Messages.allAirlinesDeleted());
      pw.flush();

      response.setStatus(HttpServletResponse.SC_OK);
  }

  /**
   * Writes an error message about a missing parameter to the HTTP response.
   * The text of the error message is created by {@link Messages#missingRequiredParameter(String)}
   */
  private void missingRequiredParameter( HttpServletResponse response, String parameterName )
      throws IOException
  {
      String message = Messages.missingRequiredParameter(parameterName);
      response.sendError(HttpServletResponse.SC_BAD_REQUEST, message);
  }

  /**
   * Returns the value of the HTTP request parameter with the given name.
   *
   * @return <code>null</code> if the value of the parameter is
   *         <code>null</code> or is the empty string
   */
  private String getParameter(String name, HttpServletRequest request) {
    String value = request.getParameter(name);
    if (value == null || "".equals(value)) {
      return null;

    } else {
      return value;
    }
  }
}
