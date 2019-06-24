import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
    private InetAddress serverAddress;
    private int port = -1;
    private ClientRequests request;
    private int clients = -1;
    private Scanner scanner;
    private int threadId;
    private ClientRunnable[] clientThreads;
    private Thread[] threadpool;

    public Client() {
        this.scanner = new Scanner(System.in);
    }

    private ClientRequests getRequest() {
        System.out.print("Please enter a command. (Enter 'h' to display commands): ");
        boolean done = false;
        while (!done) {
            String request = scanner.nextLine();
            if (request.equalsIgnoreCase("h")) {
                System.out.println("Possible commands are: ");
                System.out.println("- Date and Time: datetime");
                System.out.println("- Uptime: uptime");
                System.out.println("- Memory Usage: memuse");
                System.out.println("- Netstat: netstat");
                System.out.println("- Current Users: currentusrs");
                System.out.println("- Running Processes: runprcs");

                System.out.println();
                System.out.print("Please enter a command. (Enter 'h' to display commands): ");

            } else if (request.equalsIgnoreCase("datetime")) {
                return ClientRequests.DATETIME;
            } else if (request.equalsIgnoreCase("uptime")) {
                return ClientRequests.UPTIME;
            } else if (request.equalsIgnoreCase("memuse")) {
                return ClientRequests.MEMORYUSE;
            } else if (request.equalsIgnoreCase("netstat")) {
                return ClientRequests.NETSTAT;
            } else if (request.equalsIgnoreCase("currentusrs")) {
                return ClientRequests.CURRENTUSERS;
            } else if (request.equalsIgnoreCase("runprcs")) {
                return ClientRequests.RUNNINGPROCESSES;
            } else {
                System.err.println("Invalid command. Run 'H' to see valid commands.\n");
            }
        }
        return null;
    }


    private void getInput() {
        boolean collected = false;

        while (!collected) {
            //Get IP address
            if (this.serverAddress == null) {
                System.out.println("Please enter the IP address you would like to send a request to: ");
                String address = this.scanner.nextLine();
                try {
                    this.serverAddress = InetAddress.getByName(address);
                } catch (UnknownHostException e) {
                    System.err.println("Please enter a valid IP address, host name or IP.\n");
                    continue;
                }
            }

            //Get port number
            if (this.port == -1) {
                System.out.print("Please enter the port: ");
                String enteredPort = scanner.nextLine();
                try {
                    int parsedPort = Integer.parseInt(enteredPort);
                    if (parsedPort < 0 || parsedPort > 65535) {
                        throw new Exception();
                    } else {
                        this.port = parsedPort;
                    }
                } catch (Exception e) {
                    System.err.println("Please enter a valid port number (1 - 65536)\n");
                    continue;
                }
            }

            //Get operation
            if (this.request == null) {
                ClientRequests cr = getRequest();
                if (cr == null) {
                    continue;
                } else {
                    this.request = cr;
                }
            }

            //Get number of clients
            if (this.clients == -1) {
                System.out.print("Please enter the number of clients to generate: ");
                String clientString = this.scanner.nextLine();
                try {
                    int clientInt = Integer.parseInt(clientString);
                    if (clientInt < 0) {
                        throw new Exception();
                    } else {
                        this.clients = clientInt;
                    }
                } catch (Exception e) {
                    System.err.println("Please enter a nonzero number of clients.\n");
                    continue;
                }
            }

            collected = true;
        }
    }

    public void start() {
        //Gather input from the user
        getInput();


        //holy shit this is awful code
        this.clientThreads = new ClientRunnable[this.clients];
        this.threadpool = new Thread[this.clients];

        for (int i = 0; i < this.clients; i++) {
            this.clientThreads[i] = new ClientRunnable(this.serverAddress, this.port, this.threadId++, this.request);
            this.threadpool[i] = new Thread(this.clientThreads[i]);
        }

        for (Thread thread : this.threadpool) {
            thread.start();
        }

        for (Thread thread : this.threadpool) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("All threads have finished!");

        long totalTime = 0;
        for (ClientRunnable client : this.clientThreads) {
            System.out.println(client.getThreadID() + " has been running for " + client.getRunningTime() + " milliseconds");
            totalTime += client.getRunningTime();
        }

        System.out.println("Total elapsed time: " + totalTime + " milliseconds");
        System.out.println("Average elapsed time: " + totalTime / this.clients + " milliseconds");
    }



    public static void main(String[] args) {
        Client client = new Client();
        client.start();
    }
}
