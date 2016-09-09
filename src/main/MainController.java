package main;

import java.io.IOException;

import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.util.PriorityQueue;


public class MainController implements Initializable  {

	private double agent[] = new double[2];
	private double goal[] = new double[2];
	private double Qvalue[][][] = new double[App.SIZE][App.SIZE][4];
	private double Qvalue2[][][] = new double[App.SIZE][App.SIZE][8];
	private int windyMatrix[][][] = new int[App.SIZE][App.SIZE][2];
	private double model[][][] = new double[App.SIZE][App.SIZE][4];
	private int reward[][] = new int[App.SIZE][App.SIZE];
	private int twentyMove[] = new int[20];
	private int thirtyMove[] = new int[30];
	private ArrayList xCoor = new ArrayList();
	private ArrayList yCoor = new ArrayList();
	private Label[][] QLabel = new Label[App.SIZE][App.SIZE];
	private GridPane QGrid = new GridPane();

	@FXML
	private Button done;
	@FXML
	private Button done2;
	@FXML
	private TextField AgentX;
	@FXML
	private TextField AgentY;
	@FXML
	private TextField GoalX;
	@FXML
	private TextField GoalY;
	@FXML
	private TextField alpha;
	@FXML
	private TextField gamma;
	@FXML
	private TextField epsilon;
	@FXML
	private TextField startVal;
	@FXML
	private TextField endVal;
	@FXML
	private TextField episodes;
	@FXML
	private Button windButton;
	@FXML
	private RadioButton staticRadio;
	@FXML
	private RadioButton wind;
	@FXML
	private Label note;
	@FXML
	private ComboBox<String> algorithm;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		algorithm.getItems().addAll("Q Learning","Q Learning(Wind)","King's Move","Prioritized Sweeping");

		startVal.setDisable(true);
		endVal.setDisable(true);
		episodes.setDisable(true);
		windButton.setDisable(true);
		done2.setDisable(true);
		staticRadio.setOnAction(this::actionPerformed);

