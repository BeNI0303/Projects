package View;

import Controller.GameController;
import Controller.KeyHandler;
import Controller.MouseHandler;
import Model.Model;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameWindow extends View {

    // Game speed constants
    public static final int SPEED_HOUR = 1;
    public static final int SPEED_HALF_DAY = 12;
    public static final int SPEED_DAY = 24;

    public JLabel balanceLabel;
    public JLabel timeLabel;
    private final GamePanel gamePanel;
    private final ButtonPanel buttonPanel;
    private final MouseHandler mouseH;
    private final KeyHandler keyH;
    private JPanel topPanel;
    private JButton hourButton;
    private JButton halfDayButton;
    private JButton dayButton;
    private JButton saveButton;
    private final Minimap minimap;
    private final GameController gc;
    private final Model model;
    private boolean isMinimapFullscreen = false;
    private boolean isMinimapVisible = true;
    private int currentSpeed = SPEED_HOUR;
    private int elapsedHours = 0;
    private int elapsedDays = 0;
    private View actualView;

    public GameWindow(View w, String difficulty) {
        super();
        actualView = w;
        this.setTitle("Game");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(screenSize.width / 8, 20);
        this.setLayout(new BorderLayout());

        // Making the controller and the model
        this.mouseH = new MouseHandler();
        this.keyH = new KeyHandler();
        this.model = new Model(difficulty);

        // Create top panel with balance and time controls
        createTopPanel();

        this.gamePanel = new GamePanel(mouseH, keyH, model, balanceLabel, difficulty);
        this.gc = new GameController(keyH, mouseH, gamePanel, model, this);

        // Set the GameWindow in the GamePanel
        this.gamePanel.setGameWindow(this);

        buttonPanel = new ButtonPanel(gamePanel);
        minimap = new Minimap(gamePanel, model);

        // Set the game controller reference in the game panel
        gamePanel.setGameController(gc);

        // Set the game controller reference in the minimap
        minimap.setGameController(gc);

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(model.screenWidth, model.screenHeight));

        gamePanel.setBounds(0, 0, model.screenWidth, model.screenHeight);
        minimap.setBounds(0, 0, minimap.getPreferredSize().width, minimap.getPreferredSize().height);

        layeredPane.add(gamePanel, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(minimap, JLayeredPane.PALETTE_LAYER);

        // Adding the panels and labels
        this.add(layeredPane, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);
        this.add(topPanel, BorderLayout.NORTH);
        this.setFocusable(true);
        this.requestFocus();
        this.pack();
        this.setVisible(true);

        gc.gameStart();
    }

    private void createTopPanel() {

        topPanel = new JPanel(new GridBagLayout());
        topPanel.setBackground(new Color(210, 144, 78));
        topPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 5, 0, 5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;

        balanceLabel = createStyledLabel("Balance: 0", Font.BOLD, 16);
        timeLabel = createStyledLabel("Day 1, 08:00", Font.BOLD, 16);

        JPanel balancePanel = createDecoratedPanel(balanceLabel, "💰");
        JPanel timePanel = createDecoratedPanel(timeLabel, "🕒");
        JPanel speedPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        speedPanel.setOpaque(false);

        JLabel speedLabel = createStyledLabel("Game Speed:", Font.BOLD, 14);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setOpaque(false);

        hourButton = createSpeedButton("Hour", SPEED_HOUR);
        halfDayButton = createSpeedButton("Half Day", SPEED_HALF_DAY);
        dayButton = createSpeedButton("Day", SPEED_DAY);
        saveButton = createSaveButton("Save Game");

        buttonPanel.add(hourButton);
        buttonPanel.add(halfDayButton);
        buttonPanel.add(dayButton);
        buttonPanel.add(saveButton);

        speedPanel.add(speedLabel);
        speedPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        speedPanel.add(buttonPanel);

        // Mark the initial speed button as selected
        updateSelectedSpeedButton();

        // Add components to top panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.25;
        topPanel.add(balancePanel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.35;
        topPanel.add(timePanel, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0.4;
        topPanel.add(speedPanel, gbc);
    }

    // Creates a decorated panel with a label and an icon
    private JPanel createDecoratedPanel(JLabel label, String iconText) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panel.setOpaque(false);

        // Create icon label
        JLabel iconLabel = new JLabel(iconText);
        iconLabel.setFont(new Font("Dialog", Font.PLAIN, 18));

        panel.add(iconLabel);
        panel.add(label);

        // Add a subtle border and rounded corners
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(160, 120, 70), 1),
                BorderFactory.createEmptyBorder(3, 8, 3, 8)
        ));

        return panel;
    }

    // Creates a styled label with custom font size and style
    private JLabel createStyledLabel(String text, int fontStyle, int fontSize) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", fontStyle, fontSize));
        label.setForeground(new Color(60, 30, 10));
        return label;
    }

    private JButton createSpeedButton(String text, final int speed) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.PLAIN, 12));
        button.setFocusPainted(false);
        button.setFocusable(false);
        button.setMargin(new Insets(4, 8, 4, 8));
        button.setBorderPainted(true);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(150, 100, 70), 1),
                BorderFactory.createEmptyBorder(2, 6, 2, 6)
        ));

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setGameSpeed(speed);
            }
        });
        return button;
    }

    private JButton createSaveButton(String text) {
        JButton saveButton = new JButton(text);

        saveButton.setFont(new Font("SansSerif", Font.PLAIN, 12));
        saveButton.setFocusPainted(false);
        saveButton.setFocusable(false);
        saveButton.setBorderPainted(true);
        saveButton.setBackground(new Color(144, 238, 144));
        saveButton.setForeground(new Color(0, 0, 0));
        saveButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(150, 100, 70), 1),
                BorderFactory.createEmptyBorder(2, 6, 2, 6)
        ));

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String saveName = JOptionPane.showInputDialog(null, "Enter save name:", "Save Game", JOptionPane.PLAIN_MESSAGE);
                if (saveName != null && !saveName.trim().isEmpty()) {
                    gc.saveGame(saveName.trim());
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid save name!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        return saveButton;
    }

    public void gameEndOptionPane() {
        String message = "";
        if (gc.isLost()) {
            if (model.getBalance() <= 0) {
                message = "Run out of money";
            } else if (model.getPredators().size() < 2) {
                message = "Not enough predators";
            } else if (model.getHerbivores().size() < 5) {
                message = "Not enough herbivores";
            } else {
                message = "?";
            }
        } else if (gc.isWin()) {
            message = "Congratulation!";
        } else {
            message = "?";
        }

        Object[] options = {"Restart", "Exit"};

        int choice = JOptionPane.showOptionDialog(
                null,
                message + "\nWhat would you like to do?",
                gc.isLost() ? "Game Lost" : "Game Won",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0] // alapértelmezett gomb
        );

        // Döntés kezelése:
        if (choice == 0) {
            // Újraindítás logika
            this.dispose();
            actualView = new MenuWindow(actualView);
        } else if (choice == 1 || choice == JOptionPane.CLOSED_OPTION) {
            // Kilépés
            this.dispose();
        }
       

    }

    public void setGameSpeed(int speed) {
        currentSpeed = speed;
        updateSelectedSpeedButton();
        if (gc != null) {
            gc.setGameSpeed(speed);
        }
    }

    private void updateSelectedSpeedButton() {
        Color selectedColor = new Color(150, 100, 70);
        Color normalColor = new Color(210, 170, 125);
        Color selectedTextColor = Color.WHITE;
        Color normalTextColor = new Color(60, 30, 10);

        hourButton.setBackground(currentSpeed == SPEED_HOUR ? selectedColor : normalColor);
        halfDayButton.setBackground(currentSpeed == SPEED_HALF_DAY ? selectedColor : normalColor);
        dayButton.setBackground(currentSpeed == SPEED_DAY ? selectedColor : normalColor);

        hourButton.setForeground(currentSpeed == SPEED_HOUR ? selectedTextColor : normalTextColor);
        halfDayButton.setForeground(currentSpeed == SPEED_HALF_DAY ? selectedTextColor : normalTextColor);
        dayButton.setForeground(currentSpeed == SPEED_DAY ? selectedTextColor : normalTextColor);
    }

    public void updateGameTime(int hours, int days) {
        this.elapsedHours = hours;
        this.elapsedDays = days;
        String hourStr = String.format("%02d:00", hours % 24);
        timeLabel.setText("Day " + (days + 1) + ", " + hourStr);
    }

    public int getCurrentSpeed() {
        return currentSpeed;
    }

    public void toggleMinimapFullscreen() {
        isMinimapFullscreen = !isMinimapFullscreen;

        if (isMinimapFullscreen) {
            minimap.setBounds(0, 0, model.screenWidth, model.screenHeight);
            minimap.setPreferredSize(new Dimension(model.screenWidth, model.screenHeight));
            minimap.setFullscreen(true);
            gamePanel.setVisible(false);

            minimap.addKeyListener(keyH);
            minimap.setFocusable(true);
            minimap.requestFocusInWindow();
        } else {
            minimap.setBounds(0, 0, minimap.originalMinimapWidth + 2 * minimap.borderThickness,
                    minimap.originalMinimapHeight + 2 * minimap.borderThickness);
            minimap.setPreferredSize(new Dimension(minimap.originalMinimapWidth + 2 * minimap.borderThickness,
                    minimap.originalMinimapHeight + 2 * minimap.borderThickness));
            minimap.setFullscreen(false);
            gamePanel.setVisible(true);

            minimap.removeKeyListener(keyH);
            gamePanel.requestFocusInWindow();
        }
        this.revalidate();
        this.repaint();
    }

    public void toggleMinimapVisibility() {
        if (!isMinimapFullscreen) {
            isMinimapVisible = !isMinimapVisible;
            minimap.setVisible(isMinimapVisible);
            this.revalidate();
            this.repaint();
        }
    }

    public void checkForMinimapToggle(KeyHandler keyH) {
        if (keyH.mPressed && !keyH.ctrlPressed) {
            if (!isMinimapVisible) {
                toggleMinimapVisibility();
                toggleMinimapFullscreen();
            } else {
                toggleMinimapFullscreen();
            }
            keyH.mPressed = false;
        }
        if (keyH.mPressed && keyH.ctrlPressed) {
            toggleMinimapVisibility();
            keyH.mPressed = false;
        }
    }

    public Minimap getMinimap() {
        return minimap;
    }

    public boolean isMinimapFullscreen() {
        return isMinimapFullscreen;
    }
}
