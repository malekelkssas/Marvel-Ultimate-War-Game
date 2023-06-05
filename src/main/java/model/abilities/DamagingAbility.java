package model.abilities;

import java.util.ArrayList;

import model.world.Damageable;

public class DamagingAbility extends Ability {
	
	private int damageAmount;
	public DamagingAbility(String name, int cost, int baseCoolDown, int castRadius, AreaOfEffect area,int required,int damageAmount) {
		super(name, cost, baseCoolDown, castRadius, area,required);
		this.damageAmount=damageAmount;
	}
	public int getDamageAmount() {
		return damageAmount;
	}
	public void setDamageAmount(int damageAmount) {
		this.damageAmount = damageAmount;
	}
	@Override
	public void execute(ArrayList<Damageable> targets) {
		for(Damageable i: targets)
		{
			i.setCurrentHP(i.getCurrentHP()-this.getDamageAmount());
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
		tmp.append("            damageAmount:"+damageAmount+'\n');
		
		return tmp.toString();
	}
	

}
