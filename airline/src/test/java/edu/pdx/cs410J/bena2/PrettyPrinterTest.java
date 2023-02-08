package edu.pdx.cs410J.bena2;

import edu.pdx.cs410J.ParserException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
public class PrettyPrinterTest {

    @Test
    public void testPrettyPrintOUT()
    {
        StringWriter sw = new StringWriter();

        PrettyPrinter pp = new PrettyPrinter(sw);

        pp.dump(AirlineTest.getValidAirline());

        String out = sw.toString();

        assertThat(out, equalTo("Airline: name\nCurrent Flight Roster:\n"+FlightTest.validFlightDump+"\n"));
    }

    @Test
    public void testOrderedFlights()
    {
        Airline test = AirlineTest.getValidAirline();
        AirlineTest.addManyFlightsToAirline(test);

        try(StringWriter sw = new StringWriter())
        {
            PrettyPrinter pw = new PrettyPrinter(sw);

            pw.dump(test);

            String outPut = sw.toString();
            assertThat(outPut.length(), equalTo(754));
            assertTrue(outPut.startsWith("Airline: name\nCurrent Flight Roster:\nFlight 4"));
        }
        catch (IOException | IllegalArgumentException ex)
        {
            fail(ex.getMessage());
        }
    }
    @Test
    public void testPrettyPrintFile(@TempDir File dir)
    {
        File file = new File(dir, "text.txt");

       try(InputStream fr = getClass().getResourceAsStream("valid-airline.txt");
           FileWriter fw = new FileWriter(file);
           BufferedReader br = new BufferedReader(new FileReader(file)))
       {
          TextParser parser = new TextParser(new InputStreamReader(fr));

          Airline testIn = parser.parse();

          PrettyPrinter testOut = new PrettyPrinter(fw);

          testOut.dump(testIn);

          assertThat(br.readLine(), equalTo("Airline: Test Airline"));
          assertThat(file.length(), equalTo((long)45+9*(4+FlightTest.validFlightDump.length())));

       }
       catch (IOException | ParserException ex)
       {
         fail(ex.getMessage());
       }
    }


    @Test
    public void prettyPrintNullAirline()
    {
        PrettyPrinter test = new PrettyPrinter(System.out);
        assertThrows(IllegalArgumentException.class, () -> test.dump(null));
    }


}