		note.setText("Please enter coordinates \nbetween 0-9!");
	}

	@FXML
	public void actionPerformed(ActionEvent event){

		if(staticRadio.isSelected()){
			startVal.setDisable(true);
			endVal.setDisable(true);
			episodes.setDisable(true);
			epsilon.setDisable(false);
			done2.setDisable(true);
			done.setDisable(false);
		}else{
			startVal.setDisable(false);
			endVal.setDisable(false);
			episodes.setDisable(false);
			epsilon.setDisable(true);
			done2.setDisable(false);
			done.setDisable(true);
		}

		if(wind.isSelected()){
			windButton.setDisable(false);
		}else{
			windButton.setDisable(true);
		}

	}
	public void doneButton(ActionEvent event){
		boolean condition = true;
		do {

			String agX = AgentX.getText();

			double ageX = 0;
			try {
				ageX = Double.parseDouble(agX);

				if(ageX < 0 || ageX > 9){
					note.setText("Invalid Coordinates!\nPlease enter between 0-9");
					condition = false;
					break;
				}else{
					condition = true;
					note.setText("");
				}

			} catch (NumberFormatException e) {
				System.out.println("Invalid agX");
			}

			String agY = AgentY.getText();

			double ageY = 0;
			try {
				ageY = Double.parseDouble(agY);

				if(ageY < 0 || ageY > 9){
					note.setText("Invalid Coordinates!\nPlease enter between 0-9");
					condition = false;
					break;
				}else{
					condition = true;
					note.setText("");
				}
			} catch (NumberFormatException e) {
				System.out.println("Invalid agY");
			}

			agent[0] = ageX;
			agent[1] = ageY;

			String goX = GoalX.getText();

			double goaX = 0;
			try {
				goaX = Double.parseDouble(goX);

				if(goaX < 0 || goaX > 9){
					note.setText("Invalid Coordinates!\nPlease enter between 0-9");
					condition = false;
					break;
				}else{
					condition = true;
					note.setText("");
				}
			} catch (NumberFormatException e) {
				System.out.println("Invalid goX");
			}

			String goY = GoalY.getText();

			double goaY = 0;
			try {
				goaY = Double.parseDouble(goY);

				if(goaY < 0 || goaY > 9){
					note.setText("Invalid Coordinates!\nPlease enter between 0-9");
					condition = false;
					break;
				}else{
					condition = true;
					note.setText("");
				}
			} catch (NumberFormatException e) {
				System.out.println("Invalid goalY");
			}

			goal[0] = goaX;
			goal[1] = goaY;
		}while(condition == false);

		App.label[(int) agent[0]][(int) agent[1]].setStyle("-fx-background-color: black");
		App.label[(int) goal[0]][(int) goal[1]].setStyle("-fx-background-color: red");

		if((int)agent[0] == 0 && (int)agent[1] == 0 &&(int)goal[0] == 0 && (int)goal[1] == 0){
			App.label[0][0].setStyle("-fx-background-color: none");
			App.label[0][0].setStyle("-fx-border-color: black;");
		}

	}
	public void addWind(ActionEvent event){
		
		int x;
		for(x = 0;x < 10;x++){
			windyMatrix[4][x][0] = 3;
			windyMatrix[4][x][1] = 2;
		}

		for (int i = 0; i < App.SIZE; i++) {
            for (int j = 0; j < App.SIZE; j++) {
            	
            	if(windyMatrix[i][j][0] == 0){
            		App.label[i][j].setText(" ");
            	}
            	else if(windyMatrix[i][j][0] == 1){
            		if(windyMatrix[i][j][1] == 2){
            			App.label[i][j].setText("↑(2)");
            		}else if(windyMatrix[i][j][1] == 3){
            			App.label[i][j].setText("↑(3)");
            		}
            	}else if(windyMatrix[i][j][0] == 2){
            		if(windyMatrix[i][j][1] == 2){
            			App.label[i][j].setText("↓(2)");
            		}else if(windyMatrix[i][j][1] == 3){
            			App.label[i][j].setText("↓(3)");
            		}
            	}else if(windyMatrix[i][j][0] == 3){
            		if(windyMatrix[i][j][1] == 2){
            			App.label[i][j].setText("→(2)");
            		}else if(windyMatrix[i][j][1] == 3){
            			App.label[i][j].setText("→(3)");
            		}           		
            	}else if(windyMatrix[i][j][0] == 4){
            		if(windyMatrix[i][j][1] == 2){
            			App.label[i][j].setText("←(2)");
            		}else if(windyMatrix[i][j][1] == 3){
            			App.label[i][j].setText("←(3)");
            		}   
            	}
            	
            }
		}
	}

	public void doneValues(ActionEvent event){
		
		
		String alphaValue = alpha.getText();
		double alphaVal = 0;
		try {
		    alphaVal = Double.parseDouble(alphaValue);
		} catch (NumberFormatException e) {
		    System.out.println("Invalid alpha");
		}
		
		String gammaValue = gamma.getText();
		double gammaVal = 0;
		try {
		    gammaVal = Double.parseDouble(gammaValue);
		} catch (NumberFormatException e) {
		    System.out.println("Invalid gamma");
		}
		
		String epsilonValue = epsilon.getText();
		double epsilonVal = 0;
		try {
		    epsilonVal = Double.parseDouble(epsilonValue);
		} catch (NumberFormatException e) {
		    System.out.println("Invalid epsilon");
		}

		for(int a = 0; a < App.SIZE; a++){
			for(int b = 0; b < App.SIZE;b++ ){
				App.label[a][b].setStyle("-fx-background-color: none");
				App.label[a][b].setStyle("-fx-border-color: black;");
			}
		}

		String output = algorithm.getSelectionModel().getSelectedItem().toString();

		if(output == "Q Learning"){
			QLearning(alphaVal,gammaVal,epsilonVal);
		}else if(output == "King's Move"){
			KingsMove(alphaVal,gammaVal,epsilonVal);
		}else if(output == "Q Learning(Wind)"){
			QLearningWind(alphaVal,gammaVal,epsilonVal);
		}else if(output == "Prioritized Sweeping"){
			PrioritizedSweeping(alphaVal,gammaVal,epsilonVal);
		}

	}
	private void QLearning(double alphaVal,double gammaVal,double epsilonVal){
		reward[(int) goal[0]][(int) goal[1]] = 100;

		int std = -1;
		int iteration = 0;
		int t = 0;

		while(t < 20 || std != 0){
			int i = (int) agent[0];
			int j = (int) agent[1];
			xCoor.clear();
			yCoor.clear();

			xCoor.add(i);
			yCoor.add(j);
			int stepNumber = 0;

			while((i !=(int) goal[0]) || (j !=(int) goal[1])){

				double rand = Math.random();

				if(rand > epsilonVal){ //greedy move
					int iIndex1;
					int jIndex1;
					takeMaxIndexes result1;
					result1 = takeMaxValue(Qvalue,i,j,App.SIZE);
					iIndex1 = result1.getI();
					jIndex1 = result1.getJ();
					int action = result1.getAction();

					double rewardValue = reward[iIndex1][jIndex1];

					takeMaxIndexes result2 = takeMaxValue(Qvalue,iIndex1,jIndex1,App.SIZE);
					int nextAction = result2.getAction();

					Qvalue[i][j][action] = Qvalue[i][j][action] +  alphaVal*(rewardValue + (gammaVal*Qvalue[iIndex1][jIndex1][nextAction]) - Qvalue[i][j][action]);

					xCoor.add(iIndex1);
					yCoor.add(jIndex1);
					i = iIndex1;
					j = jIndex1;
					++stepNumber;
				}else{
					int iIndex2;
					int jIndex2;
					takeRandom result3;
					result3  = takeRand(i,j,App.SIZE,Qvalue);
					iIndex2 = result3.getI();
					jIndex2 = result3.getJ();
					int action = result3.getAction();

					double rewardValue = reward[iIndex2][jIndex2];

					takeMaxIndexes result4 = takeMaxValue(Qvalue,iIndex2,jIndex2,App.SIZE);
					int nextAction = result4.getAction();

					Qvalue[i][j][action] += alphaVal*(rewardValue + (gammaVal*Qvalue[iIndex2][jIndex2][nextAction]) - Qvalue[i][j][action]);

					xCoor.add(iIndex2);
					yCoor.add(jIndex2);
					i = iIndex2;
					j = jIndex2;
					++stepNumber;
				}
			}

			if(t < 20){
				twentyMove[t]= stepNumber;
				++t;
			}else if(t == 20){
				std = compute(twentyMove,20);
				++t;
			}else if(t > 20){
				int z;
				for(z = 0;z < (20-1);z++){
					twentyMove[z] = twentyMove[z + 1];
				}

				twentyMove[20-1] = stepNumber;
				std = compute(twentyMove,20);
				++t;
			}

			System.out.println("stepNumber: " + stepNumber);
			++iteration;

		}

		System.out.println("Iteration: " + iteration);

		int sizeList = xCoor.size();

		for(int a = 0; a < (sizeList-1); a++){
			App.label[(int) xCoor.get(a)][(int) yCoor.get(a)].setStyle("-fx-background-color: black");
		}

		App.label[(int) agent[0]][(int) agent[1]].setStyle("-fx-background-color: black");
		App.label[(int) goal[0]][(int) goal[1]].setStyle("-fx-background-color: red");

	}
	private void KingsMove(double alphaVal,double gammaVal,double epsilonVal){
		reward[(int) goal[0]][(int) goal[1]] = 100;

		int std = -1;
		int iteration = 0;
		int t = 0;

		while(t < 30 || std != 0){
			int i = (int) agent[0];
			int j = (int) agent[1];
			xCoor.clear();
			yCoor.clear();

			xCoor.add(i);
			yCoor.add(j);

			int stepNumber = 0;

			while((i !=(int) goal[0]) || (j !=(int) goal[1])){

				double rand = Math.random();

				if(rand > epsilonVal){ //greedy move

					int iIndex1;
					int jIndex1;
					takeMaxIndexes result1;
					result1 = takeMaxValue2(Qvalue2,i,j,App.SIZE);
					iIndex1 = result1.getI();
					jIndex1 = result1.getJ();
					int action = result1.getAction();

					double rewardValue = reward[iIndex1][jIndex1];

					takeMaxIndexes result2 = takeMaxValue2(Qvalue2,iIndex1,jIndex1,App.SIZE);
					int nextAction = result2.getAction();

					Qvalue2[i][j][action] = Qvalue2[i][j][action] +  alphaVal*(rewardValue + (gammaVal*Qvalue2[iIndex1][jIndex1][nextAction]) - Qvalue2[i][j][action]);

					xCoor.add(iIndex1);
					yCoor.add(jIndex1);
					i = iIndex1;
					j = jIndex1;
					++stepNumber;
				}else{//exploration

					int iIndex2;
					int jIndex2;
					takeRandom result3;
					result3  = takeRand2(i,j,App.SIZE,Qvalue2);
					iIndex2 = result3.getI();
					jIndex2 = result3.getJ();
					int action = result3.getAction();

					double rewardValue = reward[iIndex2][jIndex2];

					takeMaxIndexes result4 = takeMaxValue2(Qvalue2,iIndex2,jIndex2,App.SIZE);
					int nextAction = result4.getAction();

					Qvalue2[i][j][action] += alphaVal*(rewardValue + (gammaVal*Qvalue2[iIndex2][jIndex2][nextAction]) - Qvalue2[i][j][action]);

					xCoor.add(iIndex2);
					yCoor.add(jIndex2);
					i = iIndex2;
					j = jIndex2;
					++stepNumber;
				}
			}

			if(t < 30){
				thirtyMove[t]= stepNumber;
				++t;
			}else if(t == 30){
				std = compute(thirtyMove,30);
				++t;
			}else if(t > 30){
				int z;
				for(z = 0;z < (30-1);z++){
					thirtyMove[z] = thirtyMove[z + 1];
				}
				thirtyMove[30-1] = stepNumber;
				std = compute(thirtyMove,30);
				++t;
			}

			System.out.println("stepNumber: " + stepNumber);
			++iteration;
		}
		System.out.println("Iteration: " + iteration);
		int sizeList = xCoor.size();

		for(int a = 0; a < (sizeList-1); a++){
			App.label[(int) xCoor.get(a)][(int) yCoor.get(a)].setStyle("-fx-background-color: black");
		}

		App.label[(int) agent[0]][(int) agent[1]].setStyle("-fx-background-color: black");
		App.label[(int) goal[0]][(int) goal[1]].setStyle("-fx-background-color: red");
	}
	private void QLearningWind(double alphaVal,double gammaVal,double epsilonVal){

		reward[(int) goal[0]][(int) goal[1]] = 100;

		int std = -1;
		int iteration = 0;
		int t = 0;

		while(t < 20 || std != 0){
			int i = (int) agent[0];
			int j = (int) agent[1];
			xCoor.clear();
			yCoor.clear();

			xCoor.add(i);
			yCoor.add(j);

			int stepNumber = 0;

			while((i !=(int) goal[0]) || (j !=(int) goal[1])){

				double rand = Math.random();

				if(rand > epsilonVal){//greedy move
					if(windyMatrix[i][j][0] == 0){//there is no wind

						int iIndex1;
						int jIndex1;
						takeMaxIndexes result1 = takeMaxValue(Qvalue,i,j,App.SIZE);
						iIndex1 = result1.getI();
						jIndex1 = result1.getJ();
						int action = result1.getAction();

						double rewardValue = reward[iIndex1][jIndex1];

						takeMaxIndexes result2 = takeMaxValue(Qvalue,iIndex1,jIndex1,App.SIZE);
						int nextAction = result2.getAction();

						Qvalue[i][j][action] = Qvalue[i][j][action] +  alphaVal*(rewardValue + (gammaVal*Qvalue[iIndex1][jIndex1][nextAction]) - Qvalue[i][j][action]);

						xCoor.add(iIndex1);
						yCoor.add(jIndex1);
						i = iIndex1;
						j = jIndex1;
						++stepNumber;
					}else{//there is wind

						takeMaxIndexes result = takeMaxValue(Qvalue,i,j,App.SIZE);
						int action = result.getAction();

						windEffect windIndex = takeWindIndexes(windyMatrix,i,j,App.SIZE);
						int iIndex = windIndex.getI();
						int jIndex = windIndex.getJ();

						double rewardValue = reward[iIndex][jIndex];

						takeMaxIndexes result2 = takeMaxValue(Qvalue,iIndex,jIndex,App.SIZE);
						int nextAction = result2.getAction();

						Qvalue[i][j][action] += alphaVal*(rewardValue + (gammaVal*Qvalue[iIndex][jIndex][nextAction]) - Qvalue[i][j][action]);

						xCoor.add(iIndex);
						yCoor.add(jIndex);

						int i2 = result2.getI();
						int j2 = result2.getJ();
						takeMaxIndexes result3 = takeMaxValue(Qvalue,i2,j2,App.SIZE);
						int nextAction2 = result3.getAction();

						Qvalue[iIndex][jIndex][nextAction] +=  alphaVal*(rewardValue + (gammaVal*Qvalue[i2][j2][nextAction2]) - Qvalue[iIndex][jIndex][nextAction]);

						xCoor.add(i2);
						yCoor.add(j2);
						i = i2;
						j = j2;
						++stepNumber;
					}
				}else {
					if(windyMatrix[i][j][0] == 0){//there is no wind (exploration)

						int iIndex2;
						int jIndex2;
						takeRandom result3 = takeRand(i,j,App.SIZE,Qvalue);
						iIndex2 = result3.getI();
						jIndex2 = result3.getJ();
						int action = result3.getAction();

						double rewardValue = reward[iIndex2][jIndex2];

						takeMaxIndexes result4 = takeMaxValue(Qvalue,iIndex2,jIndex2,App.SIZE);
						int nextAction = result4.getAction();
						Qvalue[i][j][action] += alphaVal*(rewardValue + (gammaVal*Qvalue[iIndex2][jIndex2][nextAction]) - Qvalue[i][j][action]);

						xCoor.add(iIndex2);
						yCoor.add(jIndex2);
						i = iIndex2;
						j = jIndex2;
						++stepNumber;
					}
					else{ //have wind (exploration)

						takeRandom result = takeRand(i,j,App.SIZE,Qvalue);
						int action = result.getAction();

						windEffect windIndex = takeWindIndexes(windyMatrix,i,j,App.SIZE);
						int iIndex = windIndex.getI();
						int jIndex = windIndex.getJ();

						takeRandom result2 = takeRand(iIndex,jIndex,App.SIZE,Qvalue);
						int nextAction = result2.getAction();

						double rewardValue = reward[iIndex][jIndex];

						Qvalue[i][j][action] += alphaVal*(rewardValue + (gammaVal*Qvalue[iIndex][jIndex][nextAction]) - Qvalue[i][j][action]);

						int i2 = result2.getI();
						int j2 = result2.getJ();

						xCoor.add(iIndex);
						yCoor.add(jIndex);
						i = i2;
						j = j2;

						takeMaxIndexes result4 = takeMaxValue(Qvalue,i2,j2,App.SIZE);
						int nextAction2 = result4.getAction();
						Qvalue[iIndex][jIndex][nextAction] += alphaVal*(rewardValue + (gammaVal*Qvalue[i2][j2][nextAction2]) - Qvalue[iIndex][jIndex][nextAction]);

						xCoor.add(i2);
						yCoor.add(j2);
						++stepNumber;
					}
				}
			}//second while end

			if(t < 20){
				twentyMove[t]= stepNumber;
				++t;
			}else if(t == 20){
				std = compute(twentyMove,20);
				++t;
			}else if(t > 20){
				int z;
				for(z = 0;z < (20-1);z++){
					twentyMove[z] = twentyMove[z + 1];
				}
				twentyMove[20-1] = stepNumber;
				std = compute(twentyMove,20);
				++t;
			}
			System.out.println("stepNumber: " + stepNumber);
			++iteration;
		}//first while end

		System.out.println("Iteration: " + iteration);

		int sizeList = xCoor.size();

		for(int a = 0; a < (sizeList-1); a++){
			App.label[(int) xCoor.get(a)][(int) yCoor.get(a)].setStyle("-fx-background-color: black");
		}

		App.label[(int) agent[0]][(int) agent[1]].setStyle("-fx-background-color: black");
		App.label[(int) goal[0]][(int) goal[1]].setStyle("-fx-background-color: red");
	}
	private void PrioritizedSweeping(double alphaVal,double gammaVal,double epsilonVal){
		reward[(int) goal[0]][(int) goal[1]] = 100;

		int std = -1;
		int iteration = 0;
		int t = 0;
		double teta = 0.0001;

		while(t < 20 || std != 0){
			int i = (int) agent[0];
			int j = (int) agent[1];
			xCoor.clear();
			yCoor.clear();

			xCoor.add(i);
			yCoor.add(j);

			int stepNumber = 0;

			while((i !=(int) goal[0]) || (j !=(int) goal[1])){

				int N = 18;
				double rand = Math.random();

				if(rand > epsilonVal){//greedy move
					if(windyMatrix[i][j][0] == 0){//there is no wind

						int iIndex1;
						int jIndex1;
						takeMaxIndexes result1 = takeMaxValue(Qvalue,i,j,App.SIZE);
						iIndex1 = result1.getI();
						jIndex1 = result1.getJ();
						int action = result1.getAction();

						double rewardValue = reward[iIndex1][jIndex1];

						model[i][j][0] = rewardValue;
						model[i][j][1] = iIndex1;
						model[i][j][2] = jIndex1;
						model[i][j][3] = action;

						takeMaxIndexes result2 = takeMaxValue(Qvalue,iIndex1,jIndex1,App.SIZE);
						int nextAction = result2.getAction();

						xCoor.add(iIndex1);
						yCoor.add(jIndex1);
						i = iIndex1;
						j = jIndex1;
						++stepNumber;

						double p = Math.abs(rewardValue + gammaVal*Qvalue[iIndex1][jIndex1][nextAction] - Qvalue[i][j][action]);
						prioritized(p,teta,i,j,action,Qvalue,model,alphaVal,gammaVal,N,App.SIZE);

					}else{//current state have wind

						//take the max action of current state

						takeMaxIndexes result = takeMaxValue(Qvalue,i,j,App.SIZE);
						int action = result.getAction();

						windEffect windIndex = takeWindIndexes(windyMatrix,i,j,App.SIZE);
						int iIndex = windIndex.getI();
						int jIndex = windIndex.getJ();

						double rewardValue = reward[iIndex][jIndex];

						model[i][j][0] = rewardValue;
						model[i][j][1] = iIndex;
						model[i][j][2] = jIndex;
						model[i][j][3] = action;

						xCoor.add(iIndex);
						yCoor.add(jIndex);

						//now take the next max value and indexes after wind effect

						takeMaxIndexes result2 = takeMaxValue(Qvalue,iIndex,jIndex,App.SIZE);
						int nextAction = result2.getAction();

						double p = Math.abs(rewardValue + gammaVal*Qvalue[iIndex][jIndex][nextAction] - Qvalue[i][j][action]);
						prioritized(p,teta,i,j,action,Qvalue,model,alphaVal,gammaVal,N,App.SIZE);

						int i2 = result2.getI();
						int j2 = result2.getJ();

						rewardValue = reward[i2][j2];

						model[iIndex][jIndex][0] = rewardValue;
						model[iIndex][jIndex][1] = i2;
						model[iIndex][jIndex][2] = j2;
						model[iIndex][jIndex][3] = nextAction;

						takeMaxIndexes result3 = takeMaxValue(Qvalue,i2,j2,App.SIZE);
						int nextAction2 = result3.getAction();

						p = Math.abs(rewardValue + gammaVal*Qvalue[i2][j2][nextAction2] - Qvalue[iIndex][jIndex][nextAction]);
						prioritized(p,teta,iIndex,jIndex,nextAction,Qvalue,model,alphaVal,gammaVal,N,App.SIZE);

						xCoor.add(i2);
						yCoor.add(j2);

						i = i2;
						j = j2;

						++stepNumber;
					}
				}else {//not greedy move

					if(windyMatrix[i][j][0] == 0){//there is no wind

						int iIndex2;
						int jIndex2;

						takeRandom result3 = takeRand(i,j,App.SIZE,Qvalue);
						iIndex2 = result3.getI();
						jIndex2 = result3.getJ();

						int action = result3.getAction();

						double rewardValue = reward[iIndex2][jIndex2];

						model[i][j][0] = rewardValue;
						model[i][j][1] = iIndex2;
						model[i][j][2] = jIndex2;
						model[i][j][3] = action;

						takeMaxIndexes result4 = takeMaxValue(Qvalue,iIndex2,jIndex2,App.SIZE);
						int nextAction = result4.getAction();

						double p = Math.abs(rewardValue + gammaVal*Qvalue[iIndex2][jIndex2][nextAction] - Qvalue[i][j][action]);
						prioritized(p,teta,i,j,action,Qvalue,model,alphaVal,gammaVal,N,App.SIZE);

						xCoor.add(iIndex2);
						yCoor.add(jIndex2);
						i = iIndex2;
						j = jIndex2;
						++stepNumber;
					}
					else{ //have wind(exploration)

						takeRandom result = takeRand(i,j,App.SIZE,Qvalue);
						int action = result.getAction();

						windEffect windIndex = takeWindIndexes(windyMatrix,i,j,App.SIZE);
						int iIndex = windIndex.getI();
						int jIndex = windIndex.getJ();

						double rewardValue = reward[iIndex][jIndex];

						model[i][j][0] = rewardValue;
						model[i][j][1] = iIndex;
						model[i][j][2] = jIndex;
						model[i][j][3] = action;

						xCoor.add(iIndex);
						yCoor.add(jIndex);

						takeMaxIndexes result2 = takeMaxValue(Qvalue,iIndex,jIndex,App.SIZE);
						int nextAction = result2.getAction();

						double p = Math.abs(rewardValue + gammaVal*Qvalue[iIndex][jIndex][nextAction] - Qvalue[i][j][action]);
						prioritized(p,teta,i,j,action,Qvalue,model,alphaVal,gammaVal,N,App.SIZE);

						int i2 = result2.getI();
						int j2 = result2.getJ();

						rewardValue = reward[i2][j2];

						model[iIndex][jIndex][0] = rewardValue;
						model[iIndex][jIndex][1] = i2;
						model[iIndex][jIndex][2] = j2;
						model[iIndex][jIndex][3] = nextAction;

						i = i2;
						j = j2;
						xCoor.add(i2);
						yCoor.add(j2);
						++stepNumber;
						takeMaxIndexes result3 = takeMaxValue(Qvalue,i2,j2,App.SIZE);
						int nextAction2 = result3.getAction();

						p = Math.abs(rewardValue + gammaVal*Qvalue[i2][j2][nextAction2] - Qvalue[iIndex][jIndex][nextAction]);
						prioritized(p,teta,iIndex,jIndex,nextAction,Qvalue,model,alphaVal,gammaVal,N,App.SIZE);
					}
				}
			}//second while end

			if(t < 20){
				twentyMove[t]= stepNumber;
				++t;
			}else if(t == 20){
				std = compute(twentyMove,20);
				++t;
			}else if(t > 20){
				int z;
				for(z = 0;z < (20-1);z++){
					twentyMove[z] = twentyMove[z + 1];
				}
				twentyMove[20-1] = stepNumber;
				std = compute(twentyMove,20);
				++t;
			}
			System.out.println("stepNumber: " + stepNumber);
			++iteration;

		}//first while end

		System.out.println("Iteration: " + iteration);

		int sizeList = xCoor.size();

		for(int a = 0; a < (sizeList-1); a++){
			App.label[(int) xCoor.get(a)][(int) yCoor.get(a)].setStyle("-fx-background-color: black");
		}

		App.label[(int) agent[0]][(int) agent[1]].setStyle("-fx-background-color: black");
		App.label[(int) goal[0]][(int) goal[1]].setStyle("-fx-background-color: red");
	}

	public void doneValues2(ActionEvent event){
		
		String alphaValue = alpha.getText();
		double alphaVal = 0;
		try {
		    alphaVal = Double.parseDouble(alphaValue);
		} catch (NumberFormatException e) {
		    System.out.println("Invalid alpha");
		}
		
		String gammaValue = gamma.getText();
		double gammaVal = 0;
		try {
		    gammaVal = Double.parseDouble(gammaValue);
		} catch (NumberFormatException e) {
		    System.out.println("Invalid gamma");
		}
		
		String epsilonStartVal = startVal.getText();
		double epsilonStart = 0;
		
		try {
		    epsilonStart = Double.parseDouble(epsilonStartVal);
		} catch (NumberFormatException e) {
		    System.out.println("Invalid gamma");
		}
		
		String epsilonEndVal = endVal.getText();
		double epsilonEnd = 0;
		
		try {
		    epsilonEnd = Double.parseDouble(epsilonEndVal);
		} catch (NumberFormatException e) {
		    System.out.println("Invalid gamma");
		}
		
		String getEpisode = episodes.getText();
		double getEpisodes = 0;
		
		try {
		    getEpisodes = Double.parseDouble(getEpisode);
		} catch (NumberFormatException e) {
		    System.out.println("Invalid gamma");
		}

		for(int a = 0; a < App.SIZE; a++){
			for(int b = 0; b < App.SIZE;b++ ){
				App.label[a][b].setStyle("-fx-background-color: none");
				App.label[(int) agent[0]][(int) agent[1]].setStyle("-fx-background-color: black");
				App.label[(int) goal[0]][(int) goal[1]].setStyle("-fx-background-color: red");
				App.label[a][b].setStyle("-fx-border-color: black;");
			}
		}

		String output = algorithm.getSelectionModel().getSelectedItem().toString();

		if(output == "Q Learning"){
			QLearningDynamic(alphaVal,gammaVal,epsilonStart,epsilonEnd,getEpisodes);
		}else if(output == "King's Move"){
			KingsMoveDynamic(alphaVal,gammaVal,epsilonStart,epsilonEnd,getEpisodes);
		}else if(output == "Q Learning(Wind)"){
			QLearningWindDynamic(alphaVal,gammaVal,epsilonStart,epsilonEnd,getEpisodes);
		}else if(output == "Prioritized Sweeping"){
			PrioritizedSweepingDynamic(alphaVal,gammaVal,epsilonStart,epsilonEnd,getEpisodes);
		}
	}
	private void QLearningDynamic(double alphaVal,double gammaVal,double epsilonStart,double epsilonEnd,double getEpisodes){

		reward[(int) goal[0]][(int) goal[1]] = 100;

		int std = -1;
		int iteration = 0;
		int t = 0;

		while(t < 20 || std != 0){
			int i = (int) agent[0];
			int j = (int) agent[1];
			xCoor.clear();
			yCoor.clear();

			xCoor.add(i);
			yCoor.add(j);

			int stepNumber = 0;

			while((i !=(int) goal[0]) || (j !=(int) goal[1])){

				double rand = Math.random();

				if(rand > epsilonStart){ //greedy move
					int iIndex1;
					int jIndex1;
					takeMaxIndexes result1;
					result1 = takeMaxValue(Qvalue,i,j,App.SIZE);
					iIndex1 = result1.getI();
					jIndex1 = result1.getJ();
					int action = result1.getAction();

					double rewardValue = reward[iIndex1][jIndex1];

					takeMaxIndexes result2 = takeMaxValue(Qvalue,iIndex1,jIndex1,App.SIZE);
					int nextAction = result2.getAction();

					Qvalue[i][j][action] = Qvalue[i][j][action] +  alphaVal*(rewardValue + (gammaVal*Qvalue[iIndex1][jIndex1][nextAction]) - Qvalue[i][j][action]);

					xCoor.add(iIndex1);
					yCoor.add(jIndex1);
					i = iIndex1;
					j = jIndex1;
					++stepNumber;
				}else{
					int iIndex2;
					int jIndex2;
					takeRandom result3;
					result3  = takeRand(i,j,App.SIZE,Qvalue);
					iIndex2 = result3.getI();
					jIndex2 = result3.getJ();
					int action = result3.getAction();

					double rewardValue = reward[iIndex2][jIndex2];

					takeMaxIndexes result4 = takeMaxValue(Qvalue,iIndex2,jIndex2,App.SIZE);
					int nextAction = result4.getAction();

					Qvalue[i][j][action] += alphaVal*(rewardValue + (gammaVal*Qvalue[iIndex2][jIndex2][nextAction]) - Qvalue[i][j][action]);

					xCoor.add(iIndex2);
					yCoor.add(jIndex2);
					i = iIndex2;
					j = jIndex2;
					++stepNumber;
				}
			}

			if(t < 20){
				twentyMove[t]= stepNumber;
				++t;
			}else if(t == 20){
				std = compute(twentyMove,20);
				++t;
			}else if(t > 20){
				int z;
				for(z = 0;z < (20-1);z++){
					twentyMove[z] = twentyMove[z + 1];
				}

				twentyMove[20-1] = stepNumber;
				std = compute(twentyMove,20);
				++t;
			}
			if(epsilonStart < 0.05){
				epsilonStart = 0.02;
			}else {
				epsilonStart = epsilonStart * 0.99987;
			}

			System.out.println("stepNumber: " + stepNumber);
			++iteration;

		}

		System.out.println("Iteration: " + iteration);

		int sizeList = xCoor.size();

		for(int a = 0; a < (sizeList-1); a++){
			App.label[(int) xCoor.get(a)][(int) yCoor.get(a)].setStyle("-fx-background-color: black");
		}

		App.label[(int) agent[0]][(int) agent[1]].setStyle("-fx-background-color: black");
		App.label[(int) goal[0]][(int) goal[1]].setStyle("-fx-background-color: red");
	}
	private void KingsMoveDynamic(double alphaVal,double gammaVal,double epsilonStart,double epsilonEnd,double getEpisodes){

		reward[(int) goal[0]][(int) goal[1]] = 100;

		int std = -1;
		int iteration = 0;
		int t = 0;

		while(t < 30 || std != 0){
			int i = (int) agent[0];
			int j = (int) agent[1];
			xCoor.clear();
			yCoor.clear();

			xCoor.add(i);
			yCoor.add(j);
			int stepNumber = 0;

			while((i !=(int) goal[0]) || (j !=(int) goal[1])){

				double rand = Math.random();

				if(rand > epsilonStart){ //greedy move

					int iIndex1;
					int jIndex1;
					takeMaxIndexes result1;
					result1 = takeMaxValue2(Qvalue2,i,j,App.SIZE);
					iIndex1 = result1.getI();
					jIndex1 = result1.getJ();
					int action = result1.getAction();

					double rewardValue = reward[iIndex1][jIndex1];

					takeMaxIndexes result2 = takeMaxValue2(Qvalue2,iIndex1,jIndex1,App.SIZE);
					int nextAction = result2.getAction();

					Qvalue2[i][j][action] = Qvalue2[i][j][action] +  alphaVal*(rewardValue + (gammaVal*Qvalue2[iIndex1][jIndex1][nextAction]) - Qvalue2[i][j][action]);

					xCoor.add(iIndex1);
					yCoor.add(jIndex1);
					i = iIndex1;
					j = jIndex1;
					++stepNumber;
				}else{//exploration

					int iIndex2;
					int jIndex2;
					takeRandom result3;
					result3  = takeRand2(i,j,App.SIZE,Qvalue2);
					iIndex2 = result3.getI();
					jIndex2 = result3.getJ();
					int action = result3.getAction();

					double rewardValue = reward[iIndex2][jIndex2];

					takeMaxIndexes result4 = takeMaxValue2(Qvalue2,iIndex2,jIndex2,App.SIZE);
					int nextAction = result4.getAction();

					Qvalue2[i][j][action] += alphaVal*(rewardValue + (gammaVal*Qvalue2[iIndex2][jIndex2][nextAction]) - Qvalue2[i][j][action]);

					xCoor.add(iIndex2);
					yCoor.add(jIndex2);
					i = iIndex2;
					j = jIndex2;
					++stepNumber;
				}
			}

			if(t < 30){
				thirtyMove[t]= stepNumber;
				++t;
			}else if(t == 30){
				std = compute(thirtyMove,30);
				++t;
			}else if(t > 30){
				int z;
				for(z = 0;z < (30-1);z++){
					thirtyMove[z] = thirtyMove[z + 1];
				}
				thirtyMove[30-1] = stepNumber;
				std = compute(thirtyMove,30);
				++t;
			}

			if(epsilonStart < 0.002){
				epsilonStart = 0.002;
			}else{
				epsilonStart = epsilonStart * 0.99987;
			}

			System.out.println("stepNumber: " + stepNumber);
			++iteration;
		}
		System.out.println("Iteration: " + iteration);
		int sizeList = xCoor.size();

		for(int a = 0; a < (sizeList-1); a++){
			App.label[(int) xCoor.get(a)][(int) yCoor.get(a)].setStyle("-fx-background-color: black");
		}

		App.label[(int) agent[0]][(int) agent[1]].setStyle("-fx-background-color: black");
		App.label[(int) goal[0]][(int) goal[1]].setStyle("-fx-background-color: red");
	}
	private void QLearningWindDynamic(double alphaVal,double gammaVal,double epsilonStart,double epsilonEnd,double getEpisodes){


		reward[(int) goal[0]][(int) goal[1]] = 100;

		int std = -1;
		int iteration = 0;
		int t = 0;

		while(t < 20 || std != 0){

			xCoor.clear();
			yCoor.clear();

			int i = (int) agent[0];
			int j = (int) agent[1];

			xCoor.add(i);
			yCoor.add(j);

			int stepNumber = 0;

			while((i !=(int) goal[0]) || (j !=(int) goal[1])){

				double rand = Math.random();

				if(rand > epsilonStart){//greedy move
					if(windyMatrix[i][j][0] == 0){//there is no wind

						int iIndex1;
						int jIndex1;
						takeMaxIndexes result1 = takeMaxValue(Qvalue,i,j,App.SIZE);
						iIndex1 = result1.getI();
						jIndex1 = result1.getJ();
						int action = result1.getAction();

						double rewardValue = reward[iIndex1][jIndex1];

						takeMaxIndexes result2 = takeMaxValue(Qvalue,iIndex1,jIndex1,App.SIZE);
						int nextAction = result2.getAction();

						Qvalue[i][j][action] = Qvalue[i][j][action] +  alphaVal*(rewardValue + (gammaVal*Qvalue[iIndex1][jIndex1][nextAction]) - Qvalue[i][j][action]);

						xCoor.add(iIndex1);
						yCoor.add(jIndex1);
						i = iIndex1;
						j = jIndex1;
						++stepNumber;
					}else{

						takeMaxIndexes result = takeMaxValue(Qvalue,i,j,App.SIZE);
						int action = result.getAction();

						windEffect windIndex = takeWindIndexes(windyMatrix,i,j,App.SIZE);
						int iIndex = windIndex.getI();
						int jIndex = windIndex.getJ();

						double rewardValue = reward[iIndex][jIndex];

						takeMaxIndexes result2 = takeMaxValue(Qvalue,iIndex,jIndex,App.SIZE);
						int nextAction = result2.getAction();

						Qvalue[i][j][action] += alphaVal*(rewardValue + (gammaVal*Qvalue[iIndex][jIndex][nextAction]) - Qvalue[i][j][action]);

						int i2 = result2.getI();
						int j2 = result2.getJ();

						xCoor.add(iIndex);
						yCoor.add(jIndex);

						takeMaxIndexes result3 = takeMaxValue(Qvalue,i2,j2,App.SIZE);
						int nextAction2 = result3.getAction();

						Qvalue[iIndex][jIndex][nextAction] +=  alphaVal*(rewardValue + (gammaVal*Qvalue[i2][j2][nextAction2]) - Qvalue[iIndex][jIndex][nextAction]);

						xCoor.add(i2);
						yCoor.add(j2);
						i = i2;
						j = j2;
						++stepNumber;
					}
				}else {
					if(windyMatrix[i][j][0] == 0){//there is no wind

						int iIndex2;
						int jIndex2;
						takeRandom result3 = takeRand(i,j,App.SIZE,Qvalue);
						iIndex2 = result3.getI();
						jIndex2 = result3.getJ();
						int action = result3.getAction();

						double rewardValue = reward[iIndex2][jIndex2];

						takeMaxIndexes result4 = takeMaxValue(Qvalue,iIndex2,jIndex2,App.SIZE);
						int nextAction = result4.getAction();

						Qvalue[i][j][action] += alphaVal*(rewardValue + (gammaVal*Qvalue[iIndex2][jIndex2][nextAction]) - Qvalue[i][j][action]);

						xCoor.add(iIndex2);
						yCoor.add(jIndex2);
						i = iIndex2;
						j = jIndex2;
						++stepNumber;
					}
					else{ //have wind

						takeRandom result = takeRand(i,j,App.SIZE,Qvalue);
						int action = result.getAction();

						windEffect windIndex = takeWindIndexes(windyMatrix,i,j,App.SIZE);
						int iIndex = windIndex.getI();
						int jIndex = windIndex.getJ();

						takeRandom result2 = takeRand(iIndex,jIndex,App.SIZE,Qvalue);
						int i2 = result2.getI();
						int j2 = result2.getJ();

						takeMaxIndexes result3 = takeMaxValue(Qvalue,iIndex,jIndex,App.SIZE);
						int nextAction = result3.getAction();

						double rewardValue = reward[iIndex][jIndex];

						Qvalue[i][j][action] += alphaVal*(rewardValue + (gammaVal*Qvalue[iIndex][jIndex][nextAction]) - Qvalue[i][j][action]);

						xCoor.add(iIndex);
						yCoor.add(jIndex);
						i = i2;
						j = j2;
						xCoor.add(i2);
						yCoor.add(j2);
						++stepNumber;

						takeMaxIndexes result4 = takeMaxValue(Qvalue,i2,j2,App.SIZE);
						int nextAction2 = result4.getAction();

						Qvalue[iIndex][jIndex][nextAction] += alphaVal*(rewardValue + (gammaVal*Qvalue[i2][j2][nextAction2]) - Qvalue[iIndex][jIndex][nextAction]);
					}
				}
			}//second while end

			if(t < 20){
				twentyMove[t]= stepNumber;
				++t;
			}else if(t == 20){
				std = compute(twentyMove,20);
				++t;
			}else if(t > 20){
				int z;
				for(z = 0;z < (20-1);z++){
					twentyMove[z] = twentyMove[z + 1];
				}
				twentyMove[20-1] = stepNumber;
				std = compute(twentyMove,20);
				++t;
			}

			if(epsilonStart < epsilonEnd){
				epsilonStart = epsilonEnd;
			}else{
				epsilonStart = epsilonStart * 0.99987;
			}

			System.out.println("stepNumber: " + stepNumber);
			++iteration;

		}//first while end

		System.out.println("Iteration: " + iteration);
		System.out.println("epsilon: " + epsilonStart);
		int sizeList = xCoor.size();

		for(int a = 0; a < (sizeList-1); a++){
			App.label[(int) xCoor.get(a)][(int) yCoor.get(a)].setStyle("-fx-background-color: black");
		}

		App.label[(int) agent[0]][(int) agent[1]].setStyle("-fx-background-color: black");
		App.label[(int) goal[0]][(int) goal[1]].setStyle("-fx-background-color: red");

	}
	private void PrioritizedSweepingDynamic(double alphaVal,double gammaVal,double epsilonStart,double epsilonEnd,double getEpisodes){
		reward[(int) goal[0]][(int) goal[1]] = 100;

		int std = -1;
		int iteration = 0;
		int t = 0;
		double teta = 0.0001;

		while(t < 20 || std != 0){
			int i = (int) agent[0];
			int j = (int) agent[1];
			xCoor.clear();
			yCoor.clear();

			xCoor.add(i);
			yCoor.add(j);

			int stepNumber = 0;

			while((i !=(int) goal[0]) || (j !=(int) goal[1])){

				int N = 5;
				double rand = Math.random();

				if(rand > epsilonStart){//greedy move
					if(windyMatrix[i][j][0] == 0){//there is no wind

						int iIndex1;
						int jIndex1;
						takeMaxIndexes result1 = takeMaxValue(Qvalue,i,j,App.SIZE);

						iIndex1 = result1.getI();
						jIndex1 = result1.getJ();

						int action = result1.getAction();
						double rewardValue = reward[iIndex1][jIndex1];

						model[i][j][0] = rewardValue;
						model[i][j][1] = iIndex1;
						model[i][j][2] = jIndex1;
						model[i][j][3] = action;

						takeMaxIndexes result2 = takeMaxValue(Qvalue,iIndex1,jIndex1,App.SIZE);
						int nextAction = result2.getAction();

						xCoor.add(iIndex1);
						yCoor.add(jIndex1);

						i = iIndex1;
						j = jIndex1;

						++stepNumber;

						double p = Math.abs(rewardValue + gammaVal*Qvalue[iIndex1][jIndex1][nextAction] - Qvalue[i][j][action]);
						prioritized(p,teta,i,j,action,Qvalue,model,alphaVal,gammaVal,N,App.SIZE);

					}else{//current state have wind

						//take the max action of current state

						takeMaxIndexes result = takeMaxValue(Qvalue,i,j,App.SIZE);
						int action = result.getAction();

						windEffect windIndex = takeWindIndexes(windyMatrix,i,j,App.SIZE);
						int iIndex = windIndex.getI();
						int jIndex = windIndex.getJ();

						double rewardValue = reward[iIndex][jIndex];

						model[i][j][0] = rewardValue;
						model[i][j][1] = iIndex;
						model[i][j][2] = jIndex;
						model[i][j][3] = action;

						xCoor.add(iIndex);
						yCoor.add(jIndex);

						takeMaxIndexes result2 = takeMaxValue(Qvalue,iIndex,jIndex,App.SIZE);
						int nextAction = result2.getAction();

						double p = Math.abs(rewardValue + gammaVal*Qvalue[iIndex][jIndex][nextAction] - Qvalue[i][j][action]);
						prioritized(p,teta,i,j,action,Qvalue,model,alphaVal,gammaVal,N,App.SIZE);

						int i2 = result2.getI();
						int j2 = result2.getJ();

						rewardValue = reward[i2][j2];

						model[iIndex][jIndex][0] = rewardValue;
						model[iIndex][jIndex][1] = i2;
						model[iIndex][jIndex][2] = j2;
						model[iIndex][jIndex][3] = nextAction;

						takeMaxIndexes result3 = takeMaxValue(Qvalue,i2,j2,App.SIZE);
						int nextAction2 = result3.getAction();

						p = Math.abs(rewardValue + gammaVal*Qvalue[i2][j2][nextAction2] - Qvalue[iIndex][jIndex][nextAction]);
						prioritized(p,teta,iIndex,jIndex,nextAction,Qvalue,model,alphaVal,gammaVal,N,App.SIZE);

						xCoor.add(i2);
						yCoor.add(j2);
						i = i2;
						j = j2;
						++stepNumber;
					}
				}else {//not greedy move

					if(windyMatrix[i][j][0] == 0){//there is no wind

						int iIndex2;
						int jIndex2;

						takeRandom result3 = takeRand(i,j,App.SIZE,Qvalue);
						iIndex2 = result3.getI();
						jIndex2 = result3.getJ();

						int action = result3.getAction();

						double rewardValue = reward[iIndex2][jIndex2];

						model[i][j][0] = rewardValue;
						model[i][j][1] = iIndex2;
						model[i][j][2] = jIndex2;
						model[i][j][3] = action;

						takeMaxIndexes result4 = takeMaxValue(Qvalue,iIndex2,jIndex2,App.SIZE);
						int nextAction = result4.getAction();

						double p = Math.abs(rewardValue + gammaVal*Qvalue[iIndex2][jIndex2][nextAction] - Qvalue[i][j][action]);
						prioritized(p,teta,i,j,action,Qvalue,model,alphaVal,gammaVal,N,App.SIZE);

						xCoor.add(iIndex2);
						yCoor.add(jIndex2);
						i = iIndex2;
						j = jIndex2;
						++stepNumber;
					}
					else{ //have wind

						takeRandom result = takeRand(i,j,App.SIZE,Qvalue);
						int action = result.getAction();

						windEffect windIndex = takeWindIndexes(windyMatrix,i,j,App.SIZE);
						int iIndex = windIndex.getI();
						int jIndex = windIndex.getJ();

						double rewardValue = reward[iIndex][jIndex];

						model[i][j][0] = rewardValue;
						model[i][j][1] = iIndex;
						model[i][j][2] = jIndex;
						model[i][j][3] = action;

						xCoor.add(iIndex);
						yCoor.add(jIndex);

						takeMaxIndexes result2 = takeMaxValue(Qvalue,iIndex,jIndex,App.SIZE);
						int nextAction = result2.getAction();

						double p = Math.abs(rewardValue + gammaVal*Qvalue[iIndex][jIndex][nextAction] - Qvalue[i][j][action]);
						prioritized(p,teta,i,j,action,Qvalue,model,alphaVal,gammaVal,N,App.SIZE);

						int i2 = result2.getI();
						int j2 = result2.getJ();

						rewardValue = reward[i2][j2];

						model[iIndex][jIndex][0] = rewardValue;
						model[iIndex][jIndex][1] = i2;
						model[iIndex][jIndex][2] = j2;
						model[iIndex][jIndex][3] = nextAction;

						i = i2;
						j = j2;
						xCoor.add(i2);
						yCoor.add(j2);
						++stepNumber;

						takeMaxIndexes result3 = takeMaxValue(Qvalue,i2,j2,App.SIZE);
						int nextAction2 = result3.getAction();

						p = Math.abs(rewardValue + gammaVal*Qvalue[i2][j2][nextAction2] - Qvalue[iIndex][jIndex][nextAction]);
						prioritized(p,teta,iIndex,jIndex,nextAction,Qvalue,model,alphaVal,gammaVal,N,App.SIZE);

					}
				}
			}//second while end

			if(t < 20){
				twentyMove[t]= stepNumber;
				++t;
			}else if(t == 20){
				std = compute(twentyMove,20);
				++t;
			}else if(t > 20){
				int z;
				for(z = 0;z < (20-1);z++){
					twentyMove[z] = twentyMove[z + 1];
				}
				twentyMove[20-1] = stepNumber;
				std = compute(twentyMove,20);
				++t;
			}
			if(epsilonStart < epsilonEnd){
				epsilonStart = epsilonEnd;
			}else{
				epsilonStart = epsilonStart * 0.99987;
			}
			System.out.println("stepNumber: " + stepNumber);
			++iteration;

		}//first while end

		System.out.println("Iteration: " + iteration);

		int sizeList = xCoor.size();

		for(int a = 0; a < (sizeList-1); a++){
			App.label[(int) xCoor.get(a)][(int) yCoor.get(a)].setStyle("-fx-background-color: black");
		}
		App.label[(int) agent[0]][(int) agent[1]].setStyle("-fx-background-color: black");
		App.label[(int) goal[0]][(int) goal[1]].setStyle("-fx-background-color: red");

	}
	
	private static windEffect takeWindIndexes(int windyMatrix[][][],int i , int j,int dimension){
		int iIndex = i;
		int jIndex = j;
		
		if(windyMatrix[i][j][0] == 1){//North
			if(windyMatrix[i][j][1] == 1){//gale force (1)
				
				if((i-1) >= 0){
					iIndex = i-1;
				}else if((i-1) < 0){
					iIndex = 0;
				}
					
			}else if(windyMatrix[i][j][1] == 2){//gale force (2)
				
				if((i-2) >= 0){
					iIndex = i-2;
				}else if((i-2) < 0){
					iIndex = 0;
				}
			}else if(windyMatrix[i][j][1] == 3){//gale force (3)
				
				if((i-3) >= 0){
					iIndex = i-3;
				}else if((i-3) < 0){
					iIndex = 0;
				}
			}else{
				System.out.println("Exception1");
			}
		}else if(windyMatrix[i][j][0] == 2){//South
			if(windyMatrix[i][j][1] == 1){//gale force (1)
				if((i+1) < dimension){
					iIndex = i+1;
				}else if((i+1) >= dimension){
					iIndex = dimension-1;
				}
				
			}else if(windyMatrix[i][j][1] == 2){//gale force (2)
				if((i+2) < dimension){
					iIndex = i+2;
				}else if((i+2) >= dimension){
					iIndex = dimension-1;
				}

			}else if(windyMatrix[i][j][1] == 3){//gale force (3)
				if((i+3) < dimension){
					iIndex = i+3;
				}else if((i+3) >= dimension){
					iIndex = dimension-1;
				}

			}else{
				System.out.println("Exception2");
			}
		}else if(windyMatrix[i][j][0] == 3){//East
			if(windyMatrix[i][j][1] == 1){//gale force (1)
				if((j+1) < dimension){
					jIndex = j+1;
				}else if((j+1) >= dimension){
					jIndex = dimension-1;
				}

			}else if(windyMatrix[i][j][1] == 2){//gale force (2)
				if((j+2) < dimension){
					jIndex = j+2;
				}else if((j+2) >= dimension){
					jIndex = dimension-1;
				}
				
			}else if(windyMatrix[i][j][1] == 3){//gale force (3)
				if((j+3) < dimension){
					jIndex = j+3;
				}else if((j+3) >= dimension){
					jIndex = dimension-1;
				}

			}else{
				System.out.println("Exception3");
			}
		}else if(windyMatrix[i][j][0] == 4){//West
			if(windyMatrix[i][j][1] == 1){//gale force (1)
				if((j-1) >= 0){
					jIndex = j-1;
				}else if((j-1) < 0){
					jIndex = 0;
				}
				
			}else if(windyMatrix[i][j][1] == 2){//gale force (2)
				if((j-2) >= 0){
					jIndex = j-2;
				}else if((j-2) < 0){
					jIndex = 0;
				}
				
			}else if(windyMatrix[i][j][1] == 3){//gale force (3)
				if((j-3) >= 0){
					jIndex = j-3;
				}else if((j-3) < 0){
					jIndex = 0;
				}
			}else{
				System.out.println("Exception4");
			}
		}else {
			System.out.println("Exception5");
		}

		return new windEffect (iIndex,jIndex);
	}
	private static void prioritized(double p, double teta, int i, int j, int action, double[][][] Qvalue, double[][][] model, double alfa, double gama, int N, int dimension){

		PriorityQueue<stateModel> modelQueue = new PriorityQueue<>(new pComparator());
		stateModel first;

		int tempI,tempJ,tempAction;
		int tempiIndex2,tempjIndex2;
		int rewardValue;

		if(p > teta){
			modelQueue.offer(new stateModel(i,j,action,p));
		}

		while(modelQueue.isEmpty() != true && N != 0){
			first = modelQueue.poll();
			tempI = first.getxCoor();
			tempJ = first.getyCoor();
			tempAction = first.getAction();

			tempiIndex2 = (int) model[tempI][tempJ][1];
			tempjIndex2 = (int) model[tempI][tempJ][2];
			rewardValue = (int) model[tempI][tempJ][0];
			int nextAction2 = (int) model[tempiIndex2][tempjIndex2][3];

			Qvalue[tempI][tempJ][tempAction] +=  alfa*(rewardValue + (gama*Qvalue[tempiIndex2][tempjIndex2][nextAction2]) - Qvalue[tempI][tempJ][tempAction]);

			int x,y;
			int predictedReward;
			for(x = 0; x < dimension;x++){
				for(y = 0;y < dimension;y++){
					if(model[x][y][1] == tempI && model[x][y][2] == tempJ){
						
						predictedReward = (int) model[x][y][0];
						takeMaxIndexes result5 = takeMaxValue(Qvalue,tempI,tempJ,dimension);
						int action2 = result5.getAction();
						int predictedAction = (int) model[x][y][3];
						p = predictedReward + gama*Qvalue[tempI][tempJ][action2] - Qvalue[x][y][predictedAction];

						if(p > teta){
							modelQueue.offer(new stateModel(x,y,predictedAction,p));

						}
					}
				}
			}
			N = N - 1;
		}

	}
	private static int compute(int[] fiveMove,int move){

		int i;
		int total = 0;
		for(i = 0;i < (move-1);i++){
			total = (int) (total + Math.pow(fiveMove[i]-fiveMove[i+1],2));
		}

		return total;
	}
	private static takeRandom takeRand2(int i,int j,int dimension,double[][][] Q){

		ArrayList list = new ArrayList();

		int z;
		for(z = 0;z < 8; z++){
			list.add(z);
		}

		Random random = new Random();
		int whichAction;
		if(Q[0][0][0] == 0 && Q[0][0][1] == 0 && Q[0][0][2] == 0 && Q[0][0][3] == 0 &&
				Q[0][0][4] == 0 && Q[0][0][5] == 0 && Q[0][0][6] == 0 && Q[0][0][7] == 0){

			int number = random.nextInt(list.size());
			whichAction = (int) list.get(number);

		}else{

			double max = -99999;

			//find max action
			for(z = 0;z <8;z++){
				if(Q[0][0][z]>max){
					max = Q[0][0][z];
				}
			}

			//find how many max is equal
			int numberMax = 0;
			for(z = 7;z > 0;z--){
				if(Q[0][0][z] == max){
					list.remove(z);
					++numberMax;
				}
			}

			int number = random.nextInt(list.size());
			whichAction = (int) list.get(number);
		}
		
		int iIndex=-1,jIndex=-1;
		
		if(whichAction == 0){
			iIndex = i-1;
			jIndex = j;
			if(iIndex < 0)
				iIndex = i;
		}else if(whichAction == 1){
			iIndex = i-1;
			jIndex = j-1;
				if(iIndex < 0 || jIndex < 0){
					iIndex = i;
					jIndex = j;
				}
		}else if(whichAction == 2){
			iIndex = i;
			jIndex = j-1;
				if(jIndex < 0)
					jIndex = j;
		}else if(whichAction == 3){
			iIndex = i+1;
			jIndex = j-1;
				if(iIndex == dimension || jIndex < 0){
					iIndex = i;
					jIndex = j;
				}
		}else if(whichAction == 4){
			iIndex = i+1;
			jIndex = j;
				if(iIndex == dimension)
					iIndex = i;
		}else if(whichAction == 5){
			iIndex = i+1;
			jIndex = j+1;
				if(iIndex == dimension || jIndex == dimension){
					iIndex = i;
					jIndex = j;
				}
		}else if(whichAction == 6){
			iIndex = i;
			jIndex = j+1;
				if(jIndex == dimension)
					jIndex = j;
		}else if(whichAction == 7){
			iIndex = i-1;
			jIndex = j+1;
				if(iIndex < 0 || jIndex == dimension){
					iIndex = i;
					jIndex = j;
				}
		}
		else{
			System.out.println("Wrong action");
		}
		return new takeRandom(iIndex,jIndex,whichAction);
	}
	private static takeRandom takeRand(int i,int j,int dimension,double[][][] Q){
		ArrayList list = new ArrayList();

		int z;
		for(z = 0;z < 4; z++){
			list.add(z);
		}

		Random random = new Random();
		int whichAction;
		if(Q[0][0][0] == 0 && Q[0][0][1] == 0 && Q[0][0][2] == 0 && Q[0][0][3] == 0){

			int number = random.nextInt(list.size());
			whichAction = (int) list.get(number);

		}else{

			double max = -99999;

			//find max action
			for(z = 0;z <4;z++){
				if(Q[0][0][z]>max){
					max = Q[0][0][z];
				}
			}

			//find how many max is equal
			int numberMax = 0;
			for(z = 3;z > 0;z--){
				if(Q[0][0][z] == max){
					list.remove(z);
					++numberMax;
				}
			}

			int number = random.nextInt(list.size());
			whichAction = (int) list.get(number);
		}

		int iIndex,jIndex;
		
		if(whichAction == 0){
			iIndex = i-1;
			jIndex = j;
			if(iIndex < 0)
				iIndex = i;
		}else if(whichAction == 1){
			iIndex = i;
			jIndex = j-1;
				if(jIndex < 0)
					jIndex = j;
		}else if(whichAction == 2){
			iIndex = i+1;
			jIndex = j;
				if(iIndex == dimension)
					iIndex = i;
		}else{
			iIndex = i;
			jIndex = j+1;
				if(jIndex == dimension)
					jIndex = j;
		}
		return new takeRandom(iIndex,jIndex,whichAction);
}
	private static takeMaxIndexes takeMaxValue2(double Q[][][],int i,int j,int dimension){
		
		Random random = new Random();
		
		int z;
		double max = -99999;

		ArrayList list = new ArrayList();

		for(z = 0;z <8;z++){
			if(Q[i][j][z]>max){
				max = Q[i][j][z];
			}
		}
		
		for(z = 0;z < 8;z++){
			if(Q[i][j][z] == max){
				list.add(z);
			}
		}
		
		int number = random.nextInt(list.size());
		int whichAction = (int) list.get(number);
		int iIndex = -1,jIndex = -1;
		
		if(whichAction == 0){
			iIndex = i-1;
			jIndex = j;
			if(iIndex < 0)
				iIndex = i;
		}else if(whichAction == 1){
			iIndex = i-1;
			jIndex = j-1;
				if(iIndex < 0 || jIndex < 0){
					iIndex = i;
					jIndex = j;
				}
		}else if(whichAction == 2){
			iIndex = i;
			jIndex = j-1;
				if(jIndex < 0)
					jIndex = j;
		}else if(whichAction == 3){
			iIndex = i+1;
			jIndex = j-1;
				if(iIndex == dimension || jIndex < 0){
					iIndex = i;
					jIndex = j;
				}
		}else if(whichAction == 4){
			iIndex = i+1;
			jIndex = j;
				if(iIndex == dimension)
					iIndex = i;
		}else if(whichAction == 5){
			iIndex = i+1;
			jIndex = j+1;
				if(iIndex == dimension || jIndex == dimension){
					iIndex = i;
					jIndex = j;
				}
		}else if(whichAction == 6){
			iIndex = i;
			jIndex = j+1;
				if(jIndex == dimension)
					jIndex = j;
		}else if(whichAction == 7){
			iIndex = i-1;
			jIndex = j+1;
				if(iIndex < 0 || jIndex == dimension){
					iIndex = i;
					jIndex = j;
				}
		}
		else{
			System.out.println("Wrong action");
		}
		return new takeMaxIndexes(iIndex,jIndex,whichAction);
	}
	private static takeMaxIndexes takeMaxValue(double Q[][][],int i,int j,int dimension){
		Random random = new Random();
		
		int z = 0;
		double max = -99999;

		ArrayList list = new ArrayList();

		for(z = 0;z <4;z++){
			if(Q[i][j][z]>max){
				max = Q[i][j][z];
			}
		}
		
		for(z = 0;z < 4;z++){
			if(Q[i][j][z] == max){
				list.add(z);
			}
		}
		
		int number = random.nextInt(list.size());
		
		int whichAction = (int) list.get(number);
		int iIndex,jIndex;
		
		if(whichAction == 0){
			iIndex = i-1;
			jIndex = j;
			if(iIndex < 0)
				iIndex = i;
		}else if(whichAction == 1){
			iIndex = i;
			jIndex = j-1;
				if(jIndex < 0)
					jIndex = j;
		}else if(whichAction == 2){
			iIndex = i+1;
			jIndex = j;
				if(iIndex == dimension)
					iIndex = i;
		}else{
			iIndex = i;
			jIndex = j+1;
				if(jIndex == dimension)
					jIndex = j;
		}
		return new takeMaxIndexes(iIndex,jIndex,whichAction);
	}
	public void showValues(ActionEvent event) throws IOException{

		String output = algorithm.getSelectionModel().getSelectedItem().toString();

		if(output == "King's Move"){

			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/QValue.fxml"));
			BorderPane pane = (BorderPane)fxmlLoader.load();

			Image image1 = new Image(getClass().getResourceAsStream("sol.jpg"));
			Image image2 = new Image(getClass().getResourceAsStream("sag.jpg"));
			Image image3 = new Image(getClass().getResourceAsStream("yukarÄ±.jpg"));
			Image image4 = new Image(getClass().getResourceAsStream("asagi.jpg"));
			Image image5 = new Image(getClass().getResourceAsStream("solY.jpg"));
			Image image6 = new Image(getClass().getResourceAsStream("solA.jpg"));
			Image image7 = new Image(getClass().getResourceAsStream("sagY.jpg"));
			Image image8 = new Image(getClass().getResourceAsStream("sagA.jpg"));


			for (int i = 0; i < App.SIZE; i++) {
				for (int j = 0; j < App.SIZE; j++) {
					QLabel[i][j] = new Label(); // This is missing in the original code
					QLabel[i][j].setPrefHeight(60);
					QLabel[i][j].setPrefWidth(60);
					QLabel[i][j].setAlignment(Pos.CENTER);
					QLabel[i][j].setStyle("-fx-border-color: black;");

					int a;
					double max = -9999;
					int action = 1;
					for(a = 0;a < 8;a++){
						if(Qvalue2[i][j][a] > max){
							max = Qvalue2[i][j][a];
							action = a;
						}
					}

					if(Qvalue2[i][j][0] == 0 && Qvalue2[i][j][1] == 0 && Qvalue2[i][j][2] == 0 && Qvalue2[i][j][3] == 0
							&& Qvalue2[i][j][4] == 0 && Qvalue2[i][j][5] == 0 && Qvalue2[i][j][6] == 0 && Qvalue2[i][j][7] == 0){
						QLabel[i][j].setText(" ");
					}
					else if(action == 0){
						QLabel[i][j].setGraphic(new ImageView(image3));
					}else if(action == 1){
						QLabel[i][j].setGraphic(new ImageView(image5));
					}else if(action == 2){
						QLabel[i][j].setGraphic(new ImageView(image1));
					}else if(action == 3){
						QLabel[i][j].setGraphic(new ImageView(image6));
					}else if(action == 4){
						QLabel[i][j].setGraphic(new ImageView(image4));
					}else if(action == 5){
						QLabel[i][j].setGraphic(new ImageView(image8));
					}else if(action == 6){
						QLabel[i][j].setGraphic(new ImageView(image2));
					}else{
						QLabel[i][j].setGraphic(new ImageView(image7));
					}
					QGrid.add(QLabel[i][j],j , i);
				}
			}

			pane.setCenter(QGrid);

			Stage stage = new Stage();
			stage.setTitle("Q Values");
			Scene scene = new Scene(pane);
			stage.setScene(scene);
			stage.show();
		}else{
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/QValue.fxml"));
			BorderPane pane = (BorderPane)fxmlLoader.load();

			Image image1 = new Image(getClass().getResourceAsStream("sol.jpg"));
			Image image2 = new Image(getClass().getResourceAsStream("sag.jpg"));
			Image image3 = new Image(getClass().getResourceAsStream("yukarÄ±.jpg"));
			Image image4 = new Image(getClass().getResourceAsStream("asagi.jpg"));


			for (int i = 0; i < App.SIZE; i++) {
				for (int j = 0; j < App.SIZE; j++) {

					QLabel[i][j] = new Label(); // This is missing in the original code
					QLabel[i][j].setPrefHeight(60);
					QLabel[i][j].setPrefWidth(60);
					QLabel[i][j].setAlignment(Pos.CENTER);
					QLabel[i][j].setStyle("-fx-border-color: black;");

					int a;
					double max = -9999;
					int action = 1;
					for(a = 0;a < 4;a++){
						if(Qvalue[i][j][a] > max){
							max = Qvalue[i][j][a];
							action = a;
						}
					}

					if(Qvalue[i][j][0] == 0 && Qvalue[i][j][1] == 0 && Qvalue[i][j][2] == 0 && Qvalue[i][j][3] == 0 ){
						QLabel[i][j].setText(" ");
					}
					else if(action == 0){
						QLabel[i][j].setGraphic(new ImageView(image3));

					}else if(action == 1){
						QLabel[i][j].setGraphic(new ImageView(image1));

					}else if(action == 2){
						QLabel[i][j].setGraphic(new ImageView(image4));

					}else{
						QLabel[i][j].setGraphic(new ImageView(image2));
					}
					QGrid.add(QLabel[i][j],j , i);
				}
			}

			pane.setCenter(QGrid);
			Stage stage = new Stage();
			stage.setTitle("Q Values");
			Scene scene = new Scene(pane);
			stage.setScene(scene);
			stage.show();
		}
	}
}
