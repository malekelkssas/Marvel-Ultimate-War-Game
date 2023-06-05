package engine;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

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
import model.effects.Dodge;
import model.effects.Effect;
import model.effects.Embrace;
import model.effects.PowerUp;
import model.effects.Root;
import model.effects.Shield;
import model.effects.Shock;
import model.effects.Silence;
import model.effects.SpeedUp;
import model.effects.Stun;
import model.world.AntiHero;
import model.world.Champion;
import model.world.Condition;
import model.world.Cover;
import model.world.Damageable;
import model.world.Direction;
import model.world.Hero;
import model.world.Villain;

public class Game {
	private static ArrayList<Champion> availableChampions;
	private static ArrayList<Ability> availableAbilities;
	private Player firstPlayer;
	private Player secondPlayer;
	private Object[][] board;
	private PriorityQueue turnOrder;
	private boolean firstLeaderAbilityUsed;
	private boolean secondLeaderAbilityUsed;
	private final static int BOARDWIDTH = 5;
	private final static int BOARDHEIGHT = 5;

	public Game(Player first, Player second) {
		firstPlayer = first;
		secondPlayer = second;
		availableChampions = new ArrayList<Champion>();
		availableAbilities = new ArrayList<Ability>();
		board = new Object[BOARDWIDTH][BOARDHEIGHT];
		turnOrder = new PriorityQueue(6);
		placeChampions();
		placeCovers();
		ArrayList<Champion> team = firstPlayer.getTeam();
		for(Champion i:team)
			turnOrder.insert(i);
		team = secondPlayer.getTeam();
		for(Champion i:team)
			turnOrder.insert(i);
		
	}

	public static void loadAbilities(String filePath) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String line = br.readLine();
		while (line != null) {
			String[] content = line.split(",");
			Ability a = null;
			AreaOfEffect ar = null;
			switch (content[5]) {
			case "SINGLETARGET":
				ar = AreaOfEffect.SINGLETARGET;
				break;
			case "TEAMTARGET":
				ar = AreaOfEffect.TEAMTARGET;
				break;
			case "SURROUND":
				ar = AreaOfEffect.SURROUND;
				break;
			case "DIRECTIONAL":
				ar = AreaOfEffect.DIRECTIONAL;
				break;
			case "SELFTARGET":
				ar = AreaOfEffect.SELFTARGET;
				break;

			}
			Effect e = null;
			if (content[0].equals("CC")) {
				switch (content[7]) {
				case "Disarm":
					e = new Disarm(Integer.parseInt(content[8]));
					break;
				case "Dodge":
					e = new Dodge(Integer.parseInt(content[8]));
					break;
				case "Embrace":
					e = new Embrace(Integer.parseInt(content[8]));
					break;
				case "PowerUp":
					e = new PowerUp(Integer.parseInt(content[8]));
					break;
				case "Root":
					e = new Root(Integer.parseInt(content[8]));
					break;
				case "Shield":
					e = new Shield(Integer.parseInt(content[8]));
					break;
				case "Shock":
					e = new Shock(Integer.parseInt(content[8]));
					break;
				case "Silence":
					e = new Silence(Integer.parseInt(content[8]));
					break;
				case "SpeedUp":
					e = new SpeedUp(Integer.parseInt(content[8]));
					break;
				case "Stun":
					e = new Stun(Integer.parseInt(content[8]));
					break;
				}
			}
			switch (content[0]) {
			case "CC":
				a = new CrowdControlAbility(content[1], Integer.parseInt(content[2]), Integer.parseInt(content[4]),
						Integer.parseInt(content[3]), ar, Integer.parseInt(content[6]), e);
				break;
			case "DMG":
				a = new DamagingAbility(content[1], Integer.parseInt(content[2]), Integer.parseInt(content[4]),
						Integer.parseInt(content[3]), ar, Integer.parseInt(content[6]), Integer.parseInt(content[7]));
				break;
			case "HEL":
				a = new HealingAbility(content[1], Integer.parseInt(content[2]), Integer.parseInt(content[4]),
						Integer.parseInt(content[3]), ar, Integer.parseInt(content[6]), Integer.parseInt(content[7]));
				break;
			}
			availableAbilities.add(a);
			line = br.readLine();
		}
		br.close();
	}

	public static void loadChampions(String filePath) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String line = br.readLine();
		while (line != null) {
			String[] content = line.split(",");
			Champion c = null;
			switch (content[0]) {
			case "A":
				c = new AntiHero(content[1], Integer.parseInt(content[2]), Integer.parseInt(content[3]),
						Integer.parseInt(content[4]), Integer.parseInt(content[5]), Integer.parseInt(content[6]),
						Integer.parseInt(content[7]));
				break;

			case "H":
				c = new Hero(content[1], Integer.parseInt(content[2]), Integer.parseInt(content[3]),
						Integer.parseInt(content[4]), Integer.parseInt(content[5]), Integer.parseInt(content[6]),
						Integer.parseInt(content[7]));
				break;
			case "V":
				c = new Villain(content[1], Integer.parseInt(content[2]), Integer.parseInt(content[3]),
						Integer.parseInt(content[4]), Integer.parseInt(content[5]), Integer.parseInt(content[6]),
						Integer.parseInt(content[7]));
				break;
			}

			c.getAbilities().add(findAbilityByName(content[8]));
			c.getAbilities().add(findAbilityByName(content[9]));
			c.getAbilities().add(findAbilityByName(content[10]));
			availableChampions.add(c);
			line = br.readLine();
		}
		br.close();
	}

	private static Ability findAbilityByName(String name) {
		for (Ability a : availableAbilities) {
			if (a.getName().equals(name))
				return a;
		}
		return null;
	}

	public void placeCovers() {
		int i = 0;
		while (i < 5) {
			int x = ((int) (Math.random() * (BOARDWIDTH - 2))) + 1;
			int y = (int) (Math.random() * BOARDHEIGHT);

			if (board[x][y] == null) {
				board[x][y] = new Cover(x, y);
				i++;
			}
		}

	}

	public void placeChampions() {
		int i = 1;
		for (Champion c : firstPlayer.getTeam()) {
			board[0][i] = c;
			c.setLocation(new Point(0, i));
			i++;
		}
		i = 1;
		for (Champion c : secondPlayer.getTeam()) {
			board[BOARDHEIGHT - 1][i] = c;
			c.setLocation(new Point(BOARDHEIGHT - 1, i));
			i++;
		}
	
	}

	public static ArrayList<Champion> getAvailableChampions() {
		return availableChampions;
	}

	public static ArrayList<Ability> getAvailableAbilities() {
		return availableAbilities;
	}

	public Player getFirstPlayer() {
		return firstPlayer;
	}

	public Player getSecondPlayer() {
		return secondPlayer;
	}

	public Object[][] getBoard() {
		return board;
	}

	public PriorityQueue getTurnOrder() {
		return turnOrder;
	}

	public boolean isFirstLeaderAbilityUsed() {
		return firstLeaderAbilityUsed;
	}

	public boolean isSecondLeaderAbilityUsed() {
		return secondLeaderAbilityUsed;
	}

	public static int getBoardwidth() {
		return BOARDWIDTH;
	}

	public static int getBoardheight() {
		return BOARDHEIGHT;
	}
	
