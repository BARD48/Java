
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
public  class Main{
    public static void main(String[] args) {
        ArrayList<Toy> toyList=new ArrayList<>();
        ArrayList<Book> bookList=new ArrayList<>();
        ArrayList<Stationery> stationerieryList=new ArrayList<>();
        FileInput fileInput=new FileInput();
        String outputPath=args[1];
        String [] file=fileInput.readFile(args[0], false, false);
        Product produce=new Product();
        boolean lineChecker=true;//It is for last line doesn't have empty line
        int checkNumber=0; //It is for last line doesn't have empty line
        //That for loop Check the file which method and execute That method
        for(String line :file){
            if(checkNumber==file.length-1){
                lineChecker=false;
            }
            String[] tabLines=line.split("\t");
            if (tabLines[0].equals("ADD")){
                if(tabLines[1].equals("Book")){
                    Book product=new Book(tabLines[2], tabLines[3], Integer.parseInt(tabLines[4]), Double.parseDouble(tabLines[5]));
                    bookList.add(product);
                    product.setBookList(bookList);
                    
                }
                else if(tabLines[1].equals("Toy")){
                    Toy product=new Toy(tabLines[2],tabLines[3], Integer.parseInt(tabLines[4]), Double.parseDouble(tabLines[5]));
                    toyList.add(product);
                    product.setToyList(toyList);
                }
                else if(tabLines[1].equals("Stationery")){
                    Stationery product=new Stationery(tabLines[2], tabLines[3],Integer.parseInt(tabLines[4]), Double.parseDouble(tabLines[5]));
                    stationerieryList.add(product);
                    product.setStationeryList(stationerieryList);
                }

            }
            else if(tabLines[0].equals("REMOVE")){
                if(produce.removeObject(bookList,Integer.parseInt(tabLines[1]),outputPath)||produce.removeObject(toyList,Integer.parseInt(tabLines[1]),outputPath)||produce.removeObject(stationerieryList,Integer.parseInt(tabLines[1]),outputPath)){
                    FileOutput.writeToFile(outputPath,"REMOVE RESULTS:\nItem is removed.\n------------------------------" , true, lineChecker);
                }
                else {
                    FileOutput.writeToFile(outputPath, "REMOVE RESULTS:\nItem is not found.\n------------------------------", true, lineChecker);
                }
            }
            else if(tabLines[0].equals("SEARCHBYBARCODE")){
                if(produce.searchByBarcodeObject(bookList, Integer.parseInt(tabLines[1]), outputPath)||produce.searchByBarcodeObject(toyList, Integer.parseInt(tabLines[1]), outputPath)||produce.searchByBarcodeObject(stationerieryList, Integer.parseInt(tabLines[1]), outputPath)){
                }
               
                else {
                    FileOutput.writeToFile(outputPath, "SEARCH RESULTS:\nItem is not found.", true, lineChecker);

                }
                FileOutput.writeToFile(outputPath, "------------------------------", true, lineChecker);
            }
            else if(tabLines[0].equals("SEARCHBYNAME")){
                if(produce.searchByNameObject(bookList, tabLines[1], outputPath)||produce.searchByNameObject(toyList, tabLines[1], outputPath)||produce.searchByNameObject(stationerieryList, tabLines[1], outputPath)){
                }
                else{
                    FileOutput.writeToFile(outputPath, "SEARCH RESULTS:\nItem is not found.", true, lineChecker);

                }
                FileOutput.writeToFile(outputPath, "------------------------------", true, lineChecker);
            }
            else if(tabLines[0].equals("DISPLAY")){
                FileOutput.writeToFile(outputPath,"INVENTORY:", true, true);
                produce.genericDisplay(bookList,outputPath);
                produce.genericDisplay(toyList,outputPath);
                produce.genericDisplay(stationerieryList,outputPath);
                if(stationerieryList.isEmpty()==false){
                    FileOutput.writeToFile(outputPath,"------------------------------", true, lineChecker);
                }
                else if(toyList.isEmpty()==false){
                    FileOutput.writeToFile(outputPath,"------------------------------", true, lineChecker);
                }
                else if(bookList.isEmpty()==false){
                    FileOutput.writeToFile(outputPath,"------------------------------", true, lineChecker);
                }
            }
            checkNumber++;
        }
    }
}
interface Describeable {
    void describe(String outputPath);
    int getBarcode();
    String getName();
    void remove(Object object);
    void searchByBarcode(Object object,String outputPath);
    void searchByName(Object object,String outputPath);
}
/**
 * The {@code Product} class represents a generic product.
 * It implements the {@code Describeable} interface, which defines methods for describing and searching products.
 * This class serves as a base class for more specific product types.
 */
