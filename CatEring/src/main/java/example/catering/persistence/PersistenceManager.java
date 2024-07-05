package example.catering.persistence;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.stream.Collectors;

public class PersistenceManager {
    private static final String url = "jdbc:mysql://localhost:3306/catering?serverTimezone=UTC";
    private static final String username = "root";
    private static final String password = "4321test";

    // Usare la seguente stringa per connettersi a un database PostgreSQL
    // private static String url = String.format("jdbc:postgresql://localhost/catering?ssl=true");

    private static int lastId;

    public static String escapeString(String input) {
        input = input.replace("\\", "\\\\");
        input = input.replace("'", "\\'");
        input = input.replace("\"", "\\\"");
        input = input.replace("\n", "\\n");
        input = input.replace("\t", "\\t");
        return input;
    }

    public static void testSQLConnection() {
        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM Users");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("username");
                System.out.println(name + " ha id = " + id);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void executeQuery(String query, ResultHandler handler) {
        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                handler.handle(rs);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    public static int[] executeBatchUpdate(String parametrizedQuery, int itemNumber, BatchUpdateHandler handler) {
        int[] result = new int[0];
        try (
                Connection conn = DriverManager.getConnection(url, username, password);
                PreparedStatement ps = conn.prepareStatement(parametrizedQuery, Statement.RETURN_GENERATED_KEYS)
        ) {
            for (int i = 0; i < itemNumber; i++) {
                handler.handleBatchItem(ps, i);
                ps.addBatch();
            }
            result = ps.executeBatch();
            ResultSet keys = ps.getGeneratedKeys();
            int count = 0;
            while (keys.next()) {
                handler.handleGeneratedIds(keys, count);
                count++;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return result;
    }

    public static int executeUpdate(String update) {
        int result = 0;
        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = conn.prepareStatement(update, Statement.RETURN_GENERATED_KEYS)) {
            result = ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                lastId = rs.getInt(1);
            } else {
                lastId = 0;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public static int getLastId() {
        return lastId;
    }

    private static String readSqlFile(String filePath) throws IOException {
        return Files.lines(Paths.get(filePath)).collect(Collectors.joining("\n"));
    }

    public static void executeSqlFile(String filePath) {
        try {
            String sqlCommands = readSqlFile(filePath);
            String[] commands = sqlCommands.split(";");

            Connection conn = DriverManager.getConnection(url, username, password);
            Statement stmt = conn.createStatement();

            for (String command : commands) {
                if (!command.trim().isEmpty()) {
                    stmt.execute(command);
                }
            }

            stmt.close();
            conn.close();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}
