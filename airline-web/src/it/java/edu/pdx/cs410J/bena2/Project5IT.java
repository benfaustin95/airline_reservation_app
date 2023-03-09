package edu.pdx.cs410J.bena2;

import edu.pdx.cs410J.AirportNames;
import edu.pdx.cs410J.InvokeMainTestCase;
import edu.pdx.cs410J.UncaughtExceptionInMain;
import edu.pdx.cs410J.web.HttpRequestHelper.RestException;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.UnknownHostException;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.MethodOrderer.MethodName;

/**
 * An integration test for that invokes its main method with
 * various arguments
 */
@TestMethodOrder(MethodName.class)
class Project5IT extends InvokeMainTestCase {
    private static final String HOSTNAME = "localhost";
    private static final String PORT = System.getProperty("http.port", "8080");

    private MainMethodResult invokeMain(String... args) {
        return invokeMain(Project5.class, args);
    }
    @Test
    void test0RemoveAllMappings(){
        try {
            AirlineRestClient client = new AirlineRestClient(HOSTNAME, Integer.parseInt(PORT));
            client.removeAllAirlines();

        }catch (Exception ex){
            fail(ex.getMessage());
        }
    }

    @Test
    void test1NoCommandLineArguments() {
        MainMethodResult result = invokeMain(Project5.class);
        assertThat(result.getTextWrittenToStandardError(), containsString(CommandLineParser.missingArguments));
    }

    @Test
    void test2NoCommandLineOptions() {
        MainMethodResult result = invokeMain(Project5.class, "Test", "1", "PDX", "1/1/2023", "10:39", "AM", "SeA", "1/2/2023", "1:21","am");
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    }

