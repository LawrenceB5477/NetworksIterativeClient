import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientRunnable implements Runnable {
    private InetAddress ip;
    private int port;
    private int threadID;
    private long runningtime;
    private ClientRequests clientRequests;

    public ClientRunnable(InetAddress ip, int port, int threadID, ClientRequests clientRequests) {
        this.ip = ip;
        this.port = port;
        this.threadID = threadID;
        this.runningtime = System.currentTimeMillis();
        this.clientRequests = clientRequests;
    }

    private void clientRequestProtocolSend(PrintWriter out) {
        System.out.println("Thread " + this.threadID + " sending request...");
        out.println(this.getThreadID());
        out.println(ClientRequests.getCommandCode(this.clientRequests));
        out.flush();
    }

    private void clientRequestProtocolRecieve(BufferedReader in) {

    }

    @Override
    public void run() {
        System.out.println("Thread " + this.threadID + " started!");

        try (
                Socket clientSocket = new Socket(this.ip, this.port);
                PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())));
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
        )
        {
            System.out.println("Connected?");
            clientRequestProtocolSend(out);
            clientRequestProtocolRecieve(in);

        } catch (UnknownHostException e) {
            System.err.println("Please specifiy a valid IP address.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.runningtime = System.currentTimeMillis() - this.runningtime;
    }

    public long getRunningTime() {
        return this.runningtime;
    }

    public int getThreadID() {
        return this.threadID;
    }
}
