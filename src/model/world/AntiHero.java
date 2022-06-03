package model.world;

import java.util.ArrayList;

import model.abilities.Ability;
import model.effects.Effect;
import model.effects.Stun;

public class AntiHero extends Champion {

	public AntiHero(String name, int maxHP, int maxMana, int actions, int speed, int attackRange, int attackDamage) {
		super(name, maxHP, maxMana, actions, speed, attackRange, attackDamage);

	}


	@Override
	public void useLeaderAbility(ArrayList<Champion> targets) throws CloneNotSupportedException {
		for(Champion i:targets)
		{
			Effect st = new Stun(2);
			st.apply(i);
			i.getAppliedEffects().add(st);
		}
		
	}
}
