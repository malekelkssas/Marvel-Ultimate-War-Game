package model.effects;

import java.util.ArrayList;

import model.world.Champion;
import model.world.Condition;

public class Stun extends Effect {

	public Stun(int duration) {
		super("Stun", duration, EffectType.DEBUFF);
	}

	@Override
	public void apply(Champion c) throws CloneNotSupportedException {
		
		c.setCondition(Condition.INACTIVE);
		
	}

	@Override
	public void remove(Champion c) {
		//c.setCondition(Condition.ACTIVE);
		int stun =0,root=0;
		ArrayList<Effect> applied = c.getAppliedEffects();
		for(Effect tmp :applied)
		{
		 if (tmp instanceof Stun)
				stun++;
		 else if (tmp instanceof Root)
			 root++;
		}
		if(stun<1) {
			if(root>0)
				c.setCondition(Condition.ROOTED);
			else
				c.setCondition(Condition.ACTIVE);
		}
		else
			c.setCondition(Condition.INACTIVE);
	}


}
