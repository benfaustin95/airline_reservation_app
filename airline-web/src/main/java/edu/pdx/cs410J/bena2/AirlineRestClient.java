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

    @VisibleForTesting
    AirlineRestClient(HttpRequestHelper http) {
      this.http = http;
    }

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
   * Returns the definition for the given word
   */
  private Airline getAirline(Response response) throws IOException, ParserException , RestException{
          throwExceptionIfNotOkayHttpStatus(response);
          String content = response.getContent();

          XMLParser parser = new XMLParser(new StringReader(content));
          return parser.parse();
  }

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

  private void throwExceptionIfNotOkayHttpStatus(Response response) throws RestException{
    int code = response.getHttpStatusCode();
    if (code != HTTP_OK) {
      String message = response.getContent();
      throw new RestException(code, message);
    }
  }

}
