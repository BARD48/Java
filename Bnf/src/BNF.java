import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
public class BNF {
    static int index;
    public static void main(String[] args) {
        String outputFile=args[1];
        String []lines=FileInput.readFile(args[0], false, false);
        Map<String,String>VariableMap=new HashMap<>();//It have key value pairs and  variable has their name and their values. Because of used in
        String decipher="";
        for (String line:lines){
            String[]split=line.split("->");
            if (split[0].equals("S")){
                decipher=split[1];
            }
            else{
                VariableMap.put(split[0], split[1]);

            }
            
        }
       Decoder(decipher, VariableMap,outputFile);
    }
/**
 * Decodes the given string by replacing certain substrings with their corresponding values from the VariableMap.
 * If the input string contains lowercase letters, it is wrapped with parentheses and written to the outputFile.
 * Otherwise, it replaces substrings with their corresponding values recursively until all replacements are done.
 *
 * @param decipher    the string to be decoded
 * @param VariableMap the map containing key-value pairs for replacements
 * @param outputFile  the file path where the decoded string will be written
 */
    public static void Decoder(String decipher,Map<String,String>VariableMap,String outputFile){
        if(isSmall(decipher)==true){
            //that condtion stop condition for recursive function
            decipher="("+decipher+")";
            FileOutput.writeToFile(outputFile, decipher, false, false);
            
            return;
        }
        else if (isSmall(decipher)==false){
            //else call himself and write first place its values
            Set<String> keys=VariableMap.keySet(); 
            for(String key:keys){
                if (key.equals(String.valueOf(decipher.charAt(index)))){
                    decipher=decipher.substring(0, index)+"("+VariableMap.get(key)+")"+decipher.substring(index+1);
                    Decoder(decipher, VariableMap,outputFile);
                    break;
                }
            }
            
        }

    }
    /**
 * Checks if the given word contains lowercase letters.
 * Also accepts a word starting with '|' or containing '(' or ')' characters.
 *
 * @param words the word to be checked
 * @return true if the word contains lowercase letters or starts with '|', or contains '(' or ')' characters;
 * otherwise, returns false. Also sets the index when the word does not contain lowercase letters.
 */
    public static boolean isSmall(String words){
        char[]word=words.toCharArray();
        boolean solution=true;
        int i=0;
        //that funciton check letter is lower case or not. 
        for(char letter:word){
            if(Character.isLowerCase(letter)){
                solution=true;
            }
            else if(letter=='|'||letter=='('||letter==')'){//It checks that char case
                solution=true;
            }
            else{
                index=i;
                return false;
            }
            i++;
        }
        return solution;
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