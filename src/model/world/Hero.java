package model.world;

import java.util.ArrayList;

import model.abilities.Ability;
import model.effects.Effect;
import model.effects.Embrace;

public class Hero extends Champion {

	public Hero(String name, int maxHP, int maxMana, int actions, int speed, int attackRange, int attackDamage) {
		super(name, maxHP, maxMana, actions, speed, attackRange, attackDamage);

	}


	
	public void useLeaderAbility(ArrayList<Champion> targets) throws CloneNotSupportedException {
		for(Champion i:targets)
		{
			ArrayList<Effect> applied = i.getAppliedEffects();
			ArrayList<Integer> removeindex = new ArrayList<>();
			int c=0;
			for(Effect j : applied)
			{
				if(j.getType().name().equals("DEBUFF"))
				{
					removeindex.add(c);
				}
				c++;
			}
			 c=0;
			for(int j:removeindex)
			{
				
				Effect effect =applied.remove(j-c);
				effect.remove(i);
				c++;
			}
			Effect tmp = new Embrace(2);
			tmp.apply(i);
			i.getAppliedEffects().add(tmp);
		}
		
	}

	
}
