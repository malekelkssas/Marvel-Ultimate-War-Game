package model.abilities;

import java.util.ArrayList;

import model.world.Champion;
import model.world.Damageable;

public  class HealingAbility extends Ability {
	private int healAmount;

	public HealingAbility(String name,int cost, int baseCoolDown, int castRadius, AreaOfEffect area,int required, int healingAmount) {
		super(name,cost, baseCoolDown, castRadius, area,required);
		this.healAmount = healingAmount;
	}

	public int getHealAmount() {
		return healAmount;
	}

	public void setHealAmount(int healAmount) {
		this.healAmount = healAmount;
	}

	@Override
	public void execute(ArrayList<Damageable> targets) {
		for(Damageable i: targets)
		{
			i.setCurrentHP(i.getCurrentHP()+this.getHealAmount());
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
	tmp.append("            healAmount:"+healAmount+'\n');
	
	return tmp.toString();
	}
	

}
