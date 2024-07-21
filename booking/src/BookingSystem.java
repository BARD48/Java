import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.File;
import java.util.List;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
public class BookingSystem{
    
    public static void main(String[] args) {
        if(args.length!=2){
            System.out.println("ERROR: This program works exactly with two command line arguments, the first one is the path to the input file whereas the second one is the path to the output file. Sample usage can be as follows: \"java8 BookingSystem input.txt output.txt\". Program is going to terminate!");
            System.exit(1);
        }
        File file1 = new File(args[1]);
        if (file1.exists()) {//That one for apppend use 
            if (file1.length() > 0) {
                try {
                    file1.delete();
                    file1.createNewFile(); 
                } catch (IOException e) {
                }
            }
        }
        String [] lines = FileOne.readFile(args[0], true, true);  
        ArrayList<Voyage> voyages = new ArrayList<>();
        ArrayList<Integer> idList=new ArrayList<>();
        String outputPath=args[1];
        //assigned values above
        int counter=0;//That is for Z report when Z report isn't last
        for (String line : lines) {
            counter++;
            String[] tabLines = line.split("\t");
            String createVoyage = tabLines[0];
            boolean solution=false;
            if (createVoyage.equals("INIT_VOYAGE")) {//It checks Is it INIT voyage or not and ıt checks length .It is valid or not.
                if ((7<=tabLines.length)&&(tabLines.length<=9)){
                String sentence="COMMAND: "+line;
                FileOne.writeToFile(args[1], sentence, true, true);
                if (processTabLine(tabLines, outputPath)){//That condition statemnet checks type of values
                Voyage voyage = new Voyage();
                Vehicle vehicle=new Vehicle();
                String voyageType = tabLines[1];
                int voyageId = Integer.parseInt(tabLines[2]);
                String voyageFrom = tabLines[3];
                String voyageTo = tabLines[4];
                int rowNumber = Integer.parseInt(tabLines[5]);
                float voyagePrice = Float.parseFloat(tabLines[6]);
                //assigned values above   
                if(!voyageType.equals("Minibus")&&!voyageType.equals("Premium")&&!voyageType.equals("Standard")){
                    String sentence2="ERROR: Erroneous usage of \""+createVoyage+"\" command!";
                    FileOne.writeToFile(args[1], sentence2, true, true);
                    continue;   
                }
                if(voyageId<0){
                    String sentence3="ERROR: "+voyageId+" is not a positive integer, ID of a voyage must be a positive integer!";
                    FileOne.writeToFile(args[1], sentence3,true, true);
                    continue;
                }
                else if(voyage.checkID(voyageId, voyages)){
                    String sentence4="ERROR: There is already a voyage with ID of "+voyageId+"!";
                    FileOne.writeToFile(args[1], sentence4, true, true);
                    continue;
                }
                if (rowNumber<0){
                    String sentence5="ERROR: "+rowNumber+" is not a positive integer, number of seat rows of a voyage must be a positive integer!";
                    FileOne.writeToFile(args[1], sentence5, true, true);
                    continue;
                }
                if(voyagePrice<0){
                    int price=Math.round(voyagePrice);
                    String sentence6="ERROR: "+price+" is not a positive number, price must be a positive number!";
                    FileOne.writeToFile(args[1], sentence6, true, true);
                    continue;
                }
                if (voyageType.equals("Premium")||voyageType.equals("Standard")){
                    int refund=Integer.parseInt(tabLines[7]);
                    if (refund<0){
                        String sentence7="ERROR: "+refund+" is not an integer that is in range of [0, 100], refund cut must be an integer that is in range of [0, 100]!";
                        FileOne.writeToFile(args[1], sentence7, true, true);
                        continue;
                    }
                }
                if (voyageType.equals("Premium")){
                    int fee=Integer.parseInt(tabLines[8]);
                    if(fee<0){
                        String sentence8="ERROR: "+fee+ " is not a non-negative integer, premium fee must be a non-negative integer!";
                        FileOne.writeToFile(args[1], sentence8, true, true);
                        continue;
                    }
                }
                //If condition statement checks error case
                idList.add(voyageId);
                voyage.setVoyageId(voyageId);
                voyage.setVoyagePrice(voyagePrice);
                voyage.setVoyageFrom(voyageFrom);
                voyage.setVoyageTo(voyageTo);
                voyage.setRowNumber(rowNumber);
                //assigned values above for object of voyage and add id list
                if (voyageType.equals("Minibus")) {
                    //assigned values for object of  minibus and write about minibus information
                    Minibus minibus = new Minibus();
                    voyage.setVehicle(minibus);
                    minibus.setRowNumber(rowNumber);
                    minibus.quatoMaker();
                    minibus.setPrice(voyagePrice);
                    String sentence9="Voyage "+voyageId+" was initialized as a minibus (2) voyage from "+voyageFrom+" to "+voyageTo+" with "+minibus.getPrice()+" TL priced "+minibus.getTotalSeat()+" regular seats. Note that minibus tickets are not refundable.";
                    FileOne.writeToFile(args[1], sentence9, true, true);
                } else if (voyageType.equals("Standard")) {
                    //assigned values for object of  Standard and write about Standard information
                    Standard standard = new Standard();
                    int refund=Integer.parseInt(tabLines[7]);
                    voyage.setVehicle(standard);
                    standard.setRowNumber(rowNumber);
                    standard.quatoMaker();
                    standard.setPrice(voyagePrice);
                    standard.setRefund(refund);
                    String sentence10="Voyage "+voyageId+" was initialized as a standard (2+2) voyage from "+voyageFrom+" to "+voyageTo+" with "+standard.getPrice()+" TL priced "+standard.getTotalSeat()+" regular seats. Note that refunds will be "+standard.getRefund()+"% less than the paid amount.";
                    FileOne.writeToFile(args[1], sentence10, true, true);
                } else if (voyageType.equals("Premium")) {
                    //assigned values for object of  Premium and write about Premium information
                    Premium premium = new Premium();
                    int refund=Integer.parseInt(tabLines[7]);
                    int fee=Integer.parseInt(tabLines[8]);
                    voyage.setVehicle(premium);
                    premium.setRowNumber(rowNumber);
                    premium.quatoMaker();
                    premium.setPrice(voyagePrice);
                    premium.setRefund(refund);
                    premium.setPremiumFee(fee);
                    String sentence11="Voyage "+voyageId+" was initialized as a premium (1+2) voyage from "+voyageFrom+" to "+voyageTo+" with "+premium.getPrice()+" TL priced "+(premium.getTotalSeat()-premium.getPremiumSeat())+" regular seats and "+premium.getPremiumPrice()+" TL priced "+premium.getPremiumSeat()+" premium seats. Note that refunds will be "+premium.getRefund()+"% less than the paid amount.";
                    FileOne.writeToFile(args[1], sentence11, true, true);
                }
                voyages.add(voyage);//Voyage is added to voyages
                }}
            else{
                //That one for error message for errorous case
                String sentence12="COMMAND: "+line;
                String sentence13="ERROR: Erroneous usage of \""+createVoyage+"\" command!";
                FileOne.writeToFile(args[1], sentence12, true, true);
                FileOne.writeToFile(args[1], sentence13, true, true);
            }
            }
            /**
 * Process the Z_REPORT command and generate the Z report.
 *
 * @param tabLines    The array containing the command and its parameters.
 * @param outputPath  The path to the output file.
 * @param lines       The lines read from the input file.
 * @param counter     The counter for generating Z report when Z report isn't last.
 * @param idList      The list of voyage IDs.
 * @param voyages     The list of voyage objects.
 */
            else if(createVoyage.equals("Z_REPORT")){
                //It writes about voyages's informaton
               if(tabLines.length==1){
                String zReport="COMMAND: Z_REPORT\nZ Report:\n----------------";
                FileOne.writeToFile(outputPath, zReport, true, true);
               Collections.sort(idList);//ıt Sort 
               int counterId=0;
               for(int id: idList){//It is for writes respectively
                for(Voyage voyage:voyages){
                        if (voyage.getVoyageId()==id){
                            Vehicle vehicle=voyage.getVehicle();
                            if (vehicle instanceof Standard){
                                //It writes about voyage's information
                                voyage.Zreport(outputPath);
                                Standard standard=(Standard) vehicle;
                                standard.Zreport(outputPath);
                                if(lines.length==counter&&idList.size()-1==counterId){
                                    standard.Revenue(outputPath, false);
                                    //It is for last part don't have space
                                }
                                else{
                                    standard.Revenue(outputPath);
                                } 

                            }
                            else if(vehicle instanceof Minibus){
                                //It writes about voyage's information
                                voyage.Zreport(outputPath);
                                Minibus minibus=(Minibus) vehicle;
                                minibus.Zreport(outputPath);
                                if(lines.length==counter&&idList.size()-1==counterId){
                                    minibus.Revenue(outputPath, false);
                                }
                                else{
                                    minibus.Revenue(outputPath);
                                }
                            }
                            else if(vehicle instanceof Premium){
                                //It writes about voyage's information
                                Premium premium=(Premium) vehicle;
                                voyage.Zreport(outputPath);
                                premium.Zreport(outputPath);
                                if(lines.length==counter&&idList.size()-1==counterId){
                                    premium.Revenue(outputPath, false);
                                }
                                else{
                                    premium.Revenue(outputPath);

                                }
                            }
                            
                        }
                    }
                    counterId++;
                }
                if(voyages.isEmpty()){
                    //It is for voyages aren't available case
                    String empty="No Voyages Available!\n----------------";
                    if(lines.length==counter){
                        FileOne.writeToFile(args[1], empty, true, false);
                    }
                    else{
                        FileOne.writeToFile(args[1], empty, true, true);
                    }
                    
                }
            }
            else {
                //It error mesage of print voyage 
                String error="COMMAND: "+line;
                String error1="ERROR: Erroneous usage of \""+createVoyage+"\" command!";
                FileOne.writeToFile(args[1], error,true, true);
                FileOne.writeToFile(args[1], error1, true, true);
            }
            }
            /**
 * Process the SELL_TICKET command, sell tickets, and handle errors.
 *
 * @param tabLines    The array containing the command and its parameters.
 * @param outputPath  The path to the output file.
 * @param lines       The lines read from the input file.
 * @param idList      The list of voyage IDs.
 * @param voyages     The list of voyage objects.
 * @param counter     The counter for generating Z report when Z report isn't last.
 */
            else if(createVoyage.equals("SELL_TICKET")){
                //Sell tickets and check error and writes about information of that
                String command="COMMAND: "+line;
                FileOne.writeToFile(args[1], command, true, true);
                if (tabLines.length==3){
                    if(checkInt(tabLines, outputPath)){
                int voyageId=Integer.parseInt(tabLines[1]);
                if (voyageId<0){         
                    String error3="ERROR: "+voyageId+" is not a positive integer, ID of a voyage must be a positive integer!";
                    FileOne.writeToFile(args[1], error3, true, true);
                    continue;
                }
                else{
                    solution=true;
                    boolean sell=true;
                    boolean breaking=false;
                    for (Voyage voyage:voyages){
                        //It checks voyage id is valid or not
                        if (checkInt(tabLines, outputPath)){
                        if (voyage.checkID(voyageId, voyages)==false){
                            String error5="ERROR: There is no voyage with ID of "+voyageId+"!";
                            FileOne.writeToFile(args[1], error5, true, true);
                            sell=false;
                            breaking=true;
                            break;
                        }}}
                    if (voyages.size()==0){
                        String error5="ERROR: There is no voyage with ID of "+voyageId+"!";
                            FileOne.writeToFile(args[1], error5, true, true);
                            sell=false;
                            breaking=true;
                    }
                    String [] ticketListStrings=tabLines[2].split("_");
                    ArrayList<Integer> ticketList = new ArrayList<>();
                    boolean type=true;
                    for (String ticket: ticketListStrings){
                        if(checkSellInt(ticket, outputPath)&&breaking==false){
                        int ticketNumber = Integer.parseInt(ticket);
                        if (ticketNumber<0){
                            //negative case's error
                            String error4="ERROR: "+ ticketNumber+" is not a positive integer, seat number must be a positive integer!";
                            FileOne.writeToFile(args[1], error4, true, true);
                            solution=false;
                            break;
                        }
                        else{
                            if (ticketList.contains(ticketNumber)&&breaking==false){
                            //It is for ticket number unique case
                            String error4="ERROR: "+ ticketNumber+" is unique, ticket number must be unique";
                            FileOne.writeToFile(args[1], error4, true, true);
                            solution=false;
                            break;
                            }
                            else{
                            ticketList.add(ticketNumber);}
                        }
                    }
                    else{if(breaking==false){
                        String error4="ERROR: "+ ticket +" is not a positive integer, seat number must be a positive integer!";
                        FileOne.writeToFile(outputPath, error4, true, true);
                        solution=false;
                        type=false;
                        break;}
                    }
                    }
                    String orderSeat=tabLines[2].replace("_", "-");
                    for (Voyage voyage:voyages){
                        //It checks voyage id is valid or not
                        if (breaking){
                            break;
                        }
                        if (voyage.getVoyageId()==voyageId){
                            Vehicle vehicle=voyage.getVehicle();
                            if (vehicle instanceof Standard){
                                Standard standard=(Standard) vehicle;
                                if(standard.validTicket(ticketList)){//It checks tickets for a few case
                                if(standard.checkTicket(ticketList)==true){
                                if(solution){
                                    sell=false;
                                    //It makes operation and writes information about that.
                                standard.sellTicket(ticketList);
                                String seat="Seat "+(orderSeat)+" of the Voyage "+voyageId+" from "+voyage.getVoyageFrom()+" to "+voyage.getVoyageTo()+" was successfully sold for "+standard.getTotal()+" TL.";
                                FileOne.writeToFile(args[1], seat, true, true);
                            }}
                                else{
                                    //It is for seats already sold case 
                                    sell=false;
                                    String error6="ERROR: One or more seats already sold!";
                                    FileOne.writeToFile(args[1], error6, true, true);
                                    break;
                                }}
                                else{
                                    //It is for writing seat is valid or not
                                    sell=false;
                                    String error7="ERROR: There is no such a seat!";
                                    FileOne.writeToFile(args[1], error7, true, true);
                                }
                            }
                            else if(vehicle instanceof Minibus){
                                //Same things above
                                Minibus minibus=(Minibus) vehicle;
                                if(minibus.validTicket(ticketList)){
                                if(minibus.checkTicket(ticketList)==true){
                                if(solution){
                                minibus.sellTicket(ticketList);
                                sell=false;
                                String seat2="Seat "+(orderSeat)+" of the Voyage "+voyageId+" from "+voyage.getVoyageFrom()+" to "+voyage.getVoyageTo()+" was successfully sold for "+minibus.getTotal()+" TL.";
                                FileOne.writeToFile(args[1],seat2, true, true);
                            } 
                            }
                            else{
                                sell=false;
                                String error8="ERROR: One or more seats already sold!";
                                FileOne.writeToFile(args[1], error8, true, true);
                                break;
                            }}
                            else{
                                sell=false;
                                String error9="ERROR: There is no such a seat!";
                                FileOne.writeToFile(args[1], error9, true, true);
                            }
                            }
                            else if(vehicle instanceof Premium){
                                //Same things above
                                Premium premium=(Premium) vehicle;
                                if(premium.validTicket(ticketList)){
                                if(premium.checkTicket(ticketList)==true){
                                if(solution){
                                sell=false;
                                premium.sellTicket(ticketList);
                                String seat3="Seat "+(orderSeat)+" of the Voyage "+voyageId+" from "+voyage.getVoyageFrom()+" to "+voyage.getVoyageTo()+" was successfully sold for "+premium.getTotal()+" TL."; 
                                FileOne.writeToFile(args[1], seat3, true, true);
                            }
                              
                            }  
                            else{
                                sell=false;
                                String error10="ERROR: One or more seats already sold!";
                                FileOne.writeToFile(args[1],error10, true, true);

                            }}
                            else{
                                sell=false;
                                String error11="ERROR: There is no such a seat!";
                                FileOne.writeToFile(args[1],error11, true, true);
                            }

                            }
                           
                        }
                    }
                    if (voyages.size()==0){
                        if(sell&&type){
                        //It is for no available voyage case
                        String error12="ERROR: There is no voyage with ID of "+voyageId+"!";
                        FileOne.writeToFile(args[1], error12, true, true);}
                    }
                }
                }
                else{
                    String error14="ERROR: "+tabLines[1]+" is not a positive integer, ID of a voyage must be a positive integer!";
                    FileOne.writeToFile(args[1], error14, true, true);
                    continue;
                }
            }
            else{
                //It is errorneous usage for sell ticket
                String error13="ERROR: Erroneous usage of \""+createVoyage+"\" command!";
                FileOne.writeToFile(args[1], error13,true, true);

            }
            }
            /**
 * Process the REFUND_TICKET command, refund tickets, and handle errors.
 *
 * @param tabLines    The array containing the command and its parameters.
 * @param outputPath  The path to the output file.
 * @param voyages     The list of voyage objects.
 * @param args        The command line arguments passed to the program.
 * @param lines       The lines read from the input file.
 * @param idList      The list of voyage IDs.
 */
            else if(createVoyage.equals("REFUND_TICKET")){
                //That check tickets for a few error and make operation about that
                String command2="COMMAND: "+line;
                FileOne.writeToFile(args[1], command2,true,true);
                if (tabLines.length==3){if(checkInt(tabLines, outputPath)){
                    
                
                int voyageId=Integer.parseInt(tabLines[1]);
                if (voyageId<0){
                    String error14="ERROR: "+voyageId+" is not a positive integer, ID of a voyage must be a positive integer!";
                    FileOne.writeToFile(args[1], error14, true, true);
                    continue;
                }
                
                else{
                    boolean solution1=true;
                    boolean free=true;
                    boolean breaking=false;
                    boolean type=true;
                    for (Voyage voyage:voyages){
                        //ıt checks voyage id, voyage type and if not writes error message
                        if(checkInt(tabLines, outputPath)){
                        if (voyage.checkID(voyageId, voyages)==false){
                            String error15="ERROR: There is no voyage with ID of "+voyageId+"!";
                            FileOne.writeToFile(args[1], error15, true, true);
                            free=false;
                            breaking=true;
                            break;
                        } }
                    }
                        if (voyages.size()==0){
                            String error5="ERROR: There is no voyage with ID of "+voyageId+"!";
                                FileOne.writeToFile(args[1], error5, true, true);
                                free=false;
                                breaking=true;
                        }
                    String [] refundListStrings=tabLines[2].split("_");
                    ArrayList<Integer> refundList = new ArrayList<>();
                    //It check ticket number is positive or not
                    for (String refund: refundListStrings){
                        if(checkSellInt(refund, outputPath)&&breaking==false){
                        int ticketNumber = Integer.parseInt(refund);
                        if (ticketNumber<0){
                            String error14="ERROR: "+ticketNumber+" is not a positive integer, seat number must be a positive integer!";
                            FileOne.writeToFile(args[1], error14, true, true);
                            solution1=false;
                            break;
                        }
                        else{
                            if(refundList.contains(ticketNumber)&&breaking==false){
                            //It is for ticket number unique case
                            String error4="ERROR: "+ ticketNumber+" is unique, ticket number must be unique";
                            FileOne.writeToFile(args[1], error4, true, true);
                            solution1=false;
                            break;
                            }
                            else{
                            refundList.add(ticketNumber);} }
                        }
                        else{
                            if(breaking==false){
                            String error14="ERROR: "+refund+" is not a positive integer, seat number must be a positive integer!";
                            FileOne.writeToFile(outputPath, error14, true, true);
                            solution1=false;
                            type=false;
                            break;}
                        }
                        }
                    String orderSeat1=tabLines[2].replace("_", "-");
                    for (Voyage voyage:voyages){
                        //ıt checks voyage id, voyage type and if not writes error message
                        if (breaking){
                            break;
                        } 
                        if (voyage.getVoyageId()==voyageId){
                            //Voyage  check type ıt is minibus it writes error
                            if(voyage.checkRefund(voyages,voyageId)==false){
                                String error16="ERROR: Minibus tickets are not refundable!";
                                FileOne.writeToFile(args[1], error16, true, true);
                                free=false;
                                break;
                            }
                            Vehicle vehicle=voyage.getVehicle();
                            if (vehicle instanceof Standard){
                                //ıt check tickets condition and make operation .It writes information about that
                                Standard standard=(Standard) vehicle;
                                if(standard.validTicket(refundList)){
                                if(standard.checkRefundTicket(refundList)==true){
                                    if(solution1){
                                        free=false;
                                standard.refundTicket(refundList);
                                String seat4="Seat "+(orderSeat1)+" of the Voyage "+voyageId+" from "+voyage.getVoyageFrom()+" to "+voyage.getVoyageTo()+" was successfully refunded for "+standard.getTotal()+" TL.";
                                FileOne.writeToFile(args[1], seat4,true,true);
                            }                                   }
                                else{
                                    //seat is empty case It writes information about that
                                    free=false;
                                    String error20="ERROR: One or more seats are already empty!";
                                    FileOne.writeToFile(args[1], error20, true, true);
                                    break;
                                }}
                                else{
                                    //Seat number is inlogical case. It writes information about that
                                    free=false;
                                    String error21="ERROR: There is no such a seat!";
                                    FileOne.writeToFile(args[1], error21, true, true);
                                }
                            }
                            else if(vehicle instanceof Premium){
                                //ıt check tickets condition and make operation .It writes information about that
                                Premium premium=(Premium) vehicle;
                                if(premium.validTicket(refundList)){
                                if(premium.checkRefundTicket(refundList)==true){
                                if(solution1){
                                    free=false;
                                premium.refundTicket(refundList);
                                
                                String seat5="Seat "+(orderSeat1)+" of the Voyage "+voyageId+" from "+voyage.getVoyageFrom()+" to "+voyage.getVoyageTo()+" was successfully refunded for "+premium.getTotal()+" TL.";
                                FileOne.writeToFile(args[1], seat5, true, true);
                                }  
                            }
                            else{
                                free=false;
                                String error25="ERROR: One or more seats are already empty!";
                                FileOne.writeToFile(args[1], error25, true, true);
                                break;}
                            }
                            else{
                                free=false;
                                String error26="ERROR: There is no such a seat!";
                                FileOne.writeToFile(args[1], error26,true,true);
                            }

                            }
                        }
                        
                    }
                    if (voyages.size()==0){
                        if(free&&type){
                        //It is for no voyage . Id isn't found.
                        String error48="ERROR: There is no voyage with ID of "+voyageId+"!";
                        FileOne.writeToFile(outputPath, error48,true, true);
                        }
                    }
                }
                }
                else{
                    String error14="ERROR: "+tabLines[1]+" is not a positive integer, ID of a voyage must be a positive integer!";
                    FileOne.writeToFile(args[1], error14, true, true);
                    continue;
                }
            }
            else{
                //It errorneous usage of refund
                String error30="ERROR: Erroneous usage of \""+createVoyage+"\" command!";
                FileOne.writeToFile(args[1], error30, true, true);
            }
            }
/**
 * Process the CANCEL_VOYAGE command, cancel voyages, and handle errors.
 *
 * @param tabLines    The array containing the command and its parameters.
 * @param outputPath  The path to the output file.
 * @param voyages     The list of voyage objects.
 * @param args        The command line arguments passed to the program.
 * @param lines       The lines read from the input file.
 * @param idList      The list of voyage IDs.
 */
            else if(createVoyage.equals("CANCEL_VOYAGE")){
                String command15="COMMAND: "+line;
                FileOne.writeToFile(args[1], command15, true, true);
                if(tabLines.length==2){
                    //It check type condition,voyage id is valid or not and make operation about cancel
                if(checkInt(tabLines, outputPath)){
                int voyageId=Integer.parseInt(tabLines[1]);
                if(voyageId<0){
                    String error40="ERROR: "+voyageId+" is not a positive integer, ID of a voyage must be a positive integer!";
                    FileOne.writeToFile(outputPath, error40, true, true);
                    continue;

                }
                else{
                    boolean this1=true;
                    for (Voyage voyage:voyages){
                        //It check id and if not ıt writes error to the file
                        if (voyage.checkID(voyageId, voyages)==false){
                            String error41="ERROR: There is no voyage with ID of "+voyageId+"!";
                            FileOne.writeToFile(outputPath, error41,true,true);
                            this1=false;
                            break;
                        }
                        if (voyage.getVoyageId()==voyageId){
                            Vehicle vehicle=voyage.getVehicle();
                            if (vehicle instanceof Premium){
                                //It make operation cancel and writes about information about that
                                Premium premium=(Premium) vehicle;
                                premium.cancel();
                                String error42="Voyage "+voyage.getVoyageId()+" was successfully cancelled!";
                                FileOne.writeToFile(outputPath, error42, true, true);
                                voyage.Cancel(outputPath);
                                premium.Zreport(outputPath);
                                premium.Cancel(outputPath);
                                idList.remove(Integer.valueOf(voyageId));
                                voyages.remove(voyage);
                                this1=false;
                                break;
                            }
                            else if (vehicle instanceof Minibus ){
                                //It make operation cancel and writes about information about that
                                Minibus minibus=(Minibus) vehicle;
                                minibus.cancel();
                                String error46="Voyage "+voyage.getVoyageId()+" was successfully cancelled!";
                                FileOne.writeToFile(outputPath, error46,true,true);
                                voyage.Cancel(outputPath);
                                minibus.Zreport(outputPath);
                                minibus.Cancel(outputPath);
                                idList.remove(Integer.valueOf(voyageId));
                                voyages.remove(voyage);
                                this1=false;
                                break;
                            }
                            else if(vehicle instanceof Standard){
                                //It make operation cancel and writes about information about that
                                Standard standard=(Standard) vehicle;
                                standard.cancel();
                                String error47="Voyage "+voyage.getVoyageId()+" was successfully cancelled!";
                                FileOne.writeToFile(outputPath, error47,true, true);
                                voyage.Cancel(outputPath);
                                standard.Zreport(outputPath);
                                standard.Cancel(outputPath);
                                idList.remove(Integer.valueOf(voyageId));
                                voyages.remove(voyage);
                                this1=false;
                                break;
                            }
                        }
                    }
                    if (voyages.size()==0){
                        if(this1){
                            //It is for no voyage case
                        String error48="ERROR: There is no voyage with ID of "+voyageId+"!";
                        FileOne.writeToFile(outputPath, error48,true, true);}
                    }
                    
                }
                }
                else{
                    //type is not correct . It writes error like that
                    String error49="ERROR: "+tabLines[1]+" is not a positive integer, ID of a voyage must be a positive integer!";
                    FileOne.writeToFile(outputPath,error49,true,true);
                    continue;
                }
            }
            else{
                //It is errorneous usage of cancel
                String error49="ERROR: Erroneous usage of \""+createVoyage+"\" command!";
                FileOne.writeToFile(outputPath,error49,true,true);

            }
        }
        /**
 * Process the PRINT_VOYAGE command, print voyage information, and handle errors.
 *
 * @param tabLines    The array containing the command and its parameters.
 * @param outputPath  The path to the output file.
 * @param voyages     The list of voyage objects.
 * @param args        The command line arguments passed to the program.
 * @param lines       The lines read from the input file.
 * @param idList      The list of voyage IDs.
 */
            else if(createVoyage.equals("PRINT_VOYAGE")){
                //It check type condition,voyage id is valid or not and make operation about Print
                String commmand10="COMMAND: "+line;
                FileOne.writeToFile(outputPath, commmand10, true, true);
                if (tabLines.length==2){
                if(checkInt(tabLines, outputPath)){
                int voyageId=Integer.parseInt(tabLines[1]);
                if(voyageId<0){
                    String error49="ERROR: "+voyageId+" is not a positive integer, ID of a voyage must be a positive integer!";
                    FileOne.writeToFile(outputPath,error49,true,true);
                    continue;
                }
                else{
                    boolean that=false;
                    for (Voyage voyage:voyages){
                        if (voyage.checkID(voyageId, voyages)==false){
                            String error50="ERROR: There is no voyage with ID of "+voyageId+"!";
                            FileOne.writeToFile(outputPath,error50,true, true);
                            break;
                        }
                        if (voyage.getVoyageId()==voyageId){
                            Vehicle vehicle=voyage.getVehicle();
                            if (vehicle instanceof Premium){
                                //It make operation cancel and writes about information about that
                                Premium premium=(Premium) vehicle;
                                voyage.Print(outputPath);
                                premium.Zreport(outputPath);
                                premium.Print(outputPath);
                                that=true;
                            }
                            else if (vehicle instanceof Minibus ){
                                //It make operation cancel and writes about information about that
                                Minibus minibus=(Minibus) vehicle;
                                voyage.Print(outputPath);
                                minibus.Zreport(outputPath);
                                minibus.Print(outputPath);
                                that=true;

                                
                            }
                            else if(vehicle instanceof Standard){
                                //It make operation cancel and writes about information about that
                                Standard standard=(Standard) vehicle;
                                voyage.Print(outputPath);
                                standard.Zreport(outputPath);
                                standard.Print(outputPath);
                                that=true;
                            }
                        }
                    }
                    if (voyages.size()==0){
                        if(that==false){
                            //It is for no voyage case and writes information
                        String error51="ERROR: There is no voyage with ID of "+voyageId+"!";
                        FileOne.writeToFile(outputPath,error51, true, true);}
                    }

                }
            }
            else{
                //It is for type error case .It writes error like this
                String error49="ERROR: "+tabLines[1]+" is not a positive integer, ID of a voyage must be a positive integer!";
                FileOne.writeToFile(outputPath,error49,true,true);
                continue;
            }
                }
                else{
                    //It writes information errorneous usage if length isn't appropiate 
                    String error52="ERROR: Erroneous usage of \""+createVoyage+"\" command!";
                    FileOne.writeToFile(outputPath,error52,true,true);
                }
       

            }
            else{
                //It command name isn't valid. It writes error like this
                String command7="COMMAND: "+line;
                FileOne.writeToFile(outputPath,command7,true,true);
                String error53="ERROR: There is no command namely "+createVoyage+"!";
                FileOne.writeToFile(outputPath, error53, true, true);
                
            }
            
            }
            if (lines.length==0){
                //It is empty txt case
                String zReport1="Z Report:\n----------------";
                FileOne.writeToFile(outputPath, zReport1, true, true);
                String empty="No Voyages Available!\n----------------";
                    if(lines.length==counter){
                        FileOne.writeToFile(args[1], empty, true, false);
                    }
                    else{
                        FileOne.writeToFile(args[1], empty, true, true);
                    }
            }
            else if(!lines[lines.length-1].equals("Z_REPORT")||lines.length==0){
                //It is for when last part is not Z report case . It writes z report
                String zReport1="Z Report:\n----------------";
                FileOne.writeToFile(outputPath, zReport1, true, true);
               Collections.sort(idList);
               int counterId=0;
               for(int id: idList){
                for(Voyage voyage:voyages){
                    //It writes information about voyages
                        if (voyage.getVoyageId()==id){
                            Vehicle vehicle=voyage.getVehicle();
                            if (vehicle instanceof Standard){
                                voyage.Zreport(outputPath);
                                Standard standard=(Standard) vehicle;
                                standard.Zreport(outputPath);
                                if(lines.length==counter&&idList.size()-1==counterId){
                                    standard.Revenue(outputPath, false);
                                }
                                else{
                                    standard.Revenue(outputPath);

                                } 

                            }
                            else if(vehicle instanceof Minibus){
                                voyage.Zreport(outputPath);
                                Minibus minibus=(Minibus) vehicle;
                                minibus.Zreport(outputPath);
                                if(lines.length==counter&&idList.size()-1==counterId){
                                    minibus.Revenue(outputPath, false);
                                }
                                else{
                                    minibus.Revenue(outputPath);
                                }
                            }
                            else if(vehicle instanceof Premium){
                                Premium premium=(Premium) vehicle;
                                voyage.Zreport(outputPath);
                                premium.Zreport(outputPath);
                                if(lines.length==counter&&idList.size()-1==counterId){
                                    premium.Revenue(outputPath, false);
                                }
                                else{
                                    premium.Revenue(outputPath);

                                }
                            }
                            
                        }
                    }
                    counterId++;
                }
                if(voyages.isEmpty()){
                    //It is for no voyage case
                    String empty="No Voyages Available!\n----------------";
                    if(lines.length==counter){
                        FileOne.writeToFile(args[1], empty, true, false);
                    }
                    else{
                        FileOne.writeToFile(args[1], empty, true, true);
                    }
                }
            }
        }
        /**
 * Process the tab line, checking its components and writing errors if any.
 * 
 * @param tabLines    The array containing the tab-separated line's components.
 * @param outputPath  The path to the output file.
 * @return            True if the tab line is valid, false otherwise.
 */
        public static boolean processTabLine(String[] tabLines, String outputPath) {
            //It checks type of object It writes error for that
            try {
                String voyageType = tabLines[1];
            } catch (Exception e) {
                String errorType="ERROR: Erroneous usage of \""+"INIT_VOYAGE"+"\" command!";
                FileOne.writeToFile(outputPath, errorType, true, true);
                return false;
            }
          
            try {
                int voyageId = Integer.parseInt(tabLines[2]);
            } catch (NumberFormatException e) {
                String errorType = "ERROR: "+tabLines[2]+" is not a positive integer, ID of a voyage must be a positive integer!";
                FileOne.writeToFile(outputPath, errorType, true, true);
                return false;
            }
            try {
                int rowNumber = Integer.parseInt(tabLines[5]);
            } catch (NumberFormatException e) {
                String errorType = "ERROR: "+tabLines[5]+" is not a positive integer, number of seat rows of a voyage must be a positive integer!";
                FileOne.writeToFile(outputPath, errorType, true, true);
                return false;
            }
        
            try {
                float voyagePrice = Float.parseFloat(tabLines[6]);
            } catch (NumberFormatException e) {
                String errorType = "ERROR: " +tabLines[6]+" is not a positive number, price must be a positive number!";
                FileOne.writeToFile(outputPath, errorType, true, true);
                return false;
            }
            String voyageType=tabLines[1];
            if(voyageType.equals("Minibus")&&tabLines.length==7){
                return true;
            }
            else if(voyageType.equals("Standard")&&tabLines.length==8){
                try{
                    int refund=Integer.parseInt(tabLines[7]);
                }
                catch(NumberFormatException e){
                    String errorType="ERROR: "+tabLines[7]+" is not an integer that is in range of [0, 100], refund cut must be an integer that is in range of [0, 100]!";
                    FileOne.writeToFile(outputPath, errorType, true, true);
                    return false;
                }
                return true;
            }
            else if(voyageType.equals("Premium")&&tabLines.length==9){
                try{
                    int refund=Integer.parseInt(tabLines[7]);
                }
                catch(NumberFormatException e){
                    String errorType="ERROR: "+tabLines[7]+" is not an integer that is in range of [0, 100], refund cut must be an integer that is in range of [0, 100]!";
                    FileOne.writeToFile(outputPath, errorType, true, true);
                    return false;
                }
                try{
                    int fee=Integer.parseInt(tabLines[8]);
                }
                catch(NumberFormatException e){
                    String errorType="ERROR: "+tabLines[8]+" is not a non-negative integer, premium fee must be a non-negative integer!";
                    FileOne.writeToFile(outputPath, errorType, true, true);
                    return false;
                }
                return true;
            }
            else{
                String errorType="ERROR: Erroneous usage of \""+"INIT_VOYAGE"+"\" command!";
                FileOne.writeToFile(outputPath, errorType, true, true);
                return false;
            }
        }
/**
 * Check if the given string can be parsed into an integer.
 * 
 * @param tabLines    The array containing the tab-separated line's components.
 * @param outputPath  The path to the output file.
 * @return            True if the string can be parsed into an integer, false otherwise.
 */
        public static boolean checkInt(String[] tabLines,String outputPath) {
            //It checks type of ıd
            try {
                int voyageId=Integer.parseInt(tabLines[1]);
            }
            catch (NumberFormatException e){
                return false;
            }
            return true;
        }
/**
 * Check if the given string can be parsed into an integer for selling tickets.
 * 
 * @param word        The string to check.
 * @param outputPath  The path to the output file.
 * @return            True if the string can be parsed into an integer, false otherwise.
 */
        public static boolean checkSellInt(String word,String outputPath) {
            //It checks type of ıd
            try {
                int ticketNumber=Integer.parseInt(word);
            }
            catch (NumberFormatException e){
                return false;
            }
            return true;
        }
       
    }
