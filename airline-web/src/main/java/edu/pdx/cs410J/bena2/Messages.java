package edu.pdx.cs410J.bena2;

/**
 * Class for formatting messages on the server side.  This is mainly to enable
 * test methods that validate that the server returned expected strings.
 */
public class Messages
{
    public static String missingRequiredParameter( String parameterName )
    {
        return String.format("The required parameter \"%s\" is missing", parameterName);
    }

    public static String allAirlinesDeleted() {
        return "All Airlines have been deleted";
    }

    public static String addedFlight(Airline airline, Flight flight) {
      return String.format("%s %d added to %s", "Flight", flight.getNumber(), airline.getName());
    }
}
