package beadando_3;

import java.awt.BorderLayout;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


public class Leaderboard{
    
    public static void saveScore(String name, int score) {
        try {
            
            String url = "jdbc:derby://localhost:1527/sample";
            Connection conn = DriverManager.getConnection(url,"app","app");
            String query = "INSERT INTO leaderboard (name, score) VALUES ('" + name + "', " + score + ")";
            // Először ellenőrizzük, hogy a név már létezik-e
            String checkQuery = "SELECT score FROM leaderboard WHERE name = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setString(1, name);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
            // Ha a név már létezik, ellenőrizzük a pontszámot
            int existingScore = rs.getInt("score");
            if (score > existingScore) {
                // Csak akkor frissítjük, ha az új pontszám nagyobb
                String updateQuery = "UPDATE leaderboard SET score = ? WHERE name = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
                updateStmt.setInt(1, score);
                updateStmt.setString(2, name);
                updateStmt.executeUpdate();
                System.out.println("Score updated for " + name);
            } else {
                System.out.println("Score not updated; existing score is higher or equal.");
            }
            } else {
                // Ha a név nem létezik, beszúrjuk az új rekordot
                String insertQuery = "INSERT INTO leaderboard (name, score) VALUES (?, ?)";
                PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
                insertStmt.setString(1, name);
                insertStmt.setInt(2, score);
                insertStmt.executeUpdate();
                System.out.println("New score saved for " + name);
            }

            System.out.println("Score saved successfully!");

            // Kapcsolat lezárása
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error saving score.");
        }
    }

    public static void showTopScores() {
        JFrame frame = new JFrame("Top Scores");
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Táblázat modell inicializálása
        DefaultTableModel tableModel = new DefaultTableModel(new String[]{"Name", "Score"}, 0);
        JTable table = new JTable(tableModel);
        try {
            // Adatbázis kapcsolat
            String url = "jdbc:derby://localhost:1527/sample";
            Connection conn = DriverManager.getConnection(url, "app", "app");

            // Lekérdezés a legjobb 10 pontszám lekérésére
            String query = "SELECT name, score FROM leaderboard ORDER BY score DESC FETCH FIRST 10 ROWS ONLY";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);


            while (rs.next()) {
                String name = rs.getString("name");
                int score = rs.getInt("score");
                tableModel.addRow(new Object[]{name, score});
            }

            // Kapcsolat lezárása
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error retrieving scores.");
        }
        
        // Táblázat hozzáadása a kerethez
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Keret megjelenítése
        frame.setVisible(true);
    }
}

