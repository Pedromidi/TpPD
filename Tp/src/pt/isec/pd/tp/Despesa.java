package pt.isec.pd.tp;

import pt.isec.pd.tp.Cliente.Cliente;

import java.util.List;

public class Despesa {
    private int id;
    private String descricao;
    private double valor;
    private Cliente pagador; //provalvelmente não
    private List<Cliente> divididaCom;
    private String data;

    public Despesa(String descricao, double valor, Cliente pagador,
                   List<Cliente> dividaCom, String data) {
        this.descricao = descricao;
        this.valor = valor;
        this.pagador = pagador;
        this.divididaCom = dividaCom;
        this.data = data;
    }

    public String getData(){
        return data;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public Cliente getPagador() {
        return pagador;
    }

    public void setPagador(Cliente pagador) {
        this.pagador = pagador;
    }

    public List<Cliente> getDivididaCom() {
        return divididaCom;
    }

    @Override
    public String toString() {
        return "Despesa{" +
                "descricao='" + descricao + '\'' +
                ", valor=" + valor +
                ", pagador=" + pagador.getNome() +
                ", divididaCom=" + divididaCom +
                ", data='" + data + '\'' +
                '}';
    }
}
