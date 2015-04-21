package internet_test;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class ClientWorker implements Runnable {
    private Socket client;
    private TestFrame frame;
    private List<ClientWorker> clients;
    private PrintWriter out;

    ClientWorker(Socket client, TestFrame frame) {
	this.client = client;
	this.frame = frame;
    }

    public void addClientWorkers(List<ClientWorker> clients){
	this.clients = clients;
    }

    public void run(){

	BufferedReader in = null;
	out = null;
	try{
	    in = new BufferedReader(new InputStreamReader(client.getInputStream()));
	    out = new PrintWriter(client.getOutputStream(), true);
	} catch (IOException e) {
	    System.out.println("in or out failed");
	    System.exit(-1);
	}
	while(true){
	    try{
		String line;
		line = in.readLine();
		for (ClientWorker clientWorker : clients) {
		    clientWorker.getOut().println("TEXT&" + line);
		}

		frame.updateTextSquare(line);
	    }catch (IOException e) {
		System.out.println("Read failed");
		System.exit(-1);
	    }
	}
    }



    public PrintWriter getOut() {
	return out;
    }
}

