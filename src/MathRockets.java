/* Ainsley Taylor
 * ICS3U
 * January 22 2019
 * Math rocket game
 */

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.animation.AnimationTimer;
import javafx.scene.media.AudioClip;


public class MathRockets extends Application {

	int rocketNum = 3; //number of rockets on screen at any given time
	double[] rocketX = new double[rocketNum]; //rocket's x position
	double[] rocketY = new double[rocketNum]; //rocket's y position
	int rocketLength = 200; //length of rocket
	int rocketHeight = 100; //height of rocket
	long[] rocketLaunchTime = new long[rocketNum]; //time rocket is launched
	long startTime = System.nanoTime();
	double[] rocketSpeed = new double[rocketNum]; //speed of rocket
	double starSpeed = 40; //speed of stars
	String[] equation = new String[rocketNum]; //equation as a string
	int[] product = new int[rocketNum]; //equation product
	int[] num1 = new int[rocketNum]; //first number of equation
	int[] num2 = new int[rocketNum]; //second number of equation
	boolean haveFirstDigit = false;
	int currentAns = -1; //current inputed answer
	boolean[] rocketIsExploding = new boolean[rocketNum];
	long[] explosionStartTime = new long[rocketNum]; //time explosion starts
	double explosionFrameDuration = 0.1; //duration of explosion
	int points = 0; //points of player
	long wrongTime; //duration of X
	final int INTRO = 0; //first state - introduction screen
	final int GAME = 1; //second state - game
	final int BIG_EXPLOSION = 2; //third state - big explosion
	final int GAME_OVER = 3; //fourth state - game over screen
	int state = INTRO; //state the game is in
	long stateStartTime = System.nanoTime(); //start time of state
	AudioClip rocketExplosionSound = new AudioClip("file:src/explosion.wav"); //rocket explosion sound effect
	AudioClip bigExplosionSound = new AudioClip("file:src/bigExplosion.wav"); //big explosion sound effect
	AudioClip rocketLaunchSound = new AudioClip("file:src/launch.wav"); //rocket launch sound effect
	AudioClip wrongSound = new AudioClip("file:src/Error.mp3"); //X sound
	
	
	public int getSecondNumber(int firstNum) {
		
		//ensures product does not exceed 99
		switch (firstNum) {
			case 0: return (int)(13*Math.random());
			case 1: return (int)(13*Math.random());
			case 2: return (int)(13*Math.random());
			case 3: return (int)(13*Math.random());
			case 4: return (int)(13*Math.random());
			case 5: return (int)(13*Math.random());
			case 6: return (int)(13*Math.random());
			case 7: return (int)(13*Math.random());
			case 8: return (int)(13*Math.random());
			case 9: return (int)(12*Math.random());
			case 10: return (int)(10*Math.random());
			case 11: return (int)(10*Math.random());
			case 12: return (int)(9*Math.random());
			default: return -1;
		
		}
	}
	
	public void explodeRocket(int rocketNum) {
		
		rocketExplosionSound.play();
		product[rocketNum] = -1; //deletes current product number
		explosionStartTime[rocketNum] = System.nanoTime();
		rocketIsExploding[rocketNum] = true;
		points++; //adds a point
	}
	
	public void makeNewRocket(int rocketNum) {
		
		rocketLaunchSound.play();
		rocketIsExploding[rocketNum] = false;
		//launches new rocket with new data
		rocketLaunchTime[rocketNum] = System.nanoTime();
		rocketX[rocketNum] = 0;
		rocketY[rocketNum] = (int)(571*Math.random() + 50);
		rocketSpeed[rocketNum] = (151*Math.random() + 50);
		num1[rocketNum] = (int)(13*Math.random());
		num2[rocketNum] = getSecondNumber(num1[rocketNum]);
		equation[rocketNum] = num1[rocketNum] + " x " + num2[rocketNum];
		product[rocketNum] = num1[rocketNum]*num2[rocketNum];
	}
	
	public static void main(String[] args) {
		
		launch(args);
		
	}
	
