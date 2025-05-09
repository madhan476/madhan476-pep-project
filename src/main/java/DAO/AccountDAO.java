package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountDAO {

    public Account insertAccount(Account acc) {
        Connection conn = ConnectionUtil.getConnection();
        String sql = "INSERT INTO account (username, password) VALUES (?, ?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, acc.getUsername());
            ps.setString(2, acc.getPassword());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                return new Account(id, acc.getUsername(), acc.getPassword());
            }
        } catch (SQLException e) {
            // log or handle
        }
        return null;
    }

    public Account getAccountByUsername(String uname) {
        Connection conn = ConnectionUtil.getConnection();
        String sql = "SELECT * FROM account WHERE username = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, uname);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Account(
                        rs.getInt("account_id"),
                        rs.getString("username"),
                        rs.getString("password")
                );
            }
        } catch (SQLException e) {
            // log or handle
        }
        return null;
    }

    public Account getAccountByUsernameAndPassword(String uname, String pwd) {
        Connection conn = ConnectionUtil.getConnection();
        String sql = "SELECT * FROM account WHERE username = ? AND password = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, uname);
            ps.setString(2, pwd);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Account(
                        rs.getInt("account_id"),
                        rs.getString("username"),
                        rs.getString("password")
                );
            }
        } catch (SQLException e) {
            // log or handle
        }
        return null;
    }
}
