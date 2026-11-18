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
			
			while(true){	// per accettare più client
				
				try(Socket client = serverSocket.accept();){	// try with resources
					
					System.out.println("client connesso");

					BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));	// per leggere i messaggi del client
					PrintWriter out = new PrintWriter(client.getOutputStream(), true);	// serve a inviare al client le risposte
									
					while(true){	// per accettare più richieste da un client
						
						try{
							System.out.println("in attesa di una richiesta dal client");
							String richiesta = in.readLine();
							System.out.println("richiesta ricevuta: "+richiesta);
							if(richiesta == null){
								break;
							}
							String[] parti = richiesta.trim().split("\\s+");
							
							if(parti.length == 1 && parti[0].equals("quit")){
								out.println("connessione chiusa");
								client.close();
								break;
							}else if (parti.length != 3) {
								out.println("ERRORE: Formato non valido");
								continue;
							}
							
							double num1 = Double.parseDouble(parti[0]);
							String operazione = parti[1];
							double num2 = Double.parseDouble(parti[2]);
							if (!Double.isFinite(num1) || !Double.isFinite(num2)) {
								out.println("ERRORE: Sono ammessi soltanto numeri finiti");
								continue;
							}
							out.println(calcola(num1, operazione, num2));
							System.out.println("risposta inviata");
						}catch(Exception e){
							out.println("ERRORE: "+ e);
						}
					}
				}catch(IOException e){
					System.out.println(e);
				}

			}

		}catch(IOException e){
			System.out.println(e);
		}
	}
}
