package model.effects;

import java.util.ArrayList;

import model.world.Champion;
import model.world.Condition;

public class Root extends Effect {

	public Root( int duration) {
		super("Root", duration, EffectType.DEBUFF);
		
	}

	@Override
	public void apply(Champion c) throws CloneNotSupportedException {
		if(c.getCondition().name().equals("ACTIVE"))
			c.setCondition(Condition.ROOTED);
		
		
	}

	
	public void remove(Champion c) {
		int root =0;
		ArrayList<Effect> applied = c.getAppliedEffects();
		//System.out.println(applied);
		for(Effect i : applied)
			if(i instanceof Root) {
				root++;
				break;
			}
		if(c.getCondition().name().equals("ROOTED")&&root<1)
			c.setCondition(Condition.ACTIVE);
	
	}

}