class Product implements Describeable {
    @Override
    public void describe(String outputPath) {}

    @Override
    public int getBarcode() {
        return 0;
    }
    @Override
    public String getName(){
        return null;
    }

    @Override
    public void remove(Object object) {
        // This will be overridden in subclasses
    }
     /**
     * Removes an object with the specified barcode from the given collection.
     * 
     * @param objects    the collection of objects to search and remove from
     * @param barcode    the barcode of the object to remove
     * @param outputPath the path to describe the action taken
     * @return true if an object with the specified barcode is found and removed, otherwise false
     */
    public <T extends Describeable> boolean removeObject(Iterable<T> objects, int barcode, String outputPath) {
        for (T element : objects) {
            if (element.getBarcode() == barcode) {
                element.remove(element);
                return true;
            }
        }
        return false;
    }
     /**
     * Searches for an object with the specified barcode in the given collection.
     * 
     * @param objects    the collection of objects to search
     * @param barcode    the barcode to search for
     * @param outputPath the path to describe the action taken
     * @return true if an object with the specified barcode is found, otherwise false
     */
    public <T extends Describeable> boolean searchByBarcodeObject(Iterable <T> objects,int barcode,String outputPath){
        for (T element : objects) {
            if (element.getBarcode() == barcode) {
                element.searchByBarcode(element,outputPath);
                return true;
            }
        }
        return false;

    }
      /**
     * Searches for an object with the specified name in the given collection.
     * 
     * @param objects    the collection of objects to search
     * @param name       the name to search for
     * @param outputPath the path to describe the action taken
     * @return true if an object with the specified name is found, otherwise false
     */
    public <T extends Describeable> boolean searchByNameObject(Iterable<T> objects,String name,String outputPath){
        for (T element: objects){
            if(element.getName().equals(name)){
                element.searchByName(element,outputPath);
                return true;
            }
        }
        return false;
    }
    @Override
    public void searchByBarcode(Object object,String outputPath) {
    }
    @Override
    public void searchByName(Object object,String outpath) {
    }
     /**
     * Displays information about each object in the given collection.
     * 
     * @param objects    the collection of objects to display information for
     * @param outputPath the path to describe the action taken
     */
    public <T extends Describeable> void genericDisplay(Iterable<T> objects, String outputPath) {
        for (T element : objects) {
            element.describe(outputPath);
        }
    }
}
class Book extends Product {
    private List<Book> bookList = new ArrayList<>();
    private String name;
    private String author;
    private int barcode;
    private double price;
        /**
     * Constructs a new Book object with the specified attributes.
     * 
     * @param name    the name of the book
     * @param author  the author of the book
     * @param barcode the barcode of the book
     * @param price   the price of the book
     */

    Book(String name, String author, int barcode, double price) {
        this.name = name;
        this.author = author;
        this.barcode = barcode;
        this.price = price;
    }

    @Override
    public void describe(String outputPath) {
        
        FileOutput.writeToFile(outputPath, String.format("Author of the %s is %s. Its barcode is %d and its price is %s", name, author, barcode,PriceFormatter.formatPrice(price)), true, true);
    }
    //Set and get method barcode, name and BookList
    @Override
    public int getBarcode() {
        return barcode;
    }
    @Override
    public String getName(){
        return name;
    }

    @Override
    public void remove(Object object) {
            bookList.remove(object);
    }

    public void setBookList(List<Book> bookList) {
        this.bookList = bookList;
    }

    public List<Book> getBookList() {
        return bookList;
    }
    @Override
    public void searchByBarcode(Object object,String outputPath){
        FileOutput.writeToFile(outputPath,String.format("SEARCH RESULTS:\nAuthor of the %s is %s. Its barcode is %d and its price is %s",name,author,barcode,PriceFormatter.formatPrice(price)), true, true);
        
    }
    @Override
    public void searchByName(Object object,String outputPath){
        FileOutput.writeToFile(outputPath,String.format("SEARCH RESULTS:\nAuthor of the %s is %s. Its barcode is %d and its price is %s",name,author,barcode,PriceFormatter.formatPrice(price)), true, true);
    }
}
class Toy extends Product {
    private List<Toy> toyList = new ArrayList<>();
    private String name;
    private String color;
    private int barcode;
    private double price;
      /**
     * Constructs a new Toy object with the specified attributes.
     * 
     * @param name    the name of the toy
     * @param color   the color of the toy
     * @param barcode the barcode of the toy
     * @param price   the price of the toy
     */
    Toy(String name, String color, int barcode, double price) {
        this.name = name;
        this.color = color;
        this.barcode = barcode;
        this.price = price;
    }

