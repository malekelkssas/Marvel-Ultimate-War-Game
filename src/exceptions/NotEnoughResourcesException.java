package exceptions;


//Is thrown when trying to perform any action without
//having enough resource(s) for this action
public class NotEnoughResourcesException extends GameActionException {

	public NotEnoughResourcesException() {
		super();
	}

	public NotEnoughResourcesException(String s) {
		super(s);
		
	}
}
