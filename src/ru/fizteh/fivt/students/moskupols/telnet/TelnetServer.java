package ru.fizteh.fivt.students.moskupols.telnet;

import ru.fizteh.fivt.students.moskupols.cliutils2.CommandChooser;
import ru.fizteh.fivt.students.moskupols.cliutils2.NameFirstCommandChooser;
import ru.fizteh.fivt.students.moskupols.cliutils2.commands.ExitCommand;
import ru.fizteh.fivt.students.moskupols.cliutils2.interpreters.Interpreter;
import ru.fizteh.fivt.students.moskupols.proxy.AutoCloseableCachingTableProvider;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by moskupols on 25.12.14.
 */
public class TelnetServer {
    private final AutoCloseableCachingTableProvider localProvider;
    private Listener listener;


    public TelnetServer(AutoCloseableCachingTableProvider localProvider) {
        this.localProvider = localProvider;
    }

    void start(int port) throws IOException {
        listener = new Listener(port);
        listener.start();
    }

    void stop() {
        try {
            listener.serverSocket.close();
        } catch (IOException e) {
            //
        }
    }

    protected Interpreter createMasterInterpreter(Socket socket) throws IOException {
        TelnetContext context =
                TelnetContext.createRemoteMaster(localProvider, this, socket.getOutputStream());
        CommandChooser commandChooser = new NameFirstCommandChooser(
                new ExitCommand()
        );
        return new StreamCommandInterpreter(
                socket.getInputStream(), socket.getOutputStream(),
                context, commandChooser);
    }

    private class Listener extends Thread {
        public final ServerSocket serverSocket;

        public Listener(int port) throws IOException {
            serverSocket = new ServerSocket(port);
        }

        @Override
        public void run() {
            boolean closed = false;
            while (!closed) {
                try {
                    final Socket clientSocket = serverSocket.accept();
                    final Interpreter interpreter = createMasterInterpreter(clientSocket);
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                interpreter.interpret();
                                clientSocket.close();
                            } catch (Exception e) {
                                // actually impossible
                                e.printStackTrace();
                            }
                        }
                    }.start();
                } catch (IOException e) {
                    closed = true;
                }
            }
        }
    }
}
