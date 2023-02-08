package edu.pdx.cs410J.bena2;

import edu.pdx.cs410J.AirlineDumper;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Iterator;

public class PrettyPrinter implements AirlineDumper<Airline> {

    private final Writer writer;
    private final PrintStream stream;

    public PrettyPrinter(Writer writer) {
        stream = null;
        this.writer = writer;
    }

    public PrettyPrinter(PrintStream stream)
    {
        writer = null;
        this.stream = stream;
    }

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
