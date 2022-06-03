package model.world;

import java.util.ArrayList;

import model.abilities.Ability;

public class Villain extends Champion {

	public Villain(String name, int maxHP, int maxMana, int actions, int speed, int attackRange, int attackDamage) {
		super(name, maxHP, maxMana, actions, speed, attackRange, attackDamage);

	}

	
	public void useLeaderAbility(ArrayList<Champion> targets) {
		
		for(Champion i:targets)
		{
			//System.out.println(i.getName()+" "+i.getCurrentHP()+" "+i.getMaxHP());
			//if(i.getCurrentHP()<i.getMaxHP()*0.3) {
				i.setCondition(Condition.KNOCKEDOUT);
				i.setCurrentHP(0);
			//}
		}
		
	}

	
}
