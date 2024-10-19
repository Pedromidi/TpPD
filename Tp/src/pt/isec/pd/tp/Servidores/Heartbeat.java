package pt.isec.pd.tp.Servidores;

//(cada 10s / alteração base dados)
//número de versão atual da sua base dados local
//porto de escuta TCP automático no qual aguarda pedidos de ligação de servidores de backup para obtenção de uma cópia integral da base de dados
//atualiza verão na tabela SQL
public class Heartbeat implements Runnable{
    Float versaoAtual;
    int TCPport;

    @Override
    public void run() {
        //running
    }
}
