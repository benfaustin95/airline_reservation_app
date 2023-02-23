#################################################################################
#              Ben Austin, CS410J-001, 02/22/2023, bena2@pdx.edu                #
#                Program 4: Storing Airlines as XML                             #
#################################################################################

Overview:
    Program Four airline-2023.0.0 allows the user to create and print an airline
    as well as its associated flight(s) from the command line. In addition, the
    program allows the user to read/write airline contents to/from a text file,
    print airline information in an easy-to-read format, and read/write airline
    contents to/from an XML file.
    Please see the below execution instructions.

##################################################################################
#                            Provided Interface                                  #
##################################################################################
Please enter the following to execute the program.

    Command Line Execution: java -jar target/airline-2023.0.0.jar [options] <arguments>

        Options (all options are optional and can be provided in any order)
            -README         -Print README
                            -Please note if provided all other instructions will
                             be ignored

            -print          -Print most recently added flight

            -textFile file  -Read/Write to/from text file
                            -Please note if file does not yet exist one will be
                             created

            -pretty file    -Pretty print the airline's flights to a text file or
                             standard out
                            -Please note if file does not yet exist one will be
                             created
                            -Please note a "-" can be used in place of a file name
                             to output to standard out
                            -Please note the file path can not match what is provided
                             with the -textFile/-xmlFile option

            -xmlFile file   -Read/Write to/from an xml file
                            -Please note that -textFile can not be exercised in
                             conjunction with -xmlFile
                            -Please note if the file does not exist one will be
                             created

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