    @Test
    void test3OnlyPrintMeValid() {
        MainMethodResult result = invokeMain("-print","Airline Name",
                "1","PDX","1/1/2023","10:39","am", "SEA","1/1/2023","9:49","pm");
        assertThat(result.getTextWrittenToStandardOut(), CoreMatchers.equalTo("Flight 1 departs " +
                "PDX at 01/01/2023 10:39 AM arrives SEA at 01/01/2023 09:49 PM\n"));
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.equalTo(""));
    }
    @Test
    void test4OnlyPrintMeAndTooFewArguments() {
        MainMethodResult result = invokeMain("-print","1","PDX","1/1/2023","10:39","am", "SEA","1/1/2023","9:49","pm");
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.containsString("Error Command " +
                "Line: Arguments Provided - Airline Name 1 , Flight Number PDX , Source Location 1/1/2023 ," +
                " Departure Date 10:39 , Departure Time am , Departure am/pm marker SEA , Destination " +
                "Location 1/1/2023 , Arrival Date 9:49 , Arrival Time pm \n" +
                "Arguments Missing - Arrival am/pm marker \n" +
                "Please see README for further instructions."));
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    }

    @Test
    void test5TooManyArguments() {
        MainMethodResult result = invokeMain("-print", "name", "1", "PDX", "1/1/2023", "10:39", "am", "SEA", "1/1/2023", "11:39", "am", "extra argument");
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.containsString("Error Command Line: Extra Argument - extra argument"));

        result = invokeMain( "name", "1", "PDX", "1/1/2023", "10:39", "am","SEA", "1/1/2023", "11:39","pm", "extra argument");
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.containsString("Error Command Line: Extra Argument - extra argument"));

        result = invokeMain( "name", "1", "PDX", "1/1/2023", "10:39", "am","SEA", "1/1/2023", "11:39","pm", "extra argument","extra argument");
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.containsString("Error Command Line: Extra Argument - extra argument," +
                "Extra Argument - extra argument"));
    }

    @Test
    void test6OnlyPrintAndValidFlightDataOnCommandLine() {
        MainMethodResult result = invokeMain("-print", "name", "1", "PDX", "1/1/2023", "10:39","pm", "SEA", "1/1/2023", "11:49","pm");
        assertThat(result.getTextWrittenToStandardOut(), CoreMatchers.containsString("Flight 1 departs PDX at 01/01/2023 10:39 PM arrives SEA at 01/01/2023 11:49 PM"));
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.equalTo(""));

        result = invokeMain( "name", "1", "PDX", "1/1/2023", "10:39","am", "SEA", "1/1/2023", "11:49","pm");
        assertThat(result.getTextWrittenToStandardOut(), CoreMatchers.equalTo(""));
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.equalTo(""));
    }


    @Test
    void test7OnlyREADMEOnCommandLine() {
        MainMethodResult result = invokeMain("-README");
        assertThat(result.getTextWrittenToStandardOut(), CoreMatchers.containsString("Ben Austin, CS410J-001, 03/08/2023, bena2@pdx.edu"));
    }

    @Test
    void test8READMEAndFlightDataOnCommandLine() {
        MainMethodResult result = invokeMain("-README","name","1","PDX","1/1/2023","10:39","am","SEA","1/1/2023","11:49","pm");
        assertThat(result.getTextWrittenToStandardOut(), CoreMatchers.containsString("Ben Austin, CS410J-001, 03/08/2023, bena2@pdx.edu"));

    }

    @Test
    void test9READMEAndPrintMeAndFlightDataOnCommandLine() {
        MainMethodResult result = invokeMain("-README","-print","name","1","PDX","1/1/2023","10:39","am","SEA","1/1/2023","9:49", "pm");
        assertThat(result.getTextWrittenToStandardOut(), CoreMatchers.containsString("Ben Austin, CS410J-001, 03/08/2023, bena2@pdx.edu"));
    }

    @Test
    void test10READMEAndPrintMeOnCommandLine() {
        MainMethodResult result = invokeMain("-README","-print");
        assertThat(result.getTextWrittenToStandardOut(), CoreMatchers.containsString("Ben Austin, CS410J-001, 03/08/2023, bena2@pdx.edu"));
    }

    @Test
    void test11OnlyInvalidREADMEOnCommandLine() {
        MainMethodResult result = invokeMain("- README");
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.containsString("Invalid Options: - README"));
        MainMethodResult result1 = invokeMain("README");
        assertThat(result1.getTextWrittenToStandardError(), CoreMatchers.containsString("Error Command Line: " +
                "Arguments Provided - Airline Name README \nArguments Missing - Flight Number ," +
                " Source Location , Departure Date , Departure Time , Departure am/pm marker , Destination Location , Arrival Date ," +
                " Arrival Time , Arrival am/pm marker "));
        MainMethodResult result2 = invokeMain("readme");
        assertThat(result2.getTextWrittenToStandardError(), CoreMatchers.containsString("Error Command Line: " +
                "Arguments Provided - Airline Name readme \nArguments Missing - Flight Number ," +
                " Source Location , Departure Date , Departure Time , Departure am/pm marker , Destination Location , Arrival Date ," +
                " Arrival Time , Arrival am/pm marker "));
        MainMethodResult result3 = invokeMain("-readme");
        assertThat(result3.getTextWrittenToStandardError(), CoreMatchers.containsString("Invalid Options: -readme"));
    }

    @Test
    void test12InvalidREADMEOnAndOtherCommandLine() {
        MainMethodResult result = invokeMain("- README", "-print","name","1","PDX","1/1/2023",
                "10:39","SEA","1/1/2023","13:50");
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.containsString("Invalid Options: " +
                "- README"));
        MainMethodResult result1 = invokeMain("README","-print");
        assertThat(result1.getTextWrittenToStandardError(), CoreMatchers.containsString("Error Command Line: " +
                "Arguments Provided - Airline Name README \nArguments Missing - Flight Number ," +
                " Source Location , Departure Date , Departure Time , Departure am/pm marker , Destination Location , Arrival Date ," +
                " Arrival Time , Arrival am/pm marker"));
        MainMethodResult result2 = invokeMain("README", "-prin", "test","test");
        assertThat(result2.getTextWrittenToStandardError(), CoreMatchers.containsString("Invalid Options: " +
                "-prin"));
        MainMethodResult result3 = invokeMain("-readme","-textFile");
        assertThat(result3.getTextWrittenToStandardError(), CoreMatchers.containsString("Invalid Options: " +
                "-readme -textFile"));
    }
    @Test
    void test13OnlyPrintMeOnCommandLine() {
        MainMethodResult result = invokeMain("-print");
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.containsString("Error Command Line:" +
                " Arguments Provided - \nArguments Missing - Airline Name , Flight Number , Source Location ," +
                " Departure Date , Departure Time , Departure am/pm marker , Destination Location , Arrival Date , Arrival Time , Arrival am/pm marker \n" +
                "Please see README for further instructions."));
    }

    @Test
    void test14PrintMeOnCommandLineToFewArguments() {
        MainMethodResult result = invokeMain("-print","name","PDX","1/1/2023","10:39","SEA");
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.containsString("Error Command Line: " +
                "Arguments Provided - Airline Name name , Flight Number PDX , Source Location 1/1/2023 , " +
                "Departure Date 10:39 , Departure Time SEA \n" +
                "Arguments Missing - Departure am/pm marker , Destination Location , Arrival Date , " +
                "Arrival Time , Arrival am/pm marker \n" +
                "Please see README for further instructions."));
    }

    @Test
    void test15PrintMeOnCommandLineToManyArguments() {
        MainMethodResult result = invokeMain("-print","name", "1", "1", "PDX","1/1/2023",
                "10:39","am", "SEA","1/1/2023","4:23", "pm");
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.containsString("Error Command" +
                " Line: Extra Argument - pm"));
    }

    @Test
    void test16PrintMeOnCommandLineValid() {
        MainMethodResult result = invokeMain("-print","name", "1", "PDX","1/1/2023","10:39","am","SEA","1/1/2023","4:23","pm");
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.equalTo(""));
        assertThat(result.getTextWrittenToStandardOut(), CoreMatchers.containsString("Flight 1 departs PDX at 01/01/2023 10:39 AM arrives SEA at 01/01/2023 04:23 PM"));
    }

    /*
    Test Invalid data
     */

    @Test
    void test17PrintMeInvalidDateAndTime()
    {
        MainMethodResult result = invokeMain("-print", "name of airline", "1", "PDX", "1/32/2023", "10:39", "am","SEA","1/1/2023","4:23","pm");
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.containsString("Departure date 1/32/2023 is invalid, date must be in format mm/dd/yyyy"));

        result = invokeMain("-print", "name of airline", "1", "PDX", "1/31/23", "10:39", "am", "SEA","1/1/2023","14:23","pm");
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.containsString("Departure date 1/31/23 is invalid, date must be in format mm/dd/yyyy"));

        result = invokeMain("-print", "name of airline", "1", "PDX", "13/31/2023", "10:39", "am", "SEA","1/1/2023","14:23","pm");
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.containsString("Departure date 13/31/2023 is invalid, date must be in format mm/dd/yyyy"));

        result = invokeMain("-print", "name of airline", "1", "PDX", "12/31/2023", "25:39", "am", "SEA","1/1/2023","14:23","pm");
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.containsString("Departure time 25:39 is invalid, time must be in format hh:mm"));

        result = invokeMain("-print", "name of airline", "1", "PDX", "12/31/2023", "12:99", "am", "SEA","1/1/2023","14:23","pm");
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.containsString("Departure time 12:99 is invalid, time must be in format hh:mm"));

        result = invokeMain("-print", "name of airline", "1", "PDX", "12/31/2023", "12:54", "am", "SEA","1/1/2023","12:99","pm");
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.containsString("Arrival time 12:99 is invalid, time must be in format hh:mm"));
    }

    @Test
    void test18PrintMeInvalidName()
    {
        MainMethodResult result = invokeMain("-print", " ", "1", "PDX", "1/31/2023", "10:39", "am", "SEA","1/31/2023","4:23", "pm");
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.containsString("Name   is invalid, must not be empty."));
    }

    @Test
    void test19PrintMeInvalidFNumber()
    {
        MainMethodResult result = invokeMain("-print", "airline name", "1fc", "PDX", "1/31/2023", "10:39", "am","SEA","1/1/2023","4:23","pm");
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.containsString("Flight Number 1fc is invalid, must be numeric."));

        result = invokeMain("-print", "airline name", "0", "PDX", "1/31/2023", "10:39", "am","SEA","1/1/2023","4:23","pm");
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.containsString("Flight Number 0 is invalid, must be greater than zero."));
    }

    @Test
    void test20PrintMeInvalidLocation()
    {
        MainMethodResult result = invokeMain("-print", "airline name", "1", "s1f", "1/31/2023", "10:39","am", "SEA","1/1/2023","4:23", "pm");
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.containsString("Location s1f is invalid, format must be three alphabetic letters."));

        result = invokeMain("-print", "airline name", "1", "PDXsd", "1/31/2023", "10:39","am","SEA","1/1/2023","4:23", "pm");
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.containsString("Location PDXsd is invalid, format must be three alphabetic letters."));

        result = invokeMain("-print", "airline name", "1", "PDX", "1/31/2023", "10:39", "am","d","1/1/2023","4:23","pm");
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.containsString("Location d is invalid, format must be three alphabetic letters."));

        result = invokeMain("-print", "airline name", "1", "PDX", "1/31/2023", "10:39", "am","dsn","1/1/2023","4:23","pm");
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.containsString("Location dsn is invalid"));
    }

    @Test
    void test20HostPortNoValue(){
        MainMethodResult result = invokeMain("-host","-port");
        assertThat(result.getTextWrittenToStandardError(), equalTo("Command Line Error: host can not be specified without a port and vice versa\n"));

        result = invokeMain("-host","-port","-print", "airline name", "1", "s1f", "1/31/2023", "10:39","am", "SEA","1/1/2023","4:23", "pm");
        assertThat(result.getTextWrittenToStandardError(), equalTo("Command Line Error: host can not be specified without a port and vice versa\n"));

        result = invokeMain("-host","host", "-port");
        assertThat(result.getTextWrittenToStandardError(), equalTo("Command Line Error: host can not be specified without a port and vice versa\n"));

        result = invokeMain("-host","host","-port","-print", "airline name", "1", "s1f", "1/31/2023", "10:39","am", "SEA","1/1/2023","4:23", "pm");
        assertThat(result.getTextWrittenToStandardError(), equalTo("Command Line Error: host can not be specified without a port and vice versa\n"));

        result = invokeMain("-host", "-port", "host");
        assertThat(result.getTextWrittenToStandardError(), equalTo("Command Line Error: host can not be specified without a port and vice versa\n"));

        result = invokeMain("-host", "-port","port","-print", "airline name", "1", "s1f", "1/31/2023", "10:39","am", "SEA","1/1/2023","4:23", "pm");
        assertThat(result.getTextWrittenToStandardError(), equalTo("Command Line Error: host can not be specified without a port and vice versa\n"));

        result = invokeMain("-host");
        assertThat(result.getTextWrittenToStandardError(), equalTo("Command Line Error: host can not be specified without a port and vice versa\n"));

        result = invokeMain("-port","-print", "airline name", "1", "s1f", "1/31/2023", "10:39","am", "SEA","1/1/2023","4:23", "pm");
        assertThat(result.getTextWrittenToStandardError(), equalTo("Command Line Error: host can not be specified without a port and vice versa\n"));

        result = invokeMain("-host","host");
        assertThat(result.getTextWrittenToStandardError(), equalTo("Command Line Error: host can not be specified without a port and vice versa\n"));

        result = invokeMain("-host","host","-print", "airline name", "1", "s1f", "1/31/2023", "10:39","am", "SEA","1/1/2023","4:23", "pm");
        assertThat(result.getTextWrittenToStandardError(), equalTo("Command Line Error: host can not be specified without a port and vice versa\n"));

        result = invokeMain("-port", "host");
        assertThat(result.getTextWrittenToStandardError(), equalTo("Command Line Error: host can not be specified without a port and vice versa\n"));

        result = invokeMain("-port","port","-print", "airline name", "1", "s1f", "1/31/2023", "10:39","am", "SEA","1/1/2023","4:23", "pm");
        assertThat(result.getTextWrittenToStandardError(), equalTo("Command Line Error: host can not be specified without a port and vice versa\n"));
    }
    @Test
    void test21InvalidPort() {
        MainMethodResult result = invokeMain("-host", "host", "-port", "abs");
        assertThat(result.getTextWrittenToStandardError(), equalTo("Command Line Error: abs is not a valid integer\n"));
    }

    @Test
    void test22InvalidSearch(){
        MainMethodResult result = invokeMain("-search","-print", "airline name", "1", "s1f", "1/31/2023", "10:39","am", "SEA","1/1/2023","4:23", "pm");
        assertThat(result.getTextWrittenToStandardError(), equalTo("Command Line Error: -print and -search can not be asserted together\n"));

        result = invokeMain("-host", "host", "-port", "8080","-search");
        assertThat(result.getTextWrittenToStandardError(), equalTo("Error Command Line: No -search arguments provided for search option\nPlease see README for further instructions.\n"));

        result = invokeMain("-host", "host", "-port", "8080","-search","one","two");
        assertThat(result.getTextWrittenToStandardError(), containsString("Error Command Line: -search argument missing: Destination\n"));

        result = invokeMain("-host", "host", "-port", "8080","-search","one","two","three","four");
        assertThat(result.getTextWrittenToStandardError(), containsString("Error Command Line: Extra Argument - four\n"));

        result = invokeMain("-search","one","two","three","four");
        assertThat(result.getTextWrittenToStandardError(), containsString("Error Command Line: -search must be asserted with -host and -port options\n" +
                "Please see README for further instructions"));
    }

    @Test
    void test23NoAirportGet(){
        MainMethodResult result = invokeMain("-host", HOSTNAME, "-port", PORT, "-search", "no Airline");
        assertThat(result.getTextWrittenToStandardError(), equalTo("Error Server: Airline no Airline does not exist\n"));

         result = invokeMain("-host", HOSTNAME, "-port", PORT, "-search", "no Airline","PDX", "SEA");
        assertThat(result.getTextWrittenToStandardError(), equalTo("Error Server: Airline no Airline does not exist\n"));
    }
    @Test
    void test24InvalidHostAndPort(){
        MainMethodResult result = invokeMain("-host", "testing", "-port", PORT, "-search", "no Airline");
        assertThat(result.getTextWrittenToStandardError(), equalTo("Error Server: unknown host testing\n"));

        result = invokeMain("-host", HOSTNAME, "-port", "1111", "-search", "no Airline");
        assertThat(result.getTextWrittenToStandardError(), equalTo("Error Server: Connection refused invalid port\n"));

        result = invokeMain("-host", HOSTNAME, "-port", "1111", "-search", "no Airline","PDX", "SEA");
        assertThat(result.getTextWrittenToStandardError(), equalTo("Error Server: Connection refused invalid port\n"));

        result = invokeMain("-host", "testing", "-port", PORT, "-search", "no Airline","PDX", "SEA");
        assertThat(result.getTextWrittenToStandardError(), equalTo("Error Server: unknown host testing\n"));

        result = invokeMain("-host","testing","-port",PORT, "Airline One", "1", "PDX", "1/1/2023", "10:39","pm", "SEA", "1/1/2023", "11:49","pm");
        assertThat(result.getTextWrittenToStandardError(), equalTo("Error Server: unknown host testing\n"));

        result = invokeMain("-host",HOSTNAME,"-port","1111", "Airline One", "1", "PDX", "1/1/2023", "10:39","pm", "SEA", "1/1/2023", "11:49","pm");
        assertThat(result.getTextWrittenToStandardError(), equalTo("Error Server: Connection refused invalid port\n"));
    }
    @Test
    void test25addValidAirline(){
        test0RemoveAllMappings();
        MainMethodResult result = invokeMain("-host",HOSTNAME,"-port",PORT, "Airline One", "1", "PDX", "1/1/2023", "10:39","pm", "SEA", "1/1/2023", "11:49","pm");
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.equalTo(""));

        result = invokeMain("-host", HOSTNAME, "-port", PORT,"-search", "Airline One");
        assertThat(result.getTextWrittenToStandardOut(),containsString("Airline One flight roster"));
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));

        result = invokeMain("-host",HOSTNAME,"-port",PORT, "Airline Two", "1", "PDX", "1/1/2023", "10:39","pm", "SEA", "1/1/2023", "11:49","pm");
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.equalTo(""));

        result = invokeMain("-host", HOSTNAME, "-port", PORT,"-search", "Airline Two");
        assertThat(result.getTextWrittenToStandardOut(),containsString("Airline Two flight roster"));
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
    }

    @Test
    void test26addFlight(){
        MainMethodResult result = null;
        String[] airports = {"SEA","JFK","PDX","AMA"};
        for(int i=1; i<21; ++i)
        {
            if(i%2==0) {
                result = invokeMain("-host", HOSTNAME, "-port", PORT, "Airline Two", String.valueOf(i+1), airports[0], "1/"+(21-i)+"/2023", "10:39", "pm", airports[1], "1/"+(21-i)+"/2023", "11:49", "pm");
                assertThat(result.getTextWrittenToStandardError(), CoreMatchers.equalTo(""));
            }else{
                result = invokeMain("-host", HOSTNAME, "-port", PORT, "Airline Two", String.valueOf(i+1), airports[2], "1/"+(21-i)+"/2023", "10:39", "pm", airports[3], "1/"+(21-i)+"/2023", "11:49", "pm");
                assertThat(result.getTextWrittenToStandardError(), CoreMatchers.equalTo(""));
            }
        }
        result = invokeMain("-host", HOSTNAME, "-port", PORT,"-search", "Airline Two");
        assertThat(result.getTextWrittenToStandardOut(),containsString("|       6       |      " +
                "  Portland, OR        |  Jan, 16 2023 10:39 PM  |        Amarillo, TX        | " +
                " Jan, 16 2023 11:49 PM  |  70 minutes   |"));
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
    }

    @Test
    void test27addInvalidFlight(){
        MainMethodResult result = invokeMain("-host",HOSTNAME,"-port",PORT, "Airline One", "abs", "PDX", "1/1/2023", "10:39","pm", "SEA", "1/1/2023", "11:49","pm");
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.equalTo("Error Command Line: Flight Number abs is invalid, must be numeric.\nPlease see README for further instructions.\n"));
        result = invokeMain("-host",HOSTNAME,"-port",PORT, "Airline One", "1", "SRC", "1/1/2023", "10:39","pm", "SEA", "1/1/2023", "11:49","pm");
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.containsString("Error Command Line:"));
        result = invokeMain("-host",HOSTNAME,"-port",PORT, "Airline One", "1", "PDX", "1/1/223", "10:39","pm", "SEA", "1/1/2023", "11:49","pm");
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.containsString("Error Command Line:"));
        result = invokeMain("-host",HOSTNAME,"-port",PORT, "Airline One", "1", "PDX", "1/1/2023", "23:39","pm", "SEA", "1/1/2023", "11:49","pm");
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.containsString("Error Command Line:"));
        result = invokeMain("-host",HOSTNAME,"-port",PORT, "Airline One", "1", "PDX", "1/1/2023", "02:39","xm", "SEA", "1/1/2023", "11:49","pm");
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.containsString("Error Command Line:"));
    }

    @Test
    void test28addInvalidAirport(){
        MainMethodResult result = invokeMain("-host",HOSTNAME,"-port",PORT, " ", "1", "PDX", "1/1/2023", "10:39","pm", "SEA", "1/1/2023", "11:49","pm");
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.containsString("Error Command Line: "));
    }

    @Test
    void test29SearchValidAirline(){
        MainMethodResult result = invokeMain("-host", HOSTNAME, "-port", PORT,"-search", "Airline Two");
        assertThat(result.getTextWrittenToStandardOut(),containsString("|       6       |        Portland, OR        |  Jan, 16 2023 10:39 PM  |        Amarillo, TX        |  Jan, 16 2023 11:49 PM  |  70 minutes   |"));
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
    }

    @Test
    void test30SearchValidAirlineValidSourceAndDest(){
        MainMethodResult result = invokeMain("-host", HOSTNAME, "-port", PORT,"-search", "Airline Two", "pdx","ama");
        assertThat(result.getTextWrittenToStandardOut(),containsString("|       6       |   Portland, OR   |  Jan, 16 2023 10:39 PM  |   Amarillo, TX   |  Jan, 16 2023 11:49 PM  |  70 minutes   |"));
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));

    }

    @Test
    void test31SearchEmptyAirlineReturned(){
        MainMethodResult result = invokeMain("-host", HOSTNAME, "-port", PORT,"-search", "Airline Two", "sea","ama");
        assertThat(result.getTextWrittenToStandardOut(),equalTo(""));
        assertThat(result.getTextWrittenToStandardError(), equalTo("Error Command Line: " +
                "Airline Two contains no direct flights between sea(Seattle, WA) and ama(Amarillo, TX)" +
                "\nPlease see README for further instructions.\n"));
    }

    @Test
    void test32SearchInvalidSource(){
        MainMethodResult result = invokeMain("-host", HOSTNAME, "-port", PORT,"-search", "Airline Two", "src","ama");
        assertThat(result.getTextWrittenToStandardOut(),containsString(""));
        assertThat(result.getTextWrittenToStandardError(), equalTo("Error Command Line: Source Location src is invalid, must be a known airport.\nPlease see README for further instructions.\n"));

    }

    @Test
    void test32InvalidHostPortRemoveAll(){

        AirlineRestClient client2 = new AirlineRestClient("testing", Integer.parseInt(PORT));
        assertThrows(IOException.class, ()->client2.removeAllAirlines());

        AirlineRestClient client3 = new AirlineRestClient(HOSTNAME, 111);
        assertThrows(IOException.class, ()->client3.removeAllAirlines());

    }


}