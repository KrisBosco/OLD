import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Gui extends JFrame {
    private JPanel cardPanel;
    private JButton selectButton;
    private JButton sellButton;
    private JButton quitButton;
    private JButton displayButton; // New button to display correct choices
    private JLabel welcomeLabel;
    private Map<String, ImageIcon> cardImages;

    public Gui(Map<String, ImageIcon> cardImages) {
        this.cardImages = cardImages;
        setTitle("The Art Dealer");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initComponents();
        layoutComponents();
        setLocationRelativeTo(null);
    }

    private void layoutComponents() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(selectButton);
        buttonPanel.add(sellButton);
        buttonPanel.add(displayButton);
        buttonPanel.add(quitButton);
        

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(welcomeLabel, BorderLayout.NORTH);
        getContentPane().add(cardPanel, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    }

    public void initComponents() {
        cardPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        selectButton = new JButton("Select Cards");
        sellButton = new JButton("Sell to Art Dealer");
        quitButton = new JButton("Quit");
        displayButton = new JButton("History"); 
        welcomeLabel = new JLabel("<html><body>Card Selling Program. Built With Java.<br><br>" +
                "HW4 Chief Programmer: Kris Bosco<br>" +
                "Revision Date: March 24th, 2024<br><br>" +
                "Team Members: Kris Bosco, Jesse Gemple, Karlin Clabon-Barnes, Kyle Peniston<br>" +
                "CMP_SCI-4500 Keith Miller<br><br>" +
                "This program is a guessing game consisting of six rounds of challenges.<br>" +
                "See if you can guess what cards the Art Dealer is interested in.<br><br>" +
                "To begin guessing, click on the Select Cards button and select four cards from the deck.<br>" +
                "Once four cards have been selected, click the Sell To Art Dealer button to see if you guessed correctly.");

        selectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (GuiMethods.selectedCardCount == 4) {
                    GuiMethods.clearCardPanel(cardPanel);
                    GuiMethods.selectedCardCount = 0;
                    GuiMethods.userInput(e, cardImages, cardPanel, GameMethods.getRoundCount());
                } else {
                    GuiMethods.userInput(e, cardImages, cardPanel, GameMethods.getRoundCount());
                }
            }
        });

        sellButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<String> selectedCards = GuiMethods.getSelectedCards();
                GameMethods.checkPattern(selectedCards, cardPanel, cardImages);
                selectedCards.clear();
            }
        });

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GuiMethods.quitGame();
            }
        });

        displayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayCorrectChoices();
            }
        });
    }

    private void displayCorrectChoices() {
        JTextArea textArea = new JTextArea(20, 20);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));
    
        JFrame frame = new JFrame("Correct Choices");
        frame.getContentPane().add(scrollPane);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    
        // Read the contents of CardsDealt.txt and display them
        refreshTextArea(textArea);
    
        // Schedule a task to refresh the JTextArea periodically
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshTextArea(textArea);
            }
        });
        timer.start();
    }
    
    private void refreshTextArea(JTextArea textArea) {
        textArea.setText(""); // Clear the text area before refreshing
        try (BufferedReader reader = new BufferedReader(new FileReader("ArtDealersSelection.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                textArea.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
