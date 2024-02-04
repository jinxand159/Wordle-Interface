import java.io.FileInputStream;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Wordle1 extends Application {
	private static StackPane rootPrincipal;
	private static GridPane root;
	private final int sizeBox = 40;
	private Label answerBoxes[][];
	
	private int tries = 0;
	private int pos = 0;
	
	private final int clave = 22;
	private Dictionary dictionary = new Dictionary("C:\\Users\\HP\\eclipse-workspace\\First Project\\src\\words.txt");
	
	private Label mensajeErroneo = new Label();
	private String misteriusWord;
	
	private Button buttons[] = {
            new Button("q"), new Button("w"), new Button("e"), new Button("r"),
            new Button("t"), new Button("y"), new Button("u"), new Button("i"),
            new Button("o"), new Button("p"), new Button("a"), new Button("s"),
            new Button("d"), new Button("f"), new Button("g"), new Button("h"),
            new Button("j"), new Button("k"), new Button("l"), new Button("<--"),
            new Button("z"), new Button("x"), new Button("c"), new Button("v"), 
            new Button("b"), new Button("n"), new Button("m"), new Button("Submit")
    };
	
	public void start(Stage stage) {
		try {
			root = new GridPane();
			rootPrincipal = new StackPane();
			rootPrincipal.getChildren().add(root);
			answerBoxes = new Label[6][5];
			Scene scene = new Scene(rootPrincipal, 400, 620);
			setKeyBoard(scene);
			scene.getStylesheets().add(getClass().getResource("aplication.css").toExternalForm());
			stage.setTitle("Wordle");
			Image icon = new Image(new FileInputStream("wordle.png"));
			stage.getIcons().add(icon);
			
			stage.setScene(scene);
			stage.show();
			
			misteriusWord = codificarPalabra("MONTE");
			mensajeAviso();
			load();

		} catch (Exception e) {
			System.err.println("Theres some problemmmm\n");
		}

	}

	public static void main(String[] args) {
		launch();
	}
	public void load() {
		root.setVgap(20);
		HBox introBox[] = new HBox[6];
		
		for(int i = 0 ; i<6 ; i++) {
			introBox[i] = new HBox();
			for(int j = 0 ; j<5 ; j++) {
				introBox[i].setSpacing(20);
				answerBoxes[i][j] = new Label();
				answerBoxes[i][j].setText("_");
				answerBoxes[i][j].setId("square-label");
				answerBoxes[i][j].setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, sizeBox));
				introBox[i].getChildren().add(answerBoxes[i][j]);
			}
			root.addRow(i, introBox[i]);
		}
		
		GridPane grid = new GridPane();
		grid.setHgap(6);

        // Create buttons for each letter in the specified sequence
        
        for(Button bot : buttons) {
        	if(bot.getText().compareTo("<--")==0) {
        		bot.setOnAction(e -> deleteValue());
        	} else if(bot.getText().compareTo("Submit")==0) {
        		bot.setOnAction(e -> verification());
        	}
        	else {
        		bot.setOnAction(e -> updateValue(bot.getText()));
        	}
        }
        // Add buttons to the grid
        int col = 0;
        int row = 0;
        for (Button button : buttons) {
            grid.add(button, col, row);
            col++;
            if (col == 10) {
                col = 0;
                row++;
            }
        }
		root.addRow(7, grid);
	}
	
	private void updateValue(String value) {
		if(pos<5) {
			answerBoxes[tries][pos].setText(value.toUpperCase());
			pos++;
		}
	}
	
	private void deleteValue() {
		if(pos>0) {
			pos--;
			answerBoxes[tries][pos].setText("_");
		}
	}
	
	
	private void verification() {
		if(pos<5) {
			mensajeErroneo.setText("Rellene TODOS los huecos.\n clickear para quitarlo");
			mensajeErroneo.setVisible(true);
		}
		else {
			String word = "";
			for(int i=0 ; i<5 ; i++) {
				word+=answerBoxes[tries][i].getText();
			}
			if(!dictionary.inDictionary(codificarPalabra(word))) {
				for(int i = 0 ; i<5 ; i++) {
					answerBoxes[tries][i].setText("_");
				}
				pos = 0;
				mensajeErroneo.setText("La palabra tiene que EXISTIR\n clickear para quitarlo");
				mensajeErroneo.setVisible(true);
			}
			else {
				int esta=-1;
			    int j=0;
			    int correctos[] = {0, 0, 0, 0, 0};
			    int comprobado[] = {0, 0, 0, 0, 0};
			    for(int v=0 ; v<5 ; v++){
			        if(answerBoxes[tries][v].getText().compareToIgnoreCase(""+codificarLetra(misteriusWord.charAt(v), 2)) == 0){
			            correctos[v] = 1;
			            comprobado[v] = 1;
			        }
			    }

			    for(int v = 0 ; v<5 ; v++){
			        esta = -1;
			        j = 0;
			        if(correctos[v]!=1){

			            while(esta<0 && j<5){
			                    if(v!=j && answerBoxes[tries][v].getText().compareToIgnoreCase(""+codificarLetra(misteriusWord.charAt(j), 2)) == 0 && comprobado[j]==0){
			                        esta=1;
			                        correctos[v] = -1;
			                        comprobado[j] = 1;
			                    }
			                    else{
			                        j++;
			                    }
			            }
			            if(esta <0){
			                correctos[v] = 0;
			            }
			        }
			    }
			    int res = 0;
			    for(int v = 0 ; v<5 ; v++) {
			    	if(correctos[v] == 1) {
			    		answerBoxes[tries][v].setId("good");
			    		res++;
			    	}
			    	else if(correctos[v] == -1) {
			    		answerBoxes[tries][v].setId("wrong-placement");	
			    	}
			    	else {
			    		answerBoxes[tries][v].setId("wrong");
			    	}
			    }
			    
				
			    //Colocar la primera posicion de la siguiente fila
				tries++;
				pos = 0;
				
				word = word.toLowerCase();
				System.out.println("Your answer is: "+word);
				if(res == 5 || tries>=6) {
					if(res == 5) {
						fin(1);
					}
					else {
						fin(2);
					}
			    }
			}
		}
	}
	
	private void setKeyBoard(Scene scene) {
		root.requestFocus();
		scene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			public void handle(KeyEvent key) {
				String keyText = key.getText();
				if(key.getCode() == KeyCode.BACK_SPACE || !keyText.isEmpty()) {
					if(key.getCode() == KeyCode.BACK_SPACE) {
						deleteValue();
					}
					else if(key.getCode() == KeyCode.ENTER) {
						verification();
					}
					else {
						updateValue(keyText);
					}
				}
			}
			
		});
	}
	
	private void fin(int resultado) {
		if(resultado == 1) {
			// Create a circle representing the firecracker
	        Circle firecracker = new Circle(10, Color.RED);
	        
	        rootPrincipal.getChildren().add(firecracker);
	        
	        // Create a scale transition for the "explosion" effect
	        ScaleTransition explosion = new ScaleTransition(Duration.seconds(0.5), firecracker);
	        explosion.setFromX(1);
	        explosion.setFromY(1);
	        explosion.setToX(50);
	        explosion.setToY(50);
	        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), firecracker);
	        fadeOut.setToValue(0);

	        // Create a sequential transition to combine the explosion and fade-out transitions
	        SequentialTransition firecrackerAnimation = new SequentialTransition(explosion, fadeOut);

	     // Reset the firecracker's scale and opacity
	        firecracker.setScaleX(1);
	        firecracker.setScaleY(1);
	        firecracker.setOpacity(1);

	        // Play the firecracker animation
	        firecrackerAnimation.play();
	        
		}
		else {
			mensajeErroneo.setText("La palabra misteriosa era: "+descodificarPalabra(misteriusWord));
			mensajeErroneo.setVisible(true);
		}
	}
	
	private char codificarLetra(char letra, int mode) {
		
		int ansii = letra-65;
		//Mode 1 => codify
		if(mode == 1) {
			ansii = (ansii+clave)%26;
		}
		else {	//Decodify
			ansii = (ansii-clave)%26;
		}
		if(ansii<0) {
			ansii+=26;
		}
		return (char)(ansii+65);
	}
	
	private String codificarPalabra(String word) {
		String res = "";
		for(int i=0 ; i<5 ; i++) {
			res+= codificarLetra(word.charAt(i), 1);
		}
		
		return res;
	}
	private String descodificarPalabra(String word) {
		String res = "";
		for(int i=0 ; i<5 ; i++) {
			res+= codificarLetra(word.charAt(i), 2);
		}
		
		return res;
	}
	
	private void mensajeAviso() {
		rootPrincipal.getChildren().add(mensajeErroneo);
		mensajeErroneo.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
		mensajeErroneo.setId("rectangle-label");
		mensajeErroneo.setVisible(false);
		mensajeErroneo.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				mensajeErroneo.setVisible(false);
			}
		}
		);
	}
}
