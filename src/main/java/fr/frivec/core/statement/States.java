package fr.frivec.core.statement;

public enum States {

	WAIT(),
	GAME(),
	END();
	
	private static States currentState;
	
	public static States getCurrentState() {
		return currentState;
	}
	
	public static void setCurrentState(States currentState) {
		States.currentState = currentState;
	}
	
	public static boolean isState(final States states) {
		
		if(getCurrentState().equals(states))
			
			return true;
		
		return false;
		
	}
	
}
