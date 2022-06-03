package application;
	
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


import engine.Game;
import engine.Player;
import exceptions.AbilityUseException;
import exceptions.ChampionDisarmedException;
import exceptions.InvalidTargetException;
import exceptions.LeaderAbilityAlreadyUsedException;
import exceptions.LeaderNotCurrentException;
import exceptions.NotEnoughResourcesException;
import exceptions.UnallowedMovementException;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.abilities.Ability;
import model.abilities.CrowdControlAbility;
import model.abilities.DamagingAbility;
import model.abilities.HealingAbility;
import model.effects.Effect;
import model.world.AntiHero;
import model.world.Champion;
import model.world.Cover;
import model.world.Damageable;
import model.world.Direction;
import model.world.Hero;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;


public class Main extends Application {
	
	private Stage window;
	private Game game;
	private MediaPlayer	mediaplayersound; 
	private Button [][]board ;
	
	public void start(Stage stage) throws InterruptedException, IOException
	{
		window = stage;
		Image icon = new Image("logo.Png");
		window.getIcons().add(icon);
		window.setTitle("marvel");
	
		//scene1();
		
		scene2();
		
		//scene3();
		
		//scene5();
		//info.display("captin","captin.jpg");
		
		window.show();
	}
	public void scene1() throws InterruptedException
	{
		File file = new File("/game2/vod/intro.mp4");
		Media media = new Media(file.toURI().toString()); 
		MediaPlayer mediaPlayer = new MediaPlayer(media);
		//mediaPlayer.setAutoPlay(true);
		MediaView mediaView = new MediaView (mediaPlayer);
		mediaPlayer.play();
		mediaView.autosize();
		Group root = new Group();  
		root.getChildren().add(mediaView) ; 
		Scene scene = new Scene(root,1200,700);  
		//window.setHeight(mediaView.getFitHeight());
		//window.setWidth(mediaView.getFitWidth());
		mediaView.setFitHeight(window.getHeight());
		mediaView.setFitWidth(window.getWidth());
		window.setScene(scene);
		
		mediaPlayer.setOnEndOfMedia(new Runnable() {
			
			@Override
			public void run() {
				scene2();
				
			}
		}
		
			);
		window.setResizable(false);
	}
	public void forbedinscene()
	{
		File file = new File("/game2/vod/forbedin.mp4");
		Media media = new Media(file.toURI().toString()); 
		MediaPlayer mediaPlayer = new MediaPlayer(media);
		//mediaPlayer.setAutoPlay(true);
		MediaView mediaView = new MediaView (mediaPlayer);
		mediaView.autosize();
		Group root = new Group();  
		root.getChildren().add(mediaView) ; 
		Scene scene = new Scene(root,1200,700);  
		//window.setHeight(mediaView.getFitHeight());
		//window.setWidth(mediaView.getFitWidth());
		mediaView.setFitHeight(window.getHeight());
		mediaView.setFitWidth(window.getWidth());
		window.setScene(scene);
		mediaPlayer.play();
	}
	
