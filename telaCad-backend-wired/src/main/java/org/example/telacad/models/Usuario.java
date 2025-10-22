package org.example.telacad.models;

public class Usuario {
    
    private String email;
    private String nome;
    private String curso;
    private String senha;
    private int perfil;
    private int status;

    public Usuario(String email, String nome, String curso, String senha, int perfil, int status) {
        this.email = email;
        this.nome = nome;
        this.curso = curso;
        this.senha = senha;
        this.perfil = perfil;
        this.status = status;
    }

    public String getEmail() { return email; }
    public String getNome() { return nome; }
    public String getCurso() { return curso; }
    public String getSenha() { return senha; }
    public int getPerfil() { return perfil; }
    public int getStatus() { return status; }

    public void setPerfil(int p) { this.perfil = p; }
    public void setStatus(int s) { this.status = s; }
}
