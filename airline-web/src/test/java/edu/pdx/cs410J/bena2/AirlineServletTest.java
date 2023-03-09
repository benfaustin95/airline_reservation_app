package edu.pdx.cs410J.bena2;

import edu.pdx.cs410J.ParserException;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.then;
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
      verify(response).sendError(HttpServletResponse.SC_NOT_FOUND, "Airline test does not exist");
    }catch (Exception ex){
      fail(ex.getMessage());
    }
  }

  @Test
  void testInvalidAirline(){
    try(StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw)) {

      AirlineServlet servlet = getValidServlet(1);
      HttpServletRequest request = getMockRequest("test", null, "");
      HttpServletResponse response = getMockResponse(pw);

      servlet.doGet(request, response);
      verify(response).sendError(HttpServletResponse.SC_NOT_FOUND, "Airline test does not exist");

    }catch (IOException ex){
      fail(ex.getMessage());
    }


  }

  protected static HttpServletResponse getMockResponse(PrintWriter pw) {
    HttpServletResponse toReturn = mock(HttpServletResponse.class);

    try {
      when(toReturn.getWriter()).thenReturn(pw);
    } catch (IOException e) {
      fail(e.getMessage());
    }
    return toReturn;
  }

  protected static HttpServletRequest getMockRequest(String test, String o, String s) {
    HttpServletRequest toReturn = mock(HttpServletRequest.class);

    when(toReturn.getParameter(AirlineServlet.AIRLINE_PARAMETER)).thenReturn(test);
    when(toReturn.getParameter(AirlineServlet.SRC_PARAMETER)).thenReturn(o);
    when(toReturn.getParameter(AirlineServlet.DST_PARAMETER)).thenReturn(s);

    return  toReturn;
  }

  protected AirlineServlet getValidServlet(int flightCount) {

    AirlineServlet toReturn = new AirlineServlet();

     try(StringWriter sw = new StringWriter();
         PrintWriter pw = new PrintWriter(sw)) {
    for(int i=1; i<flightCount+1; ++i) {
      HttpServletResponse response = mock(HttpServletResponse.class);
      HttpServletRequest request = mock(HttpServletRequest.class);

      when(request.getParameter(AirlineServlet.AIRLINE_PARAMETER)).thenReturn("test"+i);
      when(request.getParameter(AirlineServlet.SRC_PARAMETER)).thenReturn("PDX");
      when(request.getParameter(AirlineServlet.DST_PARAMETER)).thenReturn("SEA");
      when(request.getParameter(AirlineServlet.FNUMBER_PARAMETER)).thenReturn(String.valueOf(i));
      when(request.getParameter(AirlineServlet.DEPART_PARAMETER)).thenReturn("1/1/2023 10:23 AM");
      when(request.getParameter(AirlineServlet.ARRIVE_PARAMETER)).thenReturn("1/1/2023 10:32 PM");
      when(response.getWriter()).thenReturn(pw);
      toReturn.doPost(request, response);
      verify(response).setStatus(HttpServletResponse.SC_OK);
    };
    }catch (IOException ex){
     fail(ex.getMessage());
   }

   return toReturn;
  }

  @Test
  void testValidAirline(){
   try(StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw)) {
     HttpServletResponse rep = buildMockServlet(4, "test1", "", "",pw);

     verify(rep).setStatus(HttpServletResponse.SC_OK);
     assertThat(sw.toString(), containsString("<name>test1</name>\n"));

   }catch (IOException ex){
     fail(ex.getMessage());
   }
  }

  @Test
  void testNoAirlineButSRCAndDest()
  {
    try(StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw)){
      HttpServletResponse rep = buildMockServlet(4, "" , "PDX", "SEA", pw);
      verify(rep).sendError(HttpServletResponse.SC_BAD_REQUEST, "The required parameter " +
              "\"airline\" is missing");
    } catch (IOException e) {
      fail(e.getMessage());
    }
  }

  private HttpServletResponse buildMockServlet(int flightCount, String test1, String o, String s,PrintWriter pw) throws IOException {
    AirlineServlet test = getValidServlet(flightCount);
    HttpServletRequest req = getMockRequest(test1, o, s);
    HttpServletResponse rep = getMockResponse(pw);

    test.doGet(req, rep);
    return rep;
  }

  private HttpServletResponse buildMockServlet(int flightCount, String test1, Flight flight,PrintWriter pw) throws IOException {
    AirlineServlet test = getValidServlet(flightCount);
    HttpServletResponse rep = getMockResponse(pw);
    HttpServletRequest request = mock(HttpServletRequest.class);

    when(request.getParameter(AirlineServlet.AIRLINE_PARAMETER)).thenReturn(test1);
    when(request.getParameter(AirlineServlet.SRC_PARAMETER)).thenReturn(flight.getSource());
    when(request.getParameter(AirlineServlet.DST_PARAMETER)).thenReturn(flight.getDestination());
    when(request.getParameter(AirlineServlet.FNUMBER_PARAMETER)).thenReturn(String.valueOf(flight.getNumber()));
    when(request.getParameter(AirlineServlet.DEPART_PARAMETER)).thenReturn(flight.getDepartureString());
    when(request.getParameter(AirlineServlet.ARRIVE_PARAMETER)).thenReturn(flight.getArrivalString());

    test.doPost(request, rep);
    return rep;
  }
  @Test
  void testAllAirlines(){
    try(StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw)) {
      HttpServletResponse rep = buildMockServlet(4, "", "","",pw);
      verify(rep,times(4)).setStatus(HttpServletResponse.SC_OK);
      assertThat(sw.toString(), containsString("<name>test3</name>\n"));

    }catch (IOException ex){
      fail(ex.getMessage());
    }
  }

  @Test
  void testValidSrcDest() {
    try (StringWriter sw = new StringWriter();
         PrintWriter pw = new PrintWriter(sw)) {
      HttpServletResponse rep = buildMockServlet(4, "test2", "PDX", "SEA", pw);
      verify(rep, times(1)).setStatus(HttpServletResponse.SC_OK);
      String xml = sw.toString();
      assertThat(new XMLParser(new StringReader(xml)).parse().getFlights().size(), equalTo(1));

    } catch (Exception ex) {
      fail(ex.getMessage());
    }
  }

  @Test
  void  testInvalidSrcValidDst() {

    try (StringWriter sw = new StringWriter();
         PrintWriter pw = new PrintWriter(sw)) {
      HttpServletResponse rep = buildMockServlet(10, "test2", "PIX", "SEA", pw);
      verify(rep, times(1)).sendError(HttpServletResponse.SC_BAD_REQUEST,
              "Source Location PIX is invalid, must be a known airport.");
    } catch (Exception ex) {
      fail(ex.getMessage());
    }
  }

  @Test
  void  testValidSrcInvalidDst() {
    try (StringWriter sw = new StringWriter();
         PrintWriter pw = new PrintWriter(sw)) {
      HttpServletResponse rep = buildMockServlet(3, "test2", "PDX", "SIA", pw);
      verify(rep, times(1)).sendError(HttpServletResponse.SC_BAD_REQUEST,
              "Destination Location SIA is invalid, must be a known airport.");
    } catch (Exception ex) {
      fail(ex.getMessage());
    }
  }

  @Test
  void  testInvalidSrcInvalidDst() {
    try (StringWriter sw = new StringWriter();
         PrintWriter pw = new PrintWriter(sw)) {
      HttpServletResponse rep = buildMockServlet(3, "test2", "PIX", "SIA", pw);
      verify(rep, times(1)).sendError(HttpServletResponse.SC_BAD_REQUEST,
              "Source Location PIX is invalid, must be a known airport.");
    } catch (Exception ex) {
      fail(ex.getMessage());
    }
  }
  @Test
  void testNoSrcNoDest(){
    try (StringWriter sw = new StringWriter();
         PrintWriter pw = new PrintWriter(sw)) {
      HttpServletResponse rep = buildMockServlet(3, "test2", "", "", pw);
      String out = sw.toString();
      assertThat(new XMLParser(new StringReader(out)).parse().getFlights().size(), equalTo(1));
    } catch (Exception ex) {
      fail(ex.getMessage());
    }
  }

  @Test
  void testValidSrcAndDestinationNoFlights(){
    try (StringWriter sw = new StringWriter();
         PrintWriter pw = new PrintWriter(sw)) {
      HttpServletResponse rep = buildMockServlet(5, "test2", "JFK", "JFk", pw);
      String out = sw.toString();
      assertThat(new XMLParser(new StringReader(out)).parse().getFlights().size(), equalTo(0));
    } catch (Exception ex) {
      fail(ex.getMessage());
    }

  }
  @Test
  void testInvalidSrcNoDest(){
    try (StringWriter sw = new StringWriter();
         PrintWriter pw = new PrintWriter(sw)) {
      HttpServletResponse rep = buildMockServlet(5, "test2", "PDX", "", pw);
      verify(rep).sendError(HttpServletResponse.SC_BAD_REQUEST, "If a source airport is " +
              "provided a destination airport is required");
    } catch (Exception ex) {
      fail(ex.getMessage());
    }

  }
  @Test
  void testNoSrcInvalidDest(){
    try (StringWriter sw = new StringWriter();
         PrintWriter pw = new PrintWriter(sw)) {
      HttpServletResponse rep = buildMockServlet(5, "test2", "", "SEA", pw);
    verify(rep).sendError(HttpServletResponse.SC_BAD_REQUEST, "If a destination airport is " +
            "provided a source airport is required");
    } catch (Exception ex) {
      fail(ex.getMessage());
    }
  }

 /* Tests for HTTP Post Requests*/


  @Test
  void testValidPostSingleFlight(){
    try (StringWriter sw = new StringWriter();
         PrintWriter pw = new PrintWriter(sw)) {
      HttpServletResponse rep = buildMockServlet(5, "test2",FlightTest.getValidFlight(), pw);
      verify(rep).setStatus(HttpServletResponse.SC_OK);
      assertThat(sw.toString(), equalTo("Flight 1 added to test2\n"));
    } catch (Exception ex) {
      fail(ex.getMessage());
    }
  }

  @Test
  void testValidPostSingleMultipleFlights(){
    Flight flight = FlightTest.getValidFlight();
    try (StringWriter sw = new StringWriter();
         PrintWriter pw = new PrintWriter(sw)) {
      AirlineServlet test = getValidServlet(4);
      HttpServletResponse rep = getMockResponse(pw);
      HttpServletRequest request = mock(HttpServletRequest.class);

      for(int i=0; i<4; ++i) {
        when(request.getParameter(AirlineServlet.AIRLINE_PARAMETER)).thenReturn("test1");
        when(request.getParameter(AirlineServlet.SRC_PARAMETER)).thenReturn(flight.getSource());
        when(request.getParameter(AirlineServlet.DST_PARAMETER)).thenReturn(flight.getDestination());
        when(request.getParameter(AirlineServlet.FNUMBER_PARAMETER)).thenReturn(String.valueOf(flight.getNumber()));
        when(request.getParameter(AirlineServlet.DEPART_PARAMETER)).thenReturn(flight.getDepartureString());
        when(request.getParameter(AirlineServlet.ARRIVE_PARAMETER)).thenReturn(flight.getArrivalString());
        test.doPost(request, rep);
      }

      verify(rep, times(4)).setStatus(HttpServletResponse.SC_OK);

      request = getMockRequest("test1", "","");
      StringWriter sw2 = new StringWriter();
      rep = getMockResponse(new PrintWriter(sw2));
      test.doGet(request, rep);

      verify(rep, times(1)).setStatus(HttpServletResponse.SC_OK);
      assertThat(new XMLParser(new StringReader(sw2.toString())).parse().getFlights().size(),equalTo(5));

      request = getMockRequest("test1", "PDX","SEA");
      sw2.close();
      sw2 = new StringWriter();
      rep = getMockResponse(new PrintWriter(sw2));
      test.doGet(request, rep);

      verify(rep, times(1)).setStatus(HttpServletResponse.SC_OK);
      assertThat(new XMLParser(new StringReader(sw2.toString())).parse().getFlights().size(),equalTo(5));

      request = getMockRequest("test1", "JFK","SEA");
      sw2.close();
      sw2 = new StringWriter();
      rep = getMockResponse(new PrintWriter(sw2));
      test.doGet(request, rep);

      verify(rep, times(1)).setStatus(HttpServletResponse.SC_OK);
      assertThat(new XMLParser(new StringReader(sw2.toString())).parse().getFlights().size(),equalTo(0));
    } catch (Exception ex) {
      fail(ex.getMessage());
    }
  }

  @Test
  void testValidPostSingleMultipleFlightsMultipleAirlines(){

    Flight flight = FlightTest.getValidFlight();
    try (StringWriter sw = new StringWriter();
         PrintWriter pw = new PrintWriter(sw)) {
      AirlineServlet test = getValidServlet(4);
      HttpServletResponse rep = getMockResponse(pw);
      HttpServletRequest request = mock(HttpServletRequest.class);

      for(int i=0; i<4; ++i) {
        when(request.getParameter(AirlineServlet.AIRLINE_PARAMETER)).thenReturn("test1");
        when(request.getParameter(AirlineServlet.SRC_PARAMETER)).thenReturn(flight.getSource());
        when(request.getParameter(AirlineServlet.DST_PARAMETER)).thenReturn(flight.getDestination());
        when(request.getParameter(AirlineServlet.FNUMBER_PARAMETER)).thenReturn(String.valueOf(flight.getNumber()));
        when(request.getParameter(AirlineServlet.DEPART_PARAMETER)).thenReturn(flight.getDepartureString());
        when(request.getParameter(AirlineServlet.ARRIVE_PARAMETER)).thenReturn(flight.getArrivalString());
        test.doPost(request, rep);
      }

      verify(rep, times(4)).setStatus(HttpServletResponse.SC_OK);

      for(int i=0; i<4; ++i) {
        when(request.getParameter(AirlineServlet.AIRLINE_PARAMETER)).thenReturn("test2");
        when(request.getParameter(AirlineServlet.SRC_PARAMETER)).thenReturn(flight.getSource());
        when(request.getParameter(AirlineServlet.DST_PARAMETER)).thenReturn(flight.getDestination());
        when(request.getParameter(AirlineServlet.FNUMBER_PARAMETER)).thenReturn(String.valueOf(flight.getNumber()));
        when(request.getParameter(AirlineServlet.DEPART_PARAMETER)).thenReturn(flight.getDepartureString());
        when(request.getParameter(AirlineServlet.ARRIVE_PARAMETER)).thenReturn(flight.getArrivalString());
        test.doPost(request, rep);
      }

      verify(rep, times(8)).setStatus(HttpServletResponse.SC_OK);

      request = getMockRequest("test1", "","");
      StringWriter sw2 = new StringWriter();
      rep = getMockResponse(new PrintWriter(sw2));
      test.doGet(request, rep);

      verify(rep, times(1)).setStatus(HttpServletResponse.SC_OK);
      assertThat(new XMLParser(new StringReader(sw2.toString())).parse().getFlights().size(),equalTo(5));

      request = getMockRequest("test2", "","");
      sw2.close();
      sw2 = new StringWriter();
      rep = getMockResponse(new PrintWriter(sw2));
      test.doGet(request, rep);

      verify(rep, times(1)).setStatus(HttpServletResponse.SC_OK);
      assertThat(new XMLParser(new StringReader(sw2.toString())).parse().getFlights().size(),equalTo(5));

      request = getMockRequest("test3", "","");
      sw2.close();
      sw2 = new StringWriter();
      rep = getMockResponse(new PrintWriter(sw2));
      test.doGet(request, rep);

      verify(rep, times(1)).setStatus(HttpServletResponse.SC_OK);
      assertThat(new XMLParser(new StringReader(sw2.toString())).parse().getFlights().size(),equalTo(1));
    } catch (Exception ex) {
      fail(ex.getMessage());
    }
  }

  @Test
  void testInvalidAirlineName(){
    try (StringWriter sw = new StringWriter();
         PrintWriter pw = new PrintWriter(sw)) {
      HttpServletResponse rep = buildMockServlet(5, "",FlightTest.getValidFlight(), pw);
      verify(rep).sendError(HttpServletResponse.SC_BAD_REQUEST, "Name null is invalid, must not be empty.");
    } catch (Exception ex) {
      fail(ex.getMessage());
    }
  }

  @Test
  void testInvalidFNumber(){
    Flight flight = FlightTest.getValidFlight();
    try (StringWriter sw = new StringWriter();
         PrintWriter pw = new PrintWriter(sw)) {
      AirlineServlet servlet = getValidServlet(10);
      HttpServletResponse rep = getMockResponse(pw);
      HttpServletRequest request = mock(HttpServletRequest.class);

      when(request.getParameter(AirlineServlet.AIRLINE_PARAMETER)).thenReturn("test2");
      when(request.getParameter(AirlineServlet.SRC_PARAMETER)).thenReturn(flight.getSource());
      when(request.getParameter(AirlineServlet.DST_PARAMETER)).thenReturn(flight.getDestination());
      when(request.getParameter(AirlineServlet.FNUMBER_PARAMETER)).thenReturn(String.valueOf(0));
      when(request.getParameter(AirlineServlet.DEPART_PARAMETER)).thenReturn(flight.getDepartureString());
      when(request.getParameter(AirlineServlet.ARRIVE_PARAMETER)).thenReturn(flight.getArrivalString());
      servlet.doPost(request, rep);

      verify(rep).sendError(HttpServletResponse.SC_BAD_REQUEST, "Flight Number 0 is invalid, " +
              "must be greater than zero.");
    } catch (Exception ex) {
      fail(ex.getMessage());
    }
  }

  @Test
  void testInvalidSource(){
    Flight flight = FlightTest.getValidFlight();
    try (StringWriter sw = new StringWriter();
         PrintWriter pw = new PrintWriter(sw)) {
      AirlineServlet servlet = getValidServlet(10);
      HttpServletResponse rep = getMockResponse(pw);
      HttpServletRequest request = mock(HttpServletRequest.class);

      when(request.getParameter(AirlineServlet.AIRLINE_PARAMETER)).thenReturn("test2");
      when(request.getParameter(AirlineServlet.SRC_PARAMETER)).thenReturn("src");
      when(request.getParameter(AirlineServlet.DST_PARAMETER)).thenReturn(flight.getDestination());
      when(request.getParameter(AirlineServlet.FNUMBER_PARAMETER)).thenReturn(String.valueOf(flight.getNumber()));
      when(request.getParameter(AirlineServlet.DEPART_PARAMETER)).thenReturn(flight.getDepartureString());
      when(request.getParameter(AirlineServlet.ARRIVE_PARAMETER)).thenReturn(flight.getArrivalString());
      servlet.doPost(request, rep);

      verify(rep).sendError(HttpServletResponse.SC_BAD_REQUEST, "Source Location src is invalid, must be a known airport.");
    } catch (Exception ex) {
      fail(ex.getMessage());
    }

  }

  @Test
  void testInvalidSRC2(){
    Flight flight = FlightTest.getValidFlight();
    try (StringWriter sw = new StringWriter();
         PrintWriter pw = new PrintWriter(sw)) {
      AirlineServlet servlet = getValidServlet(10);
      HttpServletResponse rep = getMockResponse(pw);
      HttpServletRequest request = mock(HttpServletRequest.class);

      when(request.getParameter(AirlineServlet.AIRLINE_PARAMETER)).thenReturn("test2");
      when(request.getParameter(AirlineServlet.SRC_PARAMETER)).thenReturn("DS23");
      when(request.getParameter(AirlineServlet.DST_PARAMETER)).thenReturn(flight.getDestination());
      when(request.getParameter(AirlineServlet.FNUMBER_PARAMETER)).thenReturn(String.valueOf(flight.getNumber()));
      when(request.getParameter(AirlineServlet.DEPART_PARAMETER)).thenReturn(flight.getDepartureString());
      when(request.getParameter(AirlineServlet.ARRIVE_PARAMETER)).thenReturn(flight.getArrivalString());
      servlet.doPost(request, rep);

      verify(rep).sendError(HttpServletResponse.SC_BAD_REQUEST, "Source Location DS23 is invalid, " +
              "format must be three alphabetic letters.");
    } catch (Exception ex) {
      fail(ex.getMessage());
    }
  }

  @Test
  void testInvalidDepart(){
    Flight flight = FlightTest.getValidFlight();
    try (StringWriter sw = new StringWriter();
         PrintWriter pw = new PrintWriter(sw)) {
      AirlineServlet servlet = getValidServlet(10);
      HttpServletResponse rep = getMockResponse(pw);
      HttpServletRequest request = mock(HttpServletRequest.class);

      when(request.getParameter(AirlineServlet.AIRLINE_PARAMETER)).thenReturn("test2");
      when(request.getParameter(AirlineServlet.SRC_PARAMETER)).thenReturn("Pdx");
      when(request.getParameter(AirlineServlet.DST_PARAMETER)).thenReturn(flight.getDestination());
      when(request.getParameter(AirlineServlet.FNUMBER_PARAMETER)).thenReturn(String.valueOf(flight.getNumber()));
      when(request.getParameter(AirlineServlet.DEPART_PARAMETER)).thenReturn("10/10-1212 10:23 XP");
      when(request.getParameter(AirlineServlet.ARRIVE_PARAMETER)).thenReturn(flight.getArrivalString());
      servlet.doPost(request, rep);

      verify(rep).sendError(HttpServletResponse.SC_BAD_REQUEST, "Departure date 10/10-1212 is " +
              "invalid, date must be in format mm/dd/yyyy");
    } catch (Exception ex) {
      fail(ex.getMessage());
    }
  }

  @Test
  void testDeleteAirport() {
    AirlineServlet servlet = getValidServlet(5);
    try(StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw)){
      HttpServletResponse rep = getMockResponse(pw);
      HttpServletRequest request = getMockRequest("", "","");
      servlet.doGet(request, rep);
      verify(rep, times(5)).setStatus(HttpServletResponse.SC_OK);
      assertThat(sw.toString(), containsString("<name>test3</name>"));

      StringWriter sw2 = new StringWriter();
      HttpServletResponse rep2 = getMockResponse(new PrintWriter(sw2));

      servlet.doDelete(request, rep2);
      verify(rep2).setStatus(HttpServletResponse.SC_OK);

      servlet.doGet(request, rep2);
      verify(rep2).sendError(HttpServletResponse.SC_NOT_FOUND, "No airlines currently stored in " +
              "program");


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
