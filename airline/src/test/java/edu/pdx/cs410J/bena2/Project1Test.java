package edu.pdx.cs410J.bena2;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * A unit test for code in the <code>Project1</code> class.  This is different
 * from <code>Project1IT</code> which is an integration test (and can capture data)
 * written to {@link System#out} and the like.
 */
class Project1Test {

  @Test
  void readmeCanBeReadAsResource() throws IOException {
    try (
            InputStream readme = Project1.class.getResourceAsStream("README.txt")
    ) {
      assertThat(readme, not(nullValue()));
      BufferedReader reader = new BufferedReader(new InputStreamReader(readme));
      String line = reader.readLine();
      assertThat(line, containsString("This is a README file!"));
    }
  }

  @Test
  void validateDateAndTimeGoodInput() {
    Project1 test = new Project1();
    String date = "1/1/2023", time = "10:39";
    Date rdate = null, tdate = null;
    try {
      rdate = test.validateDateAndTime(date, time);
      tdate = new SimpleDateFormat("MM/dd/yyyy HH:mm").parse(date + " " + time);
    } catch (ParseException ex) {
      System.out.println("Bad Date");
      System.exit(1);
    } catch (IllegalArgumentException ex) {
      fail(ex);
    }
    assertThat(0, equalTo(tdate.compareTo(rdate)));
  }

  @Test
  void ValidateDateAndTimeBadInput() {
    Project1 test = new Project1();

    assertThrows(IllegalArgumentException.class,
            () -> test.validateDateAndTime("12-23-2022", "10:39 AM"));

    assertThrows(IllegalArgumentException.class,
            () -> test.validateDateAndTime("12/23/22", "10:39"));

    assertThrows(IllegalArgumentException.class,
            () -> test.validateDateAndTime("12/23/2022", "25:59"));
  }

  @Test
  void validateLocationGoodInput() {
    Project1 test = new Project1();
    String valid_location = "PDX", test_location = null;
    try {
      test_location = test.validateLocation(valid_location);
    } catch (IllegalArgumentException ex) {
      fail(ex);
    }

    assertThat(valid_location, equalTo(test_location));
  }

  @Test
  void validateLocationBadInput() {
    Project1 test = new Project1();

    assertThrows(IllegalArgumentException.class, () -> test.validateLocation("CS"));
    assertThrows(IllegalArgumentException.class, () -> test.validateLocation("1S"));
    assertThrows(IllegalArgumentException.class, () -> test.validateLocation("sstre"));
    assertThrows(IllegalArgumentException.class, () -> test.validateLocation("S1V"));
  }

  @Test
  void validateNumberGoodInput() {
    Project1 test = new Project1();
    String valid_int = "1232";
    int test_int = 0;

    try {
      test_int = test.validateNumber(valid_int);
    } catch (IllegalArgumentException ex) {
      fail(ex);
    }
    assertThat(Integer.parseInt(valid_int), is(equalTo(test_int)));
  }

  @Test
  void validateNameGoodInput() {
    Project1 test = new Project1();
    String valid_name = "valid_name", test_name = null;
    try {
      test_name = test.validateName(valid_name);
    } catch (IllegalArgumentException ex) {
      fail(ex);
    }
    assertThat(valid_name, is(equalTo(test_name)));
  }


  @Test
  void validateNameBadInput() {
    Project1 test = new Project1();

    assertThrows(IllegalArgumentException.class, () -> test.validateName(""));
  }
}