public Champion getCurrentChampion()
	{
		
			return (Champion) this.getTurnOrder().peekMin();
		
	}
	
public Player checkGameOver()
	{
		
		if(this.getSecondPlayer().getTeam().size()==0)
			return this.getFirstPlayer();
		if(this.getFirstPlayer().getTeam().size()==0)
			return this.getSecondPlayer();
		return null;
	}
	
public void move(Direction d)throws UnallowedMovementException ,NotEnoughResourcesException
	{
		
		Champion currentchampion = this.getCurrentChampion();
		if(currentchampion.getCondition().name().equals("ROOTED"))
			throw new  UnallowedMovementException("You can not move while being rooted");
		if(currentchampion.getCurrentActionPoints()<1)
			throw new NotEnoughResourcesException("You need at least one action point to move");
		
		switch(d.name())
		{
		case("UP"):
		{
			
			if(currentchampion.getLocation().x==this.getBoardheight()-1)throw new  UnallowedMovementException("Can not move out of the board");
			else if(this.getBoard()[currentchampion.getLocation().x+1][currentchampion.getLocation().y]!=null)	throw new  UnallowedMovementException("target cell is not empty");
			else 
					{
						
						
						this.getBoard()[currentchampion.getLocation().x+1][currentchampion.getLocation().y]=currentchampion;
						this.getBoard()[currentchampion.getLocation().x][currentchampion.getLocation().y]=null;
						currentchampion.setLocation(new Point(currentchampion.getLocation().x+1, currentchampion.getLocation().y));
					}
		
		break;
		}
		case("DOWN"):
		{
			if(currentchampion.getLocation().x==0)throw new  UnallowedMovementException("Can not move out of the board");
			else if(this.getBoard()[currentchampion.getLocation().x-1][currentchampion.getLocation().y]!=null)throw new  UnallowedMovementException("target cell is not empty");
		else 
		{
			this.getBoard()[currentchampion.getLocation().x-1][currentchampion.getLocation().y]=currentchampion;
			this.getBoard()[currentchampion.getLocation().x][currentchampion.getLocation().y]=null;
			currentchampion.setLocation(new Point(currentchampion.getLocation().x-1, currentchampion.getLocation().y));
		}
			
		break;
		}
		case("LEFT"):
		{
			if(currentchampion.getLocation().y==0)throw new  UnallowedMovementException("Can not move out of the board");
			else if(this.getBoard()[currentchampion.getLocation().x][currentchampion.getLocation().y-1]!=null)throw new  UnallowedMovementException("target cell is not empty");
		else 
		{
			this.getBoard()[currentchampion.getLocation().x][currentchampion.getLocation().y-1]=currentchampion;
			this.getBoard()[currentchampion.getLocation().x][currentchampion.getLocation().y]=null;
			currentchampion.setLocation(new Point(currentchampion.getLocation().x, currentchampion.getLocation().y-1));
		}
		
		break;
		}
		default:
		{
			
			if(currentchampion.getLocation().y==this.getBoardwidth()-1)throw new  UnallowedMovementException("Can not move out of the board");
			else if(this.getBoard()[currentchampion.getLocation().x][currentchampion.getLocation().y+1]!=null)throw new  UnallowedMovementException("target cell is not empty");
		else 
		{
			this.getBoard()[currentchampion.getLocation().x][currentchampion.getLocation().y+1]=currentchampion;
			this.getBoard()[currentchampion.getLocation().x][currentchampion.getLocation().y]=null;
			currentchampion.setLocation(new Point(currentchampion.getLocation().x, currentchampion.getLocation().y+1));
		}
			
		break;
		}
		}
		currentchampion.setCurrentActionPoints(currentchampion.getCurrentActionPoints()-1);
		
	}

	public void attack(Direction d) throws ChampionDisarmedException, NotEnoughResourcesException
	{
		
		
		Champion currentchampion = this.getCurrentChampion();
		ArrayList<Effect> applied = currentchampion.getAppliedEffects();
		for(Effect i:applied)
			if(i instanceof Disarm)
				throw new ChampionDisarmedException("Can not attack while being disarmed");
		if(currentchampion.getCurrentActionPoints()<2)
			throw new NotEnoughResourcesException("You need at least two action point to perform a normal attack");
		Damageable tmp = null;
		switch(d.name())
		{
		case("UP"):{
					// x --> height  && y --> width
			int i=currentchampion.getLocation().x+1;
			int c=1;
			for(;i<BOARDHEIGHT&&c<=currentchampion.getAttackRange();i++,c++)
			{
				if(this.getBoard()[i][currentchampion.getLocation().y]!=null)
				{
						tmp = (Damageable) this.getBoard()[i][currentchampion.getLocation().y] ;
						break;	
				}
			}
			break;
			}
		case("DOWN"):{
			
		int i=currentchampion.getLocation().x-1;
		int c=1;
		for(;i>-1&&c<=currentchampion.getAttackRange();i--,c++)
		{
			if(this.getBoard()[i][currentchampion.getLocation().y]!=null)
			{
					tmp = (Damageable) this.getBoard()[i][currentchampion.getLocation().y] ;
					break;
			}
		}
			break;
			}
		case("LEFT"):{
			
			int i=currentchampion.getLocation().y-1;
			int c=1;
			for(;i>-1&&c<=currentchampion.getAttackRange();i--,c++)
			{
				if(this.getBoard()[currentchampion.getLocation().x][i]!=null)
				{
					
						tmp = (Damageable) this.getBoard()[currentchampion.getLocation().x][i] ;
						break;
					
				}
			}
			break;
			}
		default:{
			
			int i=currentchampion.getLocation().y+1;
			int c=1;
			for(;i<BOARDWIDTH&&c<=currentchampion.getAttackRange();i++,c++)
			{
				if(this.getBoard()[currentchampion.getLocation().x][i]!=null)
				{
						tmp = (Damageable) this.getBoard()[currentchampion.getLocation().x][i] ;
						break;
				}
			}
			break;
			}
		}
		if(tmp !=null)
		{
			if(tmp instanceof Champion)
			{
				if(!checkfriend((Champion) tmp,currentchampion))
					attackhealper((Champion) tmp);
			}
			else {
				tmp.setCurrentHP(tmp.getCurrentHP()-currentchampion.getAttackDamage());	
				if(tmp.getCurrentHP()==0)
					this.getBoard()[tmp.getLocation().x][tmp.getLocation().y]=null;
				}
			}
		currentchampion.setCurrentActionPoints(currentchampion.getCurrentActionPoints()-2);	
	}
	
	public void attackhealper(Champion tmp) 
	{
		
		ArrayList<Effect> applied = tmp.getAppliedEffects();
		boolean shieldflag = false;
		int shield =0;
		boolean dodgeflag=false;
		applied =tmp.getAppliedEffects();
		for(int j=0;j!=applied.size();j++)
		{
			if(applied.get(j) instanceof Shield)
			{
				shieldflag = true;
				shield=j;
				break;
			}
			else if(!dodgeflag && applied.get(j) instanceof Dodge)
			{
				dodgeflag=true;
			}
		}
		if(shieldflag)
		{
			Effect m =applied.remove(shield);
			m.remove(tmp);
		}
		else if(dodgeflag)
		{
			Random r = new Random();
			int m = r.nextInt(2);
			if(m==0)
				attackhealperhealper(tmp);	
		}
		else
			attackhealperhealper(tmp);
				
			
			
		
	}
	
	public void attackhealperhealper(Champion tmp)
	{
		
		
		Champion currentchampion = this.getCurrentChampion();
		
		if(currentchampion instanceof Hero)
		{
			if(tmp instanceof Villain || tmp instanceof AntiHero)
				tmp.setCurrentHP(tmp.getCurrentHP()-(int)(currentchampion.getAttackDamage()*1.5));
			else
				tmp.setCurrentHP(tmp.getCurrentHP()-currentchampion.getAttackDamage());
		}
		else if(currentchampion instanceof Villain )
		{
			if(tmp instanceof Hero || tmp instanceof AntiHero)
				tmp.setCurrentHP(tmp.getCurrentHP()-(int)(currentchampion.getAttackDamage()*1.5));
			else
				tmp.setCurrentHP(tmp.getCurrentHP()-currentchampion.getAttackDamage());
		}
		else
		{
			
			if(tmp instanceof AntiHero) {
				
				tmp.setCurrentHP(tmp.getCurrentHP()-currentchampion.getAttackDamage());
				
			}
				else {
				
				tmp.setCurrentHP(tmp.getCurrentHP()-(int)(currentchampion.getAttackDamage()*1.5));
			}
			}
		if(tmp.getCurrentHP()<=0)
		{
			this.getBoard()[tmp.getLocation().x][tmp.getLocation().y]=null;
			
			tmp.setCondition(Condition.KNOCKEDOUT);
			ArrayList<Champion> remo = this.firstPlayer.getTeam();
			boolean flag = false;
			for(int i=0;i!=remo.size();i++)
				if(remo.get(i)==tmp)
				{
					flag=true;
					remo.remove(i);
					break;
				}
			remo = this.secondPlayer.getTeam();
			for(int i=0;i!=remo.size()&!flag;i++)
				if(remo.get(i)==tmp)
				{
					remo.remove(i);
					break;
				}
			removecham(tmp);
		}
		
	}
	public void removecham(Champion tmp)
	{
		ArrayList<Champion> min =new ArrayList<>();
		while(this.getTurnOrder().size()!=0)
		{
			if(this.getTurnOrder().peekMin()==tmp)
				this.getTurnOrder().remove();
			else
				min.add((Champion) this.getTurnOrder().remove());
		}
		while(!min.isEmpty())
		{
			this.getTurnOrder().insert(min.remove(min.size()-1));
		}
		
	}
	public  boolean checkfriend(Champion a ,Champion b)
	{
		// true if they are from the same team
		
		boolean aa = false;
		boolean bb = false;
		ArrayList<Champion> tmp = this.getFirstPlayer().getTeam();
		if((tmp.indexOf(a)>=0&&tmp.indexOf(b)>=0)||(tmp.indexOf(a)<0&&tmp.indexOf(b)<0))
			return true;
		
		return false;
			
	}
	
	public void castAbility(Ability a) throws AbilityUseException, NotEnoughResourcesException, CloneNotSupportedException
	{
		Champion currentchampion = this.getCurrentChampion();
		ArrayList<Effect> applied = currentchampion.getAppliedEffects();
		
		for(Effect i:applied)
			if(i instanceof Silence)
				throw new AbilityUseException("You can not cast an ability while being silenced");
		if(a.getCurrentCooldown()!=0)
			throw new AbilityUseException("You can not use an ability while it is in cooldown");
		if(currentchampion.getCurrentActionPoints()<a.getRequiredActionPoints()) 
			throw new NotEnoughResourcesException("you need at least " + a.getRequiredActionPoints() + " action points to cast this ability");
		if(currentchampion.getMana()<a.getManaCost())
			throw new NotEnoughResourcesException("you need at least " + a.getManaCost() + " mana to cast this ability");
		ArrayList<Damageable> targets = new ArrayList<>();
		switch(a.getCastArea().name())
		{
		case("TEAMTARGET"):
		{
			
			ArrayList<Champion> team =firstPlayer.getTeam(); 
			
			for(Champion i :team)
			{
				if(Math.abs(currentchampion.getLocation().x-i.getLocation().x)+Math.abs(currentchampion.getLocation().y-i.getLocation().y)<=a.getCastRange())
					targets.add(i);
			}
				team = secondPlayer.getTeam();
				for(Champion i :secondPlayer.getTeam())
				{
					if(Math.abs(currentchampion.getLocation().x-i.getLocation().x)+Math.abs(currentchampion.getLocation().y-i.getLocation().y)<=a.getCastRange())
						targets.add(i);
				}
			break;
		}
		case("SELFTARGET"):
		{
			targets.add(currentchampion);
			break;
		}
		default:
		{
			int i = currentchampion.getLocation().x-1;
			for(;i<=currentchampion.getLocation().x+1;i++)
			{
				for(int j = currentchampion.getLocation().y-1;j<=currentchampion.getLocation().y+1;j++)
				{
					if(i<0||i>=BOARDHEIGHT||j<0||j>=BOARDWIDTH||(i==currentchampion.getLocation().x&&j==currentchampion.getLocation().y))
						continue;
					else
						if(this.getBoard()[i][j]!=null)
							targets.add((Damageable) this.getBoard()[i][j]);
				}
			}
			break;
		}
		
		}
		
		abilityremovetarget(a,targets);	
			a.execute(targets);
		if(a instanceof DamagingAbility)
		{
			for (Damageable damageable : targets)
			{
				if(damageable.getCurrentHP()<=0) {
					this.getBoard()[damageable.getLocation().x][damageable.getLocation().y]=null;
					if(damageable instanceof Champion){
					((Champion)damageable).setCondition(Condition.KNOCKEDOUT);
					ArrayList<Champion> remo = this.firstPlayer.getTeam();
					boolean flag = false;
					for(int i=0;i!=remo.size();i++)
						if(remo.get(i)==damageable)
						{
							flag=true;
							remo.remove(i);
							break;
						}
					remo = this.secondPlayer.getTeam();
					for(int i=0;i!=remo.size()&!flag;i++)
						if(remo.get(i)==damageable)
						{
							remo.remove(i);
							break;
						}
					removecham((Champion) damageable);
					}
				}
			}
		}
		currentchampion.setCurrentActionPoints(currentchampion.getCurrentActionPoints()-a.getRequiredActionPoints());
		currentchampion.setMana(currentchampion.getMana()-a.getManaCost());
		a.setCurrentCooldown(a.getBaseCooldown());
				
				
	}
	
	public void castAbility(Ability a, int x, int y) throws AbilityUseException, NotEnoughResourcesException, InvalidTargetException, CloneNotSupportedException
	{
		Champion currentchampion = this.getCurrentChampion();
		ArrayList<Effect> applied = currentchampion.getAppliedEffects();
		
		for(Effect i:applied)
			if(i instanceof Silence)
				throw new AbilityUseException("You can not cast an ability while being silenced");
		if(a.getCurrentCooldown()!=0)
			throw new AbilityUseException("You can not use an ability while it is in cooldown");
		if(currentchampion.getCurrentActionPoints()<a.getRequiredActionPoints())
			throw new NotEnoughResourcesException("you need at least " + a.getRequiredActionPoints() + " action points to cast this ability");
		if(currentchampion.getMana()<a.getManaCost())
			throw new NotEnoughResourcesException("you need at least " + a.getManaCost() + " mana to cast this ability");
		
		if(this.getBoard()[x][y]==null)
			throw new InvalidTargetException("You can not cast an ability on an empty cell"); 
		if(Math.abs(currentchampion.getLocation().x-x)+(Math.abs(currentchampion.getLocation().y-y))>a.getCastRange())
			throw new AbilityUseException("Target out of the ability's cast range");
		Boolean check = null;					//(null for covers) (true for champion from the same team)
		Damageable tmp = (Damageable) this.getBoard()[x][y];
		ArrayList<Damageable> targets = new ArrayList<>();
		if(tmp instanceof Champion)
			check = checkfriend(currentchampion, (Champion) tmp);
		if(a instanceof HealingAbility)
		{

			if(check!=null&&check)
			{
				targets.add(tmp);
				a.execute(targets);
				
			}
			else
				throw new InvalidTargetException("Covers can only be damaged");
		}
		else if(a instanceof DamagingAbility)
		{
			if(check!=null&&check)
				throw new InvalidTargetException("Can not cast damaging ability on friendly targets");
			else
			{
				targets.add(tmp);
				if(check!=null&&!damageabilishield((Champion) tmp))
				{
					a.execute(targets);
					if(tmp.getCurrentHP()<=0)
					{
						this.getBoard()[x][y]=null;
						if(check!=null) {
							((Champion)tmp).setCondition(Condition.KNOCKEDOUT);
							ArrayList<Champion> remo = this.firstPlayer.getTeam();
							boolean flag = false;
							for(int i=0;i!=remo.size();i++)
								if(remo.get(i)==tmp)
								{
									flag=true;
									remo.remove(i);
									break;
								}
							remo = this.secondPlayer.getTeam();
							for(int i=0;i!=remo.size()&!flag;i++)
								if(remo.get(i)==tmp)
								{
									remo.remove(i);
									break;
								}
							removecham((Champion) tmp);
						}
					}
				}
				else if (check==null)
				{
					a.execute(targets);
					if(tmp.getCurrentHP()==0)
					{
						this.getBoard()[x][y]=null;
					}
				}
			}
		}
		else
		{
			if(check==null)
				throw new InvalidTargetException("can not use effects on cover");
			else if(((CrowdControlAbility)a).getEffect().getType().name().equals("BUFF")) {
				if(!check)
					throw new InvalidTargetException("Can not buff enemy targets");
			}
			else {
				if(check)
					throw new InvalidTargetException("Can not debuff friendly targets");
			}
			targets.add(tmp);
			a.execute(targets);
		}
		currentchampion.setCurrentActionPoints(currentchampion.getCurrentActionPoints()-a.getRequiredActionPoints());
		currentchampion.setMana(currentchampion.getMana()-a.getManaCost());
		a.setCurrentCooldown(a.getBaseCooldown());
		
	}

	 public void castAbility(Ability a, Direction d) throws NotEnoughResourcesException, CloneNotSupportedException, AbilityUseException
	 {
		 Champion currentchampion = this.getCurrentChampion();
		ArrayList<Effect> applied = currentchampion.getAppliedEffects();
		
		
		for(Effect i:applied)
			if(i instanceof Silence)
				throw new AbilityUseException("You can not cast an ability while being silenced");
		if(a.getCurrentCooldown()!=0)
			throw new AbilityUseException("\"You can not use an ability while it is in cooldown\"");
		if(currentchampion.getCurrentActionPoints()<a.getRequiredActionPoints())
			throw new NotEnoughResourcesException("you need at least " + a.getRequiredActionPoints() + " action points to cast this ability");
		if(currentchampion.getMana()<a.getManaCost())
			throw new NotEnoughResourcesException("you need at least " + a.getManaCost() + " mana to cast this ability");
		ArrayList<Damageable> targets = new ArrayList<>();
		switch(d.name())
		{
		case("UP"):
		{
			int i=currentchampion.getLocation().x+1;
			int c=1;
			for(;i<BOARDHEIGHT&&c<=a.getCastRange();i++,c++)
				if(this.getBoard()[i][currentchampion.getLocation().y]!=null)
					targets.add((Damageable) this.getBoard()[i][currentchampion.getLocation().y]);
			
			break;
		}
		case("DOWN"):
		{
			
			int i=currentchampion.getLocation().x-1;
			int c=1;
			for(;i>-1&&c<=a.getCastRange();i--,c++)
				if(this.getBoard()[i][currentchampion.getLocation().y]!=null)
					targets.add((Damageable) this.getBoard()[i][currentchampion.getLocation().y]);
			break;
		}
		case("RIGHT"):
		{
			int i=currentchampion.getLocation().y+1;
			int c=1;
			for(;i<BOARDWIDTH&&c<=a.getCastRange();i++,c++)
				if(this.getBoard()[currentchampion.getLocation().x][i]!=null)
					targets.add((Damageable) this.getBoard()[currentchampion.getLocation().x][i]);
			break;
		}
		default:
		{
			int i=currentchampion.getLocation().y-1;
			int c=1;
			for(;i>-1&&c<=a.getCastRange();i--,c++)
				if(this.getBoard()[currentchampion.getLocation().x][i]!=null)
					targets.add((Damageable) this.getBoard()[currentchampion.getLocation().x][i]);
			break;
		}
		}
		abilityremovetarget(a,targets);
		if(targets.size()!=0)
			a.execute(targets);
		if(a instanceof DamagingAbility)
		{
			for (Damageable damageable : targets) 
			{
				if(damageable.getCurrentHP()<=0)
				{
					this.getBoard()[damageable.getLocation().x][damageable.getLocation().y]=null;
					if(damageable instanceof Champion)
					{
					((Champion)damageable).setCondition(Condition.KNOCKEDOUT);
					ArrayList<Champion> remo = this.firstPlayer.getTeam();
					boolean flag = false;
					for(int i=0;i!=remo.size();i++)
						if(remo.get(i)==damageable)
						{
							flag=true;
							remo.remove(i);
							break;
						}
					remo = this.secondPlayer.getTeam();
					for(int i=0;i!=remo.size()&!flag;i++)
						if(remo.get(i)==damageable)
						{
							remo.remove(i);
							break;
						}
					removecham((Champion) damageable);
					
					}
				}
			}
		}
		currentchampion.setCurrentActionPoints(currentchampion.getCurrentActionPoints()-a.getRequiredActionPoints());
		currentchampion.setMana(currentchampion.getMana()-a.getManaCost());
		a.setCurrentCooldown(a.getBaseCooldown());
		
	 }
	 public boolean damageabilishield(Champion a)
		{
			int index = -1;
			ArrayList<Effect> applied = a.getAppliedEffects();
			for(int i=0;i!=applied.size();i++)
			{
				if(applied.get(i) instanceof Shield)
				{
					index = i;
					break;
				}
			}
			if(index!= -1)
			{
				Effect effect =applied.remove(index);
				effect.remove(a);
				return true;
			}
			return false;
		}	
 public void abilityremovetarget(Ability a,ArrayList<Damageable> targets)
	 {
	 
		 Champion currentchampion = this.getCurrentChampion();
		 if(a instanceof HealingAbility)
			{
			
				ArrayList<Integer> remove = new ArrayList<>();
				int c=0;
				for(Damageable i:targets)
				{
					if(i instanceof Cover||!checkfriend((Champion)i,currentchampion))
						remove.add(c);
					c++;
				}
				c=0;
				for(int i:remove) {
					targets.remove(i-c);
					c++;
				}
			}
			else if(a instanceof DamagingAbility)
			{
				
				ArrayList<Integer> remove = new ArrayList<>();
				int c=0;
				for(Damageable i:targets)
				{
					if(i instanceof Champion && (checkfriend((Champion)i,currentchampion)||damageabilishield((Champion) i)))
						remove.add(c);
					c++;
				}
				c=0;
				for(int i:remove) {
					targets.remove(i-c);
					c++;
				}
			}
			else
			{
				
				if(((CrowdControlAbility)a).getEffect().getType().name().equals("BUFF"))
				{
					ArrayList<Integer> remove = new ArrayList<>();
					int c=0;
					for(Damageable i:targets)
					{
						if(i instanceof Cover||!checkfriend((Champion)i,currentchampion))
							remove.add(c);
						c++;
					}
					c=0;
					for(int i:remove) {
						targets.remove(i-c);
						c++;
					}
				}
				else
				{
					ArrayList<Integer> remove = new ArrayList<>();
					int c=0;
					for(Damageable i:targets)
					{
						if(i instanceof Cover||checkfriend((Champion)i,currentchampion))
							remove.add(c);
						c++;
					}
					c=0;
					for(int i:remove) {
						targets.remove(i-c);
						c++;
					}
				}
			}
	 }
	private void prepareChampionTurns()
	{
		ArrayList<Champion> team = firstPlayer.getTeam();
		for(Champion i:team)
			turnOrder.insert(i);
		team = secondPlayer.getTeam();
		for(Champion i:team)
				turnOrder.insert(i);
	}
	public void endTurn() 
	{
		Champion tmp = (Champion) this.getTurnOrder().remove();
		ArrayList<Effect> applied = new ArrayList<>();
		ArrayList<Integer> remove = new ArrayList<>();
		int c=0;
		ArrayList<Ability> abi = new ArrayList<>();
		if(this.getTurnOrder().size()==0) {
			prepareChampionTurns();
		}
		//((!((Champion)this.turnOrder.peekMin()).getCondition().name().equals("INACTIVE"))||(stunned(((Champion)this.turnOrder.peekMin()))))
		while(this.getTurnOrder().size()!=0&&((Champion)this.turnOrder.peekMin()).getCondition().name().equals("INACTIVE"))
			{
			tmp =(Champion) this.getTurnOrder().remove();
			
				applied =tmp.getAppliedEffects();
				remove = new ArrayList<>();
				 c=0;
				for(Effect i:applied)
				{
					i.setDuration(i.getDuration()-1);
					if(i.getDuration()<=0) {
						remove.add(c);
						
					}
					c++;
				}
				c=0;
				for(int i:remove)
				{
					applied.remove(i-c).remove(tmp);
						
					c++;
				}
				tmp.setCurrentActionPoints(tmp.getMaxActionPointsPerTurn());
				abi = tmp.getAbilities();
				for(Ability i:abi)
					i.setCurrentCooldown(i.getCurrentCooldown()-1);
			}
		if(this.getTurnOrder().size()==0) {
			prepareChampionTurns();
			endTurn();
		}
		else
		{
			tmp =(Champion) this.getTurnOrder().peekMin();
			applied =tmp.getAppliedEffects();
			remove = new ArrayList<>();
			 c=0;
			for(Effect i:applied)
			{
				i.setDuration(i.getDuration()-1);
				if(i.getDuration()<=0) {
					remove.add(c);
					//i.remove(tmp);	
				}
				c++;
			}
			c=0;
			for(int i:remove)
			{
				//applied.remove(i-c);
				applied.remove(i-c).remove(tmp);
				c++;
			}
			tmp.setCurrentActionPoints(tmp.getMaxActionPointsPerTurn());
			abi = tmp.getAbilities();
			for(Ability i:abi)
				i.setCurrentCooldown(i.getCurrentCooldown()-1);
			if(this.getTurnOrder().size()==0) {
				prepareChampionTurns();
			}
		}
	}	

	public void useLeaderAbility() throws LeaderAbilityAlreadyUsedException, LeaderNotCurrentException, CloneNotSupportedException
	{
			
		 Champion currentchampion = this.getCurrentChampion();
		 boolean firstpl = false;
			 if(firstPlayer.getTeam().contains(currentchampion))
				 firstpl=true;
		 ArrayList<Champion> targets = new ArrayList<>();
		 if(firstpl)
		 {
			
			 if(currentchampion!=firstPlayer.getLeader())
				 throw new LeaderNotCurrentException("The current champion is not a leader");
			 if(firstLeaderAbilityUsed)
				 throw new LeaderAbilityAlreadyUsedException("This leader already used his ability");
			 if(currentchampion instanceof Hero) {
				 for(Champion i:firstPlayer.getTeam())
						 targets.add(i);
			 }
			else if(currentchampion instanceof  Villain)
				for(Champion i:secondPlayer.getTeam()) {
					if(i.getCurrentHP()<i.getMaxHP()*0.3)
						 targets.add(i);
				}
			else
			{
				for(Champion i:firstPlayer.getTeam())
					 if(i!=firstPlayer.getLeader())
						 targets.add(i);
				for(Champion i:secondPlayer.getTeam())
					 if(i!=secondPlayer.getLeader())
						 targets.add(i);
			}
			 firstLeaderAbilityUsed =true;
		 }
		 else
		 {
			 if(currentchampion!=secondPlayer.getLeader())
				 throw new LeaderNotCurrentException("The current champion is not a leader");
			 if(secondLeaderAbilityUsed)
				 throw new LeaderAbilityAlreadyUsedException("This leader already used his ability");
			 if(currentchampion instanceof Hero) {
				 for(Champion i:secondPlayer.getTeam())
						 targets.add(i);
			 }
			else if(currentchampion instanceof  Villain)
				for(Champion i:firstPlayer.getTeam()) {
					if(i.getCurrentHP()<i.getMaxHP()*0.3)	
						targets.add(i);
						 
				}
			else
			{
				for(Champion i:firstPlayer.getTeam())
					 if(i!=firstPlayer.getLeader())
						 targets.add(i);
				for(Champion i:secondPlayer.getTeam())
					 if(i!=secondPlayer.getLeader())
						 targets.add(i);
			}
			 secondLeaderAbilityUsed =true;
		 }
		 
		 currentchampion.useLeaderAbility(targets);
		 if(currentchampion instanceof Villain)
		 {
			 ArrayList<Integer> remove = new ArrayList<>();
			 int c=0;
			 for(Champion j:firstPlayer.getTeam()) {
				 if(j.getCondition().name().equals("KNOCKEDOUT"))
				 {
					 this.getBoard()[j.getLocation().x][j.getLocation().y]=null;
					 //																	there is a problem  here <--------
					 remove.add(c);
					 removecham(j);
				 }
				c++; 
			 }
			 c=0;
			 for(int i:remove) {
				 firstPlayer.getTeam().remove(i-c);
					c++;
				}
			 remove = new ArrayList<>();
			 c=0;
			 for(Champion i:secondPlayer.getTeam()) {
				 if(i.getCondition().name().equals("KNOCKEDOUT"))
				 {
					 this.getBoard()[i.getLocation().x][i.getLocation().y]=null;
					 //																	there is a problem  here <--------
					 remove.add(c);
					 removecham(i);
				 }
				 c++;
			 }
			 c=0;
			 for(int i:remove) {
				 secondPlayer.getTeam().remove(i-c);
					c++;
				}
		 }
	}
	
}
