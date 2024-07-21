import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
public class Main {
    public static void main(String[] args) {
        String[] items = File.readFile(args[0], true, true);
        String []decoresThing=File.readFile(args[1], true, true);
        ArrayList<ClassRoom> classrooms = new ArrayList<>();
        ArrayList<Decoration> decoration=new ArrayList<>();
        for(String item:items){
            String [] tabItem =item.split("\t");
            String typeItem=tabItem[0];
            if (typeItem.equals("CLASSROOM")){
                // It creates class object for classroom
                ClassRoom classRoom=new ClassRoom();
                classRoom.setClassRoomName(tabItem[1]);
                //It assign classroomname
                String shape=tabItem[2];
                int width=Integer.parseInt(tabItem[3]);
                int length=Integer.parseInt(tabItem[4]);
                int height=Integer.parseInt(tabItem[5]);
                //It assign witdh, length,height
                if (shape.equals("Circle")){
                    Circle circle=new Circle(width/2.0,height);
                    //ıt assign constructor for circle
                    classRoom.setShape(circle);
              
                }
                else if(shape.equals("Rectangle")){
                    Rectangle rectangle=new Rectangle(width, length, height);
                    //ıt assign constructor for rectangle
                    classRoom.setShape(rectangle);
                }
                classrooms.add(classRoom);
            }
            else if(typeItem.equals("DECORATION")){
                Decoration decorate=new Decoration();
                //ıt creates decoration object and their constructor
                decorate.setDecorationName(tabItem[1]);
                //It assign Decoration object name
                String typeDecore=tabItem[2];
                int decorePrice=Integer.parseInt(tabItem[3]);
                if(typeDecore.equals("Paint")){
                    Paint paint=new Paint(decorePrice);
                    //ıt assign values of constructor for paint

                    decorate.setPaint(paint);
                }
                else if(typeDecore.equals("Tile")){
                    int tileArea=Integer.parseInt(tabItem[4]);
                    Tile tile=new Tile(decorePrice, tileArea);
                    //ıt assign values of constructor for tile
                    decorate.setTile(tile);
                }
                else if(typeDecore.equals("Wallpaper")){
                    WallPaper wallPaper=new WallPaper(decorePrice);
                    //ıt assign values of constructor for wallPaper
                    decorate.setWallPaper(wallPaper);
                }
                decoration.add(decorate);
            }

        }
        int totalOfMoney=0; //It is for total of money all classroom
        for (String decore : decoresThing){
            String[] decoreItem=decore.split("\t");
            String className=decoreItem[0];
            String wallName=decoreItem[1];
            String floorName=decoreItem[2];
            int classRoomTotal=0;
            for (ClassRoom room:classrooms){
                if (className.equals(room.getClassRoomName())){
                    double floorArea=room.getShape().calculateFloorArea();
                    double wallArea=room.getShape().calculateWallArea();
                    //ıt call method about  which floor or wall area according to the shape
                    boolean solution=false;
                    Shape shape=room.getShape();
                    for(Decoration ornament:decoration){
                        if (ornament.getDecorationName().equals(wallName)){
                            if(shape instanceof Rectangle){
                            if(ornament.getTile()!=null){
                            //ıt calculate for tile and write its information
                            int Area=ornament.getTile().calculateTilePrice(wallArea);
                            int tile=ornament.getTile().calculateTileNumber(wallArea);
                            String sentence="Classroom "+className+" used "+tile+" Tiles for walls and ";
                            classRoomTotal+=Area;
                            File.writeToFile(args[2],sentence,true,false);
                            solution=true;
                            break;
                        }
                        else if(ornament.getPaint()!=null){
                        //ıt calculate for paint and write its information
                        int Area=ornament.getPaint().calculatePaintPrice(wallArea);
                        String sentence1="Classroom "+className+" used "+(int) Math.ceil(wallArea)+"m2 of Paint for walls and ";
                        classRoomTotal+=Area;
                        solution=true;
                        File.writeToFile(args[2], sentence1, true, false);
                        break;
                        
                    }
                        
                        else if(ornament.getWallPaper()!=null){
                        //ıt calculate for WallPaper and write its information
                            int Area=ornament.getWallPaper().calculateWallPaperPrice(wallArea);
                        String sentence2="Classroom "+className+" used "+(int) Math.ceil(wallArea)+"m2 of Wallpaper for walls and ";
                        classRoomTotal+=Area;
                        solution=true;
                        File.writeToFile(args[2], sentence2, true, false);
                        break;

                        }
                        }
                        else if(shape instanceof Circle){
                            if(ornament.getPaint()!=null){
                                //ıt calculate for paint and write its information
                                int Area=ornament.getPaint().calculatePaintPrice(wallArea);
                                String sentence10="Classroom "+className+" used "+(int) Math.ceil(wallArea)+"m2 of Paint for walls and ";
                                classRoomTotal+=Area;
                                solution=true;
                                File.writeToFile(args[2], sentence10, true, false);
                                break;
                            }
                            else{
                                //ıt is info message
                                String Sentence11="You can choose only paint for wall area";
                                File.writeToFile(args[2], Sentence11, true, true);
                            }

                        }
                    }
                       
                        
                        }
                        for(Decoration ornament:decoration){
                        if(ornament.getDecorationName().equals(floorName)){
                            if (ornament.getTile()!=null){
                                int Area2=ornament.getTile().calculateTilePrice(floorArea);
                                int tile=ornament.getTile().calculateTileNumber(floorArea);
                                classRoomTotal+=Area2;
                                //ıt calculate for tile and write its information
                                
                                String sentence4="used "+tile+" Tiles for flooring, these costed "+classRoomTotal+"TL.";
                                totalOfMoney+=classRoomTotal;
                                File.writeToFile(args[2], sentence4, true, true);
                                
                            }
                            else{
                                //ıt is info message
                                String ErrorSentence="You can choose only tile for floor area";
                                File.writeToFile(args[2], ErrorSentence,true, true);
                            }
                        }
                    }
                        
                    }
                   
                }
                
            }
            //It is for total money which is info message
            String finalSentence="Total price is: "+totalOfMoney+"TL.";
            File.writeToFile(args[2], finalSentence, true, false);
            
        }
        
    }

  