    @Override
    public void describe(String outputPath) {
        FileOutput.writeToFile(outputPath, String.format("Color of the %s is %s. Its barcode is %d and its price is %s", name, color, barcode, PriceFormatter.formatPrice(price)), true, true);
    }
    //Set and get method barcode, name and toylist
    @Override
    public int getBarcode() {
        return barcode;
    }
    @Override
    public String getName(){
        return name;
    }
    @Override
    public void remove(Object object) {
        if (object instanceof Toy) {
            toyList.remove(object);
        }
    }
    public void setToyList(List<Toy> toyList) {
        this.toyList = toyList;
    }
    public List<Toy> getToyList() {
        return toyList;
    }
    @Override
    public void searchByBarcode(Object object,String outputPath){
        FileOutput.writeToFile(outputPath,String.format("SEARCH RESULTS:\nColor of the %s is %s. Its barcode is %d and its price is %s",name,color,barcode,PriceFormatter.formatPrice(price)), true, true);
        
    }
    @Override
    public void searchByName(Object object,String outputPath){
        FileOutput.writeToFile(outputPath,String.format("SEARCH RESULTS:\nColor of the %s is %s. Its barcode is %d and its price is %s",name,color,barcode,PriceFormatter.formatPrice(price)), true, true);  
    }
}
class Stationery extends Product {
    private List<Stationery> stationeryList = new ArrayList<>();
    private String name;
    private String kind;
    private int barcode;
    private double price;
      /**
     * Constructs a new Stationery object with the specified attributes.
     * 
     * @param name    the name of the stationery
     * @param kind    the kind of the stationery
     * @param barcode the barcode of the stationery
     * @param price   the price of the stationery
     */
    Stationery(String name, String kind, int barcode, double price) {
        this.name = name;
        this.kind = kind;
        this.barcode = barcode;
        this.price = price;
    }

    @Override
    public void describe(String outputPath) {
        FileOutput.writeToFile(outputPath, String.format("Kind of the %s is %s. Its barcode is %d and its price is %s", name, kind, barcode,PriceFormatter.formatPrice(price)), true, true);
    }
    //Set and get method barcode, name and StationaryList
    @Override
    public int getBarcode() {
        return barcode;
    }
    @Override
    public String getName(){
        return name;
    }

    @Override
    public void remove(Object object) {
        if (object instanceof Stationery) {
            stationeryList.remove(object);
        }
    }

    public void setStationeryList(List<Stationery> stationeryList) {
        this.stationeryList = stationeryList;
    }

    public List<Stationery> getStationeryList() {
        return stationeryList;
    }
    @Override
    public void searchByBarcode(Object object,String outputPath){
        FileOutput.writeToFile(outputPath,String.format("SEARCH RESULTS:\nKind of the %s is %s. Its barcode is %d and its price is %s",name,kind,barcode,PriceFormatter.formatPrice(price)), true, true);
        
    }
    @Override
    public void searchByName(Object object,String outputPath){
        FileOutput.writeToFile(outputPath,String.format("SEARCH RESULTS:\nKind of the %s is %s. Its barcode is %d and its price is %s",name,kind,barcode,PriceFormatter.formatPrice(price)), true, true);
    }
}
class PriceFormatter {
    public static String formatPrice(double price) {
        // Tam sayı kontrolü
        if (price % 1 == 0) {
            return String.format("%.1f", price); // Ondalık kısmı gösterme
        } else {
            String formattedPrice = String.format("%.2f", price);
            if (formattedPrice.endsWith(".00")) {
                formattedPrice = formattedPrice.substring(0, formattedPrice.length() - 3);
            } else if (formattedPrice.charAt(formattedPrice.length() - 1) == '0') {
                formattedPrice = formattedPrice.substring(0, formattedPrice.length() - 1);
            }
            return formattedPrice;
        }
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