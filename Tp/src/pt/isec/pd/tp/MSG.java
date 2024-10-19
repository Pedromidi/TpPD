package pt.isec.pd.tp;

import pt.isec.pd.tp.Cliente.Cliente;

import java.util.List;

public class MSG {
    public String comando;
    private List<Despesa> despesas;
    private List<Grupo> grupos;
    private List<Pagamento> pagamentos;
    private List<Convite> convites;
    boolean aceite;

    public MSG(String comando, boolean aceite) {
        this.comando = comando;
        this.aceite = aceite;
    }
}
