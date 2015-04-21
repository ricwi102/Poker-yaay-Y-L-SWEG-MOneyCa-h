package poker_client;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client
{
    private GameInfo gameInfo;
    private JFrame frame;
    private PrintWriter out;
    private BufferedReader in;

    public Client(final GameInfo gameInfo, ClientFrame frame) {
	this.gameInfo = gameInfo;
	this.frame = frame;
    }

    public void listenSocket(int port, InetAddress address) {
	try {
	    Socket socket;
	    socket = new Socket(address, port);
	    out = new PrintWriter(socket.getOutputStream(), true);
	    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	} catch (UnknownHostException e) {
	    System.out.println("Unknown host");
	    System.exit(1);
	} catch (IOException e) {
	    System.out.println("No I/O");
	    System.exit(1);
	}
    }

    public void read() {
	while (true) {
	    try {
		String[] command = in.readLine().split("&");

		switch (command[0]){
		    case "TEXT":

			break;
		    case "ADDPLAYER":
			if (command.length > 2){
			    gameInfo.addYou(command[1], Integer.parseInt(command[2]));
			} else
			gameInfo.addPlayer(command[1], Integer.parseInt(command[2]));

			break;
		    case "VALID":

			break;
		    case "ERROR":

			break;






		}
	    } catch (IOException e) {
		System.out.println("Read failed");
		System.exit(-1);
	    }
	}
    }
}
