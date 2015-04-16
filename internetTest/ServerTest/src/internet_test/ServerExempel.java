package internet_test;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ServerExempel extends Thread
{

    private ServerSocket server;
    //private Socket client;
    //private BufferedReader in;
    //private PrintWriter out;
    //private String line;
    private TestFrame frame;
    private List<ClientWorker> clients;

    public ServerExempel() {
	this.frame = new TestFrame(this);
	frame.pack();
	frame.setVisible(true);
	clients = new ArrayList<>();
    }

    public void shutdown(){
	try{
	    server.close();
	} catch (IOException e) {
	    System.out.println("Could not close socket");
	    System.exit(-1);
	}
	System.exit(0);
    }

    public void listenSocket(int port) {
	try {
	    server = new ServerSocket(port);
	} catch (IOException e) {
	    System.out.println("Could not listen on port " + port);
	    System.exit(-1);
	}
	while (true) {
	    ClientWorker worker;
	    try {
		//server.accept returns a client connection
		worker = new ClientWorker(server.accept(), frame);
		clients.add(worker);
		worker.addClientWorkers(clients);
		Thread t = new Thread(worker);
		t.start();
	    } catch (IOException e) {
		System.out.println("Accept failed: " + port);
		System.exit(-1);
	    }
	}
    }
}





    /*
    public void listenSocket(int port){
	try{
	    server = new ServerSocket(port);
	} catch (IOException e) {
	    System.out.println("Could not listen on port " + port);
	    System.exit(-1);
	}

	try{
	    client = server.accept();
	} catch (IOException e) {
	    System.out.println("Accept failed: " + port);
	    System.exit(-1);
	}

	try{
	    in = new BufferedReader(new InputStreamReader(client.getInputStream()));
	    out = new PrintWriter(client.getOutputStream(), true);
	} catch (IOException e) {
	    System.out.println("Read failed");
	    System.exit(-1);
	}
    }*/

/*
    public void receive()
    {
	while (true) {
	    try {
		line = in.readLine();
		frame.updateTextSquare(line);
		//Send data back to client
		out.println(line);
	    } catch (IOException e) {
		System.out.println("Read failed");
		System.exit(-1);
	    }
	}
    }*/

