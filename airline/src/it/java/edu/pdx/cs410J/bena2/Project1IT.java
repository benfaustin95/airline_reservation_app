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
        assertThat(result.getTextWrittenToStandardError(), containsString("Missing command line arguments"));
    }

    /**
     * Tests that invoking the main method with no valid command line arguments issues an error
     */
    @Test
    void testNoCommandLineOptions() {
        MainMethodResult result = invokeMain("-TextFile", "name", "1", "src", "1/1/2023","10:39", "dsn", "1/1/2023", "19:49");
        assertThat(result.getTextWrittenToStandardError(), containsString("Missing command line options, please see README for instructions"));
    }

    /**
     * Tests that invoking the main method with too few command line arguments and no -README command
     * results in error
     */
    @Test
    void testOnlyPrintMeAndTooFewArguments() {
        MainMethodResult result = invokeMain("-printme","name","1","src","1/1/2023","10:39","dsw","1/1/2023");
        assertThat(result.getTextWrittenToStandardError(), containsString("Number of Command line arguments = 8 is incorrect"));
    }

    /**
     * Tests that invoking the main method with too many command line arguments and no -README command
     * results in error
     */
    @Test
    void testOnlyPrintMeAndTooManyArguments() {
        MainMethodResult result = invokeMain("-printme", "name", "1", "src", "1/1/2023", "10:39", "dsw", "1/1/2023", "11:39", "extra argument");
        assertThat(result.getTextWrittenToStandardError(), containsString("Number of Command line arguments = 10 is incorrect"));
    }

    /**
     * Tests that invoking the main method with valid number of command line arguments and -printme option results in
     * correct standard out
     */
    @Test
    void testOnlyPrintMeAndValidFlightDataOnCommandLine() {
        MainMethodResult result = invokeMain("-printme", "name", "1", "src", "1/1/2023", "10:39", "dsw", "1/1/2023", "19:49");
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight 1 departs src at 01/01/2023 10:39 arrives dsw at 01/01/2023 19:49"));

    }

    /*
    README command line tests
     */


    @Test
    void testOnlyREADMEOnCommandLine() {
        MainMethodResult result = invokeMain("-README");
        assertThat(result.getTextWrittenToStandardOut(), containsString("WILL PRINT README"));
    }

    @Test
    void testREADMEAndFlightDataOnCommandLine() {
        MainMethodResult result = invokeMain("-README","name","1","src","1/1/2023","10:39","dsn","1/1/2023","19:49");
        assertThat(result.getTextWrittenToStandardOut(), containsString("WILL PRINT README"));
    }

    @Test
    void testREADMEAndPrintMeAndFlightDataOnCommandLine() {
        MainMethodResult result = invokeMain("-README","-printme","name","1","src","1/1/2023","10:39","dsn","1/1/2023","19:49");
        assertThat(result.getTextWrittenToStandardOut(), containsString("WILL PRINT README"));
    }

    @Test
    void testOnlyPrintMeOnCommandLine() {
        MainMethodResult result = invokeMain("-printme");
        assertThat(result.getTextWrittenToStandardError(), containsString("Number of Command line arguments = 1 is incorrect"));
    }

}