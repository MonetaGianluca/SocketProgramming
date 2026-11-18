import java.io.*;
import java.net.*;

class GestoreClient implements Runnable {
    private final Socket client;

    GestoreClient(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        try (Socket s = client) {

            System.out.println("client connesso");

            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));	// per leggere i messaggi del client
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);	// serve a inviare al client le risposte
                            
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
                    out.println(ServerCalcolatrice.calcola(num1, operazione, num2));
                    System.out.println("risposta inviata");
                }catch(RuntimeException e){ // ho usato RuntimeExeption per evitare di catturare IOException (il client si è disconnesso senza scrivere quit) e finire in un loop infinito
                    out.println("ERRORE: "+ e);
                }
            }
        } catch (IOException e) {
            System.out.println("errore server: " + e);
        }
    }
}