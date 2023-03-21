import edu.pdx.cs410J.ParserException;
import org.checkerframework.checker.units.qual.C;

import java.io.*;
import java.util.Arrays;

/**
 * Converter extends the Project 4 class and contains the main method which can be utilized by the
 * user to convert a plain textFile (of type CSV) to XML.
 */
public class Converter extends Project4{
    public final static String [] arguments = {"textFile", "xmlFile"};

    /**
     * main method for Converter parses the command line for file names and directs the conversion
     * of the text file1 one into XML file2. All errors result in the programing terminating and an
     * error message being output to the user.
     * @param args a String array holding the file names to be utlized by the program.
     */
    public static void main(String [] args) {
        File[] files = new File[2];
        Converter test = new Converter();

        if(args.length == 0)
        {
            System.err.println("No arguments provided please see below Command Line Usage: ");
            printConverterUsage();
            return;
        }
        if (args.length < 2) {
            for (int i = args.length; i < 2; ++i) {
                System.err.print("Missing: " + arguments[i]);
            }
            System.err.println("\nPlease see README for further instructions");
            return;
        }
        if (args.length > 2) {
            for (int i = 2; i < args.length; ++i) {
                System.err.print("Extra Argument: " + args[i]);
                if(i!=args.length-1)
                    System.err.print(" ");
            }
            System.err.println("\nPlease see README for further instructions");
            return;
        }
        files[0] = fileValidation(args[0], arguments[0], 1);
        files[1] = fileValidation(args[1], arguments[1], 0);

        if (files[0] == null || files[1] == null) return;
        try {
            test.parseFile(files[0], 0);
            test.dumpFile(files[1], 1);
        } catch (ParserException | IllegalArgumentException | IOException ex) {
            System.err.println("File Error: "+ex.getMessage());
            System.err.println("please see README for further instructions");
        }
    }


    /**
     * printConverterUsage prints out the ConverterUsage stored in the resources folder.
     */
    protected static void printConverterUsage()
    {
        String line;

        try (InputStream readme = CommandLineParser.class.getResourceAsStream("ConverterCommandLineUsage.txt")) {
            if (readme == null)
                throw new IOException("CommandLineUsage not available");
            BufferedReader reader = new BufferedReader(new InputStreamReader(readme));
            while((line=reader.readLine()) != null)
            {
                System.err.println(line);

            }
        }
        catch(IOException ex)
        {
            System.err.println("CommandLineUsage not available");
        }

    }
}
