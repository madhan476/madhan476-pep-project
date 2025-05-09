package Controller;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;

public class SocialMediaController {
    private AccountService accountService;
    private MessageService messageService;

    public SocialMediaController() {
        accountService = new AccountService();
        messageService = new MessageService();
    }

    public Javalin startAPI() {
        Javalin app = Javalin.create();

        app.post("/register", this::handleRegister);
        app.post("/login", this::handleLogin);

        app.post("/messages", this::handleCreateMessage);
        app.get("/messages", this::handleGetAllMessages);
        app.get("/messages/{message_id}", this::handleGetMessageById);
        app.delete("/messages/{message_id}", this::handleDeleteMessage);
        app.patch("/messages/{message_id}", this::handleUpdateMessage);
        app.get("/accounts/{account_id}/messages", this::handleGetMessagesByAccount);

        return app;
    }

    private void handleRegister(Context ctx) {
        Account newAccount = ctx.bodyAsClass(Account.class);
        Account savedAccount = accountService.register(newAccount);
        if (savedAccount != null) {
            ctx.json(savedAccount);
        } else {
            ctx.status(400);
        }
    }

    private void handleLogin(Context ctx) {
        Account loginData = ctx.bodyAsClass(Account.class);
        Account foundAccount = accountService.login(loginData);
        if (foundAccount != null) {
            ctx.json(foundAccount);
        } else {
            ctx.status(401);
        }
    }

    private void handleCreateMessage(Context ctx) {
        Message incoming = ctx.bodyAsClass(Message.class);
        Message created = messageService.createMessage(incoming);
        if (created != null) {
            ctx.json(created);
        } else {
            ctx.status(400);
        }
    }

    private void handleGetAllMessages(Context ctx) {
        ctx.json(messageService.fetchAllMessages());
    }

    private void handleGetMessageById(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("message_id"));
        Message result = messageService.fetchMessageById(id);
        if (result != null) {
            ctx.json(result);
        } else {
            ctx.result("");
        }
    }

    private void handleDeleteMessage(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("message_id"));
        Message deleted = messageService.removeMessage(id);
        if (deleted != null) {
            ctx.json(deleted);
        } else {
            ctx.result("");
        }
    }

    private void handleUpdateMessage(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("message_id"));
        Message data = ctx.bodyAsClass(Message.class);
        Message updated = messageService.modifyMessageText(id, data.getMessage_text());
        if (updated != null) {
            ctx.json(updated);
        } else {
            ctx.status(400);
        }
    }

    private void handleGetMessagesByAccount(Context ctx) {
        int accountId = Integer.parseInt(ctx.pathParam("account_id"));
        ctx.json(messageService.fetchMessagesByUser(accountId));
    }
}
