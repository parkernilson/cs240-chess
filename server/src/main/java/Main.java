import server.Server;

public class Main {
    public static void main(String[] args) {
        var server = new Server();
        try {
            server.run(8080);
        } catch(Exception e) {
            System.out.println("Unable to start server: " + e.getMessage());
        }
    }
}