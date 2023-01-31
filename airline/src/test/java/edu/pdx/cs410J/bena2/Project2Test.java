package edu.pdx.cs410J.bena2;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Project2Test {

    @Test
    public void testSplitOptionsAndArgsValid() {
        String[] test = {"-README", "-readme", "-textFile", "text.txt", "args", "source"};
        Set<String> options = new HashSet<>();
        ArrayList<String> args = new ArrayList<>();
        String name = null;

        name = Project2.splitOptionsAndArgs(test, args, options);

        assertThat(name, equalTo("text.txt"));
        assertThat(options.size(), equalTo(3));
        assertThat(args.size(), equalTo(2));
    }

    @Test
    public void testSplitOptionsAndArgsMissingFile() {
        String[] test = {"-README", "-readme", "-textFile"};
        Set<String> options = new HashSet<>();
        ArrayList<String> args = new ArrayList<>();
        String name = null;

        assertThrows(IllegalArgumentException.class,()->Project2.splitOptionsAndArgs(test, args, options));

    }
    @Test
    public void testSplitOptionsAndArgsNoOptions() {
        String[] test = {"README", "readme", "textFile"};
        Set<String> options = new HashSet<>();
        ArrayList<String> args = new ArrayList<>();
        String name = null;

        name = Project2.splitOptionsAndArgs(test, args, options);

        assertThat(name, equalTo(null));
        assertThat(options.size(), equalTo(0));
        assertThat(args.size(), equalTo(3));

    }
    @Test
    public void testSplitOptionsAndArgsDuplicateOptions() {
        String[] test = {"README", "readme", "textFile","-README","-README"};
        Set<String> options = new HashSet<>();
        ArrayList<String> args = new ArrayList<>();
        String name = null;

        name = Project2.splitOptionsAndArgs(test, args, options);

        assertThat(name, equalTo(null));
        assertThat(options.size(), equalTo(1));
        assertThat(args.size(), equalTo(3));

    }
    @Test
    public void testAddFlight()
    {
        Project2 test = new Project2();

        test.createAirlineAndFlight(Project1Test.getValidFlightData());
        test.addFlight(Project1Test.getValidFlightData());

        assertThat(test.airline.getFlights().size(), equalTo(2));
    }
    @Test
    public void testAddFlightWrongAirline()
    {
        Project2 test = new Project2();

        test.createAirlineAndFlight(Project1Test.getValidFlightData());
        assertThrows(IllegalArgumentException.class,
                ()->test.addFlight(Project1Test.getInvalidFlightData()));
    }

    @Test
    public void testParseFileValid(@TempDir File dir) {
        File file = new File(dir,"text.txt");
        Project2 test = new Project2();
        Project2 test2 = new Project2();
        test.createAirlineAndFlight(Project1Test.getValidFlightData());

        test.dumpFile(file);
        test2.parseFile(file);

        assertTrue(test.airline.equals(test2.airline));
    }

}