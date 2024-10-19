package pt.isec.pd.tp.Servidores;

import pt.isec.pd.tp.MSG;

public class AtendeCliente extends Thread {

    public boolean VerificaComando(String comando){
        //login
        comando = comando.toUpperCase();
        String arr[] = comando.split(" ");
        //String firstWord = arr[0];

        switch(arr[0]) {
            case "LOGIN":
                if(arr.length != 3){
                    //TODO envia mensagem como parametros incorretos
                    System.out.print("login username password - nr parametros incorreto");
                    return false;
                }
                else{
                    //TODO - verifica se existe na base de dados
                    //SQL PTSD
                }
                break;

            case "REGISTAUTILIZADOR":
                if(arr.length != 3){
                    //TODO envia mensagem como parametros incorretos
                    System.out.print("RegistaUtilizador nome password - nr parametros incorreto");
                    return false;
                }
                else{
                    //TODO - verifica se existe na base de dados
                    if() {
                        System.out.print("Utilizador a registar já existe");
                        return false;
                    }
                    else{
                        //TODO - adiciona novo utilizador na base de dados
                    }
                }
                break;

            case "EDITAPERFIL":
                if(arr.length != 4){
                    //TODO envia mensagem como parametros incorretos
                    System.out.print("EditaPerfil novoNome novaPassword passwordAtual - nr parametros incorreto");
                    return false;
                }
                break;

            case "CRIAGRUPO":
                if(arr.length != 2){
                    //TODO envia mensagem como parametros incorretos
                    System.out.print("criaGrupo nome - nr parametros incorreto");
                    return false;
                }
                break;

            case "TROCAGRUPOATUAL":
                if(arr.length != 2){
                    //TODO envia mensagem como parametros incorretos
                    System.out.print("trocaGrupoAtual novoGrupo - nr parametros incorreto");
                    return false;
                }
                break;

            case "CRIACONVITE": //criaconvite grupo
                if(arr.length == 1){
                    //TODO envia mensagem como parametros incorretos
                    System.out.print("criaConvite destinatario/s - nr parametros incorreto");
                    return false;
                }
                break;

            case "VERCONVITES": //verconvites

                //TODO chamar verConvite()

            case "GERECONVITE": //convite <pessoa> <resposta>
                if(arr.length != 3){
                    //TODO envia mensagem como parametros incorretos
                    System.out.print("login username password - nr parametros incorreto");
                    return false;
                }
                //TODO gere(aceita/recusa) convite

            case "LISTARGRUPO": //listagrupo


            case "EDITARNOMEGRUPO": //editarnomegrupo <novonome>
                if(arr.length != 2) {
                    //TODO envia mensagem como parametros incorretos
                    System.out.print("login username password - nr parametros incorreto");
                    return false;
                }
//--------------------------------------


            case "ELIMINARGRUPO": //eliminargrupo <nome>
                if(arr.length != 3){
                    //TODO envia mensagem como parametros incorretos
                    System.out.print("login username password - nr parametros incorreto");
                    return false;
                }
            case "SAIRGRUPO":
                if(arr.length != 3){
                    //TODO envia mensagem como parametros incorretos
                    System.out.print("login username password - nr parametros incorreto");
                    return false;
                }
            case "INSERIRDESPESA":
                if(arr.length != 3){
                    //TODO envia mensagem como parametros incorretos
                    System.out.print("login username password - nr parametros incorreto");
                    return false;
                }
            case "VERGASTOS":
                if(arr.length != 3){
                    //TODO envia mensagem como parametros incorretos
                    System.out.print("login username password - nr parametros incorreto");
                    return false;
                }
            case "VERHISTORICODESPESAS":
                if(arr.length != 3){
                    //TODO envia mensagem como parametros incorretos
                    System.out.print("login username password - nr parametros incorreto");
                    return false;
                }
            case "EXPROTARFICHEIRODESPESAS":
                if(arr.length != 3){
                    //TODO envia mensagem como parametros incorretos
                    System.out.print("login username password - nr parametros incorreto");
                    return false;
                }
            case "EDITARDESPESA":
                if(arr.length != 3){
                    //TODO envia mensagem como parametros incorretos
                    System.out.print("login username password - nr parametros incorreto");
                    return false;
                }break;

            case "ELIMINARDESPESA":
                if(arr.length != 3){
                    //TODO envia mensagem como parametros incorretos
                    System.out.print("login username password - nr parametros incorreto");
                    return false;
                }
                break;

            case "INSERIRPAGAMENTO":
                if(arr.length != 4){
                    //TODO envia mensagem como parametros incorretos
                    System.out.print("inserePagamento grupo pagamento valor - nr parametros incorreto");
                    return false;
                }
                break;

            case "LISTARPAGAMENTO":

                break;
            case "ELIMINAPAGAMENTO":
                if(arr.length != 2){
                    //TODO envia mensagem como parametros incorretos
                    System.out.print("eliminaPagamento cliente - nr parametros incorreto");
                    return false;
                }
                break;

            case "VERSALDO":

                break;
        }
        //verconvites
        VerConvites(new MSG(comando, false));

        return true;
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
