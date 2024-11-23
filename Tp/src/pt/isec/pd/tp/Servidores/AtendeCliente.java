package pt.isec.pd.tp.Servidores;

//import pt.isec.pd.tp.MSG;

import pt.isec.pd.tp.Convite;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Objects;

public class AtendeCliente implements Runnable {
    Socket clientSocket;
    DbManager db;
    String email;
    String grupoAtual;

    public AtendeCliente(Socket clientSocket, DbManager db) {
        this.clientSocket = clientSocket;
        this.db = db;
        email = grupoAtual = null;
    }

    @Override
    public void run() {
        String comando;

        try {
            clientSocket.setSoTimeout(60000);
        } catch (SocketException e) {
            e.printStackTrace();
        }

        try (ObjectInputStream oin = new ObjectInputStream(clientSocket.getInputStream());
             ObjectOutputStream oout = new ObjectOutputStream(clientSocket.getOutputStream())) {

            while(true) {
                //Deserializar o objecto recebido
                comando = (String) oin.readObject();
                System.out.println("Recebido \"" + comando + "\" de " + clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort());

                //Serializar o objecto
                String res = VerificaComando(comando);
                oout.writeObject(res);
                oout.flush();
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Problema na comunicacao com o cliente " + clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort() + "\n\t" + e);
        }
    }

    public String VerificaComando(String comando) throws IOException {
        //login
        comando = comando.toLowerCase();
        String[] arr = comando.split(" ");
        //String firstWord = arr[0];

        switch (arr[0]) {
            case "1" -> { //1 <email> <password>
                clientSocket.setSoTimeout(0);
                return login(arr);
            }
            case "2" -> { //2 <email> <nome> <telefone> <password>
                clientSocket.setSoTimeout(0);
                return registar(arr);
            }
            case "3" -> { //3 n <novoValor> <password>
                return editarPerfil(arr);
            }
            case "4" -> { // 4 <novonome>
                return criarGrupo(arr);
            }
            case "5" -> { //5 <novogrupo>
                return trocarGrupoAtual(arr);
            }
            case "6" -> { //6 <email>
                return criarConvite(arr);
            }
            case "7" -> { //7
                return verConvites();
            }
            case "8" -> { //8 <idconvite> <resposta>
                return responderConvite(arr);
            }
            case "9" -> { //listagrupo
                return listarGrupos();
            }
            case "10" -> { //10 <novonome>
                return editarNomeGrupo(arr);
            }
            case "11" -> { //11 <nome>
                return eliminarGrupo(arr);
            }
            case "12" -> { //12
                return sairGrupo(arr);
            }
            case "13" -> { //13 <valor> <data> <quempagou> ;<partilhados>;<descricao>
                return inserirDespesa(comando, arr);
            }
            case "14" -> { //vergastos
                return verGastos();
            }
            case "15" -> { //verhistoriocodespesas
                return verHistoricoDespesas();
            }
            case "16" -> { //exportardespesas
                return exportarDespesas();
            }
            case "17" -> { //17 <id> <campoAeditar> <novoValor>
                return editarDespesa(comando, arr);
            }
            case "18" -> { //18 <id>
                return eliminarDespesa(arr);
            }
            case "19" -> { // 19 <quemPagou> <quemrecebeu> <data> <valor>
                return inserirPagamento(arr);
            }
            case "20" -> {
                return verPagamentos();
            }
            case "21" -> { //21 <id>
                return eliminarPagamento(arr);
            }
            case "22" -> { //versaldo
                return verSaldos();
            }
        }
        return "";
    }

    /**
     *  Autenticacao do cliente.
     *  Verifica se o email existe na base de dados.
     *  De seguida verifica se a password corresponde
     */
    public String login(String[] arr) throws IOException {

        if(!db.verificaEmail(arr[1])){
            clientSocket.close();
            return "\nEmail incorreto";
        }
        email = arr[1];
        if(!db.verificaPassword(email, arr[2])){
            clientSocket.close();
            return "\nPassword incorreta";
        }
        return "Login aceite! Bem vindo " + email;

    }

    public String registar(String[] arr) {
        if (db.verificaEmail(arr[1])) {
            return "\nEmail já existente na Base de Dados.";
        }

        if (db.verificaTelefone(arr[3])) {
            return "\nTelefone já existente na Base de Dados.";
        }

        //Adicionar novo utilizador
        boolean sucesso = db.adicionaRegisto(arr[1], arr[2], arr[3], arr[4]); // arr[1] = email, arr[2] = nome, arr[3] = telefone, arr[4] = password
        if (sucesso) {
            email = arr[1];
            db.incDbVersion();
            return "\nO seu registo foi criado com sucesso!";
        } else {
            return "\nOcorreu um erro ao criar o seu registo. Por favor, tente novamente.";
        }
    }

    /**
     * Edição dos dados de registo.
     * 0 - comando, 1 - campo, 2 - novocampo, 3 - pass
     */
    public String editarPerfil(String[] arr) {
        // Verificar password na base de dados
        if(!db.verificaPassword(email, arr[3])){
            return "\nPassword incorreta";
        }

        switch (arr[1]) {
            case "1" -> { // Alterar nome
                boolean alterouNome = db.alteraCampoPerfil(email,"nome" ,arr[2]);
                if (alterouNome) {
                    return "\nNome alterado com sucesso!";
                } else {
                    return "\nOcorreu um erro ao alterar o nome.";
                }
            }
            case "2" -> { // Alterar telefone
                if (db.verificaTelefone(arr[2])) {
                    return "\nNúmero de telefone inválido.";
                }
                boolean alterouTelefone = db.alteraCampoPerfil(email,"telefone" ,arr[2]);
                if (alterouTelefone) {
                    return "\nTelefone alterado com sucesso!";
                } else {
                    return "\nOcorreu um erro ao alterar o telefone.";
                }
            }
            case "3" -> { // Alterar email
                if (db.verificaEmail(arr[2])) {
                    return "\nEmail inválido.";
                }
                boolean alterouEmail = db.alteraCampoPerfil(email,"email" ,arr[2]);
                if (alterouEmail) {
                    email = arr[2];
                    return "\nEmail alterado com sucesso!";
                } else {
                    return "\nOcorreu um erro ao alterar o email.";
                }
            }
            case "4" -> { // Alterar password
                boolean alterouPassword = db.alteraCampoPerfil(email,"password" ,arr[2]);
                if (alterouPassword) {
                    return "\nPassword alterada com sucesso!";
                } else {
                    return "\nOcorreu um erro ao alterar a password.";
                }
            }
            default -> {
                return "\nOpção inválida.";
            }
        }
    }

    public String criarGrupo(String[] arr) {

        if (db.verificaGrupo(arr[1])) { // arr[1] = nome do grupo
            return "\nNome de grupo indisponível.";
        }

        boolean grupoCriado = db.criaGrupo(email, arr[1]); // email é o utilizador autenticado no sistema
        if (grupoCriado) {
            grupoAtual = arr[1];
            db.incDbVersion();
            return "\nGrupo criado com sucesso!";
        } else {
            return "\nOcorreu um erro ao criar o grupo.";
        }
    }

    /**
     * o utilizador escolhe um dos grupos a que pertence e, a partir de esse momento, as
     * operações que executar referem-se implicitamente a esse grupo
     */
    public String trocarGrupoAtual (String[] arr){
        if(!db.verificaPertenceGrupo(email,arr[1]))
             return "\nNão pertence a este grupo o ou grupo nao existe... Tente novamente";

        grupoAtual = arr[1];

        return "\nGrupo atual alterado com sucesso!";
    }

    /**
     *  Criação de convites para adesão a um grupo, sendo os destinatários identificados
     *  através dos seus emails de registo no sistema
     */
    public String criarConvite(String[] arr) {

        String emailDestinatario = arr[1];
        if (!db.verificaEmail(emailDestinatario)) {
            return "\nEmail do destinatário inválido";
        }

        Convite convite = new Convite(email, emailDestinatario, "Pendente");


        return "\nConvite criado com sucesso!";
    }

    /**
     *     Visualização automática e atualizada dos convites de adesão recebidos/pendentes
     */
    public String verConvites (){
        //TODO listar convites
        return "Ainda por implementar :)";
    }

    /**
     *   Aceitação e recusa de convites de adesão a grupos
     */
    public String  responderConvite (String[] arr){
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
    public String  listarGrupos (){
        String lista = db.listaGrupos(email);
        return "\nLista de grupos: " + (lista.equals("")? "\nNão pertence a nenhum grupo.."
                :lista);
    }

    /**
     * Edição do nome de um grupo por qualquer um dos seus elementos
     */
    public String  editarNomeGrupo(String[] arr){
        if(grupoAtual == null)
            return "Sem grupo atual selecionado. Por favor escolha um grupo";

        if(db.verificaGrupo(arr[1]))
            return "Nome de grupo indisponivel";

        if(db.alteraNomeGrupo(arr[1], grupoAtual)){
            grupoAtual = arr[1];
            return "Nome do Grupo alterado com sucesso!";
        }

        return "Não foi possivel alterar o nome do grupo " + grupoAtual;
    }

    /**
     * Eliminação de um grupo e respetivos dados, desde que não exista qualquer conta por
     * saldar/valor em dívida (ou seja, não podem existir situações de o elemento X deve a
     * quantia Z ao elemento Y / o elemento Y tem a receber a quantia Z do elemento Y).
     */
    public String  eliminarGrupo(String[] arr){
        if(!db.verificaPertenceGrupo(email,arr[1]))
            return "\nNão pertence a este grupo o ou grupo nao existe... Tente novamente";

        if(db.listaDespesas(arr[1]).size() > 0)
            return "\nEste grupo nao pode ser eliminado. Ainda existem despesas...";

        if(db.eliminarGrupodaDB(arr[1])){
            grupoAtual = null;
            return "\nGrupo eliminado com sucesso!";
        }


        return "\nOcorreu um erro ao eliminar o grupo...";
    }

    /**
     * Saída de um grupo se ainda não existir qualquer despesa associada ao utilizador;
     */
    public String sairGrupo(String[] arr){
        if(!db.verificaPertenceGrupo(email,arr[1]))
            return "\nNão pertence a este grupo o ou grupo nao existe... Tente novamente";

        if(db.verificaDespesaPessoaGrupo(email,arr[1]))
            return "\nNão pode sair do grupo. Ainda tem despesas por liquidar...";

        if(db.retiraEmailGrupo(email, arr[1]))
            return "\nSaiu do grupo " + arr[1];

        return "Ocorreu um erro ao tentar sair do grupo...";
    }

    /**
     *Inserção de uma despesa associada ao grupo corrente, por qualquer um dos seus
     * elementos, com: data; descrição; valor; quem pagou; e os elementos com quem é
     * partilhada (pode não incluir quem pagou);
     *
     * (arr) 1-valor, 2-data, 3-quemPagou
     * ()
     */
    public String  inserirDespesa(String comando, String[] arr){
        // valor data quem ;comQuemm;descricao
        String[] ast = comando.split(";"); //ast[1] -> partilhados, ast[2]-> descricao

        ast[1] = ast[1].toLowerCase();
        String partilhados[] = ast[1].split(" ");


        if(grupoAtual == null)
            return "Sem grupo atual selecionado. Por favor escolha um grupo";

        if(!db.verificaEmail(arr[3])){ //email pagador
            return "\nEmail de quem pagou incorreto";
        }
        int id = db.adicionaDespesa(email,grupoAtual,arr[1],arr[2],arr[3],ast[2]);
        if(id < 0){
            return "\n Não foi possivel adicionar a despesa";
        }

        StringBuilder quemNaodeu = new StringBuilder("\nEmail(s) de partilha invalido(s). Não foi partilhada a despesa com :");
        boolean uh = false;
        for (String email_partilha: partilhados) {
            if(!db.verificaPertenceGrupo(email_partilha,grupoAtual)||!db.adicionaDespesaPartilhada(id, email_partilha)){
                quemNaodeu.append("\n - " + email_partilha);
                uh = true;
            }
        }
        return "\nDespesa adicionada com sucesso!" + (uh ? quemNaodeu : "\nPartilhado com todos!");
    }

    /**
     *Visualização do valor total de gastos efetuados pelo grupo corrente;
     */
    public String  verGastos(){
        if(grupoAtual == null)
            return "\nSem grupo atual selecionado. Por favor escolha um grupo";

        float result = db.somaDespesas(grupoAtual);
        if(result > 0)
            return "\nSoma das despesas do grupo: " + result + " Euros";
        return "Não foi possivel calcular o valor...";
    }

    /**
     * Visualização do histórico das despesas associadas ao grupo corrente, ordenadas
     * cronologicamente, com todos os detalhes, incluindo a identificação de quem a inseriu
     * no sistema (pode não ser quem efetuou a despesa);
     */
    public String verHistoricoDespesas() {
        if (grupoAtual == null)
            return "\nSem grupo atual selecionado. Por favor escolha um grupo.";

        // Obter lista de despesas do grupo atual
        ArrayList<String> despesas = db.listaDespesas(grupoAtual);

        // Verificar se existem despesas
        if (despesas.size() == 0)
            return "\nHistórico de despesas do grupo " + grupoAtual + ":\nNão há despesas a listar...";

        // Ordenar despesas cronologicamente
        despesas.sort((d1, d2) -> {
            String[] detalhes1 = d1.split(";");
            String[] detalhes2 = d2.split(";");
            String data1 = detalhes1[0]; // Supõe que a data está na posição 0
            String data2 = detalhes2[0];

            return data1.compareTo(data2); // Ordenação ascendente
        });

        // Construir string com o histórico
        StringBuilder retu = new StringBuilder("\nHistórico de despesas do grupo " + grupoAtual + ":\n");

        for (String despesa : despesas) {
            retu.append(despesa).append("\n");
        }

        return retu.toString();
    }

    /**
     *Exportação, para um ficheiro em formato CSV, da lista de despesas associadas ao
     *grupo corrente, ordenadas cronologicamente e detalhada (ver exemplo na Figura 1);
     */
    public String  exportarDespesas(){
        //TODO servidor faz logica de exportar ja q é ele q tem acesso aos dados maybe
        return "\n....A teoria está lá... Nao testamos ainda...";
    }

    /**
     *Edição dos campos de uma despesa já introduzida no sistema;
     */
    public String  editarDespesa(String comando, String[] arr){ //arr[1]- id, arr[2]-campo, ast[1]-valor
        String[] ast = comando.split(";"); //

        ast[1] = ast[1].toLowerCase();
        String partilhados[] = ast[1].split(" "); //case 5 e 6
        if(!db.verificaId(arr[1], "despesa"))
            return "\nId invalido";

        switch (arr[2]){
            case "1":
                if(db.alteraCampoDespesa(arr[1], "data", ast[1]))
                    return "Data alterada com sucesso";
                return "Não foi possivel alterar a data...";

            case "2":
                if(db.alteraCampoDespesa(arr[1], "descricao",ast[1]))
                    return "Descrição alterada com sucesso";
                return "Não foi possivel alterar a descrição...";

            case "3":
                if(db.alteraCampoDespesa(arr[1], "valor",ast[1]))
                    return "Valor alterado com sucesso";
                return "Não foi possivel alterar o valor...";

            case "4":
                if(!db.verificaPertenceGrupo(arr[1],grupoAtual))
                    return "\nEmail invalido";

                if(db.alteraCampoDespesa(arr[1], "email_pagador",ast[1]))
                    return "Valor alterado com sucesso";
                return "Não foi possivel alterar o valor...";

            case "5":
                //TODO - verifica se existe(m) o(s) email(s) na BD
                //return "\nEmail invalido";
                //TODO - altera password na BD
                break;
            case "6":
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
    public String  eliminarDespesa(String[] arr){
        if(!db.verificaId(arr[1], "despesa"))
            return "\nId invalido";
        if(db.eliminarDespesa(arr[1]))
            return "\nDespesa eliminada com sucesso";
        return "\n Nao foi possivel eliminar a pagamento";

    }

    /**
     *Para efeitos de liquidação/acerto das contas, inserção de um pagamento efetuado
     * por um elemento do grupo corrente a outro elemento do mesmo grupo, com
     * indicação de: quem pagou; quem recebeu; data; e valor;
     */
    public String inserirPagamento(String[] arr) {
        String quemPagou = arr[1];   // Email de quem efetuou o pagamento
        String quemRecebeu = arr[2]; // Email de quem recebeu o pagamento
        String data = arr[3];        // Data do pagamento
        String valor = arr[4];       // Valor do pagamento

        if(grupoAtual == null)
            return "\nSem grupo atual selecionado. Por favor escolha um grupo";

        // Verifica se os emails são válidos
        if (!db.verificaEmail(quemPagou)) {
            return "\nEmail de quem pagou é inválido.";
        }
        if (!db.verificaEmail(quemRecebeu)) {
            return "\nEmail de quem recebeu é inválido.";
        }

        // Tenta inserir o pagamento na base de dados
        boolean pagamentoInserido = db.adicionaPagamento(quemPagou, quemRecebeu, data, valor, grupoAtual);

        if (pagamentoInserido) {
            return "\nPagamento adicionado com sucesso.";
        } else {
            return "\nNão foi possível adicionar o pagamento. Por favor, tenta novamente.";
        }
    }

    /**
     * Listagem dos pagamentos efetuados entre elementos do grupo;
     */
    public String  verPagamentos(){
        if(grupoAtual == null)
            return "\nSem grupo atual selecionado. Por favor escolha um grupo";

        String ret = db.listaPagamentos(grupoAtual);
        return "\nLista de pagamentos: "
                +  (ret.equals("") ? "\nNão há pagamentos a listar..": ret);
    }

    /**
     * Eliminação de um pagamento efetuado por um elemento a outro elemento;
     */
    public String  eliminarPagamento(String[] arr){
        if(!db.verificaId(arr[1], "pagamento"))
            return "\nId invalido";
        if(db.eliminarPagamento(arr[1]))
            return "\nDespesa eliminada com sucesso";
        return "\n Nao foi possivel eliminar a pagamento";

    }

    /**
     * Visualização dos saldos do grupo corrente com, para cada elemento, indicação do:
     *    o gasto total;
     *    o valor total que deve;
     *    o valor que que deve a cada um dos restantes elementos;
     *    o valor total que tem a receber;
     *    o valor que tem a receber de cada um dos restantes elementos;
     */
    public String  verSaldos(){
        //TODO calcular saldos. Ir a BD no grupo atual
        return "Gastos total do grupo: Ainda nao sei :(";
    }
}
