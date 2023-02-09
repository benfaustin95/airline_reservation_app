package edu.pdx.cs410J.bena2;

import edu.pdx.cs410J.AirlineDumper;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Iterator;


/**
 * The  PrettyPrinter class handles outputting the provided airline in an easy-to-read and
 * informative format. The PrettyPrinter class implements the AirlineDumper class,
 * thus it implements the dump method, which is of void return type and takes an airline reference.
 * <ul>
 *   <li> Writer: Handles writing out character data to the desired writer or stream </li>
 * </ul>
 */
public class PrettyPrinter implements AirlineDumper<Airline> {

    private final Writer writer;
    private final PrintStream stream;

    /**
     * PrettyPrinter acts as the primary constructor for the PrettyPrinter class. The Writer field is
     * set to the value passed in and stream is set to null.
     * @param writer a Writer holding the outputstream used to write to the file.
     */
    public PrettyPrinter(Writer writer) {
        stream = null;
        this.writer = writer;
    }

    /**
     * PrettyPrinter acts as the secondary constructor for the PrettyPrinter class. The Writer field is
     * set to null and stream is set to the value passed in.
     * @param stream a stream holding the outputstream used to write out.
     */
    public PrettyPrinter(PrintStream stream)
    {
        writer = null;
        this.stream = stream;
    }

    /**
     * dump Dumps the airline and flight data to the stream referenced by the writer
     * in an easy-to-read textual format. The method serves as the class's implementation of the
     * AirlineDumper class.
     * @param airline the Airline object to be written out.
     * @throws IllegalArgumentException Thrown if the airline is null.
     */
    @Override
    public void dump(Airline airline) throws IllegalArgumentException {

        PrintWriter pw = null;

        if (airline == null)
            throw new IllegalArgumentException("No Airline is available to pretty print");

        if(writer == null && stream == null)
            return;

        try {
            Iterator<Flight> toDump = airline.getFlights().iterator();
            String name = airline.getName();
            pw = (writer ==null ? new PrintWriter(stream): new PrintWriter(writer));

            pw.println("Airline: " + name);
            pw.println("Current Flight Roster:");
            pw.flush();

            while (toDump.hasNext()) {
                pw.println(toDump.next().getPrettyDump());
                pw.flush();
            }
        }
        finally {
            if(writer != null)
                pw.close();
            else
                pw.flush();;
        }
    }
}
