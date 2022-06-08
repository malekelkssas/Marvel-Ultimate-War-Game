package application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

import engine.Game;
import exceptions.AbilityUseException;
import exceptions.ChampionDisarmedException;
import exceptions.InvalidTargetException;
import exceptions.LeaderAbilityAlreadyUsedException;
import exceptions.LeaderNotCurrentException;
import exceptions.NotEnoughResourcesException;
import exceptions.UnallowedMovementException;
import model.abilities.Ability;
import model.abilities.AreaOfEffect;
import model.abilities.CrowdControlAbility;
import model.abilities.DamagingAbility;
import model.abilities.HealingAbility;
import model.effects.Disarm;
import model.effects.Effect;
import model.effects.EffectType;
import model.effects.Silence;
import model.world.Champion;
import model.world.Cover;
import model.world.Damageable;
import model.world.Direction;
import model.world.Hero;
import model.world.Villain;

public class computer {
	private static Game game;
	private static TreeMap<Integer,Direction> inattackrange;
	private static TreeMap<Integer,Direction> inattackcover;
	private static boolean attack; 
	private static boolean attackability;
	private static boolean healingability;
	private static boolean crowdcontrolability;
	private static boolean leaderab;
	private static boolean move;
	private static TreeMap<Integer,Pair> damageabilities;
	private static TreeMap<Integer,Pair> healingabilities;
	private static TreeMap<Integer,Pair> crowdcontrolabilities;
	private static Champion currentchampion;
	public static boolean play(Game originalgame) throws LeaderAbilityAlreadyUsedException, LeaderNotCurrentException, CloneNotSupportedException, AbilityUseException, NotEnoughResourcesException, InvalidTargetException, ChampionDisarmedException, UnallowedMovementException, InterruptedException, IOException
	{
		game = originalgame;
		inattackrange = new TreeMap<>();
		 inattackcover = new TreeMap<>();
		 damageabilities = new TreeMap<>();
		 healingabilities = new TreeMap<>();
		 crowdcontrolabilities = new TreeMap<>();
		currentchampion = game.getCurrentChampion();
		attack=false;
		attackability = false;
		healingability = false;
		crowdcontrolability = false;
		leaderab = false;
		move = false;
		useleaderab();
		if(leaderab)
		{
			alerts.display(currentchampion.getName()+'\n'+"use leader ability", "computer action");
			game.useLeaderAbility();
		}
		else
		{
			healingability();
			if(healingability)
			{
				alerts.display(currentchampion.getName()+'\n'+"use the healing ability : "+'\n'+healingabilities.get(healingabilities.lastKey()).x.getName() , "computer action");
				if(healingabilities.get(healingabilities.lastKey()).y == null)
					game.castAbility(healingabilities.get(healingabilities.lastKey()).x);
				else if(healingabilities.get(healingabilities.lastKey()).y instanceof Direction)
				{
					game.castAbility(healingabilities.get(healingabilities.lastKey()).x,(Direction) healingabilities.get(healingabilities.lastKey()).y);
				}
				else
				{
					game.castAbility(healingabilities.get(healingabilities.lastKey()).x,((pair2)healingabilities.get(healingabilities.lastKey()).y).x,((pair2)healingabilities.get(healingabilities.lastKey()).y).y);
				}
			}
			else
			{
				damageability();
				if(attackability)
				{
					alerts.display(currentchampion.getName()+'\n'+"use the damaging ability : "+'\n'+damageabilities.get(damageabilities.lastKey()).x.getName() , "computer action");
					if(damageabilities.get(damageabilities.lastKey()).y == null)
						game.castAbility(damageabilities.get(damageabilities.lastKey()).x);
					else if(damageabilities.get(damageabilities.lastKey()).y instanceof Direction)
					{
						game.castAbility(damageabilities.get(damageabilities.lastKey()).x,(Direction) damageabilities.get(damageabilities.lastKey()).y);
					}
					else
					{
						game.castAbility(damageabilities.get(damageabilities.lastKey()).x,((pair2)damageabilities.get(damageabilities.lastKey()).y).x,((pair2)damageabilities.get(damageabilities.lastKey()).y).y);
					}
				}
				else
				{
					crowdcontrolability();
					if(crowdcontrolability)
					{
						alerts.display(currentchampion.getName()+'\n'+"use the crowd control ability : "+'\n'+crowdcontrolabilities.get(crowdcontrolabilities.lastKey()).x.getName() , "computer action");
						if(crowdcontrolabilities.get(crowdcontrolabilities.lastKey()).y == null)
							game.castAbility(crowdcontrolabilities.get(crowdcontrolabilities.lastKey()).x);
						else if(crowdcontrolabilities.get(crowdcontrolabilities.lastKey()).y instanceof Direction)
						{
							game.castAbility(crowdcontrolabilities.get(crowdcontrolabilities.lastKey()).x,(Direction) crowdcontrolabilities.get(crowdcontrolabilities.lastKey()).y);
						}
						else
						{
							game.castAbility(crowdcontrolabilities.get(crowdcontrolabilities.lastKey()).x,((pair2)crowdcontrolabilities.get(crowdcontrolabilities.lastKey()).y).x,((pair2)crowdcontrolabilities.get(crowdcontrolabilities.lastKey()).y).y);
						}
					}
					else
					{
						getinattackrange();
						if(attack && inattackrange.size()!=0)
						{
							alerts.display(currentchampion.getName()+'\n'+"attack : "+'\n'+inattackrange.get(inattackrange.firstKey()).name() , "computer action");
							game.attack(inattackrange.get(inattackrange.firstKey()));
						}
						else
						{
							move();
							if(move)
							{
								alerts.display(currentchampion.getName()+'\n'+"move : "+'\n'+m.get(0).name() , "computer action");
								game.move(m.get(0));
							}
							else
							{
								if(attack && inattackcover.size()!=0)
								{
									alerts.display(currentchampion.getName()+'\n'+"attack : "+'\n'+inattackcover.get(inattackcover.firstKey()).name() , "computer action");
									game.attack(inattackcover.get(inattackcover.firstKey()));
								}
							}
						}
					}
					
				}
			}
		}
		return attack |attackability | healingability | crowdcontrolability |leaderab |move;
	}
	private static void getinattackrange()
	{
		for(Effect i:currentchampion.getAppliedEffects())
			if(i instanceof Disarm)
				return;
		if(currentchampion.getCurrentActionPoints()<2)
			return;
		int i=currentchampion.getLocation().x+1;
		int c=1;
		for(;i<5&&c<=currentchampion.getAttackRange();i++,c++)
		{
			if(game.getBoard()[i][currentchampion.getLocation().y]!=null)
			{
				if(game.getBoard()[i][currentchampion.getLocation().y] instanceof Cover)
					inattackcover.put(((Cover) game.getBoard()[i][currentchampion.getLocation().y]).getCurrentHP(),Direction.UP);
				else
					if(!game.checkfriend(game.getCurrentChampion(), ((Champion) game.getBoard()[i][currentchampion.getLocation().y])))
						inattackrange.put(((Champion) game.getBoard()[i][currentchampion.getLocation().y]).getCurrentHP(),Direction.UP);
				break;
			}
		}
		i=currentchampion.getLocation().x-1;
		c=1;
		for(;i>-1&&c<=currentchampion.getAttackRange();i--,c++)
		{
			if(game.getBoard()[i][currentchampion.getLocation().y]!=null)
			{
				if(game.getBoard()[i][currentchampion.getLocation().y] instanceof Cover)
					inattackcover.put(((Cover) game.getBoard()[i][currentchampion.getLocation().y]).getCurrentHP(),Direction.DOWN);
				else {
					if(!game.checkfriend(game.getCurrentChampion(), ((Champion) game.getBoard()[i][currentchampion.getLocation().y])))
					inattackrange.put(((Champion) game.getBoard()[i][currentchampion.getLocation().y]).getCurrentHP(),Direction.DOWN);
				}
				break;
			}
		}
		i=currentchampion.getLocation().y-1;
		c=1;
		for(;i>-1&&c<=currentchampion.getAttackRange();i--,c++)
		{
			if(game.getBoard()[currentchampion.getLocation().x][i]!=null)
			{
				
				if(game.getBoard()[currentchampion.getLocation().x][i] instanceof Cover)
					inattackcover.put(((Cover) game.getBoard()[currentchampion.getLocation().x][i]).getCurrentHP(),Direction.LEFT);
				else {
					if(!game.checkfriend(game.getCurrentChampion(), ((Champion) game.getBoard()[currentchampion.getLocation().x][i])))
					inattackrange.put(((Champion) game.getBoard()[currentchampion.getLocation().x][i]).getCurrentHP(),Direction.LEFT);
				}
				break;
				
			}
		}
		i=currentchampion.getLocation().y+1;
		c=1;
		for(;i<5&&c<=currentchampion.getAttackRange();i++,c++)
		{
			if(game.getBoard()[currentchampion.getLocation().x][i]!=null)
			{
				if(game.getBoard()[currentchampion.getLocation().x][i] instanceof Cover)
					inattackcover.put(((Cover) game.getBoard()[currentchampion.getLocation().x][i]).getCurrentHP(),Direction.RIGHT);
				else {
					if(!game.checkfriend(game.getCurrentChampion(), ((Champion) game.getBoard()[currentchampion.getLocation().x][i])))
					inattackrange.put(((Champion) game.getBoard()[currentchampion.getLocation().x][i]).getCurrentHP(),Direction.RIGHT);
				}
				break;
			}
		}
		if(inattackcover.size()!=0|| inattackrange.size()!=0)
			attack = true;
	}
	private static void damageability()
	{
		for(Effect i:currentchampion.getAppliedEffects())
			if(i instanceof Silence)
				return;
		for(Ability i : currentchampion.getAbilities())
		{
			if(!(i instanceof DamagingAbility)||i.getCurrentCooldown()!=0||(currentchampion.getCurrentActionPoints()<i.getRequiredActionPoints())||(currentchampion.getMana()<i.getManaCost()))
				continue;
			if(i.getCastArea().equals(AreaOfEffect.SURROUND))
					{
				int all=0;
				int k = currentchampion.getLocation().x-1;
				for(;k<=currentchampion.getLocation().x+1;k++)
				{
					for(int j = currentchampion.getLocation().y-1;j<=currentchampion.getLocation().y+1;j++)
					{
						if(k<0||k>=5||j<0||j>=5||(k==currentchampion.getLocation().x&&j==currentchampion.getLocation().y))
							continue;
						else
							if(game.getBoard()[k][j]!=null&&game.getBoard()[k][j] instanceof Champion &&!game.checkfriend(currentchampion, (Champion) game.getBoard()[k][j]) )
								all++;
					}
				}
				if(all!=0) {
					attackability = true;
				damageabilities.put(all*((DamagingAbility)i).getDamageAmount(),new Pair(((DamagingAbility)i),null));
				}
					}
			else if(i.getCastArea().equals(AreaOfEffect.TEAMTARGET))
			{
				int all=0;
				for(int j=0;j!=5;j++)
				{
					for(int k=0;k!=5;k++)
					{
						if(game.getBoard()[j][k]!=null)
						{
							if(Math.abs(currentchampion.getLocation().x-j)+(Math.abs(currentchampion.getLocation().y-k))<=i.getCastRange())
							{
								if(game.getBoard()[j][k] instanceof Champion) {
								if(!game.checkfriend(currentchampion,(Champion) game.getBoard()[j][k]))
									all++;
								}
							}
						}
					}
				}
				if(all!=0) {
					attackability = true;
				damageabilities.put(all*((DamagingAbility)i).getDamageAmount(),new Pair(((DamagingAbility)i),null));
				}
			}
			else if(i.getCastArea().equals(AreaOfEffect.DIRECTIONAL))
			{
				int up = 0;
				int j=currentchampion.getLocation().x+1;
				int c=1;
				for(;j<5&&c<=i.getCastRange();j++,c++)
					if(game.getBoard()[j][currentchampion.getLocation().y]!=null)
					{
						if(game.getBoard()[j][currentchampion.getLocation().y] instanceof Champion &&!game.checkfriend(currentchampion,(Champion) game.getBoard()[j][currentchampion.getLocation().y]))
						{
							up++;
						}
					}
				int down =0;
				j=currentchampion.getLocation().x-1;
				c=1;
				for(;j>-1&&c<=i.getCastRange();j--,c++)
					if(game.getBoard()[j][currentchampion.getLocation().y]!=null)
					{
						if(game.getBoard()[j][currentchampion.getLocation().y] instanceof Champion &&!game.checkfriend(currentchampion,(Champion) game.getBoard()[j][currentchampion.getLocation().y]))
						{
							down++;
						}
					}
				int right=0;
				j = currentchampion.getLocation().y+1;
				c = 1;
				for(;j<5&&c<=i.getCastRange();j++,c++)
					if(game.getBoard()[currentchampion.getLocation().x][j]!=null)
					{
						if(game.getBoard()[currentchampion.getLocation().x][j] instanceof Champion &&!game.checkfriend(currentchampion,(Champion) game.getBoard()[currentchampion.getLocation().x][j]))
						{
							right++;
						}
					}
				int left =0;
				j = currentchampion.getLocation().y-1;
				c = 1;
				for(;j>-1&&c<=i.getCastRange();j--,c++)
					if(game.getBoard()[currentchampion.getLocation().x][j]!=null)
					{
						if(game.getBoard()[currentchampion.getLocation().x][j] instanceof Champion &&!game.checkfriend(currentchampion,(Champion) game.getBoard()[currentchampion.getLocation().x][j]))
						{
							left++;
						}
					}
				if(up!=0||right!=0||left!=0||down!=0)
				{
					attackability = true;
					
					int t = (Math.max(Math.max(up, down), Math.max(right, left)));
					if(t==up)
						damageabilities.put(up*((DamagingAbility)i).getDamageAmount(),new Pair(((DamagingAbility)i),Direction.UP));
					else if(t==down)
						damageabilities.put(down*((DamagingAbility)i).getDamageAmount(),new Pair(((DamagingAbility)i),Direction.DOWN));
					else if (t==right)
						damageabilities.put(right*((DamagingAbility)i).getDamageAmount(),new Pair(((DamagingAbility)i),Direction.RIGHT));
					else
						damageabilities.put(left*((DamagingAbility)i).getDamageAmount(),new Pair(((DamagingAbility)i),Direction.LEFT));
				}
			}
			else
			{
				int all=Integer.MAX_VALUE;
				pair2 tm = null;
				for(int j=0;j!=5;j++)
				{
					for(int k=0;k!=5;k++)
					{
						if(game.getBoard()[j][k]!=null)
						{
							if(Math.abs(currentchampion.getLocation().x-j)+(Math.abs(currentchampion.getLocation().y-k))<=i.getCastRange())
							{
								if(game.getBoard()[j][k] instanceof Champion &&!game.checkfriend(currentchampion,(Champion) game.getBoard()[j][k]))
								{
									int tmp = all;
									all = Math.min(all,((Damageable)game.getBoard()[j][k]).getCurrentHP());
									if(tmp!=all)
									{
										tm = new pair2(j,k);
									}
								}
							}
						}
					}
				}
				if(all !=Integer.MAX_VALUE)
				{
					attackability=true;
					damageabilities.put(((DamagingAbility)i).getDamageAmount(),new Pair(((DamagingAbility)i),tm));
				}
			}
		}
	}
	private static void healingability()
	{
			for(Effect i:currentchampion.getAppliedEffects())
				if(i instanceof Silence)
					return;
			for(Ability i : currentchampion.getAbilities())
			{
				if(!(i instanceof HealingAbility)||i.getCurrentCooldown()!=0||(currentchampion.getCurrentActionPoints()<i.getRequiredActionPoints())||(currentchampion.getMana()<i.getManaCost()))
					continue;
				if(i.getCastArea().equals(AreaOfEffect.SURROUND))
				{
			int all=0;
			int k = currentchampion.getLocation().x-1;
			for(;k<=currentchampion.getLocation().x+1;k++)
			{
				for(int j = currentchampion.getLocation().y-1;j<=currentchampion.getLocation().y+1;j++)
				{
					if(k<0||k>=5||j<0||j>=5||(k==currentchampion.getLocation().x&&j==currentchampion.getLocation().y))
						continue;
					else
						if(game.getBoard()[k][j]!=null&&game.getBoard()[k][j] instanceof Champion &&game.checkfriend(currentchampion, (Champion) game.getBoard()[k][j]) )
							all+= ((Champion) game.getBoard()[j][k]).getMaxHP()-((((HealingAbility)i).getHealAmount()+((Champion) game.getBoard()[j][k]).getCurrentHP())>((Champion) game.getBoard()[j][k]).getMaxHP()?((Champion) game.getBoard()[j][k]).getMaxHP():(((HealingAbility)i).getHealAmount()+((Champion) game.getBoard()[j][k]).getCurrentHP()));
				}
			}
			if(all!=0) {
				healingability = true;
				healingabilities.put(all,new Pair(((HealingAbility)i),null));
			}
			}
				if(i.getCastArea().equals(AreaOfEffect.TEAMTARGET))
				{
					int all=0;
					for(int j=0;j!=5;j++)
					{
						for(int k=0;k!=5;k++)
						{
							if(game.getBoard()[j][k]!=null && !(game.getBoard()[j][k] instanceof Cover))
							{
								if(Math.abs(currentchampion.getLocation().x-j)+(Math.abs(currentchampion.getLocation().y-k))<=i.getCastRange())
								{
									if(game.checkfriend(currentchampion,(Champion) game.getBoard()[j][k]))
										all+= ((Champion) game.getBoard()[j][k]).getMaxHP()-((((HealingAbility)i).getHealAmount()+((Champion) game.getBoard()[j][k]).getCurrentHP())>((Champion) game.getBoard()[j][k]).getMaxHP()?((Champion) game.getBoard()[j][k]).getMaxHP():(((HealingAbility)i).getHealAmount()+((Champion) game.getBoard()[j][k]).getCurrentHP()));
								}
							}
						}
					}
					if(all!=0) {
						healingability = true;
						healingabilities.put(all,new Pair(((HealingAbility)i),null));
					}
				}
				else if(i.getCastArea().equals(AreaOfEffect.DIRECTIONAL))
				{
					int up = 0;
					int j=currentchampion.getLocation().x+1;
					int c=1;
					for(;j<5&&c<=i.getCastRange();j++,c++)
						if(game.getBoard()[j][currentchampion.getLocation().y]!=null)
						{
							if(game.getBoard()[j][currentchampion.getLocation().y] instanceof Champion &&game.checkfriend(currentchampion,(Champion) game.getBoard()[j][currentchampion.getLocation().y]))
							{
								up+=((Champion) game.getBoard()[j][currentchampion.getLocation().y]).getMaxHP()-((((HealingAbility)i).getHealAmount()+((Champion) game.getBoard()[j][currentchampion.getLocation().y]).getCurrentHP())>((Champion) game.getBoard()[j][currentchampion.getLocation().y]).getMaxHP()?((Champion) game.getBoard()[j][currentchampion.getLocation().y]).getMaxHP():(((HealingAbility)i).getHealAmount()+((Champion) game.getBoard()[j][currentchampion.getLocation().y]).getCurrentHP()));
							}
						}
					int down =0;
					j=currentchampion.getLocation().x-1;
					c=1;
					for(;j>-1&&c<=i.getCastRange();j--,c++)
						if(game.getBoard()[j][currentchampion.getLocation().y]!=null)
						{
							if(game.getBoard()[j][currentchampion.getLocation().y] instanceof Champion &&game.checkfriend(currentchampion,(Champion) game.getBoard()[j][currentchampion.getLocation().y]))
							{
								down+= ((Champion) game.getBoard()[j][currentchampion.getLocation().y]).getMaxHP()-((((HealingAbility)i).getHealAmount()+((Champion) game.getBoard()[j][currentchampion.getLocation().y]).getCurrentHP())>((Champion) game.getBoard()[j][currentchampion.getLocation().y]).getMaxHP()?((Champion) game.getBoard()[j][currentchampion.getLocation().y]).getMaxHP():(((HealingAbility)i).getHealAmount()+((Champion) game.getBoard()[j][currentchampion.getLocation().y]).getCurrentHP()));
							}
						}
					int right=0;
					j = currentchampion.getLocation().y+1;
					c = 1;
					for(;j<5&&c<=i.getCastRange();j++,c++)
						if(game.getBoard()[currentchampion.getLocation().x][j]!=null)
						{
							if(game.getBoard()[currentchampion.getLocation().x][j] instanceof Champion &&game.checkfriend(currentchampion,(Champion) game.getBoard()[currentchampion.getLocation().x][j]))
							{
								right+=((Champion) game.getBoard()[currentchampion.getLocation().x][j]).getMaxHP()-((((HealingAbility)i).getHealAmount()+((Champion) game.getBoard()[currentchampion.getLocation().x][j]).getCurrentHP())>((Champion) game.getBoard()[currentchampion.getLocation().x][j]).getMaxHP()?((Champion) game.getBoard()[currentchampion.getLocation().x][j]).getMaxHP():(((HealingAbility)i).getHealAmount()+((Champion) game.getBoard()[currentchampion.getLocation().x][j]).getCurrentHP()));
							}
						}
					int left =0;
					j = currentchampion.getLocation().y-1;
					c = 1;
					for(;j>-1&&c<=i.getCastRange();j--,c++)
						if(game.getBoard()[currentchampion.getLocation().x][j]!=null)
						{
							if(game.getBoard()[currentchampion.getLocation().x][j] instanceof Champion &&game.checkfriend(currentchampion,(Champion) game.getBoard()[currentchampion.getLocation().x][j]))
							{
								left+= ((Champion) game.getBoard()[currentchampion.getLocation().x][j]).getMaxHP()-((((HealingAbility)i).getHealAmount()+((Champion) game.getBoard()[currentchampion.getLocation().x][j]).getCurrentHP())>((Champion) game.getBoard()[currentchampion.getLocation().x][j]).getMaxHP()?((Champion) game.getBoard()[currentchampion.getLocation().x][j]).getMaxHP():(((HealingAbility)i).getHealAmount()+((Champion) game.getBoard()[currentchampion.getLocation().x][j]).getCurrentHP()));
							}
						}
					if(up!=0||right!=0||left!=0||down!=0)
					{
						healingability = true;
						
						int t = (Math.max(Math.max(up, down), Math.max(right, left)));
						if(t==up)
							healingabilities.put(up,new Pair(((HealingAbility)i),Direction.UP));
						else if(t==down)
							healingabilities.put(down,new Pair(((HealingAbility)i),Direction.DOWN));
						else if (t==right)
							healingabilities.put(right,new Pair(((HealingAbility)i),Direction.RIGHT));
						else
							healingabilities.put(left,new Pair(((HealingAbility)i),Direction.LEFT));
					}
				}
				else
				{
					int all=Integer.MIN_VALUE;
					pair2 tm = null;
					for(int j=0;j!=5;j++)
					{
						for(int k=0;k!=5;k++)
						{
							if(game.getBoard()[j][k]!=null)
							{
								if(Math.abs(currentchampion.getLocation().x-j)+(Math.abs(currentchampion.getLocation().y-k))<=i.getCastRange())
								{
									 if(game.getBoard()[j][k] instanceof Champion &&game.checkfriend(currentchampion,(Champion) game.getBoard()[j][k]))
									{
										 int tmp = all;
										all = Math.max(all,((Champion) game.getBoard()[j][k]).getMaxHP()-((((HealingAbility)i).getHealAmount()+((Champion) game.getBoard()[j][k]).getCurrentHP())>((Champion) game.getBoard()[j][k]).getMaxHP()?((Champion) game.getBoard()[j][k]).getMaxHP():(((HealingAbility)i).getHealAmount()+((Champion) game.getBoard()[j][k]).getCurrentHP())));
										if(tmp !=all)
											tm = new pair2(j,k);
									}
								}
							}
						}
					}
					if(all !=Integer.MIN_VALUE&& all!=0)
					{
						healingability=true;
						healingabilities.put(all,new Pair(((HealingAbility)i),tm));
					}
				}
			}
	}
	private static void crowdcontrolability()
	{
		for(Effect i:currentchampion.getAppliedEffects())
			if(i instanceof Silence)
				return;
		for(Ability i : currentchampion.getAbilities())
		{
			if(!(i instanceof CrowdControlAbility)||i.getCurrentCooldown()!=0||(currentchampion.getCurrentActionPoints()<i.getRequiredActionPoints())||(currentchampion.getMana()<i.getManaCost()))
				continue;
			if(((CrowdControlAbility)i).getEffect().getType().equals(EffectType.BUFF))
			{
				if(i.getCastArea().equals(AreaOfEffect.SURROUND))
				{
			int all=0;
			int k = currentchampion.getLocation().x-1;
			for(;k<=currentchampion.getLocation().x+1;k++)
			{
				for(int j = currentchampion.getLocation().y-1;j<=currentchampion.getLocation().y+1;j++)
				{
					if(k<0||k>=5||j<0||j>=5||(k==currentchampion.getLocation().x&&j==currentchampion.getLocation().y))
						continue;
					else
						if(game.getBoard()[k][j]!=null&&game.getBoard()[k][j] instanceof Champion &&game.checkfriend(currentchampion, (Champion) game.getBoard()[k][j]) )
							all++;
				}
			}
			if(all!=0) {
				crowdcontrolability = true;
				crowdcontrolabilities.put(all,new Pair(((CrowdControlAbility)i),null));
			}
			}
			else if(i.getCastArea().equals(AreaOfEffect.TEAMTARGET))
				{
					int all=0;
					for(int j=0;j!=5;j++)
					{
						for(int k=0;k!=5;k++)
						{
							if(game.getBoard()[j][k]!=null && !(game.getBoard()[j][k] instanceof Cover))
							{
								if(Math.abs(currentchampion.getLocation().x-j)+(Math.abs(currentchampion.getLocation().y-k))<=i.getCastRange())
								{
									if(game.checkfriend(currentchampion,(Champion) game.getBoard()[j][k]))
										all++;
								}
							}
						}
					}
					if(all!=0) {
						crowdcontrolability = true;
						crowdcontrolabilities.put(all,new Pair(((CrowdControlAbility)i),null));
					}
				}
				else if(i.getCastArea().equals(AreaOfEffect.DIRECTIONAL))
				{
					int up = 0;
					int j=currentchampion.getLocation().x+1;
					int c=1;
					for(;j<5&&c<=i.getCastRange();j++,c++)
						if(game.getBoard()[j][currentchampion.getLocation().y]!=null)
						{
							if(game.getBoard()[j][currentchampion.getLocation().y] instanceof Champion &&game.checkfriend(currentchampion,(Champion) game.getBoard()[j][currentchampion.getLocation().y]))
							{
								up++ ;
							}
						}
					int down =0;
					j=currentchampion.getLocation().x-1;
					c=1;
					for(;j>-1&&c<=i.getCastRange();j--,c++)
						if(game.getBoard()[j][currentchampion.getLocation().y]!=null)
						{
							if(game.getBoard()[j][currentchampion.getLocation().y] instanceof Champion &&game.checkfriend(currentchampion,(Champion) game.getBoard()[j][currentchampion.getLocation().y]))
							{
								down++;
							}
						}
					int right=0;
					j = currentchampion.getLocation().y+1;
					c = 1;
					for(;j<5&&c<=i.getCastRange();j++,c++)
						if(game.getBoard()[currentchampion.getLocation().x][j]!=null)
						{
							if(game.getBoard()[currentchampion.getLocation().x][j] instanceof Champion &&game.checkfriend(currentchampion,(Champion) game.getBoard()[currentchampion.getLocation().x][j]))
							{
								right++;
							}
						}
					int left =0;
					j = currentchampion.getLocation().y-1;
					c = 1;
					for(;j>-1&&c<=i.getCastRange();j--,c++)
						if(game.getBoard()[currentchampion.getLocation().x][j]!=null)
						{
							if(game.getBoard()[currentchampion.getLocation().x][j] instanceof Champion &&game.checkfriend(currentchampion,(Champion) game.getBoard()[currentchampion.getLocation().x][j]))
							{
								left++;
							}
						}
					if(up!=0||right!=0||left!=0||down!=0)
					{
						crowdcontrolability = true;
						
						int t = (Math.max(Math.max(up, down), Math.max(right, left)));
						if(t==up)
							crowdcontrolabilities.put(up,new Pair(((CrowdControlAbility)i),Direction.UP));
						else if(t==down)
							crowdcontrolabilities.put(down,new Pair(((CrowdControlAbility)i),Direction.DOWN));
						else if (t==right)
							crowdcontrolabilities.put(right,new Pair(((CrowdControlAbility)i),Direction.RIGHT));
						else
							crowdcontrolabilities.put(left,new Pair(((CrowdControlAbility)i),Direction.LEFT));
					}
				}
				else
				{
					int all=0;
					pair2 tm = null;
					w:for(int j=0;j!=5;j++)
					{
						for(int k=0;k!=5;k++)
						{
							if(game.getBoard()[j][k]!=null)
							{
								if(Math.abs(currentchampion.getLocation().x-j)+(Math.abs(currentchampion.getLocation().y-k))<=i.getCastRange())
								{
									 if(game.getBoard()[j][k] instanceof Champion &&game.checkfriend(currentchampion,(Champion) game.getBoard()[j][k]))
									{
										 all=1;
										tm = new pair2(j,k);
										break w;
									}
								}
							}
						}
					}
					if(all !=0)
					{
						crowdcontrolability =true;
						crowdcontrolabilities.put(all,new Pair(((CrowdControlAbility)i),tm));
					}
				}
			}
			else
			{
				if(i.getCastArea().equals(AreaOfEffect.SURROUND))
				{
			int all=0;
			int k = currentchampion.getLocation().x-1;
			for(;k<=currentchampion.getLocation().x+1;k++)
			{
				for(int j = currentchampion.getLocation().y-1;j<=currentchampion.getLocation().y+1;j++)
				{
					if(k<0||k>=5||j<0||j>=5||(k==currentchampion.getLocation().x&&j==currentchampion.getLocation().y))
						continue;
					else
						if(game.getBoard()[k][j]!=null&&game.getBoard()[k][j] instanceof Champion && !game.checkfriend(currentchampion, (Champion) game.getBoard()[k][j]) )
							all++;
				}
			}
			if(all!=0) {
				crowdcontrolability = true;
				crowdcontrolabilities.put(all,new Pair(((CrowdControlAbility)i),null));
			}
			}
			else if(i.getCastArea().equals(AreaOfEffect.TEAMTARGET))
				{
					int all=0;
					for(int j=0;j!=5;j++)
					{
						for(int k=0;k!=5;k++)
						{
							if(game.getBoard()[j][k]!=null && !(game.getBoard()[j][k] instanceof Cover))
							{
								if(Math.abs(currentchampion.getLocation().x-j)+(Math.abs(currentchampion.getLocation().y-k))<=i.getCastRange())
								{
									if(!game.checkfriend(currentchampion,(Champion) game.getBoard()[j][k]))
										all++;
								}
							}
						}
					}
					if(all!=0) {
						crowdcontrolability = true;
						crowdcontrolabilities.put(all,new Pair(((CrowdControlAbility)i),null));
					}
				}
				else if(i.getCastArea().equals(AreaOfEffect.DIRECTIONAL))
				{
					int up = 0;
					int j=currentchampion.getLocation().x+1;
					int c=1;
					for(;j<5&&c<=i.getCastRange();j++,c++)
						if(game.getBoard()[j][currentchampion.getLocation().y]!=null)
						{
							if(game.getBoard()[j][currentchampion.getLocation().y] instanceof Champion && !game.checkfriend(currentchampion,(Champion) game.getBoard()[j][currentchampion.getLocation().y]))
							{
								up++ ;
							}
						}
					int down =0;
					j=currentchampion.getLocation().x-1;
					c=1;
					for(;j>-1&&c<=i.getCastRange();j--,c++)
						if(game.getBoard()[j][currentchampion.getLocation().y]!=null)
						{
							if(game.getBoard()[j][currentchampion.getLocation().y] instanceof Champion && !game.checkfriend(currentchampion,(Champion) game.getBoard()[j][currentchampion.getLocation().y]))
							{
								down++;
							}
						}
					int right=0;
					j = currentchampion.getLocation().y+1;
					c = 1;
					for(;j<5&&c<=i.getCastRange();j++,c++)
						if(game.getBoard()[currentchampion.getLocation().x][j]!=null)
						{
							if(game.getBoard()[currentchampion.getLocation().x][j] instanceof Champion && !game.checkfriend(currentchampion,(Champion) game.getBoard()[currentchampion.getLocation().x][j]))
							{
								right++;
							}
						}
					int left =0;
					j = currentchampion.getLocation().y-1;
					c = 1;
					for(;j>-1&&c<=i.getCastRange();j--,c++)
						if(game.getBoard()[currentchampion.getLocation().x][j]!=null)
						{
							if(game.getBoard()[currentchampion.getLocation().x][j] instanceof Champion && !game.checkfriend(currentchampion,(Champion) game.getBoard()[currentchampion.getLocation().x][j]))
							{
								left++;
							}
						}
					if(up!=0||right!=0||left!=0||down!=0)
					{
						crowdcontrolability = true;
						
						int t = (Math.max(Math.max(up, down), Math.max(right, left)));
						if(t==up)
							crowdcontrolabilities.put(up,new Pair(((CrowdControlAbility)i),Direction.UP));
						else if(t==down)
							crowdcontrolabilities.put(down,new Pair(((CrowdControlAbility)i),Direction.DOWN));
						else if (t==right)
							crowdcontrolabilities.put(right,new Pair(((CrowdControlAbility)i),Direction.RIGHT));
						else
							crowdcontrolabilities.put(left,new Pair(((CrowdControlAbility)i),Direction.LEFT));
					}
				}
				else
				{
					int all=0;
					pair2 tm = null;
					w:for(int j=0;j!=5;j++)
					{
						for(int k=0;k!=5;k++)
						{
							if(game.getBoard()[j][k]!=null)
							{
								if(Math.abs(currentchampion.getLocation().x-j)+(Math.abs(currentchampion.getLocation().y-k))<=i.getCastRange())
								{
									 if(game.getBoard()[j][k] instanceof Champion && !game.checkfriend(currentchampion,(Champion) game.getBoard()[j][k]))
									{
										all=1;
										tm = new pair2(j,k);
										break w;
									}
								}
							}
						}
					}
					if(all !=0)
					{
						crowdcontrolability =true;
						crowdcontrolabilities.put(all,new Pair(((CrowdControlAbility)i),tm));
					}
				}
			}
		}
	}
	private static void useleaderab()
	{
		Champion current = game.getCurrentChampion();
		if(game.getFirstPlayer().getLeader()!=current || game.isFirstLeaderAbilityUsed())
		{
			return;
		}
		else
		{
			if(current instanceof Hero)
			{
				int c =0;
				for(Champion i : game.getFirstPlayer().getTeam())
				{
					if(i.getCurrentHP()< i.getMaxHP()/1.3)
						c++;
				}
				if(c>=2)
					leaderab = true;
			}
			else if (current instanceof Villain)
			{
				int c =0;
				for(Champion i : game.getSecondPlayer().getTeam())
				{
					if(i.getCurrentHP()< (int)(i.getMaxHP()*0.3))
						c++;
				}
				if(c>=2)
					leaderab = true;
			}
			else
			{
				leaderab = true;
			}
		}
	}
	private static int best;
	private static ArrayList<Direction> m;
	private static boolean [][] grid;
	private static void move()
	{
		Champion current =  game.getCurrentChampion();
		if(currentchampion.getCondition().name().equals("ROOTED"))
			return;
		if(currentchampion.getCurrentActionPoints()<1)
			return ;
		m = new ArrayList<>();
		grid = new boolean [5][5];
		best = Integer.MAX_VALUE;
		grid[current.getLocation().x][current.getLocation().y] = false;
		graph(0,current.getLocation().x,current.getLocation().y,false,new ArrayList<Direction>());
	}
	public static void graph(int c,int i,int j,boolean t , ArrayList<Direction> di)
	{
		if(t&&game.getBoard()[i][j]!=null)
		{
			if(game.getBoard()[i][j] instanceof Champion)
				if(!game.checkfriend(currentchampion, ((Champion)game.getBoard()[i][j])))
				{
					if(c<best&&di.size()!=0)
					{
						best = c;
						m = di;
						move = true;
					}
				}
			return;
		}
		else {
		grid[i][j] = true;
		ArrayList<Direction> up = (ArrayList<Direction>) di.clone();
		if(i+1<5&&game.getBoard()[i+1][j]==null)
			up.add(Direction.UP);
		ArrayList<Direction> down = (ArrayList<Direction>) di.clone();
		if(i-1>-1&&game.getBoard()[i-1][j]==null)
		down.add(Direction.DOWN);
		ArrayList<Direction> right = (ArrayList<Direction>) di.clone();
		if(j+1<5 &&game.getBoard()[i][j+1]==null)
		right.add(Direction.RIGHT);
		ArrayList<Direction> left = (ArrayList<Direction>) di.clone();
		if(j-1>-1 &&game.getBoard()[i][j-1]==null)
		left.add(Direction.LEFT);
		if(i+1<5&& !grid[i+1][j])
			graph(c+1, i+1, j, true, up);
		if(i-1>-1 &&!grid[i-1][j])
			graph(c+1, i-1, j, true, down);
		if(j+1<5 && !grid[i][j+1])
			graph(c+1, i, j+1, true, right);
		if(j-1>-1 && !grid[i][j-1])
			graph(c+1, i, j-1, true, left);
		grid[i][j] = false;
		}
	}
}


