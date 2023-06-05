package application;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class alerts {

	
	public static void  display(String message,String title)
	{
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);		// if i have multiwindows now it must on the user (forse him) to deal with it first
		window.setTitle(title);
		window.setMinWidth(250);
		window.setMinHeight(150);
		Label l = new Label(message);
		Button b = new Button("ok");
		b.setOnAction(e -> window.close());
		VBox vb = new VBox(20);
		vb.getChildren().addAll(l , b);
		vb.setAlignment(Pos.TOP_CENTER);
		Scene sc = new Scene(vb);
		window.setScene(sc);
		window.showAndWait();
		
		
	}
}
