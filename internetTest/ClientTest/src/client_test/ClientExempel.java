package client_test;
import javafx.event.ActionEvent;

import java.net.*;
import java.io.*;

public class ClientExempel{
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;



    public void listenSocket(int port){
    //Create socket connection
       try{
         socket = new Socket(InetAddress.getLocalHost(), port);
         out = new PrintWriter(socket.getOutputStream(), true);
         in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
       } catch (UnknownHostException e) {
         System.out.println("Unknown host");
         System.exit(1);
       } catch  (IOException e) {
         System.out.println("No I/O");
         System.exit(1);
       }
    }

    public void read(TestFrame frame){
        while(true){
            try{
                String line = in.readLine();
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

    public BufferedReader getIn() {
	return in;
    }


    /* public static void main(String [] args)
    {
	String serverName = args[0];
	int port = Integer.parseInt(args[1]);
	try
	{
	    System.out.println("Connecting to " + serverName + " on port " + port);
	    Socket client = new Socket(serverName, port);
	    System.out.println("Just connected to "+ client.getRemoteSocketAddress());

	    OutputStream outToServer = client.getOutputStream();
	    DataOutputStream out = new DataOutputStream(outToServer);

	    out.writeUTF("Hello from " + client.getLocalSocketAddress());

	    InputStream inFromServer = client.getInputStream();
	    DataInputStream in = new DataInputStream(inFromServer);

	    System.out.println("Server says " + in.readUTF());
	    client.close();
	}catch(IOException e)
	{
	    e.printStackTrace();
	}
    } */
}



