import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

public class RoadIcon extends JComponent implements RoadListener
{
	private static final long serialVersionUID = 1L;
	private Road road;

	//initialization
	public RoadIcon(Road newR) {
		setOpaque(false);
		road = newR;
		setSize(new Dimension(800,500));
	}

	//return the road this icon connected with
	public Road returnR() {
        return road;
    }
	
	//override paint
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.BLACK);
        
        g2.drawLine(road.firstPlace().getX()+5,
        		road.firstPlace().getY()+5,
        		road.secondPlace().getX()+5,
        		road.secondPlace().getY()+5);
        
        g2.drawString(road.roadName()+" "+road.length(),
        		road.firstPlace().getX()/2+road.secondPlace().getX()/2,
        		road.firstPlace().getY()/2+road.secondPlace().getY()/2);
    }
	
	//Called whenever the visible state of a place has changed
    public void roadChanged() {
    	revalidate();
    	repaint();
    }
}