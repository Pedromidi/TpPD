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

            // a)
            // [PT] Deserializar o objecto recebido
            // [EN] Deserialize the received object
            while(true) {
                comando = (String) oin.readObject();
                System.out.println("Recebido \"" + comando + "\" de " + clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort());

                // b)
                // [PT] Sair da thread se o pedido recebido for diferente do valor dado pela variável Server.TIME_REQUEST
                // [EN] Exit the thread if the request received is different from the value given by the Server.TIME_REQUEST variable
                /*if (!request.equalsIgnoreCase(Server.TIME_REQUEST)) {
                    return;
                }*/

                // c)
                // [PT] Serializar o objecto do tipo Time
                // [EN] Serialize the object of type Time
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
            case "1": //login <email> <password>
                if (arr.length != 3) {
                    //TODO envia mensagem como parametros incorretos
                    System.out.print("login username password - nr parametros incorreto");
                    return "login username password - nr parametros incorreto";
                } else {
                    //TODO - verifica se existe na base de dados
                    //SQL PTSD
                    return "recebi comando login";
                }

            case "REGISTAUTILIZADOR": //registar <nome> <telefone> <email> <password>
                if (arr.length != 3) {
                    //TODO envia mensagem como parametros incorretos
                    System.out.print("RegistaUtilizador nome password - nr parametros incorreto");
                    return "RegistaUtilizador nome password - nr parametros incorreto";
                } else {
                   /* //TODO - verifica se existe na base de dados (talvez deva verificar ja na funcao?)
                    if() {
                        System.out.print("Utilizador a registar já existe");
                        return false;
                    }
                    else{
                        //TODO - adiciona novo utilizador na base de dados
                    }*/
                }
                break;

            case "EDITARPERFIL": //editarperfil <password> <campoAeditar> <novoValor>
                if (arr.length != 3) {
                    //TODO envia mensagem como parametros incorretos
                    System.out.print("EditaPerfil novoNome novaPassword passwordAtual - nr parametros incorreto");
                    return "EditaPerfil novoNome novaPassword passwordAtual - nr parametros incorreto";
                }
                break;

            case "CRIARGRUPO": // criargrupo <novonome>
                if (arr.length != 2) {
                    //TODO envia mensagem como parametros incorretos
                    System.out.print("criaGrupo nome - nr parametros incorreto");
                    return "criaGrupo nome - nr parametros incorreto";
                }
                break;

            case "TROCARGRUPO": //trocargrupo <novogrupo>
                if (arr.length != 2) {
                    //TODO envia mensagem como parametros incorretos
                    System.out.print("trocaGrupoAtual novoGrupo - nr parametros incorreto");
                    return "trocaGrupoAtual novoGrupo - nr parametros incorreto";
                }
                break;

            case "CRIACONVITE": //criaconvite <grupo>
                if (arr.length == 1) {
                    //TODO envia mensagem como parametros incorretos
                    System.out.print("criaConvite destinatario/s - nr parametros incorreto");
                    return "criaConvite destinatario/s - nr parametros incorreto";
                }
                break;

            case "VERCONVITES": //verconvites

                //TODO chamar verConvite()

            case "GERECONVITE": //gereconvite <pessoa> <resposta>
                if (arr.length != 3) {
                    //TODO envia mensagem como parametros incorretos
                    System.out.print("login username password - nr parametros incorreto");
                    return "login username password - nr parametros incorreto";
                }
                //TODO gere(aceita/recusa) convite

            case "LISTARGRUPOS": //listagrupo
                //TODO listar todos os grupos a que pertence o user


            case "EDITARNOMEGRUPO": //editarnomegrupo <novonome>
                if (arr.length != 2) {
                    //TODO envia mensagem como parametros incorretos
                    System.out.print("login username password - nr parametros incorreto");
                    return "login username password - nr parametros incorreto";
                }

            case "ELIMINARGRUPO": //eliminargrupo <nome>
                if (arr.length != 3) {
                    //TODO envia mensagem como parametros incorretos
                    System.out.print("login username password - nr parametros incorreto");
                }
            case "SAIRGRUPO": //sairgrupo
                //TODO sair do grupo atual (eliminar utilizador do grupo)

            case "INSERIRDESPESA": //inserirdespesa  <grupo> <data> <descricao> <valor> <elementosPartilhados> <podem ser varios..>
                if (arr.length < 6) {
                    //TODO envia mensagem como parametros incorretos
                    System.out.print("login username password - nr parametros incorreto");
                }
            case "VERGASTOS": //vergastos <grupoConcorrente>
                if (arr.length != 2) {
                    //TODO envia mensagem como parametros incorretos
                    System.out.print("login username password - nr parametros incorreto");
                }
            case "VERHISTORICODESPESAS": //verhistoriocodespesas <grupoconcorrente>
                if (arr.length != 2) {
                    //TODO envia mensagem como parametros incorretos
                    System.out.print("login username password - nr parametros incorreto");
                }
            case "EXPROTARDESPESAS": //exportardespesas <grupoconcorrente>
                if (arr.length != 2) {
                    //TODO envia mensagem como parametros incorretos
                    System.out.print("login username password - nr parametros incorreto");
                }
                //TODO envia lista de despesas, cliente faz a logica de exportacao para ficheiro ?

            case "EDITARDESPESA": //editardespesa <campoAeditar> <novoValor>
                if (arr.length != 3) {
                    //TODO envia mensagem como parametros incorretos
                    System.out.print("login username password - nr parametros incorreto");
                }
                break;

            case "ELIMINARDESPESA": //eliminardespesa <nome> (algo q identifique a despesa...)
                if (arr.length != 2) {
                    //TODO envia mensagem como parametros incorretos
                    System.out.print("login username password - nr parametros incorreto");
                }
                break;

            case "INSERIRPAGAMENTO": // inserirpagamento <quemPagou> <quemrecebeu> <data> <valor>
                if (arr.length != 5) {
                    //TODO envia mensagem como parametros incorretos
                    System.out.print("inserePagamento grupo pagamento valor - nr parametros incorreto");
                }
                break;

            case "LISTARPAGAMENTOS":

                break;

            case "ELIMINAPAGAMENTO": //eliminapagamento <nome> (algo q identifique pagamento)
                if (arr.length != 2) {
                    //TODO envia mensagem como parametros incorretos
                    System.out.print("eliminaPagamento cliente - nr parametros incorreto");
                }
                break;

            case "VERSALDO": //versaldo <grupoconcorrente>
                if (arr.length != 2) {
                    //TODO envia mensagem como parametros incorretos
                    System.out.print("eliminaPagamento cliente - nr parametros incorreto");
                } else {

                }
                break;
        }
        return "";
    }

    //Edição dos dados de registo
    public MSG EditaPerfil (MSG msg){
        return msg;
    }

    //Criação de um novo grupo, sendo este caracterizado por um nome (por exemplo,
    //Viagem finalistas deis-isec 2025 - Andorra) que deve ser único no sistema. O
    //utilizador que cria o grupo passa automaticamente a integrá-lo (primeiro elemento);
    public MSG CriaGrupo (MSG msg){
        return msg;
    }

    //o utilizador escolhe um dos grupos a que pertence e, a partir de esse momento, as
    //operações que executar referem-se implicitamente a esse grupo
    public MSG TrocaGrupoAtual (MSG msg){
        return msg;
    }

    //Criação de convites para adesão a um grupo, sendo os destinatários identificados
    //através dos seus emails de registo no sistema
    public MSG CriaConvite (MSG msg){
        return msg;
    }

    //Visualização automática e atualizada dos convites de adesão recebidos/pendentes
    public MSG VerConvites (MSG msg){
        return msg;
    }

    //Aceitação e recusa de convites de adesão a grupos
    public MSG AceitaConvite (MSG msg){
        return msg;
    }

    //Lista dos grupos a que pertence o utilizador autenticado
    public MSG ListaGrupo (MSG msg){
        return msg;
    }

    //Edição do nome de um grupo por qualquer um dos seus elementos
    public MSG EditaNomeGrupo(MSG msg){
        //TODO
        return msg;
    }

    //Eliminação de um grupo e respetivos dados, desde que não exista qualquer conta por
    //saldar/valor em dívida (ou seja, não podem existir situações de o elemento X deve a
    //quantia Z ao elemento Y / o elemento Y tem a receber a quantia Z do elemento Y).
    public MSG EliminaGrupo(MSG msg){
        //TODO
        return msg;
    }

    //Saída de um grupo se ainda não existir qualquer despesa associada ao utilizador;
    public MSG SairGrupo(MSG msg){
        //TODO
        return msg;
    }

    //Inserção de uma despesa associada ao grupo corrente, por qualquer um dos seus
    //elementos, com: data; descrição; valor; quem pagou; e os elementos com quem é
    //partilhada (pode não incluir quem pagou);
    public MSG InsereDespesa(MSG msg){
        //TODO
        return msg;
    }

    //Edição dos campos de uma despesa já introduzida no sistema;
    public MSG EditaDespesa(MSG msg){
        //TODO
        return msg;
    }

    //Visualização do valor total de gastos efetuados pelo grupo corrente;
    public MSG VerGastos(MSG msg){
        //TODO
        return msg;
    }

    //Visualização do histórico das despesas associadas ao grupo corrente, ordenadas
    //cronologicamente, com todos os detalhes, incluindo a identificação de quem a inseriu
    //no sistema (pode não ser quem efetuou a despesa);
    public MSG VerHistoricoDespesas(MSG msg){
        //TODO
        return msg;
    }


    //Exportação, para um ficheiro em formato CSV, da lista de despesas associadas ao
    //grupo corrente, ordenadas cronologicamente e detalhada (ver exemplo na Figura 1);
    public MSG ExportaFichDespesas(MSG msg){
        //TODO
        return msg;
    }


    //Eliminação de uma despesa;
    public MSG PagarDespesa(MSG msg){
        //TODO
        return msg;
    }

    //Para efeitos de liquidação/acerto das contas, inserção de um pagamento efetuado
    //por um elemento do grupo corrente a outro elemento do mesmo grupo, com
    //indicação de: quem pagou; quem recebeu; data; e valor;
    public MSG PagaOutroCliente(MSG msg){
        //TODO
        return msg;
    }

    //Listagem dos pagamentos efetuados entre elementos do grupo;
    public MSG VerPagamentosGrupo(MSG msg){
        //TODO
        return msg;
    }

    //Eliminação de um pagamento efetuado por um elemento a outro elemento;
    public MSG EliminaPagamento(MSG msg){
        //TODO
        return msg;
    }

    //Visualização dos saldos do grupo corrente com, para cada elemento, indicação do:
    //o gasto total;
    //o valor total que deve;
    //o valor que que deve a cada um dos restantes elementos;
    //o valor total que tem a receber;
    //o valor que tem a receber de cada um dos restantes elementos;
    public MSG verSaldo(MSG msg){
        //TODO
        return msg;
    }


}
