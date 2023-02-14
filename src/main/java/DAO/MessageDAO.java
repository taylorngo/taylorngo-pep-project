package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO {

    public Message createMessage(Message message){
        Connection connection = ConnectionUtil.getConnection();

        try{
            String sql = "INSERT INTO Message (posted_by, message_text, time_posted_epoch) VALUES (?,?,?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());

            int rows = preparedStatement.executeUpdate();

            if(rows > 0){
                String sql2 ="SELECT * FROM Message WHERE time_posted_epoch = ?;";
                PreparedStatement preparedStatement2 = connection.prepareStatement(sql2);
                preparedStatement2.setLong(1, message.getTime_posted_epoch());
                ResultSet rs = preparedStatement2.executeQuery();
                while(rs.next()){
                    Message msg = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                    return msg;
                }
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public List<Message> getAllMessage(){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();

        try{
            String sql = "SELECT * FROM Message;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                Message message = new Message(resultSet.getInt("message_id"),
                                          resultSet.getInt("posted_by"),
                                          resultSet.getString("message_text"),
                                          resultSet.getLong("time_posted_epoch"));
                messages.add(message);
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }


    public Message getMessage(int id){
        Connection connection = ConnectionUtil.getConnection();

        try{
            String sql = "SELECT * FROM Message WHERE message_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                Message message = new Message(resultSet.getInt("message_id"),
                                              resultSet.getInt("posted_by"),
                                              resultSet.getString("message_text"),
                                              resultSet.getLong("time_posted_epoch"));
                return message;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Message deleteMessage(int id){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "DELETE FROM Message WHERE message_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, id);

            int rows = preparedStatement.executeUpdate();
            if(rows == 1){
                String sql2 = "SELECT * FROM Message WHERE message_id = ?;";
                PreparedStatement preparedStatement2 = connection.prepareStatement(sql2);
                preparedStatement2.setInt(1, id);
                ResultSet resultSet = preparedStatement2.executeQuery();

                while(resultSet.next()){
                    Message message = new Message(resultSet.getInt("message_id"), resultSet.getInt("posted_by"), resultSet.getString("message_text"), resultSet.getLong("time_posted_epoch"));
                    return message;
                }
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Message updateMessage(int id, String updateString){
        Connection connection = ConnectionUtil.getConnection();

        try{
            String sql = "UPDATE Message SET message_text = ? WHERE message_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, updateString);
            preparedStatement.setInt(2, id);
            int rows = preparedStatement.executeUpdate();

            if(rows > 0) {
                String sql2 = "SELECT * FROM Message WHERE message_id = ?;";
                PreparedStatement preparedStatement2 = connection.prepareStatement(sql2);
                preparedStatement2.setInt(1, id);
                
                ResultSet resultSet = preparedStatement2.executeQuery();
                while(resultSet.next()){
                    Message message = new Message(resultSet.getInt("message_id"), resultSet.getInt("posted_by"), resultSet.getString("message_text"), resultSet.getLong("time_posted_epoch"));
                    return message;
                }
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    

    public List<Message> getAllAccountMessage(int id){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();

        try{
            String sql = "SELECT * FROM Message WHERE posted_by = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                Message message = new Message(resultSet.getInt("message_id"), resultSet.getInt("posted_by"), resultSet.getString("message_text"), resultSet.getLong("time_posted_epoch"));
                messages.add(message);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }
}
