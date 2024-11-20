package Assignement;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;

@SuppressWarnings("serial")
public class Client_Gui extends JFrame implements AdjustmentListener, ActionListener, WindowListener
{
	JLabel Title = new JLabel("This is a Client");
	JButton Connect, ChangeName;
	
	public Client_Gui(){
		JPanel hp = new JPanel();
		hp.setLayout(new BoxLayout(hp, BoxLayout.Y_AXIS));
		this.getContentPane().add(hp);

		JPanel p = new JPanel();
		JSlider s = new JSlider(-10, 100, 20);
		p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		p.setBorder(new TitledBorder("Major Ticks"));
		s = new JSlider(100, 1000, 400);
		s.setPaintTicks(true);
		s.setMajorTickSpacing(100);
		s.getAccessibleContext().setAccessibleName("Major Ticks");
		s.getAccessibleContext().setAccessibleDescription("A slider showing major tick marks");
		//s.addChangeListener(this);
		p.add(s);
		hp.add(p);


		this.pack();
		this.setVisible(true);
	}

	public void actionPerformed(ActionEvent e){
	
	}



	public static void main(String[] args) {
		new Client_Gui();
	}

	public void windowActivated(WindowEvent arg0) {
	}
	public void windowClosed(WindowEvent arg0) {}
	public void windowClosing(WindowEvent arg0) {
		System.exit(0);
	}
	public void windowDeactivated(WindowEvent arg0) {}
	public void windowDeiconified(WindowEvent arg0) {}
	public void windowIconified(WindowEvent arg0) {}
	public void windowOpened(WindowEvent arg0) {}

	@Override
	public void adjustmentValueChanged(AdjustmentEvent e) {
		// TODO Auto-generated method stub
		
	}
}