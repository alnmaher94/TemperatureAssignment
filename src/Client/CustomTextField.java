package Client;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextField;

class CustomTextField extends JTextField implements KeyListener{
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CustomTextField(){
		super();
        this.addKeyListener(this);

    }

	@Override
	public void keyTyped(KeyEvent e) {
		char character = e.getKeyChar();

        if (character < '0' || character > '9' ) {
            e.consume();
        }
	}

	@Override
	public void keyPressed(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {}
}