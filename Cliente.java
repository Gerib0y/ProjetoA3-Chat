import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {
    Socket socket;
    // Inicia o Cliente
    public void init() throws IOException {
        int porta = 9999;
        String host = "localhost";
        this.socket = new Socket(host,porta);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    escutar();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    escrever();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    // Entrada de dados
    public void escutar() throws IOException {
        Scanner entrada = new Scanner(this.socket.getInputStream());
        while(entrada.hasNextLine()){
            String mensagem = entrada.nextLine();
            System.out.println("Nova mensagem: "+mensagem);
        }
    }

    // Saida de dados
    public void escrever() throws IOException {

        PrintStream saida = new PrintStream(this.socket.getOutputStream());

        Scanner input = new Scanner(System.in);

        System.out.println("Digite seu id: ");
        String meuid = input.nextLine();

        System.out.println("Digite o id de destino");
        String idDestino = input.nextLine();
        // ID
        saida.println("meuid:"+meuid);

        String mensagem = "";

        // Palavra chave para sair do chate + loop
        while(!mensagem.contains("encerrar")){
            mensagem = idDestino+":";

            System.out.println("Digite sua mensagem: ");
            mensagem += input.nextLine();
            
            saida.println(mensagem);
        }

    }

    public static void main(String args[]) throws IOException {
        new Cliente().init();
    }
}
