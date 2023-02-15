package edu.pdx.cs410J.bena2;

import edu.pdx.cs410J.ParserException;
import org.junit.jupiter.api.Test;
import org.xml.sax.InputSource;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
public class XMLParserTest {

    @Test
    public void testCanParseValidXML()
    {
       try(InputStream res = this.getClass().getResourceAsStream("valid-airline.xml"))
       {
           assertNotNull(res);

           XMLParser parser = new XMLParser(res);
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
        invalidFile("invalid-BadSystemId-airline.xml","error opening XML file: " +
                "http://web.cecs.pdx.edu/~whitlock/dts/airline.dtd");

    }



    protected void invalidFile(String name, String error) {

        try(InputStream res = this.getClass().getResourceAsStream(name)) {
            assertNotNull(res);
            XMLParser parser = new XMLParser(res);

            parser.parse();

        } catch (IOException e) {
            fail(e.getMessage());
        } catch (ParserException e) {
            assertThat(e.getMessage(), equalTo(error));
        }

    }
}