class ClassRoom {
    private String classRoomName;
    private Shape shapes;
    //Encapculation for name and shape
    public void setShape(Shape shapes){
        this.shapes=shapes;
    }
    public Shape  getShape(){
        return shapes;
    }
    public void setClassRoomName(String classRoomName){
        this.classRoomName=classRoomName;
    }
    public String getClassRoomName(){
        return classRoomName;
    }
}

abstract class Shape extends ClassRoom {
    //abstract class floor AREa and wall area method
    abstract double calculateFloorArea();
    abstract double calculateWallArea();
}

class Rectangle extends Shape {
    private int width;
    private int length;
    private int height;

    Rectangle(int width, int length, int height) {
        this.width = width;
        this.length = length;
        this.height = height;
        //ıt is constructor
    }
    //ıt calculate wall area for rectangle
    double calculateWallArea() {
        return ((2 * (width + length)) * height);
    }
    //ıt calculate Floor area for rectangle
    double calculateFloorArea() {
        return (width * length);
    }
}

class Circle extends Shape {
    private double radius;
    private double height;
    //ıt is constructor of circle
    Circle(double radius, double height) {
        this.radius = radius;
        this.height = height;
    }
    //ıt is calculate floor area for circle 
    double calculateFloorArea() {
        return (Math.PI * radius * radius);
    }
    //ıt is calculate wall area for circle
    double calculateWallArea() {
        return (2 * Math.PI * radius * height);
    }
}

class Decoration {
    
    private String decorationName;
    private Tile tile;
    private WallPaper wallPaper;
    private Paint paint;
    //encapsulation for tile,decorationName,wallPaper,paint
    public void setDecorationName(String decorationName){
        this.decorationName=decorationName;
    }
    public String getDecorationName(){
        return decorationName;
    }
    public void setWallPaper(WallPaper wallPaper){
        this.wallPaper=wallPaper;
    }
    public WallPaper getWallPaper(){
        return wallPaper;
    }
    public void setTile(Tile tile){
        this.tile=tile;
    }
    public Tile getTile(){
        return tile;
    } public void setPaint(Paint paint){
        this.paint=paint;
    }
    public Paint getPaint(){
        return paint;
    }
        

}


class Tile extends Decoration {
    private int tilePrice;
    private int tileArea;
    //ıt is constructor for tile
    Tile(int tilePrice, int tileArea) {
        this.tileArea = tileArea;
        this.tilePrice = tilePrice;
    }
    public int getTilearea(){
        return tileArea;
    }
    //ıt is calculate tile numbers
    int calculateTileNumber(double area) {
        return (int) Math.ceil(area / tileArea);
    }
    //ıt calculate total tile price
    int calculateTilePrice(double area) {
        return tilePrice * calculateTileNumber(area);
    }
}

class WallPaper extends Decoration {
    private int wallPaperPrice;
    //It is constructor for wallpaperPrice
    WallPaper(int wallPaperPrice) {
        this.wallPaperPrice = wallPaperPrice;
    }
    //It calculate wallpaperPrice
    int calculateWallPaperPrice(double area) {
        return (int) Math.ceil(wallPaperPrice * area);
    }
}

class Paint extends Decoration {
    private int paintPrice;
    //ıt is constructor for paint
    Paint(int paintPrice) {
        this.paintPrice = paintPrice;
    }
    //ıt calculate paint prices
    int calculatePaintPrice(double area){
    return (int)Math.ceil(area *paintPrice);}
}
class File {
    public static void main(String[] args) {
       
    }

    public static String[] readFile(String path, boolean discardEmptyLines, boolean trim) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(path)); //Gets the content of file to the list.
            if (discardEmptyLines) { //Removes the lines that are empty with respect to trim.
                lines.removeIf(line -> line.trim().equals(""));
            }
            if (trim) { //Trims each line.
                lines.replaceAll(String::trim);
            }
            return lines.toArray(new String[0]);
        } catch (IOException e) { //Returns null if there is no such a file.
            e.printStackTrace();
            return null;
        }
    }
    public static void writeToFile(String path, String content, boolean append, boolean newLine) {
        PrintStream ps = null;
        try {
            ps = new PrintStream(new FileOutputStream(path, append));
            ps.print(content + (newLine ? "\n" : ""));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (ps != null) { //Flushes all the content and closes the stream if it has been successfully created.
                ps.flush();
                ps.close();
            }
        }
    }
}

