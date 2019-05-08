import java.util.*;

public class MapImpl implements Map
{
	Set<MapListener> mapL = new HashSet<MapListener>();
	Set<Place> place = new HashSet<Place>();
	Set<Road> road = new HashSet<Road>();
	Place startP = null;
	Place endP = null;
	
    public void addListener(MapListener ml) {
    	mapL.add(ml);
    }

    public void deleteListener(MapListener ml) {
    	mapL.remove(ml);
    }

    //Create a new Place and add it to this map
    //Return the new place
    //Throws IllegalArgumentException if:S
    //  the name is not valid or is the same as that
    //  of an existing place
    //Note: A valid placeName begins with a letter, and is 
    //followed by optional letters, digits, or underscore characters
    public Place newPlace(String placeName, int xPos, int yPos) throws IllegalArgumentException {
    	PlaceInf newP = new PlaceInf();
    	if(!place.isEmpty())
    		for(Place pl : place) {
    			if(pl.getName().equals(placeName)&&pl.getX()==xPos&&pl.getY()==yPos)
    				return null;
    			if(pl.getName().equals(placeName)&&(pl.getX()!=xPos||pl.getY()!=yPos))
    				throw new IllegalArgumentException();
    		}
    	if(!newP.addPlace(placeName, xPos, yPos))
    		throw new IllegalArgumentException();
    	place.add(newP);
    	PlaceIcon pi =new PlaceIcon(newP);
		newP.addListener(pi);
		if(!mapL.isEmpty())
			for(MapListener mapP : mapL) {
        		((MapPanel)mapP).addPI(pi);
        		((MapPanel)mapP).placesChanged();
        		pi.setMP(((MapPanel)mapP));
			}
		pi.placeChanged();
    	return newP;
    }


    //Remove a place from the map
    //If the place does not exist, returns without error
    public void deletePlace(Place s) {
    	if(place.isEmpty()||!place.contains(s))
    		return;
    	if(!road.isEmpty() && road.removeAll(s.toRoads())) {
    		if(!place.isEmpty())
    			for (Place pl : place)
    				pl.toRoads().removeAll(s.toRoads());
		}
    	place.remove(s);
    	if(!mapL.isEmpty())
    		for(MapListener ml : mapL)
    			ml.placesChanged();
    }


    //Find and return the Place with the given name
    //If no place exists with given name, return NULL
    public Place findPlace(String placeName) {
    	if(!place.isEmpty())
    		for (Place pl : place)
    			if(pl.getName().equals(placeName))
    				return pl;
    	return null;
    }

    //Return a set containing all the places in this map
    public Set<Place> getPlaces() {
    	return place;
    }
    
    //Create a new Road and add it to this map
    //Returns the new road.
    //Throws IllegalArgumentException if:
    //  the firstPlace or secondPlace does not exist or
    //  the roadName is invalid or
    //  the length is negative
    //Note: A valid roadName is either the empty string, or starts
    //with a letter and is followed by optional letters and digits
    public Road newRoad(Place from, Place to, String roadName, int length) throws IllegalArgumentException {
    	RoadInf newR = new RoadInf();
    	if(place.isEmpty()||!place.contains(from)||!place.contains(to)||!newR.addRoad(from, to, roadName, length))
    		throw new IllegalArgumentException();
    	road.add(newR);
    	RoadIcon ri =new RoadIcon(newR);
		newR.addListener(ri);
		ri.roadChanged();
		if(!mapL.isEmpty())
			for(MapListener mapP : mapL) {
        		((MapPanel)mapP).addRI(ri);
        		((MapPanel)mapP).roadsChanged();
			}
    	for(Place pl : place)
    		if(pl==from || pl==to)
    			pl.toRoads().add(newR);
    	return newR;
    }

    //Remove a road r from the map
    //If the road does not exist, returns without error
    public void deleteRoad(Road r) {
    	if(road.isEmpty()||!road.contains(r))
    		return;
    	if(!place.isEmpty())
    		for (Place pl : place)
    			pl.toRoads().remove(r);
    	road.remove(r);
    	if(!mapL.isEmpty())
    		for(MapListener ml : mapL)
    			ml.roadsChanged();
    }

