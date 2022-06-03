package model.effects;

import java.util.ArrayList;

import model.abilities.Ability;
import model.abilities.AreaOfEffect;
import model.abilities.DamagingAbility;
import model.abilities.HealingAbility;
import model.world.Champion;
import model.world.Condition;

public abstract class Effect implements Cloneable {
	private String name;
	private EffectType type;
	private int duration;

	@Override
	public Object clone() throws CloneNotSupportedException {
		
		return super.clone();
	}
	public Effect(String name, int duration, EffectType type) {
		this.name = name;
		this.type = type;
		this.duration = duration;
	}

	public String getName() {
		return name;
	}

	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}

	public EffectType getType() {
		return type;
	}
	
	public abstract void apply(Champion c) throws CloneNotSupportedException;
	public abstract void remove(Champion c) ;
	
public String toString()
{
return name+":   duration : "+duration;	
}
}
