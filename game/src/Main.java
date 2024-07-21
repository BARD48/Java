import javafx.geometry.Pos;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.util.Duration;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.Random;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
   /**
 * A method that randomly places lavas, soils, valuable items, and obstacles within a given scene width and height.
 *
 * @param lavaImage         ArrayList containing images representing lavas
 * @param soilImages        ArrayList containing images representing soils
 * @param valuableImages    ArrayList containing images representing valuable items
 * @param obstacleImage      ArrayList containing images representing obstacle iamge
 * @param topSoilImage     ArrayList containing images representing topSoilImages
 * @param money  It is start value of money
 * @param haul It is start value of weight
 * @param obstacleImage     ArrayList containing images representing obstacles
 */

public class Main extends Application {
    private static double startFuel=400.999;
    private static ArrayList<Image> valuableImages;
    private static ArrayList<Image> obstacleImage;
    private  static ArrayList<Image> soilImages;
    private static ArrayList<Image> lavaImage;
    private static ArrayList<Value> valuableInformation;
    private static ArrayList<Image> topSoilimages;
    private  static int money;
    private  static int haul;
    @Override
    public void start(Stage stage) throws Exception {
        Main app=new Main();
        Random random = new Random();
        Group root=new Group();
        Scene scene=new Scene(root,1000,1000,Color.DARKBLUE);
        stage.setScene(scene);
        stage.setTitle("HU-Load");
        stage.setResizable(true);
        //It add image to Arraylist
        ArrayList<String> topSoilPath=new ArrayList<>();
        ArrayList<Image> topSoilimages = new ArrayList<>();
        ArrayList<String>valuablePath=new ArrayList<>();
        ArrayList<Image> valuableImages=new ArrayList<>();
        ArrayList<String> soilPath=new ArrayList<>();
        ArrayList<Image> soilImages=new ArrayList<>();
        ArrayList<String> lavaPath=new ArrayList<>();
        ArrayList<Image> lavaImage=new ArrayList<>();
        ArrayList<Image> obstacleImage=new ArrayList<>();
        ArrayList<Value> valuableInformation=new ArrayList<>();
        topSoilPath.add("assets\\underground\\top_01.png");
        topSoilPath.add("assets\\underground\\top_02.png");
        valuablePath.add("assets\\underground\\valuable_amazonite.png");
        valuablePath.add("assets\\underground\\valuable_bronzium.png");
        valuablePath.add("assets\\underground\\valuable_diamond.png");
        valuablePath.add("assets\\underground\\valuable_einsteinium.png");
        valuablePath.add("assets\\underground\\valuable_emerald.png");
        valuablePath.add("assets\\underground\\valuable_goldium.png");
        valuablePath.add("assets\\underground\\valuable_ironium.png");
        valuablePath.add("assets\\underground\\valuable_platinum.png");
        valuablePath.add("assets\\underground\\valuable_ruby.png");
        valuablePath.add("assets\\underground\\valuable_silverium.png");
        soilPath.add("assets\\underground\\soil_01.png");
        soilPath.add("assets\\underground\\soil_02.png");
        soilPath.add("assets\\underground\\soil_03.png");
        soilPath.add("assets\\underground\\soil_04.png");
        soilPath.add("assets\\underground\\soil_05.png");
        lavaPath.add("assets\\underground\\lava_01.png");
        lavaPath.add("assets\\underground\\lava_02.png");
        lavaPath.add("assets\\underground\\lava_03.png");
        double sceneWidth= scene.getWidth();
        double sceneHeight = scene.getHeight();
        for (String path : topSoilPath) {
            Image image = new Image(path);
            topSoilimages.add(image);
        }
        //It assign value for valuable items
        for(String path: valuablePath){
            String[]parts=path.split("\\\\");
            String name=(parts[2].split("\\.")[0]).split("_")[1];
            int weight;
            int cost;
            switch (name.toLowerCase()) {
                case "bronzium":
                    weight = 10;
                    cost = 60;
                    break;
                case "silverium":
                    weight = 10;
                    cost = 100;
                    break;
                case "ironium":
                    weight = 10;
                    cost = 30;
                    break;
                case "platinum":
                    weight = 30;
                    cost = 750;
                    break;
                case "einsteinium":
                    weight = 40;
                    cost = 2000;
                    break;
                case "emerald":
                    weight = 60;
                    cost = 5000;
                    break;
                case "ruby":
                    weight = 80;
                    cost = 20000;
                    break;
                case "diamond":
                    weight = 100;
                    cost = 100000;
                    break;
                case "amazonite":
                    weight = 120;
                    cost = 500000;
                    break;
                case "goldium":
                    weight = 20;
                    cost = 250;
                    break;
                default:
                    weight = 0; // Default weight if name is not recognized
                    cost = 0;   // Default cost if name is not recognized
                    break;
            }
            Image image=new Image(path);
            Value value=new Value(name,weight,cost,path,image);
            valuableInformation.add(value);
            valuableImages.add(image);
        }
        
        for(String path:soilPath){
            Image image=new Image(path);
            soilImages.add(image);
        }
        for(String path:lavaPath){
            Image image=new Image(path);
            lavaImage.add(image);
        }
        //set obstacles and set the scene
        
        Image icon=new Image("assets\\drill\\drill_right\\01.png");
        
        Image leftObstacle=new Image("assets\\underground\\obstacle_01.png");
        Image downObstacle=new Image("assets\\underground\\obstacle_02.png");
        Image rightObstacle=new Image("assets\\underground\\obstacle_03.png");
        obstacleImage.add(leftObstacle);
        obstacleImage.add(rightObstacle);
        obstacleImage.add(downObstacle);
        double width=leftObstacle.getWidth();
        double height=leftObstacle.getHeight();
        app.setLava(lavaImage);
        app.setObstacle(obstacleImage);
        app.setTopSoil(topSoilimages);
        app.setValuable(valuableImages);
        app.setValueInfo(valuableInformation);
        app.setSoil(soilImages);
        stage.getIcons().add(icon);
        int randomTopIndex=random.nextInt(topSoilimages.size());
        int topSoilCount=0;
        //It is for top soil
        for(double i=0 ;i<sceneWidth;i+=50){
            ImageView randomTopView= new ImageView(topSoilimages.get(randomTopIndex));
            randomTopView.setX(i);
            randomTopView.setY(100);
            root.getChildren().add(randomTopView);
            topSoilCount++;
        }
        int obstacleCount=0;
        for (double i=0;i<sceneHeight-150;i+=50){
            ImageView leftObstacleView=new ImageView(leftObstacle);
            leftObstacleView.setX(0);
            leftObstacleView.setY(i+150);
            root.getChildren().add(leftObstacleView);
            ImageView rightObstacleView=new ImageView(rightObstacle);
            rightObstacleView.setX(sceneWidth-width);
            rightObstacleView.setY(i+150);
            root.getChildren().add(rightObstacleView);
            obstacleCount+=2;
        }
        for(double i=50 ;i<sceneWidth-50;i+=50){
            ImageView downObstacleView= new ImageView(downObstacle);
            downObstacleView.setX(i);
            downObstacleView.setY(sceneHeight-height);
            root.getChildren().add(downObstacleView);
            obstacleCount++;

        }
        //text for left side
        stage.setScene(scene);
        stage.setX(0);
        stage.setY(0);
        Text text=new Text();
        text.setText("fuel:"+startFuel);
        text.setFont(Font.font("Arial", 18));
        text.setFill(Color.RED);
        text.setX(10);
        text.setY(30);
        root.getChildren().add(text);
        Text text1=new Text();
        text1.setText("Haul:"+haul);
        text1.setFont(Font.font("Arial", 18));
        text1.setFill(Color.RED);
        text1.setX(10);
        text1.setY(50);
        root.getChildren().add(text1);
        Text text2=new Text();
        text2.setText("Money:"+money);
        text2.setFont(Font.font("Arial", 18));
        text2.setFill(Color.RED);
        text2.setX(10);
        text2.setY(70);
        root.getChildren().add(text2);
        tableMaker(lavaImage, soilImages, valuableImages, obstacleCount, topSoilCount, obstacleImage,sceneWidth,sceneHeight,root);
        Image image = new Image("assets\\drill\\drill_60.png");
        ImageView imageView = new ImageView(image);
        StackPane root1 = new StackPane();
        root1.setLayoutX(100);
        root1.setLayoutY(50);
        root.getChildren().add(root1); 
        root1.getChildren().add(imageView);//ıt is machine
        stage.setScene(scene); 
        stage.show();
        //timeline decrease
        double fuelDecreaseRate = 0.001; // fuel decrease rate
        long intervalMillis = 1; // time interval
        Timeline timeline = new Timeline();
        Timeline keyTimeline=new Timeline(new KeyFrame(Duration.millis(1700),event->{
            if(app.gravitycheck(root1, root, scene, stage)){
                app.gravity(root1, root, scene, stage);
            } 

        }));
        keyTimeline.setCycleCount(Animation.INDEFINITE); 
        keyTimeline.stop();

    
       
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(intervalMillis), event -> {
            //It checks fuel less than  0 and equal or not If it is zero gameover screen
    if (startFuel > 0) {
        text1.setText("Haul:"+haul);
        text2.setText("Money: "+money);
        startFuel -= fuelDecreaseRate;
        if (startFuel <=0 ) {
            startFuel=0;
            Label gameOverLabel=new Label("GAME OVER");
            gameOverLabel.setStyle("-fx-font-size: 48pt;");
            Label moneyLabel = new Label("Money Collected: " + money);
            moneyLabel.setStyle("-fx-font-size: 36pt;");
            Label haulLabel= new Label("Total haul is: "+haul);
            haulLabel.setStyle("-fx-font-size: 36pt;");
            VBox rootOver = new VBox(20);
            rootOver.setAlignment(Pos.CENTER);
            rootOver.setBackground(new Background(new BackgroundFill(Color.GREEN, null, null))); 
            rootOver.getChildren().addAll(gameOverLabel, moneyLabel,haulLabel); 
            Scene sceneOver = new Scene(rootOver, 1000, 1000);
            stage.setScene(sceneOver);
            stage.show();
            timeline.stop(); 
        }
        text.setText(String.format("fuel:" + "%.3f", startFuel));
    }
}));
timeline.setCycleCount(Timeline.INDEFINITE);
timeline.play();
scene.setOnKeyPressed(event -> {
    switch (event.getCode()) {

        case UP:
            if (keyTimeline.getStatus() == Animation.Status.STOPPED) {
                keyTimeline.play();
            }
            if(startFuel<fuelDecreaseRate*1000){
                startFuel=0;
                //ıt check fuel less than zero or not then gameover 
                Label gameOverLabel=new Label("GAME OVER");
                gameOverLabel.setStyle("-fx-font-size: 48pt;");
                Label moneyLabel = new Label("Money Collected: " + money);
                moneyLabel.setStyle("-fx-font-size: 36pt;");
                Label haulLabel= new Label("Total haul is: "+haul);
                haulLabel.setStyle("-fx-font-size: 36pt;");
                VBox rootOver = new VBox(20);
                rootOver.setAlignment(Pos.CENTER);
                rootOver.setBackground(new Background(new BackgroundFill(Color.GREEN, null, null))); 
                rootOver.getChildren().addAll(gameOverLabel, moneyLabel,haulLabel); 
                Scene sceneOver = new Scene(rootOver, 1000, 1000);
                stage.setScene(sceneOver);
                keyTimeline.stop();
                stage.show();
                break;
            }
            else{
                if(root1.getLayoutY()<=0){
                    break;
                }
                Controller.moveup(root1,scene,stage,root);
                startFuel-=fuelDecreaseRate*1000;
                break;
            }
        case DOWN:
            if (keyTimeline.getStatus() == Animation.Status.STOPPED) {
                keyTimeline.play();
            }
            if(startFuel<fuelDecreaseRate*1000){
                startFuel=0;
                //ıt check fuel less than zero or not then gameover 
               Label gameOverLabel=new Label("GAME OVER");
               gameOverLabel.setStyle("-fx-font-size: 48pt;");
               Label moneyLabel = new Label("Money Collected: " + money);
               moneyLabel.setStyle("-fx-font-size: 36pt;");
               Label haulLabel= new Label("Total haul is: "+haul);
               haulLabel.setStyle("-fx-font-size: 36pt;");
               VBox rootOver = new VBox(20);
               rootOver.setAlignment(Pos.CENTER);
               rootOver.setBackground(new Background(new BackgroundFill(Color.GREEN, null, null))); 
               rootOver.getChildren().addAll(gameOverLabel, moneyLabel,haulLabel); 
                Scene sceneOver = new Scene(rootOver, 1000, 1000);
                stage.setScene(sceneOver);
                keyTimeline.stop();
                stage.show();
                break;
            }
            else{Controller.movedown(root1,scene,stage,root,keyTimeline);
            startFuel-=fuelDecreaseRate*1000;
           
            break;}
        case LEFT:
            if (keyTimeline.getStatus() == Animation.Status.STOPPED) {
                keyTimeline.play();
            }
            if(startFuel<fuelDecreaseRate*1000){
                startFuel=0;
                Label gameOverLabel=new Label("GAME OVER");
                gameOverLabel.setStyle("-fx-font-size: 48pt;");
                Label moneyLabel = new Label("Money Collected: " + money);
                moneyLabel.setStyle("-fx-font-size: 36pt;");
                Label haulLabel= new Label("Total haul is: "+haul);
                haulLabel.setStyle("-fx-font-size: 36pt;");
                VBox rootOver = new VBox(20);
                rootOver.setAlignment(Pos.CENTER);
                rootOver.setBackground(new Background(new BackgroundFill(Color.GREEN, null, null))); 
                rootOver.getChildren().addAll(gameOverLabel, moneyLabel,haulLabel); 
                Scene sceneOver = new Scene(rootOver, 1000, 1000);
                stage.setScene(sceneOver);
                keyTimeline.stop();
                stage.show();
                break;
               
            }
            else{
                if(root1.getLayoutX()<=0){
                    break;
                }
            Controller.moveleft(root1,scene,stage,root,keyTimeline);
            startFuel-=fuelDecreaseRate*1000;
            break;}
        case RIGHT:
                if (keyTimeline.getStatus() == Animation.Status.STOPPED) {
                    keyTimeline.play();
                }
            if(startFuel<fuelDecreaseRate*1000){
                startFuel=0;
                Label gameOverLabel=new Label("GAME OVER");
                gameOverLabel.setStyle("-fx-font-size: 48pt;");
                Label moneyLabel = new Label("Money Collected: " + money);
                moneyLabel.setStyle("-fx-font-size: 36pt;");
                Label haulLabel= new Label("Total haul is: "+haul);
                haulLabel.setStyle("-fx-font-size: 36pt;");
                VBox rootOver = new VBox(20);
                rootOver.setAlignment(Pos.CENTER);
                rootOver.setBackground(new Background(new BackgroundFill(Color.GREEN, null, null))); 
                rootOver.getChildren().addAll(gameOverLabel, moneyLabel,haulLabel); 
                Scene sceneOver = new Scene(rootOver, 1000, 1000);
                stage.setScene(sceneOver);
                keyTimeline.stop();
                stage.show();
                break;
               
            }
            else{
                if(root1.getLayoutX()>900){
                    break;
                }
            Controller.moveright(root1,scene,stage,root,keyTimeline);
            startFuel-=fuelDecreaseRate*1000;
            break;}
        default:
            
            break;
    }
});
    
    }
    //Encapsulation there
    public void setSoil(ArrayList<Image> soilImages){
        this.soilImages=soilImages;
    }
    public ArrayList <Image> getSoil(){
        return soilImages;
    }
    public void setObstacle(ArrayList<Image> obstacleImage){
        this.obstacleImage=obstacleImage;
    }
    public ArrayList<Image> getObstacle(){
        return obstacleImage;
    }
    public void setValuable(ArrayList<Image> valuableImages){
        this.valuableImages=valuableImages;
    }
    public ArrayList<Image> getValuable(){
        return valuableImages;
    }
    public void setLava(ArrayList<Image> lavaImage){
        this.lavaImage=lavaImage;
    }
    public ArrayList<Image> getLava(){
        return lavaImage;
    }
    public void setValueInfo(ArrayList<Value> valuableInformation){
        this.valuableInformation=valuableInformation;
    }
    public ArrayList<Value> getValueInfo(){
        return valuableInformation;
    }
    public void setTopSoil(ArrayList<Image> topSoilimages){
        this.topSoilimages=topSoilimages;
    }
    public ArrayList<Image> getTopSoil(){
        return topSoilimages;
    }
    public void setMoney(int money){
        this.money=money;
    }
    public void setHaul(int haul){
        this.haul=haul;
    }
    public int getMoney(){
        return money;
    }
    public int getHaul(){
        return haul;
    }
    /**
 * Checks if there is an obstacle below the object represented by the provided StackPane.
 * An obstacle is considered to be present if any Rectangle or ImageView in the given Group
 * is positioned exactly below the current position of the StackPane object.
 *
 * @param root1 The StackPane representing the object for which gravity is being checked.
 * @param root The Group containing the obstacles in the scene.
 * @param scene The Scene object representing the current scene.
 * @param stage The Stage object representing the application window.
 * @return true if there is an obstacle below the object, false otherwise.
 */
    public boolean gravitycheck(StackPane root1,Group root,Scene scene,Stage stage){
        double currentY=root1.getLayoutY();
        double currentX=root1.getLayoutX();
        boolean solution=true;
        for(Node node: root.getChildren()){
            if (node instanceof Rectangle){
                Rectangle rectangle=(Rectangle) node;
                double xNode=rectangle.getX();
                double yNode=rectangle.getY();
                if (currentX==xNode&&currentY+50==yNode){
                    return true;
                }
            }
            else if(node instanceof ImageView){
                ImageView imageView=(ImageView) node;
                double xNode=imageView.getX();
                double yNode=imageView.getY();
                if(currentX==xNode&&currentY+50==yNode){
                return false;}
                
            } 
        }
        return true;
    }
    /**
 * Applies gravity to the object represented by the provided StackPane by incrementing its
 * layout Y-coordinate by 50 units. After applying gravity, updates the scene on the given stage.
 *
 * @param root1 The StackPane representing the object to which gravity is being applied.
 * @param root The Group containing the obstacles in the scene.
 * @param scene The Scene object representing the current scene.
 * @param stage The Stage object representing the application window.
 */
    public void gravity(StackPane root1, Group root, Scene scene, Stage stage){
        double currentY=root1.getLayoutY();
        root1.setLayoutY(currentY+50);
        stage.setScene(scene);
    }
    /**
 * A method that randomly places lavas, soils, valuable items, and obstacles within a given scene width and height.
 *
 * @param lavaImage         ArrayList containing images representing lavas
 * @param soilImages        ArrayList containing images representing soils
 * @param valuableImages    ArrayList containing images representing valuable items
 * @param obstacleCount     Number of obstacles to be placed on the scene
 * @param topSoilCount      Number of soils to be placed on the scene
 * @param obstacleImage     ArrayList containing images representing obstacles
 * @param sceneWidth        Width of the scene
 * @param sceneHeight       Height of the scene
 * @param root              JavaFX root node used to add elements onto the scene
 */
   
    public static void tableMaker(ArrayList<Image> lavaImage,ArrayList<Image> soilImages, ArrayList<Image> valuableImages,int obstacleCount,int topSoilCount,ArrayList<Image> obstacleImage,double sceneWidth,double sceneHeight,Group root){
        Main app=new Main();
        Random threshHold=new Random();
        for(double j=150;j<sceneHeight-50;j+=50){
            for(double i=50;i<sceneWidth-50;i+=50){
                double randomNumber = threshHold.nextDouble()*10;
                double lavaThreshHold=threshHold.nextDouble()*0.7;
                double soilThreshHold=threshHold.nextDouble()*9.20;
                double valuableThresHold=threshHold.nextDouble()*9.70;
                double obstacleThresHold=(threshHold.nextDouble()*9.80);

                if (randomNumber<lavaThreshHold){
                    app.viewMaker(lavaImage, i, j,root);
                }
                else if((lavaThreshHold<randomNumber)&&(randomNumber<soilThreshHold)){
                    app.viewMaker(soilImages, i, j, root);
                }
                else if((soilThreshHold<randomNumber)&&(randomNumber<valuableThresHold)){
                    app.viewMaker(valuableImages, i, j, root);

                }
                else if((valuableThresHold<randomNumber)&&(randomNumber<obstacleThresHold)){
                    app.viewMaker(obstacleImage, i, j, root);

                }
                else{
                    app.viewMaker(soilImages, i, j, root);
                }
            }
           
        }
    }
    /**
 * A method that adds a randomly selected image from a specified list of images at a given position on the scene.
 *
 * @param Images  ArrayList containing images to be added to the scene
 * @param xAxis   X coordinate
 * @param yAxis   Y coordinate
 * @param root    JavaFX root node used to add elements onto the scene
 */
    public void viewMaker(ArrayList<Image> Images,double xAxis,double yAxis,Group root){
        Random random = new Random();
        int imageSize=random.nextInt(Images.size());
        ImageView randomView= new ImageView(Images.get(imageSize));
        randomView.setX(xAxis);
        randomView.setY(yAxis);
        root.getChildren().add(randomView);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
/**
 * This class provides methods for moving an ImageView representing a drill in different directions within a scene.
 */

class Controller{
    private static ImageView imageView = new ImageView();
     /**
     * Moves the drill image upwards within the specified scene if the drill is not obstructed.
     *
     * @param root1  StackPane containing the drill image
     * @param scene  The scene in which the drill is moving
     * @param stage  The stage associated with the scene
     * @param root   JavaFX root node of the scene
     */
    public static void moveup( StackPane root1,Scene scene,Stage stage,Group root
        
    ){  Drill drill=new Drill();
        Image up=new Image("assets\\drill\\drill_27.png");
        imageView.setImage(up);
        if (!root1.getChildren().contains(imageView)) {
            root1.getChildren().clear();
            root1.getChildren().add(imageView);
        }
        
        double currentY=root1.getLayoutY();
        if(drill.checkDriilOrNot(root1, root, root1.getLayoutX(), currentY-50,true)==true){
            root1.setLayoutY(currentY-50);
            stage.setScene(scene); // Assuming 'scene' is already initialized
        }
        
    }
     /**
     * Moves the drill image downwards within the specified scene if the drill is not obstructed.
     *
     * @param root1       StackPane containing the drill image
     * @param scene       The scene in which the drill is moving
     * @param stage       The stage associated with the scene
     * @param root        JavaFX root node of the scene
     * @param keyTimeline Timeline used for animation
     */
    public static void movedown(StackPane root1,Scene scene,Stage stage,Group root,Timeline keyTimeline){
        Image down=new Image("assets\\drill\\drill_41.png");
        imageView.setImage(down);
        Drill drill=new Drill();
        if (!root1.getChildren().contains(imageView)) {
            root1.getChildren().clear();
            root1.getChildren().add(imageView);
        }
        double currentY=root1.getLayoutY();
        if(drill.checkDriilOrNot(root1, root, root1.getLayoutX(), currentY+50,stage,keyTimeline)){
            root1.setLayoutY(currentY+50);
            stage.setScene(scene); // Assuming 'scene' is already initialized
        }
      
    }
      /**
     * Moves the drill image to the left within the specified scene if the drill is not obstructed.
     *
     * @param root1       StackPane containing the drill image
     * @param scene       The scene in which the drill is moving
     * @param stage       The stage associated with the scene
     * @param root        JavaFX root node of the scene
     * @param keyTimeline Timeline used for animation
     */
    public static void moveleft(StackPane root1,Scene scene,Stage stage,Group root,Timeline keyTimeline){
        Image left=new Image("assets\\drill\\drill_52.png");
        imageView.setImage(left);
        Drill drill=new Drill();
        if (!root1.getChildren().contains(imageView)) {
            root1.getChildren().clear();
            root1.getChildren().add(imageView);
        }
        double currentX=root1.getLayoutX();
        if(drill.checkDriilOrNot(root1, root, currentX-50, root1.getLayoutY(),stage,keyTimeline)){
        root1.setLayoutX(currentX-50);
        stage.setScene(scene);} // Assuming 'scene' is already initialized
    }
      /**
     * Moves the drill image to the right within the specified scene if the drill is not obstructed.
     *
     * @param root1       StackPane containing the drill image
     * @param scene       The scene in which the drill is moving
     * @param stage       The stage associated with the scene
     * @param root        JavaFX root node of the scene
     * @param keyTimeline Timeline used for animation
     */
    public static void moveright(StackPane root1,Scene scene,Stage stage,Group root,Timeline keyTimeline){
        Image right=new Image("assets\\drill\\drill_60.png");
        imageView.setImage(right);
        Drill drill=new Drill();
        if (!root1.getChildren().contains(imageView)) {
            root1.getChildren().clear();
            root1.getChildren().add(imageView);
        }
        double currentX=root1.getLayoutX();
        if(drill.checkDriilOrNot(root1, root, currentX+50, root1.getLayoutY(),stage,keyTimeline)){
        root1.setLayoutX(currentX+50);
        
        stage.setScene(scene);} // Assuming 'scene' is already initialized

    }
}
/**
 * This class provides methods for checking if the drill can move to a specified position within the game environment.
 */
class Drill{
      /**
     * Checks whether the drill can move to the specified position on the game grid and performs relevant actions.
     *
     * @param root1      StackPane containing the drill image
     * @param root       JavaFX root node of the game scene
     * @param xAxis      X coordinate of the target position
     * @param yAxis      Y coordinate of the target position
     * @param stage      The stage associated with the game scene
     * @param keyTimeline Timeline used for animation
     * @return true if the drill can move to the specified position, false otherwise
     */
    public boolean checkDriilOrNot(StackPane root1,Group root,double xAxis,double yAxis,Stage stage,Timeline keyTimeline){
        Main app=new Main();
        ArrayList<Image> lavaImages=app.getLava();
        ArrayList<Image> soilImages=app.getSoil();
        ArrayList<Image> obstacleImages=app.getObstacle();
        ArrayList<Value> valueInfo=app.getValueInfo();
        ArrayList<Image> topSoilImages=app.getTopSoil();
        int money=app.getMoney();
        int haul=app.getHaul();
        for(Node node: root.getChildren()){
           if(node instanceof ImageView){
            ImageView imageView=(ImageView) node;
            double xNode=imageView.getX();
            double yNode=imageView.getY();
            double width=imageView.getBoundsInLocal().getWidth();
            double height=imageView.getBoundsInLocal().getHeight();
            Image image = imageView.getImage(); 
            if((xAxis==xNode)&&(yAxis==yNode)){
                if(valueInfo!=null){
                for (Value value : valueInfo) {
                    if (value.getImage() != null && value.getImage().equals(image)) {
                        int cost=value.getCost();
                        money+=cost;
                        app.setMoney(money);
                        int weight=value.getWeight();
                        haul+=weight;
                        app.setHaul(haul);
                        root.getChildren().remove(node);
                        Rectangle rectangle = new Rectangle(xNode, yNode, width, height);
                        rectangle.setFill(Color.BROWN);
                        root.getChildren().add(rectangle);
                        rectangle.toBack();
                        return true; 
                    }
                }}
                if(image!=null){
                    if(lavaImages.contains(image)){
                        Text gameOverText = new Text("GAME OVER");
                        gameOverText.setFont(Font.font("Arial", FontWeight.BOLD, 48));
                        gameOverText.setFill(Color.BLACK);
                
                        StackPane rootOver = new StackPane();
                        rootOver.getChildren().add(gameOverText);
                        rootOver.setStyle("-fx-background-color: red;"); // Arka plan rengini ayarlar
                        Scene scene = new Scene(rootOver, 1000, 1000);
                        keyTimeline.stop();
                
                        stage.setTitle("HU LOAD");
                        stage.setScene(scene);
                        stage.show();
                      
                        return false;
                    }
                    else if(obstacleImages.contains(image)){
                        return false;
                        
                    }
                    else if(soilImages.contains(image)){
                        root.getChildren().remove(node);
                        Rectangle rectangle = new Rectangle(xNode, yNode, width, height);
                        rectangle.setFill(Color.BROWN);
                        root.getChildren().add(rectangle);
                        rectangle.toBack();
                        return true;
                    }
                    else if(topSoilImages.contains(image)){
                        root.getChildren().remove(node);
                        Rectangle rectangle = new Rectangle(xNode, yNode, width, height);
                        rectangle.setFill(Color.BROWN);
                        root.getChildren().add(rectangle);
                        rectangle.toBack();
                        return true;
                    }
                    else{
                        return true;
                    }
                }
                else{
                    return false;
                }

            }
           }
        }
        return true;
    }
    /**
     * Checks whether the drill can move to the specified position on the game grid.
     *
     * @param root1  StackPane containing the drill image
     * @param root   JavaFX root node of the game scene
     * @param xAxis  X coordinate of the target position
     * @param yAxis  Y coordinate of the target position
     * @param upControl Flag indicating if the drill is moving upwards
     * @return true if the drill can move to the specified position, false otherwise
     */
    public boolean checkDriilOrNot(StackPane root1,Group root,double xAxis,double yAxis,boolean upControl){
        Main app=new Main();
        ArrayList<Image> lavaImages=app.getLava();
        ArrayList<Image> soilImages=app.getSoil();
        ArrayList<Image> obstacleImages=app.getObstacle();
        ArrayList<Value> valueInfo=app.getValueInfo();
        ArrayList<Image> topSoilImages=app.getTopSoil();
        for(Node node: root.getChildren()){
           if(node instanceof ImageView){
            ImageView imageView=(ImageView) node;
            double xNode=imageView.getX();
            double yNode=imageView.getY();
            double width=imageView.getBoundsInLocal().getWidth();
            double height=imageView.getBoundsInLocal().getHeight();
            Image image = imageView.getImage(); 
            if((xAxis==xNode)&&(yAxis==yNode)){
                if(valueInfo!=null){
                for (Value value : valueInfo) {
                    if (value.getImage() != null && value.getImage().equals(image)) {
                        return false; 
                    }
                }}
                if(image!=null){
                    if(lavaImages.contains(image)){
                        return false;
                    }
                    else if(obstacleImages.contains(image)){
                        return false;
                    }
                    else if(soilImages.contains(image)){
                        return false;
                      
                    }
                    else if(topSoilImages.contains(image)){
                       
                        return false;
                    }
                    else{
                        return true;
                    }
                }
                else{
                    return true;
                }

            }
           }
        }
        return true;
    }

    
}
/**
 * The Value class represents an item with a name, weight, cost, image path, and associated image.
 */
class Value{
    private String name;
    private int weight;
    private int cost;
    private String path;
    private Image image_thing;
     /**
     * Constructs a Value object with the specified attributes.
     *
     * @param name        The name of the item
     * @param weight      The weight of the item
     * @param cost        The cost of the item
     * @param path        The file path to the image of the item
     * @param image_thing The Image object representing the item
     */
    public Value(String name,int weight,int cost,String path,Image image_thing){
        this.name=name;
        this.weight=weight;
        this.cost=cost;
        this.path=path;
        this.image_thing=image_thing;
    }
    //There are get method for every variable
    public int getWeight(){
        return weight;
    }
    public int getCost(){
        return cost;
    }
    public Image getImage(){
        return image_thing;
    }
 

}