package org.example.telacad.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.example.telacad.models.Solicitacao;

public class SolicitacaoDAO {
    private Connection conn;

    public SolicitacaoDAO(Connection conn) {
        this.conn = conn;
    }

    // Inserir nova solicitação
    public void inserirSolicitacao(Solicitacao solicitacao) throws SQLException {
        String sql = "INSERT INTO solicitacao (email_aluno, email_prof, _status) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, solicitacao.getEmailAluno());
            stmt.setString(2, solicitacao.getEmailProf());
            stmt.setInt(3, solicitacao.getStatus());
            stmt.executeUpdate();
        }
    }

    // Buscar solicitações por professor
    public List<Solicitacao> listarPorProfessor(String emailProf) throws SQLException {
        List<Solicitacao> lista = new ArrayList<>();
        String sql = "SELECT * FROM solicitacao WHERE email_prof = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, emailProf);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Solicitacao s = new Solicitacao();
                s.setEmailAluno(rs.getString("email_aluno"));
                s.setEmailProf(rs.getString("email_prof"));
                s.setStatus(rs.getInt("_status"));
                lista.add(s);
            }
        }
        return lista;
    }
}
