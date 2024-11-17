package pt.isec.pd.tp.Cliente;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;

import java.util.InputMismatchException;
import java.util.Scanner;

import static java.lang.System.exit;

public class ClienteUI {
    public static final int TIMEOUT = 10;

    private static ObjectInputStream in;
    private static ObjectOutputStream out;
    public static String enviaComando(String comando) throws IOException, ClassNotFoundException {
        out.writeObject(comando);
        out.flush();//envio imediato

        String response = (String) in.readObject();
        return "\nServer: " + response;
    }

    public static void desconectarDoServidor(Socket socket) {
        try {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            System.out.println("Desconectado do servidor com sucesso.");
        } catch (IOException e) {
            System.err.println("Erro ao desconectar do servidor: " + e.getMessage());
        }
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

                //Serializar o objecto do tipo String TIME_REQUEST para o OutputStream disponibilizado pelo socket
                out = new ObjectOutputStream(socket.getOutputStream());
                out.writeObject("Hello - cliente");
                out.flush();

                //Deserializa o objecto do tipo Calendar recebido no InputStream disponibilizado pelo socket
                in = new ObjectInputStream(socket.getInputStream());
                String response = (String) in.readObject();

                System.out.println("Server:\n" + response);

                Scanner input = new Scanner(System.in);
                int opcao = 0;

                System.out.println("Escolha uma opcao:");
                System.out.println("1. Autenticacao");
                System.out.println("2. Registo ");
                System.out.print("3. Sair\n>");
                do {
                    try{
                        opcao = input.nextInt();

                        if (opcao < 1 || opcao > 3) { // se a excecao for lancada este if n é executado
                            System.out.print("Opcao invalida. Por favor escolha entre 1 e 3\n> ");
                        }
                        if(opcao == 3){
                            desconectarDoServidor(socket);
                            exit(0);
                        }


                    }catch (InputMismatchException e){
                        System.out.print("Opcao invalida. Por favor escolha entre 1 e 3\n> ");
                        input.nextLine();
                    }
                } while (opcao < 1 || opcao > 3);

                if (opcao == 1) {
                    do {
                        System.out.print("Nome: ");
                        String nome = input.next();
                        System.out.print("Password: ");
                        String pass = input.next();

                        //Envia comando login ao servidor - Codigo 1
                        command = "1 " + nome + " " + pass;
                        res = enviaComando(command);
                        System.out.println(res);

                        if(!res.contains("Login aceite"))
                            System.out.println("Tente novamente..");

                    }while(!res.contains("Login aceite"));

                } else {
                    System.out.print("Email: ");
                    String email = input.next();
                    System.out.print("Nome: ");
                    String nome = input.next();
                    System.out.print("Numero de Telefone: ");
                    int telefone = 0;
                    do {
                        try{
                            telefone = input.nextInt();

                        }catch (InputMismatchException e){
                            System.out.print("Telefone invalido. Tente novamente...\n> ");
                            input.nextLine();
                        }
                    } while (telefone == 0);

                    System.out.print("Password: ");
                    String password = input.next();

                    //enviar ao server como register - Codigo 2
                    command = "2 " + email + " " + nome + " " + telefone+ " " + password;
                    res = enviaComando(command);
                    System.out.println(res);
                }

                boolean continuar = true;  // Variável de controle para encerrar o loop

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
                    System.out.print("21. Logout\n> "); // nao precisa de selecionar grupo atual

                    int option = 0;
                    do{
                        try{
                            option = input.nextInt();

                            if (option <= 1 || option >=21) { // se a excecao for lancada este if n é executado (acho...)
                                System.out.print("Opcao invalida. Por favor escolha novamente\n> ");
                            }
                        }catch (InputMismatchException e){
                            System.out.print("Opcao invalida. Por favor escolha novamente\n> ");
                            input.nextLine();
                        }
                    }while (option < 1 || option >21);


                    switch (option) {
                        case 1 -> {
                            System.out.println("\nEscolha o campo a editar:");
                            System.out.println("1. Editar nome");
                            System.out.println("2. Editar numero de telefone");
                            System.out.println("3. Editar email");
                            System.out.println("4. Editar password");
                            System.out.print("5. Cancelar\n> ");

                            int escolha = 0;
                            do{
                                try{
                                    escolha = input.nextInt();

                                    if (escolha < 1 || escolha > 5) { // se a excecao for lancada este if n é executado (acho...)
                                        System.out.print("Escolha invalida. Por favor escolha novamente\n> ");
                                    }
                                }catch (InputMismatchException e){
                                    System.out.print("Escolha invalida. Por favor escolha novamente\n> ");
                                    input.nextLine();
                                }
                            } while (escolha < 1 || escolha > 5);

                            if (escolha == 5) break;

                            System.out.print("\nNovo dado: ");
                            String novoCampo = input.next();

                            System.out.print("\nConfirme a sua password:  ");
                            String password = input.next();

                            //enviar ao server como mundanca de campo - Codigo 3
                            command = "3 " + escolha + " " + novoCampo + " " + password;
                            res = enviaComando(command);
                            System.out.println(res);
                        }
                        case 2 -> {
                            System.out.print("\nNome do novo grupo: ");
                            String novo = input.next();

                            //enviar ao server como mundanca de nome de grupo - Codigo 4
                            command = "4 " + novo;
                            res = enviaComando(command);
                            System.out.println(res);
                        }
                        case 3 -> {
                            System.out.print("\nNome do grupo: ");
                            String novo = input.next();

                            //enviar ao server como selecao de grupo - Codigo 5
                            command = "5 " + novo;
                            res = enviaComando(command);
                            System.out.println(res);
                        }
                        case 4 -> {
                            System.out.print("\nEmail do destinatário: ");
                            String email = input.next();

                            //enviar ao server como criacao de novo convite - Codigo 6
                            command = "6 " + email;
                            res = enviaComando(command);
                            System.out.println(res);
                        }
                        case 5 -> {
                            //enviar ao server como mostrar convites - Codigo 7
                            command = "7 ";
                            res = enviaComando(command);
                            System.out.println(res);
                        }
                        case 6 -> {
                            System.out.print("\nId do convite: "); //convites teem id? ou é so o nome do grupo. é possivel receber dois convites do mesmo grupo?
                            int id = -1;
                            do {
                                try{
                                    id = input.nextInt();
                                }catch (InputMismatchException e){
                                    System.out.print("Id invalido. Tente novamente\n> ");
                                    input.nextLine();
                                }
                            } while (id < 0);

                            System.out.println("Escolha o que fazer com o convite: ");

                            System.out.println("\n1. Aceitar");
                            System.out.println("2. Recusar");
                            System.out.print("3. Cancelar\n> ");

                            int escolha = 0;
                            do{
                                try{
                                    escolha = input.nextInt();

                                    if (escolha < 1 || escolha > 3) { // se a excecao for lancada este if n é executado
                                        System.out.print("Escolha invalida. Por favor escolha novamente\n> ");
                                    }
                                }catch (InputMismatchException e){
                                    System.out.print("Escolha invalida. Por favor escolha novamente\n> ");
                                    input.nextLine();
                                }
                            }while (escolha < 1 || escolha > 3);

                            if (escolha == 3) break;

                            //enviar ao server como criacao de novo convite - Codigo 8
                            command = "8 " + id + " " + escolha;
                            res = enviaComando(command);
                            System.out.println(res);
                        }
                        case 7 -> {
                            //enviar ao server como listar grupos - Codigo 9
                            command = "9";
                            res = enviaComando(command);
                            System.out.println(res);
                        }
                        case 8 -> {
                            System.out.print("\nNovo nome do grupo: ");
                            String novo = input.next();

                            //enviar ao server como edicao do nome do grupo - Codigo 10
                            command = "10 " + novo;
                            res = enviaComando(command);
                            System.out.println(res);
                        }
                        case 9 -> {
                            System.out.print("\nNome do grupo a eliminar: ");
                            String novo = input.next();

                            //enviar ao server como eliminar grupo - Codigo 9
                            command = "11 " + novo;
                            res = enviaComando(command);
                            System.out.println(res);
                        }
                        case 10 -> {
                            System.out.print("\nNome do grupo: ");
                            String novo = input.next();

                            //enviar ao server como sair de grupo - Codigo 9
                            command = "12 " + novo;
                            res = enviaComando(command);
                            System.out.println(res);
                        }
                        case 11 -> {

                            System.out.print("Valor: ");
                            float valor = -1;
                            do {
                                try{
                                    valor = input.nextFloat();
                                }catch (InputMismatchException e){
                                    System.out.print("Valor invalido. Tente novamente\n> ");
                                    input.nextLine();
                                }
                            } while (valor < 0);

                            System.out.print("Data (dd/mm/aa): ");
                            String data = input.next();
                            System.out.print("Quem pagou: ");
                            String quem = input.next();
                            System.out.print("Elementos a partilhar (<email> <email> ...): ");
                            String comQuem = input.nextLine();
                            System.out.print("Descricao: ");
                            String descricao = input.nextLine();

                            //enviar ao server como nova despesa - Codigo 9
                            command = "13 " + valor + " " + data + " " + quem + " ;" + comQuem + ";" + descricao;

                            res = enviaComando(command);
                            System.out.println(res);
                        }
                        case 12 -> {
                            //enviar ao server como ver valor das despesas- Codigo 9
                            command = "14";
                            res = enviaComando(command);
                            System.out.println(res);
                        }
                        case 13 -> {
                            //enviar ao server como ver historico das despesas- Codigo 15
                            command = "15";
                            res = enviaComando(command);
                            System.out.println(res);
                        }
                        case 14 -> {
                            //enviar ao server como exportar historico das despesas- Codigo 16
                            command = "16";
                            res = enviaComando(command);
                            System.out.println(res);
                        }
                        case 15 -> {
                            System.out.print("Id da despesa: ");
                            int id = -1;
                            do {
                                try{
                                    id = input.nextInt();
                                }catch (InputMismatchException e){
                                    System.out.print("Id invalido. Tente novamente\n> ");
                                    input.nextLine();
                                }
                            } while (id < 0);

                            System.out.println("\nParametro a editar: ");

                            System.out.println("1. Data (dd/mm/aa) ");
                            System.out.println("2. Descricao ");
                            System.out.println("3. Valor ");
                            System.out.println("4. Quem pagou ");
                            System.out.println("5. Elementos partilhados (<nome> <nome> ...) ");
                            System.out.print("6. Cancelar\n> ");

                            int campo = 0;
                            do {
                                try{
                                    campo = input.nextInt();

                                    if (campo < 1 || campo > 6) { // se a excecao for lancada este if n é executado (acho...)
                                        System.out.print("Opcao invalida. Por favor escolha novamente\n> ");
                                    }
                                }catch (InputMismatchException e){
                                    System.out.print("Opcao invalida. Por favor escolha 1 ou 2\n> ");
                                    input.nextLine();
                                }
                            } while (campo < 1 || campo > 6);

                            if (campo == 6) break;

                            System.out.print("Novo valor: ");
                            String valor = input.nextLine();

                            //enviar ao server como edicao de  despesa - Codigo 17
                            command = "17 " + id + " " + campo + " " + valor;
                            res = enviaComando(command);
                            System.out.println(res);
                        }
                        case 16 -> {
                            System.out.print("Id da despesa: ");
                            int id = -1;
                            do {
                                try{
                                    id = input.nextInt();
                                }catch (InputMismatchException e){
                                    System.out.print("Id invalido. Tente novamente\n> ");
                                    input.nextLine();
                                }
                            } while (id < 0);

                            //enviar ao server como eliminar despesa - Codigo 18
                            command = "18 " + id;
                            res = enviaComando(command);
                            System.out.println(res);

                        }
                        case 17 -> {
                            System.out.print("Quem pagou: ");
                            String quemP = input.next();
                            System.out.print("Quem recebeu: ");
                            String quemR = input.next();
                            System.out.print("Data (dd/mm/aa): ");
                            String data = input.next();
                            System.out.print("Valor: ");
                            float valor = -1;
                            do {
                                try{
                                    valor = input.nextFloat();
                                }catch (InputMismatchException e){
                                    System.out.print("Valor invalido. Tente novamente\n> ");
                                    input.nextLine();
                                }
                            } while (valor < 0);

                            //enviar ao server como novo pagamento - Codigo 19
                            command = "19 " + quemP + " " + quemR + " " + data + " " + valor;
                            res = enviaComando(command);
                            System.out.println(res);
                        }
                        case 18 -> {
                            //enviar ao server como novo pagamento - Codigo 19
                            command = "20";
                            res = enviaComando(command);
                            System.out.println(res);
                        }
                        case 19 -> {
                            System.out.print("Id do pagamento: ");
                            int id = -1;
                            do {
                                try{
                                    id = input.nextInt();
                                }catch (InputMismatchException e){
                                    System.out.print("Id invalido. Tente novamente\n> ");
                                    input.nextLine();
                                }
                            } while (id < 0);

                            command = "21 " + id;
                            res = enviaComando(command);
                            System.out.println(res);
                        }
                        case 20 -> {
                            command = "21";
                            res = enviaComando(command);
                            System.out.println(res);
                        }
                        case 21 -> {

                            continuar = false;
                            //desconectarDoServidor(in, out, socket);
                            System.out.println("\nAte a proxima!");

                        }
                    }

                } while (continuar);

                //logout ou servidor termina
                desconectarDoServidor(socket);
                System.out.println("Desconectado com sucesso.");
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

    }
}
