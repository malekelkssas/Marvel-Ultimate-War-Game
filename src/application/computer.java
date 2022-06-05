package application;

import java.util.TreeMap;

import engine.Game;
import model.abilities.Ability;
import model.abilities.AreaOfEffect;
import model.abilities.CrowdControlAbility;
import model.abilities.DamagingAbility;
import model.abilities.HealingAbility;
import model.effects.Effect;
import model.effects.EffectType;
import model.effects.Silence;
import model.world.Champion;
import model.world.Cover;
import model.world.Damageable;
import model.world.Direction;

public class computer {
	private static Game game;
	private static TreeMap<Integer,Champion> inattackrange;
	private static TreeMap<Integer,Cover> inattackcover;
	private static boolean attack; 
	private static boolean attackability;
	private static boolean healingability;
	private static boolean crowdcontrolability;
	private static TreeMap<Integer,Pair> damageabilities;
	private static TreeMap<Integer,Pair> healingabilities;
	private static TreeMap<Integer,Pair> crowdcontrolabilities;
	private static Champion currentchampion;
	public void play(Game originalgame)
	{
		game = originalgame;
		inattackrange = new TreeMap<>();
		 inattackcover = new TreeMap<>();
		 damageabilities = new TreeMap<>();
		 healingabilities = new TreeMap<>();
		 crowdcontrolabilities = new TreeMap<>();
		currentchampion = game.getCurrentChampion();

	}
	public void getinattackrange()
	{
		for(int i=0;i!=5;i++)
		{
			for(int j=0;j!=5;j++)
			{
				if(game.getBoard()[i][j]!=null)
				{
					if(game.getBoard()[i][j] instanceof Cover)
						inattackcover.put(((Cover) game.getBoard()[i][j]).getCurrentHP(),(Cover) game.getBoard()[i][j]);
					else
					{
						if(Math.abs(currentchampion.getLocation().x-i)+(Math.abs(currentchampion.getLocation().y-j))<=currentchampion.getAttackRange())
						{
							if(!game.checkfriend(currentchampion,(Champion) game.getBoard()[i][j]))
								inattackrange.put(((Champion) game.getBoard()[i][j]).getCurrentHP(), currentchampion);
						}
					}
				}
			}
		}
	}
	public void damageability()
	{
		for(Effect i:currentchampion.getAppliedEffects())
			if(i instanceof Silence)
				return;
		for(Ability i : currentchampion.getAbilities())
		{
			if(!(i instanceof DamagingAbility)||i.getCurrentCooldown()!=0||(currentchampion.getCurrentActionPoints()<i.getRequiredActionPoints())||(currentchampion.getMana()<i.getManaCost()))
				continue;
			if(i.getCastArea().equals(AreaOfEffect.SURROUND)||i.getCastArea().equals(AreaOfEffect.TEAMTARGET))
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
								if(!game.checkfriend(currentchampion,(Champion) game.getBoard()[j][k]))
									all++;
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
						if(game.getBoard()[j][currentchampion.getLocation().y] instanceof Cover||!game.checkfriend(currentchampion,(Champion) game.getBoard()[j][currentchampion.getLocation().y]))
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
						if(game.getBoard()[j][currentchampion.getLocation().y] instanceof Cover||!game.checkfriend(currentchampion,(Champion) game.getBoard()[j][currentchampion.getLocation().y]))
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
						if(game.getBoard()[currentchampion.getLocation().x][j] instanceof Cover||!game.checkfriend(currentchampion,(Champion) game.getBoard()[currentchampion.getLocation().x][j]))
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
						if(game.getBoard()[currentchampion.getLocation().x][j] instanceof Cover||!game.checkfriend(currentchampion,(Champion) game.getBoard()[currentchampion.getLocation().x][j]))
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
				pair tm = null;
				boolean findcham = false;
				for(int j=0;j!=5;j++)
				{
					for(int k=0;k!=5;k++)
					{
						if(game.getBoard()[j][k]!=null)
						{
							if(Math.abs(currentchampion.getLocation().x-j)+(Math.abs(currentchampion.getLocation().y-k))<=i.getCastRange())
							{
								if((game.getBoard()[j][k] instanceof Cover && !findcham))
								{
									int tmp = all;
									all = Math.min(all,((Damageable)game.getBoard()[j][k]).getCurrentHP());
									if(tmp !=all)
									{
										tm = new pair(j,k);
									}
								}
								else if(game.getBoard()[j][k] instanceof Champion &&!game.checkfriend(currentchampion,(Champion) game.getBoard()[j][k]))
								{
									findcham=true;
									int tmp = all;
									all = Math.min(all,((Damageable)game.getBoard()[j][k]).getCurrentHP());
									if(tmp!=all)
									{
										tm = new pair(j,k);
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
	public void healingability()
	{
			for(Effect i:currentchampion.getAppliedEffects())
				if(i instanceof Silence)
					return;
			for(Ability i : currentchampion.getAbilities())
			{
				if(!(i instanceof HealingAbility)||i.getCurrentCooldown()!=0||(currentchampion.getCurrentActionPoints()<i.getRequiredActionPoints())||(currentchampion.getMana()<i.getManaCost()))
					continue;
				if(i.getCastArea().equals(AreaOfEffect.SURROUND)||i.getCastArea().equals(AreaOfEffect.TEAMTARGET))
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
										all+= (((Champion) game.getBoard()[j][k]).getMaxHP())-((((HealingAbility)i).getHealAmount()+((Champion) game.getBoard()[j][k]).getCurrentHP()));
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
								up+= (((Champion) game.getBoard()[j][currentchampion.getLocation().y]).getMaxHP())-((((HealingAbility)i).getHealAmount()+((Champion) game.getBoard()[j][currentchampion.getLocation().y]).getCurrentHP()));
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
								down+= (((Champion) game.getBoard()[j][currentchampion.getLocation().y]).getMaxHP())-((((HealingAbility)i).getHealAmount()+((Champion) game.getBoard()[j][currentchampion.getLocation().y]).getCurrentHP()));
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
								right+= (((Champion) game.getBoard()[currentchampion.getLocation().x][j]).getMaxHP())-((((HealingAbility)i).getHealAmount()+((Champion) game.getBoard()[currentchampion.getLocation().x][j]).getCurrentHP()));
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
								left+= (((Champion) game.getBoard()[currentchampion.getLocation().x][j]).getMaxHP())-((((HealingAbility)i).getHealAmount()+((Champion) game.getBoard()[currentchampion.getLocation().x][j]).getCurrentHP()));
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
					pair tm = null;
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
										all = Math.max(all,(((Champion) game.getBoard()[j][k]).getMaxHP())-((((HealingAbility)i).getHealAmount()+((Champion) game.getBoard()[j][k]).getCurrentHP())));
										if(tmp !=all)
											tm = new pair(j,k);
									}
								}
							}
						}
					}
					if(all !=Integer.MIN_VALUE)
					{
						healingability=true;
						healingabilities.put(all,new Pair(((HealingAbility)i),tm));
					}
				}
			}
	}
	public void crowdcontrolability()
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
				if(i.getCastArea().equals(AreaOfEffect.SURROUND)||i.getCastArea().equals(AreaOfEffect.TEAMTARGET))
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
					pair tm = null;
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
										tm = new pair(j,k);
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
				if(i.getCastArea().equals(AreaOfEffect.SURROUND)||i.getCastArea().equals(AreaOfEffect.TEAMTARGET))
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
					pair tm = null;
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
										tm = new pair(j,k);
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
	
	
	class Pair
	{
		Ability x;
		Object y;
		public Pair(Ability x , Object y)
		{
			this.x = x;
			this.y=y;
		}
	}
	class pair
	{
		int x;
		int y;
		public pair(int x , int y)
		{
			this.x=x;
			this.y = y;
		}
	}
}
