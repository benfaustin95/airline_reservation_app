package edu.pdx.cs410J.bena2;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.util.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class Project4Test {

    @Test
    public void testSplitOptionsAndArgsValid() {
        String[] test = {"-README", "-host","host", "-port","8080", "args", "source"};
        HashMap<String,String> options = new HashMap<>();
        ArrayList<String> args = new ArrayList<>();
        String[] name;

        Project4.splitOptionsAndArgs(test, args, options);

        assertThat(options.get("-host"), equalTo("host"));
        assertThat(options.get("-port"), equalTo("8080"));
        assertThat(options.size(), equalTo(3));
        assertThat(args.size(), equalTo(2));
    }

    @Test
    public void testSplitOptionsAndArgsMissingFile() {
        String[] test = {"-README", "-readme", "-textFile"};
        HashMap<String,String> options = new HashMap<>();
        ArrayList<String> args = new ArrayList<>();

        Project4.splitOptionsAndArgs(test,args,options);
        assertNull(options.get("-readme"));

    }

    @Test
    public void testSplitOptionsAndArgsNoOptions() {
        String[] test = {"-README", "-host", "textFile"};
        HashMap<String,String> options = new HashMap<>();
        ArrayList<String> args = new ArrayList<>();

        Project4.splitOptionsAndArgs(test, args, options);

        assertThat(options.get("-README"), equalTo(null));
        assertThat(options.size(), equalTo(2));
        assertThat(options.get("-host"), equalTo("textFile"));
        assertThat(args.size(), equalTo(0));

    }
    @Test
    public void testSplitOptionsAndArgsDuplicateOptions() {
        String[] test = {"README", "readme", "textFile","-README","-README"};
        HashMap<String,String> options = new HashMap<>();
        ArrayList<String> args = new ArrayList<>();

        Project4.splitOptionsAndArgs(test, args, options);

        assertThat(options.get("-README"), equalTo(null));
        assertThat(options.size(), equalTo(1));
        assertThat(args.size(), equalTo(3));

    }

    @Test
    public void testAddFlight()
    {
        Project4 test = new Project4();

        Flight one = test.createAirlineAndFlight(CommandLineParserTest.getValidFlightData());
        Flight two = test.createAirlineAndFlight(CommandLineParserTest.getValidFlightData());

        assertThat(one, equalTo(two));
    }
    @Test
    @Disabled
    public void testAddFlightWrongAirline()
    {
        Project4 test = new Project4();

        test.createAirlineAndFlight(CommandLineParserTest.getValidFlightData());
        assertThrows(IllegalArgumentException.class,
                ()->test.createAirlineAndFlight(CommandLineParserTest.getInvalidFlightData()));
    }
}