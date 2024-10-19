package pt.isec.pd.tp;

import pt.isec.pd.tp.Cliente.Cliente;

import java.util.ArrayList;
import java.util.List;

public class Grupo {
    float saldo;

    private String nome;
    private List<Cliente> membros;
    private List<Despesa> despesas;

    public Grupo(String nome){
        this.nome = nome;
        this.membros = new ArrayList<>();
        this.despesas = new ArrayList<>();
    }

    public List<Despesa> getDespesas(){
        return despesas;
    }
}
