package edu.pdx.cs410J.bena2;

import edu.pdx.cs410J.AirlineDumper;
import edu.pdx.cs410J.ParserException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

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

        assertThat(out, containsString(header));
        assertThat(out, containsString(FlightTest.validFlightDump));
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
            assertThat(outPut.length(), equalTo(1624));
            assertTrue(outPut.startsWith("name flight roster as of "+PrettyPrinter.today(new Date())));;
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

          assertThat(br.readLine(), equalTo("Test Airline flight roster as of "+PrettyPrinter.today(new Date())));
          assertThat(file.length(), equalTo(2652L));

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

    @Test
    public void testToday()
    {
        assertThat(PrettyPrinter.today(Flight.validateDateAndTime("1/1/2023","10:39", "am",0)), equalTo("Jan 01st 2023"));
        assertThat(PrettyPrinter.today(Flight.validateDateAndTime("1/2/2023","10:39", "am",0)), equalTo("Jan 02nd 2023"));
        assertThat(PrettyPrinter.today(Flight.validateDateAndTime("1/3/2023","10:39", "am",0)), equalTo("Jan 03rd 2023"));
        assertThat(PrettyPrinter.today(Flight.validateDateAndTime("1/4/2023","10:39", "am",0)), equalTo("Jan 04th 2023"));
    }

    @Test
    public void testNullStreamAndWriter(){
        AirlineDumper<Airline> test = new PrettyPrinter((Writer) null);

        try {
            test.dump(AirlineTest.getValidAirline());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }


    protected static String header = "| Flight Number |      Source      |        Departure       " +
            " |   Destination    |         Arrival         |    Length     |\n";
}
