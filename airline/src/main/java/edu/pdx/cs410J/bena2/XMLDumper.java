package edu.pdx.cs410J.bena2;

import edu.pdx.cs410J.AirlineDumper;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.Calendar;
import java.util.Date;

public class XMLDumper implements AirlineDumper<Airline> {

    protected Writer file;

    public XMLDumper(Writer file){
        this.file = file;
    }

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
