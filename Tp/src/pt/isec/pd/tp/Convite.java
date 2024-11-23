package pt.isec.pd.tp;

import java.util.ArrayList;
import java.util.List;

public class Convite {
    private String remetente;
    private String destinatario;
    private String estado; // "Pendente", "Aceito", "Recusado"?
    private int id;

    public Convite(String remetente, String destinatario, String estado) {
        this.remetente = remetente;
        this.destinatario = destinatario;
        this.estado = estado;
    }

    // Getters e Setters
    public String getRemetente() {
        return remetente;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Convite de " + remetente + " para " + destinatario + " [Estado: " + estado + "]";
    }
}
