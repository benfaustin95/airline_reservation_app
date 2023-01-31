package edu.pdx.cs410J.bena2;

import edu.pdx.cs410J.InvokeMainTestCase;
import edu.pdx.cs410J.ParserException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * An integration test for the {@link Project1} main class.
 */
class Project2IT extends InvokeMainTestCase {

    @TempDir
    static Path sharedDir;
    /**
     * Invokes the main method of {@link Project1} with the given arguments.
     */
    private MainMethodResult invokeMain(String... args) {
        return invokeMain( Project2.class, args );
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
        assertThat(result.getTextWrittenToStandardError(), containsString("Error Command Line:" +
                " Arguments Provided - Airline Name 1 ,Flight Number src ,Source Location 1/1/2023 ," +
                "Departure Date 10:39 ,Departure Time dsw ,Destination Location 1/1/2023 ,Arrival Date 19:49 \n" +
                " Arguments Missing - Arrival Time \n" +
                "Please see README for further instructions"));
        assertThat(result.getTextWrittenToStandardOut(),containsString(""));
    }

    /**
     * Tests that invoking the main method with too many command line arguments and no -README command
     * results in error
     */
    @Test
    void testTooManyArguments() {
        MainMethodResult result = invokeMain("-print", "name", "1", "src", "1/1/2023", "10:39", "dsw", "1/1/2023", "11:39", "extra argument");
        assertThat(result.getTextWrittenToStandardError(), containsString("Error Command Line: Extra Argument - extra argument"));

        result = invokeMain( "name", "1", "src", "1/1/2023", "10:39", "dsw", "1/1/2023", "11:39", "extra argument");
        assertThat(result.getTextWrittenToStandardError(), containsString("Error Command Line: Extra Argument - extra argument"));
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
        assertThat(result.getTextWrittenToStandardOut(), containsString("Ben Austin, CS410J-001, 1/24/2023, bena2@pdx.edu"));
    }

    @Test
    void testREADMEAndFlightDataOnCommandLine() {
        MainMethodResult result = invokeMain("-README","name","1","src","1/1/2023","10:39","dsn","1/1/2023","19:49");
        assertThat(result.getTextWrittenToStandardOut(), containsString("Ben Austin, CS410J-001, 1/24/2023, bena2@pdx.edu"));
    }

    @Test
    void testREADMEAndPrintMeAndFlightDataOnCommandLine() {
        MainMethodResult result = invokeMain("-README","-print","name","1","src","1/1/2023","10:39","dsn","1/1/2023","19:49");
        assertThat(result.getTextWrittenToStandardOut(), containsString("Ben Austin, CS410J-001, 1/24/2023, bena2@pdx.edu"));
    }

    @Test
    void testREADMEAndPrintMeOnCommandLine() {
        MainMethodResult result = invokeMain("-README","-print");
        assertThat(result.getTextWrittenToStandardOut(), containsString("Ben Austin, CS410J-001, 1/24/2023, bena2@pdx.edu"));
    }

    @Test
    void testOnlyInvalidREADMEOnCommandLine() {
        MainMethodResult result = invokeMain("- README");
        assertThat(result.getTextWrittenToStandardError(), containsString("Invalid Options: - README"));
        MainMethodResult result1 = invokeMain("README");
        assertThat(result1.getTextWrittenToStandardError(), containsString("Error Command Line: " +
                "Arguments Provided - Airline Name README \n Arguments Missing - Flight Number ," +
                "Source Location ,Departure Date ,Departure Time ,Destination Location ,Arrival Date ," +
                "Arrival Time "));
        MainMethodResult result2 = invokeMain("readme");
        assertThat(result2.getTextWrittenToStandardError(), containsString("Error Command Line: " +
                "Arguments Provided - Airline Name readme \n Arguments Missing - Flight Number ," +
                "Source Location ,Departure Date ,Departure Time ,Destination Location ,Arrival Date ," +
                "Arrival Time "));
        MainMethodResult result3 = invokeMain("-readme");
        assertThat(result3.getTextWrittenToStandardError(), containsString("Invalid Options: -readme"));
    }

