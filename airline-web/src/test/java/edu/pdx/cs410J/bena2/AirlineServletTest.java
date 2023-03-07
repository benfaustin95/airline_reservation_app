package edu.pdx.cs410J.bena2;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

/**
 * A unit test for the {@link AirlineServlet}.  It uses mockito to
 * provide mock http requests and responses.
 */
class AirlineServletTest {

  /* Tests for HTTP get requests*/
  @Test
  void testEmptyAirportGetReturnsError() {
    AirlineServlet servlet = new AirlineServlet();

    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    PrintWriter pw = mock(PrintWriter.class);

    try {
      when(response.getWriter()).thenReturn(pw);
      when(request.getParameter(AirlineServlet.AIRLINE_PARAMETER)).thenReturn("test");
      when(request.getParameter(AirlineServlet.SRC_PARAMETER)).thenReturn(null);
      when(request.getParameter(AirlineServlet.DST_PARAMETER)).thenReturn(null);

      servlet.doGet(request, response);

      // Nothing is written to the response's PrintWriter
      verify(pw, never()).println(anyString());
      verify(response).setContentType("text/plain");
      verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND, "Airline test does not exist");
    }catch (Exception ex){
      fail(ex.getMessage());
    }
  }

  @Test
  void addOneFlightToAirline(){
    AirlineServlet servlet = new AirlineServlet();

    String aName= "TEST AIRLINE";
    String fNumber= "123";
    String src ="PDX";
    String depart="10/10/2023 10:39 AM";
    String dest = "SEA";
    String arrive ="10/10/2023 10:43 AM";


    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getParameter(AirlineServlet.AIRLINE_PARAMETER)).thenReturn(aName);
    when(request.getParameter(AirlineServlet.FNUMBER_PARAMETER)).thenReturn(fNumber);
    when(request.getParameter(AirlineServlet.ARRIVE_PARAMETER)).thenReturn(arrive);
    when(request.getParameter(AirlineServlet.SRC_PARAMETER)).thenReturn(src);
    when(request.getParameter(AirlineServlet.DEPART_PARAMETER)).thenReturn(depart);
    when(request.getParameter(AirlineServlet.DST_PARAMETER)).thenReturn(dest);

    HttpServletResponse response = mock(HttpServletResponse.class);

    // Use a StringWriter to gather the text from multiple calls to println()
    try(
    StringWriter stringWriter = new StringWriter();
    PrintWriter pw = new PrintWriter(stringWriter, true)) {

      when(response.getWriter()).thenReturn(pw);

      servlet.doPost(request, response);

      StringWriter out = new StringWriter();
      XMLDumper dumper = new XMLDumper(out);

      dumper.dump(new Airline(aName,new Flight(fNumber,src, dest, Flight.validateDate(depart,0), Flight.validateDate(arrive,1))));
      assertThat(stringWriter.toString(), containsString("Flight 123 added to TEST AIRLINE"));

      // Use an ArgumentCaptor when you want to make multiple assertions against the value passed to the mock
      ArgumentCaptor<Integer> statusCode = ArgumentCaptor.forClass(Integer.class);
      verify(response).setStatus(statusCode.capture());

      assertThat(statusCode.getValue(), equalTo(HttpServletResponse.SC_OK));

    }catch (Exception ex){
      fail(ex.getMessage());
    }
  }

}
