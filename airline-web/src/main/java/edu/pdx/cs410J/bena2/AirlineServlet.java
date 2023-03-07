package edu.pdx.cs410J.bena2;

import com.google.common.annotations.VisibleForTesting;

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
    static final String DEFINITION_PARAMETER = "definition";
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
          default:
              break;
      }
      return;
  }

    private void writeFlights(HttpServletResponse response, int i, String ... strings) throws IOException {
      XMLDumper dumper = new XMLDumper(response.getWriter());
      Airline airline = airport.get(strings[0]);

      if(airline == null) {
          response.setStatus(HttpServletResponse.SC_NOT_FOUND, "Airline "+strings[0]+" does not " +
                  "exist");
          return;
      }
      if(i == 1)
          dumper.dump(airline);
      else
          dumper.dump(new Airline(airline, strings[1], strings[2]));

      response.setStatus(HttpServletResponse.SC_OK);
    }

    protected int validateGetParameters(String airline, String src, String dst, HttpServletResponse response) throws IOException{

      if(airline == null) {
          missingRequiredParameter(response, AIRLINE_PARAMETER);
          return 3;
      }
      if(src == null && dst == null) return 1;

      if(src == null || dst == null) {
          missingRequiredParameter(response, (src == null ? SRC_PARAMETER : DST_PARAMETER));
          return 3;
      }

      return 2;
    }

    /**
   * Handles an HTTP POST request by storing the dictionary entry for the
   * "word" and "definition" request parameters.  It writes the dictionary
   * entry to the HTTP response.
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
   * Handles an HTTP DELETE request by removing all dictionary entries.  This
   * behavior is exposed for testing purposes only.  It's probably not
   * something that you'd want a real application to expose.
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
   *
   * The text of the error message is created by {@link Messages#missingRequiredParameter(String)}
   */
  private void missingRequiredParameter( HttpServletResponse response, String parameterName )
      throws IOException
  {
      String message = Messages.missingRequiredParameter(parameterName);
      response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, message);
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
