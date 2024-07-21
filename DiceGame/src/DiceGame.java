import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class DiceGame {
    private String name;
    private int score;
    private int queue;

    public void setName(String name) {
        this.name = name;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setQueue(int queue) {
        this.queue = queue;
    }

    public static void main(String[] args) {
        game(args);
    }

    public static void game(String[] args) {
        String[] lines = FileInput.readFile(args[0], false, false);
        int numberPeople = Integer.parseInt(lines[0]);
        String[] People = lines[1].split(",");
        int number = 1;
        DiceGame[] gamers = new DiceGame[numberPeople];
        for (int i = 0; i < numberPeople; i++) {
            gamers[i] = new DiceGame();
            gamers[i].setName(People[i]);
            gamers[i].setQueue(number);
            number++;
        }
        int start = numberPeople - 2;
        String winnerName = "";
        int winnerScore = 0;
        boolean before = false;
        int eliminationNumber = numberPeople;
        boolean eliminate = false;
        for (int i = 2; i < lines.length; i++) {
            String[] Dices = lines[i].split("-");
            int dice1 = Integer.parseInt(Dices[0]);
            int dice2 = Integer.parseInt(Dices[1]);
            String result = result(dice1, dice2);
            int total = total(dice1, dice2, result);
            for (DiceGame gamer : gamers) {
                if (gamer != null) {
                    if (before == true) {
                        
                        if (gamer.getName().equals(gamers[eliminationNumber - 1].getName())) {
                            queueUpdate(gamers, eliminationNumber);
                            numberPeople--;
                            while ((i+1  + start) % numberPeople != 0) {
                                start--;
                            }
                            before = false;
                        }
                    }
                    else if ((before == false) && (eliminate)) {
                        if (gamer.getName().equals(gamers[eliminationNumber - 1].getName())){
                            numberPeople--;
                           
                            while ((i + 1 + start) % numberPeople != 0) {
                                start--;
                            }
                            eliminate = false;
                    }}
                }
                if ((gamer!=null)&&(i + start) % numberPeople == ((gamer.getQueue()) - 1)) {
                    if (result.equals("win")) {
                        gamer.score += total;
                        String content1 = gamer.name + " threw " + dice1 + "-" + dice2 + " and " + gamer.name + "’s "
                                + "score " + "is " + gamer.score + ".";
                        FileOutput.writeToFile(args[1], content1, true, true);
                        break;
                    } else if (result.equals("skip")) {
                        gamer.score += total;
                        String content1 = gamer.name + " skipped " + "the " + "turn " + "and " + gamer.name + "’s "
                                + "score " + "is " + gamer.score + ".";
                        FileOutput.writeToFile(args[1], content1, true, true);
                        break;
                    } else if (result.equals("draw")) {
                        gamer.score += total;
                        String content2 = gamer.name + " threw " + dice1 + "-" + dice2 + " and " + gamer.name + "’s "
                                + "score " + "is " + gamer.score + ".";
                        FileOutput.writeToFile(args[1], content2, true, true);
                        break;
                    } else if (result.equals("lost")) {
                        String content3 = gamer.name + " threw " + dice1 + "-" + dice2 + ". " + "Game over "
                                + gamer.name + "!";
                        FileOutput.writeToFile(args[1], content3, true, true);
                        int queueEliminate=gamer.getQueue();
                        if (queueEliminate<eliminationNumber){
                            before=true;
                        }
                        elimination(before, gamer, gamers, eliminationNumber);
                        eliminationNumber--;
                        eliminate = true;
                        break;
                    }
                }
            }
            
         
            

        }
        int remainingPlayers = 0;
        int constant = 0;
        for (DiceGame gamer : gamers) {
            if (gamer != null) {
                remainingPlayers++;
            }
            constant++;

            if (constant >= gamers.length) {
                break;
            }
        }
        if (remainingPlayers == 1) {
            for (DiceGame gamer : gamers) {
                if (gamer != null) {
                    winnerName = gamer.getName();
                    winnerScore = gamer.getScore();
                    break;
                }
            }
        }
        if (!winnerName.equals("")) {
            String content4 = winnerName + " is the winner of the game with the score of " + winnerScore + ". "
                    + "Congratulations " + winnerName + "!";
            FileOutput.writeToFile(args[1], content4, true, false);
        }
        

    }

    public static String result(int dice1, int dice2) {
        if ((dice1 == 1) && (dice2 == 1)) {
            return "lost";
        } else if (((dice1 == 1) && (dice2 != 1)) || ((dice1 != 1)) && ((dice2 == 1))) {
            return "draw";
        } else if ((dice1 == 0) && (dice2 == 0)) {
            return "skip";
        } else {
            return "win";
        }
    }

    public static int total(int dice1, int dice2, String result) {
        if (("draw".equals(result)) || ("skip".equals(result))) {
            return 0;
        } else if ("win".equals(result)) {
            return dice1 + dice2;
        } else {
            return 0;
        }
    }

    public int getQueue() {
        return queue;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public static void elimination(boolean before, DiceGame gamer, DiceGame[] gamers, int eliminationNumber) {
        for (int i = 0; i < eliminationNumber; i++) {
           
            if (gamers[i] == gamer){
                if (i == eliminationNumber - 1) {
                    gamers[eliminationNumber - 1] = null;
                } else if (i < eliminationNumber - 1) {
                    DiceGame temp = gamers[i];
                    gamers[i] = gamers[i + 1];
                    gamers[i + 1] = temp;
                   
                }
            }
        }
    }

    public static void queueUpdate(DiceGame[] gamers, int eliminationNumber) {
        for (int i = 0; i < eliminationNumber; i++) {
            
            gamers[i].setQueue(gamers[i].getQueue() - 1);
        }
    }

}

class FileInput {

    public static String[] readFile(String path, boolean discardEmptyLines, boolean trim) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(path));
            if (discardEmptyLines) {
                lines.removeIf(line -> line.trim().equals(""));
            }
            if (trim) {
                lines.replaceAll(String::trim);
            }
            return lines.toArray(new String[0]);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}

class FileOutput {

    public static void writeToFile(String path, String content, boolean append, boolean newLine) {
        PrintStream ps = null;
        try {
            ps = new PrintStream(new FileOutputStream(path, append));
            ps.print(content + (newLine ? "\n" : ""));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (ps != null) {
                ps.flush();
                ps.close();
            }
        }
    }
}