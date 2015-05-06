package poker_client;


import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * KeyAdapter which checks for the "Enter" and "Tab" key
 *
 * @author Johannes Palm Myllylä, Richard Wigren
 * @version 1.0
 */

public class ClientKeyAdapter extends KeyAdapter{

    private ClientFrame frame;

    public ClientKeyAdapter(final ClientFrame frame) {
	this.frame = frame;
    }

    @Override public void keyPressed(KeyEvent evt) {
        super.keyPressed(evt);
        if(evt.getKeyCode() == KeyEvent.VK_ENTER || evt.getKeyCode() == KeyEvent.VK_TAB){
	    frame.nextTextSquare();
        }
    }
}
