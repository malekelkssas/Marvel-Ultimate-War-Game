package application;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.world.Champion;
import model.world.Cover;

public class singletarget {

	boolean ans;
	static Object []tm = new Object[3];
	static Button [][]board;
	static  Object[][]game;
	public static Object [] display(Button [][]oriboard1, Object[][]game1)
	{
		board = new Button[5][5];
		game = game1;
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("choose location");
		window.setMinWidth(600);
		window.setMinHeight(600);


		Button b1 = new Button();
		b1.setScaleX(6);
		b1.setScaleY(2.5);
		b1.setTranslateX(-198);
		b1.setTranslateY(-140);
		board[0][0] = b1;
		b1.setOnAction(e ->
		{
			tm[0] = true;
			tm[1] = 0;
			tm[2] = 0;
			window.close();
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
			tm[0] = true;
			tm[1] = 0;
			tm[2] = 1;
			
			window.close();
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
			tm[0] = true;
			tm[1] = 0;
			tm[2] = 2;
			
			window.close();
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
			tm[0] = true;
			tm[1] = 0;
			tm[2] = 3;
			
			window.close();
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
			tm[0] = true;
			tm[1] = 0;
			tm[2] = 4;
			
			window.close();
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
			tm[0] = true;
			tm[1] = 1;
			tm[2] = 0;
			
			window.close();
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
			tm[0] = true;
			tm[1] = 1;
			tm[2] = 1;
		
			window.close();
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
			tm[0] = true;
			tm[1] = 1;
			tm[2] = 2;
			
			window.close();
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
			tm[0] = true;
			tm[1] = 1;
			tm[2] = 3;
			
			window.close();
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
			tm[0] = true;
			tm[1] = 1;
			tm[2] = 4;
			
			window.close();
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
			tm[0] = true;
			tm[1] = 2;
			tm[2] = 0;
			
			window.close();
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
			tm[0] = true;
			tm[1] = 2;
			tm[2] = 1;
			
			window.close();
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
			tm[0] = true;
			tm[1] = 2;
			tm[2] = 2;
			
			window.close();
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
			tm[0] = true;
			tm[1] = 2;
			tm[2] = 3;
			
			window.close();
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
			tm[0] = true;
			tm[1] = 2;
			tm[2] = 4;
			
			window.close();
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
			tm[0] = true;
			tm[1] = 3;
			tm[2] = 0;
			
			window.close();
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
			tm[0] = true;
			tm[1] = 3;
			tm[2] = 1;
			
			window.close();
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
			tm[0] = true;
			tm[1] = 3;
			tm[2] = 2;
		
			window.close();
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
			tm[0] = true;
			tm[1] = 3;
			tm[2] = 3;
			
			window.close();
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
			tm[0] = true;
			tm[1] = 3;
			tm[2] = 4;
			
			window.close();
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
			tm[0] = true;
			tm[1] = 4;
			tm[2] = 0;
			
			window.close();
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
			tm[0] = true;
			tm[1] = 4;
			tm[2] = 1;
			
			window.close();
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
			tm[0] = true;
			tm[1] = 4;
			tm[2] = 2;
			
			window.close();
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
			tm[0] = true;
			tm[1] = 4;
			tm[2] = 3;
			
			window.close();
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
			tm[0] = true;
			tm[1] = 4;
			tm[2] = 4;
			
			window.close();
		}
				);
		
		Button cancel = new Button("cancel");
		cancel.setScaleX(6);
		cancel.setScaleY(2.5);
		cancel.setTranslateX(0);
		cancel.setTranslateY(-140+70*6);
		cancel.setOnAction(e ->
		{
			tm[0] = false;
			window.close();
		}
		);
		put();
		StackPane gp = new StackPane();
		gp.setBackground(Background.fill(Color.BLACK));
		gp.getChildren().addAll(b1,b2,b3,b4,b5,b6,b7,b8,b9,b10,b11,b12,b13,b14,b15,b16,b17,b18,b19,b20,b21,b22,b23,b24,b25,cancel);
		Scene scene = new Scene(gp,1200,700);
		
		window.setScene(scene);
		window.showAndWait();
		return tm;
		
	}
	private static void put()
	{
		for(int i=0;i!=5;i++)
		{
			for(int j=0;j!=5;j++)
			{
				if(game[i][j]!=null)
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
				else
				{
					double x = board[i][j].getTranslateX();
					double y = board[i][j].getTranslateY();
					board[i][j] = new Button();
					board[i][j].setScaleX(6);
					board[i][j].setScaleY(2.5);
					board[i][j].setTranslateX(x);
					board[i][j].setTranslateY(y);
				}
			}
		}
	}
	private static String geticon(int x , int y)
	{
		if(game[x][y] instanceof Cover)
			return "shield.png";
		else
		{
			switch(((Champion)game[x][y]).getName())
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
}
