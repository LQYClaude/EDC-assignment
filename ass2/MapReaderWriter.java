import java.io.Reader;
import java.io.IOException;
import java.io.Writer;
import java.io.BufferedReader;

public class MapReaderWriter implements MapIo 
{
    //This class handles reading and writing map representations as 
    //described in the practical specification

    //Read the description of a map from the 
    //Reader r, and transfers it to Map, m.
    public void read (Reader r, Map m) throws IOException, MapFormatException
    {
    	BufferedReader read = new BufferedReader(r);
    	String line = "";
    	
    	while ((line = read.readLine()) != null) {
    		if(!line.equals("")) {
    			String[] data = line.split(" ");
                if(data[0].equals("place")) {
                	if(data.length!=4 || data[1].equals("null"))
                		throw new MapFormatException(10,"place input");
                	m.newPlace(data[1], Integer.parseInt(data[2]), Integer.parseInt(data[3]));
                }else if(data[0].equals("road")) {
                	if(data[2].equals(""))
                		data[2]="-";
                	if(data.length!=5 || data[2].equals("null") || m.findPlace(data[1])==null || m.findPlace(data[4])==null)
                		throw new MapFormatException(10,"road input");
                	m.newRoad(m.findPlace(data[1]), m.findPlace(data[4]), data[2], Integer.parseInt(data[3]));
                }else if(data[0].equals("start")) {
                	if(data.length!=2 || data[1].equals("null"))
                		throw new MapFormatException(10,"place input");
                	m.setStartPlace(m.findPlace(data[1]));
                }else if(data[0].equals("end")) {
                	if(data.length!=2 || data[1].equals("null"))
                		throw new MapFormatException(10,"place input");
                	m.setEndPlace(m.findPlace(data[1]));
                }else{
                	if(!data[0].equals("#"))
                		throw new IOException();
                }
    		}
        }
    	
    	read.close();
    	r.close();
    }
    
    //Write a representation of the Map, m, to the Writer w.
    public void write(Writer w, Map m) throws IOException
    {
    	if(!m.getPlaces().isEmpty())
    		for (Place pl : m.getPlaces())
    			w.write("place "+pl.getName()+" "+String.valueOf(pl.getX())+" "+String.valueOf(pl.getY())+"\n");
    	if(!m.getRoads().isEmpty())
    		for (Road ro : m.getRoads()) 
    			w.write("road "+ro.firstPlace().getName()+" "+ro.roadName()+" "+String.valueOf(ro.length())+" "+ro.secondPlace().getName()+"\n");
    	if(m.getStartPlace()!=null)
        	w.write("start "+m.getStartPlace().getName()+"\n");
    	if(m.getEndPlace()!=null)
        	w.write("end "+m.getEndPlace().getName()+"\n");
    	w.flush();
    }
}