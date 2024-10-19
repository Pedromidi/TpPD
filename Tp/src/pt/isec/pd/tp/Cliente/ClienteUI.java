package pt.isec.pd.tp.Cliente;

import pt.isec.pd.tp.MSG;

import java.util.Scanner;

public class ClienteUI {
    public MSG msg;

    private Cliente cliente;

    private Scanner scanner;

    public ClienteUI(){
        this.scanner = new Scanner(System.in);
    }
    public void ApresentaVista(){
        System.out.println("Comece por dar login (login username password)");
        System.out.println("Ou registe-se (Registar nome numero email password)");

        System.out.println("> ");
        String comando =  scanner.nextLine();
        if(!cliente.enviaComando(comando)){
            //TODO suicidio
            return;
        }

        do{
            System.out.println("Comando: ");
            comando = scanner.nextLine();
            cliente.enviaComando(comando);

        }while(true);//servidor ativo

    }

    public void recebeComando(){
        System.out.println("Comando: ");
        Scanner scan = new Scanner(System.in);

    }


}
