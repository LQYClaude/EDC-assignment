import java.io.IOException;
import java.awt.Dimension;
import java.awt.event.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.JOptionPane;

public class MapEditor {
	boolean changed = true;
	static MapImpl m = new MapImpl();
	static MapReaderWriter mrw = new MapReaderWriter();
	static JFrame win = new JFrame("Map Editor");
	static Dimension size = new Dimension(800, 600);
	static Set<MapPanel> mapPanel = new HashSet<MapPanel>();

	//this is the main class show the gui
	public static void main(String[] args) {
	    win.addComponentListener(new ComponentListener() {
	        public void componentHidden(ComponentEvent e) {}
	        public void componentMoved(ComponentEvent e) {}
	        public void componentShown(ComponentEvent e) {}
	        public void componentResized(ComponentEvent e) {
	            size = e.getComponent().getBounds().getSize();          
	        }   
	    });
    	MapPanel ml = new MapPanel();
    	ml.setMap(m);
    	win.getContentPane().add(ml);
    	m.addListener(ml);
    	mapPanel.add(ml);
		(new MapEditor()).gui();
	}
    
	//The gui
	private void gui() {
		
		//set jframe
		win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		win.setResizable(true);
        win.setSize(size);
        
        //set menu bar
        JMenuBar menuBar = new JMenuBar(); 
        win.setJMenuBar(menuBar);
        JMenu fileMenu = new JMenu("File");
        JMenu editMenu = new JMenu("Edit");
        menuBar.add(fileMenu); 
        menuBar.add(editMenu);
        
        //set file bar
        JMenuItem openItem = new JMenuItem("Open...");
        openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        openMap om = new openMap();
        openItem.addActionListener(om);
        JMenuItem saveItem = new JMenuItem("Save as...");
        saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        saveMap sm = new saveMap();
        saveItem.addActionListener(sm);
        JMenuItem appendItem = new JMenuItem("Append...");
        appendItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
        appendMap am = new appendMap();
        appendItem.addActionListener(am);
        JMenuItem quitItem = new JMenuItem("Quit");
        quitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
        closeTheWindow cw = new closeTheWindow();
        quitItem.addActionListener(cw);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(appendItem);
        fileMenu.add(quitItem);
        
        //set edit bar
        JMenuItem npItem = new JMenuItem("New Place");
        newPlace np = new newPlace();
        npItem.addActionListener(np);
        JMenuItem nrItem = new JMenuItem("New Road");
        newRoad nr = new newRoad();
        nrItem.addActionListener(nr);
        JMenuItem startItem = new JMenuItem("Set start");
        setSE ss = new setSE(true);
        startItem.addActionListener(ss);
        JMenuItem unstartItem = new JMenuItem("Unset start");
        unsetSE uss = new unsetSE(true);
        unstartItem.addActionListener(uss);
        JMenuItem endItem = new JMenuItem("Set end");
        setSE se = new setSE(false);
        endItem.addActionListener(se);
        JMenuItem unendItem = new JMenuItem("Unset end");
        unsetSE use = new unsetSE(false);
        unendItem.addActionListener(use);
        JMenuItem deleteItem = new JMenuItem("Delete");
        editMenu.add(npItem);
        editMenu.add(nrItem);
        editMenu.add(startItem);
        editMenu.add(unstartItem);
        editMenu.add(endItem);
        editMenu.add(unendItem);
        editMenu.add(deleteItem);
        
        win.setVisible(true);
	}
	
	//Function for open button
	private class openMap implements ActionListener{
	    public void actionPerformed(ActionEvent e) {
	    	JFileChooser fileChooser  = new JFileChooser();
	        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
	        fileChooser.setAcceptAllFileFilterUsed(false);
	        fileChooser.setFileFilter(new FileFilter() {
	        	   public String getDescription() {return "Map file(*.map)";}
	        	   public boolean accept(File f) {
	        	       if (f.isDirectory()) {
	        	           return true;
	        	       }else{
	        	           String filename = f.getName().toLowerCase();
	        	           return filename.endsWith(".map");
	        	       }}});
	        int returnValue = fileChooser.showOpenDialog(null);
	        if (returnValue == JFileChooser.APPROVE_OPTION) {
	        	File selectedFile = fileChooser.getSelectedFile();
	        	try {
	        		m = new MapImpl();
		        	Reader mapI= new FileReader(selectedFile);
		        	MapPanel ml = new MapPanel();
		        	ml.setMap(m);
		        	win.getContentPane().add(ml);
		        	m.addListener(ml);
		        	mapPanel.add(ml);
		        	mrw.read(mapI, m);
		        }catch (IOException | MapFormatException e1) {
		        	JOptionPane.showMessageDialog(null,e1,"fail to open file",JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
		        }
	        }
	    }
	}
	
