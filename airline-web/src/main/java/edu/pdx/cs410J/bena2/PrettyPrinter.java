package edu.pdx.cs410J.bena2;

import edu.pdx.cs410J.AirlineDumper;
import edu.pdx.cs410J.AirportNames;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
            int maxAirportLen = maxAirportLength(airline)+6;
            String divider = new String(new char[87+2*maxAirportLen]).replace('\0','-');
            pw = (writer ==null ? new PrintWriter(stream): new PrintWriter(writer));

            pw.println(name + " flight roster as of " + today(new Date()));
            pw.println();
            pw.flush();
            pw.println(divider);
            pw.printf("|%s|%s|%s|%s|%s|%s|\n",PrettyPrinter.centerString("Flight Number",15),
                    PrettyPrinter.centerString("Source",maxAirportLen),
                    PrettyPrinter.centerString("Departure",25),
                    PrettyPrinter.centerString("Destination",maxAirportLen),
                    PrettyPrinter.centerString("Arrival",25),
                    PrettyPrinter.centerString("Length",15)
            );
            pw.println(divider);
            while (toDump.hasNext()) {
                pw.print(toDump.next().getPrettyDump(maxAirportLen));
                pw.println(divider);
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

    /**
     * today Returns a String representation of the date passed in.
     * @param date the Date to be converted to String.
     * @return the String representation of the Date argument.
     */
    public static String today(Date date) {
        String toReturn;
       Calendar cDate = Calendar.getInstance();
       cDate.setTime(date);

       switch(cDate.get(Calendar.DAY_OF_MONTH)%10){
           case 1: toReturn = new SimpleDateFormat("MMM dd'st' yyyy").format(date);
                   break;
           case 2: toReturn = new SimpleDateFormat("MMM dd'nd' yyyy").format(date);
                   break;
           case 3: toReturn = new SimpleDateFormat("MMM dd'rd' yyyy").format(date);
               break;
           default: toReturn = new SimpleDateFormat("MMM dd'th' yyyy").format(date);
               break;
       }

       return toReturn;
    }

    /**
     * maxAirportLength determines airport with the maximum name length contained within the airlines
     * flight roster and returns the length.
     * @param airline the Airline to be
     * @return the max name length.
     */
    public static int maxAirportLength(Airline airline)
    {
         return AirportNames.getNamesMap().keySet().stream().filter(s->airline.containsAirport(s)).map(s->AirportNames.getName(s)).max((s1,s2)->s1.length()-s2.length()).orElse("").length();
    }

    /**
     * centerString centers the string passed using the with argument provided.
     * @param toCenter the string to be centered.
     * @param width the width the string should be centered in.
     * @return the centered string.
     */
    public static String centerString(String toCenter, int width)
    {
        int padding = width-toCenter.length();
        int start = toCenter.length()+padding/2;
        return String.format("%-"+width+"s",String.format("%"+start+"s",toCenter));
    }
}
