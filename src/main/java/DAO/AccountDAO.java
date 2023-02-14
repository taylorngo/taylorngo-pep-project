package DAO;

import Model.Account;
import Util.ConnectionUtil;
import java.sql.*;

public class AccountDAO {
    /*
     * TODO: insert an account into the Account table
     */
    public Account insertAccount(Account account){
        Connection connection = ConnectionUtil.getConnection();
        try{
            //Write SQL Logic here.
            String sql = "INSERT INTO Account (username, password) VALUES (?,?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            //write prepared statement setString method here
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());

            int rows = preparedStatement.executeUpdate();
            if(rows > 0){
                String sql2 = "SELECT * FROM Account WHERE username = ?;";
                PreparedStatement preparedStatement2 = connection.prepareStatement(sql2);
                preparedStatement2.setString(1, account.getUsername());
                ResultSet resultSet = preparedStatement2.executeQuery();
                while(resultSet.next()){
                    Account acc = new Account(resultSet.getInt("account_id"), resultSet.getString("username"), resultSet.getString("password"));
                    return acc;
                }
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static Account login(Account account){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "SELECT * FROM Account WHERE username = ? AND password = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());

            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                Account loginAccount = new Account(resultSet.getInt("account_id"),
                                                    resultSet.getString("username"),
                                                    resultSet.getString("password"));
                return loginAccount;
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
}
