package DAO;
import Model.Message;
import Util.ConnectionUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class MessageDAO {
    public Message insertMessage(Message msg) {
        Connection conn = ConnectionUtil.getConnection();
        String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, msg.getPosted_by());
            ps.setString(2, msg.getMessage_text());
            ps.setLong(3, msg.getTime_posted_epoch());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                return new Message(id, msg.getPosted_by(), msg.getMessage_text(), msg.getTime_posted_epoch());
            }
        } catch (SQLException e) {
        }
        return null;
    }
    public List<Message> getAllMessages() {
        Connection conn = ConnectionUtil.getConnection();
        List<Message> list = new ArrayList<>();
        String sql = "SELECT * FROM message";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Message msg = new Message(
                        rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch")
                );
                list.add(msg);
            }
        } catch (SQLException e) {
        }
        return list;
    }

    public Message getMessageById(int msgId) {
        Connection conn = ConnectionUtil.getConnection();
        String sql = "SELECT * FROM message WHERE message_id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, msgId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Message(
                        rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch")
                );
            }
        } catch (SQLException e) {
        }
        return null;
    }

    public Message deleteMessageById(int msgId) {
        Message existing = getMessageById(msgId);
        if (existing == null) return null;

        Connection conn = ConnectionUtil.getConnection();
        String sql = "DELETE FROM message WHERE message_id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, msgId);
            ps.executeUpdate();
        } catch (SQLException e) {
        }
        return existing;
    }
    public Message updateMessageText(int msgId, String newText) {
        Connection conn = ConnectionUtil.getConnection();
        String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, newText);
            ps.setInt(2, msgId);
            int affected = ps.executeUpdate();
            if (affected > 0) {
                return getMessageById(msgId);
            }
        } catch (SQLException e) {
        }
        return null;
    }

    public List<Message> getMessagesByAccountId(int userId) {
        Connection conn = ConnectionUtil.getConnection();
        List<Message> userMessages = new ArrayList<>();
        String sql = "SELECT * FROM message WHERE posted_by = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Message msg = new Message(
                        rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch")
                );
                userMessages.add(msg);
            }
        } catch (SQLException e) {
        }
        return userMessages;
    }
}