    //Return a set containing all the roads in this map
    public Set<Road> getRoads() {
    	return road;
    }
    
    //Set the place p as the starting place
    //If p==null, unsets the starting place
    //Throws IllegalArgumentException if the place p is not in the map
    public void setStartPlace(Place p) throws IllegalArgumentException {
    	if(place.isEmpty()||(p!=null && !place.contains(p)))
    		throw new IllegalArgumentException();
    	if(startP!=null)
    		((PlaceInf)findPlace(startP.getName())).setSE(0);
    	if(p!=null)
    		((PlaceInf)findPlace(p.getName())).setSE(1);
    	startP = p;
    	if(!mapL.isEmpty())
    		for(MapListener ml : mapL)
    			ml.otherChanged();
    }

    //Return the starting place of this map
    public Place getStartPlace() {
    	return startP;
    }

    //Set the place p as the ending place
    //If p==null, unsets the ending place
    //Throws IllegalArgumentException if the place p is not in the map
    public void setEndPlace(Place p) throws IllegalArgumentException {
    	if(place.isEmpty()||(p!=null && !place.contains(p)))
    		throw new IllegalArgumentException();
    	if(endP!=null)
    		((PlaceInf)findPlace(endP.getName())).setSE(2);
    	if(p!=null)
    		((PlaceInf)findPlace(p.getName())).setSE(3);
    	endP = p;
    	if(!mapL.isEmpty())
    		for(MapListener ml : mapL)
    			ml.otherChanged();
    }

    //Return the ending place of this map
    public Place getEndPlace() {
    	return endP;
    }

    //Causes the map to compute the shortest trip between the
    //"start" and "end" places
    //For each road on the shortest route, sets the "isChosen" property
    //to "true".
    //Returns the total distance of the trip.
    //Returns -1, if there is no route from start to end
    public int getTripDistance() {
    	return -1;
    }

    //Return a string describing this map
    //Returns a string that contains (in this order):
    //for each place in the map, a line (terminated by \n)
    //  PLACE followed the toString result for that place
    //for each road in the map, a line (terminated by \n)
    //  ROAD followed the toString result for that road
    //if a starting place has been defined, a line containing
    //  START followed the name of the starting-place (terminated by \n)
    //if an ending place has been defined, a line containing
    //  END followed the name of the ending-place (terminated by \n)
    public String toString() {
    	StringBuffer result=new StringBuffer("");
    	if(!place.isEmpty())
    		for (Place pl : place)
    			result.append("PLACE "+pl.toString()+"\n");
    	if(!road.isEmpty())
    		for (Road ro : road)
    			result.append("ROAD "+ro.toString()+"\n");
    	if(startP!=null)
    		result.append("START "+startP.getName()+"\n");
    	if(endP!=null)
    		result.append("END "+endP.getName()+"\n");
    	return result.toString();
    }
}



class PlaceInf implements Place
{
	String name = "";
	int x = 0;
	int y = 0;
	Boolean st = false;
	Boolean ed = false;
	Set<PlaceListener> placeL = new HashSet<PlaceListener>();
	Set<Road> road = new HashSet<Road>();

	//Initialize new place
	public Boolean addPlace(String placeName, int xPos, int yPos) {
     	if(!Character.isLetter(placeName.charAt(0)))
     		return false;
     	for(int i=1;i<placeName.length();i++){ 
     		char ch = placeName.charAt(i);
     		if(ch!='-'&&!Character.isLetter(ch)&&!Character.isDigit(ch))
     			return false;
     	}
 		name=placeName;
 		x=xPos;
 		y=yPos;
 		return true;
	}
	
	//set start/end place. accept an int argument, 0 for not start, 1 for is start, 2 for not end, 3 for is end.
	public void setSE(int i) {
		if(i==0)
			st=false;
		else if(i==1)
			st=true;
		else if(i==2)
			ed=false;
		else if(i==3)
			ed=true;
		if(!placeL.isEmpty())
			for(PlaceListener pl : placeL)
				pl.placeChanged();
	}
	
	//Add the PlaceListener pl to this place. 
    //Note: A place can have multiple listeners
    public void addListener(PlaceListener pl) {
    	placeL.add(pl);
    }

