package edu.pdx.cs410J.bena2;

import com.google.common.annotations.VisibleForTesting;
import edu.pdx.cs410J.ParserException;
import edu.pdx.cs410J.web.HttpRequestHelper;

import java.io.IOException;
import java.io.StringReader;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import static edu.pdx.cs410J.web.HttpRequestHelper.Response;
import static edu.pdx.cs410J.web.HttpRequestHelper.RestException;
import static java.net.HttpURLConnection.HTTP_OK;

/**
 * A helper class for accessing the rest client.  Note that this class provides
 * an example of how to make gets and posts to a URL.  You'll need to change it
 * to do something other than just send dictionary entries.
 */
public class AirlineRestClient
{
    private static final String WEB_APP = "airline";
    private static final String SERVLET = "flights";

    private final HttpRequestHelper http;


    /**
     * Creates a client to the airline REST service running on the given host and port
     * @param hostName The name of the host
     * @param port The port
     */
    public AirlineRestClient( String hostName, int port )
    {
        this(new HttpRequestHelper(String.format("http://%s:%d/%s/%s", hostName, port, WEB_APP, SERVLET)));
    }

    /**
     * Called by the above constructor, sets instance HttpRequestHelper reference.
     * @param http the HttpRequestHelper the instance reference should point to.
     */
    @VisibleForTesting
    AirlineRestClient(HttpRequestHelper http) {
      this.http = http;
    }

    /**
     * getAirline called when search is executed with just an airline name.
     * @param airline a String referencing the Airline to be returned
     * @return  the Airline found.
     * @throws IOException thrown if connection to server can not be established
     * @throws ParserException not thrown by method, but rather thrown by calling routine
     *                         and not caught.
     * @throws RestException   not thrown by method, but rather thrown by calling routine
     *                         and not caught.
     */
    public Airline getAirline(String airline) throws IOException, ParserException, RestException{
        try{
            Response response = http.get(Map.of(AirlineServlet.AIRLINE_PARAMETER, airline));
            return getAirline(response);
        }catch (UnknownHostException ex)
        {
            throw new IOException("unknown host "+ex.getMessage());
        }
        catch (ConnectException ex){
            throw new IOException(ex.getMessage()+" invalid port");
        }

    }

    /**
     * getAirline called when search is executed with an airline name, source, and destination.
     * @param airline a String referencing the Airline to be returned
     * @param  src    a String referencing the Source airport.
     * @param  dst    a String referencing the Destination airport.
     * @return  the Airline found.
     * @throws IOException thrown if connection to server can not be established
     * @throws ParserException not thrown by method, but rather thrown by calling routine
     *                         and not caught.
     * @throws RestException   not thrown by method, but rather thrown by calling routine
     *                         and not caught.
     */
    public Airline getAirline(String airline, String src, String dst) throws IOException, ParserException, RestException{
        try{
            Response response = http.get(Map.of(AirlineServlet.AIRLINE_PARAMETER, airline, AirlineServlet.SRC_PARAMETER,
                    src, AirlineServlet.DST_PARAMETER, dst));
            return getAirline(response);
        }catch (UnknownHostException ex)
        {
            throw new IOException("unknown host "+ex.getMessage());
        }
        catch (ConnectException ex){
            throw new IOException(ex.getMessage()+" invalid port");
        }

    }
  /**
   * getAirline is called by the previous definitions of getAirline, extracts and parses the content
   * returned from the get request.
   * @param response the Response returned from the get request.
   * @return  the Airline parsed.
   * @throws ParserException not thrown by method, but rather thrown by calling routine
   *                         and not caught.
   * @throws RestException   not thrown by method, but rather thrown by calling routine
   *                         and not caught.
   */
  private Airline getAirline(Response response) throws IOException, ParserException , RestException{
          throwExceptionIfNotOkayHttpStatus(response);
          String content = response.getContent();

          XMLParser parser = new XMLParser(new StringReader(content));
          return parser.parse();
  }

    /**
     *  addFlight builds and sends the post request to add the given flight to the airline with the
     *  given name.
     * @param aName          the name of the airline
     * @param flight         the Flight to be added.
     * @throws IOException   thrown if connection to server can not be established
     * @throws RestException not thrown by method, but rather thrown by calling routine
     *                       and not caught.
     */
  public void addFlight(String aName, Flight flight) throws IOException, RestException{
      try{
          Map<String, String> map = new HashMap<>();
          map.put(AirlineServlet.AIRLINE_PARAMETER, aName);
          flight.put(map);
          Response response = http.post(map);
          throwExceptionIfNotOkayHttpStatus(response);
      }catch (UnknownHostException ex)
      {
          throw new IOException("unknown host "+ex.getMessage());
      }
      catch (ConnectException ex){
          throw new IOException(ex.getMessage()+" invalid port");
      }
  }

    /**
     * removeAllAirlines deletes all airlines from server.
     * @throws IOException Thrown if connection to the server can not be established
     * @throws RestException not thrown by method, by rather thrown by the calling routine and not
     *                       caught.
     */
  public void removeAllAirlines() throws IOException, RestException{
      try {
          Response response = http.delete(Map.of());
          throwExceptionIfNotOkayHttpStatus(response);
      }catch (UnknownHostException ex)
      {
          throw new IOException("unknown host "+ex.getMessage());
      }
      catch (ConnectException ex){
          throw new IOException(ex.getMessage()+" invalid port");
      }
  }

    /**
     * throwExecptionIfNotOkayHttpStatus throws RestException if HttpStatus is not 2xx.
     * @param response the Response holding content and HttpStatus.
     * @throws RestException thrown if the error message and code if the HttpStatus is not 2xx.
     */
  private void throwExceptionIfNotOkayHttpStatus(Response response) throws RestException{
    int code = response.getHttpStatusCode();
    if (code != HTTP_OK) {
      String message = response.getContent();
      throw new RestException(code, message);
    }
  }

}
