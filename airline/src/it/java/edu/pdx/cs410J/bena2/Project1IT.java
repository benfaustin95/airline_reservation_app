package edu.pdx.cs410J.bena2;

import edu.pdx.cs410J.InvokeMainTestCase;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * An integration test for the {@link Project1} main class.
 */
class Project1IT extends InvokeMainTestCase {

    /**
     * Invokes the main method of {@link Project1} with the given arguments.
     */
    private MainMethodResult invokeMain(String... args) {
        return invokeMain( Project1.class, args );
    }

    /*
    Invalid Command Line options Tests:
     */

    /**
     * Tests that invoking the main method with no arguments issues an error
     */
    @Test
    void testNoCommandLineArguments() {
        MainMethodResult result = invokeMain();
        assertThat(result.getTextWrittenToStandardError(), containsString(Project1.missingArguments));
    }

    /**
     * Tests that invoking the main method with no valid command line options instantiates a flight
     */
    @Test
    void testNoCommandLineOptions() {
        MainMethodResult result = invokeMain( "name", "1", "src", "1/1/2023","10:39", "dsn", "1/1/2023", "19:49");
        assertThat(result.getTextWrittenToStandardError(), containsString(""));
        assertThat(result.getTextWrittenToStandardOut(),containsString(""));

    }

    /**
     * Tests that invoking the main method with too few command line arguments and no -README command
     * results in error
     */
    @Test
    void testOnlyPrintMeAndTooFewArguments() {
        MainMethodResult result = invokeMain("-print","1","src","1/1/2023","10:39","dsw","1/1/2023","19:49");
        assertThat(result.getTextWrittenToStandardError(), containsString(Project1.fewArguments));
        assertThat(result.getTextWrittenToStandardOut(),containsString(""));
    }

    /**
     * Tests that invoking the main method with too many command line arguments and no -README command
     * results in error
     */
    @Test
    void testTooManyArguments() {
        MainMethodResult result = invokeMain("-print", "name", "1", "src", "1/1/2023", "10:39", "dsw", "1/1/2023", "11:39", "extra argument");
        assertThat(result.getTextWrittenToStandardError(), containsString("To many arguments to create a flight, please see README " +
                "for further instruction on valid command line arguments."));

        result = invokeMain( "name", "1", "src", "1/1/2023", "10:39", "dsw", "1/1/2023", "11:39", "extra argument");
        assertThat(result.getTextWrittenToStandardError(), containsString("To many arguments to create a flight, please see README " +
                "for further instruction on valid command line arguments."));
    }

    /**
     * Tests that invoking the main method with valid number of command line arguments and -print option results in
     * correct standard out
     */
    @Test
    void testOnlyPrintAndValidFlightDataOnCommandLine() {
        MainMethodResult result = invokeMain("-print", "name", "1", "src", "1/1/2023", "10:39", "dsw", "1/1/2023", "19:49");
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight 1 departs src at 01/01/2023 10:39 arrives dsw at 01/01/2023 19:49"));
        assertThat(result.getTextWrittenToStandardError(), containsString(""));

        result = invokeMain( "name", "1", "src", "1/1/2023", "10:39", "dsw", "1/1/2023", "19:49");
        assertThat(result.getTextWrittenToStandardOut(), containsString(""));
        assertThat(result.getTextWrittenToStandardError(), containsString(""));
    }

    /*
    README command line tests
     */

    @Test
    void testOnlyREADMEOnCommandLine() {
        MainMethodResult result = invokeMain("-README");
        assertThat(result.getTextWrittenToStandardOut(), containsString("Ben Austin, CS410J-001, 1/1/2023, bena2@pdx.edu"));
    }

    @Test
    void testREADMEAndFlightDataOnCommandLine() {
        MainMethodResult result = invokeMain("-README","name","1","src","1/1/2023","10:39","dsn","1/1/2023","19:49");
        assertThat(result.getTextWrittenToStandardOut(), containsString("Ben Austin, CS410J-001, 1/1/2023, bena2@pdx.edu"));
    }

    @Test
    void testREADMEAndPrintMeAndFlightDataOnCommandLine() {
        MainMethodResult result = invokeMain("-README","-print","name","1","src","1/1/2023","10:39","dsn","1/1/2023","19:49");
        assertThat(result.getTextWrittenToStandardOut(), containsString("Ben Austin, CS410J-001, 1/1/2023, bena2@pdx.edu"));
    }

    @Test
    void testREADMEAndPrintMeOnCommandLine() {
        MainMethodResult result = invokeMain("-README","-print");
        assertThat(result.getTextWrittenToStandardOut(), containsString("Ben Austin, CS410J-001, 1/1/2023, bena2@pdx.edu"));
    }

    @Test
    void testOnlyInvalidREADMEOnCommandLine() {
        MainMethodResult result = invokeMain("- README");
        assertThat(result.getTextWrittenToStandardError(), containsString(Project1.fewArguments));
        MainMethodResult result1 = invokeMain("README");
        assertThat(result1.getTextWrittenToStandardError(), containsString(Project1.fewArguments));
        MainMethodResult result2 = invokeMain("readme");
        assertThat(result2.getTextWrittenToStandardError(), containsString(Project1.fewArguments));
        MainMethodResult result3 = invokeMain("-readme");
        assertThat(result3.getTextWrittenToStandardError(), containsString(Project1.fewArguments));
    }

