import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;
import java.util.HashMap;
public class MapAnalyzer{
    public static void main(String[] args) {
        FileInput fileInput = new FileInput();
        String[] file = fileInput.readFile(args[0], false, false);
        ArrayList<Road> roadList = new ArrayList<>();
        String outputPath=args[1];
        String startPoint = (file[0].split("\t"))[0];
        String endPoint = (file[0].split("\t"))[1];
        MapAnalyzer analyzer = new MapAnalyzer();
        HashMap<String, Road> hashRoad = new HashMap<>();
        analyzer.addRoad(roadList, file, startPoint);
        Collections.sort(roadList);
        //It call methods and It contınues to the roadlistempty 
        while (!roadList.isEmpty()){
            Road currentObject = roadList.remove(0);
            String point1 = currentObject.firstPoint;
            String point2 = currentObject.secondPoint;
            String newPoint = hashRoad.containsKey(point2) ? point1 : point2;
            if (hashRoad.containsKey(newPoint)) {
                continue;
            }
            else{
                analyzer.updateAddRoad(roadList, file, currentObject);
                hashRoad.put(newPoint, currentObject);
            }
            Collections.sort(roadList);
        }
        
        ArrayList<String> shortRoad=analyzer.roadAdder(hashRoad, startPoint, endPoint);
        Collections.reverse(shortRoad);
        analyzer.writter(hashRoad, startPoint, endPoint, shortRoad, outputPath);
        ArrayList<Road> kruskalRoad=new ArrayList<>();
        HashSet<String> pointsSet=new HashSet<>();
        analyzer.kruskalOrderRoad(kruskalRoad, file,pointsSet);
        ArrayList<Road> kruskalPath=analyzer.kruskalRoadFinder(kruskalRoad, pointsSet);
        analyzer.writter(kruskalPath,outputPath);
        ArrayList<Road> kruskalinitial=analyzer.kruskalAddRoad(kruskalPath, startPoint);
        HashMap<String, Road> hashRoadKruskal = new HashMap<>();
        Collections.sort(kruskalinitial);
        while (!kruskalinitial.isEmpty()){
            Road currentObject = kruskalinitial.remove(0);
            String point1 = currentObject.firstPoint;
            String point2 = currentObject.secondPoint;
            
    
            String newPoint = hashRoadKruskal.containsKey(point2) ? point1 : point2;
            if (hashRoadKruskal.containsKey(newPoint)) {
                continue;
            }
            else{
                analyzer.krusakalUpdateRoad(kruskalinitial, kruskalPath, currentObject);
                hashRoadKruskal.put(newPoint, currentObject);
            }
            Collections.sort(kruskalinitial);
            

        }
        ArrayList<String> shortRoadKruskal=analyzer.roadAdder(hashRoadKruskal, startPoint, endPoint);
        Collections.reverse(shortRoadKruskal);
        analyzer.writterKruskal(hashRoadKruskal, startPoint, endPoint, shortRoadKruskal, outputPath);
        int originalDistance=0;
        for (int i = 1; i < file.length; i++) {
            String[] tabLines = file[i].split("\t");
            originalDistance+=Integer.parseInt(tabLines[2]);
        }
        int kruskalsDistance=0;
        for(Road road: kruskalPath){
            kruskalsDistance+=road.distance;
        }
        analyzer.writterAnalysz(endPoint, hashRoad, hashRoadKruskal, kruskalsDistance, originalDistance,outputPath);
    }
      /**
     * It is for find road from hashmap
     * 
     * @param road It is hashmap which have short roads path
     * @param startpoint It is startpoint of destination
     * @param endPoint It is end of destination
     * @return Name of points which is road
     */

