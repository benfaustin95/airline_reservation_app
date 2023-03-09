package edu.pdx.cs410J.bena2;

import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class MessagesTest {

    @Test
    public void testMissingRequiredParameter()
    {
        assertThat(Messages.missingRequiredParameter("test"), equalTo("The required" +
                " parameter \"test\" is missing"));
    }

    @Test
    public void testAllAirlinesDeleted(){
        assertThat(Messages.allAirlinesDeleted(), equalTo("All Airlines have been deleted"));
    }

    @Test
    public void testAddedFlight(){
        assertThat(Messages.addedFlight(AirlineTest.getValidAirline(), FlightTest.getValidFlight()),
                containsString("added to name"));
    }
}
