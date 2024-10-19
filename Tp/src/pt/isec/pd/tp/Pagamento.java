package pt.isec.pd.tp;

import pt.isec.pd.tp.Cliente.Cliente;

public class Pagamento {
    private Cliente pagador;
    private Cliente receptor;
    private double valor;
    private String data;

    public Pagamento(Cliente pagador, Cliente receptor, double valor, String data) {
        this.pagador = pagador;
        this.receptor = receptor;
        this.valor = valor;
        this.data = data;
    }

    // Getters e Setters
    public Cliente getPagador() {
        return pagador;
    }

    public void setPagador(Cliente pagador) {
        this.pagador = pagador;
    }

    public Cliente getReceptor() {
        return receptor;
    }

    public void setReceptor(Cliente receptor) {
        this.receptor = receptor;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Pagamento{" +
                "pagador=" + pagador.getNome() +
                ", receptor=" + receptor.getNome() +
                ", valor=" + valor +
                ", data='" + data + '\'' +
                '}';
    }
}
