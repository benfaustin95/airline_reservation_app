#################################################################################
#              Ben Austin, CS410J-001, 1/24/2023, bena2@pdx.edu                  #
#                Program 2: Designing an Airline Application                    #
#################################################################################

Overview:
    Program Two airline-2023.0.0 allows the user to create and print an airline
    as well as its associated flight(s) from the command line. In addition, the
    program allows the user to read and write airline contents to/from a text file.
    Please see the below execution instructions.

#################################################################################
#                            Provided Interface                                 #
#################################################################################
Please enter the following to execute the program.

    Command Line Execution: java -jar target/airline-2023.0.0.jar [options] <arguments>

        Options (all options are optional and can be provided in any order)
            -README         -Print README
                            -Please note if provided all other instructions will be ignored.

            -print          -Print Most recent flight

            -textFile file  -Read/Write to text file
                            -Please note if file does not yet exist one will be created.

        Arguments (program will terminate in error if all arguments are not present
                   and in correct format)

            - airline:      the name of the airline
                            multi-word names must be delimited by
                            double quotes example: "Airline Name".

            - flightNumber: the flight number
                            must be integer greater than zero.
                            example: 139

            - Source:       departure airport code
                            must be three alphabetic characters
                            example: PDX

            - Departure:    departure date and time
                            must be in following format mm/dd/yyyy hh:mm
                            leading zeros may be omitted for month, day, and hour
                            time must be in 24-hour format
                            example: 1/1/2023 19:49 or 01/01/2023 19:49

            - Destination:  destination airport code
                            must be three alphabetic characters
                            example: SEA

            - Arrival:      must be in following format mm/dd/yyyy hh:mm
                            leading zeros may be omitted for month, day, and hour
                            time must be in 24-hour format
                            must occur after departure
                            example: 2/13/2023 01:09 or 02/13/2023 1:09