    //Delete the PlaceListener pl from this place.
    public void deleteListener(PlaceListener pl) {
    	placeL.remove(pl);
    }

    //Return a set containing all roads that reach this place
    public Set<Road> toRoads() {
    	return road;
    }

    //Return the road from this place to dest, if it exists
    //Returns null, if it does not
    public Road roadTo(Place dest) {
    	if(!road.isEmpty()) {
    		Iterator<Road> it = road.iterator();
    		while (it.hasNext())
    			if(it.next().firstPlace()==dest || it.next().secondPlace()==dest)
    				return it.next();
    	}
    	return null;
    }
    
    //Move the position of this place 
    //by (dx,dy) from its current position
    public void moveBy(int dx, int dy) {
    	x+=dx;
    	y+=dy;
    	if(!placeL.isEmpty())
    		for(PlaceListener pl : placeL)
    			pl.placeChanged();
    	if(!road.isEmpty())
    		for(Road ro : road)
    			((RoadInf)ro).change();
    }
    
    //Return the name of this place
    public String getName() {
    	return name;
    }
    
    //Return the X position of this place
    public int getX() {
    	return x;
    }
    
    //Return the Y position of this place
    public int getY() {
    	return y;
    }

    //Return true if this place is the starting place for a trip
    public boolean isStartPlace() {
    	return st;
    }

    //Return true if this place is the ending place for a trip
    public boolean isEndPlace() {
    	return ed;
    }

    //Return a string containing information about this place 
    //in the form (without the quotes, of course!) :
    //"placeName(xPos,yPos)"  
    public String toString() {
    	StringBuffer result=new StringBuffer("");
    	result.append(name+"("+String.valueOf(x)+","+String.valueOf(y)+")");
    	return result.toString();
    }
}

class RoadInf implements Road
{
	String name = "";
	Place fn = null;
	Place sn = null;
	int l = 0;
	Boolean chose = false;
	Set<RoadListener> roadL = new HashSet<RoadListener>();
	
	//Initialize new road
	public Boolean addRoad(Place from, Place to, String roadName, int length) {
		if(length<=0)
			return false;
		if(Character.isLetter(roadName.charAt(0))) {
			for(int i=1;i<roadName.length();i++) { 
				char ch = roadName.charAt(i);
				if(!Character.isLetter(ch)&&!Character.isDigit(ch))
					return false;
			}
			name=roadName;
	    }else if(roadName.equals("-"))
	    	name="";
	    if(from.getName().compareTo(to.getName())>0) {
	    	fn=to;
	    	sn=from;
	    }else {
	    	fn=from;
	    	sn=to;
	    }
	    l=length;
	    return true;	
	}
	
	//set chosen
	public void setChosen(Boolean i) {
		chose = i;
	}
	
	//call all the listener
	public void change() {
		if(!roadL.isEmpty())
    		for(RoadListener rl : roadL)
    			rl.roadChanged();
	}
	
    //Add the RoadListener rl to this place.
    //Note: A road can have multiple listeners
    public void addListener(RoadListener rl) {
    	roadL.add(rl);
    }

    //Delete the RoadListener rl from this place.
    public void deleteListener(RoadListener rl) {
    	roadL.remove(rl);
    }

    //Return the first place of this road
    //Note: The first place of a road is the place whose name
    //comes EARLIER in the alphabet.
    public Place firstPlace() {
    	return fn;
    }
    
    //Return the second place of this road
    //Note: The second place of a road is the place whose name
    //comes LATER in the alphabet.
    public Place secondPlace() {
    	return sn;
    }
    
    //Return true if this road is chosen as part of the current trip
    public boolean isChosen() {
    	return chose;
    }

    //Return the name of this road
    public String roadName() {
    	return name;
    }
    
    //Return the length of this road
    public int length() {
    	return l;
    }

    //Return a string containing information about this road 
    //in the form (without quotes, of course!):
    //"firstPlace(roadName:length)secondPlace"
    public String toString() {
    	StringBuffer result=new StringBuffer("");
    	result.append(fn.getName()+"("+name+":"+String.valueOf(l)+")"+sn.getName());
    	return result.toString();
    }
}