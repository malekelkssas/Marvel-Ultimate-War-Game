package exceptions;

//Is thrown when a champion is trying to move while violating
//the move regulations.


@SuppressWarnings("serial")
public class UnallowedMovementException extends GameActionException {

	public UnallowedMovementException() {
		super();
	}

	public UnallowedMovementException(String s) {
		super(s);
	}

}
