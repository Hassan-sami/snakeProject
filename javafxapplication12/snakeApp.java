package javafxapplication12;


import java.io.File;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;


public class snakeApp extends Application  implements Snake , Food{
    
    private static final int WIDTH = 800;
    private static final int HEIGHT = WIDTH;
    private static final int ROWS =20;
    private static final int COLUMNS = ROWS;
    private static final int CELL_SIZE = WIDTH / ROWS;   


    private static final int RIGHT = 0;
    private static final int LEFT = 1;
    private static final int UP = 2;
    private static final int DOWN = 3;
    private static final int OPENED = 0;
    private static final int CLOSED = 1;
    private static final int BARRIERS = 2;
    
    private static final String[] IMAGES = new String[]{"ic_orange.png", "ic_apple.png", "ic_cherry.png",
            "ic_berry.png", "ic_coconut_.png", "ic_peach.png", "ic_watermelon.png", "ic_orange.png",
            "ic_pomegranate.png"};
                                            
    private Media eatingSound = new Media("file:/D:/visual/JavaFXApplication12/src/javafxapplication12/Image/eatingsound.wav.mp3");
    private int deltaX = 1;
    private int deltaY=0;
    private Timeline time;
    Timeline barrierTime;
    private int gameType=0;
    private GraphicsContext gc;
    private List<Position> snakeBody = new ArrayList();
    private List<Position> previousSnakeBody = new ArrayList();
    private Position food;
    private int index= 0;
    private boolean isRunning = false;
    private boolean isSecondaryMouseClicked = false;
    private boolean gameOver= false;
    private int duration= 200;
    private boolean keyPressed = false;
    private int currentDirection = RIGHT;
    private boolean isFoodEaten = false;
    private int score = 0;
   

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Snake");
        primaryStage.getIcons().add(new Image("D:\\visual\\JavaFXApplication12\\src\\javafxapplication12\\Image\\snake.jfif"));
        GridPane pane = new GridPane();
        Button btnSpeed = new Button("Speed");
        Button btnOpenedBox = new Button("Opened");
        Button btnClosedBox = new Button("Closed");
        Button btnbarriersBox = new Button("Barriers");
        btnSpeed.setCursor(Cursor.HAND);
        btnSpeed.setStyle("-fx-border-radius : 20;-fx-background-radius: 20;-fx-font: bold 32px adobe;");       
        
        
        
        btnOpenedBox.setCursor(Cursor.HAND);
        btnOpenedBox.setStyle("-fx-border-radius : 20;-fx-background-radius: 20;-fx-font: bold 29px adobe"); 

        
        btnClosedBox.setCursor(Cursor.HAND);
        btnClosedBox.setStyle("-fx-border-radius : 20;-fx-background-radius: 20;-fx-font: bold 32px adobe");

        
        btnbarriersBox.setCursor(Cursor.HAND);
        btnbarriersBox.setStyle("-fx-border-radius : 20;-fx-background-radius: 20;-fx-font: bold 30px adobe");

        
        
        VBox v = new VBox();
        v.getChildren().addAll(btnOpenedBox,btnClosedBox,btnbarriersBox,btnSpeed);
        v.setAlignment(Pos.CENTER);
        v.setSpacing(10);
        pane.getChildren().add(v);
        pane.setAlignment(Pos.CENTER);
        Scene openScene = new Scene(pane, 600,600);
        primaryStage.setScene(openScene);
        primaryStage.show();
        btnOpenedBox.setOnAction(e->{
            primaryStage.close();
            gameType = OPENED;
            gameOver= false;
            score = 0;
            snakeBody.clear();
            currentDirection = RIGHT;
            deltaX=1;deltaY=0;
            startGame(primaryStage);
        });


        btnClosedBox.setOnAction(e->{
            primaryStage.close();
            gameType = CLOSED;
            gameOver = false;
            score = 0;

            snakeBody.clear();
            currentDirection = RIGHT;
            deltaX=1;deltaY=0;
            startGame(primaryStage);

        
        });
        
