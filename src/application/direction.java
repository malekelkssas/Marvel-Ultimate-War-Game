package application;


import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.world.Direction;

public class direction {

	static Direction ans;
	static boolean bool;
	public static Object[]  display()
	{
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);		// if i have multiwindows now it must on the user (forse him) to deal with it first
		window.setTitle("direction");
		window.setMinWidth(250);
		
		Label l = new Label("choose a a direction");
		
		Button up = new Button("up");
		Button down = new Button("down");
		Button right = new Button("right");
		Button left = new Button("left");
		Button cancel = new Button("cancel");
		up.setOnAction(e ->
		{
			ans= Direction.DOWN;
			bool=true;
			window.close();
			
		}
		);
		down.setOnAction(e ->
		{
			ans= Direction.UP;
			bool = true;
			window.close();
			
		}
		);
		right.setOnAction(e ->
		{
			ans= Direction.RIGHT;
			bool = true;
			window.close();
		}
		);
		left.setOnAction(e ->
		{
			ans= Direction.LEFT;
			bool = true;
			window.close();
		}
		);
		cancel.setOnAction(e ->
		{
			ans= null;
			bool = false;
			window.close();
		}
		);
		HBox vb = new HBox(20);
		vb.getChildren().addAll(l , up , down,right,left,cancel);
		vb.setAlignment(Pos.CENTER);
		Scene sc = new Scene(vb);
		window.setScene(sc);
		
		window.showAndWait();
		return new Object[]{bool,ans};
	}
}

