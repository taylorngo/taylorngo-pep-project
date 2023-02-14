package Service;

import java.util.List;

import DAO.MessageDAO;
import Model.Message;

public class MessageService {
    private MessageDAO messageDAO;

    public MessageService(){
        messageDAO = new MessageDAO();
    }

    public MessageService(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
    }

    public Message addMessage(Message message){
        if(message.getMessage_text().length() > 0 && message.getMessage_text().length() < 255 && message.getPosted_by() >= 0){
            return messageDAO.createMessage(message);
        }else{
            return null;
        }
    }

    public List<Message> getAllMessage(){
        return messageDAO.getAllMessage();
    }
    
    public Message getMessage(int id){
        return messageDAO.getMessage(id);
    }

    public Message deleteMessage(int id){
        if(messageDAO.getMessage(id) != null){
            Message message = messageDAO.getMessage(id);
            messageDAO.deleteMessage(id);
            return message;
        }else{
            return null;
        }
    }

    public Message updateMessage(int id, String updateString){
        if(messageDAO.getMessage(id) != null && updateString.length() > 0 && updateString.length() < 255){
            return messageDAO.updateMessage(id, updateString);
        }else{
            return null;
        }
    }

    public List<Message> getAccountMessage(int id){
        return messageDAO.getAllAccountMessage(id);
    }
}
