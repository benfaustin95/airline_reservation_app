Ben Austin, CS410J-001, 03/22/2023, bena2@pdx.edu
Program 6: Airline Android App

Overview:
Program Six's android app allows the user to create and print multiple airlines as well as associated flight(s) from the application. Furthermore, the user can print all flights from a specific airline, as well as flights that have a user specified source and destination. Airline contents contents are returned in Pretty Printer form upon request. Please note multiple airlines are supported concurrently.

Please see the below execution instructions.

Provided Interface:

Options:

    HELP:
       -Print README (current document)

    Add Airline
        -Creates an empty airline with the
        provided name.

    Add Flight
        -Creates a Flight with user supplied
        data. Adds to existing airline or
        creates new airline. Please see
        Arguments section for Flight data
        constraints.
        -An error message will be provided
        and the flight will not be created
        if any field provided is invalid.

    Display Flights
        -Displays Flights via three paths:
           1: No Airline, Source, Destination:
           All flights displayed
           2: Airline provided
           All flights from Airline displayed
           3: Airline, Source, Destination:
           All flights from Airline with
           matching source and destination.
           Please note Source and Destination
           must be asserted together.
        -An error message will be provided
        and nothing will be displayed if the
        airline is empty/doesn't exist or no
        flights with matching source and
        destination exist.

    Arguments (An error message will be provided
     if any arguments are invalid)

        - airline:
            -the name of the airline
            multi-word names must be delimited by
            double quotes example: "Airline Name".

        - flightNumber:
            the flight number
            must be integer greater than zero.
            example: 139

        - Source:
            departure airport code
            must be three alphabetic characters
            must be a valid Airport Code as specified
            by AirportNames
            example: PDX

        - Departure:
            departure date and time
            must be in following format
            mm/dd/yyyy hh:mm aa
            leading zeros may be omitted for
            month, day, and hour
            time must be in 12-hour format
            example: 1/1/2023 9:49 am
            example: 01/01/2023 09:49 am

        - Destination:
            destination airport code
            must be three alphabetic characters
            must be a valid Airport Code as
            specified by AirportNames
            example: SEA

        - Arrival:
            arrival date and time
            must be in following
            format mm/dd/yyyy hh:mm aa
            leading zeros may be omitted
            for month, day, and hour
            time must be in 12-hour format
            must occur after departure
            example: 2/13/2023 1:09 pm
            example: 02/13/2023 01:09 pm


