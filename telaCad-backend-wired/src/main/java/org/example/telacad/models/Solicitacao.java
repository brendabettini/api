package org.example.telacad.models;

public class Solicitacao {
    private String emailAluno;
    private String emailProf;
    private int status; // 1: pendente, 2: aprovada, 3: recusada

    

    public String getEmailAluno() { 
        return emailAluno; 
    }
    public String getEmailProf() {
         return emailProf; 
        }
    public int getStatus() {
         return status; 
        }

    public void setEmailAluno(String emailAluno) {
         this.emailAluno = emailAluno; 
        }
    public void setEmailProf(String emailProf) {
         this.emailProf = emailProf; 
        }

    public void setStatus(int status) {
         this.status = status; 
        }
}