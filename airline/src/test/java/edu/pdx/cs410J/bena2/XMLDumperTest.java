package edu.pdx.cs410J.bena2;

import edu.pdx.cs410J.ParserException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.*;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
public class XMLDumperTest {

    @Test
    public void testDumpValidAirline(@TempDir File dir)
    {
        File file = new File(dir, "test.txt");
        try(Writer out = new PrintWriter(new FileWriter(file));
            Reader in = new FileReader(file)){
           XMLDumper dumper = new XMLDumper(out);
           dumper.dump(AirlineTest.getValidAirline());

           XMLParser parser = new XMLParser(in);
           Airline test = parser.parse();
           assertThat(test.getName(), equalTo(AirlineTest.getValidAirline().getName()));
           assertThat(test.getFlights().size(), equalTo(1));
        } catch (IOException | NullPointerException | ParserException e) {
            fail(e);
        }
    }


    @Test
    public void testDumpValidAirlineTwo() {
        try(StringWriter out = new StringWriter()) {
           XMLDumper dumper = new XMLDumper(out);

           dumper.dump(AirlineTest.getValidAirline());

           assertThat(out.toString(), containsString("<date day=\"1\" month=\"1\" year=\"2023\""));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testDumpNullAirline(@TempDir File dir)
    {
        try(PrintWriter pw = new PrintWriter(new File(dir,"test"))) {
            XMLDumper dumper = new XMLDumper(pw);
            assertThrows(NullPointerException.class, ()->dumper.dump(null));
        } catch (FileNotFoundException e) {
            fail(e);
        }
    }

}