    public ArrayList<String> roadAdder(HashMap<String, Road> road,String startPoint,String endPoint) {
        ArrayList<String> shortRoad=new ArrayList<>();
        while(!shortRoad.contains(startPoint)){
            for(String key: road.keySet()){
                if(key.equals(endPoint)){
                    shortRoad.add(endPoint);
                    endPoint=road.get(endPoint).getName();
                    if(endPoint.equals(startPoint)){
                        shortRoad.add(endPoint);
                    }
                    break;

                }
            }
            
        }
            return shortRoad;
    }
      /**
     * It is for find road from hashmap
     * 
     * @param kruskalPath It is hashmap which have short roads path . That one found by kruska algorithm
     * @param startpoint It is startpoint of destination
     * @param endPoint It is end of destination
     * @return Name of points which is road
     */
    public ArrayList<String> adderKruskal(ArrayList< Road> kruskalPath,String startPoint,String endPoint) {
        ArrayList<String> shortRoad=new ArrayList<>();
        while(!shortRoad.contains(startPoint)){
            for(Road road: kruskalPath){
                if(road.secondPoint.equals(endPoint)){
                    shortRoad.add(endPoint);
                    endPoint=road.firstPoint;
                    if(endPoint.equals(startPoint)){
                        shortRoad.add(endPoint);
                    }
                }
                else if(road.firstPoint.equals(endPoint)){
                    shortRoad.add(endPoint);
                    endPoint=road.secondPoint;
                    if(endPoint.equals(startPoint)){
                        shortRoad.add(endPoint);
                    }
                }
            }
            
        }
            return shortRoad;
    }
      /**
     * It writes about information about fastest route
     * 
     * @param road It is hashmap which have short roads path . 
     * @param startpoint It is startpoint of destination
     * @param endPoint It is end of destination
     * @param shortroad It is list of points which is best route
     * @param outputpath It is fileName
     */

