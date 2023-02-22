package edu.pdx.cs410J.bena2;

import edu.pdx.cs410J.InvokeMainTestCase;
import edu.pdx.cs410J.ParserException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.w3c.dom.Text;

import java.io.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.fail;

public class ConverterTest extends InvokeMainTestCase{

    private InvokeMainTestCase.MainMethodResult invokeMain(String... args) {
        return invokeMain(Converter.class, args );
    }
    @Test
    public void testValidConvert(@TempDir File dir){
        Converter test = new Converter();
        String [] files = new String[2];

        File file1 = new File(dir, "text");
        File file2 = new File(dir, "xml");

        try (Writer wr = new FileWriter(file1)) {
            TextDumper dumper = new TextDumper(wr);
            dumper.dump(AirlineTest.getValidAirline());
        } catch (IOException e) {
            fail(e.getMessage());
        }
        Converter.main(new String[] {file1.getPath(), file2.getPath()});

        try(FileReader fr1 = new FileReader(file1);
            FileReader fr2 = new FileReader(file2)){

            TextParser text = new TextParser(fr1);
            XMLParser xml = new XMLParser(fr2);

            Airline aText = text.parse();
            Airline aXML = xml.parse();

            assertThat(aText, equalTo(aXML));
            assertThat(aText.getFlights().size(), equalTo(aXML.getFlights().size()));
        }catch (IOException | ParserException ex){
          fail(ex.getMessage());
        }
    }

    @Test
    public void testArguments() {
        assertThat(invokeMain("test").getTextWrittenToStandardError(), equalTo("Missing: xmlFile" +
                "\nPlease see README for further instructions\n"));
        assertThat(invokeMain("Test","test","test").getTextWrittenToStandardError(), equalTo("Extra Argument: test" +
                "\nPlease see README for further instructions\n"));
        assertThat(invokeMain("Test","test","test","test","test").getTextWrittenToStandardError(),
                equalTo("Extra Argument: test Extra Argument: test Extra Argument: test\nPlease " +
                        "see README for further instructions\n"));
        assertThat(invokeMain("-README").getTextWrittenToStandardOut(),containsString("bena2@pdx.edu"));
        assertThat(invokeMain().getTextWrittenToStandardError(),containsString("<args>"));
    }

    @Test
    public void testBadTextFile(){
        assertThat(invokeMain("DoesNotExist","ShouldntSee").getTextWrittenToStandardError(),containsString("Error Command Line: " +
                "File Path Provided for option textFile does not exist, therefor can not be converted to XML"));
    }

    @Test
    public void testBadXMLFile(@TempDir File dir){
        File file = new File(dir, "test");
        try(PrintWriter pw = new PrintWriter(file)) {
            pw.println("non-empty textfile");
        } catch (FileNotFoundException e) {
            fail(e.getMessage());
        }
        assertThat(invokeMain(file.getPath(),dir.getPath()).getTextWrittenToStandardError(),
                containsString("Error Command Line: File Path Provided for option xmlFile " +
                        "Is Not A Valid File"));
    }
    @Test
    public void testEmptyFile(@TempDir File dir){
        File test = new File(dir, "EmptyFile");
        try(PrintWriter fw = new PrintWriter(test)) {
            fw.print("");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertThat(invokeMain(test.getPath(),"ShouldntSee").getTextWrittenToStandardError(),
                containsString("File Error: File is empty, file provided must have contents" +
                        " or not yet exist\nplease see README for further instructions"));
    }

}