        btnbarriersBox.setOnAction(e->{
            primaryStage.close();
            gameType = BARRIERS; 
            score = 0;
            gameOver = false;
            isSecondaryMouseClicked = false;
            snakeBody.clear();
            currentDirection = RIGHT;
            deltaX=1;deltaY=0;
            Barriers.getBarriers().clear();
//             Barriers  barriers= new Barriers();
//             barriers.creaetBarriersWithMouse(openScene, CELL_SIZE);
            startGame(primaryStage);
        
        
        
        });
        openScene.setOnKeyPressed(e->{
            if(e.getCode() == KeyCode.ESCAPE){
                primaryStage.close();
                
            }
        
        
        });
        
        
        btnSpeed.setOnAction(e->{
            Stage s = new Stage();
            Slider slider = new Slider();
            slider.setMin(100);
            slider.setMax(250);
            slider.setValue(duration);
            slider.setShowTickLabels(true);
            slider.setShowTickMarks(true);
            slider.setMajorTickUnit(50);
            slider.setMinorTickCount(5);
            slider.setBlockIncrement(50);
            slider.setPrefSize(200, 50);
            GridPane grid = new GridPane();
            grid.getChildren().add(slider);
            grid.setAlignment(Pos.CENTER);
            
            Scene scene = new Scene(grid,200,200);
            s.setScene(scene);
            s.setResizable(false);
            s.show(); 
            //duration = (int)slider.getValue();
            scene.setOnKeyPressed(key->{
                if(key.getCode() == KeyCode.ENTER){
                    s.close();
                }
            });
            slider.setOnMouseReleased(eSlider->{
               
                duration = (int)slider.getValue();
                slider.setValue(duration);
            
            });
            slider.setOnKeyPressed(eSlider->{
                if(eSlider.getCode()== KeyCode.LEFT){
                    duration = (int)slider.getValue();
                    slider.setValue(duration);
                }
                if(eSlider.getCode()== KeyCode.RIGHT){
                    duration = (int)slider.getValue();
                    slider.setValue(duration);
                
                }
                
            scene.setOnKeyPressed(key->{
                if(key.getCode() == KeyCode.ESCAPE){
                    s.close();

                }


            });
            
            
            });
            

            
            
        });
      
    }
    

   
    public static void main(String[] args) {
        
        launch(args);
    }
    
    private void startGame(Stage primaryStage){
        Stage s = new Stage();
        s.setTitle("Snake");
        s.getIcons().add(new Image("D:\\visual\\JavaFXApplication12\\src\\javafxapplication12\\Image\\snake.jfif"));
      


        Group root = new Group();
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        
        root.getChildren().add(canvas);
        
        snakeBody.add(new Position(0 , 0));
        gc = canvas.getGraphicsContext2D();

        Scene scene = new Scene(root);

        
        
        
        food = generateFood();

        
        scene.setOnKeyPressed(key ->{
                Position head = snakeBody.get(0);
                KeyCode code = key.getCode();
                
            if (!(head.getX() < 0 || head.getX() >  WIDTH -CELL_SIZE || head.getY() < 0 || head.getY() > HEIGHT-CELL_SIZE ||! keyPressed  )){
                
                if (code == KeyCode.RIGHT || code == KeyCode.D) {
                    if (currentDirection != LEFT) {
         
                            currentDirection = RIGHT; deltaX = 1; deltaY = 0;
                            
                        
                    }
                } else if (code == KeyCode.LEFT || code == KeyCode.A) {
                    if (currentDirection != RIGHT) {
                        
                            currentDirection = LEFT;  deltaX = -1; deltaY = 0;
                        
                    }
                } else if (code == KeyCode.UP || code == KeyCode.W) {
                    if (currentDirection != DOWN ) {
                        
                            currentDirection = UP; deltaX = 0; deltaY = -1;
                        
                    }
                } else if (code == KeyCode.DOWN || code == KeyCode.S) {
                    if (currentDirection != UP) {
                        
                            currentDirection = DOWN;  deltaX = 0; deltaY = 1;
                                       
                    }
                }
                keyPressed = false;
            }
                if (code == KeyCode.ENTER ){
                    if(gameOver){
                        gameOver= false;
                        score = 0;
                        snakeBody.clear();
                        if(gameType == BARRIERS){
                            
                            
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Dialog");
                            alert.setHeaderText("Do you want remove the barriers?");

                            ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
                            ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
                            alert.getButtonTypes().setAll(yesButton, noButton);

                            Optional<ButtonType> result = alert.showAndWait();
                            if (result.isPresent() && result.get() == yesButton) {
                                Barriers.getBarriers().clear();
                            } 
                            
                        }
                        snakeBody.add(new Position(0 , 0));
                        currentDirection = RIGHT; deltaX = 1; deltaY = 0;
                        food = generateFood();
                        if(gameType == BARRIERS){
                                Barriers.creaetBarriersWithMouse(scene, CELL_SIZE,food);

                        }
                        index = (int)(Math.random() * IMAGES.length);
                        time.play();
                        
                    }
                }
                if(code == KeyCode.SPACE){
                    if(gameType == BARRIERS){
                        if(isRunning)
                             time.pause();
                    }
                        else{
                            time.pause();
                        }
                }
                if(code == KeyCode.ESCAPE){
                   
                    time.pause();
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Exit");
                    alert.setHeaderText("Do you want  to exit");

                    Optional<ButtonType> result = alert.showAndWait();
                    
                    
                    if (result.isPresent() && result.get() == ButtonType.OK){
                        isSecondaryMouseClicked = false;
                        if(gameType == BARRIERS)
                           barrierTime.stop();
                        s.close();
                        isRunning = false;
                        primaryStage.show();
                    } else {
                        if(gameOver == false &&(gameType == OPENED || gameType == CLOSED) ){
                            time.play();
                        }
                        if(gameType == BARRIERS && isSecondaryMouseClicked == true && gameOver == false){
                        
                            time.play();
                        }
                    }
                    
                }
            
            
        });
        scene.setOnKeyReleased(event->{
            if(event.getCode() == KeyCode.SPACE){
                if(gameType == BARRIERS){
                    if(gameOver == false && isRunning){
                        time.play();
                    }
                }else{
                    if(gameOver == false){
                        time.play();
                    }
                }
            }
        });

        
        
        s.setScene(scene);
        s.setResizable(false);
        s.show();
        
        time = new Timeline(new KeyFrame(Duration.millis(duration), event -> {  run(deltaX,deltaY,scene);}));
        if(gameType == BARRIERS){
                    barrierTime = new Timeline(new KeyFrame(Duration.millis(200), e->{
                    Barriers.creaetBarriersWithMouse(scene, CELL_SIZE,food);
                    DrawBackground(gc,Barriers.getBarriers());
                    Barriers.removeBarriersWithMouse(scene,CELL_SIZE);// game Does not start
            
            }));
            
             barrierTime.setCycleCount(Animation.INDEFINITE);
            barrierTime.play();
             scene.setOnMouseClicked(e->{
                 if(e.getButton() ==  MouseButton.SECONDARY){
                     if(!isRunning){
                        isSecondaryMouseClicked = true;
                        currentDirection = RIGHT;
                        deltaX=1;deltaY=0;
                        barrierTime.stop();
                        time.setCycleCount(Animation.INDEFINITE);
                        time.play();
                     }
                     isRunning = true;
                 }
             
             });
             
             
        }else{
        
            time.setCycleCount(Animation.INDEFINITE);
            time.play();
        }
    }

    private void DrawBackground(GraphicsContext gc, List<Position> positions) {
//    if(gameType == CLOSED){
//        for ( int i = 0; i < ROWS ; i++){
//            for(int j = 0 ; j< COLUMNS; j++){
//                if(i==0|| j == 0|| i == ROWS-1|| j == COLUMNS-1){
//                    gc.setFill(Color.BLACK);
//                    gc.fillRect(i* CELL_SIZE, j * CELL_SIZE , CELL_SIZE, CELL_SIZE);
//                    
//                }else{
//                    gc.setFill(Color.WHITE);
//                    gc.fillRect(i* CELL_SIZE, j * CELL_SIZE , CELL_SIZE, CELL_SIZE);
//                }
//               
//                
//            }
//        }
//    }
            if(gameType== OPENED ||gameType ==  CLOSED){
                gc.setFill(Color.BLACK);
                gc.fillRect(0, 0, WIDTH, HEIGHT);
                
            }
            
            if(gameType == BARRIERS){
                gc.setFill(Color.WHITE);
                gc.fillRect(0, 0, WIDTH, HEIGHT);
                for(int i = 0 ; i< positions.size();i++){
                    gc.setFill(Color.BLACK);
                    gc.fillRoundRect(positions.get(i).getX(), positions.get(i).getY(), CELL_SIZE, CELL_SIZE, 20, 20);
                
                }
            }
            
    }

    @Override
    public void drawSnake(GraphicsContext gc) {
        if(!gameOver){
             previousSnakeBody.clear();
            previousSnakeBody.addAll(snakeBody);
        }else{
            snakeBody.clear();
            snakeBody.addAll(previousSnakeBody);
        }
        gc.setFill(Color.RED);
        for (Position position : snakeBody) {
            if(snakeBody.indexOf(position) == 0){
                gc.fillRoundRect(position.getX(), position.getY(), CELL_SIZE, CELL_SIZE,CELL_SIZE ,CELL_SIZE );
            }
            else{
                
                 gc.fillRoundRect(position.getX(), position.getY(), CELL_SIZE, CELL_SIZE,CELL_SIZE/2,CELL_SIZE/2);               
            }
            
        }
       
       
        
        
    }
 



    private void run(int deltaX , int deltaY,Scene scene) {
      

            DrawBackground(gc,Barriers.getBarriers()); 
            
            moveSnake(deltaX,deltaY,scene);
            if(gameType == BARRIERS){
                Barriers.removeBarriersWithMouse(scene,CELL_SIZE);
            
            }
            
            if(gameType == OPENED || gameType == BARRIERS){
                appearFromTheSides(scene);
            }
            if (Collision(scene)) {
                try{
//                    File file = new File("D:/visual/JavaFXApplication12/src/javafxapplication12/Image/deathsound.mp3");
//                    String path = file.toURI().toString();
//                    System.out.println(path);
                    Media media = new Media("file:/D:/visual/JavaFXApplication12/src/javafxapplication12/Image/deathsound.mp3");
                    MediaPlayer player = new MediaPlayer(media);
                    player.play();
                }catch(Exception ex){
                    System.out.println(ex.getMessage());
                }
                stopGame();
                //return;
                
            }
            
            drawSnake(gc);
            drawFood(gc);
            drawScore();
            

        
        
    }

    @Override
    public void moveSnake(int deltaX, int deltaY, Scene scene) {
        Position head = snakeBody.get(0);
        Position newHead = new Position(head.getX() +deltaX * CELL_SIZE,
                head.getY() + deltaY * CELL_SIZE);

        snakeBody.add(0, newHead);

        if (!newHead.equals(food)) {
            isFoodEaten = false;
            snakeBody.remove(snakeBody.size() - 1);
            keyPressed = true;
        } else {
            isFoodEaten = true;
            score += 1;
            try{

//                Task<Void> task = new Task<Void>() {
//                    @Override
//                    protected Void call() throws Exception {
//                        Media media = new Media("file:/D:/visual/JavaFXApplication12/src/javafxapplication12/Image/eatingsound.wav.mp3");
//                        MediaPlayer player = new MediaPlayer(media);
//                        player.play();
//                        return null;
//                    }
//                };
//                new Thread(task).run();
                
                MediaPlayer player = new MediaPlayer(eatingSound);
                player.play();

            }catch(Exception ex){
                System.out.println(ex.getMessage());
            }      

            food = generateFood();
            if(gameType == BARRIERS){
                Barriers.creaetBarriersWithMouse(scene, CELL_SIZE,food);

            }
            keyPressed = true;
        }
    }

    @Override
    public void drawFood(GraphicsContext gc){ 
        String path = "D:\\visual\\JavaFXApplication12\\src\\javafxapplication12\\Image\\";
        
        if(isFoodEaten){
            index = (int)(Math.random() * IMAGES.length);
        }
        path += IMAGES[index];
        try{
            gc.drawImage(new Image(path), food.getX(), food.getY(), CELL_SIZE, CELL_SIZE);
        }catch(Exception ex){
            
            gc.setFill(Color.GREEN);
            gc.fillOval(food.getX(), food.getY(), CELL_SIZE, CELL_SIZE);
        }
    }

    @Override
    public Position generateFood() {
        int x =(int)( Math.random() * ROWS )* CELL_SIZE;
        int y = (int) ( Math.random() * COLUMNS) * CELL_SIZE;
        Position food = new Position(x,y);
        for (Position position : snakeBody) {
             if (position.equals(food)) {
                return generateFood();
             }
         }
        for (Position barrier : Barriers.getBarriers()) {
             if (barrier.equals(food)) {
                return generateFood();
             }
         }
         
         return new Position(x, y);
    }
    
    private boolean Collision(Scene scene) {
          Position head = snakeBody.get(0);
          if(gameType == CLOSED){
                if (head.getX() < 0 || head.getX() >=  WIDTH  || head.getY() < 0 || head.getY() >= HEIGHT ) {
//                    for(int i = 0 ; i< snakeBody.size();i++){
//                        snakeBody.get(i).setX(snakeBody.get(i).getX()-deltaX*CELL_SIZE);
//                        snakeBody.get(i).setY(snakeBody.get(i).getY()-deltaY*CELL_SIZE);
//                    }
                    return true; 
                }
          }
        for (int i = 1; i < snakeBody.size(); i++) {
            if (head.equals(snakeBody.get(i))) {
                
                return true; 
            }
        }
        if(gameType == BARRIERS){
            for(int i = 0; i < Barriers.getBarriers().size();i++){
                if(head.equals(Barriers.getBarriers().get(i))){
//                    for(int j = 0 ; j< snakeBody.size();j++){
//                        snakeBody.get(j).setX(snakeBody.get(j).getX()-deltaX*CELL_SIZE);
//                        snakeBody.get(j).setY(snakeBody.get(j).getY()-deltaY*CELL_SIZE);
//                    }
                    return true;
                }
            }
        }
        return false;
    }

    private void stopGame() {

        gameOver = true;
        time.stop();
        gc.setFill(Color.RED);
        gc.setFont(new Font("Digital-7", 70));
        gc.fillText("Game Over\n Press Enter \nto replay", WIDTH / 3.5, HEIGHT / 2);
    }
    private void drawScore() {
        gc.setFill(Color.BLUE);
        gc.setFont(new Font("Digital-7", 35));
        
        gc.fillText("Score: " + score, 625, 35);
    }
    
    private void appearFromTheSides(Scene scene){
            
            Position head = snakeBody.get(0);
            if (head.getX()< 0) {
                for(int i =0 ; i< snakeBody.size();i++){
                    Position part = snakeBody.get(i);
                    if(part.getX()< 0){
                        part.setX(WIDTH-CELL_SIZE+i*CELL_SIZE);
                        part.setY(part.getY());
                        if(snakeBody.get(0).equals(food)){
                            MediaPlayer player = new MediaPlayer(eatingSound);
                            player.play();
                            food = generateFood();
                            if(gameType == BARRIERS){
                                Barriers.creaetBarriersWithMouse(scene, CELL_SIZE,food);

                            }
                            Position element = new Position(WIDTH-CELL_SIZE+snakeBody.size()*CELL_SIZE, part.getY());
                            snakeBody.add(element);
                            score += 1;
                        }
                    }
                    //moveSnake(0,-1);
                }
                
            }
            else if (head.getX()>= WIDTH) {
                for(int i =0 ; i< snakeBody.size();i++){
                    Position part = snakeBody.get(i);
                    if(part.getX()>= WIDTH){
                        part.setX( - i* CELL_SIZE);
                        part.setY(part.getY());
                        if(snakeBody.get(0).equals(food)){
                            MediaPlayer player = new MediaPlayer(eatingSound);
                            player.play();
                            food = generateFood();
                            if(gameType == BARRIERS){
                                Barriers.creaetBarriersWithMouse(scene, CELL_SIZE,food);

                            }
                            Position element = new Position(- snakeBody.size()* CELL_SIZE, part.getY());
                            snakeBody.add(element);
                            score += 1;
                        }                        
                    }
                        //moveSnake(0,1);
                }
            }
            else if (head.getY()< 0) {
                for(int i =0 ; i< snakeBody.size();i++){
                    Position part = snakeBody.get(i);
                    if(part.getY()< 0){
                        part.setX(part.getX());
                        part.setY(HEIGHT-CELL_SIZE+i*CELL_SIZE);
                        if(snakeBody.get(0).equals(food)){
                            MediaPlayer player = new MediaPlayer(eatingSound);
                            player.play();
                            food = generateFood();
                            if(gameType == BARRIERS){
                                Barriers.creaetBarriersWithMouse(scene, CELL_SIZE,food);

                            }
                            Position element = new Position(part.getX(), HEIGHT-CELL_SIZE+snakeBody.size()*CELL_SIZE);
                            snakeBody.add(element);
                            score += 1;
                        }                   
                    }
                    //moveSnake(0,-1);
                }
            }
            else if (head.getY()>= HEIGHT) {
                for(int i =0 ; i< snakeBody.size();i++){
                    Position part = snakeBody.get(i);
                    if(part.getY()>= HEIGHT){
                        part.setX(part.getX());
                        part.setY( - i* CELL_SIZE);
                        if(snakeBody.get(0).equals(food)){
                            MediaPlayer player = new MediaPlayer(eatingSound);
                            player.play();
                            food = generateFood();
                            if(gameType == BARRIERS){
                                Barriers.creaetBarriersWithMouse(scene, CELL_SIZE,food);

                            }
                            Position element = new Position(part.getX(), - snakeBody.size() * CELL_SIZE);
                            snakeBody.add(element);
                            score += 1;

                        }
                    }
                    //moveSnake(0,1);
                }
            }
        
    
    }



  
} 