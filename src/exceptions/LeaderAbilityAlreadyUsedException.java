package exceptions;

//Is thrown when the leader champion of any player
//tries to use his leader ability after it has already been used before. Recall that every leader
//can only use his leader ability once per game

@SuppressWarnings("serial")
public class LeaderAbilityAlreadyUsedException extends GameActionException {

	public LeaderAbilityAlreadyUsedException() {
		super();
	}

	public LeaderAbilityAlreadyUsedException(String s) {
		super(s);
	}

}
