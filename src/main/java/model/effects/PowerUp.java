package model.effects;

import java.util.ArrayList;

import model.abilities.Ability;
import model.abilities.DamagingAbility;
import model.abilities.HealingAbility;
import model.world.Champion;

public class PowerUp extends Effect {
	

	public PowerUp(int duration) {
		super("PowerUp", duration, EffectType.BUFF);
		
	}

	@Override
	public void apply(Champion c) throws CloneNotSupportedException {
		ArrayList<Ability> abilities = c.getAbilities();
		for(Ability i: abilities)
		{
			if(i instanceof DamagingAbility)
			{
				((DamagingAbility) i).setDamageAmount((int) (((DamagingAbility) i).getDamageAmount()*1.2));
			}
			else if (i instanceof HealingAbility)
			{
				((HealingAbility)i).setHealAmount((int) (((HealingAbility)i).getHealAmount()*1.2));
			}
		}
		
	}

	
	public void remove(Champion c) {
		
		ArrayList<Ability> abilities = c.getAbilities();
		for(Ability i: abilities)
		{
			if(i instanceof DamagingAbility)
			{
				((DamagingAbility) i).setDamageAmount((int) (((DamagingAbility) i).getDamageAmount()/1.2));
			}
			else if (i instanceof HealingAbility)
			{
				((HealingAbility)i).setHealAmount((int) (((HealingAbility)i).getHealAmount()/1.2));
			}
		}
	}
	
}
