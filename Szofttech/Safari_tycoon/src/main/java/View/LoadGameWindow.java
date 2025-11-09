package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Window for selecting a saved game data and map to load.
 */
public class LoadGameWindow extends View {

    private final JList<String> fileList;
    private final DefaultListModel<String> listModel;
    private final JButton loadButton;
    private final JButton cancelButton;
    private final String savedMapsPath = "src/main/resources/map/";

    public LoadGameWindow(View parentWindow) {
        super();
        this.setTitle("Load Game");
        this.setSize(400, 300);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());

        //file selector
        listModel = new DefaultListModel<>();
        fileList = new JList<>(listModel);
        fileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(fileList);
        this.loadSavedFiles();

        JPanel buttonPanel = new JPanel();
        loadButton = new JButton("Load");
        cancelButton = new JButton("Cancel");

        buttonPanel.add(loadButton);
        buttonPanel.add(cancelButton);

        this.add(new JLabel("Select a saved game:"), BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);

        // Load button action
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedFile = fileList.getSelectedValue();
                if (selectedFile != null) {
                    String saveName = selectedFile.replace(".txt", "");
                    new GameWindow(parentWindow, saveName);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a save file.", "No File Selected", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        //Cancel button returns to menu
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MenuWindow(parentWindow);
                dispose();
            }
        });

        this.setVisible(true);
    }

    /**
     * Loads the available saved game files from the directory into the JList.
     */
    private void loadSavedFiles() {
        File dir = new File(savedMapsPath);
        if (!dir.exists() || !dir.isDirectory()) {
            System.out.println("Directory does not exist: " + savedMapsPath);
            return;
        }

        File[] files = dir.listFiles((dir1, name) -> name.endsWith(".txt"));
        if (files != null) {
            for (File file : files) {
                listModel.addElement(file.getName());
            }
        }
    }
}