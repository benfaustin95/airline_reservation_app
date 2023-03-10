#################################################################################
#              Ben Austin, CS410J-001, 03/08/2023, bena2@pdx.edu                #
#                Program 5: Storing Airlines as XML                             #
#################################################################################

Overview:
    Program Five airline-client allows the user to create and print multiple
    airlines as well as associated flight(s) from the command line. In addition,
    the program allows the user to read/write airline contents via HTTP requests.
    Furthermore, the user can print all flights from a specific airline, as well
    as flights that have a user specified source and destination. Airline contents
    is returned in XML form when request is via URL, contents are returned in Pretty
    Printer form when the request is via the command line. Please note, multiple
    airlines are supported concurrently.

    Please see the below execution instructions.

##################################################################################
#                            Provided Interface                                  #
##################################################################################
Please enter the following to execute the program.

    Command Line Execution: java -jar target/airline-2023.0.0.jar [options] <arguments>

        Options (all options are optional and can be provided in any order)
            -README                 -Print README
                                    -Please note if provided all other instructions will
                                     be ignored

            -print                  -Print most recently added flight
                                    -"-search" and "-print" can not be asserted
                                     in conjunction. j

            -host hostname          -Host computer on which the server runs.
                                    -Please note it is an error to specify a
                                    host without a port.

            -port port              -Port on which the server is listening
                                    -Please note it is an error to specify a
                                     port without a host.

            -search airline src dest -Search for flights
                                     -src and dest are optional, when asserted
                                      only flights originating from the src and
                                      terminating at the dest will be returned.
                                     -Please note src and dest must be asserted
                                      together, and the same format outlined bellow
                                      is required.
                                     -Please note if -search is asserted no airline
                                      arguments other than name, src, and destination
                                      should be provided.

    URL Execution: Supported URL 1:http://host:port/airline/flights?airline=name
                       - GET returns all flights in the airline formatted via XML
                       - POST creates a new flight from the HTTP request parameters
                         airline, flightNumber, src, depart, dest, and arrive.
                         If the airline does not exist, a new one should be created.

                   Supported URL 2: http://host:port/airline/flights?airline=name
                                    &src=airport&dest=airport
                       -GET returns all of given airline’s flights
                        (in XML format) that originate at the src
                        airport and terminate at the dest airport.

    Arguments (program will terminate in error if all arguments are not present
               and in correct format)

        - airline:       the name of the airline
                         multi-word names must be delimited by
                         double quotes example: "Airline Name".

        - flightNumber:  the flight number
                         must be integer greater than zero.
                         example: 139

        - Source:        departure airport code
                         must be three alphabetic characters
                         must be a valid Airport Code as specified by AirportNames
                         example: PDX

        - Departure:     departure date and time
                         must be in following format mm/dd/yyyy hh:mm aa
                         leading zeros may be omitted for month, day, and hour
                         time must be in 12-hour format
                         example: 1/1/2023 9:49 am or 01/01/2023 09:49 am

        - Destination:   destination airport code
                         must be three alphabetic characters
                         must be a valid Airport Code as specified by AirportNames
                         example: SEA

        - Arrival:       arrival date and time
                         must be in following format mm/dd/yyyy hh:mm aa
                         leading zeros may be omitted for month, day, and hour
                         time must be in 12-hour format
                         must occur after departure
                         example: 2/13/2023 1:09 pm or 02/13/2023 01:09 pm