/**
 * The Voyage class represents a voyage with its attributes such as ID, origin, destination, price, and row number.
 * It provides methods for setting and getting these attributes, as well as for generating reports and printing voyage details.
 */
class Voyage implements Zreport  {
    private int voyageId;
    private String voyageFrom;
    private String voyageTo;
    private float voyagePrice;
    private int rowNumber;
    private ArrayList<Voyage> voyages;
    private Vehicle vehicle;
    //encapsulation by set and get methods
    public void setVoyageId(int voyageId) {
        this.voyageId = voyageId;
    }
    /**
     * Checks if a voyage ID exists in the list of voyages.
     *
     * @param voyageId The voyage ID to check.
     * @param voyages  The list of voyages to search in.
     * @return True if the voyage ID exists in the list, otherwise false.
     */
    public boolean checkID(int voyageId,ArrayList<Voyage> voyages){
        for (Voyage voyage:voyages){
            if (voyage.getVoyageId()==voyageId){
                return true;
            }
        }
        return false;
    }
     /**
     * Checks if a voyage is refundable.
     *
     * @param voyages     The list of voyages to search in.
     * @param checkNumber The voyage ID to check.
     * @return True if the voyage is refundable, otherwise false.
     */
    public boolean checkRefund(ArrayList<Voyage> voyages,int checkNumber){
        for (Voyage voyage:voyages){
            if ((voyage.getVoyageId()==checkNumber)){
            Vehicle vehicle=voyage.getVehicle();
            if (vehicle instanceof Premium){
                return true;
            }
            else if(vehicle instanceof Standard){
                return true;
            }
            else if(vehicle instanceof Minibus){
                return false;
            }
        }}
        return false;
    }
    public int getVoyageId() {
        return voyageId;
    }
    public void setVoyageFrom(String voyageFrom) {
        this.voyageFrom = voyageFrom;
    }
    public String getVoyageFrom() {
        return voyageFrom;
    }

