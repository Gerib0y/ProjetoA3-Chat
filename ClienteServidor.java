import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class ClienteServidor {
    String id;
    Socket socket;


    public void init(){
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
    }

    public ClienteServidor(Socket socket){
        this.socket = socket;
        init();
    }

    //Entrada e saída de dados
    public void enviarMensagem(String idDestino,String mensagem) throws IOException {
        ClienteServidor cliente = Servidor.clientesConectados.get(idDestino);
        cliente.escrever(mensagem);
    }
    public void escrever(String mensagem) throws IOException {
        PrintStream saida = new PrintStream(this.socket.getOutputStream());
        saida.println(mensagem);
    }

    public void escutar() throws IOException {

        Scanner entrada = new Scanner(this.socket.getInputStream());
        while(entrada.hasNextLine()){

            String texto = entrada.nextLine();
            // ID
            if(texto.contains("meuid:")){
                this.id = texto.substring("meuid:".length());
                Servidor.clientesConectados.put(this.id,this);
                continue;
            }
            // Forma que aparece no terminal do servidor
            String mensagem[] = texto.split(":");
            enviarMensagem(mensagem[0],mensagem[1]);
            System.out.println("DE: "+this.id);
            System.out.println("PARA: "+mensagem[0]);
            System.out.println("MENSAGEM: "+mensagem[1]);
        }
    }
}