	public void scene2()
	{
		playmusic("/game2/sound/background1.mp4");
		
		GridPane grid = new GridPane();
		
		grid.setPadding(new Insets(10 ,10 ,10 ,10));
		grid.setVgap(8);
		grid.setHgap(8);
		
		TextField firstpl = new TextField();
		firstpl.setPromptText("first player");
		grid.setConstraints(firstpl, 60, 42);
		
		TextField secondpl = new TextField();
		secondpl.setPromptText("second player");
		grid.setConstraints(secondpl, 60, 43);
		
		Button next = new Button("next");
		next.setOnAction(e ->
		{
			if(check(firstpl,secondpl)) {
				game = new Game(new Player(firstpl.getText()),new Player(secondpl.getText()));
				try {
					game.loadAbilities("/game2/Abilities.csv");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					game.loadChampions("/game2/Champions.csv");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				scene3();
				//forbedinscene();
			}
		}
				);
		grid.setConstraints(next, 60, 44);
		
		grid.getChildren().addAll(firstpl,secondpl,next);
		Image image = new Image("3Ghrl1.png");
		ImageView iv = new ImageView(image);

		iv.setFitWidth(1200);
		iv.setFitHeight(700);
		
		Group group = new Group();
		group.getChildren().add(iv);
		
		StackPane sp = new StackPane();
		sp.getChildren().add(group);
		sp.getChildren().add(grid);
		
//		iv.setScaleX(0.5);
//		iv.setScaleY(0.5);
//		iv.setX(500);
//		iv.setY(500);
		//grid.setConstraints(iv, 53, 31);
		
		
		
		
//		grid.getChildren().addAll(group,firstpl,secondpl,next);
		
		
		
		
		Scene scene = new Scene(sp,1200,700);
		
		//scene.setFill(iv);
		window.setScene(scene);
		
	}
	
	static boolean firstpl ;
	static int firstte ;
	static ArrayList<Champion> firstplte ;
	static Champion firstplled;
	public void scene3()
	{
		firstpl = false;
		firstte = 0;
		firstplte = new ArrayList<>();
		firstplled = null;
		
		Image im1 = new Image("captin.jpg");
		ImageView iv1 = new ImageView(im1);
		iv1.setFitHeight(130);
		iv1.setFitWidth(145);
		
		Image im3 = new Image("Deadpool.jpg");
		ImageView iv3 = new ImageView(im3);
		iv3.setFitHeight(130);
		iv3.setFitWidth(145);
		
		Image im4 = new Image("Dr Strange.jpg");
		ImageView iv4 = new ImageView(im4);
		iv4.setFitHeight(130);
		iv4.setFitWidth(145);
		
		Image im5 = new Image("Electro.png");
		ImageView iv5 = new ImageView(im5);
		iv5.setFitHeight(130);
		iv5.setFitWidth(145);
		
		HBox hb1 = new HBox();
		//vb.setPadding(new Insets(0,10,10,0));
		hb1.getChildren().addAll(iv1,iv3,iv4,iv5);
		
		Image im6 = new Image("Ghost Rider.jpg");
		ImageView iv6 = new ImageView(im6);
		iv6.setFitHeight(130);
		iv6.setFitWidth(145);
		
		Image im7 = new Image("Hela.jpg");
		ImageView iv7 = new ImageView(im7);
		iv7.setFitHeight(130);
		iv7.setFitWidth(145);
		
		Image im8 = new Image("Hulk.jpg");
		ImageView iv8 = new ImageView(im8);
		iv8.setFitHeight(130);
		iv8.setFitWidth(145);
		
		Image im9 = new Image("Iceman.jpg");
		ImageView iv9 = new ImageView(im9);
		iv9.setFitHeight(130);
		iv9.setFitWidth(145);
		
		HBox hb2 = new HBox();
		//vb.setPadding(new Insets(0,10,10,0));
		hb2.getChildren().addAll(iv6,iv7,iv8,iv9);
		
		Image im10 = new Image("Ironman.jpg");
		ImageView iv10 = new ImageView(im10);
		iv10.setFitHeight(130);
		iv10.setFitWidth(145);
		
		Image im11 = new Image("Loki.jpg");
		ImageView iv11 = new ImageView(im11);
		iv11.setFitHeight(130);
		iv11.setFitWidth(145);
		
		Image im12 = new Image("Quicksilver.png");
		ImageView iv12 = new ImageView(im12);
		iv12.setFitHeight(130);
		iv12.setFitWidth(145);
		
		Image im13 = new Image("Spiderman.jpg");
		ImageView iv13 = new ImageView(im13);
		iv13.setFitHeight(130);
		iv13.setFitWidth(145);
		
		HBox hb3 = new HBox();
		//vb.setPadding(new Insets(0,10,10,0));
		hb3.getChildren().addAll(iv10,iv11,iv12,iv13);
		
		Image im14 = new Image("Thor.jpg");
		ImageView iv14 = new ImageView(im14);
		iv14.setFitHeight(130);
		iv14.setFitWidth(145);
		
		Image im15 = new Image("Venom.jpg");
		ImageView iv15 = new ImageView(im15);
		iv15.setFitHeight(130);
		iv15.setFitWidth(145);
		
		Image im16 = new Image("Yellow Jacket.jpg");
		ImageView iv16 = new ImageView(im16);
		iv16.setFitHeight(130);
		iv16.setFitWidth(145);
		
		HBox hb4 = new HBox();
		//vb.setPadding(new Insets(0,10,10,0));
		hb4.getChildren().addAll(iv14,iv15,iv16);
		hb4.setPadding(new Insets(0,0,0,80));
		//
		
		StackPane sp2 = new StackPane();
		sp2.setPadding(new Insets(300,210,100,100));
		//sp2.getChildren().add(ta);
		
		GridPane gp = new GridPane();
	//	gp.setPadding(new Insets(10 ,10 ,10 ,10));
		gp.setLayoutX(300);
		gp.setLayoutY(300);
		gp.setVgap(8);
		gp.setHgap(8);
		gp.setConstraints(hb1, 0, 6);
		gp.getChildren().add(hb1);
		gp.setConstraints(hb2, 0, 10);
		gp.getChildren().add(hb2);
		gp.setConstraints(hb3, 0, 14);
		gp.getChildren().add(hb3);
		
		
		
		
		gp.setConstraints(hb4, 0, 18);
		gp.getChildren().add(hb4);
		
		
		BorderPane bp = new BorderPane();
		bp.setLeft(sp2);
		bp.setCenter(gp);
		
		StackPane gr = new StackPane();
		HBox hb5 = new HBox();
		hb5.setTranslateX(330);
		hb5.setTranslateY(180);
		hb5.setSpacing(70);
		
		
		
		Label tmp = new Label(game.getFirstPlayer().getName());
		//tmp.setText("");
		tmp.setScaleX(5);
		tmp.setScaleY(5);
		tmp.setTextFill(Color.DARKGREEN);
		//tmp.setFont(Font.);
		//tmp.geta
		tmp.setTranslateX(-465);
		tmp.setTranslateY(-325);
		gr.getChildren().add(tmp);
		
		
		
		Label tmp2 = new Label("");
		tmp.setScaleX(5);
		tmp.setScaleY(5);
		tmp2.setTextFill(Color.DARKGREEN);
		//tmp.setFont(Font.);
		//tmp.geta
		tmp2.setTranslateX(-460);
		tmp2.setTranslateY(0);
		gr.getChildren().add(tmp2);
		
		//////////////////////////////////////////////////////////////////////////////////////
		
		Button CaptainAmerica = new Button("Captain America");
		CaptainAmerica.setOnAction(e->
		{
				if(!firstpl){
					if(CaptainAmerica.getText().equals("<      ready      >")) 
				{	
					firstte--;
					CaptainAmerica.setText("Captain America");
					for(int i=0;i!=firstplte.size();i++)
					{
						if(firstplte.get(i).getName().equals("Captain America"))
						{
							firstplte.remove(i);
							break;
						}
						
					}
					label(tmp2, firstplte,null);
				}
				else
				{
					if(firstte>=3)
					{
						alerts.display("your team is complete","alert");
					}
					else
					{
						firstte++;
						CaptainAmerica.setText("<      ready      >");
						for(int i=0;i!=game.getAvailableChampions().size();i++)
						{
							if(game.getAvailableChampions().get(i).getName().equals("Captain America"))
							{
								firstplte.add(game.getAvailableChampions().get(i));
								break;
							}
							
						}
						label(tmp2, firstplte,null);
					}
				}
		}
		else
			{
			if(CaptainAmerica.getText().equals("<      ready      >"))
				{
				for(Champion i : firstplte)
				{
					if(i.getName().equals("Captain America"))
					{
						firstplled = i;
						break;
					}
				}
				label(tmp2, firstplte,firstplled);
				}
				else
				{
					alerts.display("the leader should be from your team", "select leader");
				}
			}
		}
				);
		
		
		Button Deadpool = new Button("Deadpool");
		Deadpool.setOnAction(e->
		{
				if(!firstpl)
				{
					if(Deadpool.getText().equals("< ready >")) 
					{	
						firstte--;
						Deadpool.setText("Deadpool");
						for(int i=0;i!=firstplte.size();i++)
						{
							if(firstplte.get(i).getName().equals("Deadpool"))
							{
								firstplte.remove(i);
								break;
							}
							
						}
						label(tmp2, firstplte,null);
					}
					else
					{
						if(firstte>=3)
						{
							alerts.display("your team is complete","alert");
						}
						else
						{
							firstte++;
							Deadpool.setText("< ready >");
							for(int i=0;i!=game.getAvailableChampions().size();i++)
							{
								if(game.getAvailableChampions().get(i).getName().equals("Deadpool"))
								{
									firstplte.add(game.getAvailableChampions().get(i));
									break;
								}
								
							}
							label(tmp2, firstplte,null);
						}
					}
				}
				else
				{
					if(Deadpool.getText().equals("< ready >"))
					{
					for(Champion i : firstplte)
					{
						if(i.getName().equals("Deadpool"))
						{
							firstplled = i;
							break;
						}
					}
					label(tmp2, firstplte,firstplled);
					}
					else
					{
						alerts.display("the leader should be from your team", "select leader");
					}
				}
		}
				);
		
		Button DrStrange = new Button("Dr Strange");
		DrStrange.setOnAction(e->
		{
				if(!firstpl)
				{
					if(DrStrange.getText().equals("<  ready  >")) 
					{	
						firstte--;
						DrStrange.setText("Dr Strange");
						for(int i=0;i!=firstplte.size();i++)
						{
							if(firstplte.get(i).getName().equals("Dr Strange"))
							{
								firstplte.remove(i);
								break;
							}
							
						}
						label(tmp2, firstplte,null);
					}
					else
					{
						if(firstte>=3)
						{
							alerts.display("your team is complete","alert");
						}
						else
						{
							firstte++;
							DrStrange.setText("<  ready  >");
							for(int i=0;i!=game.getAvailableChampions().size();i++)
							{
								if(game.getAvailableChampions().get(i).getName().equals("Dr Strange"))
								{
									firstplte.add(game.getAvailableChampions().get(i));
									break;
								}
								
							}
							label(tmp2, firstplte,null);
						}
					}
				}
				else
				{
					if(DrStrange.getText().equals("<  ready  >"))
					{
					for(Champion i : firstplte)
					{
						if(i.getName().equals("Dr Strange"))
						{
							firstplled = i;
							break;
						}
					}
					label(tmp2, firstplte,firstplled);
					}
					else
					{
						alerts.display("the leader should be from your team", "select leader");
					}
				}
		}
				);
		
		Button Electro = new Button("Electro");
		Electro.setOnAction(e->
		{
				if(!firstpl)
				{
					if(Electro.getText().equals("<ready>")) 
					{	
						firstte--;
						Electro.setText("Electro");
						for(int i=0;i!=firstplte.size();i++)
						{
							if(firstplte.get(i).getName().equals("Electro"))
							{
								firstplte.remove(i);
								break;
							}
							
						}
						label(tmp2, firstplte,null);
					}
					else
					{
						if(firstte>=3)
						{
							alerts.display("your team is complete","alert");
						}
						else
						{
							firstte++;
							Electro.setText("<ready>");
							for(int i=0;i!=game.getAvailableChampions().size();i++)
							{
								if(game.getAvailableChampions().get(i).getName().equals("Electro"))
								{
									firstplte.add(game.getAvailableChampions().get(i));
									break;
								}
								
							}
							label(tmp2, firstplte,null);
						}
					}
				}
				else
				{
					if(Electro.getText().equals("<ready>"))
					{
					for(Champion i : firstplte)
					{
						if(i.getName().equals("Electro"))
						{
							firstplled = i;
							break;
						}
					}
					label(tmp2, firstplte,firstplled);
					}
					else
					{
						alerts.display("the leader should be from your team", "select leader");
					}
				}
		}
				);
		
		hb5.getChildren().addAll(CaptainAmerica,Deadpool,DrStrange,Electro);
		
		/////////////////
		
		HBox hb6 = new HBox();
		hb6.setTranslateX(335);
		hb6.setTranslateY(340);
		hb6.setSpacing(100);
		Button GhostRider = new Button("Ghost Rider");
		GhostRider.setOnAction(e->
		{
				if(!firstpl)
				{
					if(GhostRider.getText().equals("<  ready  >")) 
					{	
						firstte--;
						GhostRider.setText("Ghost Rider");
						for(int i=0;i!=firstplte.size();i++)
						{
							if(firstplte.get(i).getName().equals("Ghost Rider"))
							{
								firstplte.remove(i);
								break;
							}
							
						}
						label(tmp2, firstplte,null);
					}
					else
					{
						if(firstte>=3)
						{
							alerts.display("your team is complete","alert");
						}
						else
						{
							firstte++;
							GhostRider.setText("<  ready  >");
							for(int i=0;i!=game.getAvailableChampions().size();i++)
							{
								if(game.getAvailableChampions().get(i).getName().equals("Ghost Rider"))
								{
									firstplte.add(game.getAvailableChampions().get(i));
									break;
								}
								
							}
							label(tmp2, firstplte,null);
						}
					}
				}
				else
				{
					if(GhostRider.getText().equals("<  ready  >"))
					{
					for(Champion i : firstplte)
					{
						if(i.getName().equals("Ghost Rider"))
						{
							firstplled = i;
							break;
						}
					}
					label(tmp2, firstplte,firstplled);
					}
					else
					{
						alerts.display("the leader should be from your team", "select leader");
					}
				}
				
		}
				);
		
		Button Hela = new Button("Hela");
		Hela.setOnAction(e->
		{
				if(!firstpl)
				{
					if(Hela.getText().equals("<ready>")) 
					{	
						firstte--;
						Hela.setText("Hela");
						for(int i=0;i!=firstplte.size();i++)
						{
							if(firstplte.get(i).getName().equals("Hela"))
							{
								firstplte.remove(i);
								break;
							}
							
						}
						label(tmp2, firstplte,null);
					}
					else
					{
						if(firstte>=3)
						{
							alerts.display("your team is complete","alert");
						}
						else
						{
							firstte++;
							Hela.setText("<ready>");
							for(int i=0;i!=game.getAvailableChampions().size();i++)
							{
								if(game.getAvailableChampions().get(i).getName().equals("Hela"))
								{
									firstplte.add(game.getAvailableChampions().get(i));
									break;
								}
								
							}
							label(tmp2, firstplte,null);
						}
					}
				}
				else
				{
					if(Hela.getText().equals("<ready>"))
					{
					for(Champion i : firstplte)
					{
						if(i.getName().equals("Hela"))
						{
							firstplled = i;
							break;
						}
					}
					label(tmp2, firstplte,firstplled);
					}
					else
					{
						alerts.display("the leader should be from your team", "select leader");
					}
				}
		}
				);
		
		Button Hulk = new Button("Hulk");
		Hulk.setOnAction(e->
		{
				if(!firstpl)
				{
					if(Hulk.getText().equals("<ready>")) 
					{	
						firstte--;
						Hulk.setText("Hulk");
						for(int i=0;i!=firstplte.size();i++)
						{
							if(firstplte.get(i).getName().equals("Hulk"))
							{
								firstplte.remove(i);
								break;
							}
							
						}
						label(tmp2, firstplte,null);
					}
					else
					{
						if(firstte>=3)
						{
							alerts.display("your team is complete","alert");
						}
						else
						{
							firstte++;
							Hulk.setText("<ready>");
							for(int i=0;i!=game.getAvailableChampions().size();i++)
							{
								if(game.getAvailableChampions().get(i).getName().equals("Hulk"))
								{
									firstplte.add(game.getAvailableChampions().get(i));
									break;
								}
								
							}
							label(tmp2, firstplte,null);
						}
					}
				}
				else
				{
					if(Hulk.getText().equals("<ready>"))
					{
					for(Champion i : firstplte)
					{
						if(i.getName().equals("Hulk"))
						{
							firstplled = i;
							break;
						}
					}
					label(tmp2, firstplte,firstplled);
					}
					else
					{
						alerts.display("the leader should be from your team", "select leader");
					}
				}
		}
				);
		
		Button Iceman = new Button("Iceman");
		Iceman.setOnAction(e->
		{
				if(!firstpl)
				{
					if(Iceman.getText().equals("< ready >")) 
					{	
						firstte--;
						Iceman.setText("Iceman");
						for(int i=0;i!=firstplte.size();i++)
						{
							if(firstplte.get(i).getName().equals("Iceman"))
							{
								firstplte.remove(i);
								break;
							}
							
						}
						label(tmp2, firstplte,null);
					}
					else
					{
						if(firstte>=3)
						{
							alerts.display("your team is complete","alert");
						}
						else
						{
							firstte++;
							Iceman.setText("< ready >");
							for(int i=0;i!=game.getAvailableChampions().size();i++)
							{
								if(game.getAvailableChampions().get(i).getName().equals("Iceman"))
								{
									firstplte.add(game.getAvailableChampions().get(i));
									break;
								}
								
							}
							label(tmp2, firstplte,null);
						}
					}
				}
				else
				{
					if(Iceman.getText().equals("< ready >"))
					{
					for(Champion i : firstplte)
					{
						if(i.getName().equals("Iceman"))
						{
							firstplled = i;
							break;
						}
					}
					label(tmp2, firstplte,firstplled);
					}
					else
					{
						alerts.display("the leader should be from your team", "select leader");
					}
				}
		}
				);
		
		hb6.getChildren().addAll(GhostRider,Hela,Hulk,Iceman);
		
		HBox hb7 = new HBox();
		hb7.setTranslateX(350);
		hb7.setTranslateY(500);
		hb7.setSpacing(85);
		
		Button Ironman = new Button("Ironman");
		Ironman.setOnAction(e->
		{
				if(!firstpl)
				{
					if(Ironman.getText().equals("< ready >")) 
					{	
						firstte--;
						Ironman.setText("Ironman");
						for(int i=0;i!=firstplte.size();i++)
						{
							if(firstplte.get(i).getName().equals("Ironman"))
							{
								firstplte.remove(i);
								break;
							}
							
						}
						label(tmp2, firstplte,null);
					}
					else
					{
						if(firstte>=3)
						{
							alerts.display("your team is complete","alert");
						}
						else
						{
							firstte++;
							Ironman.setText("< ready >");
							for(int i=0;i!=game.getAvailableChampions().size();i++)
							{
								if(game.getAvailableChampions().get(i).getName().equals("Ironman"))
								{
									firstplte.add(game.getAvailableChampions().get(i));
									break;
								}
								
							}
							label(tmp2, firstplte,null);
						}
					}
				}
				else
				{
					if(Ironman.getText().equals("< ready >"))
					{
					for(Champion i : firstplte)
					{
						if(i.getName().equals("Ironman"))
						{
							firstplled = i;
							break;
						}
					}
					label(tmp2, firstplte,firstplled);
					}
					else
					{
						alerts.display("the leader should be from your team", "select leader");
					}
				}
		}
				);
		
		
		Button Loki = new Button("Loki");
		Loki.setOnAction(e->
		{
				if(!firstpl)
				{
					if(Loki.getText().equals("<ready>")) 
					{	
						firstte--;
						Loki.setText("Loki");
						for(int i=0;i!=firstplte.size();i++)
						{
							if(firstplte.get(i).getName().equals("Loki"))
							{
								firstplte.remove(i);
								break;
							}
							
						}
						label(tmp2, firstplte,null);
					}
					else
					{
						if(firstte>=3)
						{
							alerts.display("your team is complete","alert");
						}
						else
						{
							firstte++;
							Loki.setText("<ready>");
							for(int i=0;i!=game.getAvailableChampions().size();i++)
							{
								if(game.getAvailableChampions().get(i).getName().equals("Loki"))
								{
									firstplte.add(game.getAvailableChampions().get(i));
									break;
								}
								
							}
							label(tmp2, firstplte,null);
						}
					}
				}
				else
				{
					if(Loki.getText().equals("<ready>"))
					{
					for(Champion i : firstplte)
					{
						if(i.getName().equals("Loki"))
						{
							firstplled = i;
							break;
						}
					}
					label(tmp2, firstplte,firstplled);
					}
					else
					{
						alerts.display("the leader should be from your team", "select leader");
					}
				}
		}
				);
		
		Button Quicksilver = new Button("Quicksilver");
		Quicksilver.setOnAction(e->
		{
				if(!firstpl)
				{
					if(Quicksilver.getText().equals("<   ready   >")) 
					{	
						firstte--;
						Quicksilver.setText("Quicksilver");
						for(int i=0;i!=firstplte.size();i++)
						{
							if(firstplte.get(i).getName().equals("Quicksilver"))
							{
								firstplte.remove(i);
								break;
							}
							
						}
						label(tmp2, firstplte,null);
					}
					else
					{
						if(firstte>=3)
						{
							alerts.display("your team is complete","alert");
						}
						else
						{
							firstte++;
							Quicksilver.setText("<   ready   >");
							for(int i=0;i!=game.getAvailableChampions().size();i++)
							{
								if(game.getAvailableChampions().get(i).getName().equals("Quicksilver"))
								{
									firstplte.add(game.getAvailableChampions().get(i));
									break;
								}
								
							}
							label(tmp2, firstplte,null);
						}
					}
				}
				else
				{
					if(Quicksilver.getText().equals("<   ready   >"))
					{
					for(Champion i : firstplte)
					{
						if(i.getName().equals("Quicksilver"))
						{
							firstplled = i;
							break;
						}
					}
					label(tmp2, firstplte,firstplled);
					}
					else
					{
						alerts.display("the leader should be from your team", "select leader");
					}
				}
		}
				);
		
		Button Spiderman = new Button("Spiderman");
		Spiderman.setOnAction(e->
		{
				if(!firstpl)
				{
					if(Spiderman.getText().equals("<   ready   >")) 
					{	
						firstte--;
						Spiderman.setText("Spiderman");
						for(int i=0;i!=firstplte.size();i++)
						{
							if(firstplte.get(i).getName().equals("Spiderman"))
							{
								firstplte.remove(i);
								break;
							}
							
						}
						label(tmp2, firstplte,null);
					}
					else
					{
						if(firstte>=3)
						{
							alerts.display("your team is complete","alert");
						}
						else
						{
							firstte++;
							Spiderman.setText("<   ready   >");
							for(int i=0;i!=game.getAvailableChampions().size();i++)
							{
								if(game.getAvailableChampions().get(i).getName().equals("Spiderman"))
								{
									firstplte.add(game.getAvailableChampions().get(i));
									break;
								}
								
							}
							label(tmp2, firstplte,null);
						}
					}
				}
				else
				{
					if(Spiderman.getText().equals("<   ready   >"))
					{
					for(Champion i : firstplte)
					{
						if(i.getName().equals("Spiderman"))
						{
							firstplled = i;
							break;
						}
					}
					label(tmp2, firstplte,firstplled);
					}
					else
					{
						alerts.display("the leader should be from your team", "select leader");
					}
				}
		}
				);
		
		hb7.getChildren().addAll(Ironman,Loki,Quicksilver,Spiderman);
		
		
		HBox hb8 = new HBox();
		hb8.setTranslateX(445);
		hb8.setTranslateY(665);
		hb8.setSpacing(90);
		Button Thor = new Button("Thor");
		Thor.setOnAction(e->
		{
				if(!firstpl)
				{
					if(Thor.getText().equals("<ready>")) 
					{	
						firstte--;
						Thor.setText("Thor");
						for(int i=0;i!=firstplte.size();i++)
						{
							if(firstplte.get(i).getName().equals("Thor"))
							{
								firstplte.remove(i);
								break;
							}
							
						}
						label(tmp2, firstplte,null);
					}
					else
					{
						if(firstte>=3)
						{
							alerts.display("your team is complete","alert");
						}
						else
						{
							firstte++;
							Thor.setText("<ready>");
							for(int i=0;i!=game.getAvailableChampions().size();i++)
							{
								if(game.getAvailableChampions().get(i).getName().equals("Thor"))
								{
									firstplte.add(game.getAvailableChampions().get(i));
									break;
								}
								
							}
							label(tmp2, firstplte,null);
						}
					}
				}
				else
				{
					if(Thor.getText().equals("<ready>"))
					{
					for(Champion i : firstplte)
					{
						if(i.getName().equals("Thor"))
						{
							firstplled = i;
							break;
						}
					}
					label(tmp2, firstplte,firstplled);
					}
					else
					{
						alerts.display("the leader should be from your team", "select leader");
					}
				}
		}
				);
		
		Button Venom = new Button("Venom");
		Venom.setOnAction(e->
		{
				if(!firstpl)
				{
					if(Venom.getText().equals("< ready >")) 
					{	
						firstte--;
						Venom.setText("Venom");
						for(int i=0;i!=firstplte.size();i++)
						{
							if(firstplte.get(i).getName().equals("Venom"))
							{
								firstplte.remove(i);
								break;
							}
							
						}
						label(tmp2, firstplte,null);
					}
					else
					{
						if(firstte>=3)
						{
							alerts.display("your team is complete","alert");
						}
						else
						{
							firstte++;
							Venom.setText("< ready >");
							for(int i=0;i!=game.getAvailableChampions().size();i++)
							{
								if(game.getAvailableChampions().get(i).getName().equals("Venom"))
								{
									firstplte.add(game.getAvailableChampions().get(i));
									break;
								}
								
							}
							label(tmp2, firstplte,null);
						}
					}
				}
				else
				{
					if(Venom.getText().equals("< ready >"))
					{
					for(Champion i : firstplte)
					{
						if(i.getName().equals("Venom"))
						{
							firstplled = i;
							break;
						}
					}
					label(tmp2, firstplte,firstplled);
					}
					else
					{
						alerts.display("the leader should be from your team", "select leader");
					}
				}
				
		}
				);
		
		Button YellowJacket = new Button("Yellow Jacket");
		YellowJacket.setOnAction(e->
		{
				if(!firstpl)
				{
					if(YellowJacket.getText().equals("< ready >")) 
					{	
						firstte--;
						YellowJacket.setText("Yellow Jacket");
						for(int i=0;i!=firstplte.size();i++)
						{
							if(firstplte.get(i).getName().equals("Yellow Jacket"))
							{
								firstplte.remove(i);
								break;
							}
							
						}
						label(tmp2, firstplte,null);
					}
					else
					{
						if(firstte>=3)
						{
							alerts.display("your team is complete","alert");
						}
						else
						{
							firstte++;
							YellowJacket.setText("< ready >");
							for(int i=0;i!=game.getAvailableChampions().size();i++)
							{
								if(game.getAvailableChampions().get(i).getName().equals("Yellow Jacket"))
								{
									firstplte.add(game.getAvailableChampions().get(i));
									break;
								}
								
							}
							label(tmp2, firstplte,null);
						}
					}
				}
				else
				{
					if(YellowJacket.getText().equals("< ready >"))
					{
					for(Champion i : firstplte)
					{
						if(i.getName().equals("Yellow Jacket"))
						{
							firstplled = i;
							break;
						}
					}
					label(tmp2, firstplte,firstplled);
					}
					else
					{
						alerts.display("the leader should be from your team", "select leader");
					}
				}
		}
				);
		
		hb8.getChildren().addAll(Thor,Venom,YellowJacket);
		
		
		gr.getChildren().addAll(hb5,hb6,hb7,hb8);
		
		
		
		Button next = new Button("next");
		next.setTranslateX(500);
		next.setTranslateY(300);
		next.setScaleX(1.5);
		next.setScaleY(1.5);
		gr.getChildren().add(next);
		
		next.setOnAction(e ->
		{
			if(firstte==3) {
				if(firstpl&&firstplled!=null)
					scene4(tmp , tmp2);
				else
				{
					firstpl = true;
					alerts.display("select a leader from your team", "select leader");
				}
			}
			else
				alerts.display("your team is not complete","alert");
		}
				
				);
		
		
		////////////////////////////////////////////////////////////////////////////
		
		Image im2 = new Image("m.png");
		ImageView iv2 = new ImageView(im2);
		iv2.setFitHeight(700);
		iv2.setFitWidth(1200);
		Group g = new Group();
		g.getChildren().add(iv2);
		
		StackPane sp = new StackPane();
		sp.getChildren().add(g);
		sp.getChildren().add(bp);
		sp.getChildren().add(gr);
//		Button b = new Button("captin");
		


		
		Scene sc = new Scene(sp,1200,700);
		window.setScene(sc);
		//window.show();
	}
	
	static boolean secondpl = true;
	static int secondte = 0;
	static ArrayList<Champion> secondplte = new ArrayList<>();
	static Champion secondplled;
	public void scene4(Label fr , Label fr2)
	{
		secondpl = false;
		secondte = 0;
		secondplte = new ArrayList<>();
		secondplled=null;
		
		Image im1 = new Image("captin.jpg");
		ImageView iv1 = new ImageView(im1);
		iv1.setFitHeight(130);
		iv1.setFitWidth(145);
		
		Image im3 = new Image("Deadpool.jpg");
		ImageView iv3 = new ImageView(im3);
		iv3.setFitHeight(130);
		iv3.setFitWidth(145);
		
		Image im4 = new Image("Dr Strange.jpg");
		ImageView iv4 = new ImageView(im4);
		iv4.setFitHeight(130);
		iv4.setFitWidth(145);
		
		Image im5 = new Image("Electro.png");
		ImageView iv5 = new ImageView(im5);
		iv5.setFitHeight(130);
		iv5.setFitWidth(145);
		
		HBox hb1 = new HBox();
		//vb.setPadding(new Insets(0,10,10,0));
		hb1.getChildren().addAll(iv1,iv3,iv4,iv5);
		
		Image im6 = new Image("Ghost Rider.jpg");
		ImageView iv6 = new ImageView(im6);
		iv6.setFitHeight(130);
		iv6.setFitWidth(145);
		
		Image im7 = new Image("Hela.jpg");
		ImageView iv7 = new ImageView(im7);
		iv7.setFitHeight(130);
		iv7.setFitWidth(145);
		
		Image im8 = new Image("Hulk.jpg");
		ImageView iv8 = new ImageView(im8);
		iv8.setFitHeight(130);
		iv8.setFitWidth(145);
		
		Image im9 = new Image("Iceman.jpg");
		ImageView iv9 = new ImageView(im9);
		iv9.setFitHeight(130);
		iv9.setFitWidth(145);
		
		HBox hb2 = new HBox();
		//vb.setPadding(new Insets(0,10,10,0));
		hb2.getChildren().addAll(iv6,iv7,iv8,iv9);
		
		Image im10 = new Image("Ironman.jpg");
		ImageView iv10 = new ImageView(im10);
		iv10.setFitHeight(130);
		iv10.setFitWidth(145);
		
		Image im11 = new Image("Loki.jpg");
		ImageView iv11 = new ImageView(im11);
		iv11.setFitHeight(130);
		iv11.setFitWidth(145);
		
		Image im12 = new Image("Quicksilver.png");
		ImageView iv12 = new ImageView(im12);
		iv12.setFitHeight(130);
		iv12.setFitWidth(145);
		
		Image im13 = new Image("Spiderman.jpg");
		ImageView iv13 = new ImageView(im13);
		iv13.setFitHeight(130);
		iv13.setFitWidth(145);
		
		HBox hb3 = new HBox();
		//vb.setPadding(new Insets(0,10,10,0));
		hb3.getChildren().addAll(iv10,iv11,iv12,iv13);
		
		Image im14 = new Image("Thor.jpg");
		ImageView iv14 = new ImageView(im14);
		iv14.setFitHeight(130);
		iv14.setFitWidth(145);
		
		Image im15 = new Image("Venom.jpg");
		ImageView iv15 = new ImageView(im15);
		iv15.setFitHeight(130);
		iv15.setFitWidth(145);
		
		Image im16 = new Image("Yellow Jacket.jpg");
		ImageView iv16 = new ImageView(im16);
		iv16.setFitHeight(130);
		iv16.setFitWidth(145);
		
		HBox hb4 = new HBox();
		//vb.setPadding(new Insets(0,10,10,0));
		hb4.getChildren().addAll(iv14,iv15,iv16);
		hb4.setPadding(new Insets(0,0,0,80));
		//
		
		StackPane sp2 = new StackPane();
		sp2.setPadding(new Insets(300,210,100,100));
		//sp2.getChildren().add(ta);
		
		GridPane gp = new GridPane();
	//	gp.setPadding(new Insets(10 ,10 ,10 ,10));
		gp.setLayoutX(300);
		gp.setLayoutY(300);
		gp.setVgap(8);
		gp.setHgap(8);
		gp.setConstraints(hb1, 0, 6);
		gp.getChildren().add(hb1);
		gp.setConstraints(hb2, 0, 10);
		gp.getChildren().add(hb2);
		gp.setConstraints(hb3, 0, 14);
		gp.getChildren().add(hb3);
		
		
		
		
		gp.setConstraints(hb4, 0, 18);
		gp.getChildren().add(hb4);
		
		BorderPane bp = new BorderPane();
		bp.setLeft(sp2);
		bp.setCenter(gp);
		
		StackPane gr = new StackPane();
		HBox hb5 = new HBox();
		hb5.setTranslateX(330);
		hb5.setTranslateY(180);
		hb5.setSpacing(70);
		
		Label tmp = new Label(game.getSecondPlayer().getName());
		//tmp.setText("");
		tmp.setScaleX(5);
		tmp.setScaleY(5);
		tmp.setTextFill(Color.DARKGREEN);
		//tmp.setFont(Font.);
		//tmp.geta
		tmp.setTranslateX(465);
		tmp.setTranslateY(-325);
		gr.getChildren().add(tmp);
		
		Label tmp2 = new Label("");
		tmp.setScaleX(5);
		tmp.setScaleY(5);
		tmp2.setTextFill(Color.DARKGREEN);
		//tmp.setFont(Font.);
		//tmp.geta
		tmp2.setTranslateX(460);
		tmp2.setTranslateY(0);
		gr.getChildren().add(tmp2);
		
		gr.getChildren().addAll(fr,fr2);
		
		Button CaptainAmerica = new Button(isfirstplayerhas("Captain America")?"<      taken      >":"Captain America");
		CaptainAmerica.setOnAction(e->
		{
				
					if(!CaptainAmerica.getText().equals("<      taken      >")){
					if(!secondpl)
					{
						if(CaptainAmerica.getText().equals("<      ready      >")) 
						{	
							secondte--;
							CaptainAmerica.setText("Captain America");
							for(int i=0;i!=secondplte.size();i++)
							{
								if(secondplte.get(i).getName().equals("Captain America"))
								{
									secondplte.remove(i);
									break;
								}
								
							}
							label(tmp2, secondplte,null);
						}
						else
						{
							if(secondte>=3)
							{
								alerts.display("your team is complete","alert");
							}
							else
							{
								secondte++;
								CaptainAmerica.setText("<      ready      >");
								for(int i=0;i!=game.getAvailableChampions().size();i++)
								{
									if(game.getAvailableChampions().get(i).getName().equals("Captain America"))
									{
										secondplte.add(game.getAvailableChampions().get(i));
										break;
									}
									
								}
								label(tmp2, secondplte,null);
							}
						}
					}
						else
						{
							if(CaptainAmerica.getText().equals("<      ready      >"))
							{
							for(Champion i : secondplte)
							{
								if(i.getName().equals("Captain America"))
								{
									secondplled = i;
									break;
								}
							}
							label(tmp2, secondplte,secondplled);
							}
							else
							{
								alerts.display("the leader should be from your team", "select leader");
							}
						}
					}
				
				
				
		}
				);
		
		
		Button Deadpool = new Button(isfirstplayerhas("Deadpool")?"< taken >":"Deadpool");
		Deadpool.setOnAction(e->
		{
				if(!Deadpool.getText().equals("< taken >")){
				if(!secondpl)
				{
					if(Deadpool.getText().equals("< ready >")) 
					{	
						secondte--;
						Deadpool.setText("Deadpool");
						for(int i=0;i!=secondplte.size();i++)
						{
							if(secondplte.get(i).getName().equals("Deadpool"))
							{
								secondplte.remove(i);
								break;
							}
							
						}
						label(tmp2, secondplte,null);
					}
					else
					{
						if(secondte>=3)
						{
							alerts.display("your team is complete","alert");
						}
						else
						{
							secondte++;
							Deadpool.setText("< ready >");
							for(int i=0;i!=game.getAvailableChampions().size();i++)
							{
								if(game.getAvailableChampions().get(i).getName().equals("Deadpool"))
								{
									secondplte.add(game.getAvailableChampions().get(i));
									break;
								}
								
							}
							label(tmp2, secondplte,null);
						}
					}
				}
				else
				{
					if(Deadpool.getText().equals("< ready >"))
					{
					for(Champion i : secondplte)
					{
						if(i.getName().equals("Deadpool"))
						{
							secondplled = i;
							break;
						}
					}
					label(tmp2, secondplte,secondplled);
					}
					else
					{
						alerts.display("the leader should be from your team", "select leader");
					}
				}
		}
		}
				);
		
		Button DrStrange = new Button(isfirstplayerhas("Dr Strange")?"<  taken  >":"Dr Strange");
		DrStrange.setOnAction(e->
		{
				if(!DrStrange.getText().equals("<  taken  >")){
					if(!secondpl)
					{
						if(DrStrange.getText().equals("<  ready  >")) 
						{	
							secondte--;
							DrStrange.setText("Dr Strange");
							for(int i=0;i!=secondplte.size();i++)
							{
								if(secondplte.get(i).getName().equals("Dr Strange"))
								{
									secondplte.remove(i);
									break;
								}
								
							}
							label(tmp2, secondplte,null);
						}
						else
						{
							if(secondte>=3)
							{
								alerts.display("your team is complete","alert");
							}
							else
							{
								secondte++;
								DrStrange.setText("<  ready  >");
								for(int i=0;i!=game.getAvailableChampions().size();i++)
								{
									if(game.getAvailableChampions().get(i).getName().equals("Dr Strange"))
									{
										secondplte.add(game.getAvailableChampions().get(i));
										break;
									}
									
								}
								label(tmp2, secondplte,null);
							}
						}
					}
					else
					{
						if(DrStrange.getText().equals("<  ready  >"))
						{
						for(Champion i : secondplte)
						{
							if(i.getName().equals("Dr Strange"))
							{
								secondplled = i;
								break;
							}
						}
						label(tmp2, secondplte,secondplled);
						}
						else
						{
							alerts.display("the leader should be from your team", "select leader");
						}
					}
		}
		}
				);
		
		Button Electro = new Button(isfirstplayerhas("Electro")?"<taken>":"Electro");
		Electro.setOnAction(e->
		{
				if(!Electro.getText().equals("<taken>")){
					if(!secondpl)
					{
						if(Electro.getText().equals("<ready>")) 
						{	
							secondte--;
							Electro.setText("Electro");
							for(int i=0;i!=secondplte.size();i++)
							{
								if(secondplte.get(i).getName().equals("Electro"))
								{
									secondplte.remove(i);
									break;
								}
								
							}
							label(tmp2, secondplte,null);
						}
						else
						{
							if(secondte>=3)
							{
								alerts.display("your team is complete","alert");
							}
							else
							{
								secondte++;
								Electro.setText("<ready>");
								for(int i=0;i!=game.getAvailableChampions().size();i++)
								{
									if(game.getAvailableChampions().get(i).getName().equals("Electro"))
									{
										secondplte.add(game.getAvailableChampions().get(i));
										break;
									}
									
								}
								label(tmp2, secondplte,null);
							}
						}
					}
					else
					{
						if(Electro.getText().equals("<ready>"))
						{
						for(Champion i : secondplte)
						{
							if(i.getName().equals("Electro"))
							{
								secondplled = i;
								break;
							}
						}
						label(tmp2, secondplte,secondplled);
						}
						else
						{
							alerts.display("the leader should be from your team", "select leader");
						}
					}
		}
		}
				);
		
		hb5.getChildren().addAll(CaptainAmerica,Deadpool,DrStrange,Electro);
		
		/////////////////
		
		HBox hb6 = new HBox();
		hb6.setTranslateX(335);
		hb6.setTranslateY(340);
		hb6.setSpacing(100);
		Button GhostRider = new Button(isfirstplayerhas("Ghost Rider")?"<  taken  >":"Ghost Rider");
		GhostRider.setOnAction(e->
		{
				if(!GhostRider.getText().equals("<  taken  >")){
					if(!secondpl)
					{
						if(GhostRider.getText().equals("<  ready  >")) 
						{	
							secondte--;
							GhostRider.setText("Ghost Rider");
							for(int i=0;i!=secondplte.size();i++)
							{
								if(secondplte.get(i).getName().equals("Ghost Rider"))
								{
									secondplte.remove(i);
									break;
								}
								
							}
							label(tmp2, secondplte,null);
						}
						else
						{
							if(secondte>=3)
							{
								alerts.display("your team is complete","alert");
							}
							else
							{
								secondte++;
								GhostRider.setText("<  ready  >");
								for(int i=0;i!=game.getAvailableChampions().size();i++)
								{
									if(game.getAvailableChampions().get(i).getName().equals("Ghost Rider"))
									{
										secondplte.add(game.getAvailableChampions().get(i));
										break;
									}
									
								}
								label(tmp2, secondplte,null);
							}
						}
					}
					else
					{
						if(GhostRider.getText().equals("<  ready  >"))
						{
						for(Champion i : secondplte)
						{
							if(i.getName().equals("Ghost Rider"))
							{
								secondplled = i;
								break;
							}
						}
						label(tmp2, secondplte,secondplled);
						}
						else
						{
							alerts.display("the leader should be from your team", "select leader");
						}
					}
		}
		}
				);
		
		Button Hela = new Button(isfirstplayerhas("Hela")?"<taken>":"Hela");
		Hela.setOnAction(e->
		{
				if(!Hela.getText().equals("<taken>")){
					if(!secondpl)
					{
						if(Hela.getText().equals("<ready>")) 
						{	
							secondte--;
							Hela.setText("Hela");
							for(int i=0;i!=secondplte.size();i++)
							{
								if(secondplte.get(i).getName().equals("Hela"))
								{
									secondplte.remove(i);
									break;
								}
								
							}
							label(tmp2, secondplte,null);
						}
						else
						{
							if(secondte>=3)
							{
								alerts.display("your team is complete","alert");
							}
							else
							{
								secondte++;
								Hela.setText("<ready>");
								for(int i=0;i!=game.getAvailableChampions().size();i++)
								{
									if(game.getAvailableChampions().get(i).getName().equals("Hela"))
									{
										secondplte.add(game.getAvailableChampions().get(i));
										break;
									}
									
								}
								label(tmp2, secondplte,null);
							}
						}
					}
					else
					{
						if(Hela.getText().equals("<ready>"))
						{
						for(Champion i : secondplte)
						{
							if(i.getName().equals("Hela"))
							{
								secondplled = i;
								break;
							}
						}
						label(tmp2, secondplte,secondplled);
						}
						else
						{
							alerts.display("the leader should be from your team", "select leader");
						}
					}
		}
		}
				);
		
		Button Hulk = new Button(isfirstplayerhas("Hulk")?"<taken>":"Hulk");
		Hulk.setOnAction(e->
		{
				if(!Hulk.getText().equals("<taken>")){
					if(!secondpl)
					{
						if(Hulk.getText().equals("<ready>")) 
						{	
							secondte--;
							Hulk.setText("Hulk");
							for(int i=0;i!=secondplte.size();i++)
							{
								if(secondplte.get(i).getName().equals("Hulk"))
								{
									secondplte.remove(i);
									break;
								}
								
							}
							label(tmp2, secondplte,null);
						}
						else
						{
							if(secondte>=3)
							{
								alerts.display("your team is complete","alert");
							}
							else
							{
								secondte++;
								Hulk.setText("<ready>");
								for(int i=0;i!=game.getAvailableChampions().size();i++)
								{
									if(game.getAvailableChampions().get(i).getName().equals("Hulk"))
									{
										secondplte.add(game.getAvailableChampions().get(i));
										break;
									}
									
								}
								label(tmp2, secondplte,null);
							}
						}
					}
					else
					{
						if(Hulk.getText().equals("<ready>"))
						{
						for(Champion i : secondplte)
						{
							if(i.getName().equals("Hulk"))
							{
								secondplled = i;
								break;
							}
						}
						label(tmp2, secondplte,secondplled);
						}
						else
						{
							alerts.display("the leader should be from your team", "select leader");
						}
					}
		}
		}
				);
		
		Button Iceman = new Button(isfirstplayerhas("Iceman")?"< taken >":"Iceman");
		Iceman.setOnAction(e->
		{
				if(!Iceman.getText().equals("< taken >")){
					if(!secondpl)
					{
						if(Iceman.getText().equals("< ready >")) 
						{	
							secondte--;
							Iceman.setText("Iceman");
							for(int i=0;i!=secondplte.size();i++)
							{
								if(secondplte.get(i).getName().equals("Iceman"))
								{
									secondplte.remove(i);
									break;
								}
								
							}
							label(tmp2, secondplte,null);
						}
						else
						{
							if(secondte>=3)
							{
								alerts.display("your team is complete","alert");
							}
							else
							{
								secondte++;
								Iceman.setText("< ready >");
								for(int i=0;i!=game.getAvailableChampions().size();i++)
								{
									if(game.getAvailableChampions().get(i).getName().equals("Iceman"))
									{
										secondplte.add(game.getAvailableChampions().get(i));
										break;
									}
									
								}
								label(tmp2, secondplte,null);
							}
						}
					}
					else
					{
						if(Iceman.getText().equals("< ready >"))
						{
						for(Champion i : secondplte)
						{
							if(i.getName().equals("Iceman"))
							{
								secondplled = i;
								break;
							}
						}
						label(tmp2, secondplte,secondplled);
						}
						else
						{
							alerts.display("the leader should be from your team", "select leader");
						}
					}
		}
		}
				);
		
		hb6.getChildren().addAll(GhostRider,Hela,Hulk,Iceman);
		
		HBox hb7 = new HBox();
		hb7.setTranslateX(350);
		hb7.setTranslateY(500);
		hb7.setSpacing(85);
		
		Button Ironman = new Button(isfirstplayerhas("Ironman")?"< taken >":"Ironman");
		Ironman.setOnAction(e->
		{
				if(!Ironman.getText().equals("< taken >")){
					if(!secondpl)
					{
						if(Ironman.getText().equals("< ready >")) 
						{	
							secondte--;
							Ironman.setText("Ironman");
							for(int i=0;i!=secondplte.size();i++)
							{
								if(secondplte.get(i).getName().equals("Ironman"))
								{
									secondplte.remove(i);
									break;
								}
								
							}
							label(tmp2, secondplte,null);
						}
						else
						{
							if(secondte>=3)
							{
								alerts.display("your team is complete","alert");
							}
							else
							{
								secondte++;
								Ironman.setText("< ready >");
								for(int i=0;i!=game.getAvailableChampions().size();i++)
								{
									if(game.getAvailableChampions().get(i).getName().equals("Ironman"))
									{
										secondplte.add(game.getAvailableChampions().get(i));
										break;
									}
									
								}
								label(tmp2, secondplte,null);
							}
						}
					}
					else
					{
						if(Ironman.getText().equals("< ready >"))
						{
						for(Champion i : secondplte)
						{
							if(i.getName().equals("Ironman"))
							{
								secondplled = i;
								break;
							}
						}
						label(tmp2, secondplte,secondplled);
						}
						else
						{
							alerts.display("the leader should be from your team", "select leader");
						}
					}
		}
		}
				);
		
		
		Button Loki = new Button(isfirstplayerhas("Loki")?"<taken>":"Loki");
		Loki.setOnAction(e->
		{
			if(!Loki.getText().equals("<taken>")){
				if(!secondpl)
				{
					if(Loki.getText().equals("<ready>")) 
					{	
						secondte--;
						Loki.setText("Loki");
						for(int i=0;i!=secondplte.size();i++)
						{
							if(secondplte.get(i).getName().equals("Loki"))
							{
								secondplte.remove(i);
								break;
							}
							
						}
						label(tmp2, secondplte,null);
					}
					else
					{
						if(secondte>=3)
						{
							alerts.display("your team is complete","alert");
						}
						else
						{
							secondte++;
							Loki.setText("<ready>");
							for(int i=0;i!=game.getAvailableChampions().size();i++)
							{
								if(game.getAvailableChampions().get(i).getName().equals("Loki"))
								{
									secondplte.add(game.getAvailableChampions().get(i));
									break;
								}
								
							}
							label(tmp2, secondplte,null);
						}
					}
				}
				else
				{
					if(Loki.getText().equals("<ready>"))
					{
					for(Champion i : secondplte)
					{
						if(i.getName().equals("Loki"))
						{
							secondplled = i;
							break;
						}
					}
					label(tmp2, secondplte,secondplled);
					}
					else
					{
						alerts.display("the leader should be from your team", "select leader");
					}
				}
		}
		}
				);
		
		Button Quicksilver = new Button(isfirstplayerhas("Quicksilver")?"<   taken   >":"Quicksilver");
		Quicksilver.setOnAction(e->
		{
				if(!Quicksilver.getText().equals("<   taken   >")){
					if(!secondpl)
					{
						if(Quicksilver.getText().equals("<   ready   >")) 
						{	
							secondte--;
							Quicksilver.setText("Quicksilver");
							for(int i=0;i!=secondplte.size();i++)
							{
								if(secondplte.get(i).getName().equals("Quicksilver"))
								{
									secondplte.remove(i);
									break;
								}
								
							}
							label(tmp2, secondplte,null);
						}
						else
						{
							if(secondte>=3)
							{
								alerts.display("your team is complete","alert");
							}
							else
							{
								secondte++;
								Quicksilver.setText("<   ready   >");
								for(int i=0;i!=game.getAvailableChampions().size();i++)
								{
									if(game.getAvailableChampions().get(i).getName().equals("Quicksilver"))
									{
										secondplte.add(game.getAvailableChampions().get(i));
										break;
									}
									
								}
								label(tmp2, secondplte,null);
							}
						}
					}
					else
					{
						if(Quicksilver.getText().equals("<   ready   >"))
						{
						for(Champion i : secondplte)
						{
							if(i.getName().equals("Quicksilver"))
							{
								secondplled = i;
								break;
							}
						}
						label(tmp2, secondplte,secondplled);
						}
						else
						{
							alerts.display("the leader should be from your team", "select leader");
						}
					}
		}
		}
				);
		
		Button Spiderman = new Button(isfirstplayerhas("Spiderman")?"<   taken   >":"Spiderman");
		Spiderman.setOnAction(e->
		{
				if(!Spiderman.getText().equals("<   taken   >")){
					if(!secondpl)
					{
						if(Spiderman.getText().equals("<   ready   >")) 
						{	
							secondte--;
							Spiderman.setText("Spiderman");
							for(int i=0;i!=secondplte.size();i++)
							{
								if(secondplte.get(i).getName().equals("Spiderman"))
								{
									secondplte.remove(i);
									break;
								}
								
							}
							label(tmp2, secondplte,null);
						}
						else
						{
							if(secondte>=3)
							{
								alerts.display("your team is complete","alert");
							}
							else
							{
								secondte++;
								Spiderman.setText("<   ready   >");
								for(int i=0;i!=game.getAvailableChampions().size();i++)
								{
									if(game.getAvailableChampions().get(i).getName().equals("Spiderman"))
									{
										secondplte.add(game.getAvailableChampions().get(i));
										break;
									}
									
								}
								label(tmp2, secondplte,null);
							}
						}
					}
					else
					{
						if(Spiderman.getText().equals("<   ready   >"))
						{
						for(Champion i : secondplte)
						{
							if(i.getName().equals("Spiderman"))
							{
								secondplled = i;
								break;
							}
						}
						label(tmp2, secondplte,secondplled);
						}
						else
						{
							alerts.display("the leader should be from your team", "select leader");
						}
					}
		}
		}
				);
		
		hb7.getChildren().addAll(Ironman,Loki,Quicksilver,Spiderman);
		
		
		HBox hb8 = new HBox();
		hb8.setTranslateX(445);
		hb8.setTranslateY(665);
		hb8.setSpacing(90);
		Button Thor = new Button(isfirstplayerhas("Thor")?"<taken>":"Thor");
		Thor.setOnAction(e->
		{
				if(!Thor.getText().equals("<taken>")){
					if(!secondpl)
					{
						if(Thor.getText().equals("<ready>")) 
						{	
							secondte--;
							Thor.setText("Thor");
							for(int i=0;i!=secondplte.size();i++)
							{
								if(secondplte.get(i).getName().equals("Thor"))
								{
									secondplte.remove(i);
									break;
								}
								
							}
							label(tmp2, secondplte,null);
						}
						else
						{
							if(secondte>=3)
							{
								alerts.display("your team is complete","alert");
							}
							else
							{
								secondte++;
								Thor.setText("<ready>");
								for(int i=0;i!=game.getAvailableChampions().size();i++)
								{
									if(game.getAvailableChampions().get(i).getName().equals("Thor"))
									{
										secondplte.add(game.getAvailableChampions().get(i));
										break;
									}
									
								}
								label(tmp2, secondplte,null);
							}
						}
					}
					else
					{
						if(Thor.getText().equals("<ready>"))
						{
						for(Champion i : secondplte)
						{
							if(i.getName().equals("Thor"))
							{
								secondplled = i;
								break;
							}
						}
						label(tmp2, secondplte,secondplled);
						}
						else
						{
							alerts.display("the leader should be from your team", "select leader");
						}
					}
		}
		}
				);
		
		Button Venom = new Button(isfirstplayerhas("Venom")?"< taken >":"Venom");
		Venom.setOnAction(e->
		{
				if(!Venom.getText().equals("< taken >")){
					if(!secondpl)
					{
						if(Venom.getText().equals("< ready >")) 
						{	
							secondte--;
							Venom.setText("Venom");
							for(int i=0;i!=secondplte.size();i++)
							{
								if(secondplte.get(i).getName().equals("Venom"))
								{
									secondplte.remove(i);
									break;
								}
								
							}
							label(tmp2, secondplte,null);
						}
						else
						{
							if(secondte>=3)
							{
								alerts.display("your team is complete","alert");
							}
							else
							{
								secondte++;
								Venom.setText("< ready >");
								for(int i=0;i!=game.getAvailableChampions().size();i++)
								{
									if(game.getAvailableChampions().get(i).getName().equals("Venom"))
									{
										secondplte.add(game.getAvailableChampions().get(i));
										break;
									}
									
								}
								label(tmp2, secondplte,null);
							}
						}
					}
					else
					{
						if(Venom.getText().equals("< ready >"))
						{
						for(Champion i : secondplte)
						{
							if(i.getName().equals("Venom"))
							{
								secondplled = i;
								break;
							}
						}
						label(tmp2, secondplte,secondplled);
						}
						else
						{
							alerts.display("the leader should be from your team", "select leader");
						}
					}
					
		}
		}
				);
		
		Button YellowJacket = new Button(isfirstplayerhas("Yellow Jacket")?"< taken >":"Yellow Jacket");
		YellowJacket.setOnAction(e->
		{
			if(!YellowJacket.getText().equals("< taken >")){	
			if(!secondpl)
			{
				if(YellowJacket.getText().equals("< ready >")) 
				{	
					secondte--;
					YellowJacket.setText("Yellow Jacket");
					for(int i=0;i!=secondplte.size();i++)
					{
						if(secondplte.get(i).getName().equals("Yellow Jacket"))
						{
							secondplte.remove(i);
							break;
						}
						
					}
					label(tmp2, secondplte,null);
				}
				else
				{
					if(secondte>=3)
					{
						alerts.display("your team is complete","alert");
					}
					else
					{
						secondte++;
						YellowJacket.setText("< ready >");
						for(int i=0;i!=game.getAvailableChampions().size();i++)
						{
							if(game.getAvailableChampions().get(i).getName().equals("Yellow Jacket"))
							{
								secondplte.add(game.getAvailableChampions().get(i));
								break;
							}
							
						}
						label(tmp2, secondplte,null);
					}
				}
			}
			else
			{
				if(YellowJacket.getText().equals("< ready >"))
				{
				for(Champion i : secondplte)
				{
					if(i.getName().equals("Yellow Jacket"))
					{
						secondplled = i;
						break;
					}
				}
				label(tmp2, secondplte,secondplled);
				}
				else
				{
					alerts.display("the leader should be from your team", "select leader");
				}
			}
		}
		}
				);
		
		hb8.getChildren().addAll(Thor,Venom,YellowJacket);
		
		
		gr.getChildren().addAll(hb5,hb6,hb7,hb8);
		
		Button next = new Button("next");
		next.setTranslateX(500);
		next.setTranslateY(300);
		next.setScaleX(1.5);
		next.setScaleY(1.5);
		gr.getChildren().add(next);
		
		next.setOnAction(e ->
		{
			if(secondte==3) {
				if(secondpl&&secondplled!=null) 
				{
					try {
						mediaplayersound.setMute(true);
						playmusic("/game2/sound/Most Epic Music Ever_ _The Wolf And The Moon_ by BrunuhVille.mp4");
						Player pl1 = new Player(game.getFirstPlayer().getName());
						Player pl2 = new Player(game.getSecondPlayer().getName());
						for(Champion i : firstplte)
						{
							pl1.getTeam().add(i);
						}
						for(Champion i : secondplte)
						{
							pl2.getTeam().add(i);
						}
						pl1.setLeader(firstplled);
						pl2.setLeader(secondplled);
						
						game = new Game(pl1, pl2);
						game.loadAbilities("/game2/Abilities.csv");
						game.loadChampions("/game2/Champions.csv");
						scene5();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
					
				else
				{
					secondpl = true;
					alerts.display("select a leader from your team", "select leader");
				}
			}
			else
				alerts.display("your team is not complete","alert");
		}
				
				);
		Button prev = new Button("prev");
		prev.setTranslateX(-500);
		prev.setTranslateY(300);
		prev.setScaleX(1.5);
		prev.setScaleY(1.5);
		gr.getChildren().add(prev);
		
		prev.setOnAction(e ->
		{
			scene3();
		}
				
				);
		Image im2 = new Image("m.png");
		ImageView iv2 = new ImageView(im2);
		iv2.setFitHeight(700);
		iv2.setFitWidth(1200);
		Group g = new Group();
		g.getChildren().add(iv2);
		
		StackPane sp = new StackPane();
		sp.getChildren().add(g);
		sp.getChildren().add(bp);
		sp.getChildren().add(gr);
//		Button b = new Button("captin");
		


		
		Scene sc = new Scene(sp,1200,700);
		window.setScene(sc);
		//window.show();
		
	}
	static HBox nextcha;
	static Label pl3 ;
	static Label pl4 ;
	//static boolean done;
	static int i;
	static int j;
	public void scene5() throws IOException
	{
		
		
		
		
		
		
		StackPane sp2 = new StackPane();
		sp2.setBackground(Background.fill(Color.BLACK));
		Image image = new Image("board7.png");
		ImageView iv = new ImageView(image);
		iv.setFitWidth(800);
		iv.setFitHeight(600);
		
		Group group = new Group();
		group.getChildren().add(iv);
		
		
		
		Group group2 = new Group();
		
		Image image2 = new Image("board3.png");
		ImageView iv2 = new ImageView(image2);

		iv2.setFitWidth(500);
		iv2.setFitHeight(350);
		group2.getChildren().add(iv2);
		
		board = new Button [5][5];
		{
//			b1.setBackground(Background.fill(Color.GREEN));
			{
//    			b3.setScaleX(6);
//			    b3.setScaleY(2.5);
			}
			{
//				b3.setScaleX(1);
//				b3.setScaleY(1);
//				iv3.setFitHeight(55);
//				iv3.setFitWidth(75);
			}
		}
	

		Button b1 = new Button();
		b1.setScaleX(6);
		b1.setScaleY(2.5);
		b1.setTranslateX(-198);
		b1.setTranslateY(-140);
		board[0][0] = b1;
		b1.setOnAction(e ->
		{
			i=0;
			j=0;
			
				if(game.getBoard()[i][j] instanceof Cover)
				{
					alerts.display(getall((Damageable) game.getBoard()[i][j])[0], "cover");
				}
				else
				{
					String []tmp = getall((Damageable) game.getBoard()[i][j]);
					info.display(tmp[0], tmp[1]);
				}
			
		}
		);
		
		
		
		Button b2 = new Button();
		b2.setScaleX(6);
		b2.setScaleY(2.5);
		b2.setTranslateX(-198+99);
		b2.setTranslateY(-140);
		board[0][1] = b2;
		b2.setOnAction(e ->
		{
			i=0;
			j=1;
			
				if(game.getBoard()[i][j] instanceof Cover)
				{
					alerts.display(getall((Damageable) game.getBoard()[i][j])[0], "cover");
				}
				else
				{
					String []tmp = getall((Damageable) game.getBoard()[i][j]);
					info.display(tmp[0], tmp[1]);
				}
				
		}
		);
		
		
		Button b3 = new Button();
		b3.setScaleX(6);
		b3.setScaleY(2.5);
		b3.setTranslateX(-198+99*2);
		b3.setTranslateY(-140);
		board[0][2] = b3;
		b3.setOnAction(e ->
		{
			i=0;
			j=2;
			
				if(game.getBoard()[i][j] instanceof Cover)
				{
					alerts.display(getall((Damageable) game.getBoard()[i][j])[0], "cover");
				}
				else
				{
					String []tmp = getall((Damageable) game.getBoard()[i][j]);
					info.display(tmp[0], tmp[1]);
				}
				
		}
		);
	
		
		Button b4 = new Button();
		b4.setScaleX(6);
		b4.setScaleY(2.5);
		b4.setTranslateX(-198+99*3);
		b4.setTranslateY(-140);
		board[0][3] = b4;
		b4.setOnAction(e ->
		{
			i=0;
			j=3;
			
				if(game.getBoard()[i][j] instanceof Cover)
				{
					alerts.display(getall((Damageable) game.getBoard()[i][j])[0], "cover");
				}
				else
				{
					String []tmp = getall((Damageable) game.getBoard()[i][j]);
					info.display(tmp[0], tmp[1]);
				}
				
		}
		);
	
		
		Button b5 = new Button();
		b5.setScaleX(6);
		b5.setScaleY(2.5);
		b5.setTranslateX(-198+99*4);
		b5.setTranslateY(-140);
		board[0][4] = b5;
		b5.setOnAction(e ->
		{
			i=0;
			j=4;
			
				if(game.getBoard()[i][j] instanceof Cover)
				{
					alerts.display(getall((Damageable) game.getBoard()[i][j])[0], "cover");
				}
				else
				{
					String []tmp = getall((Damageable) game.getBoard()[i][j]);
					info.display(tmp[0], tmp[1]);
				}
			
		}
		);
	
		
		
		Button b6 = new Button();
		b6.setScaleX(6);
		b6.setScaleY(2.5);
		b6.setTranslateX(-198);
		b6.setTranslateY(-140+70);
		board[1][0] = b6;
		b6.setOnAction(e ->
		{
			i=1;
			j=0;
			
				if(game.getBoard()[i][j] instanceof Cover)
				{
					alerts.display(getall((Damageable) game.getBoard()[i][j])[0], "cover");
				}
				else
				{
					String []tmp = getall((Damageable) game.getBoard()[i][j]);
					info.display(tmp[0], tmp[1]);
				}
				
		}
		);
	
		
		Button b7 = new Button();
		b7.setScaleX(6);
		b7.setScaleY(2.5);
		b7.setTranslateX(-198+99);
		b7.setTranslateY(-140+70);
		board[1][1] = b7;
		b7.setOnAction(e ->
		{
			i=1;
			j=1;
			
				if(game.getBoard()[i][j] instanceof Cover)
				{
					alerts.display(getall((Damageable) game.getBoard()[i][j])[0], "cover");
				}
				else
				{
					String []tmp = getall((Damageable) game.getBoard()[i][j]);
					info.display(tmp[0], tmp[1]);
				}
				
		}
		);
		
		
		
		Button b8 = new Button();
		b8.setScaleX(6);
		b8.setScaleY(2.5);
		b8.setTranslateX(-198+99*2);
		b8.setTranslateY(-140+70);
		board[1][2] = b8;
		b8.setOnAction(e ->
		{
			i=1;
			j=2;
			
				if(game.getBoard()[i][j] instanceof Cover)
				{
					alerts.display(getall((Damageable) game.getBoard()[i][j])[0], "cover");
				}
				else
				{
					String []tmp = getall((Damageable) game.getBoard()[i][j]);
					info.display(tmp[0], tmp[1]);
				}
				
		}
		);
		
		
		Button b9 = new Button();
		b9.setScaleX(6);
		b9.setScaleY(2.5);
		b9.setTranslateX(-198+99*3);
		b9.setTranslateY(-140+70);
		board[1][3] = b9;
		b9.setOnAction(e ->
		{
			i=1;
			j=3;
			
				if(game.getBoard()[i][j] instanceof Cover)
				{
					alerts.display(getall((Damageable) game.getBoard()[i][j])[0], "cover");
				}
				else
				{
					String []tmp = getall((Damageable) game.getBoard()[i][j]);
					info.display(tmp[0], tmp[1]);
				}
			
		}
		);
		
		
		Button b10 = new Button();
		b10.setScaleX(6);
		b10.setScaleY(2.5);
		b10.setTranslateX(-198+99*4);
		b10.setTranslateY(-140+70);
		board[1][4] = b10;
		b10.setOnAction(e ->
		{
			i=1;
			j=4;
			
				if(game.getBoard()[i][j] instanceof Cover)
				{
					alerts.display(getall((Damageable) game.getBoard()[i][j])[0], "cover");
				}
				else
				{
					String []tmp = getall((Damageable) game.getBoard()[i][j]);
					info.display(tmp[0], tmp[1]);
				}
				
		}
		);
	
		
		Button b11 = new Button();
		b11.setScaleX(6);
		b11.setScaleY(2.5);
		b11.setTranslateX(-198);
		b11.setTranslateY(-140+70*2);
		board[2][0] = b11;
		b11.setOnAction(e ->
		{
			i=2;
			j=0;
			
				if(game.getBoard()[i][j] instanceof Cover)
				{
					alerts.display(getall((Damageable) game.getBoard()[i][j])[0], "cover");
				}
				else
				{
					String []tmp = getall((Damageable) game.getBoard()[i][j]);
					info.display(tmp[0], tmp[1]);
				}
			
		}
		);
		
		
		Button b12 = new Button();
		b12.setScaleX(6);
		b12.setScaleY(2.5);
		b12.setTranslateX(-198+99);
		b12.setTranslateY(-140+70*2);
		board[2][1] = b12;
		b12.setOnAction(e ->
		{
			i=2;
			j=1;
			
				if(game.getBoard()[i][j] instanceof Cover)
				{
					alerts.display(getall((Damageable) game.getBoard()[i][j])[0], "cover");
				}
				else
				{
					String []tmp = getall((Damageable) game.getBoard()[i][j]);
					info.display(tmp[0], tmp[1]);
				}
			
		}
		);
		
		
		Button b13 = new Button();
		b13.setScaleX(6);
		b13.setScaleY(2.5);
		b13.setTranslateX(-198+99*2);
		b13.setTranslateY(-140+70*2);
		board[2][2] = b13;
		b13.setOnAction(e ->
		{
			i=2;
			j=2;
			
				if(game.getBoard()[i][j] instanceof Cover)
				{
					alerts.display(getall((Damageable) game.getBoard()[i][j])[0], "cover");
				}
				else
				{
					String []tmp = getall((Damageable) game.getBoard()[i][j]);
					info.display(tmp[0], tmp[1]);
				}
			
		}
		);
		
		
		Button b14 = new Button();
		b14.setScaleX(6);
		b14.setScaleY(2.5);
		b14.setTranslateX(-198+99*3);
		b14.setTranslateY(-140+70*2);
		board[2][3] = b14;
		b14.setOnAction(e ->
		{
			i=2;
			j=3;
			
				if(game.getBoard()[i][j] instanceof Cover)
				{
					alerts.display(getall((Damageable) game.getBoard()[i][j])[0], "cover");
				}
				else
				{
					String []tmp = getall((Damageable) game.getBoard()[i][j]);
					info.display(tmp[0], tmp[1]);
				}
			
		}
		);
		
		
		Button b15 = new Button();
		b15.setScaleX(6);
		b15.setScaleY(2.5);
		b15.setTranslateX(-198+99*4);
		b15.setTranslateY(-140+70*2);
		board[2][4] = b15;
		b15.setOnAction(e ->
		{
			i=2;
			j=4;
			
				if(game.getBoard()[i][j] instanceof Cover)
				{
					alerts.display(getall((Damageable) game.getBoard()[i][j])[0], "cover");
				}
				else
				{
					String []tmp = getall((Damageable) game.getBoard()[i][j]);
					info.display(tmp[0], tmp[1]);
				}
			
		}
		);
	
		
		Button b16 = new Button();
		b16.setScaleX(6);
		b16.setScaleY(2.5);
		b16.setTranslateX(-198);
		b16.setTranslateY(-140+70*3);
		board[3][0] = b16;
		b16.setOnAction(e ->
		{
			i=3;
			j=0;
			
				if(game.getBoard()[i][j] instanceof Cover)
				{
					alerts.display(getall((Damageable) game.getBoard()[i][j])[0], "cover");
				}
				else
				{
					String []tmp = getall((Damageable) game.getBoard()[i][j]);
					info.display(tmp[0], tmp[1]);
				}
			
		}
		);
		
		
		Button b17 = new Button();
		b17.setScaleX(6);
		b17.setScaleY(2.5);
		b17.setTranslateX(-198+99);
		b17.setTranslateY(-140+70*3);
		board[3][1] = b17;
		b17.setOnAction(e ->
		{
			i=3;
			j=1;
			
				if(game.getBoard()[i][j] instanceof Cover)
				{
					alerts.display(getall((Damageable) game.getBoard()[i][j])[0], "cover");
				}
				else
				{
					String []tmp = getall((Damageable) game.getBoard()[i][j]);
					info.display(tmp[0], tmp[1]);
				}
				
		}
		);
		
		
		Button b18 = new Button();
		b18.setScaleX(6);
		b18.setScaleY(2.5);
		b18.setTranslateX(-198+99*2);
		b18.setTranslateY(-140+70*3);
		board[3][2] = b18;
		b18.setOnAction(e ->
		{
			i=3;
			j=2;
			
				if(game.getBoard()[i][j] instanceof Cover)
				{
					alerts.display(getall((Damageable) game.getBoard()[i][j])[0], "cover");
				}
				else
				{
					String []tmp = getall((Damageable) game.getBoard()[i][j]);
					info.display(tmp[0], tmp[1]);
				}
			
		}
		);
	
		
		Button b19 = new Button();
		b19.setScaleX(6);
		b19.setScaleY(2.5);
		b19.setTranslateX(-198+99*3);
		b19.setTranslateY(-140+70*3);
		board[3][3] = b19;
		b19.setOnAction(e ->
		{
			i=3;
			j=3;
			
				if(game.getBoard()[i][j] instanceof Cover)
				{
					alerts.display(getall((Damageable) game.getBoard()[i][j])[0], "cover");
				}
				else
				{
					String []tmp = getall((Damageable) game.getBoard()[i][j]);
					info.display(tmp[0], tmp[1]);
				}
			
		}
		);
	
		
		Button b20 = new Button();
		b20.setScaleX(6);
		b20.setScaleY(2.5);
		b20.setTranslateX(-198+99*4);
		b20.setTranslateY(-140+70*3);
		board[3][4] = b20;
		b20.setOnAction(e ->
		{
			i=3;
			j=4;
			
				if(game.getBoard()[i][j] instanceof Cover)
				{
					alerts.display(getall((Damageable) game.getBoard()[i][j])[0], "cover");
				}
				else
				{
					String []tmp = getall((Damageable) game.getBoard()[i][j]);
					info.display(tmp[0], tmp[1]);
				}
				
		}
		);


		

		Button b21 = new Button();
		b21.setScaleX(6);
		b21.setScaleY(2.5);
		b21.setTranslateX(-198);
		b21.setTranslateY(-140+70*4);
		board[4][0] = b21;
		b21.setOnAction(e ->
		{
			i=4;
			j=0;
			
				if(game.getBoard()[i][j] instanceof Cover)
				{
					alerts.display(getall((Damageable) game.getBoard()[i][j])[0], "cover");
				}
				else
				{
					String []tmp = getall((Damageable) game.getBoard()[i][j]);
					info.display(tmp[0], tmp[1]);
				}
			
		}
		);
	
		
		Button b22 = new Button();
		b22.setScaleX(6);
		b22.setScaleY(2.5);
		b22.setTranslateX(-198+99);
		b22.setTranslateY(-140+70*4);
		board[4][1] = b22;
		b22.setOnAction(e ->
		{
			i=4;
			j=1;
			
				if(game.getBoard()[i][j] instanceof Cover)
				{
					alerts.display(getall((Damageable) game.getBoard()[i][j])[0], "cover");
				}
				else
				{
					String []tmp = getall((Damageable) game.getBoard()[i][j]);
					info.display(tmp[0], tmp[1]);
				}
			
		}
		);
		
		
		Button b23 = new Button();
		b23.setScaleX(6);
		b23.setScaleY(2.5);
		b23.setTranslateX(-198+99*2);
		b23.setTranslateY(-140+70*4);
		board[4][2] = b23;
		b23.setOnAction(e ->
		{
			i=4;
			j=2;
			
				if(game.getBoard()[i][j] instanceof Cover)
				{
					alerts.display(getall((Damageable) game.getBoard()[i][j])[0], "cover");
				}
				else
				{
					String []tmp = getall((Damageable) game.getBoard()[i][j]);
					info.display(tmp[0], tmp[1]);
				}
			
		}
		);
		
		
		Button b24 = new Button();
		b24.setScaleX(6);
		b24.setScaleY(2.5);
		b24.setTranslateX(-198+99*3);
		b24.setTranslateY(-140+70*4);
		board[4][3] = b24;
		b24.setOnAction(e ->
		{
			i=4;
			j=3;
			
				if(game.getBoard()[i][j] instanceof Cover)
				{
					alerts.display(getall((Damageable) game.getBoard()[i][j])[0], "cover");
				}
				else
				{
					String []tmp = getall((Damageable) game.getBoard()[i][j]);
					info.display(tmp[0], tmp[1]);
				}
			
		}
		);
	
		
		Button b25 = new Button();
		b25.setScaleX(6);
		b25.setScaleY(2.5);
		b25.setTranslateX(-198+99*4);
		b25.setTranslateY(-140+70*4);
		board[4][4] = b25;
		b25.setOnAction(e ->
		{
			i=4;
			j=4;
	
				if(game.getBoard()[i][j] instanceof Cover)
				{
					alerts.display(getall((Damageable) game.getBoard()[i][j])[0], "cover");
				}
				else
				{
					String []tmp = getall((Damageable) game.getBoard()[i][j]);
					info.display(tmp[0], tmp[1]);
				}
			
		}
		);
		
		
		Image image3 = new Image("shape2.png");
		Image image4 = new Image("shape2.png");
		ImageView iv3 = new ImageView(image3);
		ImageView iv4 = new ImageView(image4);
		iv3.setTranslateX(-460);
		iv3.setTranslateY(-280);
		iv4.setTranslateX(460);
		iv4.setTranslateY(-280);
		Label name1 = new Label(game.getFirstPlayer().getName());	
		Label name2 = new Label(game.getSecondPlayer().getName());
		name1.setTextFill(Color.LIGHTGREEN);
		name1.setScaleX(3);
		name1.setScaleY(3);
		name1.setTranslateX(-460);
		name1.setTranslateY(-280);
		name2.setTextFill(Color.LIGHTGREEN);
		name2.setScaleX(3);
		name2.setScaleY(3);
		name2.setTranslateX(460);
		name2.setTranslateY(-280);
		
		Image image5 = new Image("shape3.png");
		Image image6 = new Image("shape3.png");
		ImageView iv5 = new ImageView(image5);
		ImageView iv6 = new ImageView(image6);
		iv5.setTranslateX(-460);
		iv5.setTranslateY(50);
		iv5.setScaleY(0.8);
		iv6.setTranslateX(460);
		iv6.setTranslateY(50);
		iv6.setScaleY(0.8);
		
		Image image7 = new Image("shape2.png");
		ImageView iv7 = new ImageView(image7);
		iv7.setTranslateX(0);
		iv7.setTranslateY(-281);
		
		
		
		
		Image image8 = new Image("shape4.png");
		ImageView iv8 = new ImageView(image8);
		iv8.setTranslateX(210-75);
		iv8.setTranslateY(290);
		iv8.setScaleX(0.5);
		iv8.setScaleY(0.5);
		
		Label useleaderab = new Label("use Leader Ability");
		useleaderab.setTextFill(Color.LIGHTGREEN);
		useleaderab.setScaleX(1.5);
		useleaderab.setScaleY(1.5);
		useleaderab.setTranslateX(210-75);
		useleaderab.setTranslateY(230);
		Button useleaderability = new Button("use");
		useleaderability.setTranslateX(210-75);
		useleaderability.setTranslateY(290);
		useleaderability.setScaleX(1.2);
		useleaderability.setScaleY(1.5);
		useleaderability.setBackground(Background.fill(Color.BLACK));
		useleaderability.setTextFill(Color.DARKGREEN);
		useleaderability.setOnAction(e ->
		{
			try {
			game.useLeaderAbility();
			try {
				scene5();
			} catch (IOException e1) {
				
				e1.printStackTrace();
			}
			if(game.checkGameOver()!=null) {
				alerts.display(game.checkGameOver().getName()+" Winner ", "end game");
				
				scene6();
			}
			
			}
			catch(LeaderAbilityAlreadyUsedException e1)
			{
				alerts.display(e1.getMessage(), "warning");
			}
			catch(LeaderNotCurrentException e2)
			{
				alerts.display(e2.getMessage(), "warning");
			}
			catch (CloneNotSupportedException e2) {
				System.out.println("lol");
			}
		}
		);
		
		Image image9 = new Image("shape4.png");
		ImageView iv9 = new ImageView(image9);
		iv9.setTranslateX(80-75);
		iv9.setTranslateY(290);
		iv9.setScaleX(0.5);
		iv9.setScaleY(0.5);
		
		Label useleaderab2 = new Label("use Ability");
		useleaderab2.setTextFill(Color.LIGHTGREEN);
		useleaderab2.setScaleX(1.5);
		useleaderab2.setScaleY(1.5);
		useleaderab2.setTranslateX(80-75);
		useleaderab2.setTranslateY(230);
		Button useability = new Button("use");
		useability.setTranslateX(80-75);
		useability.setTranslateY(290);
		useability.setScaleX(1.2);
		useability.setScaleY(1.5);
		useability.setBackground(Background.fill(Color.BLACK));
		useability.setTextFill(Color.DARKGREEN);
		useability.setOnAction(e ->
		{
			Object [] tm = chooseab.display(((Champion)game.getTurnOrder().peekMin()).getAbilities());
			if((boolean) tm[0])
			{
				if(((Ability)tm[1]).getCastArea().name().equals("DIRECTIONAL"))
				{
					Object [] tm2 = direction.display();
					if((boolean) tm2[0])
					{
						try {
						game.castAbility((Ability)tm[1], (Direction)tm2[1]);
						try {
							scene5();
						} catch (IOException e1) {
							
							e1.printStackTrace();
						}
						if(game.checkGameOver()!=null) {
							alerts.display(game.checkGameOver().getName()+" Winner ", "end game");
							scene6();
						}
						}
						catch (NotEnoughResourcesException e1) {
							alerts.display(e1.getMessage(), "warning");
						}
						catch (AbilityUseException e2) {
							alerts.display(e2.getMessage(), STYLESHEET_CASPIAN);
						} catch (CloneNotSupportedException e1) {
							
							System.out.println("lol2");
						}
					}
				}
				else if(((Ability)tm[1]).getCastArea().name().equals("SINGLETARGET"))
				{
					Object [] tm2 =singletarget.display(board,game.getBoard());
					if((boolean) tm2[0])
					{
						
							try {
								game.castAbility((Ability) tm[1], (int)tm2[1],(int) tm2[2]);
								try {
									scene5();
								} catch (IOException e1) {
									
									e1.printStackTrace();
								}
								if(game.checkGameOver()!=null) {
									alerts.display(game.checkGameOver().getName()+" Winner ", "end game");
									scene6();
								}
							} catch (AbilityUseException | NotEnoughResourcesException | InvalidTargetException
									| CloneNotSupportedException e1) {
								
								alerts.display(e1.getMessage(), "warning");
							}
						
					}
				}
				else
				{
					try {
						game.castAbility((Ability) tm[1]);
						try {
							scene5();
						} catch (IOException e1) {
							
							e1.printStackTrace();
						}
						if(game.checkGameOver()!=null) {
							alerts.display(game.checkGameOver().getName()+" Winner ", "end game");
							scene6();
						}
					} catch (AbilityUseException | NotEnoughResourcesException | CloneNotSupportedException e1) {
						
						alerts.display(e1.getMessage(), "warning");
					}
				}
			}
		}
		);
			Image image10 = new Image("shape4.png");
			ImageView iv10 = new ImageView(image10);
			iv10.setTranslateX(80-130-75);
			iv10.setTranslateY(290);
			iv10.setScaleX(0.5);
			iv10.setScaleY(0.5);
			
			Label useleaderab3 = new Label("Move");
			useleaderab3.setTextFill(Color.LIGHTGREEN);
			useleaderab3.setScaleX(1.5);
			useleaderab3.setScaleY(1.5);
			useleaderab3.setTranslateX(80-130-75);
			useleaderab3.setTranslateY(230);
			Button move = new Button("use");
			move.setTranslateX(80-130-75);
			move.setTranslateY(290);
			move.setScaleX(1.2);
			move.setScaleY(1.5);
			move.setBackground(Background.fill(Color.BLACK));
			move.setTextFill(Color.DARKGREEN);
		move.setOnAction(e ->
		{
			Object [] tm = direction.display();
			if((boolean) tm[0])
			{
				try
				{
					game.move(((Direction) tm[1]));
					try {
						scene5();
					} catch (IOException e1) {
						
						e1.printStackTrace();
					}
					if(game.checkGameOver()!=null) {
						alerts.display(game.checkGameOver().getName()+" Winner ", "end game");
						scene6();
					}
				}
				catch (UnallowedMovementException e1) {
					alerts.display(e1.getMessage(), "waning");
				}
				catch (NotEnoughResourcesException e2) {
					alerts.display(e2.getMessage(), "warning");
				}
			}
		}	
				);
		
			
				Image image11 = new Image("shape4.png");
				ImageView iv11 = new ImageView(image11);
				iv11.setTranslateX(80-130*2-75);
				iv11.setTranslateY(290);
				iv11.setScaleX(0.5);
				iv11.setScaleY(0.5);
				
				Label useleaderab4 = new Label("Attack");
				useleaderab4.setTextFill(Color.LIGHTGREEN);
				useleaderab4.setScaleX(1.5);
				useleaderab4.setScaleY(1.5);
				useleaderab4.setTranslateX(80-130*2-75);
				useleaderab4.setTranslateY(230);
				Button attack = new Button("use");
				attack.setTranslateX(80-130*2-75);
				attack.setTranslateY(290);
				attack.setScaleX(1.2);
				attack.setScaleY(1.5);
				attack.setBackground(Background.fill(Color.BLACK));
				attack.setTextFill(Color.DARKGREEN);
				attack.setOnAction(e ->
				{
					Object [] tm = direction.display();
					if((boolean) tm[0])
					{
						try
						{
							game.attack((Direction) tm[1]);
							try {
								scene5();
							} catch (IOException e1) {
								
								e1.printStackTrace();
							}
							if(game.checkGameOver()!=null) {
								alerts.display(game.checkGameOver().getName()+" Winner ", "end game");
								scene6();
							}
						}
						catch(ChampionDisarmedException e1)
						{
							alerts.display(e1.getMessage(), "warning");
						}
						catch(NotEnoughResourcesException e2)
						{
							alerts.display(e2.getMessage(), "warning");
						}
					}
				}
						);
		
				Image image12 = new Image("shape4.png");
				ImageView iv12 = new ImageView(image12);
				iv12.setTranslateX(210+45);
				iv12.setTranslateY(290);
				iv12.setScaleX(0.5);
				iv12.setScaleY(0.5);
				
				Label useleaderab5 = new Label("End turn");
				useleaderab5.setTextFill(Color.LIGHTGREEN);
				useleaderab5.setScaleX(1.5);
				useleaderab5.setScaleY(1.5);
				useleaderab5.setTranslateX(210+45);
				useleaderab5.setTranslateY(230);
				Button endturn = new Button("use");
				endturn.setTranslateX(210+45);
				endturn.setTranslateY(290);
				endturn.setScaleX(1.2);
				endturn.setScaleY(1.5);
				endturn.setBackground(Background.fill(Color.BLACK));
				endturn.setTextFill(Color.DARKGREEN);
				endturn.setOnAction(e ->
				{
					game.endTurn();
					try {
						scene5();
					} catch (IOException e1) {
						
						e1.printStackTrace();
					}
					
				}
				);
				
		nextcha = new HBox();
		nextcha.setTranslateY(29);
		nextcha.setTranslateX(500);
		loadchampionturn();			
		pl3 = new Label();
		pl4 = new Label();
		label2(pl3, game.getFirstPlayer().getTeam());
		label2(pl4, game.getSecondPlayer().getTeam());
		pl3.setTranslateX(-460);
		pl3.setTranslateY(40);
		pl4.setTranslateX(460);
		pl4.setTranslateY(40);
		pl3.setTextFill(Color.DARKGREEN);
		pl4.setTextFill(Color.DARKGREEN);
		pl3.setScaleY(0.8);
		pl4.setScaleY(0.8);
		sp2.getChildren().add(group);
		sp2.getChildren().add(group2);
		sp2.getChildren().addAll(iv3,iv4,name1,name2,iv5,iv6,iv7,iv8,useleaderab,iv9,useleaderab2,iv10,useleaderab3,iv11,useleaderab4,iv12,useleaderab5,nextcha,pl3,pl4,move,attack,useability,useleaderability,endturn,b1,b2,b3,b4,b5,b6,b7,b8,b9,b10,b11,b12,b13,b14,b15,b16,b17,b18,b19,b20,b21,b22,b23,b24,b25);
		put();			
		
		Scene scene = new Scene(sp2,1200,700);
		window.setScene(scene);
		
	}
	public void scene6()
	{
		mediaplayersound.setMute(true);
		playmusic("/game2/sound/[FULL] Demon Slayer_ Kimetsu no Yaiba Episode 19 ED _ Ending 2 - _Kamado Tanjiro no Uta_ (Piano).mp4");
		 Image im = new Image("end.png");
		 ImageView iv = new ImageView(im);
		 StackPane sp = new StackPane();
		 sp.setBackground(Background.fill(Color.BLACK));
		 sp.getChildren().add(iv);
		 mediaplayersound.setOnEndOfMedia(new Runnable() {
				
				@Override
				public void run() {
					window.close();
					
				}
			}
				 );
		 Scene scene = new Scene(sp);
		 window.setScene(scene);
		 
	}
	public void loadchampionturn()
	{
		nextcha = new HBox();
		nextcha.setTranslateY(29);
		nextcha.setTranslateX(500);
		int i=0;
		ArrayList<Champion> arr = new ArrayList<>();
		while(!game.getTurnOrder().isEmpty()&&i!=3)
		{
			Champion tmp = (Champion) game.getTurnOrder().remove();
		
			arr.add(tmp);
			ImageView iv =new ImageView(new Image(geticon(tmp.getName())));
			iv.setFitHeight(85);
			iv.setFitWidth(70);
			nextcha.getChildren().add(iv);
			i++;
			
			
		}
		while(!arr.isEmpty())
		{
			game.getTurnOrder().insert(arr.remove(0));
		}
	}
	public String geticon(String s)
	{
		
			switch(s)
			{
			case("Captain America"): return "Captain Americabat.png";
			case("Deadpool"): return "Deadpoolbat.png";
			case("Dr Strange") : return "Dr Strangebat.png";
			case("Electro"):return "Electrobat.png";
			case("Ghost Rider") : return "Ghost Riderbat.png";
			case("Hela") : return "Helabat.png";
			case("Hulk"): return "Hulkbat.png";
			case("Iceman"):return "Icemanbat.png";
			case("Ironman"): return "Ironmanbat.png";
			case("Loki"):return "Lokibat.png";
			case("Quicksilver"):return "Quicksilverbat.png";
			case("Spiderman"):return "Spidermanbat.png";
			case("Thor") :return "Thorbat.png";
			case("Venom"):return "Venombat.png";
			default : return "Yellow Jacketbat.png";
			
			}
		
			
	}
	private String geticon(int x , int y)
	{
		if(game.getBoard()[x][y] instanceof Cover)
			return "shield.png";
		else
		{
			switch(((Champion)game.getBoard()[x][y]).getName())
			{
			case("Captain America"): return "Captain Americabat.png";
			case("Deadpool"): return "Deadpoolbat.png";
			case("Dr Strange") : return "Dr Strangebat.png";
			case("Electro"):return "Electrobat.png";
			case("Ghost Rider") : return "Ghost Riderbat.png";
			case("Hela") : return "Helabat.png";
			case("Hulk"): return "Hulkbat.png";
			case("Iceman"):return "Icemanbat.png";
			case("Ironman"): return "Ironmanbat.png";
			case("Loki"):return "Lokibat.png";
			case("Quicksilver"):return "Quicksilverbat.png";
			case("Spiderman"):return "Spidermanbat.png";
			case("Thor") :return "Thorbat.png";
			case("Venom"):return "Venombat.png";
			default : return "Yellow Jacketbat.png";
			
			}
		}
			
	}
	
	public String [] getall(Damageable m)
	{
		StringBuilder sb = new StringBuilder();
		String path = new String();
		if(m instanceof Cover)
		{
			sb.append("CurrentHP : "+m.getCurrentHP());
		}
		else
		{
			sb.append(getallhealper((Champion)m));
			path = geticon(((Champion)m).getName());
		}
		String [] tmp = new String []{sb.toString(),path};
		return tmp;
	}
	public String getallhealper(Champion m)
	{
		StringBuilder tmp = new StringBuilder();
		tmp.append("-"+m.getName()+" :  "+getins(m)+'\n');
		tmp.append("    currentHP : "+m.getCurrentHP()+'\n');
		tmp.append("    CurrentActionPoints : "+m.getCurrentActionPoints()+'\n');
		tmp.append("    attackDamage : "+m.getAttackDamage()+'\n');
		tmp.append("    attackRange : "+m.getAttackRange()+'\n');
		tmp.append("    speed: "+m.getSpeed()+'\n');
		tmp.append("    -abilities : "+'\n');
		for(Ability j : m.getAbilities())
		{
			tmp.append("        .name: "+j.getName()+": "+getins2(j)+'\n');
			tmp.append("          .Cast Area : "+j.getCastArea().name()+'\n');
			tmp.append("           .Cast Range : "+j.getCastRange()+'\n');
			tmp.append("           .Mana Cost : "+j.getManaCost()+'\n');
			tmp.append("           .Required Action Points : "+j.getRequiredActionPoints()+'\n');
			tmp.append("           .Current Cooldown : "+j.getCurrentCooldown()+'\n');
			tmp.append("           .Base Cooldown : "+j.getBaseCooldown()+'\n');
			if(j instanceof CrowdControlAbility)
					tmp.append("           Effect : "+getins3(((CrowdControlAbility)j).getEffect())+'\n');
			else if(j instanceof HealingAbility) 
					tmp.append("           Heal Amount : "+((HealingAbility)j).getHealAmount()+'\n');
			else
					tmp.append("           Damage Amount : "+((DamagingAbility)j).getDamageAmount()+'\n');
		}
		tmp.append("    -Applied Effects :"+'\n');
		for(Effect j :m.getAppliedEffects())
		{
			tmp.append("        ."+getins3(j)+'\n');
		}
		return tmp.toString();
	}
	public static String getins2(Ability t)
	{
		if(t instanceof CrowdControlAbility)
			return "Crowd Control";
		else if(t instanceof HealingAbility) 
			return "Healing";
		return "Damaging";
	}
	public static String getins3(Effect t)
	{

		return "name : "+t.getName()+".    duration: "+t.getDuration();
	}
	
	private void put()
	{
		for(int i=0;i!=5;i++)
		{
			for(int j=0;j!=5;j++)
			{
				if(game.getBoard()[i][j]!=null)
				{
					String tmp = geticon(i,j);
					Image image = new Image(tmp);
					ImageView iv = new ImageView(image);
					iv.setFitHeight(55);
					iv.setFitWidth(75);
					board[i][j].setScaleX(1);
					board[i][j].setScaleY(1);
					board[i][j].setGraphic(iv);
				}
			}
		}
	}
	
	public boolean isfirstplayerhas(String name)
	{
		for(Champion i: firstplte)
			if(i.getName().equals(name))
				return true;
		return false;
	}
	
	public void playmusic(String s)
	{
		File file = new File(s);
		Media media =new Media(file.toURI().toString());
		mediaplayersound= new MediaPlayer(media);	
		 mediaplayersound.setAutoPlay(true);
		 mediaplayersound.setCycleCount(MediaPlayer.INDEFINITE);
		 mediaplayersound.play();
		 
	}
	private boolean check(TextField firstpl, TextField secondpl)
	{	
		if(firstpl.getText().equals("")||secondpl.getText().equals("")) {
			alerts.display("please ,each player enter his/her name","alert");
			return false;
		}
		return true;
		
	}
	public String getins(Champion t)
	{
		if(t instanceof Hero)
			return "Hero";
		else if(t instanceof AntiHero)
			return "Anti Hero";
		return "Villain";
	}
	public void label2(Label l , ArrayList<Champion> t)
	{
		StringBuilder tmp = new StringBuilder();
		for(Champion i :t)
		{
			if(i==game.getFirstPlayer().getLeader()||i==game.getSecondPlayer().getLeader())
			{
				tmp.append("-Leader: "+i.getName()+":  "+getins(i)+'\n');
				tmp.append("    currentHP: "+i.getCurrentHP()+'\n');
				tmp.append("    CurrentActionPoints: "+i.getCurrentActionPoints()+'\n');
				tmp.append("    attackDamage: "+i.getAttackDamage()+'\n');
				tmp.append("    attackRange: "+i.getAttackRange()+'\n');
				tmp.append("    speed: "+i.getSpeed()+'\n');
				tmp.append("    abilities :"+'\n');
				for(Ability j : i.getAbilities())
				{
					tmp.append("        "+j.getName()+'\n');
					
				}
			}
		}
		
		for(Champion i :t)
		{
			
			
//			tmp.append("    "+i.toString()+'\n');
//			for(Effect j : i.getAppliedEffects())
//			{
//				tmp.append("            "+j.getName());
//			}
			
			if(i!=game.getFirstPlayer().getLeader()&&i!=game.getSecondPlayer().getLeader())
			{
				tmp.append("-"+i.getName()+":  "+getins(i)+'\n');
				tmp.append("    currentHP: "+i.getCurrentHP()+'\n');
				tmp.append("    CurrentActionPoints: "+i.getCurrentActionPoints()+'\n');
				tmp.append("    attackDamage: "+i.getAttackDamage()+'\n');
				tmp.append("    attackRange: "+i.getAttackRange()+'\n');
				tmp.append("    speed: "+i.getSpeed()+'\n');
				tmp.append("    abilities :"+'\n');
				for(Ability j : i.getAbilities())
				{
					tmp.append("        "+j.getName()+'\n');
					
				}
			}
			
			
		}
		l.setText(tmp.toString());
	}
	public void label(Label l , ArrayList<Champion> t,Champion m)
	{
		StringBuilder tmp = new StringBuilder();
		if(m==null) {
		
		for(Champion i :t)
		{
			tmp.append("-"+i.getName()+'\n');
			tmp.append("    maxHP: "+i.getMaxHP()+'\n');
			tmp.append("    maxActionPointsPerTurn: "+i.getMaxActionPointsPerTurn()+'\n');
			tmp.append("    attackDamage: "+i.getAttackDamage()+'\n');
			tmp.append("    attackRange: "+i.getAttackRange()+'\n');
			tmp.append("    speed: "+i.getSpeed()+'\n');
			tmp.append("    abilities :"+'\n');
			for(Ability j : i.getAbilities())
			{
				tmp.append("        "+j.getName()+'\n');
			}
			
		}
		
		}
		else
		{
			tmp.append("- Leader: "+m.getName()+'\n');
			tmp.append("    maxHP: "+m.getMaxHP()+'\n');
			tmp.append("    maxActionPointsPerTurn: "+m.getMaxActionPointsPerTurn()+'\n');
			tmp.append("    attackDamage: "+m.getAttackDamage()+'\n');
			tmp.append("    attackRange: "+m.getAttackRange()+'\n');
			tmp.append("    speed: "+m.getSpeed()+'\n');
			tmp.append("    abilities :"+'\n');
			for(Ability j : m.getAbilities())
			{
				tmp.append("        "+j.getName()+'\n');
			}
			for(Champion i :t)
			{
				if(i!=m)
				{
					tmp.append("-"+i.getName()+'\n');
					tmp.append("    maxHP: "+i.getMaxHP()+'\n');
					tmp.append("    maxActionPointsPerTurn: "+i.getMaxActionPointsPerTurn()+'\n');
					tmp.append("    attackDamage: "+i.getAttackDamage()+'\n');
					tmp.append("    attackRange: "+i.getAttackRange()+'\n');
					tmp.append("    speed: "+i.getSpeed()+'\n');
					tmp.append("    abilities :"+'\n');
					for(Ability j : i.getAbilities())
					{
						tmp.append("        "+j.getName()+'\n');
					}
				}
				
			}
			
			
		}
		l.setText(tmp.toString());
	}
	public static void main(String[] args) {
		launch(args);
	}
}
