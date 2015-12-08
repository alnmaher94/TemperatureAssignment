package Client;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextField;

class CustomTextField extends JTextField implements KeyListener{
    
	public CustomTextField(){
        this.addKeyListener(this);

    }


	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		char character = e.getKeyChar();

        if (character < '0' || character > '9' ) {
            e.consume();
        }
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}