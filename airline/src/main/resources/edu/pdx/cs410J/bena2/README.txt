#################################################################################
#              Ben Austin, CS410J-001, 1/24/2023, bena2@pdx.edu                  #
#                Program 1: Designing an Airline Application                    #
#################################################################################

Overview:
    Program One airline-2023.0.0 contains the implementation of the Airline and
    Flight classes outlined by the Abstract Airline and Abstract Flight classes.
    Thus far, the program provides the user three methods of functionality:
        1: Print the README(current document) and exit the program.
            - No other execution will occur if README option selected.
        2: Instantiate an airline and flight containing the following:
            - Name: name of airline
            - Flight Number: unique flight identification number
            - Source: three-letter code of departure airport
            - Departure: departure date and time (24 hour time)
            - Destination: three-letter code of arrival airport
            - Arrival: arrival date and time (24 hour time)
        3: Print a description of the new flight vis standard out.
   The program instantiates the airline and flight via data parsed from the command
   line. All command  line options, argument, and  formatting errors will cause the
   termination of the program and messages will be printed via standard error.

#################################################################################
#                            Provided Interface                                 #
#################################################################################

Command Line Execution: java -jar target/airline-2023.0.0.jar [options] <arguments>

        Options (provided in any order):
            - Print README: -README
            - Print Flight: -print
        Arguments (must be provided in the following order):
            - Airline Name: multi-word names must be delimited by
                            double quotes.
                            example: "Airline Name"
            - Flight Number: must be integer greater than zero.
                             example: 139
            - Source: must be three alphabetic characters
                      example: PDX
            - Departure: must be in following format mm/dd/yyyy hh:mm
                         leading zeros may be omitted for month, day,
                         and hour
                         time must be in 24-hour format
                         example: 1/1/2023 19:49 or 01/01/2023 19:49
            - Destination: must be three alphabetic characters
                           example: SEA
            - Arrival: must be in following format mm/dd/yyyy hh:mm
                       leading zeros may be omitted for month, day,
                       and hour
                       time must be in 24-hour format
                       must occur after departure 
                       example: 2/13/2023 01:09 or 02/13/2023 1:09