    @Test
    void testInvalidREADMEOnAndOtherCommandLine() {
        MainMethodResult result = invokeMain("- README", "-print","name","1","src","1/1/2023",
                "10:39","dsw","1/1/2023","13:50");
        assertThat(result.getTextWrittenToStandardError(), containsString("Invalid Options: " +
                "- README"));
        MainMethodResult result1 = invokeMain("README","-print");
        assertThat(result1.getTextWrittenToStandardError(), containsString("Error Command Line: " +
                "Arguments Provided - Airline Name README \n Arguments Missing - Flight Number ," +
                "Source Location ,Departure Date ,Departure Time ,Destination Location ,Arrival Date ," +
                "Arrival Time "));
        MainMethodResult result2 = invokeMain("README", "-prin", "test","test");
        assertThat(result2.getTextWrittenToStandardError(), containsString("Invalid Options: " +
                "-prin"));
        MainMethodResult result3 = invokeMain("-readme","-textfile");
        assertThat(result3.getTextWrittenToStandardError(), containsString("Invalid Options: " +
                "-readme -textfile"));
    }

    /*
      Print me test
     */
    @Test
    void testOnlyPrintMeOnCommandLine() {
        MainMethodResult result = invokeMain("-print");
        assertThat(result.getTextWrittenToStandardError(), containsString("Error Command Line:" +
                " Arguments Provided - \n Arguments Missing - Airline Name ,Flight Number ,Source Location ," +
                "Departure Date ,Departure Time ,Destination Location ,Arrival Date ,Arrival Time \n" +
                "Please see README for further instructions."));
    }

    @Test
    void testPrintMeOnCommandLineToFewArguments() {
        MainMethodResult result = invokeMain("-print","name","src","1/1/2023","10:39","dsw");
        assertThat(result.getTextWrittenToStandardError(), containsString("Error Command Line: " +
                "Arguments Provided - Airline Name name ,Flight Number src ,Source Location 1/1/2023 ," +
                "Departure Date 10:39 ,Departure Time dsw \n" +
                " Arguments Missing - Destination Location ,Arrival Date ,Arrival Time \n" +
                "Please see README for further instructions."));
    }

    @Test
    void testPrintMeOnCommandLineToManyArguments() {
        MainMethodResult result = invokeMain("-print","name", "1", "1", "src","1/1/2023",
                "10:39","dsw","1/1/2023","14:23");
        assertThat(result.getTextWrittenToStandardError(), containsString("Error Command" +
                " Line: Extra Argument - 14:23"));
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
        assertThat(result.getTextWrittenToStandardError(),containsString("Departure date 1/32/2023 is invalid, date must be in format mm/dd/yyyy"));

        result = invokeMain("-print", "name of airline", "1", "src", "1/31/23", "10:39", "dsw","1/1/2023","14:23");
        assertThat(result.getTextWrittenToStandardError(),containsString("Departure date 1/31/23 is invalid, date must be in format mm/dd/yyyy"));

        result = invokeMain("-print", "name of airline", "1", "src", "13/31/2023", "10:39", "dsw","1/1/2023","14:23");
        assertThat(result.getTextWrittenToStandardError(),containsString("Departure date 13/31/2023 is invalid, date must be in format mm/dd/yyyy"));

        result = invokeMain("-print", "name of airline", "1", "src", "12/31/2023", "25:39", "dsw","1/1/2023","14:23");
        assertThat(result.getTextWrittenToStandardError(),containsString("Departure time 25:39 is invalid, time must be in format hh:mm (24 hour time)"));

        result = invokeMain("-print", "name of airline", "1", "src", "12/31/2023", "12:99", "dsw","1/1/2023","14:23");
        assertThat(result.getTextWrittenToStandardError(),containsString("Departure time 12:99 is invalid, time must be in format hh:mm (24 hour time)"));

        result = invokeMain("-print", "name of airline", "1", "src", "12/31/2023", "12:54", "dsw","1/1/2023","12:99");
        assertThat(result.getTextWrittenToStandardError(),containsString("Arrival time 12:99 is invalid, time must be in format hh:mm (24 hour time)"));
    }

