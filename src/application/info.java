package application;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class info {

	public static void display(String info , String path)
	{
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);		// if i have multiwindows now it must on the user (forse him) to deal with it first
		window.setTitle("champion");
		window.setMinWidth(800);
		window.setMinHeight(700);
		Label l = new Label(info);
		Image im2 = new Image("images.png");
		ImageView iv2 = new ImageView(im2);
		iv2.setFitWidth(500);
		iv2.setFitHeight(500);
		l.setTextFill(Color.DARKGREEN);
		l.setTranslateX(200);
		l.setTranslateY(10);
		Button b = new Button("ok");
		b.setTranslateY(600);
		Image im = new Image(path);
		ImageView iv = new ImageView(im);
		iv.setFitHeight(200);
		iv.setFitWidth(200);
		iv.setTranslateX(-200);
		iv.setTranslateY(230);
		b.setOnAction(e -> window.close());
		StackPane vb = new StackPane();
		vb.setBackground(Background.fill(Color.BLACK));
		vb.getChildren().addAll(iv2,l, iv,b);
		vb.setAlignment(Pos.TOP_CENTER);
		Scene sc = new Scene(vb,300,300);
		window.setScene(sc);
		window.showAndWait();
		
	}
}
