package edu.pdx.cs410J.bena2;

import edu.pdx.cs410J.AirlineDumper;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.Calendar;
import java.util.Date;

/**
 The XMLDumper class handles outputting the provided airline in XML format as well
 * as error checking output. The XMLDumper class implements the AirlineDumper interface,
 * thus it implements the dump method, which is of void return type and takes an airline reference.
 * <ul>
 *     <li> file: Handles writing out character data to the desired writer </li>
 * </ul>
 */
public class XMLDumper implements AirlineDumper<Airline> {

    protected Writer file;

    /**
     * XML Dumper acts as the default constructor for the XMLDumper class.
     * @param file the Writer reference to be instantiated
     */
    public XMLDumper(Writer file){
        this.file = file;
    }

    /**
     * dump Creates the documents root airline element, appends the airline’s name to the root, calls
     * the createFlightNode method for each flight and appends the result to the root, and outputs
     * the DOM document to the writer the class was instantiated with.
     * @param airline a reference to the airline to be dumped.
     * @throws IOException thrown if an error is encountered creating and/or transforming the doc.
     * @throws NullPointerException thrown if the airline reference is null.
     */
    @Override
    public void dump(Airline airline) throws IOException, NullPointerException {
        Document doc = null;

        if(airline == null) throw new NullPointerException("No Airline available to print");
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            DOMImplementation dom = builder.getDOMImplementation();
            DocumentType docType = dom.createDocumentType("airline",
                    AirlineXmlHelper.PUBLIC_ID, AirlineXmlHelper.SYSTEM_ID);
            doc = dom.createDocument(null, "airline", docType);

            Element root = doc.getDocumentElement();

            Element aName = doc.createElement("name");
            root.appendChild(aName);

            aName.appendChild(doc.createTextNode(airline.getName()));

            for(Flight flight: airline.getFlights()) {
                root.appendChild(createFlightNode(doc, flight));
            }

            Source src = new DOMSource(doc);
            Result res = new StreamResult(file);

            TransformerFactory xfactory = TransformerFactory.newInstance();
            Transformer xform = xfactory.newTransformer();
            xform.setOutputProperty(OutputKeys.INDENT, "yes");
            xform.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, AirlineXmlHelper.SYSTEM_ID);
            xform.setOutputProperty(OutputKeys.ENCODING,"us-ascii");
            xform.transform(src,res);


        } catch (ParserConfigurationException e) {
            throw new IOException("Parser Configuration Error: "+e.getMessage());
        } catch (DOMException ex) {
            throw new IOException("DOMImplementation Exception: "+ ex.getMessage());
        } catch (TransformerConfigurationException e) {
            throw new IOException("Transformer Configuration Exception: "+e.getMessage());
        } catch (TransformerException e) {
            throw new IOException("Transformer Exception: "+e.getMessage());
        }
    }

    /**
     * createFlightNode Creates a flight element, appends each field element to the flight element, and returns
     * a reference to the complete flight element.
     * @param doc the Document the new flight element should be appended to.
     * @param flight the Flight to be converted to xml
     * @return returns a Node to be appended to the root element.
     */
    private Node createFlightNode(Document doc, Flight flight) {

        Element toReturn = doc.createElement("flight");

        Element number = doc.createElement("number");
        number.appendChild(doc.createTextNode(String.valueOf(flight.getNumber())));
        toReturn.appendChild(number);

        Element src = doc.createElement("src");
        src.appendChild(doc.createTextNode(String.valueOf(flight.getSource())));
        toReturn.appendChild(src);

        Element depart = createDateNode("depart",doc, flight.getDeparture());
        toReturn.appendChild(depart);

        Element dest = doc.createElement("dest");
        dest.appendChild(doc.createTextNode(flight.getDestination()));
        toReturn.appendChild(dest);

        Element arrive = createDateNode("arrive",doc, flight.getArrival());
        toReturn.appendChild(arrive);

        return toReturn;
    }

    /**
     * createDateNode Creates the Departure/Arrival element containing the date and time children.
     * Sets the attributes of and appends both the date and time elements to the departure/arrival
     * element. Returns a reference to said Departure/Arrival element.
     * @param type a String referencing the type of date item (departure/arrival)
     * @param doc a Document used to create new elements.
     * @param arrival a Date referencing the date and time to be converted.
     * @returns the element to be appended to the parent.
     */
    private Element createDateNode(String type, Document doc, Date arrival) {

        Element toReturn = doc.createElement(type);
        Calendar cal = Calendar.getInstance();
        cal.setTime(arrival);

        Element date = doc.createElement("date");
        date.setAttribute("day", String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
        date.setAttribute("month", String.valueOf(1+cal.get(Calendar.MONTH)));
        date.setAttribute("year", String.valueOf(cal.get(Calendar.YEAR)));
        toReturn.appendChild(date);

        Element time = doc.createElement("time");
        time.setAttribute("hour", String.valueOf(cal.get(Calendar.HOUR_OF_DAY)));
        time.setAttribute("minute", String.valueOf(cal.get(Calendar.MINUTE)));
        toReturn.appendChild(time);

        return toReturn;
    }

}
