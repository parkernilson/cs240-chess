package client;

import java.util.Scanner;

import exceptions.ResponseException;

import static ui.EscapeSequences.*;

public class Repl {
    private final ChessClient client;

    public Repl(String serverUrl) throws ResponseException {
        client = new ChessClient(serverUrl, this);
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

    public void printPrompt() {
        String stateString = switch(client.getState()) {
            case State.SIGNEDIN -> GREEN + "LOGGED IN";
            case State.SIGNEDOUT -> BLUE + "LOGGED OUT";
            case State.GAMEPLAY -> GREEN + "GAMEPLAY";
        };
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
