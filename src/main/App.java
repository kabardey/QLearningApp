package main;

import java.io.IOException;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class App extends Application {
	public static BorderPane rootLayout;
	public static int SIZE = 10;
	public static Label[][] label = new Label[SIZE][SIZE];
	public static GridPane root = new GridPane();
	
	@Override
	public void start(Stage primaryStage) throws Exception {

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                  label[i][j] = new Label(); // This is missing in the original code
                  label[i][j].setPrefHeight(60);
                  label[i][j].setPrefWidth(60);
                  label[i][j].setAlignment(Pos.CENTER);
                  label[i][j].setStyle("-fx-border-color: black;");
                  root.add(label[i][j], j, i);
              }
          }
        
		try{
			
			// Load the root layout from the fxml file
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/Main.fxml"));
            rootLayout = (BorderPane) loader.load();
            rootLayout.setCenter(root);
            
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Q Learning");
            primaryStage.show();
            
		}catch(IOException e) {
            // Exception gets thrown if the fxml file could not be loaded
            e.printStackTrace();
		}
		
	}
	
	
	public static void main(String[] args) {

		launch(args);
	}
}
