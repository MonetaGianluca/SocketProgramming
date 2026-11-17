import java.io.*;
import java.net.*;

public class ServerCalcolatrice{
	
	private static final int PORTA = 8844;
	private static double calcola(double num1, String operazione, double num2) {
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
	
	public static void main(String[] args){
		
		try (ServerSocket serverSocket = new ServerSocket(PORTA)) {
			System.out.println("Server avviato sulla porta " + PORTA);
			
			BufferedReader in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));	// per leggere i messaggi del client
			PrintWriter out = new PrintWriter(serverSocket.getOutputStream(), true);	// serve a inviare al client le risposte

			while (true) {
				Socket client = serverSocket.accept();
				String richiesta = in.readLine();
				
				String[] parti = richiesta.trim().split("\\s+");
				if (parti.length != 3) {
					out.println("ERRORE: Formato non valido");
					break;
				}
				
				double num1 = Double.parseDouble(parti[0]);
				String operazione = parti[1];
				double num2 = Double.parseDouble(parti[2]);
				if (!Double.isFinite(num1) || !Double.isFinite(num2)) {
					out.println("ERRORE: Sono ammessi soltanto numeri finiti");
					break;
				}
				out.println(calcola(num1, operazione, num2));
			}
		}catch(IOException e){
			System.out.println(e);
		}
	}
}
