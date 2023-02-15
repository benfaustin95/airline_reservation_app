package edu.pdx.cs410J.bena2;

import edu.pdx.cs410J.AirlineParser;
import edu.pdx.cs410J.ParserException;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Objects;

public class XMLParser implements AirlineParser<Airline> {

    protected final InputSource file;

    public XMLParser(InputStream file) {
        this.file = new InputSource(file);
    }

    @Override
    public Airline parse() throws ParserException {
        AirlineXmlHelper helper = new AirlineXmlHelper();
        Document doc = null;

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(true);

            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setErrorHandler(helper);
            builder.setEntityResolver(helper);

            doc = builder.parse(file);

            Element element = doc.getDocumentElement();

            return parseAirline(element);

        } catch(IllegalArgumentException | NullPointerException ex){
            throw new ParserException(ex.getMessage());
        } catch (ParserConfigurationException e) {
            throw new ParserException("error creating Document Builder");
        } catch (IOException e) {
            throw new ParserException("error opening XML file: "+e.getMessage());
        } catch (SAXException e) {
            throw new ParserException("XML file does not conform to DTD: "+e.getMessage());
        }
    }

    private Airline parseAirline(Element element) throws NullPointerException, IllegalArgumentException{
        Airline airline = null;
        NodeList children = element.getChildNodes();

        Objects.requireNonNull(element);

        for (int i = 0; i < children.getLength(); ++i) {
            if (!(children.item(i) instanceof Element))
                continue;

            Element current = (Element) children.item(i);

            switch (current.getNodeName()) {
                case "name":
                    Node value = current.getFirstChild();
                    if(value == null) throw new NullPointerException("Empty airline name element");
                    airline = new Airline(value.getNodeValue());
                    break;
                case "flight":
                    if(airline != null) airline.addFlight(parseFlight(current));
                    else throw new IllegalArgumentException("No airline name provided");
                    break;
                default:
                    break;
            }
        }
        return airline;
    }

    private Flight parseFlight(Element element) throws IllegalArgumentException, NullPointerException{
        String number = null, source = null, destination = null;
        Date departure = null, arrival = null;
        Node value = null;

        Objects.requireNonNull(element, "null flight element");

        NodeList children = element.getChildNodes();

        try {
            for (int i = 0; i < children.getLength(); ++i) {
                if (!(children.item(i) instanceof Element))
                    continue;

                Element current = (Element) children.item(i);
                switch (current.getNodeName()) {
                    case "number":
                        number = value(current.getFirstChild(), "number");
                        break;
                    case "src":
                        source = value(current.getFirstChild(), "src");
                        break;
                    case "depart":
                        departure = parseDateAndTime(current, 0);
                        break;
                    case "dest":
                        destination = value(current.getFirstChild(), "dest");
                        break;
                    case "arrive":
                        arrival = parseDateAndTime(current, 1);
                        break;
                    default:
                        break;
                }
            }
            return new Flight(number, source, destination, departure, arrival);
        }
        catch (IllegalArgumentException | NullPointerException ex)
        {
            throw new IllegalArgumentException("Flight "+number+": "+ex.getMessage());
        }
    }

    private Date parseDateAndTime(Element element, int type) throws IllegalArgumentException, NullPointerException {
        String date = null, time = null;

        Objects.requireNonNull(element, "null "+(type == 0 ? "departure ":"arrival")+ " element");

        NodeList children = element.getChildNodes();

        for (int i = 0; i < children.getLength(); ++i) {
            if (!(children.item(i) instanceof Element))
                continue;

            Element current = (Element) children.item(i);

            switch (current.getNodeName()) {
                case "date":
                    date = parseDate(current);
                    break;
                case "time":
                    time = parseTime(current);
                    break;
                default:
                    break;
            }
        }
        return Flight.validateDateAndTime(date, time, type);
    }

    private String parseTime(Element current) throws IllegalArgumentException, NullPointerException{
        String hour = null, min= null;

        Objects.requireNonNull(current, "null time element");

        NamedNodeMap attributes = current.getAttributes();

        for (int i = 0; i < attributes.getLength(); ++i) {
            Node att = attributes.item(i);

            switch (att.getNodeName()) {
                case "hour":
                    hour = att.getNodeValue();
                    break;
                case "minute":
                    min= att.getNodeValue();
                    break;
                default:
                    break;
            }
        }
        try {
            if (min != null && Integer.parseInt(min) < 10)
                min = "0" + min;
            return hour+":"+min;
        }catch(NumberFormatException ex){
            throw new IllegalArgumentException("Invalid minute value: "+min);
        }
    }

    private String parseDate(Element current) {
        String day = null, month = null, year = null;

        Objects.requireNonNull(current, "null date element");

        NamedNodeMap attributes = current.getAttributes();

        for (int i = 0; i < attributes.getLength(); ++i) {
            Node att = attributes.item(i);

            switch (att.getNodeName()) {
                case "day":
                    day = att.getNodeValue();
                    break;
                case "month":
                    month = att.getNodeValue();
                    break;
                case "year":
                    year = att.getNodeValue();
                    break;
                default:
                    break;
            }
        }
        return month + "/" + day + "/" + year;
    }

    private String value(Node current, String type) throws NullPointerException{
        Objects.requireNonNull(current, "empty "+type+" element");
        return current.getNodeValue();
    }
}
