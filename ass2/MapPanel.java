import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MapPanel extends JPanel implements MapListener
{
	private static final long serialVersionUID = 1L;
	private Set<PlaceIcon> placeIcon = new HashSet<PlaceIcon>();
	private Set<RoadIcon> roadIcon = new HashSet<RoadIcon>();
	private Map m;
	private int sx;
	private int sy;
	private int ex;
	private int ey;
	private boolean select;
	private int type;
	private String name;
	private int length;
	private Place from;
	private Place to;
	
	public MapPanel() {
		setLayout(null);
        setSize(800, 500);
		setOpaque(false);
		type=0;
		
		addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
            	if((type==0 && select) || type==2){
            		ex=e.getX();
            		ey=e.getY();
            		repaint();
            	}
            }
        });
		
		addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
            	if(type==0) {
            		if(!placeIcon.isEmpty())
                		for(PlaceIcon pi : placeIcon)
                			pi.setChoose(false);
                	repaint();
            	}
            }
            public void mousePressed(MouseEvent e) {
        		if(type==0) {
        			select=true;
            		sx=e.getX();
            		sy=e.getY();
        		}
            }
            public void mouseReleased(MouseEvent e) {
            	if(type==0 && select) {
            		ex=e.getX();
            		ey=e.getY();
            		repaint();
            		if(!placeIcon.isEmpty())
        	    		for(PlaceIcon pi : placeIcon)
        	    			pi.comparexy(Math.min(sx,ex), Math.max(sx,ex), Math.min(sy,ey), Math.max(sy,ey));			
            		select=false;
            	}
            }
        });
	}
	
	//connect map panel with the map it belong to;
	public void setMap(Map newM) {
		m=newM;
	}
	
	//return the mouse event type this panel belong to. only used when add new roads.
	public int getType() {
		return type;
	}
	
	//the start of add new road, accecpt a string as new road's name, an int as length.
	public void newRoad(String rm, int l) {
		type=1;
		if(!placeIcon.isEmpty())
        	for(PlaceIcon pi: placeIcon) {
        		pi.setChoose(false);
        		pi.placeChanged();
        	}
		name=rm;
		length=l;
		setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
	}
	
	//set the new road's from and to. accept a boolean as argument, true for from, false for to.
	public void setFT(Place place, boolean ft) {
		if(ft) {
			from = place;
			type = 2;
		}else {
			type = 0;
			to = place;
			try {
    			m.newRoad(from, to, name, length);
    		}catch (IllegalArgumentException e1) {
	        	JOptionPane.showMessageDialog(null,e1,"fail to create new place",JOptionPane.ERROR_MESSAGE);
				e1.printStackTrace();
	        }
			setCursor(Cursor.getDefaultCursor());
		}
		if(!placeIcon.isEmpty())
        	for(PlaceIcon pi: placeIcon)
        		pi.placeChanged();
	}
	
	//return the placeIcon in this panel
	public Set<PlaceIcon> selectPI() {
		return placeIcon;
	}
	
	//return the roadIcon in this panel
	public Set<RoadIcon> selectRI() {
		return roadIcon;
	}
	
	//add a placeIcon to this panel
	public void addPI(PlaceIcon pi) {
		placeIcon.add(pi);
		add(pi);
	}
	
	//add a roadIcon to this panel
	public void addRI(RoadIcon ri) {
		roadIcon.add(ri);
		add(ri);
	}
	
	//override paint
	public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.gray);
        if(select)
        	g2.drawRect(Math.min(sx,ex), Math.min(sy,ey), Math.abs(sx-ex), Math.abs(sy-ey));
        if(type==2)
        	g2.drawLine(from.getX()+5, from.getY()+5, ex, ey);
    }
	
    //Called whenever the number of places in the map has changed
    public void placesChanged() {
    	validate();
    	repaint();
    }

    //Called whenever the number of roads in the map has changed
    public void roadsChanged() {
    	validate();
    	repaint();
    }

    //Called whenever something about the map has changed
    //(other than places and roads)
    public void otherChanged() {
    	validate();
    	repaint();
    }
}