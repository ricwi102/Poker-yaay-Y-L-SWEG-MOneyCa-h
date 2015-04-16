package client_test;


public class Testing{
    public static void main(String[] args){

	ClientExempel client = new ClientExempel();
	client.listenSocket(15388);

	TestFrame frame = new TestFrame(client);
	frame.pack();
	frame.setVisible(true);

	client.read(frame);
    }
}
