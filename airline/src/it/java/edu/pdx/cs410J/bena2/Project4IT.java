package edu.pdx.cs410J.bena2;

import edu.pdx.cs410J.InvokeMainTestCase;
import edu.pdx.cs410J.ParserException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.file.Path;
import java.util.Date;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * An integration test for the {@link CommandLineParser} main class.
 */
class Project4IT extends InvokeMainTestCase {

    /**
     * Invokes the main method of {@link CommandLineParser} with the given arguments.
     */
    private MainMethodResult invokeMain(String... args) {
        return invokeMain( Project4.class, args );
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
        assertThat(result.getTextWrittenToStandardError(), containsString(CommandLineParser.missingArguments));
    }

    /**
     * Tests that invoking the main method with no valid command line options instantiates a flight
     */
    @Test
    void testNoCommandLineOptions() {
        MainMethodResult result = invokeMain( "name", "1", "PDX", "1/1/2023","10:39","am", "SEA", "1/1/2023", "10:49","am");
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
        assertThat(result.getTextWrittenToStandardOut(),equalTo(""));

    }

    /**
     * Tests that invoking the main method with too few command line arguments and no -README command
     * results in error
     */
    @Test
    void testOnlyPrintMeAndTooFewArguments() {
        MainMethodResult result = invokeMain("-print","1","PDX","1/1/2023","10:39","am", "SEA","1/1/2023","9:49","pm");
        assertThat(result.getTextWrittenToStandardError(), containsString("Error Command " +
                "Line: Arguments Provided - Airline Name 1 , Flight Number PDX , Source Location 1/1/2023 ," +
                " Departure Date 10:39 , Departure Time am , Departure am/pm marker SEA , Destination " +
                "Location 1/1/2023 , Arrival Date 9:49 , Arrival Time pm \n" +
                "Arguments Missing - Arrival am/pm marker \n" +
                "Please see README for further instructions."));
        assertThat(result.getTextWrittenToStandardOut(),containsString(""));
    }

    /**
     * Tests that invoking the main method with too many command line arguments and no -README command
     * results in error
     */
    @Test
    void testTooManyArguments() {
        MainMethodResult result = invokeMain("-print", "name", "1", "PDX", "1/1/2023", "10:39", "am", "SEA", "1/1/2023", "11:39", "am", "extra argument");
        assertThat(result.getTextWrittenToStandardError(), containsString("Error Command Line: Extra Argument - extra argument"));

        result = invokeMain( "name", "1", "PDX", "1/1/2023", "10:39", "am","SEA", "1/1/2023", "11:39","pm", "extra argument");
        assertThat(result.getTextWrittenToStandardError(), containsString("Error Command Line: Extra Argument - extra argument"));
    }

    /**
     * Tests that invoking the main method with valid number of command line arguments and -print option results in
     * correct standard out
     */
    @Test
    void testOnlyPrintAndValidFlightDataOnCommandLine() {
        MainMethodResult result = invokeMain("-print", "name", "1", "PDX", "1/1/2023", "10:39","pm", "SEA", "1/1/2023", "11:49","pm");
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight 1 departs PDX at 01/01/2023 10:39 PM arrives SEA at 01/01/2023 11:49 PM"));
        assertThat(result.getTextWrittenToStandardError(), containsString(""));

        result = invokeMain( "name", "1", "PDX", "1/1/2023", "10:39","am", "SEA", "1/1/2023", "11:49","pm");
        assertThat(result.getTextWrittenToStandardOut(),equalTo(""));
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
    }

    /*
    README command line tests
     */

    @Test
    void testOnlyREADMEOnCommandLine() {
        MainMethodResult result = invokeMain("-README");
        assertThat(result.getTextWrittenToStandardOut(), containsString("Ben Austin, CS410J-001, 02/08/2023, bena2@pdx.edu"));
    }

    @Test
    void testREADMEAndFlightDataOnCommandLine() {
        MainMethodResult result = invokeMain("-README","name","1","PDX","1/1/2023","10:39","am","SEA","1/1/2023","11:49","pm");
        assertThat(result.getTextWrittenToStandardOut(), containsString("Ben Austin, CS410J-001, 02/08/2023, bena2@pdx.edu"));
    }

    @Test
    void testREADMEAndPrintMeAndFlightDataOnCommandLine() {
        MainMethodResult result = invokeMain("-README","-print","name","1","PDX","1/1/2023","10:39","am","SEA","1/1/2023","9:49", "pm");
        assertThat(result.getTextWrittenToStandardOut(), containsString("Ben Austin, CS410J-001, 02/08/2023, bena2@pdx.edu"));
    }

