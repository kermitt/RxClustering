package playground;

import java.math.BigDecimal;

import healthpath.common.Caller;

public class Scratchpad {

	enum Day {
	    SUNDAY, MONDAY, TUESDAY, WEDNESDAY,
	    THURSDAY, FRIDAY, SATURDAY 
	}

	public Day day; 
	 public Scratchpad(Day day) {
	        this.day = day;
	    }
	    
	    public void tellItLikeItIs() {
	        switch (day) {
	            case MONDAY:
	                System.out.println("Mondays are bad.");
	                break;
	                    
	            case FRIDAY:
	                System.out.println("Fridays are better.");
	                break;
	                         
	            case SATURDAY: case SUNDAY:
	                System.out.println("Weekends are best.");
	                break;
	                        
	            default:
	                System.out.println("Midweek days are so-so.");
	                break;
	        }
	    }
	    
	    public static void main(String[] args) {
	        Scratchpad firstDay = new Scratchpad(Day.MONDAY);
	        firstDay.tellItLikeItIs();
	        Scratchpad thirdDay = new Scratchpad(Day.WEDNESDAY);
	        thirdDay.tellItLikeItIs();
	        Scratchpad fifthDay = new Scratchpad(Day.FRIDAY);
	        fifthDay.tellItLikeItIs();
	        Scratchpad sixthDay = new Scratchpad(Day.SATURDAY);
	        sixthDay.tellItLikeItIs();
	        Scratchpad seventhDay = new Scratchpad(Day.SUNDAY);
	        seventhDay.tellItLikeItIs();
	    }
	
}