	@Override
	public void start(Stage theStage) {
		
		theStage.setTitle("Rocket Animation");
		Group root = new Group();
		Scene theScene = new Scene(root);
		theStage.setScene(theScene);
		
		//new canvas, colored black
		Canvas canvas = new Canvas(1400, 700);
		root.getChildren().add(canvas);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, 1400, 700);
		
		
		
		//adds images
		Image rocket = new Image("rocket.png");
		Image wrong = new Image("X sign.png");
		Image[] explosion = new Image[6];
		
		for (int i = 0; i < explosion.length; i++) {
			explosion[i] = new Image("explosion" + i + ".png");
		}
		
		//creates fonts
		Font equationFont = Font.font("Calibri", FontWeight.BOLD, 30);
		Font answerFont = Font.font("Calibri", FontWeight.BOLD, 48);
		Font pointsFont = Font.font("Calibri", FontWeight.BOLD, 24);
		Font introFont = Font.font("Calibri", FontWeight.BOLD, 18);
		gc.setFont(equationFont);
		
		//adds key events for when keys are pressed
		theScene.addEventFilter(KeyEvent.KEY_PRESSED,  keyEvent -> {
			int digit = -1; //sets key pressed to corresponding digit
			if ((keyEvent.getCode()==KeyCode.DIGIT1) || (keyEvent.getCode()==KeyCode.NUMPAD1)) {
				digit = 1;
			} else if ((keyEvent.getCode()==KeyCode.DIGIT2) || (keyEvent.getCode()==KeyCode.NUMPAD2)){
				digit = 2;
			} else if ((keyEvent.getCode()==KeyCode.DIGIT3)  || (keyEvent.getCode()==KeyCode.NUMPAD3)){
				digit = 3;
			} else if ((keyEvent.getCode()==KeyCode.DIGIT4)  || (keyEvent.getCode()==KeyCode.NUMPAD4)){
				digit = 4;
			} else if ((keyEvent.getCode()==KeyCode.DIGIT5) || (keyEvent.getCode()==KeyCode.NUMPAD5)) {
				digit = 5;
			} else if ((keyEvent.getCode()==KeyCode.DIGIT6) || (keyEvent.getCode()==KeyCode.NUMPAD6)){
				digit = 6;
			} else if ((keyEvent.getCode()==KeyCode.DIGIT7) || (keyEvent.getCode()==KeyCode.NUMPAD7)){
				digit = 7;
			} else if ((keyEvent.getCode()==KeyCode.DIGIT8)  || (keyEvent.getCode()==KeyCode.NUMPAD8)){
				digit = 8;
			} else if ((keyEvent.getCode()==KeyCode.DIGIT9)  || (keyEvent.getCode()==KeyCode.NUMPAD9)){
				digit = 9;
			} else if ((keyEvent.getCode()==KeyCode.DIGIT0)  || (keyEvent.getCode()==KeyCode.NUMPAD0)){
				digit = 0;
			} else if (keyEvent.getCode()==KeyCode.BACK_SPACE) {
				//if backspace is pressed, the current answer is deleted
				haveFirstDigit = false;
				currentAns = -1;
			} else if (keyEvent.getCode()==KeyCode.SPACE) {
				if (state == INTRO) {
					//if the state is into and space bar is pressed, the game starts and points become 0
					state = GAME;
					points = 0;
					
					//makes first rockets
					for (int i = 0; i < rocketNum; i++) {
						makeNewRocket(i);
					}
				}
			}
			
			if (digit >= 0) {
				if (haveFirstDigit == false && state == GAME) {
					//first digit is inputed
					currentAns = digit;
					haveFirstDigit = true;
				} else {
					//second digit is inputed
					currentAns = currentAns*10 + digit;
					haveFirstDigit = false;
				}
			
			boolean gotOneRight = false;
			
			//if they got any rocket right (checks through them all)
			for (int i = 0; i<rocketNum; i++) {
				if (currentAns == product[i]) {
					gotOneRight = true;
					haveFirstDigit = false;
					explodeRocket(i);
				}
			}
			
			//shows X if no answers are right
			if (gotOneRight == false && currentAns > 9) {
				wrongTime = System.nanoTime();
				wrongSound.play();
			}
			
			}
		
		});
		