    @Test
    void testREADMEAndPrintMeOnCommandLine() {
        MainMethodResult result = invokeMain("-README","-print");
        assertThat(result.getTextWrittenToStandardOut(), containsString("Ben Austin, CS410J-001, 02/08/2023, bena2@pdx.edu"));
    }

    @Test
    void testOnlyInvalidREADMEOnCommandLine() {
        MainMethodResult result = invokeMain("- README");
        assertThat(result.getTextWrittenToStandardError(), containsString("Invalid Options: - README"));
        MainMethodResult result1 = invokeMain("README");
        assertThat(result1.getTextWrittenToStandardError(), containsString("Error Command Line: " +
                "Arguments Provided - Airline Name README \nArguments Missing - Flight Number ," +
                " Source Location , Departure Date , Departure Time , Departure am/pm marker , Destination Location , Arrival Date ," +
                " Arrival Time , Arrival am/pm marker "));
        MainMethodResult result2 = invokeMain("readme");
        assertThat(result2.getTextWrittenToStandardError(), containsString("Error Command Line: " +
                "Arguments Provided - Airline Name readme \nArguments Missing - Flight Number ," +
                " Source Location , Departure Date , Departure Time , Departure am/pm marker , Destination Location , Arrival Date ," +
                " Arrival Time , Arrival am/pm marker "));
        MainMethodResult result3 = invokeMain("-readme");
        assertThat(result3.getTextWrittenToStandardError(), containsString("Invalid Options: -readme"));
    }

