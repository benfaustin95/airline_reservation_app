package edu.pdx.cs410J.bena2;

import edu.pdx.cs410J.ParserException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
public class XMLParserTest {

    @Test
    public void testCanParseValidXML()
    {
       try(InputStream res = this.getClass().getResourceAsStream("valid-airline.xml"))
       {
           assertNotNull(res);

           XMLParser parser = new XMLParser(new InputStreamReader(res));
           Airline test = parser.parse();

           assertThat(test.getName(), equalTo("Valid Airlines"));
           assertThat(test.getFlights().size(), equalTo(2));
       }
       catch (IOException | ParserException ex){
           fail(ex.getMessage());
       }
    }

    @Test
    public void testInvalidFile(){
        invalidFile("invalid-airline.xml","XML file does not conform to DTD: The c" +
                "ontent of element type \"depart\" is incomplete, it must match \"(date,time)\".");
        invalidFile("invalid-EmptyFile-airline.xml","XML file does not conform to DTD: " +
                "Premature end of file.");
        invalidFile("invalid-MissingId-airline.xml","XML file does not conform to DTD: " +
                "Element type \"airline\" must be declared.");
        invalidFile("invalid-MissingType-airline.xml","XML file does not conform to " +
                "DTD: Element type \"encoding\" must be followed by either attribute " +
                "specifications, \">\" or \"/>\".");
        invalidFile("invalid-MissingSysID-airline.xml","XML file does not conform to " +
                "DTD: Element type \"airline\" must be declared.");
        invalidFile("invalid-MissingSysID-airline.xml","XML file does not conform to " +
                "DTD: Element type \"airline\" must be declared.");
        invalidFile("invalid-MissingAirlineElement-airline.xml","XML file does not conform" +
                " to DTD: Premature end of file.");
        invalidFile("invalid-MissingFlight-airline.xml","XML file does not conform to DTD:" +
                " The content of element type \"airline\" must match \"(name,flight*)\".");
        invalidFile("invalid-MissingNumber-airline.xml","XML file does not conform to DTD: " +
                "The content of element type \"flight\" must match \"(number,src,depart,dest,arrive)\".");
        /*invalidFile("invalid-MissingDate2-airline.xml","XML file does not conform to DTD: " +
                "The content of element type \"flight\" must match \"(number,src,depart,dest,arrive)\".");*/
        invalidFile("invalid-MissingSrc-airline.xml","XML file does not conform to DTD: " +
                "The content of element type \"flight\" must match \"(number,src,depart,dest,arrive)\".");
        invalidFile("invalid-EmptyAirline-airline.xml","XML file does not conform to DTD: " +
                "The content of element type \"airline\" is incomplete, it must match \"(name,flight*)\".");
        invalidFile("invalid-EmptyFlight-airline.xml","XML file does not conform to DTD: " +
                "The content of element type \"flight\" is incomplete, it must match \"(number,src," +
                "depart,dest,arrive)\".");
        invalidFile("invalid-EmptyNumber-airline.xml","Flight : empty number element");
        invalidFile("invalid-EmptySRC-airline.xml","Flight 1437: empty src element");
        invalidFile("invalid-EmptyDate-airline.xml","XML file does not conform to DTD: " +
                        "Attribute \"day\" is required and must be specified for element type \"date\".");
        invalidFile("invalid-EmptyTime-airline.xml","XML file does not conform to DTD: " +
                "Attribute \"hour\" is required and must be specified for element type \"time\".");
        invalidFile("invalid-EmptyName-airline.xml","Empty airline name element");
        invalidFile("invalid-MissingName-airline.xml","XML file does not conform to DTD:" +
                " The content of element type \"airline\" must match \"(name,flight*)\".");
        invalidFile("invalid-BadName-airline.xml","Name    is invalid, must not be empty.");
        invalidFile("invalid-BadNumber-airline.xml","Flight  : Flight Number   is " +
                "invalid, must be numeric.");
        invalidFile("invalid-BadNumber2-airline.xml","Flight bf1: Flight Number bf1 is" +
                " invalid, must be numeric.");
        invalidFile("invalid-BadSource-airline.xml","Flight 1437: Source Location  is " +
                "invalid, format must be three alphabetic letters.");
        invalidFile("invalid-BadSource2-airline.xml","Flight 1437: Source Location BJ2 " +
                "is invalid, format must be three alphabetic letters.");
        invalidFile("invalid-BadSource3-airline.xml","Flight 1437: Source Location SRC " +
                "is invalid, must be a known airport.");
        invalidFile("invalid-BadDate-airline.xml","Flight 1437: Departure date 9/a/2020 " +
                "is invalid, date must be in format mm/dd/yyyy");
        invalidFile("invalid-BadDate2-airline.xml","Flight 1437: Departure date 9/32/2020 "+
                "is invalid, date must be in format mm/dd/yyyy");
        invalidFile("invalid-BadDate3-airline.xml","Flight 1437: Departure date 9//2020 " +
                "is invalid, date must be in format mm/dd/yyyy");
        invalidFile("invalid-BadTime-airline.xml","Flight 1437: Invalid minute value: ");
        invalidFile("invalid-BadTime2-airline.xml","Flight 1437: Invalid minute value: a");
        invalidFile("invalid-BadTime3-airline.xml","Flight 1437: Departure time 17:-1 " +
                "is invalid, time must be in format hh:mm (24 hour format)");
        invalidFile("invalid-BadTime4-airline.xml","Flight 1437: Departure time a:00 is " +
                "invalid, time must be in format hh:mm (24 hour format)");
        invalidFile("invalid-BadTime5-airline.xml","Flight 1437: Departure time 25:00 is " +
                "invalid, time must be in format hh:mm (24 hour format)");
        invalidFile("invalid-MultipleAirlines-airline.xml","XML file does not conform " +
                "to DTD: The markup in the document following the root element must be well-formed.");
        invalidFile("valid-airline.txt","XML file does not conform to DTD: Content is not " +
                "allowed in prolog.");
        invalidFile("invalid-BadTime6-airline.xml","Flight 1437: Departure time 23:72 " +
                "is invalid, time must be in format hh:mm (24 hour format)");

    }



    protected void invalidFile(String name, String error) {

        try(InputStream res = this.getClass().getResourceAsStream(name)) {
            assertNotNull(res);
            XMLParser parser = new XMLParser(new InputStreamReader(res));

            ParserException c = assertThrows(ParserException.class, parser::parse);
            assertThat(c.getMessage(), equalTo(error));
        } catch (IOException e) {
            fail(e.getMessage());
        }

    }
}
