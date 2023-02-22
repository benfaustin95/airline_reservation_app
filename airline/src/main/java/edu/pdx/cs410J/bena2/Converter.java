package edu.pdx.cs410J.bena2;

import edu.pdx.cs410J.ParserException;
import org.checkerframework.checker.units.qual.C;

import java.io.*;
import java.util.Arrays;

public class Converter extends Project4{
    public final static String [] arguments = {"textFile", "xmlFile"};
    public static void main(String [] args) {
        File[] files = new File[2];
        Converter test = new Converter();

        if(args.length == 0)
        {
            printConverterUsage();
            return;
        }

        if(Arrays.asList(args).contains("-README")) {
            printREADME(0);
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
     * printUsage prints out the Usage stored in the resources folder.
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
