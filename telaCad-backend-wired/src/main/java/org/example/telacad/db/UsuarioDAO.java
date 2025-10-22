package org.example.telacad.db;

import org.example.telacad.models.Usuario;
import java.sql.*;

public class UsuarioDAO {

    public boolean emailExiste(String email) throws SQLException {
        String sql = "SELECT 1 FROM usuario WHERE email = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public void cadastrarAlunoBasico(String email, String nome, String curso, String senhaEmTexto) throws SQLException {
        String sql = "INSERT INTO usuario (email, nome, curso, senha, perfil, _status) VALUES (?, ?, ?, ?, 1, 1)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, nome);
            ps.setString(3, curso);
            ps.setString(4, senhaEmTexto); // TODO: aplicar hash posteriormente
            ps.executeUpdate();
        }
    }

    public Usuario autenticar(String email, String senhaEmTexto) throws SQLException {
        String sql = "SELECT email, nome, curso, senha, perfil, _status FROM usuario WHERE email = ? AND senha = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, senhaEmTexto); // TODO: comparar hash
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
                        rs.getString("email"),
                        rs.getString("nome"),
                        rs.getString("curso"),
                        rs.getString("senha"),
                        rs.getInt("perfil"),
                        rs.getInt("_status")
                    );
                } else {
                    return null;
                }
            }
        }
    }
}
