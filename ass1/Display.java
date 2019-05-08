import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Display implements Gui{

    private JLabel text;
    Controller con;
    
    //Connect gui to controller
    public void connect(Controller controller) {
    	con = controller;
    }

    //Initialise the gui
    public void init()	{
        JFrame win = new JFrame("assignment1");
        win.setSize(400,300);
        JButton bcs=new JButton("coin-slot");
        JButton bgs=new JButton("go/stop");
        bcs.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e){
        		con.coinInserted();
            }
        });
        bgs.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e){
        		con.goStopPressed();
        	}
        });
        text = new JLabel("Insert coin",JLabel.CENTER);

        Container panel = win.getContentPane(); 
        panel.setLayout(new FlowLayout());
        panel.add(text);
        panel.add(bcs);
        panel.add(bgs);
        win.setVisible(true);
    }

    //Change the displayed text
    public void setDisplay(String s) {
    	text.setText(s);
    }
    
}