    public void setVoyageTo(String voyageTo) {
        this.voyageTo = voyageTo;
    }

    public String getVoyageTo() {
        return voyageTo;
    }

    public void setVoyagePrice(float voyagePrice) {
        this.voyagePrice = voyagePrice;
    }

    public float getVoyagePrice() {
        return voyagePrice;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public int getRowNumber() {
        return rowNumber;
    }
   
    public void setVehicle(Vehicle vehicle){
        this.vehicle=vehicle;
    }
    public Vehicle getVehicle(){
        return vehicle;
    }
    /**
     * Generates a Z report for the voyage.
     *
     * @param args The output path to write the report.
     */
    public void Zreport(String args){
        //It writes own z_report
        String allSentence=String.format("Voyage %d\n%s-%s",voyageId,voyageFrom,voyageTo);
        FileOne.writeToFile(args, allSentence, true, true);
    }
    /**
     * Prints voyage details.
     *
     * @param args The output path to write the details.
     */
    public void Print(String args){
        String allSentence1=String.format("Voyage %d\n%s-%s",voyageId,voyageFrom,voyageTo);
        FileOne.writeToFile(args, allSentence1, true, true);
    }
    /**
     * Writes voyage cancellation details.
     *
     * @param args The output path to write the cancellation details.
     */
   public void Cancel(String args){
    //It writes own cancel
    String allSentence2=String.format("Voyage details can be found below:\nVoyage %d\n%s-%s",voyageId,voyageFrom,voyageTo);
    FileOne.writeToFile(args, allSentence2, true, true);
   }
    public void Revenue(String output) {   
    }
    public void Revenue(String output,boolean last ) {
    }
    //for override
}
class Vehicle extends Voyage implements Zreport{
    public void Revenue(String output) {
    }
    public void Revenue(String output,boolean last ) {
    }
    //for override
}
/**
 * The Standard class represents a standard type of vehicle for voyages.
 * It extends the Vehicle class and implements Zreport.
 */
class Standard extends Vehicle implements Zreport{
    private String[] quato;
    private int row;
    private float price;
    private int refund;
    private float revenue=0.00f;
    private int totalSeat;
    private float total;
    //encapsulation by set and get methods
    public void setRevenue(float revenue){
        this.revenue=revenue;
    }
    public void setPrice(float price){
        this.price=price;
    }
    public void setRefund(int refund){
        this.refund=refund;
    }
    public void setRowNumber(int row){
        this.row=row;
    }
     /**
     * Generates the seating quota for the standard vehicle.
     */
    public void quatoMaker() {
        //Assign revenue value
        quato = new String[row * 4];
        for (int i = 0; i < quato.length; i++) {
            quato[i] = "*";
        }
        this.quato=quato;
        this.totalSeat=quato.length;
        revenue=0.0f;
        this.revenue=revenue;
    }
    @Override
    public void Zreport(String args) {
        //ıt writes qutos 
        for (int i = 0; i < quato.length; i++) {
            FileOne.writeToFile(args,quato[i],true,false);
            if (i % 4 != 3) {
                FileOne.writeToFile(args," ",true,false);
            } else if (i % 4 == 3) {
                FileOne.writeToFile(args,"", true, true);
            }
            if (i % 4 == 1) {
                FileOne.writeToFile(args,"| ",true,false);
            }
        }
    }
    @Override
    public void Revenue(String args){
        // Implementation to write revenue to file
        String revenue1="Revenue: "+String.format("%.2f",revenue)+"\n----------------";
        FileOne.writeToFile(args, revenue1, true, true);
    }
     /**
     * Overloaded method to write revenue to file, specifying if it's the last entry.
     *
     * @param args  The output path to write the revenue.
     * @param found Indicates if it's the last entry.
     */
    public void Revenue(String args,boolean found){
        String revenue1="Revenue: "+String.format("%.2f",revenue)+"\n----------------";
        FileOne.writeToFile(args, revenue1, true, found);
    }
    public int getTotalSeat(){
        return totalSeat;
    }
    public int getRefund(){
        return refund;
    }
    public String getPrice(){
        return String.format("%.2f", price);
        
    }
      /**
     * Checks if the tickets are valid for the standard vehicle.
     *
     * @param tickets The list of tickets to be checked.
     * @return True if tickets are valid, otherwise false.
     */
    public boolean checkTicket(ArrayList<Integer> tickets){
        boolean solution=true;
        //It check seats empty or not
        for(int ticket: tickets){
            if (quato[ticket-1]=="*"){
                solution=true;
            }
            else if(quato[ticket-1]=="X"){
                solution=false;
                break;
            }
        }
        return solution;
    }
    public void sellTicket(ArrayList<Integer> tickets){
        //It calculate tickets money
        //update revenue
        int many=0;
        for(int ticket: tickets){
            if(quato[ticket-1]=="*"){
                quato[ticket-1]="X";
                many+=1;
            }
        }
        total=many*price;
        this.total=total;
        this.revenue+=total;
    }
    public String getTotal(){
        return String.format("%.2f",total);
    }
    public boolean checkRefundTicket(ArrayList<Integer> tickets){
        boolean solution=true;
        //It check seats full or not
        for(int ticket: tickets){
            if (quato[ticket-1]=="X"){
                solution=true;
            }
            else if(quato[ticket-1]=="*"){
                solution=false;
                break;
            }
        }
        return solution;
    }
    public void refundTicket(ArrayList<Integer> tickets){
        //ıt calcuates refund money and update revenue
        int many=0;
        for(int ticket: tickets){
            if(quato[ticket-1]=="X"){
                quato[ticket-1]="*";
                many+=1;
            }
        }
        total=many*price*((100-refund)/100.00f);
        this.total=total;
        this.revenue-=total;
    }
       /**
     * Cancels tickets for the standard vehicle.
     */
    public void cancel(){
        //It update revenue for last case
        int many=0;
        for (int i=0;i<quato.length;i++){
            if (quato[i]=="X"){
                many+=1;

            }
        }
        total=many*price;
        this.total=total;
        this.revenue-=total;
    }
    /**
 * Checks if the ticket numbers are valid for the vehicle.
 *
 * @param tickets The list of tickets to be checked.
 * @return True if tickets are valid, otherwise false.
 */
    public boolean validTicket(ArrayList<Integer> tickets){
        //It tickets number logic or not
        for(int ticket: tickets){
            if (ticket>quato.length){
                return false;
            }
        }
        return true;
    }
      /**
     * Writes cancellation revenue for the standard vehicle.
     *
     * @param args The output path to write the revenue details.
     */
    public void Cancel(String args){
        String revenue10="Revenue: "+String.format("%.2f",revenue);
        FileOne.writeToFile(args, revenue10, true, true);
    }
     /**
     * Writes printing revenues for the standard vehicle.
     *
     * @param args The output path to write the printing details.
     */
    public void Print(String args){
        String revenue11="Revenue: "+String.format("%.2f",revenue);
        FileOne.writeToFile(args, revenue11, true, true);
    }
}
/**
 * Represents a premium vehicle with special seating arrangement.
 */
class Premium extends Vehicle implements Zreport {
    private int seatsInRow = 3;
    private String[] quato;
    private int row;
    private float price;
    private int refund;
    private float revenue=0.0f;
    private int premiumFee;
    private float premiumPrice;
    private int premiumSeat;
    private int totalSeat;
    private float total;
    //It use encapsulation method by set and get
    public void setRowNumber(int row){
        this.row=row;
    }
    public void setPrice(float price){
        this.price=price;
    }
    public void setRefund(int refund){
        this.refund=refund;
    }
    public String getPrice(){
        return String.format("%.2f", price);
    }
    public void setPremiumFee(int premiumFee){
        this.premiumFee=premiumFee;
    }
    /**
     * Initializes the seating arrangement for the premium vehicle and calculates premium seats.
     * Updates revenue to zero.
     */
    public void quatoMaker() {
        //It makes quato It calculate premium seat. update revenue
        quato = new String[row * seatsInRow];
        for (int i = 0; i < quato.length; i++) {
            quato[i] = "*";
        }
        this.quato=quato;
        this.totalSeat=quato.length;
        for (int i=0;i<quato.length;i++){
            if (i%3==0){
                premiumSeat+=1;
            }
        }
        this.premiumSeat=premiumSeat;
        revenue=0.0f;
        this.revenue=revenue;
    }
      /**
     * Writes the seating arrangement of the premium vehicle.
     * Each row is separated by a newline character and seats are separated by spaces.
     * Premium seats are indicated by "|" symbols.
     *
     * @param args The file path to write the seating arrangement.
     */
    @Override
    public void Zreport(String args) {
        //It writes quato
        for (int i = 0; i < quato.length; i++) {
            FileOne.writeToFile(args,quato[i],true,false);
            if (i % 3 != 2) {
            FileOne.writeToFile(args," ",true,false);
            } else if (i % 3 == 2) {
                FileOne.writeToFile(args,"", true, true);
            }
            if (i % 3 == 0) {
                FileOne.writeToFile(args,"| ", true, false);
            }
        }
    }
     /**
     * Writes the revenue information of the premium vehicle to a file.
     *
     * @param args The file path to write the revenue information.
     */
    @Override
    public void Revenue(String args){
        // It writes revenue
        String revenue3="Revenue: "+String.format("%.2f",revenue)+"\n----------------";
        FileOne.writeToFile(args, revenue3, true, true);
    }
     /**
     * Writes the revenue information of the premium vehicle to a file.
     * If it is the last revenue entry, a newline is not added at the end.
     *
     * @param args  The file path to write the revenue information.
     * @param found True if it is the last revenue entry, otherwise false.
     */
    public void Revenue(String args,boolean found){
        //ıt writes when it is last
        String revenue1="Revenue: "+String.format("%.2f",revenue)+"\n----------------";
        FileOne.writeToFile(args, revenue1, true, found);
    }
    public int getTotalSeat(){
        
        return totalSeat;
    }
    public int getPremiumSeat(){
        return premiumSeat;
    }
    public int getPremiumFee(){
        return premiumFee;
    }
    public String getPremiumPrice(){
        premiumPrice= (float) price*(((float)premiumFee+100)/100.00f);
        this.premiumPrice=premiumPrice;
        return String.format("%.2f", premiumPrice);
    }
    public int getRefund(){
        return refund;
    }
        /**
     * Checks if the specified seats are empty or not.
     *
     * @param tickets The list of ticket numbers to check.
     * @return True if all specified seats are empty, otherwise false.
     */
    public boolean checkTicket(ArrayList<Integer> tickets){
        //It checks seats are empty or not
        boolean solution=true;
        for(int ticket: tickets){
            if (quato[ticket-1]=="*"){
                solution=true;
            }
            else if(quato[ticket-1]=="X"){
                solution=false;
                break;
            }
        }
        return solution;
    }
     /**
     * Sells tickets for the specified seats, calculates the total price, and updates the revenue.
     *
     * @param tickets The list of ticket numbers to sell.
     */
    public void sellTicket(ArrayList<Integer> tickets){
        //ıt calculate total price and update revenue
        int many=0;
        int manyPremium=0;
        
        for(int ticket: tickets){
            if(ticket%3!=1){
                quato[ticket-1]="X";
                many+=1;
            }
            else if(ticket%3==1){
                quato[ticket-1]="X";
                manyPremium+=1;
            }
        }
        total=(many*price+manyPremium*premiumPrice);
        this.total=total;
        this.revenue+=total;
    }
    public String getTotal(){
        return String.format("%.2f",total);
    }
     /**
     * Checks if the specified seats are full or not.
     *
     * @param tickets The list of ticket numbers to check.
     * @return True if all specified seats are full, otherwise false.
     */
    public boolean checkRefundTicket(ArrayList<Integer> tickets){
        boolean solution=true;
        for(int ticket: tickets){
            if (quato[ticket-1]=="X"){
                solution=true;
            }
            else if(quato[ticket-1]=="*"){
                solution=false;
                break;
            }
        }
        return solution;
    }
      /**
     * Refunds tickets for the specified seats, calculates the refund price, and updates the revenue.
     *
     * @param tickets The list of ticket numbers to refund.
     */
    public void refundTicket(ArrayList<Integer> tickets){
        int many=0;
        int manyPremium=0;
        for(int ticket: tickets){
            if(ticket%3!=1){
                quato[ticket-1]="*";
                many+=1;
            }
            else if(ticket%3==1){
                quato[ticket-1]="*";
                manyPremium+=1;
            }
        }
        total=(many*price*((100-refund)/100.00f)+manyPremium*premiumPrice*(100-refund)/100.00f);
        this.total=total;
        this.revenue-=total;
    }
       /**
     * Cancels the ticket sales, updates the revenue, and calculates the total.
     */
    public void cancel(){
        //ıt is for cancel case update revenue calculate total
        int many=0;
        int manyPremium=0;
        for(int i=0;i<quato.length;i++){
            if((i%3==0)&&(quato[i]=="X")){
                manyPremium+=1;
            }
            else if((i%3!=0)&&quato[i]=="X"){
                many+=1;
            }
        }
        total=(many*price+manyPremium*premiumPrice);
        this.total=total;
        this.revenue-=total;
    }
     /**
     * Checks if the specified tickets are valid or not.
     *
     * @param tickets The list of ticket numbers to check.
     * @return True if all specified tickets are valid, otherwise false.
     */
    public boolean validTicket(ArrayList<Integer> tickets){
        //It check ticket is logic or not
        for(int ticket: tickets){
            if (ticket>quato.length){
                return false;
            }
        }
        return true;
    }
    /**
     * Writes cancel revenue information to a file.
     *
     * @param args The file path to write the revenue information.
     */
    public void Cancel(String args){
        //It writes cancel revenue
        String revenue12="Revenue: "+String.format("%.2f",revenue);
        FileOne.writeToFile(args, revenue12, true, true);
    }
     /**
     * Writes print revenue information to a file.
     *
     * @param args The file path to write the revenue information.
     */
    public void Print(String args){
        //It writes print revenue
        String revenue13="Revenue: "+String.format("%.2f",revenue);
        FileOne.writeToFile(args, revenue13, true, true);
    }
}

class Minibus extends Vehicle implements Zreport {
    private int seatsInRow = 2;
    private String[] quato;
    private int row;
    private float price;
    private int totalSeat;
    private float total;
    private float revenue;
    //It use set ve get for encapsulation
    public void setRowNumber(int row){
        this.row=row;
    }
     /**
     * Generates the seating arrangement for the Minibus.
     */
    public void quatoMaker() {
        //That one make array for quato
        quato = new String[row * seatsInRow];
        for (int i = 0; i < quato.length; i++) {
            quato[i] = "*";
        }
        this.quato=quato;
        this.totalSeat=quato.length;
        revenue=0.0f;
        this.revenue=revenue;
    }
      /**
     * Generates the Z report for the Minibus, writing the row information to a file.
     * 
     * @param args The file path to write the report.
     */
    @Override
    public void Zreport(String args) {
        //That one override . It writes row for minibus
        for (int i = 0; i < quato.length; i++) {
                FileOne.writeToFile(args,quato[i],true,false);
            if (i % 2 != 1) {
                FileOne.writeToFile(args," ",true,false);
            } else if (i % 2 == 1) {
                FileOne.writeToFile(args, "", true, true);
            }
        }
    }
    public int getTotalSeat(){
        return totalSeat;
    }
    public void setPrice(float price){
        this.price=price;
    }
    public String getPrice(){
        return String.format("%.2f", price);
    }
     /**
     * Writes revenue information to a file.
     * 
     * @param args The file path to write the revenue information.
     */
    @Override
    public void Revenue(String args){
        //It writes information about revenue
        String revenue3="Revenue: "+String.format("%.2f",revenue)+"\n----------------";
        FileOne.writeToFile(args, revenue3, true, true);
    }
     /**
     * Writes revenue information to a file, with the option to indicate if it's the last part of the report.
     * 
     * @param args  The file path to write the revenue information.
     * @param found True if it's the last part of the report, otherwise false.
     */
    public void Revenue(String args,boolean found){
        //It writes information about revenue.Last part wouldn't have sentence case
        String revenue1="Revenue: "+String.format("%.2f",revenue)+"\n----------------";
        FileOne.writeToFile(args, revenue1, true, found);
    }
       /**
     * Checks if the specified seats are empty or not.
     * 
     * @param tickets The list of ticket numbers to check.
     * @return True if all specified seats are empty, otherwise false.
     */
    public boolean checkTicket(ArrayList<Integer> tickets){
        //It checks seat is appropiate or not
        boolean solution=true;
        for(int ticket: tickets){
            if (quato[ticket-1]=="*"){
                solution=true;
            }
            else if(quato[ticket-1]=="X"){
                solution=false;
                break;
            }
        }
        return solution;
    }
     /**
     * Sells tickets for the specified seats, calculates the total price, and updates the revenue.
     * 
     * @param tickets The list of ticket numbers to sell.
     */
    public void sellTicket(ArrayList<Integer> tickets){
        //It calcuate price of that sale and that one total
        //It update revenue
        int many=0;
        for(int ticket: tickets){
            if(quato[ticket-1]=="*"){
                quato[ticket-1]="X";
                many+=1;
            }
        }
        total=many*price;
        this.total=total;
        this.revenue+=total;
    }
    public String getTotal(){
        return String.format("%.2f",total);
    }
      /**
     * Cancels the ticket sales, updates the revenue, and calculates the total.
     */
    public void cancel(){
        int many=0;
        for (int i=0;i<quato.length;i++){
            if (quato[i]=="X"){
                many+=1;
            }
        }
        total=many*price;
        this.total=total;
        this.revenue-=total;
    }
      /**
     * Checks if the specified tickets are valid or not.
     * 
     * @param tickets The list of ticket numbers to check.
     * @return True if all specified tickets are valid, otherwise false.
     */
    public boolean validTicket(ArrayList<Integer> tickets){
        //It checks tickets are valid or not
        for(int ticket: tickets){
            if (ticket>quato.length){
                return false;
            }
        }
        return true;
    }
    /**
     * Writes cancel revenue information to a file.
     * 
     * @param args The file path to write the revenue information.
     */
    public void Cancel(String args){
        //It is revenue for Cancel case
        String revenue14="Revenue: "+String.format("%.2f",revenue);
        FileOne.writeToFile(args, revenue14, true, true);
    }
       /**
     * Writes print revenue information to a file.
     * 
     * @param args The file path to write the revenue information.
     */
    public void Print(String args){
        //It is revenue for Print case
        String revenue15="Revenue: "+String.format("%.2f",revenue);
        FileOne.writeToFile(args, revenue15, true, true);
    }
}
interface Zreport{
    //that one interface
    void Zreport(String output);
    void Revenue(String output);
    void Revenue(String output,boolean last);
    void Print(String output);
    void Cancel(String output);
}
class FileOne{
    // Reads the file and returns its content as an array
    public static String[] readFile(String path, boolean discardEmptyLines, boolean trim) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(path)); // Gets the content of the file as a list
            if (discardEmptyLines) { // Removes lines that are empty with respect to trimming
                lines.removeIf(line -> line.trim().equals(""));
            }
            if (trim) { // Trims each line
                lines.replaceAll(String::trim);
            }
            return lines.toArray(new String[0]);
        } catch (IOException e) { // Returns null if the file is not found
            
            System.err.println("ERROR: This program cannot read from the \"" + path + "\", either this program does not have read permission to read that file or file does not exist. Program is going to terminate!");
            System.exit(1);
            return null;
        }
        catch(Exception e){
            System.err.println("ERROR: This program cannot read from the \"" + path + "\", either this program does not have read permission to read that file or file does not exist. Program is going to terminate!");
            System.exit(1);
            return null;
        }
    }

    // Writes content to the specified path
    public static void writeToFile(String path, String content, boolean append, boolean newLine) {
        PrintStream ps = null;
        try {
            ps = new PrintStream(new FileOutputStream(path, append));
            ps.print(content + (newLine ? "\n" : ""));
        } catch (IOException e) {
            System.err.println("ERROR: This program cannot write to the \""+path+"\", please check the permissions to write that directory. Program is going to terminate!");
            System.exit(1);
        }
        catch(Exception e){
            System.err.println("ERROR: This program cannot write to the \""+path+"\", please check the permissions to write that directory. Program is going to terminate!");
            System.exit(1);
        }
        finally {
            if (ps != null) { // Flushes all the content and closes the stream if it has been successfully created
                ps.flush();
                ps.close();
            }
        }
    }
     
}

