import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
public class Main {
    public static void main(String[] args) {
        Product.fill(args);
        Purchase.cash(args);
        
    }
}
class Product {
    
    public double productProtein;//It is double because input file has double number
    public double productFat;//It is double because input file has double number
    public double productCarbo;//It is double because input file has double number
    public String productName;//It is name so string
    public int productPrice;//Price is integer because money is a whole number because there is no decimal money
    public static String[] kindFoods=new String[24];//It has 24 slots which has name of foods
    public static int [] numberOfproducts=new int [24];//It has 24 slots which number of foods
    public static Product [] products=new Product[24];//create object array which has 24 object because 24 slots
    public Product(double productProtein,double productCarbo,double productFat,String productName,int productPrice){//It is constructor of product
        this.productProtein=productProtein;
        this.productCarbo=productCarbo;
        this.productFat=productFat;
        this.productName=productName;
        this.productPrice=productPrice; 
    }
 private static  int  calculateCalorie(double protein,double carbo,double fat){
    //ıt calculate calorie and private was used because it is a product-specific feature
        double total= 4* protein+4*carbo+9*fat;
        return (int) Math.round(total);
    }
    public int getCalorie(){
        //It get calorie and public was used beacuse ıt must be accessible
        return calculateCalorie(productProtein, productCarbo, productFat);
    }
    public static int fill(String[] args){
        String [] lines=File.readFile(args[0], false, false);
        //read file 
        int reach=0; //It is counter
        int number=0; //ıt is counter of whhile loop
        boolean full=false; //It is for full case
        for (int i=0; i<24;i++){
            numberOfproducts[i]=0;//ıt make variable for number of product list
        }
        for (String line:lines){
            String [] tabLines=line.split("\t");
            int constantKind=0;//ıt counter of kind of food's array
            boolean solution=true;
            while ( (number<lines.length)&&(constantKind<24)&&(solution)){
                if (kindFoods[constantKind]==null){
                    kindFoods[constantKind]=tabLines[0];//It assign name of food to kind of food's array
                    numberOfproducts[constantKind]+=1;//It increases the value of variables in number of products
                    solution=false;//It is for break while loop
                    String [] productType=tabLines[2].split("\\s+");
                    double pro= Double.parseDouble(productType[0]);
                    double carbo=Double.parseDouble(productType[1]);
                    double fat=Double.parseDouble(productType[2]);
                    int price=Integer.parseInt(tabLines[1]);
                    products[constantKind]=new Product(pro, carbo, fat,tabLines[0], price);//It creates object
                    
                    
                }else if(kindFoods[constantKind].equals(tabLines[0])){
                    if (numberOfproducts[constantKind]==10){//After 10 products, the values of number of products are not increased
                        constantKind+=1;
                    }
                    else{
                        numberOfproducts[constantKind]+=1;//starts incrementing the value of the next index of number of products if the value does not reach 24
                        solution=false;

                    }
                }
                constantKind+=1;
            }
            if (solution==true){//if the value of the solution has not changed, then it is not placed
                String info1="INFO: There is no available place to put "+tabLines[0];
                File.writeToFile(args[2], info1, true, true); // It write to file
            }
            if (full){
                //If full is true ,ıt writes info about that
                String info2="INFO: The machine is full!";
                File.writeToFile(args[2], info2, true, true);
                break;
            }
        
            number++;
            if((full(numberOfproducts))){//If full method returns true full variable assign true values
                full=true;
            }

        }
        String gym="-----Gym Meal Machine-----";//It is first part of gym
        File.writeToFile(args[2], gym, true, true);
        for (Product product:products){//writes the information of the product 
            if (product!=null){
                String calorieAndNumber=String.format("%s(%d, %d)___",product.productName,product.getCalorie(),numberOfproducts[reach]);
                File.writeToFile(args[2], calorieAndNumber, true, false);
                if (reach%4==3){
                    File.writeToFile(args[2],"" , true, true);
                }
            }
            else if(product==null){
                //writes the information of the product in the slot when it is not available

                String anyZero=String.format("___(%d, %d)___",0,numberOfproducts[reach]);
                File.writeToFile(args[2], anyZero, true, false);
                if (reach%4==3){
                    File.writeToFile(args[2],"", true, true);
                }
            }
            reach++;
        }
        String last="----------";//It is for last part
        File.writeToFile(args[2], last, true, true);
        if (full){
            return -1;
        }
        else{
            return 0;
        }

    }
    public static boolean full(int[] full){
        //It return slots machine is full or not
        int total=0;
        for (int i=0;i<24;i++){
            total+=full[i];
        }
        return 240==total;
    }
}
class Purchase {
    private int money; //private are used because money is Purchase specific feature
    public void setMoney(int money){// It sets money
        this.money=money;
    }
    public Purchase(String[] args) {   
    }
    public static void cash(String[] args) {
        String[] lines = File.readFile(args[1], false, false);
        int [] counts=Product.numberOfproducts;//It call number of product which is arrays of Product class
        String [] foods=Product.kindFoods;
        Product[] productArray = Product.products;//It call producst arrays of Product class
        Purchase[] purchases=new Purchase[lines.length];//It creates object arrays
        int counter=0;//It is purchase object counter
        for (String line : lines) {
            String[] tabLines = line.split("\t");//It split information about purchase
            String[] values = tabLines[1].split("\\s+");//It split values of money
            purchases[counter]=new Purchase(args);//It creates object of Purchase
            int totalMoney=0;
            boolean invalid=false;
            if (cashControl(values)){
                //It calculates total of money
                for (String value : values) {
                        
                    
                        int value1 = Integer.parseInt(value);
                        totalMoney+=value1;
                }
                purchases[counter].setMoney(totalMoney);//It assign to Purchase's money attributes
            }
            
            else {
                invalid=true;
                for (String value : values) {
                    if (cashOne(value)){
                    int value1 = Integer.parseInt(value);
                    totalMoney+=value1;}
            }
            purchases[counter].setMoney(totalMoney);
                
            }
            boolean found=false;
            for (int i=0;i<24;i++){
                if (productArray[i]!=null){
                    int calorie=productArray[i].getCalorie();
                    if (nutritiveValue(tabLines[2], Integer.parseInt(tabLines[3]), productArray[i].productCarbo,productArray[i].productFat,
                    productArray[i].productProtein,calorie,i)){//It call function
                        found=true;
                        if (productArray[i].productPrice<=purchases[counter].getMoney()){//It checks money is sufficcient or not.That case sufficent money case
                            //It gives information about that
                            String inputSentence1="INPUT: "+tabLines[0]+"\t"+tabLines[1]+"\t"+tabLines[2]+"\t"+tabLines[3];
                      
                            String purchaseSentence1="PURCHASE: You have bought one "+productArray[i].productName;
                            String returnSentence1="RETURN: Returning your change: "+(purchases[counter].getMoney()-productArray[i].productPrice)+" "+"TL";
                            File.writeToFile(args[2], inputSentence1, true, true);
                            if (invalid==true){
                                String returnMoney="INFO: Invalid Money";//It is cash invalid case
                                File.writeToFile(args[2], returnMoney, true, true);
                            }
                            File.writeToFile(args[2], purchaseSentence1, true, true);
                            File.writeToFile(args[2], returnSentence1, true, true);

                            if (counts[i]==1){
                                counts[i]=0;
                                productArray[i]=null;//Assigns null if there is no product
                                foods[i]="no";
                            }
                            else{
                                counts[i]-=1;
                            }
                            break;
                        }
                        else if(productArray[i].productPrice>purchases[counter].getMoney()){//It checks money is sufficcient or not. That case insufficent money case
                            String  inputSentence2="INPUT: "+tabLines[0]+"\t"+tabLines[1]+"\t"+tabLines[2]+"\t"+tabLines[3];
                          
                            String infoSentence2="INFO: Insufficient money, try again with more money.";
                            String returnSentence2="RETURN: Returning your change: "+(purchases[counter].getMoney())+" "+"TL";
                            File.writeToFile(args[2], inputSentence2, true, true);
                            if (invalid==true){
                                String returnMoney="INFO: Invalid Money";//It is cash invalid case
                                File.writeToFile(args[2], returnMoney, true, true);
                            }
                            File.writeToFile(args[2], infoSentence2, true, true);
                            File.writeToFile(args[2], returnSentence2, true, true);
                            break;
                        }
                    }
                   }
                else if (productArray[i]==null){
                    if ((tabLines[2].equals("NUMBER"))&&(Integer.parseInt(tabLines[3])<24)){//It checks slot is empty or not
                        if (Integer.parseInt(tabLines[3])==i){//It gives information about that
                            String inputSentence3="INPUT: "+tabLines[0]+"\t"+tabLines[1]+"\t"+tabLines[2]+"\t"+tabLines[3];
                            String infoSentence3="INFO: This slot is empty, your money will be returned.";
                            String returnSentence3="RETURN: Returning your change: "+purchases[counter].getMoney()+" "+"TL";
                            File.writeToFile(args[2], inputSentence3, true, true);
                            File.writeToFile(args[2], infoSentence3, true, true);
                            File.writeToFile(args[2], returnSentence3, true, true);
                            found=true;
                            break;}
                    }
                    else if ((tabLines[2].equals("NUMBER"))&&(Integer.parseInt(tabLines[3])>24)){//It checks slots number is logic or not
                        //It gives information about that
                        String inputSentence4="INPUT: "+tabLines[0]+"\t"+tabLines[1]+"\t"+tabLines[2]+"\t"+tabLines[3];
                        String infoSentence4="INFO: Number cannot be accepted. Please try again with another number.";
                        String returnSentence4="RETURN: Returning your change: "+purchases[counter].getMoney()+" "+"TL";
                        File.writeToFile(args[2], inputSentence4, true, true);
                        File.writeToFile(args[2], infoSentence4, true, true);
                        File.writeToFile(args[2], returnSentence4, true, true);
                        found=true;
                        break;
                }
            }
        }
        if (!found==true){
            //Found value indicates whether the product is available.It false case and ıt gives information about that
            String inputSentence="INPUT: "+tabLines[0]+"\t"+tabLines[1]+"\t"+tabLines[2]+"\t"+tabLines[3];
            String infoSentence="INFO: Product not found, your money will be returned.";
            String returnSentence="RETURN: Returning your change: "+purchases[counter].getMoney()+" "+"TL";
            File.writeToFile(args[2], inputSentence, true, true);
            File.writeToFile(args[2], infoSentence, true, true);
            File.writeToFile(args[2], returnSentence, true, true);
        }
        counter++;
    }
    int reach=0;
    String gym="-----Gym Meal Machine-----";//It is for writing gym machine
    File.writeToFile(args[2], gym, true, true);
    for (Product product:productArray){
        if (product!=null){//writes the information of the product 
            String calorieAndMany=String.format("%s(%d, %d)___",product.productName,product.getCalorie(),counts[reach]);
            File.writeToFile(args[2], calorieAndMany, true, false);

            if (reach%4==3){
                File.writeToFile(args[2], "", true, true);
            }
        }
        else if(product==null){
            //writes the information of the product in the slot when it is not available
            String anyZero=String.format("___(%d, %d)___",0,counts[reach]);
            File.writeToFile(args[2], anyZero, true, false);

            if (reach%4==3){
                File.writeToFile(args[2], "", true, true);;
            }
        }
        reach++;
    }
    String last="----------";
    File.writeToFile(args[2], last, true, true);
    }
    public int getMoney() {//It gets money
        return money;
    }
    public static boolean cashControl(String[]array){
        boolean solutution=false;
        //Checks for valid money value
        for (String banknot:array){
            if (banknot.equals("1")||banknot.equals("5")||banknot.equals("10")||
            banknot.equals("20")||banknot.equals("50")||
            banknot.equals("100")||banknot.equals("200")){
                solutution= true;
            }
            else{
                solutution=false;
                break;
            }
        }
        return solutution;
        }
    public static boolean cashOne(String money){
        if (money.equals("1")||money.equals("5")||money.equals("10")||
        money.equals("20")||money.equals("50")||
        money.equals("100")||money.equals("200")){
            return true;}
        else{
            return false;
        }
    }
        public static boolean nutritiveValue(String choice, int value, double carbo, double fat, double pro, int calorie, int number) {
            //Checks whether the product conforms to the specified specifications
            if ("CARB".equals(choice)) {
                if ((carbo <= value) && (value - 5 <= carbo)) {
                    return true;
                } else if ((carbo >= value) && (value + 5 >= carbo)) {
                    return true;
                }
                else{
                    return false;
                }
            } else if ("PROTEIN".equals(choice)) {
                if ((pro <=value) && (value - 5 <=pro)) {
                    return true;
                } else if ((pro >= value) && (value + 5 >= pro)) {
                    return true;
                }
                else{
                    return false;
                }
            } else if ("FAT".equals(choice)) {
                if ((fat <= value) && (value - 5 <= fat)) {
                    return true;
                } else if ((fat >= value) && (value + 5 >= fat)) {
                    return true;
                }
                else{
                    return false;
                }
            } else if ("CALORIE".equals(choice)) {
                if ((calorie <= value) && (value - 5 <= calorie)) {
                    return true;
                } else if ((calorie >= value) && (value + 5 >= calorie)) {
                    return true;
                }
                else{
                    return false;
                }
            } else if ("NUMBER".equals(choice)) {
                if ((value == number) && (value < 24)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
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