    @Test
    void testInvalidREADMEOnAndOtherCommandLine() {
        MainMethodResult result = invokeMain("- README", "-print","name","1","PDX","1/1/2023",
                "10:39","SEA","1/1/2023","13:50");
        assertThat(result.getTextWrittenToStandardError(), containsString("Invalid Options: " +
                "- README"));
        MainMethodResult result1 = invokeMain("README","-print");
        assertThat(result1.getTextWrittenToStandardError(), containsString("Error Command Line: " +
                "Arguments Provided - Airline Name README \nArguments Missing - Flight Number ," +
                " Source Location , Departure Date , Departure Time , Departure am/pm marker , Destination Location , Arrival Date ," +
                " Arrival Time , Arrival am/pm marker"));
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
                " Arguments Provided - \nArguments Missing - Airline Name , Flight Number , Source Location ," +
                " Departure Date , Departure Time , Departure am/pm marker , Destination Location , Arrival Date , Arrival Time , Arrival am/pm marker \n" +
                "Please see README for further instructions."));
    }

    @Test
    void testPrintMeOnCommandLineToFewArguments() {
        MainMethodResult result = invokeMain("-print","name","PDX","1/1/2023","10:39","SEA");
        assertThat(result.getTextWrittenToStandardError(), containsString("Error Command Line: " +
                "Arguments Provided - Airline Name name , Flight Number PDX , Source Location 1/1/2023 , " +
                "Departure Date 10:39 , Departure Time SEA \n" +
                "Arguments Missing - Departure am/pm marker , Destination Location , Arrival Date , " +
                "Arrival Time , Arrival am/pm marker \n" +
                "Please see README for further instructions."));
    }

    @Test
    void testPrintMeOnCommandLineToManyArguments() {
        MainMethodResult result = invokeMain("-print","name", "1", "1", "PDX","1/1/2023",
                "10:39","am", "SEA","1/1/2023","4:23", "pm");
        assertThat(result.getTextWrittenToStandardError(), containsString("Error Command" +
                " Line: Extra Argument - pm"));
    }

    @Test
    void testPrintMeOnCommandLineValid() {
        MainMethodResult result = invokeMain("-print","name", "1", "PDX","1/1/2023","10:39","am","SEA","1/1/2023","4:23","pm");
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight 1 departs PDX at 01/01/2023 10:39 AM arrives SEA at 01/01/2023 04:23 PM"));
    }

    /*
    Test Invalid data
     */

    @Test
    void testPrintMeInvalidDateAndTime()
    {
        MainMethodResult result = invokeMain("-print", "name of airline", "1", "PDX", "1/32/2023", "10:39", "am","SEA","1/1/2023","4:23","pm");
        assertThat(result.getTextWrittenToStandardError(),containsString("Departure date 1/32/2023 is invalid, date must be in format mm/dd/yyyy"));

        result = invokeMain("-print", "name of airline", "1", "PDX", "1/31/23", "10:39", "am", "SEA","1/1/2023","14:23","pm");
        assertThat(result.getTextWrittenToStandardError(),containsString("Departure date 1/31/23 is invalid, date must be in format mm/dd/yyyy"));

        result = invokeMain("-print", "name of airline", "1", "PDX", "13/31/2023", "10:39", "am", "SEA","1/1/2023","14:23","pm");
        assertThat(result.getTextWrittenToStandardError(),containsString("Departure date 13/31/2023 is invalid, date must be in format mm/dd/yyyy"));

        result = invokeMain("-print", "name of airline", "1", "PDX", "12/31/2023", "25:39", "am", "SEA","1/1/2023","14:23","pm");
        assertThat(result.getTextWrittenToStandardError(),containsString("Departure time 25:39 is invalid, time must be in format hh:mm"));

        result = invokeMain("-print", "name of airline", "1", "PDX", "12/31/2023", "12:99", "am", "SEA","1/1/2023","14:23","pm");
        assertThat(result.getTextWrittenToStandardError(),containsString("Departure time 12:99 is invalid, time must be in format hh:mm"));

        result = invokeMain("-print", "name of airline", "1", "PDX", "12/31/2023", "12:54", "am", "SEA","1/1/2023","12:99","pm");
        assertThat(result.getTextWrittenToStandardError(),containsString("Arrival time 12:99 is invalid, time must be in format hh:mm"));
    }

    @Test
    void testPrintMeInvalidName()
    {
        MainMethodResult result = invokeMain("-print", " ", "1", "PDX", "1/31/2023", "10:39", "am", "SEA","1/31/2023","4:23", "pm");
        assertThat(result.getTextWrittenToStandardError(),containsString("Name   is invalid, must not be empty."));
    }

    @Test
    void testPrintMeInvalidFNumber()
    {
        MainMethodResult result = invokeMain("-print", "airline name", "1fc", "PDX", "1/31/2023", "10:39", "am","SEA","1/1/2023","4:23","pm");
        assertThat(result.getTextWrittenToStandardError(),containsString("Flight Number 1fc is invalid, must be numeric."));

        result = invokeMain("-print", "airline name", "0", "PDX", "1/31/2023", "10:39", "am","SEA","1/1/2023","4:23","pm");
        assertThat(result.getTextWrittenToStandardError(),containsString("Flight Number 0 is invalid, must be greater than zero."));
    }

    @Test
    void testPrintMeInvalidLocation()
    {
        MainMethodResult result = invokeMain("-print", "airline name", "1", "s1f", "1/31/2023", "10:39","am", "SEA","1/1/2023","4:23", "pm");
        assertThat(result.getTextWrittenToStandardError(),containsString("Location s1f is invalid, format must be three alphabetic letters."));

        result = invokeMain("-print", "airline name", "1", "PDXsd", "1/31/2023", "10:39","am","SEA","1/1/2023","4:23", "pm");
        assertThat(result.getTextWrittenToStandardError(),containsString("Location PDXsd is invalid, format must be three alphabetic letters."));

        result = invokeMain("-print", "airline name", "1", "PDX", "1/31/2023", "10:39", "am","d","1/1/2023","4:23","pm");
        assertThat(result.getTextWrittenToStandardError(),containsString("Location d is invalid, format must be three alphabetic letters."));

        result = invokeMain("-print", "airline name", "1", "PDX", "1/31/2023", "10:39", "am","dsn","1/1/2023","4:23","pm");
        assertThat(result.getTextWrittenToStandardError(),containsString("Location dsn is invalid"));
    }

    @Test
    void testValidEmptyFileFlight(@TempDir Path dir)
    {
        Path testFile = dir.resolve("test");

        MainMethodResult result = invokeMain("-print","-textFile",testFile.toString(), "name","1",
                "PDX","1/1/2023","10:39","am","SEA","1/2/2023","2:39","pm");
        assertThat(result.getTextWrittenToStandardOut(),containsString("Flight 1 departs PDX at 01/01/2023 10:39 AM" +
                " arrives SEA at 01/02/2023 02:39 PM"));

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
            result = invokeMain("-textFile", testFile.toString(), "name",String.valueOf(i), "PDX"
                    , "1/1/2023", "10:39", "am","SEA", "1/2/2023", "2:39","pm");
        }

        assertThat(result.getTextWrittenToStandardError(), equalTo(""));

        File file = testFile.toFile();

        try(FileReader fr = new FileReader(file))
        {
            TextParser parser = new TextParser(fr);
            Airline test = parser.parse();

            assertThat(test.getName(), equalTo("name"));
            assertThat(test.getFlights().size(), equalTo(9));
        }
        catch(ParserException | IOException ex)
        {
           fail(ex.getMessage());
        }
    }


    @Test
    public void testNoFilePathOptionFollowingTextFile()
    {
        MainMethodResult result = invokeMain("-textFile", "-print", "name","1", "PDX"
                    , "1/1/2023", "10:39", "am","SEA", "1/2/2023", "2:39","pm");

        assertThat(result.getTextWrittenToStandardError(), equalTo("Command Line: -textFile option selected but no file path provided" +
                "\n"));

    }
    @Test
    public void testNoFilePathLastOption()
    {
        MainMethodResult result = invokeMain("-print", "-textFile", "name","1", "PDX"
                , "1/1/2023", "10:39","am","SEA", "1/2/2023", "2:39","am");

        assertThat(result.getTextWrittenToStandardError(), equalTo("Error Command Line: Arguments " +
                "Provided - Airline Name 1 , Flight Number PDX , Source Location 1/1/2023 , " +
                "Departure Date 10:39 , Departure Time am , Departure am/pm marker SEA , Destination Location 1/2/2023 " +
                ", Arrival Date 2:39 , Arrival Time am \nArguments Missing - Arrival am/pm marker " +
                "\nPlease see README for further instructions.\n"));
    }


    @Test
    public void testWrongName(@TempDir Path dir)
    {
        Path testFile = dir.resolve("test2");

        invokeMain("-print", "-textFile", testFile.toString(),"name","1", "PDX"
                , "1/1/2023", "10:39", "am", "SEA", "1/2/2023", "2:39","pm");


        MainMethodResult result = invokeMain("-print", "-textFile", testFile.toString(),"nam2","1", "PDX"
                , "1/1/2023", "10:39", "am","SEA", "1/2/2023", "2:39","pm");

        assertThat(result.getTextWrittenToStandardError(), containsString("does not match Airline"));
    }

    @Test
    public void testBadFile(@TempDir Path dir)
    {
        Path testFile = dir.resolve("test2");

        MainMethodResult result = invokeMain("-print", "-textFile", testFile+"/badDirectoryTest","name","1", "PDX"
                , "1/1/2023", "10:39","am", "SEA", "1/2/2023", "2:39","pm");

        assertThat(result.getTextWrittenToStandardError(), containsString("unable to write"));
    }

    @Test
    public void testWrongName2(@TempDir Path dir)
    {
        Path testFile = dir.resolve("test2");

        MainMethodResult result = invokeMain("-print", "-textFile", testFile.toString(),"-textFile","name","1", "PDX"
                , "1/1/2023", "10:39", "am","SEA", "1/2/2023", "2:39","pm");


        result = invokeMain("-print", "-textFile", testFile.toString(),"nam2","1", "PDX"
                , "1/1/2023", "10:39", "am", "SEA", "1/2/2023", "2:39","pm");

        assertThat(result.getTextWrittenToStandardError(), containsString("does not match Airline"));
    }

    @Test
    public void testValidPrettyPrintSTDOUT()
    {
        MainMethodResult result = invokeMain("-print", "-pretty", "-","name","1", "PDX"
                , "1/1/2023", "10:39", "am","SEA", "1/2/2023", "2:39","pm");
        assertThat(result.getTextWrittenToStandardOut(), containsString("\n\n--" +
                "-------------------------------------------------------------" +
                "------------------------------------------------------------\n| Flight Number |  " +
                "    Source      |        Departure        |   Destination    |         Arrival    " +
                "     |    Length     |\n---------------------------------------------------------" +
                "------------------------------------------------------------------\n|       1    " +
                "   |   Portland, OR   |  Jan, 01 2023 10:39 AM  |   Seattle, WA    |  Jan, 02 2023 " +
                "02:39 PM  | 1680 minutes  |\n----------------------------------------------------" +
                "-----------------------------------------------------------------------\nFlight 1" +
                " departs PDX at 01/01/2023 10:39 AM arrives SEA at 01/02/2023 02:39 PM\n"));
    }

    @Test
    public void testPrettyPrintNoFileNoAirline()
    {
        MainMethodResult result = invokeMain("-pretty", "-print");
        assertThat(result.getTextWrittenToStandardError(), equalTo("Command Line: -pretty " +
                "option selected but no file path provided\n"));
    }

    @Test
    public void testPrettyPrintNoAirline()
    {
        MainMethodResult result = invokeMain("-print","-pretty", "file.txt");
        assertThat(result.getTextWrittenToStandardError(), containsString("Error Command Line:" +
                " Arguments Provided - \nArguments Missing "));
    }
    @Test
    public void testPrettyPrintNoFile()
    {
        MainMethodResult result = invokeMain("-pretty", "-print", "name","1","PDX","1/1/2023","10:39",
                "sea", "1/2/2023","2:34");
        assertThat(result.getTextWrittenToStandardError(), equalTo("Command Line: -pretty " +
                "option selected but no file path provided\n"));
    }

    @Test
    public void testSameFileName()
    {
        MainMethodResult result = invokeMain("-pretty","text","-textFile","text","-print", "name","1","PDX","1/1/2023","10:39",
                "sea", "1/2/2023","2:34");
        assertThat(result.getTextWrittenToStandardError(), equalTo("File path for (-textFile or -xmlFile) and -pretty options can not be the same\n"
                +"Please see README for further instruction\n"));
    }
    @Test
    public void testPrettyPrintNoFile2()
    {
        MainMethodResult result = invokeMain("-print", "-pretty","name","1","PDX","1/1/2023","10:39",
                "sea", "1/2/2023","2:34");
        assertThat(result.getTextWrittenToStandardError(), equalTo("Error Command Line: " +
                "Arguments Provided - Airline Name 1 , Flight Number PDX , Source Location 1/1/2023 ," +
                " Departure Date 10:39 , Departure Time sea , Departure am/pm marker 1/2/2023 ," +
                " Destination Location 2:34 \nArguments Missing - Arrival Date , Arrival Time , Arrival" +
                " am/pm marker \nPlease see README for further instructions.\n"));
    }

    @Test
    public void testPrettyPrintNoFileWithTextFile()
    {
        MainMethodResult result = invokeMain("-print", "-pretty","-textFile","name","1","PDX","1/1/2023","10:39",
                "sea", "1/2/2023","2:34");
        assertThat(result.getTextWrittenToStandardError(), equalTo("Command Line: -pretty " +
                "option selected but no file path provided\n"));

    }

    @Test
    public void testPrettyPrintFullTextFile(@TempDir Path dir)
    {
        Path testFile = dir.resolve("test2");
        Path testFile2 = dir.resolve("text3");
        MainMethodResult result = null;

        for(int i =1; i<10; ++i) {
            result = invokeMain("-textFile", testFile.toString(), "name",String.valueOf(i), "PDX"
                    , "1/1/2023", "10:39", "am","SEA", "1/2/2023", "2:39","pm");
        }

        result = invokeMain("-textFile", testFile.toString(), "name",String.valueOf(12), "JFK"
                , "1/1/2023", "10:09", "am","SEA", "1/2/2023", "2:39","pm");

        result = invokeMain("-textFile", testFile.toString(), "name",String.valueOf(12), "JFK"
                , "1/1/2023", "10:29", "am","SEA", "1/2/2023", "2:39","pm");

        result = invokeMain("-textFile", testFile.toString(), "name",String.valueOf(12), "LAX"
                , "1/1/2023", "10:29", "am","SEA", "1/2/2023", "2:39","pm");

        assertThat(result.getTextWrittenToStandardError(), equalTo(""));

        result = invokeMain("-textFile", testFile.toString(), "-pretty",testFile2.toString(),"name",String.valueOf(12), "LAX"
                , "1/1/2023", "12:29", "am","SEA", "1/2/2023", "2:39","pm");

        try(BufferedReader br = new BufferedReader(new FileReader(testFile2.toFile()))) {
           assertThat(br.readLine(), equalTo("name flight roster as of "+PrettyPrinter.today(new Date())));
           br.readLine(); br.readLine(); br.readLine(); br.readLine();
            assertThat(br.readLine(), containsString("|      12       |   New York, NY (Kennedy) " +
                    "  |  Jan, 01 2023 10:09 AM  |        Seattle, WA         |  Jan, 02 2023 02:39 PM  | 1710 minutes  |"));
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testPrettyPrintOverwrite(@TempDir Path dir) {
        Path testFile = dir.resolve("test2");
        MainMethodResult result = null;
        long prev;

        for (int i = 1; i < 10; ++i) {
            result = invokeMain("-textFile", testFile.toString(), "name", String.valueOf(i), "PDX"
                    , "1/1/2023", "10:39", "am", "SEA", "1/2/2023", "2:39", "pm");
        }

        assertThat(result.getTextWrittenToStandardError(), equalTo(""));

        prev = testFile.toFile().length();

        result =invokeMain("-print", "-pretty",testFile.toString(),"name","1","PDX","1/1/2023","10:39","am",
                "sea", "1/2/2023","2:34","pm");

        assertNotEquals(prev, testFile.toFile().length());
    }



    @Test
    public void testFileIsDirectory(@TempDir File dir)
    {
        MainMethodResult result = invokeMain("-print", "-textFile", dir.getPath());
        assertThat(result.getTextWrittenToStandardError(), equalTo("Error Command Line: File Path Provided for option " +
                "-textFile Is Not A Valid File\n"));
    }


    @Test
    public void readBadFile(@TempDir File dir)
    {
        File badOut = new File(dir, "text.txt");

        try(PrintWriter testBadOut = new PrintWriter(new FileWriter(badOut)))
        {
            testBadOut.println("Airline, 1, PDX, 1/1/2023,10:39,src");
            testBadOut.flush();
        } catch (IOException e) {
            fail(e.getMessage());
        }

        MainMethodResult result = invokeMain("-textFile", badOut.getPath(), "name","1","pdx",
                "1/1/2022","10:34","am","sea","1/2/2022","10:32", "pm");
        assertThat(result.getTextWrittenToStandardError(), equalTo("File Error While parsing " +
                "file line 1: Arguments Provided - Airline Name Airline , Flight Number  1 , Source Location" +
                "  PDX , Departure Date  1/1/2023 , Departure Time 10:39 , Departure am/pm marker src \nArguments Missing" +
                " - Destination Location , Arrival Date , Arrival Time , Arrival am/pm marker \n"));
    }

    @Test
    public void xmlFileAndTextFileTogether()
    {
        assertThat(invokeMain("-xmlFile","test","-textFile","test2").getTextWrittenToStandardError(),
                equalTo("Options -textFile and -xmlFile can not be exercised together. " +
                        "\nPlease see README for further instructions\n"));
    }

    @Test
    public void xmlParseValid(@TempDir File dir)
    {
        File file = new File(dir, "test");

        for (int i = 1; i < 9; ++i) {
            MainMethodResult result = invokeMain("-print", "-xmlFile", file.getPath(), "Airline Name", i + "23", "PDX", "3/"+i+"/2023", "10:23", "Am", "SEA", "3/"+(i+1)+"/2023", "12:34", "pm");
            try(FileReader fr = new FileReader(file)) {
                XMLParser parser = new XMLParser(fr);
                assertThat(parser.parse().getFlights().size(), equalTo(i));
            }catch (IOException | ParserException ex) {
                fail(ex.getMessage());
            }
            assertThat(result.getTextWrittenToStandardError(), equalTo(""));
            assertThat(result.getTextWrittenToStandardOut(), equalTo("Flight " + i + "23 departs PDX at 03/0"+i+"/2023 10:23 AM arrives SEA at 03/0"+(i+1)+"/2023 12:34 PM\n"));
        }

        try(BufferedReader br = new BufferedReader(new FileReader(file)))
        {
            String line = br.readLine();
            assertThat(line, equalTo("<?xml version=\"1.0\" encoding=\"us-ascii\" standalone=\"no\"?>"));
            line = br.readLine();
            assertThat(line, equalTo("<!DOCTYPE airline SYSTEM \"http://www.cs.pdx.edu/~whitlock/dtds/airline.dtd\">"));
            line = br.readLine();
            assertThat(line, equalTo("<airline>"));
            line = br.readLine();
            assertThat(line, containsString("<name>Airline Name</name>"));
        } catch (IOException e) {
            fail(e.getMessage());
        }

        MainMethodResult result = invokeMain("-xmlFile", file.getPath(),"-pretty", "-","Airline Name","23", "PDX", "3/31/2023", "10:23", "Am", "SEA", "3/31/2023", "12:34", "pm");
        assertThat(result.getTextWrittenToStandardOut(), containsString("Airline Name flight roster as of Feb 22nd 2023"));
    }

}