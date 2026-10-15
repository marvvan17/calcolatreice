package charaf.it;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class CalcServer {
    private static final int PORTA = 12345;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORTA)) {
            System.out.println("Server in ascolto sulla porta " + PORTA);

            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     BufferedReader input = new BufferedReader(
                             new InputStreamReader(clientSocket.getInputStream()));
                     PrintWriter output = new PrintWriter(
                             clientSocket.getOutputStream(), true)) {

                    System.out.println("Connesso: " + clientSocket.getInetAddress());

                    String richiesta = input.readLine();
                    String risposta = calcola(richiesta);

                    output.println(risposta);
                    System.out.println("Richiesta: " + richiesta);
                    System.out.println("Risposta: " + risposta);
                } catch (IOException e) {
                    System.err.println("Errore nella comunicazione con un client: "
                            + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Impossibile avviare il server: " + e.getMessage());
        }
    }

    private static String calcola(String richiesta) {
        if (richiesta == null) {
            return "Errore: richiesta vuota.";
        }

        String[] parti = richiesta.trim().split("\\s+");
        if (parti.length != 3) {
            return "Errore: usa il formato numero operatore numero, ad esempio 10 + 5.";
        }

        try {
            double primo = Double.parseDouble(parti[0]);
            String operatore = parti[1];
            double secondo = Double.parseDouble(parti[2]);
            double risultato;

            switch (operatore) {
                case "+":
                    risultato = primo + secondo;
                    break;
                case "-":
                    risultato = primo - secondo;
                    break;
                case "*":
                    risultato = primo * secondo;
                    break;
                case "/":
                    if (secondo == 0) {
                        return "Errore: divisione per zero.";
                    }
                    risultato = primo / secondo;
                    break;
                default:
                    return "Errore: operatore non riconosciuto. Usa +, -, * oppure /.";
            }

            return "Risultato: " + risultato;
        } catch (NumberFormatException e) {
            return "Errore: uno o entrambi gli operandi non sono numeri validi.";
        }
    }
}
