package application;


import java.util.ArrayList;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.abilities.Ability;
import model.world.Direction;

public class chooseab {

	static Ability ans;
	static boolean bool;
	public static Object[]  display(ArrayList<Ability> arr)
	{
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);		// if i have multiwindows now it must on the user (forse him) to deal with it first
		window.setTitle("ability");
		window.setMinWidth(250);
		
		Label l = new Label("choose an ability");
		
		
		
		VBox vb = new VBox(20);
		vb.getChildren().add(l);
		for(Ability i :arr)
		{
			Button tmp = new Button(i.getName());
			vb.getChildren().add(tmp);
			tmp.setOnAction(e ->
			{
				bool = true;
				ans = i;
				window.close();
			});
		}
		
		window.setOnCloseRequest(e ->
				{
					bool = false;
					window.close();
					
				}
				);
		Button cancel = new Button("cancel");
		cancel.setOnAction(e ->
		{
			bool = false;
			window.close();
		}
				);
		vb.getChildren().add(cancel);
		vb.setAlignment(Pos.CENTER);
		Scene sc = new Scene(vb);
		window.setScene(sc);
		window.showAndWait();
		
		return new Object[]{bool,ans};
	}
}

