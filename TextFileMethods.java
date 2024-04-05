import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class TextFileMethods {
    private static final String FILE1 = "CardsDealt.txt";
    private static final String FILE2 = "LastWon.txt";
    private static final String FILE3 = "ExecutionLog.txt";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");

    // Method to log the selected cards to the file CardsDealt.txt.
    public static void logSelectedCards(int roundCount, List<String> selectedCards) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE1, true))) {
            if (firstRunToday()) {
                writer.write("Date: " + DATE_FORMAT.format(new Date()));
                writer.newLine();
            }
    
            StringBuilder cardsBuilder = new StringBuilder();
            for (int i = 0; i < selectedCards.size(); i++) {
                String card = selectedCards.get(i);
                if (i % 4 == 0 && i > 0) {
                    writer.write(cardsBuilder.toString());
                    writer.newLine();
                    cardsBuilder.setLength(0);
                }
                cardsBuilder.append(card);
                if (i % 4 != 3) {
                    cardsBuilder.append(", ");
                }
            }
            if (cardsBuilder.length() > 0) {
                writer.write(cardsBuilder.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int getLastRoundWon() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE2))) {
            String line = reader.readLine();
            if (line != null) {
                // Parse the value from the file
                int lastWon = Integer.parseInt(line.trim());
                // If lastWon is 1, it means the user has passed round 1 and should start from round 2
                return lastWon + 1; // Increment by 1 to start from the next round
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        // If there's an error or the file is empty, start from round 1
        return 1;
    }

    public static void updateLastRoundWon(int round) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE2))) {
            // Write the updated round number to the file
            writer.write(String.valueOf(round));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void logExecution(String activity) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE3, true))) {
            Date currentDate = new Date();
            writer.write("[" + DATE_FORMAT.format(currentDate) + "] " + activity);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void handleEndGame(int lastWon) {
        if (lastWon == 6) {
            try (Scanner scanner = new Scanner(System.in)) {
                System.out.println("You have already won all six patterns.");
                System.out.print("Do you want to start over? (yes/no): ");
                String choice = scanner.nextLine().trim().toLowerCase();
                if (choice.equals("yes")) {
                    updateLastRoundWon(0);
                } else {
                    System.out.println("Goodbye!");
                    System.exit(0);
                }
            }
        }
    }

    private static boolean firstRunToday() {
        String todayDateString = DATE_FORMAT.format(new Date());
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE1))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Date:")) {
                    String dateString = line.substring(6).trim();
                    if (todayDateString.equals(dateString)) {
                        return false; // Timestamp for today already exists, no need to add a new one
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true; // If no timestamp for today found, it's the first run today
    }


}
