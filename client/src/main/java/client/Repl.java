package client;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Repl {
    private final ChessClient client;

    public Repl(String serverUrl) {
        client = new ChessClient(serverUrl);
    }

    public void run() {
        try (Scanner scanner = new Scanner(System.in)) {
            var result = "";
            while (!result.equals("quit")) {
                printPrompt();
                String line = scanner.nextLine();

                try {
                    result = client.eval(line);
                    System.out.print(BLUE + result);
                } catch (Throwable e) {
                    var msg = e.toString();
                    System.out.print(msg);
                }
            }
            System.out.println();
        }
    }

    private void printPrompt() {
        String stateString = client.getState() == State.SIGNEDIN ? GREEN + "LOGGED IN" : BLUE + "LOGGED OUT";
        System.out.print(
            "\n" 
            + RESET 
            + LIGHT_GREY
            + "[" + stateString + LIGHT_GREY + "] "
            + ">>> " 
            + MAGENTA
        );
    }

}