    public void writter(HashMap<String, Road> road,String starpoint,String endPoint,ArrayList<String> shortRoad,String outputPath){
        String space="";
        space=space+"Fastest Route from "+starpoint+" to "+endPoint+ " ("+road.get(endPoint).distance+" KM"+")"+":"+"\n";
        for(String shorts: shortRoad){
            if(starpoint.equals(shorts)){
                continue;
            }
            else if(starpoint.equals(road.get(shorts).firstPoint)){
                if(road.get(shorts).swap==false){
                space=space+road.get(shorts).firstPoint+"\t"+road.get(shorts).secondPoint+"\t"+road.get(shorts).distance+"\t"+road.get(shorts).roadId+"\n";}
                else{
                    space=space+road.get(shorts).secondPoint+"\t"+road.get(shorts).firstPoint+"\t"+road.get(shorts).distance+"\t"+road.get(shorts).roadId+"\n";

                }
            }
            else{
                if(road.get(shorts).swap==false){
                space=space+road.get(shorts).firstPoint+"\t"+road.get(shorts).secondPoint+"\t"+(road.get(shorts).distance-road.get(road.get(shorts).firstPoint).distance)+"\t"+road.get(shorts).roadId+"\n";}
                else if(road.get(shorts).swap==true){
                    space=space+road.get(shorts).secondPoint+"\t"+road.get(shorts).firstPoint+"\t"+(road.get(shorts).distance-road.get(road.get(shorts).firstPoint).distance)+"\t"+road.get(shorts).roadId+"\n";
                }
           }

        }
        FileOutput.writeToFile(outputPath, space, false, false);
    }
      /**
     * It writes about information about fastest route on Barely connected map
     * 
     * @param road It is hashmap which have short roads path . 
     * @param startpoint It is startpoint of destination
     * @param endPoint It is end of destination
     * @param shortroad It is list of points which is best route
     * @param outputpath It is fileName
     */
    public void writterKruskal(HashMap<String, Road> road,String starpoint,String endPoint,ArrayList<String> shortRoad,String outputPath){
        String space="";
        space=space+"Fastest Route from "+starpoint+" to "+endPoint+" on Barely Connected Map" +" ("+road.get(endPoint).distance+" KM"+")"+":"+"\n";
        for(String shorts: shortRoad){
            if(shorts.equals(starpoint)){
                continue;
            }
          
            else if(starpoint.equals(road.get(shorts).firstPoint)){
                if(road.get(shorts).swap==false){
                    space=space+road.get(shorts).firstPoint+"\t"+road.get(shorts).secondPoint+"\t"+road.get(shorts).distance+"\t"+road.get(shorts).roadId+"\n";}
                else{
                    space=space+road.get(shorts).secondPoint+"\t"+road.get(shorts).firstPoint+"\t"+road.get(shorts).distance+"\t"+road.get(shorts).roadId+"\n";

                }
            }
            else{
                if(road.get(shorts).swap==false){
                    space=space+road.get(shorts).firstPoint+"\t"+road.get(shorts).secondPoint+"\t"+(road.get(shorts).distance-road.get(road.get(shorts).firstPoint).distance)+"\t"+road.get(shorts).roadId+"\n";}
                else if(road.get(shorts).swap==true){
                    space=space+road.get(shorts).secondPoint+"\t"+road.get(shorts).firstPoint+"\t"+(road.get(shorts).distance-road.get(road.get(shorts).firstPoint).distance)+"\t"+road.get(shorts).roadId+"\n";
                }
                
           }

        }
        FileOutput.writeToFile(outputPath, space, true, false);
    }
     /**
     * It writes about information about Barely conected map
     * 
     * @param roadList It is list of barely connected map
     * @param outputpath It is fileName
     */
    public void writter(ArrayList<Road> roadList,String outputPath){
        String space="";
        space=space+"Roads of Barely Connected Map is:";
        for(Road road: roadList){
            space=space+"\n"+road.firstPoint+"\t"+road.secondPoint+"\t"+road.distance+"\t"+road.roadId;

        }
        FileOutput.writeToFile(outputPath, space, true, true);
        
    }
      /**
     * It writes about information about analyse route of two conditions
     * 
     * @param road It is hashmap which have short roads path . 
     * @param endPoint It is end of destination
     * @param road It is hashmap which is fro the first case
     * @param outputpath It is fileName
     * @param kruskalroad It is hashmap which is fro the second case
     * @param kruskalsDistance It is barely conected total length
     * @param originalDistance It is total length of original roads
     */
    public void writterAnalysz(String endPoint,HashMap<String, Road> road,HashMap<String, Road> kruskalroad,int kruskalsDistance,int originalDistance,String outputPath){
        String space="";
        space=space+"Analysis:\n";
        space=space+String.format("Ratio of Construction Material Usage Between Barely Connected and Original Map: %.2f\n",(double) kruskalsDistance/originalDistance);
        space=space+String.format("Ratio of Fastest Route Between Barely Connected and Original Map: %.2f", (double) kruskalroad.get(endPoint).distance/road.get(endPoint).distance);
        FileOutput.writeToFile(outputPath, space, true, false);

    }
    /**
 * that class is for road
 */
    class Road implements Comparable<Road> {
        private int distance;
        private String firstPoint;
        private String secondPoint;
        private int roadId;
        private int previusroadId;
        private boolean swap;
         /**
         * It is road constructor
         * 
         * @param distance It is distance to start point
         * @param firstpoint It is first point of road side
         * @param secondPoint It is last point of road side 
         * @param roadId It is id of road
         * @param swap It is direction of road İf values changes It becomes true
         * @param previousroadId It is id of previous road
         */

        Road(int distance, String firstPoint, String secondPoint, int roadId,boolean swap,int previusroadId) {
            this.distance = distance;
            this.firstPoint = firstPoint;
            this.secondPoint = secondPoint;
            this.roadId = roadId;
            this.swap=swap;
            this.previusroadId=previusroadId;
            
        }
        //They are  encapsulation for roadId,firstpoint,secondpoint,

        public int getRoadId() {
            return roadId;
        }

        public String getName() {
            return firstPoint;
        }

