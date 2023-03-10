package edu.pdx.cs410J.bena2;

/**
 * Class for formatting messages on the server side.  This is mainly to enable
 * test methods that validate that the server returned expected strings.
 */
public class Messages
{
    /**
     * missingRequiredParameter builds the error message sent when the given parameter
     * is missing.
     * @param parameterName a String reference to the missing parameter type.
     * @return a reference to the String holding the error message.
     */
    public static String missingRequiredParameter( String parameterName )
    {
        if(parameterName.equals("src"))
            return "If a destination airport is provided a source airport is required";
        if(parameterName.equals("dest"))
            return "If a source airport is provided a destination airport is required";
        return String.format("The required parameter \"%s\" is missing", parameterName);
    }

    /**
     * allAirlinesDeleted returns the message to be output when the airport is erased.
     * @return a reference to the String holding the error message.
     */
    public static String allAirlinesDeleted() {
        return "All Airlines have been deleted";
    }

    /**
     * addedFlight returns the message to be output when a flight is successfully added to an airline.
     * @param airline the Airline the flight was added to
     * @param flight  the Flight that was added.
     * @return a reference to the String holding the error message.
     */
    public static String addedFlight(Airline airline, Flight flight) {
      return String.format("%s %d added to %s", "Flight", flight.getNumber(), airline.getName());
    }
}
