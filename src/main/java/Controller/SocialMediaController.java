package Controller;

import static org.mockito.ArgumentMatchers.contains;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 * Endpoints needed:
 * 
 * POST localhost:8080/register : register new user account
 * 
 * POST localhost:8080/login : login to existing account
 * 
 * POST localhost:8080/messages : process creation of new message
 * 
 * GET localhost:8080/messages : retrieve all messages
 * 
 * GET localhost:8080/messages/{message_id} : retrieve message by its message id
 * 
 * DELETE localhost:8080/messages/{message_id} : delete a message identified by its message id
 * 
 * PATCH localhost:8080/messages/{message_id} : update a message text by its message id
 * 
 * GET localhost:8080/accounts/{account_id}/messages : retrieve all messages written by a particular user
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController(){
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::registerHandler);
        app.post("/login", this::loginHandler);
        app.post("/messages", this::postMessageHandler);
        app.get("/messages", this::getAllMessageHandler);
        app.get("/messages/{message_id}", this::getMessageHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);
        app.patch("/messages/{message_id}", this::patchMessageHandler);
        app.get("/accounts/{account_id}/messages", this::getAccountMessageHandler);

        return app;
    }

    /**
     * This is an example handler for a register endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void registerHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account addedAccount = accountService.addAccount(account);
        if (account.getUsername() != "" && account.getPassword().length() > 4 && addedAccount != null){
            context.json(mapper.writeValueAsString(addedAccount));
            context.status(200);
        }else{
            context.status(400);
        }
    }
    private void loginHandler(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account loginAccount = accountService.login(account);

        if(loginAccount != null){
            context.json(mapper.writeValueAsString(loginAccount));
            context.status(200);
        }
        else{
            context.status(401);
        }
        
    }
    private void postMessageHandler(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        Message newMessage = messageService.addMessage(message);
        if(newMessage != null){
            context.json(mapper.writeValueAsString(newMessage));
            context.status(200);
        }else{
            context.status(400);
        }
        
    }
    private void getAllMessageHandler(Context context){
        List<Message> messages = messageService.getAllMessage();
        context.json(messages);
        context.status(200);

    }
    private void getMessageHandler(Context context){
        int id = Integer.parseInt(context.pathParam("message_id"));
        Message message = messageService.getMessage(id);
        if(message != null){
            context.json(message);
            context.status(200);
        }else{
            context.status(404);
        }
    }
    private void deleteMessageHandler(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        int id = Integer.parseInt(context.pathParam("message_id"));
        Message message = messageService.deleteMessage(id);
        if(message != null){
            context.json(mapper.writeValueAsString(message));
            context.status(200);
        }else{
            context.status(200);
        }
    }
    private void patchMessageHandler(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        JsonNode json = mapper.readTree(context.body());
        String newMessgageString = json.get("message_text").asText();
        Message updatedMessaged = messageService.updateMessage(message_id, newMessgageString);
        if(updatedMessaged != null){
            context.json(mapper.writeValueAsString(updatedMessaged));
            context.status(200);
        } else{
            context.status(400);
        }

    }
    private void getAccountMessageHandler(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        int accountId = Integer.parseInt(context.pathParam("account_id"));
        List<Message> messages = messageService.getAccountMessage(accountId);
        if(messages != null){
            context.json(mapper.writeValueAsString(messages));
            context.status(200);
        }else{
            context.json(messages);
            context.status(200);
        }

    } 


}