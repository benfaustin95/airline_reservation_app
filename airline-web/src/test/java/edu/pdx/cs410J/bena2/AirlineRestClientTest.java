package edu.pdx.cs410J.bena2;

import edu.pdx.cs410J.ParserException;
import edu.pdx.cs410J.web.HttpRequestHelper;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * A unit test for the REST client that demonstrates using mocks and
 * dependency injection
 */
public class AirlineRestClientTest {
    @Test
    void testGetAirline(){
       HttpRequestHelper  helper = mock(HttpRequestHelper.class);
       AirlineRestClient client = new AirlineRestClient(helper);
       HttpRequestHelper.Response response = mock(HttpRequestHelper.Response.class);

        try(StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw)) {

            XMLDumper temp = new XMLDumper(sw);
            temp.dump(AirlineTest.getValidAirline());

            when(helper.get(Map.of(AirlineServlet.AIRLINE_PARAMETER,
                    "test"))).thenReturn(response);
            when(response.getContent()).thenReturn(sw.toString());
            when(response.getHttpStatusCode()).thenReturn(200);
            Airline airline = client.getAirline("test");

            verify(helper).get(Map.of(AirlineServlet.AIRLINE_PARAMETER,"test"));
            assertThat(airline.getFlights().size(), equalTo(1));
        } catch (Exception e) {
            fail(e.getMessage());
        }

    }

    protected Map<String, String> getValidAirport() {
       HashMap<String, String> toReturn = new HashMap<>();
       toReturn.put(AirlineServlet.AIRLINE_PARAMETER, "test");
       FlightTest.getValidFlight().put(toReturn);
       return toReturn;
    }

    @Test
    void testGetAirline2(){
        HttpRequestHelper  helper = mock(HttpRequestHelper.class);
        AirlineRestClient client = new AirlineRestClient(helper);
        HttpRequestHelper.Response response = mock(HttpRequestHelper.Response.class);

        try(StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw)) {

            XMLDumper temp = new XMLDumper(sw);
            temp.dump(AirlineTest.getValidAirline());

            when(helper.get(Map.of(AirlineServlet.AIRLINE_PARAMETER,
                    "test", AirlineServlet.SRC_PARAMETER, "Pdx",
                    AirlineServlet.DST_PARAMETER,"sea"))).thenReturn(response);
            when(response.getContent()).thenReturn(sw.toString());
            when(response.getHttpStatusCode()).thenReturn(200);
            Airline airline = client.getAirline("test","Pdx","sea");

            verify(helper).get(Map.of(AirlineServlet.AIRLINE_PARAMETER,
                    "test", AirlineServlet.SRC_PARAMETER, "Pdx",
                    AirlineServlet.DST_PARAMETER,"sea"));
            assertThat(airline.getFlights().size(), equalTo(1));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testAddFlight(){
        HttpRequestHelper helper =mock(HttpRequestHelper.class);
        AirlineRestClient client =  new AirlineRestClient(helper);
        HttpRequestHelper.Response test = mock(HttpRequestHelper.Response.class);

        try {
            when(helper.post(getValidAirport())).thenReturn(test);
            when(test.getHttpStatusCode()).thenReturn(200);

            client.addFlight("test",FlightTest.getValidFlight());

            verify(test).getHttpStatusCode();

        } catch (Exception e) {
            fail(e.getMessage());
        }


    }

    @Test
    void testRemoveAllFlights(){
        HttpRequestHelper helper =mock(HttpRequestHelper.class);
        AirlineRestClient client =  new AirlineRestClient(helper);
        HttpRequestHelper.Response test = mock(HttpRequestHelper.Response.class);

        try {
            when(helper.delete(Map.of())).thenReturn(test);
            when(test.getHttpStatusCode()).thenReturn(200);

            client.removeAllAirlines();
            verify(helper).delete(Map.of());
        } catch (Exception e) {
            fail(e.getMessage());
        }

    }

    @Test
    void testRemoveAllFlightsError(){
        HttpRequestHelper helper =mock(HttpRequestHelper.class);
        AirlineRestClient client =  new AirlineRestClient(helper);
        HttpRequestHelper.Response test = mock(HttpRequestHelper.Response.class);

        try {
            when(helper.delete(Map.of())).thenReturn(test);
            when(test.getHttpStatusCode()).thenReturn(412);
            when(test.getContent()).thenReturn("Error");
            assertThrows(HttpRequestHelper.RestException.class, client::removeAllAirlines);
            verify(helper).delete(Map.of());
        } catch (Exception e) {
            fail(e.getMessage());
        }

    }
}