    @Test
    void testPrintMeInvalidName()
    {
        MainMethodResult result = invokeMain("-print", " ", "1", "src", "1/31/2023", "10:39", "dsw","1/31/2023","14:23");
        assertThat(result.getTextWrittenToStandardError(),containsString("Name   is invalid, must not be empty."));
    }

    @Test
    void testPrintMeInvalidFNumber()
    {
        MainMethodResult result = invokeMain("-print", "airline name", "1fc", "src", "1/31/2023", "10:39", "dsw","1/1/2023","14:23");
        assertThat(result.getTextWrittenToStandardError(),containsString("Flight Number 1fc is invalid, must be numeric."));

        result = invokeMain("-print", "airline name", "0", "src", "1/31/2023", "10:39", "dsw","1/1/2023","14:23");
        assertThat(result.getTextWrittenToStandardError(),containsString("Flight Number 0 is invalid, must be greater than zero."));
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

    @Test
    void testValidEmptyFileFlight(@TempDir Path dir)
    {
        Path testFile = dir.resolve("test");

        MainMethodResult result = invokeMain("-print","-textFile",testFile.toString(), "name","1",
                "src","1/1/2023","10:39","dsn","1/2/2023","2:39");
        assertThat(result.getTextWrittenToStandardOut(),containsString("Flight 1 departs src at 01/01/2023 10:39" +
                " arrives dsn at 01/02/2023 02:39"));

        File file = testFile.toFile();

        try(FileReader fr = new FileReader(file)) {
            TextParser parser = new TextParser(fr);
            Airline test = parser.parse();

            assertThat(test.getFlights().size(), equalTo(1));
            assertThat(test.getName(), equalTo("name"));

        } catch (FileNotFoundException | ParserException e) {
            fail(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testNonEmptyValidFIle(@TempDir Path dir)
    {
        Path testFile = dir.resolve("test2");
        MainMethodResult result = null;

        for(int i =1; i<10; ++i) {
            result = invokeMain("-textFile", testFile.toString(), "name",String.valueOf(i), "src"
                    , "1/1/2023", "10:39", "dsn", "1/2/2023", "2:39");
        }

        assertThat(result.getTextWrittenToStandardError(), equalTo(""));

        File file = testFile.toFile();

        try(FileReader fr = new FileReader(file))
        {
            TextParser parser = new TextParser(fr);
            Airline test = parser.parse();

            assertThat(test.getName(), equalTo("name"));
            assertThat(test.getFlights().size(), equalTo(9));
            assertThat(test.getLastFlight().getNumber(), equalTo(9));
        }
        catch(ParserException | IOException ex)
        {
           fail(ex.getMessage());
        }
    }


    @Test
    public void testNoFilePathOptionFollowingTextFile(@TempDir Path dir)
    {
        Path testFile = dir.resolve("test2");
        MainMethodResult result = invokeMain("-textFile", "-print", "name","1", "src"
                    , "1/1/2023", "10:39", "dsn", "1/2/2023", "2:39");

        assertThat(result.getTextWrittenToStandardError(), equalTo("Command Line: -textFile option selected but no file path provided" +
                "\nPlease see README for further instructions\n"));
    }
    @Test
    public void testNoFilePathLastOption(@TempDir Path dir)
    {
        Path testFile = dir.resolve("test2");
        MainMethodResult result = invokeMain("-print", "-textFile", "name","1", "src"
                , "1/1/2023", "10:39", "dsn", "1/2/2023", "2:39");

        assertThat(result.getTextWrittenToStandardError(), equalTo("Error Command Line: " +
                "Arguments Provided - Airline Name 1 ,Flight Number src ,Source Location 1/1/2023 ," +
                "Departure Date 10:39 ,Departure Time dsn ,Destination Location 1/2/2023 ,Arrival Date " +
                "2:39 \n Arguments Missing - Arrival Time \nPlease see README for further instructions.\n"));
    }

}