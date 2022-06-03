package model.effects;

import java.util.ArrayList;

import model.abilities.Ability;
import model.abilities.AreaOfEffect;
import model.abilities.DamagingAbility;
import model.world.Champion;

public class Disarm extends Effect {
	

	public Disarm( int duration) {
		super("Disarm", duration, EffectType.DEBUFF);
		
	}

	
	public void apply(Champion c) throws CloneNotSupportedException{
		
		c.getAbilities().add(new DamagingAbility("Punch",0,1,1,AreaOfEffect.SINGLETARGET,1,50));
		
	}

	
	public void remove(Champion c)   {
		
		ArrayList<Ability> abi = c.getAbilities();
		int size = abi.size();
		for(int j=0;j!=size;j++)
		{
			if(abi.get(j) instanceof DamagingAbility&&abi.get(j).getName().equals("Punch"))
			{
				abi.remove(j);
				break;
			}
		}
	}
	
}