		int[] starX = new int[200]; //star's x position
		int[] starY = new int[200]; //star's y position
		int[] starSize = new int[200]; //size of stars
		
		int star = 0;
		//sets star's x position, y position and size
		do {
			starX[star] = (int) (1400*Math.random());
			starY[star] = (int) (700*Math.random());
			starSize[star] = (int) (3*Math.random() + 1);
			star++;
		} while (star < starX.length);
		
		
		new AnimationTimer() {
			
			public void handle(long now) {
				
				//clears canvas and resets for every second
				gc.clearRect(0, 0, 1400, 700);
				gc.setFill(Color.BLACK);
				gc.fillRect(0, 0, 1400, 700);
				
				//draws stars moving from right to left
				for (int i = 0; i < starX.length; i++) {
					double drawX = 1400-(starX[i] + (starSpeed*(now-startTime) / 1.0e9)) % 1400;
					gc.setFill(Color.WHITE);
					gc.fillRect(drawX, starY[i], starSize[i], starSize[i]);
				}
				
				//sets point counter
				gc.setFill(Color.YELLOW);
				gc.setFont(pointsFont);
				gc.fillText("Points: " + points, 1200, 55);
				
				if (state == INTRO) { //when state is intro
					//gives instructions on game
					gc.setFill(Color.WHITE);
					gc.setFont(answerFont);
					gc.fillText("Welcome to Math Rockets!", 425, 300);
					gc.setFill(Color.WHITE);
					gc.setFont(introFont);
					gc.fillText("Answer as many questions as you can before the rockets get to the other side.", 407, 350);
					gc.fillText("Press the space bar to start the game.", 549, 375);
					
				} else if (state == GAME) { //when state is game
					
					//shows current answer
					if (currentAns>=0) {
							gc.setFill(Color.WHITE);
							gc.setFont(answerFont);
							gc.fillText("" + currentAns, 675, 75);
					}
					
					for (int i = 0; i<rocketNum; i++) {
						if (rocketIsExploding[i] == false) { //if rocket is not exploding
							
							//draws rocket
							double timeFromLaunch = (now - rocketLaunchTime[i]) / 1.0e9;
							rocketX[i] = rocketSpeed[i] * timeFromLaunch - rocketLength;
							gc.drawImage(rocket, rocketX[i], rocketY[i], rocketLength, rocketHeight);
							
							//draws equation on rocket
							gc.setFill(Color.BLACK);
							gc.setFont(equationFont);
							gc.fillText(equation[i], rocketX[i]+70, rocketY[i]+55);
							
							//if rocket gets to the end, change state to big explosion
							if(rocketX[i] >= 1400-rocketLength) {
								state = BIG_EXPLOSION;
								stateStartTime = now;
								bigExplosionSound.play();
							}
							
						} else { //if rocket is exploding
							double explosionTime = (now - explosionStartTime[i]) / 1.0e9;
							
							//draws explosion
							int index = (int)(explosionTime / explosionFrameDuration);
							if (index < explosion.length) {
								gc.drawImage(explosion[index], rocketX[i]-350, rocketY[i]-300);
							} else {
								makeNewRocket(i);
							}
						}
						
						//draws X
						if ((now - wrongTime) / 1.0e9 < 1) {
							gc.drawImage(wrong, 575, 225);
						}
					}
					
				} else if (state == BIG_EXPLOSION) { //when state is big explosion
					double explosionTime = (now - stateStartTime) / 1.0e9;
					int index = (int) (explosionTime/explosionFrameDuration);
					currentAns = -1;
					
					if (index < explosion.length) { //draws big explosion
						gc.drawImage(explosion[index], -1280, -1150, 4000, 3000);
					} else { //once explosion is done, changes state to game over
						state = GAME_OVER;
						stateStartTime = now;
					}
				} else if (state == GAME_OVER) { //when state is game over
					//prints Game Over!
					gc.setFill(Color.WHITE);
					gc.setFont(answerFont);
					gc.fillText("Game Over!", 565, 330);
					
					//after 3 seconds, changes state to intro
					if ((now-stateStartTime) / 1.0e9 > 3) {
						state = INTRO;
					}
				}
				
				
			}
				
		}.start();
	
		theStage.show();
	}

}
