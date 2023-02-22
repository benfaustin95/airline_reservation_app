package edu.pdx.cs410J.bena2;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class Project4Test {

    @Test
    public void testSplitOptionsAndArgsValid() {
        String[] test = {"-README", "-readme", "-textFile", "text.txt", "args", "source"};
        Set<String> options = new HashSet<>();
        ArrayList<String> args = new ArrayList<>();
        String[] name;

        name = Project4.splitOptionsAndArgs(test, args, options);

        assertThat(name[0], equalTo("text.txt"));
        assertThat(options.size(), equalTo(3));
        assertThat(args.size(), equalTo(2));
    }

    @Test
    public void testSplitOptionsAndArgsMissingFile() {
        String[] test = {"-README", "-readme", "-textFile"};
        Set<String> options = new HashSet<>();
        ArrayList<String> args = new ArrayList<>();

        assertNull(Project4.splitOptionsAndArgs(test, args, options)[0]);

    }
    @Test
    public void testSplitOptionsAndArgsNoOptions() {
        String[] test = {"README", "readme", "textFile"};
        Set<String> options = new HashSet<>();
        ArrayList<String> args = new ArrayList<>();
        String[] name;

        name = Project4.splitOptionsAndArgs(test, args, options);

        assertThat(name[0], equalTo(null));
        assertThat(options.size(), equalTo(0));
        assertThat(args.size(), equalTo(3));

    }
    @Test
    public void testSplitOptionsAndArgsDuplicateOptions() {
        String[] test = {"README", "readme", "textFile","-README","-README"};
        Set<String> options = new HashSet<>();
        ArrayList<String> args = new ArrayList<>();
        String[] name;

        name = Project4.splitOptionsAndArgs(test, args, options);

        assertThat(name[0], equalTo(null));
        assertThat(options.size(), equalTo(1));
        assertThat(args.size(), equalTo(3));

    }
    @Test
    public void testAddFlight()
    {
        Project4 test = new Project4();

        test.createAirlineAndFlight(CommandLineParserTest.getValidFlightData());
        test.createAirlineAndFlight(CommandLineParserTest.getValidFlightData());

        assertThat(test.airline.getFlights().size(), equalTo(2));
    }
    @Test
    public void testAddFlightWrongAirline()
    {
        Project4 test = new Project4();

        test.createAirlineAndFlight(CommandLineParserTest.getValidFlightData());
        assertThrows(IllegalArgumentException.class,
                ()->test.createAirlineAndFlight(CommandLineParserTest.getInvalidFlightData()));
    }

    @Test
    public void testParseFileValid(@TempDir File dir) {
        File file = new File(dir,"text.txt");
        Project4 test = new Project4();
        Project4 test2 = new Project4();
        test.createAirlineAndFlight(CommandLineParserTest.getValidFlightData());

        try {
            test.dumpFile(file,0);
            test2.parseFile(file,0);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        assertTrue(test.airline.equals(test2.airline));
    }

}
