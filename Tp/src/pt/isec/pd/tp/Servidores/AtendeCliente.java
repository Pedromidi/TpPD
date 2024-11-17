package pt.isec.pd.tp.Servidores;

import pt.isec.pd.tp.MSG;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class AtendeCliente implements Runnable {
    Socket clientSocket;

    public AtendeCliente(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        String comando;

        try (ObjectInputStream oin = new ObjectInputStream(clientSocket.getInputStream());
             ObjectOutputStream oout = new ObjectOutputStream(clientSocket.getOutputStream())) {


            while(true) {
                //Deserializar o objecto recebido
                comando = (String) oin.readObject();
                System.out.println("Recebido \"" + comando + "\" de " + clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort());

                //Sair da thread se o pedido recebido for diferente do valor dado pela variável Server.TIME_REQUEST
                /*if (!request.equalsIgnoreCase(Server.TIME_REQUEST)) {
                    return;
                }*/

                //Serializar o objecto do tipo Time
                String res = VerificaComando(comando);
                oout.writeObject(res);
                oout.flush();
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Problema na comunicacao com o cliente " + clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort() + "\n\t" + e);
        }
    }

    public String VerificaComando(String comando){
        //login
        comando = comando.toUpperCase();
        String arr[] = comando.split(" ");
        //String firstWord = arr[0];

        switch(arr[0]) {
            case "1": //1 <email> <password>
                return login(comando, arr);

            case "2": //2 <email> <nome> <telefone> <password>
                return registar(comando,arr);

            case "3": //3 n <novoValor> <password>
                return editarPerfil(comando, arr);

            case "4": // 4 <novonome>
                return criarGrupo(comando,arr);

            case "5": //5 <novogrupo>
                return trocarGrupoAtual(comando,arr);

            case "6": //6 <email>
                return criarConvite(comando,arr);

            case "7": //7
                return verConvites(comando,arr);

            case "8": //8 <idconvite> <resposta>
                return responderConvite(comando, arr);

            case "9": //listagrupo
                return listarGrupos(comando,arr);

            case "10": //10 <novonome>
                return editarNomeGrupo(comando,arr);

            case "11": //11 <nome>
                return eliminarGrupo(comando,arr);

            case "12": //12 <grupo>
                return sairGrupo(comando,arr);

            case "13": //13 <valor> <data> <quempagou> ;<partilhados>;<descricao>
                return inserirDespesa(comando,arr);

            case "14": //vergastos
                return verGastos(comando,arr);

            case "15": //verhistoriocodespesas <grupoconcorrente>
                return  verHistoricoDespesas(comando,arr);

            case "16": //exportardespesas <grupoconcorrente>
                return exportarDespesas(comando,arr);

            case "17": //17 <id> <campoAeditar> <novoValor>
                return  editarDespesa(comando,arr);

            case "18": //18 <id>
                return eliminarDespesa(comando,arr);

            case "19": // 19 <quemPagou> <quemrecebeu> <data> <valor>
                return inserirPagamento(comando,arr);

            case "20":
                return verPagamentos(comando,arr);

            case "21": //21 <id>
                return eliminarPagamento(comando,arr);

            case "22": //versaldo <grupoconcorrente>
                return  verSaldos(comando,arr);
        }
        return "";
    }

    public String login(String comando, String[] arr){
        //TODO verificar se existe na base de dados
        return "Login aceite";
    }

    public String registar(String comando, String[] arr){
        //TODO - verifica se já existe este email na BD, arr[1]
            //return "\n Email já existente na Base de Dados";
        //TODO - verifica se já existe este telefone na BD, arr[2]
            //return "\n Telefone já existente na Base de Dados";

        //TODO - adiciona novo utilizador na base de dados
        return "\n O seu registo foi criado com sucesso!";
    }

    /**
     *  Edição dos dados de registo
     */
    public String editarPerfil (String comando, String[] arr){
        //TODO - verifica password na BD, arr[1]
        //return "\nPassword incorreta";
        switch (arr[1]){
            case "1":
                //TODO - altera nome na BD
                break;
            case "2":
                //TODO - verifica se ja existe o telefone na BD
                //return "\nNumero de telefone invalido";
                //TODO - altera telefone na BD
                break;
            case "3":
                //TODO - verifica se ja existe o email na BD
                //return "\nEmail invalido";
                //TODO - altera email na BD
                break;
            case "4":
                //TODO - altera password na BD
                break;
        }
        return "\nCampo alterado com sucesso!";
    }

    /**
     * Criação de um novo grupo, sendo este caracterizado por um nome (por exemplo,
     *  Viagem finalistas deis-isec 2025 - Andorra) que deve ser único no sistema. O
     *  utilizador que cria o grupo passa automaticamente a integrá-lo (primeiro elemento);
     */
    public String criarGrupo (String comando, String[] arr){
        //TODO - verifica se ja esse nome de grupo na BD
        //return "\nNome de Grupo indisponivel";
        //TODO - altera nome do grupo na BD
        return "\nNome do Grupo alterado com sucesso!";
    }

    /**
     * o utilizador escolhe um dos grupos a que pertence e, a partir de esse momento, as
     *      operações que executar referem-se implicitamente a esse grupo
     */
    public String trocarGrupoAtual (String comando, String[] arr){
        //TODO - verifica se cliente pertence ao grupo na BD
        //return "\nNome de Grupo Invalido";
        //TODO - altera o do grupo na BD
        return "\nGrupo atual alterado com sucesso!";
    }

    /**
     *  Criação de convites para adesão a um grupo, sendo os destinatários identificados
     *  através dos seus emails de registo no sistema
     */
    public String criarConvite (String comando, String[] arr){
        //TODO - verifica se existe email o na BD
        //return "\nEmail do destinatario invalido";
        //TODO - criar convite e associar ao cliente do email. (Sem comunicacao com a BD)
        return "\nConvite criado com sucesso!";
    }

    /**
     *     Visualização automática e atualizada dos convites de adesão recebidos/pendentes
     */
    public String verConvites (String comando, String[] arr){
        //TODO listar convites
        return "Ainda por implementar :)";
    }

    /**
     *   Aceitação e recusa de convites de adesão a grupos
     */
    public String  responderConvite (String comando, String[] arr){
        //TODO - verifica se existe convite com o id na sua lista de convites
        //return "\nId do convite invalido";
        switch (arr[2]){
            case "1":
                //TODO - adicionar cliente ao grupo do convite
            case "2":
                //TODO -  eliminar convite
        }
        return "\nAinda por implementar :D";
    }

    /**
     *  Lista dos grupos a que pertence o utilizador autenticado
     */
    public String  listarGrupos (String comando, String[] arr){
        //TODO ir a BD seleciona e depois listar todos os grupos a que pertence o user
        return "\nAinda por implementar :(";
    }

    /**
     * Edição do nome de um grupo por qualquer um dos seus elementos
     */
    public String  editarNomeGrupo(String comando, String[] arr){
        //TODO - verifica se ja existe esse nome de grupo na BD
        //return "\nNome de Grupo indisponivel";
        //TODO - altera nome do grupo na BD
        return "\nNome do Grupo alterado com sucesso!";
    }

    /**
     * Eliminação de um grupo e respetivos dados, desde que não exista qualquer conta por
     * saldar/valor em dívida (ou seja, não podem existir situações de o elemento X deve a
     *  quantia Z ao elemento Y / o elemento Y tem a receber a quantia Z do elemento Y).
     */
    public String  eliminarGrupo(String comando, String[] arr){
        //TODO - verifica se existe grupo o na BD
        //return "\nGrupo invalido";
        //TODO - verificar condicoes para a eliminacao do grupo
        //return "\nEste grupo nao pode ser eliminado";
        //TODO - Eliminar grupo da BD
        return "\nConvite criado com sucesso!";
    }

    /**
     * Saída de um grupo se ainda não existir qualquer despesa associada ao utilizador;
     */
    public String sairGrupo(String comando, String[] arr){
        //TODO - verificar se utilizador está no grupo
        //return "\nGrupo invalido";
        //TODO - verificar condicoes para a eliminacao do grupo
        //return "\nNao pode sair deste grupo! em dividas...";
        //TODO - Eliminar grupo no cliente
        return "\nConvite criado com sucesso!";
    }

    /**
     *Inserção de uma despesa associada ao grupo corrente, por qualquer um dos seus
     * elementos, com: data; descrição; valor; quem pagou; e os elementos com quem é
     * partilhada (pode não incluir quem pagou);
     */
    public String  inserirDespesa(String comando, String[] arr){
        comando = comando.toUpperCase();
        String ast[] = comando.split(";"); //ast[1] -> partilhados, ast[2]-> descricao
        String partilhados[] = ast[1].split(" ");

        //TODO - verificar se todos os emails existem (partilhados e quem pagou)
        //return "\nEmail " + y + " invalido";
        //TODO - adicionar na BD nova despesa
        return "\nDespesa adicionada com sucesso!";
    }

    /**
     *Visualização do valor total de gastos efetuados pelo grupo corrente;
     */
    public String  verGastos(String comando, String[] arr){
        //TODO calcular gastos. Ir a BD no grupo atual e ver todos os clientes, ver despesas associadas aos clientes e somar?
        return "Gastos total do grupo: Ainda nao sei :(";
    }

    /**
     * Visualização do histórico das despesas associadas ao grupo corrente, ordenadas
     * cronologicamente, com todos os detalhes, incluindo a identificação de quem a inseriu
     * no sistema (pode não ser quem efetuou a despesa);
     */
    public String  verHistoricoDespesas(String comando, String[] arr){
        //TODO calcular gastos. Ir a BD no grupo atual e ver todos os clientes, ver despesas associadas aos clientes e somar?
        return "Historico de despesas:\n - hm bem.. \n - uh.. \n - num sei :(";

    }


    /**
     *Exportação, para um ficheiro em formato CSV, da lista de despesas associadas ao
     *grupo corrente, ordenadas cronologicamente e detalhada (ver exemplo na Figura 1);
     */
    public String  exportarDespesas(String comando, String[] arr){
        //TODO servidor faz logica de exportar ja q é ele q tem acesso aos dados maybe
        return "\n....A teoria está lá... Nao testamos ainda...";
    }

    /**
     *Edição dos campos de uma despesa já introduzida no sistema;
     */
    public String  editarDespesa(String comando, String[] arr){
        //TODO - verifica na BD se o id é valido, arr[1]
        //return "\Id invalido";
        switch (arr[1]){
            case "1":
                //TODO - altera data na BD
                break;
            case "2":
                //TODO - altera descricao na BD
                break;
            case "3":
                //TODO - altera valor na BD
                break;
            case "4":
                //TODO - verifica se existe o email na BD
                //return "\nEmail invalido";
                //TODO - altera quemPagou na BD
                break;
            case "5":
                //TODO - verifica se existe(m) o(s) email(s) na BD
                //return "\nEmail invalido";
                //TODO - altera password na BD
                break;
        }
        return "\nCampo alterado com sucesso!";
    }


    /**
     * Eliminação de uma despesa;
     */
    public String  eliminarDespesa(String comando, String[] arr){
        //TODO verificar se o id existe
        //return "\nId de despesa invalido"
        //TODO eliminar a despesa
        return "\nDespesa eliminada com sucesso";
    }

    /**
     *Para efeitos de liquidação/acerto das contas, inserção de um pagamento efetuado
     * por um elemento do grupo corrente a outro elemento do mesmo grupo, com
     * indicação de: quem pagou; quem recebeu; data; e valor;
     */
    public String  inserirPagamento(String comando, String[] arr){
        //TODO verificar se os emails sao validos
        //return "\nEmail invalido";
        //TODO adicionar pagamento
        return "\nPagamento adicionado com sucesso";
    }

    /**
     * Listagem dos pagamentos efetuados entre elementos do grupo;
     */
    public String  verPagamentos(String comando, String[] arr){
        //TODO busca e lista pagamentos
        return "\nHmm";
    }

    /**
     * Eliminação de um pagamento efetuado por um elemento a outro elemento;
     */
    public String  eliminarPagamento(String comando, String[] arr){
        //TODO verificar se o id é valido
        //return "\nId invalido";
        //TODO eliminar pagamento
        return "\nPagamento adicionado com sucesso";
    }


    /**
     * Visualização dos saldos do grupo corrente com, para cada elemento, indicação do:
     *    o gasto total;
     *    o valor total que deve;
     *    o valor que que deve a cada um dos restantes elementos;
     *    o valor total que tem a receber;
     *    o valor que tem a receber de cada um dos restantes elementos;
     */
    public String  verSaldos(String comando, String[] arr){
        //TODO calcular saldos. Ir a BD no grupo atual
        return "Gastos total do grupo: Ainda nao sei :(";
    }


}
