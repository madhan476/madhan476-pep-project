package Service;

import DAO.MessageDAO;
import Model.Message;

import java.util.List;

public class MessageService {
    private MessageDAO messageDAO;

    public MessageService() {
        this.messageDAO = new MessageDAO();
    }

    public Message createMessage(Message msg) {
        if (msg.getMessage_text() == null || msg.getMessage_text().isBlank()) return null;
        if (msg.getMessage_text().length() > 255) return null;
        if (msg.getPosted_by() <= 0) return null;

        return messageDAO.insertMessage(msg);
    }

    public List<Message> fetchAllMessages() {
        return messageDAO.getAllMessages();
    }

    public Message fetchMessageById(int messageId) {
        return messageDAO.getMessageById(messageId);
    }

    public Message removeMessage(int messageId) {
        return messageDAO.deleteMessageById(messageId);
    }

    public Message modifyMessageText(int messageId, String newText) {
        if (newText == null || newText.isBlank() || newText.length() > 255) return null;
        return messageDAO.updateMessageText(messageId, newText);
    }

    public List<Message> fetchMessagesByUser(int accountId) {
        return messageDAO.getMessagesByAccountId(accountId);
    }
}