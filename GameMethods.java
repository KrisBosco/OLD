import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class GameMethods {

    private static final int MAX_ROUNDS = 6;
    private static int correctGuessesCount = 0;
    private static int roundCount = determineStartingRound();

    public static boolean roundComplete(List<String> selectedCards) {
        return correctCards(roundCount, selectedCards);
    }

    public static void checkPattern(List<String> selectedCards, JPanel cardPanel, Map<String, ImageIcon> cardImages) {
        if (selectedCards.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No cards have been selected. Click Select Cards to begin.");
            GuiMethods.clearCardPanel(cardPanel);
            return;
        }

        // Log the selected cards without considering pattern matching
        TextFileMethods.logSelectedCards(roundCount, selectedCards);

        if (roundComplete(selectedCards)) {
            correctGuessesCount++;

            if (correctGuessesCount == 2) { 
                roundCount++;
                correctGuessesCount = 0;
                if (roundCount <= MAX_ROUNDS) {
                    if (soldTwoGroups(selectedCards)) {
                        winningMessages(roundCount - 1);
                        // Update last won round after winning a round
                        TextFileMethods.updateLastRoundWon(roundCount-1);
                    } else {
                        GuiMethods.clearCardPanel(cardPanel);
                        return;
                    }
                    // Check if the user has won all six rounds
                    int lastWonRound = TextFileMethods.getLastRoundWon();
                    TextFileMethods.handleEndGame(lastWonRound);
                    int option = JOptionPane.showConfirmDialog(null, "Do you want to continue to the next round?", "Continue?", JOptionPane.YES_NO_OPTION);
                    if (option == JOptionPane.NO_OPTION) {
                        roundCount = 1;
                        JOptionPane.showMessageDialog(null, "Thank you for playing! Have a nice day!");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Congratulations!! You have completed all the rounds. The Art Dealer is quite happy with his new collection.");
                    roundCount = 1;
                    int option = JOptionPane.showConfirmDialog(null, "Do you want to play again?", "Play Again?", JOptionPane.YES_NO_OPTION);
                    if (option == JOptionPane.NO_OPTION) {
                        JOptionPane.showMessageDialog(null, "Thank you for playing! Have a nice day!");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Well done! You guessed correctly. Try to guess correctly one more time to proceed to the next round.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Incorrect cards selected. The Art Dealer isn't happy with the current selection. Maybe what he does like can be used as a hint?");
        }

        // Clear the cardPanel before displaying new cards
        GuiMethods.clearCardPanel(cardPanel);

        // Display selected cards
        for (String card : selectedCards) {
            GuiMethods.displayCardInPanel(card, cardImages, cardPanel, selectedCards, roundCount);
        }
    }

    public static boolean correctCards(int roundCount, List<String> selectedCards) {
        boolean isCorrect = false;
        switch (roundCount) {
            case 1:
                isCorrect = allRedCards(selectedCards) && soldTwoGroups(selectedCards);
                break;
            case 2:
                isCorrect = allClubs(selectedCards) && soldTwoGroups(selectedCards);
                break;
            case 3:
                isCorrect = allFaceCards(selectedCards) && soldTwoGroups(selectedCards);
                break;
            case 4:
                isCorrect = allSingleDigits(selectedCards) && soldTwoGroups(selectedCards);
                break;
            case 5:
                isCorrect = allSingleDigitPrimes(selectedCards) && soldTwoGroups(selectedCards);
                break;
            case 6:
                isCorrect = highestRank(selectedCards) && soldTwoGroups(selectedCards);
                break;
            default:
                isCorrect = false;
                break;
        }
        System.out.println("correctCards called with roundCount = " + roundCount + ", isCorrect = " + isCorrect);
        return isCorrect;
    }

    public static boolean incorrectCards(int roundCount, List<String> selectedCards) {
        switch (roundCount) {
            case 1:
                return !allRedCards(selectedCards) || !soldTwoGroups(selectedCards);
            case 2:
                return !allClubs(selectedCards) || !soldTwoGroups(selectedCards);
            case 3:
                return !allFaceCards(selectedCards) || !soldTwoGroups(selectedCards);
            case 4:
                return !allSingleDigits(selectedCards) || !soldTwoGroups(selectedCards);
            case 5:
                return !allSingleDigitPrimes(selectedCards) || !soldTwoGroups(selectedCards);
            case 6:
                return !highestRank(selectedCards) || !soldTwoGroups(selectedCards);
            default:
                return false;
        }
    }

    private static void winningMessages(int round) {
        switch (round) {
            case 1:
                JOptionPane.showMessageDialog(null, "Congratulations! The Art Dealer is quite pleased with all these red cards!");
                break;
            case 2:
                JOptionPane.showMessageDialog(null, "Congratulations! The Art Dealer is going clubbin now!");
                break;
            case 3:
                JOptionPane.showMessageDialog(null, "Congratulations! The Art Dealer has made some new friends!");
                break;
            case 4:
                JOptionPane.showMessageDialog(null, "Congratulations! The Art Dealer is happy you singled out the good cards for him");
                break;
            case 5:
                JOptionPane.showMessageDialog(null, "Congratulations! The Art Dealer is at his prime now!");
                break;
            case 6:
                JOptionPane.showMessageDialog(null, "Congratulations! The Art Dealer only wanted the best you had to offer!");
                break;
            default:
                JOptionPane.showMessageDialog(null, "Something went wrong.");
                break;
        }
    }

    public static int getRoundCount() {
        return roundCount;
    }

    public static int determineStartingRound() {
        int lastWonRound = TextFileMethods.getLastRoundWon();
        if (lastWonRound == 0) {
            return 1; // Start at the first round if the last won round is 0
        } else {
            return lastWonRound; // Start at the last won round if it's not 0
        }
    }

    public static void dealersSelection(int roundCount, List<String> selectedCards, String userInput) {
        boolean isCorrect = false;
        switch (roundCount) {
            case 1:
                isCorrect = allRedCards(selectedCards);
                break;
            case 2:
                isCorrect = allClubs(selectedCards);
                break;
            case 3:
                isCorrect = allFaceCards(selectedCards);
                break;
            case 4:
                isCorrect = allSingleDigits(selectedCards);
                break;
            case 5:
                isCorrect = allSingleDigitPrimes(selectedCards);
                break;
            case 6:
                isCorrect = highestRank(selectedCards);
                break;
            default:
                isCorrect = false;
                break;
        }
        System.out.println("correctCards called with roundCount = " + roundCount + ", isCorrect = " + isCorrect);
    
        if (isCorrect) {
            try (FileWriter writer = new FileWriter("ArtDealersSelection.txt", true)) {
                writer.write("Round " + roundCount + ": "); // Indicate the round number
                for (String card : selectedCards) {
                    writer.write(card + ", ");
                }
                writer.write("*" + userInput + "*\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static boolean allRedCards(List<String> selectedCards) {
        int hearts = 0;
        int diamonds = 0;

        for (String card : selectedCards) {
            if (card.contains("H")) {
                hearts++;
            } else if (card.contains("D")) {
                diamonds++;
            }
        }

        return (hearts == 4 || diamonds == 4) ||
                (hearts == 1 && diamonds == 3) ||
                (hearts == 3 && diamonds == 1) ||
                (hearts == 2 && diamonds == 2);
    }

    private static boolean allClubs(List<String> selectedCards) {
        int clubs = 0;

        for (String card : selectedCards) {
            if (card.contains("C")) {
                clubs++;
            }
        }

        return clubs == 4;
    }

    private static boolean allFaceCards(List<String> selectedCards) {
        int faceCards = 0;

        for (String card : selectedCards) {
            if (card.contains("K") || card.contains("Q") || card.contains("J")) {
                faceCards++;
            }
        }

        return faceCards == 4;
    }

    private static boolean allSingleDigits(List<String> selectedCards) {
        int singleDigits = 0;

        for (String card : selectedCards) {
            char rank = card.charAt(0);
            if (Character.isDigit(rank)) {
                singleDigits++;
            }
        }

        return singleDigits == 4;
    }

    private static boolean allSingleDigitPrimes(List<String> selectedCards) {
        int primeDigits = 0;

        for (String card : selectedCards) {
            char rank = card.charAt(0);
            if (rank == '2' || rank == '3' || rank == '5' || rank == '7') {
                primeDigits++;
            }
        }

        return primeDigits == 4;
    }

    private static boolean highestRank(List<String> selectedCards) {
        char highestRank = '2';

        for (String card : selectedCards) {
            char rank = card.charAt(0);
            if (rank > highestRank) {
                highestRank = rank;
            }
        }

        int count = 0;
        for (String card : selectedCards) {
            if (card.charAt(0) == highestRank) {
                count++;
            }
        }

        return count == 4;
    }

    private static boolean soldTwoGroups(List<String> selectedCards) {
        int size = selectedCards.size();
        if (size % 2 != 0 || size < 4) { 
            return false;
        }
    
        HashSet<String> firstGroup = new HashSet<>(selectedCards.subList(0, size / 2));
        HashSet<String> secondGroup = new HashSet<>(selectedCards.subList(size / 2, size));
    
        return firstGroup.size() == size / 2 && secondGroup.size() == size / 2;
    }

}
