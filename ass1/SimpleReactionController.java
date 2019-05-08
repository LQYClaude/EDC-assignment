public class SimpleReactionController implements Controller
{
	private int state;
	private int timer;
	Gui screen;
	Random ran;
	
    //Connect controller to screen
    public void connect(Gui gui, Random rng) {
    	screen = gui;
    	ran = rng;
    }

    //Called to initialise the controller
    public void init() {
    	state = 0;
    	timer = 0;
    	screen.setDisplay("Insert coin");
    }

    //Called whenever a coin is inserted into the machine
    public void coinInserted() {
    	if(state == 0){
    		screen.setDisplay("Press Go!");
    		state ++;
    		timer = 1000;
    	}else if(state == 4) {
    		screen.setDisplay("Press Go!");
    		state = 1;
    		timer = 1000;
    	}
    }

    //Called whenever the go/stop button is pressed
    public void goStopPressed() {
    	if(state == 1) {
    		screen.setDisplay("Wait...");
    		state ++;
    		timer = ran.getRandom(100,250);
    	}else if(state == 2) {
    		screen.setDisplay("Insert coin");
    		state = 0;
    	}else if(state == 3) {
    		String show = Integer.toString(timer/100)+".";
    		if(timer%100 >9){
    		  show += Integer.toString(timer%100);
    		}else{
    		  show += "0"+Integer.toString(timer%100);
    		}
    		screen.setDisplay(show);
    		state++;
    		timer = 300;
    	}else if(state == 4) {
    		screen.setDisplay("Insert coin");
    		state = 0;
    		timer = 0;
    	}
    }

    //Called to deliver a TICK to the controller
    public void tick() {
    	if(state == 3) {
    		timer ++;
    		String show = Integer.toString(timer/100)+".";
    		if(timer%100 >9){
    		  show += Integer.toString(timer%100);
    		}else{
    		  show += "0"+Integer.toString(timer%100);
    		}
    		screen.setDisplay(show);
    		if(timer == 200) {
        		screen.setDisplay("2.00");
    			state++;
    			timer = 300; 
    		}
    	}else {
    		timer --;
    	}
    	
    	if(timer == 0) {
    		if(state == 1) {
        		screen.setDisplay("Insert coin");
    			state = 0;
    		}else if(state == 2) {
        		screen.setDisplay("0.00"); 
    			state ++;
    		}else if(state == 4) {
    			screen.setDisplay("Insert coin");
    			state = 0;
    		}
    	}
    }
}
