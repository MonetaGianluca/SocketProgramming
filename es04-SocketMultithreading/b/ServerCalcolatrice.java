import java.io.*;
import java.net.*;

public class ServerCalcolatrice{
	
	private static final int PORTA = 8844;
	public static double calcola(double num1, String operazione, double num2) {
		switch (operazione) {
			case "+":
				return num1 + num2;
			case "-":
				return num1 - num2;
			case "*":
				return num1 * num2;
			case "/":
				if (num2 == 0) throw new ArithmeticException("Divisione per zero");
				return num1 / num2;
			default: 
				throw new IllegalArgumentException("Operazione non supportata");
		}
	}
	

		
    public static void main(String[] args) throws IOException {
        try (ServerSocket listener = new ServerSocket(PORTA)) {
            System.out.println("Server in ascolto sulla porta " + PORTA);
            while (true) {
                Socket socket = listener.accept(); 
                new Thread(new GestoreClient(socket)).start();
            }
        }
    }
}
