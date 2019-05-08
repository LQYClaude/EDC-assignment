import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

import javax.swing.JComponent;

public class PlaceIcon extends JComponent implements PlaceListener
{
	private static final long serialVersionUID = 1L;
	private Place place;
	private MapPanel mp;
	private int r;
	private int gr;
	private int b;
	private int dx;
	private int dy;
	private boolean choose;
	private boolean start;
	private boolean end;
	private boolean drag;
	private Point in;
	private int type;

	//initialization
	public PlaceIcon(Place newP) {
		setSize(new Dimension(11, 11));
		setOpaque(false);
		place = newP;
		in=getLocation();
		in.translate(place.getX(),place.getY());
		setLocation(in);
        Random rand = new Random();
        int pr = rand.nextInt(255);
        int pgr = rand.nextInt(255);
        int pb = rand.nextInt(255);
		r=pr;
		gr=pgr;
		b=pb;
		dx=0;
		dy=0;
		type=0;
		choose=false;
		drag=false;
		start=newP.isStartPlace();
		end=newP.isEndPlace();
		
		addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
            	if(type==0 && drag)
            		relocate(e);
            }
        });
		
		addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
            	choose=!choose;
            	repaint();
            	if(type==1) {
            		mp.setFT(place,true);
            	}else if(type==2) {
            		mp.setFT(place,false);
            	}
            }
            public void mousePressed(MouseEvent e) {
            	if(type==0) {
                	dx=e.getX();
                	dy=e.getY();
                	drag=true;
            	}
            }
            public void mouseReleased(MouseEvent e) {
            	if(type==0 && drag) {
            		relocate(e);
            		drag=false;
            	}
            }
        });
	}

	//relocate the place depend on mouse coordinate
	void relocate(MouseEvent e) {
        int x = e.getX()-dx;
        int y = e.getY()-dy;
        in.translate(x,y);
		setLocation(in);
        place.moveBy(x,y);
    }
	
	//return the place this icon connected with
	public Place returnP() {
        return place;
    }
	
	//return whether this icon is chose
	public boolean choosed() {
        return choose;
    }
	
	//set this icon to be opposite chose type
	public void setChoose(boolean set) {
		choose = set;
	}
	
	//connect this icon with the panel it belong to
	public void setMP(MapPanel newMp) {
		mp=newMp;
	}
	
	//check whether this icon is in an area
	public void comparexy(int sx, int lx, int sy, int ly) {
		if(sx<=place.getX() && lx>=place.getX() && sy<=place.getY() && ly>=place.getY())
			setChoose(true);
		else
			setChoose(false);
		repaint();
	}
	
	//override paint
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
        g2.setColor(new Color(r,gr,b));
        if(choose)
        	g2.fillRect(0, 0, 10, 10);
        else
        	g2.drawRect(0, 0, 10, 10);
        
        g2.setColor(Color.BLACK);
        if(start)
        	g2.drawString("S",2,10);
        else if(end)
        	g2.drawString("E",2,10);
    }
	
	//Called whenever the visible state of a place has changed
    public void placeChanged() {
    	start = place.isStartPlace();
    	end = place.isEndPlace();
    	type = mp.getType();
    	revalidate();
    	repaint();
    }
}