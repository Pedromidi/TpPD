package pt.isec.pd.tp.Cliente;

import pt.isec.pd.tp.MSG;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;

import java.util.InputMismatchException;
import java.util.Scanner;

public class ClienteUI {
    private MSG msg;
    private static Cliente cliente;
    public static final String TIME_REQUEST = "TIME";
    public static final int TIMEOUT = 10;

    public static String enviaComando(String comando, ObjectInputStream in, ObjectOutputStream out) throws IOException, ClassNotFoundException {
        //TODO enviar string ao servidor, servidor verifica e devolve resposta
        out.writeObject(comando);
        String response = (String) in.readObject();
        //System.out.println("Server:\n" + response);
        return ("Server:\n" + response);
    }

    public static void main(String[] args) {
        InetAddress serverAddr;
        int serverPort;
        String command;
        String res;

        if (args.length != 2) {
            System.out.println("Sintaxe: java Client serverAddress serverPort");
            return;
        }

        try {
            serverAddr = InetAddress.getByName(args[0]);
            serverPort = Integer.parseInt(args[1]);

            try (Socket socket = new Socket(serverAddr, serverPort)) {
                socket.setSoTimeout(TIMEOUT * 1000);

                // a)
                // [PT] Serializar o objecto do tipo String TIME_REQUEST para o OutputStream disponibilizado pelo socket
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                out.writeObject("Hello - cliente");
                out.flush();
                // b)
                // [PT] Deserializa o objecto do tipo Calendar recebido no InputStream disponibilizado pelo socket
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                String response = (String) in.readObject();

                System.out.println("Server:\n" + response);

                Scanner input = new Scanner(System.in);
                int opcao;
                do {
                    System.out.println("Escolha uma opcao:");
                    System.out.println("1. Autenticacao");
                    System.out.print("2. Registo\n> ");

                    opcao = input.nextInt();

                    if (opcao != 1 && opcao != 2) {
                        System.out.println("Opcao invalida. Por favor escolha 1 ou 2");
                    }
                    input.nextLine();

                } while (opcao != 1 && opcao != 2);

                if (opcao == 1) {
                    do {
                        System.out.print("Nome: ");
                        String nome = input.next();
                        System.out.print("Password: ");
                        String pass = input.next();

                        //Envia comando login ao servidor - Codigo 1
                        command = "1 " + nome + " " + pass;
                        res = enviaComando(command, in, out);
                        System.out.println(res);

                    }while(res.equals("Login aceite"));
                } else {
                    System.out.print("Nome: ");
                    String nome = input.next();
                    System.out.print("Numero de Telefone: ");
                    String telefone = input.next();
                    System.out.print("Email: ");
                    String email = input.next();
                    System.out.print("Password: ");
                    String password = input.next();

                    //TODO - enviar ao server como register
                }

                //TODO - se receber resposta positiva do servidor quanto ao login/registo
                do {

                    System.out.println("\n\nEscolha uma opção:");

                    System.out.println("1. Editar dados de registo"); // nao precisa de selecionar grupo atual
                    System.out.println("2. Criar novo grupo"); // nao precisa de selecionar grupo atual
                    System.out.println("3. Selecionar grupo atual");  // nao precisa de selecionar grupo atual
                    System.out.println("4. Criar convinte para adesao a um grupo");
                    System.out.println("5. Ver convites recebidos/pendentes"); // nao precisa de selecionar grupo atual
                    System.out.println("6. Gerir (aceitar/recusar) convites"); // nao precisa de selecionar grupo atual
                    System.out.println("7. Listar grupos"); // nao precisa de selecionar grupo atual
                    System.out.println("8. Editar nome do grupo");
                    System.out.println("9. Eliminar grupo");
                    System.out.println("10. Sair do grupo");
                    System.out.println("11. Inserir despesa");
                    System.out.println("12. Ver valor total de gastos");
                    System.out.println("13. Ver historico de despesas");
                    System.out.println("14. Exportar lista de despesas");
                    System.out.println("15. Editar campo de uma despesa");
                    System.out.println("16. Eliminar despesa");
                    System.out.println("17. Inserir pagamento efetuado (de/para)");
                    System.out.println("18. Listar pagamentos efetuados");
                    System.out.println("19. Eliminar pagamento efetuado");
                    System.out.println("20. Ver saldos do grupo");
                    System.out.println("21. Logout\n> "); // nao precisa de selecionar grupo atual

                    int option = input.nextInt();

                    switch (option) {
                        case 1: {
                            System.out.println("\nConfirme a sua password\n> ");
                            String password = input.next();
                            //TODO pedir ao servidor confirmacao da pass

                            //resposta positiva do servidor
                            int escolha;
                            do {
                                System.out.println("\nEscolha o campo a editar:");

                                System.out.println("1. Editar nome");
                                System.out.println("2. Editar numero de telefone");
                                System.out.println("3. Editar email");
                                System.out.println("4. Editar password");
                                System.out.println("5. Cancelar\n> ");
                                escolha = input.nextInt();

                                if (escolha < 1 || escolha > 5) System.out.println("\nEscolha invalida");
                            } while (escolha < 1 || escolha > 5);

                            if (escolha == 5) break;

                            System.out.print("\nNovo dado: ");
                            String novoCampo = input.next();
                            //TODO enviar ao servidor -> comando campo novoCampo
                        }
                        case 2: {
                            System.out.print("\nNome do novo grupo: ");
                            String novo = input.next();
                            //TODO enviar ao servidor -> comando novoGrupo
                        }
                        case 3: {
                            System.out.print("\nNome do grupo: ");
                            String novo = input.next();
                            //TODO enviar ao servidor -> comando novoGrupo
                        }
                        case 4: {
                            System.out.print("\nEmail do destinatário: ");
                            String novo = input.next();
                            //TODO enviar ao servidor -> comando email
                        }
                        case 5: {
                            //TODO pedir ao servidor convites deste cliente
                        }
                        case 6: {
                            System.out.print("\nId do convite: "); //convites teem id? ou é so o nome do grupo. é possivel receber dois convites do mesmo grupo?
                            String convite = input.next();
                            int campo;
                            do {
                                System.out.println("Escolha o que fazer com o convite: ");

                                System.out.println("\n1. Aceitar");
                                System.out.println("2. Recusar");
                                System.out.println("3. Cancelar\n> ");
                                campo = input.nextInt();

                                if (campo < 1 || campo > 3) System.out.println("Escolha invalida");

                            } while (campo < 1 || campo > 3);

                            if (campo == 3) break;

                            //TODO enviar ao servidor -> comando convite resposta
                        }
                        case 7: {
                            //TODO pedir ao servidor grupos deste cliente
                        }
                        case 8: {
                            System.out.print("\nNovo nome do grupo: ");
                            String novo = input.next();
                            //TODO enviar ao servidor -> comando novoNome
                        }
                        case 9: {
                            //TODO pedir ao servidor eleminacao do grupo atual deste cliente
                        }
                        case 10: {
                            //TODO pedir ao servidor saida do grupo atual deste cliente
                        }
                        case 11: {
                            System.out.print("Data (dd/mm/aa): ");
                            String data = input.next();
                            System.out.print("Descricao: ");
                            String descricao = input.next();
                            System.out.print("Valor: ");
                            String valor = input.next();
                            System.out.print("Quem pagou: ");
                            String quem = input.next();
                            System.out.print("Elementos a partilhar (<nome> <nome> ...): ");
                            String comQuem = input.next();

                            //TODO enviar ao servidor -> comando data descricao valor quem comQuem
                        }
                        case 12: {
                            //TODO pedir ao servidor valor total dos gastos do grupo atual deste cliente
                        }
                        case 13: {
                            //TODO pedir ao servidor historico de despesas do grupo atual deste cliente
                        }
                        case 14: {
                            //TODO pedir ao servidor para exportar despesas do grupo atual deste cliente
                        }
                        case 15: {
                            System.out.print("Id da despesa: ");
                            String comQuem = input.next();

                            int campo;
                            do {
                                System.out.print("\nParametro a editar: ");

                                System.out.println("1. Data (dd/mm/aa) ");
                                System.out.println("2. Descricao ");
                                System.out.println("3. Valor ");
                                System.out.println("4. Quem pagou ");
                                System.out.println("5. Elementos partilhados (<nome> <nome> ...) ");
                                System.out.println("6. Cancelar\n> ");
                                campo = input.nextInt();

                                if (campo < 1 || campo > 6) System.out.println("\nEscolha invalida");
                            } while (campo < 1 || campo > 6);

                            if (campo == 6) break;

                            System.out.print("Novo valor: ");
                            String valor = input.next();
                            //TODO enviar ao servidor -> comando idDespesa campo novoValor
                        }
                        case 16: {
                            System.out.print("Id da despesa: ");
                            String id = input.next();

                            //TODO enviar ao servidor -> comando idDespesa
                        }
                        case 17: {
                            System.out.print("Quem pagou: ");
                            String quemP = input.next();
                            System.out.print("Quem recebeu: ");
                            String quemR = input.next();
                            System.out.print("Data (dd/mm/aa): ");
                            String data = input.next();
                            System.out.print("Valor: ");
                            String valor = input.next();

                            //TODO enviar ao servidor -> comando quemP quemR data valor
                        }
                        case 18: {
                            //TODO pedir ao servidor pagamentos do grupo atual deste cliente
                        }
                        case 19: {
                            System.out.print("Id do pagamento: ");
                            String id = input.next();
                        }
                        case 20: {
                            //TODO pedir ao servidor saldos do grupo atual deste cliente
                        }
                        case 21: {
                            //TODO suicidio
                        }
                    }

                } while (true);
                //logout ou servidor termina
            }
        } catch (UnknownHostException e) {
            System.out.println("Destino desconhecido:\n\t" + e);
        } catch (NumberFormatException e) {
            System.out.println("O porto do servidor deve ser um inteiro positivo.");
        } catch (SocketTimeoutException e) {
            System.out.println("Nao foi recebida qualquer resposta:\n\t" + e);
        } catch (SocketException e) {
            System.out.println("Ocorreu um erro ao nivel do socket TCP:\n\t" + e);
        } catch (IOException e) {
            System.out.println("Ocorreu um erro no acesso ao socket:\n\t" + e);
        } catch (ClassNotFoundException e) {
            System.out.println("O objecto recebido não é do tipo esperado:\n\t" + e);
        }
/*
    public void requestExample(){
        InetAddress serverAddr;
        int serverPort;
        Calendar response;

        if (args.length != 2) {
            System.out.println("Sintaxe: java Client serverAddress serverPort");
            return;
        }

        try {
            serverAddr = InetAddress.getByName(args[0]);
            serverPort = Integer.parseInt(args[1]);

            try (Socket socket = new Socket(serverAddr, serverPort)) {
                socket.setSoTimeout(TIMEOUT * 1000);

                // a)
                // [PT] Serializar o objecto do tipo String TIME_REQUEST para o OutputStream disponibilizado pelo socket
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                out.writeObject("Time");

                // b)
                // [PT] Deserializa o objecto do tipo Calendar recebido no InputStream disponibilizado pelo socket
                ObjectInputStream in =  new ObjectInputStream(socket.getInputStream());
                response = (Calendar) in.readObject();

                System.out.println("Resposta: " + response.getTime());
            }
        } catch (UnknownHostException e) {
            System.out.println("Destino desconhecido:\n\t" + e);
        } catch (NumberFormatException e) {
            System.out.println("O porto do servidor deve ser um inteiro positivo.");
        } catch (SocketTimeoutException e) {
            System.out.println("Nao foi recebida qualquer resposta:\n\t" + e);
        } catch (SocketException e) {
            System.out.println("Ocorreu um erro ao nivel do socket TCP:\n\t" + e);
        } catch (IOException e) {
            System.out.println("Ocorreu um erro no acesso ao socket:\n\t" + e);
        } catch(ClassNotFoundException e){
            System.out.println("O objecto recebido não é do tipo esperado:\n\t"+e);
        }
    }
*/
    }
}
