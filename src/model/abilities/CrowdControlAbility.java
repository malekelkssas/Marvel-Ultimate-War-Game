package model.abilities;

import java.util.ArrayList;

import model.effects.Effect;
import model.world.Champion;
import model.world.Damageable;

public class CrowdControlAbility extends Ability {
	private Effect effect;

	public CrowdControlAbility(String name, int cost, int baseCoolDown, int castRadius, AreaOfEffect area, int required,
			Effect effect) {
		super(name, cost, baseCoolDown, castRadius, area, required);
		this.effect = effect;

	}

	public Effect getEffect() {
		return effect;
	}

	@Override
	public void execute(ArrayList<Damageable> targets) throws CloneNotSupportedException {
		
		for(Damageable i: targets)
		{
				this.getEffect().apply((Champion)i);
				((Champion)i).getAppliedEffects().add((Effect) this.getEffect().clone());
			
		}
	}
	public String toString()
	{
		StringBuilder tmp = new StringBuilder();
		tmp.append("        "+this.getName()+":  CrowdControlAbility"+'\n');
		tmp.append("            cast area:"+super.getCastArea().name()+'\n');
		tmp.append("            cast range:"+super.getCastRange()+'\n');
		tmp.append("            mana:"+super.getManaCost()+'\n');
		tmp.append("            action cost:"+super.getRequiredActionPoints()+'\n');
		tmp.append("            current cooldown:"+super.getCurrentCooldown()+'\n');
		tmp.append("            base cooldown:"+super.getBaseCooldown()+'\n');
		tmp.append("            effect:"+effect.toString()+'\n');
		
		return tmp.toString();
	}

}