    @Test
    void testInvalidREADMEOnAndOtherCommandLine() {
        MainMethodResult result = invokeMain("- README", "-print","name","1","src","1/1/2023","10:39","dsw","1/1/2023","13:50");
        assertThat(result.getTextWrittenToStandardError(), containsString(Project1.manyArguments));
        MainMethodResult result1 = invokeMain("README","-print");
        assertThat(result1.getTextWrittenToStandardError(), containsString(Project1.fewArguments));
        MainMethodResult result2 = invokeMain("README", "-prin", "test","test");
        assertThat(result2.getTextWrittenToStandardError(), containsString(Project1.fewArguments));
        MainMethodResult result3 = invokeMain("-readme","-textfile");
        assertThat(result3.getTextWrittenToStandardError(), containsString(Project1.fewArguments));
    }

    /*
      Print me test
     */
    @Test
    void testOnlyPrintMeOnCommandLine() {
        MainMethodResult result = invokeMain("-print");
        assertThat(result.getTextWrittenToStandardError(), containsString("To few arguments to create a flight, please see README" +
                " for further instruction on valid command line arguments."));
    }

    @Test
    void testPrintMeOnCommandLineToFewArguments() {
        MainMethodResult result = invokeMain("-print","name","src","1/1/2023","10:39","dsw");
        assertThat(result.getTextWrittenToStandardError(), containsString(Project1.fewArguments));
    }

    @Test
    void testPrintMeOnCommandLineToManyArguments() {
        MainMethodResult result = invokeMain("-print","name", "1", "1", "src","1/1/2023","10:39","dsw","1/1/2023","14:23");
        assertThat(result.getTextWrittenToStandardError(), containsString(Project1.manyArguments));
    }

    @Test
    void testPrintMeOnCommandLineValid() {
        MainMethodResult result = invokeMain("-print","name", "1", "src","1/1/2023","10:39","dsw","1/1/2023","14:23");
        assertThat(result.getTextWrittenToStandardError(), containsString(""));
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight 1 departs src at 01/01/2023 10:39 arrives dsw at 01/01/2023 14:23"));
    }

    /*
    Test Invalid data
     */

    @Test
    void testPrintMeInvalidDateAndTime()
    {
        MainMethodResult result = invokeMain("-print", "name of airline", "1", "src", "1/32/2023", "10:39", "dsw","1/1/2023","14:23");
        assertThat(result.getTextWrittenToStandardError(),containsString("Date 1/32/2023 is invalid, date must be in format mm/dd/yyyy"));

        result = invokeMain("-print", "name of airline", "1", "src", "1/31/23", "10:39", "dsw","1/1/2023","14:23");
        assertThat(result.getTextWrittenToStandardError(),containsString("Date 1/31/23 is invalid, date must be in format mm/dd/yyyy"));

        result = invokeMain("-print", "name of airline", "1", "src", "13/31/2023", "10:39", "dsw","1/1/2023","14:23");
        assertThat(result.getTextWrittenToStandardError(),containsString("Date 13/31/2023 is invalid, date must be in format mm/dd/yyyy"));

        result = invokeMain("-print", "name of airline", "1", "src", "12/31/2023", "25:39", "dsw","1/1/2023","14:23");
        assertThat(result.getTextWrittenToStandardError(),containsString("Time 25:39 is invalid, time must be in format hh:mm (24 hour time)"));

        result = invokeMain("-print", "name of airline", "1", "src", "12/31/2023", "12:99", "dsw","1/1/2023","14:23");
        assertThat(result.getTextWrittenToStandardError(),containsString("Time 12:99 is invalid, time must be in format hh:mm (24 hour time)"));
    }

    @Test
    void testPrintMeInvalidName()
    {
        MainMethodResult result = invokeMain("-print", " ", "1", "src", "1/31/2023", "10:39", "dsw","1/1/2023","14:23");
        assertThat(result.getTextWrittenToStandardError(),containsString("Airline name   is invalid, must not be empty."));
    }

    @Test
    void testPrintMeInvalidFNumber()
    {
        MainMethodResult result = invokeMain("-print", "airline name", "1fc", "src", "1/31/2023", "10:39", "dsw","1/1/2023","14:23");
        assertThat(result.getTextWrittenToStandardError(),containsString("Flight Number 1fc is invalid, must be numeric."));

        result = invokeMain("-print", "airline name", "0", "src", "1/31/2023", "10:39", "dsw","1/1/2023","14:23");
        assertThat(result.getTextWrittenToStandardError(),containsString("Flight Number 0 is invalid, must be greater than zero."));

        result = invokeMain("-print", "airline name", "-1", "src", "1/31/2023", "10:39", "dsw","1/1/2023","14:23");
        assertThat(result.getTextWrittenToStandardError(),containsString("Flight Number -1 is invalid, must be greater than zero."));
    }

    @Test
    void testPrintMeInvalidLocation()
    {
        MainMethodResult result = invokeMain("-print", "airline name", "1", "s1f", "1/31/2023", "10:39", "dsw","1/1/2023","14:23");
        assertThat(result.getTextWrittenToStandardError(),containsString("Location s1f is invalid, format must be three alphabetic letters."));

        result = invokeMain("-print", "airline name", "1", "srcsd", "1/31/2023", "10:39", "dsw","1/1/2023","14:23");
        assertThat(result.getTextWrittenToStandardError(),containsString("Location srcsd is invalid, format must be three alphabetic letters."));

        result = invokeMain("-print", "airline name", "1", "src", "1/31/2023", "10:39", "d","1/1/2023","14:23");
        assertThat(result.getTextWrittenToStandardError(),containsString("Location d is invalid, format must be three alphabetic letters."));
    }


}