        public String getsecondName() {
            return secondPoint;
        }

       
        /**
     * That method compares to the roads according to their distance and id
     * 
     * @param other other road object
     * @return If road is smaller than other roads and roadId is smaller than other roadId return negative 1 otherwise positive 1
     *         
     */

        @Override
        public int compareTo(Road other) {
            if (this.distance < other.distance) {
                return -1;
            } else if (this.distance > other.distance) {
                return 1;
            } else {
                if(this.previusroadId==other.previusroadId){
                return Integer.compare(this.roadId, other.roadId);}
                else{
                    return Integer.compare(this.previusroadId, other.previusroadId);
                }
            }
        }
    }
     /**
     * It is for find  directed roads and add roadlist
     * 
     * @param roadlist It is list of directed by startpoint
     * @param startpoint It is startpoint of destination
     * @param file  It is data of roads
     */
    public void addRoad(ArrayList<Road> roadlist, String[] file, String startPoint) {
        for (int i = 1; i < file.length; i++) {
            String[] tabLines = file[i].split("\t");
            if (tabLines[0].equals(startPoint)) {
                Road road = new Road(Integer.parseInt(tabLines[2]), tabLines[0], tabLines[1], Integer.parseInt(tabLines[3]),false,0);
                roadlist.add(road);
            }
            else if(tabLines[1].equals(startPoint)){
                Road road = new Road(Integer.parseInt(tabLines[2]), tabLines[1], tabLines[0], Integer.parseInt(tabLines[3]),true,0);
                roadlist.add(road);
            }
        }
    }
    /**
     * It is for find  directed roads and add roadlist and update the distance .Update the distance because of the sort.
     * 
     * @param roadlist It is list of directed by firstpoint
     * @param startpoint It is object of road which is previus road .So ıt is useful for update distance and create object
     * @param file  It is data of roads
     */
    public void updateAddRoad(ArrayList<Road> roadlist,String[] file,Road startPoint){
        for (int i = 1; i < file.length; i++) {
            String[] tabLines = file[i].split("\t");
            if (tabLines[0].equals(startPoint.secondPoint)) {
                Road road = new Road((Integer.parseInt(tabLines[2])+startPoint.distance), tabLines[0], tabLines[1], Integer.parseInt(tabLines[3]),false,startPoint.roadId);
                roadlist.add(road);
            }
            else if(tabLines[1].equals(startPoint.secondPoint)){
                Road road = new Road((Integer.parseInt(tabLines[2])+startPoint.distance), tabLines[1], tabLines[0], Integer.parseInt(tabLines[3]),true,startPoint.roadId);
                roadlist.add(road);
            }
        }
    }
    /**
     * It creates object of road add them kruskalroad and sort them distances It i first step of kruskal algorithm
     * 
     * @param kruskalRoad It is list of ordered road 
     * @param pointset It is unique names of set
     * @param file  It is data of roads
     */
    public void kruskalOrderRoad(ArrayList<Road> kruskalRoad,String[] file,HashSet<String> pointsSet){
        for (int i = 1; i < file.length; i++) {
            String[] tabLines = file[i].split("\t");
                Road road = new Road((Integer.parseInt(tabLines[2])), tabLines[0], tabLines[1], Integer.parseInt(tabLines[3]),false,0);
                kruskalRoad.add(road);
                pointsSet.add(road.firstPoint);
                pointsSet.add(road.secondPoint);
            
        }
        Collections.sort(kruskalRoad);

    }
      /**
     * It finds barely connected roads Main rules are don't cycle and one less than the number of points.
     * 
     * @param kruskalRoad It is list of ordered road 
     * @param pointset It is unique names of set
     */
    public ArrayList<Road>  kruskalRoadFinder(ArrayList<Road> kruskalRoad,HashSet<String> pointSet){
        ArrayList<Road> kruskalPath=new ArrayList<>();
        HashMap<String, List<String>> hashNeighbor = new HashMap<>();
        for (String point: pointSet){
            hashNeighbor.put(point, new ArrayList<>());
        }
        for (Road road : kruskalRoad) {
            if (!Cycle(hashNeighbor, road.firstPoint, road.secondPoint, new HashSet<>())) {
                kruskalPath.add(road);
                hashNeighbor.get(road.firstPoint).add(road.secondPoint);
                hashNeighbor.get(road.secondPoint).add(road.firstPoint);

                if (kruskalPath.size() == pointSet.size() - 1) {
                    break;
                }
            }
        }
        return kruskalPath;
    }
     /**
     * It is for find  directed roads and add roadlist
     * 
     * @param kruskalPath It is list of road which is found by kruskal
     * @param startpoint It is startpoint of destination
     */
    public ArrayList<Road>  kruskalAddRoad(ArrayList<Road> kruskalPath,String startPoint){
        ArrayList <Road> roadList=new ArrayList<>();
        for(Road road: kruskalPath){
            if (road.firstPoint.equals(startPoint)) {
                Road road1=new Road(road.distance, road.firstPoint, road.secondPoint, road.roadId,false,0);
                roadList.add(road1);
            }
            else if(road.secondPoint.equals(startPoint)){
                Road road1=new Road(road.distance, road.secondPoint, road.firstPoint, road.roadId,true,0);
                roadList.add(road1);
            }
        }
        return roadList;

    }
     /**
     * It is for find  directed roads and add roadlist and update the distance .Update the distance because of the sort.
     * 
     * @param kruskalinitial It is list of roads
     * @param startpoint It is object of road which is previus road .So ıt is useful for update distance and create object
     * @param kruskalPath It is list of road which is found by kruskal
     */
    public void  krusakalUpdateRoad(ArrayList<Road> kruskalinitial, ArrayList<Road> kruskalPath,Road startPoint){
        for(Road road: kruskalPath){
            if (road.firstPoint.equals(startPoint.secondPoint)) {
                Road road1=new Road(road.distance+startPoint.distance, road.firstPoint, road.secondPoint, road.roadId,false,startPoint.roadId);
                kruskalinitial.add(road1);
            }
            else if(road.secondPoint.equals(startPoint.secondPoint)){
                Road road1=new Road(road.distance+startPoint.distance, road.secondPoint, road.firstPoint, road.roadId,true,startPoint.roadId);
                kruskalinitial.add(road1);
            }
        }

    }
      /**
     * It finds it is cycle or not. I 
     * 
     * @param hashNeighbor It is hashmap. It show points of neigbors .
     * @param visited It is list of points initial case .It is empty
     * @param start It is name point
     * @param end It is name of point
     * @return It returns if it is cyle  true, Else false
     */
    public boolean Cycle(Map<String, List<String>> hashNeigbor, String start, String end, Set<String> visited) {
        if (start.equals(end)) {
            return true;
        }
        visited.add(start);
        for (String neighbor : hashNeigbor.get(start)) {
            if (!visited.contains(neighbor)) {
                if (Cycle(hashNeigbor, neighbor, end, visited)) {
                    return true;
                }
            }
        }
        return false;
    }

}


  
class FileInput {
    /**
     * Reads the file at the given path and returns contents of it in a string array.
     *
     * @param path              Path to the file that is going to be read.
     * @param discardEmptyLines If true, discards empty lines with respect to trim; else, it takes all the lines from the file.
     * @param trim              Trim status; if true, trims (strip in Python) each line; else, it leaves each line as-is.
     * @return Contents of the file as a string array, returns null if there is not such a file or this program does not have sufficient permissions to read that file.
     */
    public String[] readFile(String path, boolean discardEmptyLines, boolean trim) {
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
}
class FileOutput {
    /**
     * This method writes given content to file at given path.
     *
     * @param path    Path for the file content is going to be written.
     * @param content Content that is going to be written to file.
     * @param append  Append status, true if wanted to append to file if it exists, false if wanted to create file from zero.
     * @param newLine True if wanted to append a new line after content, false if vice versa.
     */
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
    
    