	//Function for save button
	private class saveMap implements ActionListener{
	    public void actionPerformed(ActionEvent e) {
	    	JFileChooser fileChooser  = new JFileChooser();
	        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
	        int returnValue = fileChooser.showOpenDialog(null);
	        if (returnValue == JFileChooser.APPROVE_OPTION) {
	        	File selectedFile = fileChooser.getSelectedFile();
	        	try {
		        	Writer w = new FileWriter(selectedFile);
		        	mrw.write(w, m);
		        }catch (IOException e1) {
		        	JOptionPane.showMessageDialog(null,e1,"fail to save file",JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
		        }
	        }
	    }
	}
	
	//Function for append button
	private class appendMap implements ActionListener{
	    public void actionPerformed(ActionEvent e) {
	    	JFileChooser fileChooser  = new JFileChooser();
	        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
	        int returnValue = fileChooser.showOpenDialog(null);
	        if (returnValue == JFileChooser.APPROVE_OPTION) {
	        	File selectedFile = fileChooser.getSelectedFile();
	        	try {
		        	MapPanel ml = new MapPanel();
		        	ml.setMap(m);
		        	m.addListener(ml);
		        	mapPanel.add(ml);
		        	win.getContentPane().add(ml);
		        	Reader mapI= new FileReader(selectedFile);
		        	mrw.read(mapI, m);
		        	changed=true;
		        }catch (IOException | MapFormatException e1) {
		        	JOptionPane.showMessageDialog(null,e1,"fail to append file",JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
		        }
	        }
	    }
	}
	
	//Function for close button
	private class closeTheWindow implements ActionListener{
		int choice = 0;
	    public void actionPerformed(ActionEvent e) {
	    	if(changed==true)
	    		choice = JOptionPane.showConfirmDialog(null, "Do you want to quit?", "File is not saved",
	    				JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
	        if(choice==0)
	        	System.exit(0);
	    }
	}
	
	//function for set start/end place. Accept a boolean as argument, true for setting start place, false for setting end place.
	private class setSE implements ActionListener{
		boolean t = true;
		public setSE(boolean newT) {
			t=newT;
		}
	    public void actionPerformed(ActionEvent e) {
	    	Set<PlaceIcon> selectPI = new HashSet<PlaceIcon>();
	    	if(!mapPanel.isEmpty())
	    		for(MapPanel mp : mapPanel)
	    			if(!mp.selectPI().isEmpty())
	    	    		for(PlaceIcon pi : mp.selectPI())
	    	    			if(pi.choosed())
	    	    				selectPI.addAll(mp.selectPI());
	    	if(selectPI.size()==1)
	    		for(PlaceIcon pi : selectPI) {
	    			if(t==true)
	    				m.setStartPlace(pi.returnP());
	    			else if(t==false)
	    				m.setEndPlace(pi.returnP());
	    		}
	    	else
	    		JOptionPane.showMessageDialog(null,"Please select one place icon.");
	    }
	}
	
	//function for unsetting start/end place. Accept a boolean as argument, true for unsetting start place, false for unsetting end place.
	private class unsetSE implements ActionListener{
		boolean t = true;
		public unsetSE(boolean newT) {
			t=newT;
		}
	    public void actionPerformed(ActionEvent e) {
	    	if(t==true) {
	    		if(m.getStartPlace()==null)
	    			JOptionPane.showMessageDialog(null,"There is no start place.");
	    		else
	    			m.setStartPlace(null);
	    	}else if(t==false) {
	    		if(m.getEndPlace()==null)
	    			JOptionPane.showMessageDialog(null,"There is no end place.");
	    		else
	    			m.setEndPlace(null);
	    	}
	    }
	}
	
	//Function for create new place
	private class newPlace implements ActionListener{
	    public void actionPerformed(ActionEvent e) {
	    	String name = JOptionPane.showInputDialog(null, "What is new place's name?");
	    	if(name!=null && !name.equals("null") && !name.equals("")) {
	    		try {
	    			m.newPlace(name, size.width/2, size.height/2);
	    		}catch (IllegalArgumentException e1) {
		        	JOptionPane.showMessageDialog(null,e1,"Fail to create new place",JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
		        }
	    	}
	    }
	}
	
	//Function for create new road
	private class newRoad implements ActionListener{
	    public void actionPerformed(ActionEvent e) {
	    	String name = JOptionPane.showInputDialog(null, "What is new road's name?");
	    	String length = JOptionPane.showInputDialog(null, "What is new road's length?");
	    	if(length!=null && !length.equals("") && !length.equals("null")) {
	    		int l=-1;
	    		try {
	    			l = Integer.parseInt(length);
	    		}catch (NumberFormatException e1) {
		        	JOptionPane.showMessageDialog(null,e1,"Length must be integer",JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
		        }
	    		if(l>0) {
		    		if(name.equals("") || name.equals("null"))
	    				name="-";
		    		if(!mapPanel.isEmpty())
			    		for(MapPanel mp : mapPanel)
			    			mp.newRoad(name,l);
	    		}else
	    			JOptionPane.showMessageDialog(null,"Length must be positive.");
	    	}else
	    		JOptionPane.showMessageDialog(null,"Length must be entered.");
	    }
	}
}