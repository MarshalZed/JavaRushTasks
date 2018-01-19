package task3310.strategy;

import java.sql.*;

public class JDBCStorageStrategy implements StorageStrategy {
    private Connection connection;

    public boolean connect(String dbURL){
        try {
            connection = null;
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(dbURL);
            return true;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void closeConnection(){
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM storage");
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean containsKey(Long key) {
        boolean result = false;
        String queryText = "SELECT hash FROM storage WHERE hash=? LIMIT 1 ";
        try (PreparedStatement prepStmt = connection.prepareStatement(queryText)){
            prepStmt.setLong(1, key);
            ResultSet resultSet = prepStmt.executeQuery();
            if (resultSet.next()){
                result = true;
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean containsValue(String value) {
        boolean result = false;
        String queryText = "SELECT string FROM storage WHERE string=? LIMIT 1 ";
        try (PreparedStatement prepStmt = connection.prepareStatement(queryText)){
            prepStmt.setString(1, value);
            ResultSet resultSet = prepStmt.executeQuery();
            if (resultSet.next()){
                result = true;
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void put(Long key, String value) {
        String queryText = "INSERT INTO storage (hash, string) VALUES (?,?)";
        try (PreparedStatement prepStmt = connection.prepareStatement(queryText)){
            prepStmt.setLong(1, key);
            prepStmt.setString(2, value);
            prepStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Long getKey(String value) {
        Long result = 0L;
        String queryText = "SELECT hash FROM storage WHERE string=? LIMIT 1";
        try (PreparedStatement prepStmt = connection.prepareStatement(queryText)){
            prepStmt.setString(1, value);
            ResultSet resultSet = prepStmt.executeQuery();
            if (resultSet.next()){
                result = resultSet.getLong("hash");
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public String getValue(Long key) {
        String result = "";
        String queryText = "SELECT string FROM storage WHERE hash=? LIMIT 1 ";
        try (PreparedStatement prepStmt = connection.prepareStatement(queryText)){
            prepStmt.setLong(1, key);
            ResultSet resultSet = prepStmt.executeQuery();
            if (resultSet.next()){
                result = resultSet.getString("string");
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
