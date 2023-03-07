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
import java.io.Reader;
import java.util.Date;
import java.util.Objects;


/**
 * The XMLParser class handles parsing provided xml file into an airline as well as error checking
 * the data stored in the provided file. The TextParser class implements the AirlineParser class,
 * thus it implements the parse method, which returns an airline reference.
 * <ul>
 *     <li> file: Handles reading in character data from the file </li>
 * </ul>
 */
public class XMLParser implements AirlineParser<Airline> {

    protected final Reader file;

    public XMLParser(Reader file) {
        this.file = file;
    }

    /**
     * parse Converts the InputStream to the DOM tree and directs the parsing of element data from the
     * DOM tree. Calls the parseAirline method with the root element to begin parsing the airline.
     * Catches all exceptions that can be thrown both by the DOM API and the airline data validation
     * process, utilizes the message within the caught exception to build and throw a more descriptive
     * ParserException.
     * @return a reference to the parsed Airline
     * @throws ParserException thrown if any error is encountered parsing the file.
     */
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

            doc = builder.parse(new InputSource(file));

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

    /**
     * parseAirline Iterates through the children of the root element and directs the parsing based on the name
     * of the child element. Calls the parseFlight to handle parsing each flight child element.
     * @param element the root Element to begin parsing
     * @return  a reference to the parsed airline
     * @throws NullPointerException thrown if a null element is encountered
     * @throws IllegalArgumentException thrown if an invalid element is encountered
     */
    private Airline parseAirline(Element element) throws NullPointerException, IllegalArgumentException{
        Objects.requireNonNull(element);

        Airline airline = null;
        NodeList children = element.getChildNodes();
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
                    break;
                default:
                    break;
            }
        }
        return airline;
    }

    /**
     * parseFlight Iterates through the children of the flight element provided as an argument and
     * directs the parsing based on the name of the child element. If the element is Departure or
     * Arrival, the parseDateAndTime method is called.
     * @param element the Flight element to be parsed
     * @return returns a reference to the parsed Flight
     * @throws IllegalArgumentException thrown if an invalid element is encountered
     * @throws NullPointerException thrown if a null/empty element is encountered.
     */
    private Flight parseFlight(Element element) throws IllegalArgumentException, NullPointerException{
        String number = null, source = null, destination = null;
        Date departure = null, arrival = null;

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
            throw new IllegalArgumentException("Flight "+(number==null?"":number)+": "+ex.getMessage());
        }
    }

    /**
     * parseDateAndTime Iterates through the children of the Date element provided as an argument
     * and directs the parsing based on the name of the child element. If the element is of type
     * date, the parseDate method is called. If the element is of type time, the parseTime method
     * is called.
     * @param element the Date element to be parsed
     * @param type an Int specifying the whether the date being parsed is of type departure of arrival
     * @return a reference to a Date object.
     * @throws IllegalArgumentException thrown if an invalid element is encountered
     * @throws NullPointerException thrown if a null/empty element is encountered.
     */
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

    /**
     * parseTime parses the hour and minute attributes from the time element provided.
     * @param current the time element to be parsed
     * @return returns a string representation of the time
     * @throws IllegalArgumentException thrown if the minute is invalid.
     * @throws NullPointerException thrown if the element is empty or null.
     */
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
            if (min != null && Integer.parseInt(min) < 10 && Integer.parseInt(min)>=0)
                min = "0" + min;
            return hour+":"+min;
        }catch(NumberFormatException ex){
            throw new IllegalArgumentException("Invalid minute value: "+min);
        }
    }

    /**
     * parseDate Parses the day, month, and year attributes from the date
     * element provided.
     * @param current the date element to be parsed
     * @return returns a string representation of the date
     * @throws NullPointerException thrown if the element is null or empty.
     */
    private String parseDate(Element current)  throws NullPointerException{
        String day = null, month = null, year = null;

        Objects.requireNonNull(current, "null date element");

        NamedNodeMap attributes = current.getAttributes();

        for (int i = 0; i < attributes.getLength(); ++i) {
            Node att = attributes.item(i);

            switch (att.getNodeName()) {
                case "day":
                    day = value(att, "day");
                    break;
                case "month":
                    month = value(att, "month");
                    break;
                case "year":
                    year = value(att, "year");
                    break;
                default:
                    break;
            }
        }
        return month + "/" + day + "/" + year;
    }

    /**
     * value Validates that the value of the element is not null.
     * @param current the Node to be validated
     * @param type the type of the Node (utilized for the exception thrown if the Node is
     *             invalid).
     * @return a String representation of the value of the node.
     * @throws NullPointerException Thrown is the node is invalid.
     */
    private String value(Node current, String type) throws NullPointerException{
        Objects.requireNonNull(current, "empty "+type+" element");
        return current.getNodeValue();
    }
}
