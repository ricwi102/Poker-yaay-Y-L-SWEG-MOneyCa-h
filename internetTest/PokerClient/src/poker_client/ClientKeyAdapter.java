package poker_client;


import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;


public class ClientKeyAdapter extends KeyAdapter{

    private ClientFrame frame;

    public ClientKeyAdapter(final ClientFrame frame) {
	this.frame = frame;
    }

    public void keyPressed(KeyEvent evt) {
        if(evt.getKeyCode() == KeyEvent.VK_ENTER || evt.getKeyCode() == KeyEvent.VK_TAB){
	    frame.nextTextSquare();
        }
      }
}
