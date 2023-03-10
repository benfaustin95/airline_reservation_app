package edu.pdx.cs410J.bena2;

import edu.pdx.cs410J.ParserException;
import edu.pdx.cs410J.web.HttpRequestHelper;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.*;

/**
 * The main class for the CS410J airline Project5
 */
public class Project5 extends CommandLineParser {
    /**
     * Main method for  CS410J Project 5, parses the command line for airline/flight data
     * and program options. All errors cause the program to exit and an error message to be printed
     * to standard error.
     * @param args an Array of Strings hold user supplied input from the command line
     */

  public static void main(String[] args) {
      Project5 test = new Project5();
      ArrayList<String> args_list = new ArrayList<>();
      HashMap<String, String> options_list = new HashMap<>();
      AirlineRestClient restClient = null;
      print = search = false;

      splitOptionsAndArgs(args, args_list, options_list);

      if(args_list.isEmpty() && options_list.isEmpty()) {
          System.err.println(missingArguments);
          printUsage(0);
          return;
      }

      if(options_list.containsKey(operations[0])) {
          printREADME(0);
          return;
      }
      

      if(options_list.containsKey(operations[1])) {
          print = true;
          options_list.remove(operations[1]);
      }

      if((options_list.containsKey(operations[2]) || options_list.containsKey(operations[3])) && (restClient = setRestClient(options_list))==null){
          return;
      }

      if(options_list.containsKey(operations[4]))
      {
          if(print) {
              System.err.println("Command Line Error: -print and -search can not be asserted together");
              System.err.println("Please see README for further instructions on valid command line" +
                      " options");
              return;
          }
          search = true;
          options_list.remove(operations[4]);
      }

      if(invalidOptions(options_list))
          return;

      test.execution(args_list, restClient);
  }

    /**
     * setRestClient validates teh host and port arguments then instantiates the AirlineRestClient.
     * @param optionsList the Map containing the host and port.
     * @return the instantiated AirlineRestClient.
     */
    protected static  AirlineRestClient setRestClient(HashMap<String, String> optionsList) {
      String host = optionsList.get(operations[2]);
      String port = optionsList.get(operations[3]);
      boolean hostE = optionsList.containsKey(operations[2]);
      boolean portE = optionsList.containsKey(operations[3]);

      optionsList.remove(operations[2]);
      optionsList.remove(operations[3]);

      if(!hostE || !portE) {
          System.err.println("Command Line error: " + (hostE ? "-host" : "-port") + " can not be asserted without" +
                  " " + (hostE ? "-port" : "-host"));
          System.err.println("Please see README for further instructions on valid command line" +
                  " options");
          return null;
      }

      if(host == null || port == null) {
          System.err.println("Command Line Error: "+(host==null?"-host":"-port")+" option was asserted" +
                  " with no argument");
          System.err.println("Please see README for further instructions on valid command line" +
                  " options");
          return null;
      }

      try{
          return new AirlineRestClient(host, Integer.parseInt(port));
      }catch (NumberFormatException ex) {
          System.err.println("Command Line Error: "+port+" is not a valid integer");
          System.err.println("Please see README for further instructions on valid command line" +
                  " options");
          return null;
      }
    }

    /**
     * invalidOptions validates that no options remain, outputs invalid options to standard error.
     * @param options_list the Set of options.
     * @return returns true if there are any invalid options and false if not.
     */
    protected static boolean invalidOptions(Map<String, String> options_list) {

        if(options_list.isEmpty())
            return false;

        System.err.print("Invalid Options: ");
        for(String option: options_list.keySet())
        {
            System.err.print(option+" ");
        }
        System.err.println("\nPlease see README for further instructions on valid command line" +
                " options");
        return true;
    }

    /**
     * execution handles the primary execution of the program, parsing the file, creating the new
     * flight, and outputting the airline.
     * @param args_list  a List of arguments used to create the airline/file.
     */
    protected void execution(ArrayList<String> args_list, AirlineRestClient client) {
        try {
            if (search) {
                searchAirline(client, args_list);
                return;
            }

            Flight toPrint = createAirlineAndFlight(args_list);

            if(client != null)
                client.addFlight(args_list.get(0), toPrint);

            if (print)
                System.out.println(toPrint.toString());

        } catch (IllegalArgumentException ex) {
            System.err.println("Error Command Line: " + ex.getMessage() + "\n" +
                    "Please see README for further instructions.");
        } catch (ParserException ex) {
            System.err.println("Error XML: " + ex.getMessage());
            System.err.println("Please see README for further instructions");
        } catch (IOException | HttpRequestHelper.RestException ex) {
            System.err.println("Error Server: "+ex.getMessage());
            System.err.println("Please see README for further instructions");
        } catch (Exception ex) {
            System.err.println("Error Command Line: " + ex.getMessage());
            System.err.println("Please see README for further instructions");
        }
    }


    /**
     * searchAirline directs the control flow of the search option functionality.
     * @param client the AirlineRestClient connected to the server.
     * @param argsList the ArrayList<String> of search arguments.
     * @throws HttpRequestHelper.RestException not thrown by method, but rather thrown by calling routine
     *                                         and not caught.
     * @throws ParserException                 not thrown by method, but rather thrown by calling routine
     *                                         and not caught.
     * @throws IOException                     not thrown by method, but rather thrown by calling routine
     *                                         and not caught.
     * @throws IllegalArgumentException        not thrown by method, but rather thrown by calling routine
     *                                         and not caught.
     */
    protected void searchAirline(AirlineRestClient client, ArrayList<String> argsList) throws HttpRequestHelper.RestException, ParserException, IOException, IllegalArgumentException{
        if(client == null)
            throw new IllegalArgumentException("-search must be asserted with -host and -port options");

        boolean path = validateSearchArguments(argsList);
        if (path) {
            airline = client.getAirline(argsList.get(0));
            prettyPrintFile(null, null);
            return;
        }
        airline = client.getAirline(argsList.get(0), argsList.get(1), argsList.get(2));
        prettyPrintFile(argsList.get(1), argsList.get(2));
    }
}
