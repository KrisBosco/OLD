import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

// Author: Kris Bosco
// Java class to hold all methods used to make the GUI buttons functional and to adjust the display when there is a change.
public class GuiMethods {

    private static List<String> selectedCards = new ArrayList<>();
    private static Set<Set<String>> previousSelections = new HashSet<>();
    public static int selectedCardCount = 0;

    // Array to store selected cards
public static String userInput(ActionEvent e, Map<String, ImageIcon> cardImages, JPanel cardPanel, int roundCount) {
    StringBuilder selectedCardsLog = new StringBuilder();

    while (selectedCardCount < 4) {
        if (selectedCardCount >= 4) {
            JOptionPane.showMessageDialog(null, "You have already selected the maximum number of cards.");
            break;
        }

        String input = JOptionPane.showInputDialog("Enter a card (e.g., 2H, 3D, 4S, 5C):");

        if (input == null) {
            return input;
        }

        input = input.toUpperCase();

        System.out.println("Selected Card: " + input);

        if (!input.matches("[2-9][HDSC]|10[HDSC]|J[HDSC]|Q[HDSC]|K[HDSC]|A[HDSC]")) {
            JOptionPane.showMessageDialog(null, "Invalid card format. Please enter a valid card (e.g., 2H, 3D, 4S, 5C).");
            continue;
        }

        // Create a set for the current selection
        Set<String> currentSelection = new HashSet<>(selectedCards);
        currentSelection.add(input);

        // Prevent user from selecting the same card in the current round
        if (selectedCards.contains(input)) {
            JOptionPane.showMessageDialog(null, "You have already selected this card in the current round. Please select a different card.");
            continue;
        }

        // Prevent user from selecting same group of four cards from previous round
        if (previousSelections.contains(currentSelection)) {
            JOptionPane.showMessageDialog(null, "You have selected these cards last round. Please select different cards.");
            continue;
        }

        // Add the selected card to the list and set of previous selections.
        selectedCards.add(input);

        // Increase the selected card count.
        selectedCardCount++;

        // Display the selected card in the card panel.
        displayCardInPanel(input, cardImages, cardPanel, selectedCards, roundCount);

        selectedCardsLog.append(input).append(", ");
    }

    if (selectedCardCount >= 4) {
        JOptionPane.showMessageDialog(null, "You have selected the maximum number of cards. " +
                "Click Sell to Art Dealer to submit choices.");

        //TextFileMethods.logSelectedCards(selectedCards);
        selectedCardsLog.setLength(0);

        // Store the current selection as a set in the previous selections
        previousSelections.add(new HashSet<>(selectedCards));
    }

    return null;
}

static void displayCardInPanel(String cardName, Map<String, ImageIcon> cardImages, JPanel cardPanel, List<String> selectedCards, int roundCount) {
    ImageIcon image = cardImages.get(cardName);
    if (image != null) {
        JLabel label;
        boolean isCorrect = !GameMethods.incorrectCards(roundCount, selectedCards);
        if (isCorrect) {
            label = new JLabel(image);
        } else {
            label = new JLabel(image);
        }
        cardPanel.add(label);
        cardPanel.revalidate();
        cardPanel.repaint();
        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(cardPanel);
        if (topFrame != null) {
            topFrame.pack();
        }
    }
}

    // Author: Kris Bosco
    // Method to load card images to be displayed in application window.
    public static void displayCards(String cardName, Map<String, ImageIcon> cardImages, JPanel cardPanel) {
        ImageIcon image = cardImages.get(cardName);
        if (image != null) {
            JLabel label = new JLabel(image);
            cardPanel.add(label);
            cardPanel.revalidate();
            cardPanel.repaint();
        } else {
            JOptionPane.showMessageDialog(null, "Card image for " + cardName + " not found.");
            TextFileMethods.logExecution("Card image for " + cardName + " not found.");
        }
    }

    // Author: Kyle Peniston 
    // Method to quit the game using "Quit" button.
    public static void quitGame() {
        TextFileMethods.logExecution("Quitting game...");
        System.out.println("Thanks for playing! Goodbye!");
        JOptionPane.showMessageDialog(null, "Thanks for playing! Goodbye!");
        System.exit(0);
    }

    // Author: Kyle Peniston
    // Method to retrieve cards selected by the user.
    public static List<String> getSelectedCards() {
        return selectedCards;
    }

    // Author: Kris Bosco
    // Method to clear the card panel.
    public static void clearCardPanel(JPanel cardPanel) {
        TextFileMethods.logExecution("Clearing card panel...");
        cardPanel.removeAll(); 
        cardPanel.revalidate(); 
        cardPanel.repaint();
        selectedCards.clear();
        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(cardPanel);
        if (topFrame != null) {
            topFrame.pack(); 
        }
        TextFileMethods.logExecution("Card panel cleared.");
    }

    
}
