package beadando_2;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class Game {
    
    private int boardSize;
    private int moveCount;
    private JButton[][] board;
    private Escaper escaper;
    private Chaser[] chasers;
    private JFrame frame;
    private Menu menu;
    
    private JPanel controlPanel;
    private JPanel movePanel;
    private JButton restartButton;
    private JButton exitButton;
    private JLabel lepes;
    private JLabel jatekos;
    private JButton uldozo;
    
    //Inicializálja a tároláshoz/játék lejátszásához szükséges dolgokat. Beállítja az ablak jellemzőit
    public Game(int n, Menu menu) {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        controlPanel = new JPanel();
        movePanel = new JPanel();
        restartButton = new JButton("Új játék");
        exitButton = new JButton("Kilépés");
        lepes = new JLabel();
        jatekos = new JLabel();
        boardSize = n;
        moveCount = 4 * n;
        chasers = new Chaser[4];
        escaper = new Escaper(boardSize / 2, boardSize / 2);
        board = new JButton[n][n];
        this.menu = menu;
        initializeGame(n);
        if(n == 7){
            frame.setPreferredSize(new Dimension(700, 700));
        }else if (n == 5){
            frame.setPreferredSize(new Dimension(500, 500));
        }else{
            frame.setPreferredSize(new Dimension(500, 400));
        }
        frame.setTitle("Játéktér");
        frame.setVisible(true);
        frame.pack();
    }
    
    //Létrehozza a játék megjelenését felépíti az alap állapotot. 
    //Gomboknak megadja az actionPerformed metódusait. 
    //Létrehozza az üldözőket és eltárolja őket. 
    //Valamint létrehozza a menekülőt és eltárolja őt is. 
    //Majd hozzá adlja az ablakhoz a játékteret és a gombokat
    private void initializeGame(int n) {
        restartButton.addActionListener((ActionEvent e) -> {
            menu.getFrame().setVisible(true);
            frame.dispose();
        });
        
        lepes.setText("Lépésszám: " + Integer.toString(moveCount));
        jatekos.setText(" Menekülő játékos következik ");
        
        controlPanel.add(jatekos);
        controlPanel.add(exitButton);
        controlPanel.add(restartButton);
        controlPanel.add(lepes);
        
        JButton jobb = new JButton("Jobbra");
        JButton bal = new JButton("Balra");
        JButton fel = new JButton("Fel");
        JButton le = new JButton("Le");
        
        jobb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (jatekos.getText().equals(" Üldöző játékos következik ")) {
                    chaserMove(uldozo, "Jobbra");
                } else if (jatekos.getText().equals(" Menekülő játékos következik ")) {
                    escaperMove("Jobbra");
                }
            }
        });
        
        bal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (jatekos.getText().equals(" Üldöző játékos következik ")) {
                    chaserMove(uldozo, "Balra");
                } else if (jatekos.getText().equals(" Menekülő játékos következik ")) {
                    escaperMove("Balra");
                }
            }
        });
        
        fel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (jatekos.getText().equals(" Üldöző játékos következik ")) {
                    chaserMove(uldozo, "Fel");
                } else if (jatekos.getText().equals(" Menekülő játékos következik ")) {
                    escaperMove("Fel");
                }
            }
        });
        
        le.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (jatekos.getText().equals(" Üldöző játékos következik ")) {
                    chaserMove(uldozo, "Le");
                } else if (jatekos.getText().equals(" Menekülő játékos következik ")) {
                    escaperMove("Le");
                }
            }
        });
        
        movePanel.add(jobb);
        movePanel.add(bal);
        movePanel.add(fel);
        movePanel.add(le);
        
        movePanel.setVisible(false);
        
        exitButton.addActionListener(e -> System.exit(0));
        JPanel boardPanel = new JPanel(new GridLayout(n, n));
        
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                JButton gomb = new JButton();
                board[i][j] = gomb;
                gomb.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (gomb.getText().equals("Üldöző") && jatekos.getText().equals(" Üldöző játékos következik ")) {
                            uldozo = gomb;                            
                            movePanel.setVisible(true);
                        } else if (gomb.getText().equals("Menekülő") && jatekos.getText().equals(" Menekülő játékos következik ")) {
                            movePanel.setVisible(true);
                        } else {
                            JOptionPane.showMessageDialog(frame, jatekos.getText());
                        }
                    }
                });
                boardPanel.add(gomb);
            }
        }
        chasers[0] = new Chaser(0, 0);
        chasers[1] = new Chaser(0, boardSize - 1);
        chasers[2] = new Chaser(boardSize - 1, 0);
        chasers[3] = new Chaser(boardSize - 1, boardSize - 1);
        
        board[0][0].setText("Üldöző");
        board[0][boardSize - 1].setText("Üldöző");
        board[boardSize - 1][0].setText("Üldöző");
        board[boardSize - 1][boardSize - 1].setText("Üldöző");
        
        board[boardSize / 2][boardSize / 2].setText("Menekülő");
        
        frame.getContentPane().add(BorderLayout.SOUTH, controlPanel);
        frame.getContentPane().add(BorderLayout.CENTER, boardPanel);
        frame.getContentPane().add(BorderLayout.NORTH, movePanel);
        
    }
    
    //Menekülő bábú mozgását kezeli le ne tudjon kimenni a pályáról és másik bábunak neki menni. 
    //Iránynak megfelelően mozgatjuk
    //Végén megnézi nem e lett vége a játéknak
    private void escaperMove(String irany) {
        movePanel.setVisible(false);
        int oldX = escaper.x;
        int oldY = escaper.y;
        
        if (irany.equals("Jobbra")) {
            escaper.x += 1;
        } else if (irany.equals("Balra")) {
            escaper.x -= 1;
        } else if (irany.equals("Fel")) {
            escaper.y -= 1;
        } else {
            escaper.y += 1;
        }
        
        if(escaper.x < 0 || escaper.x > board.length-1 || escaper.y < 0 || escaper.y > board.length-1){
            escaper.x  = oldX;
            escaper.y = oldY;
            JOptionPane.showMessageDialog(frame, "Oda nem léphetsz válassz másik irányt!");
            movePanel.setVisible(true);
        }else{
            if(board[escaper.y][escaper.x].getText().equals("")){
                board[oldY][oldX].setText("");
                board[escaper.y][escaper.x].setText("Menekülő");
                jatekos.setText(" Üldöző játékos következik ");
            }else{
                escaper.x  = oldX;
                escaper.y = oldY;
                JOptionPane.showMessageDialog(frame, "Oda nem léphetsz válassz másik irányt!");
                movePanel.setVisible(true);
            }
        }
        
        if(jatekVege()){
            if(moveCount <= 0){
                JOptionPane.showMessageDialog(frame, "A menekülő játékos nyert");
            }else{
                JOptionPane.showMessageDialog(frame, "Az üldöző játékos nyert");
            }
            menu.getFrame().setVisible(true);
            frame.dispose();
        }
    }
    
    //Üldöző bábúk mozgását kezeli le ne tudjon kimenni a pályáról és másik bábunak neki menni. 
    //Először meghatározzuk melyik bábúí lett kiválasztva.
    //Iránynak megfelelően mozgatjuk
    //Végén megnézi nem e lett vége a játéknak.
    private void chaserMove(JButton chaser, String irany) {
        movePanel.setVisible(false);
        int x = -1;
        int y = -1;
        int oldX;
        int oldY;
        
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if(board[i][j] == chaser){
                    y = i;
                    x = j;
                }
                
            }
        }
        
        Chaser chaserc = new Chaser(-1,-1);
        
        for (Chaser enemy : chasers) {
            if(enemy.x == x && enemy.y == y){
                chaserc = enemy;
            }
        }
        
        oldX = chaserc.x;
        oldY = chaserc.y;
        
        if (irany.equals("Jobbra")) {
            chaserc.x += 1;
        } else if (irany.equals("Balra")) {
            chaserc.x -= 1;
        } else if (irany.equals("Fel")) {
            chaserc.y -= 1;
        } else {
            chaserc.y += 1;
        }
        
        
        if(chaserc.x < 0 || chaserc.x > board.length-1 || chaserc.y < 0 || chaserc.y > board.length-1){
            chaserc.x  = oldX;
            chaserc.y = oldY;
            JOptionPane.showMessageDialog(frame, "Oda nem léphetsz válassz másik irányt!");
            movePanel.setVisible(true);
        }else{
            if(board[chaserc.y][chaserc.x].getText().equals("")){
                board[oldY][oldX].setText("");
                board[chaserc.y][chaserc.x].setText("Üldöző");
                jatekos.setText(" Menekülő játékos következik ");
                moveCount -= 1;
                lepes.setText("Lépésszám: " + Integer.toString(moveCount));
            }else{
                chaserc.x  = oldX;
                chaserc.y = oldY;
                JOptionPane.showMessageDialog(frame, "Oda nem léphetsz válassz másik irányt!");
                movePanel.setVisible(true);
            }
        }
        
        if(jatekVege()){
            if(moveCount <= 0){
                JOptionPane.showMessageDialog(frame, "A menekülő játékos nyert");
            }else{
                JOptionPane.showMessageDialog(frame, "Az üldöző játékos nyert");
            }
            menu.getFrame().setVisible(true);
            frame.dispose();
        }
    }
    
    
    //Megnézi hogy a játéknak vége lett e. A játéknak akkor van vége ha nem tud lépni a menekülő bábú vagy elfogyott a lépés.
    public boolean jatekVege() {
        if (moveCount <= 0) {
            return true;
        }
        return (escaper.x + 1 > board.length-1 || board[escaper.y][escaper.x + 1].getText().equals("Üldöző")) 
                && (escaper.x - 1 < 0 || board[escaper.y][escaper.x-1].getText().equals("Üldöző"))
                && (escaper.y + 1 > board.length-1 || board[escaper.y+1][escaper.x].getText().equals("Üldöző"))
                && (escaper.y - 1 < 0 || board[escaper.y-1][escaper.x].getText().equals("Üldöző"));
    }
}
