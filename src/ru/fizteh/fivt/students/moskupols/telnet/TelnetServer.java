package ru.fizteh.fivt.students.moskupols.telnet;

import ru.fizteh.fivt.students.moskupols.cliutils2.CommandChooser;
import ru.fizteh.fivt.students.moskupols.cliutils2.NameFirstCommandChooser;
import ru.fizteh.fivt.students.moskupols.cliutils2.commands.ExitCommand;
import ru.fizteh.fivt.students.moskupols.cliutils2.interpreters.Interpreter;
import ru.fizteh.fivt.students.moskupols.proxy.AutoCloseableCachingTableProvider;
import ru.fizteh.fivt.students.moskupols.telnet.commands.StreamCommandInterpreter;
import ru.fizteh.fivt.students.moskupols.telnet.commands.TelnetContext;
import ru.fizteh.fivt.students.moskupols.telnet.commands.streamed_table.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by moskupols on 25.12.14.
 */
public class TelnetServer {
    private final AutoCloseableCachingTableProvider localProvider;
    private Listener listener;

    public TelnetServer(AutoCloseableCachingTableProvider localProvider) {
        this.localProvider = localProvider;
    }

    public void start(int port) throws IOException {
        if (isListening()) {
            throw new IllegalStateException("already listening");
        }
        listener = new Listener(port);
        listener.start();
    }

    public void stop() {
        if (isListening()) {
            listener.shutdownGracefully();
            listener = null;
        }
    }

    public boolean isListening() {
        return listener != null;
    }

    private class Listener extends Thread {
        private final ServerSocket serverSocket;
        private final List<Master> masters;

        public Listener(int port) throws IOException {
            serverSocket = new ServerSocket(port);
            masters = new LinkedList<>();
        }

        @Override
        public void run() {
            boolean closed = false;
            try {
                while (!closed) {
                    try {
                        final Socket clientSocket = serverSocket.accept();
                        final Master master = new Master(clientSocket);
                        masters.add(master);
                        master.start();
                    } catch (IOException e) {
                        closed = true;
                    }
                }
            } finally {
                shutdownGracefully();
            }
        }

        public synchronized void shutdownGracefully() {
            try {
                serverSocket.close();
                masters.forEach(TelnetServer.Master::shutdownGracefully);
            } catch (IOException e) {
                // nothing to do here
            }
        }
    }

    private class Master extends Thread {
        private final Socket clientSocket;

        public Master(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        protected Interpreter createInterpreter(Socket socket) throws IOException {
            TelnetContext context =
                    TelnetContext.createRemoteMaster(
                            localProvider, TelnetServer.this, socket.getOutputStream());
            CommandChooser commandChooser = new NameFirstCommandChooser(
                    new Commit(), new Create(), new Drop(),
                    new Get(), new Put(), new Remove(),
                    new Rollback(), new Size(), new Use(),
                    new ShowTables(), new ExitCommand()
            );
            return new StreamCommandInterpreter(
                    socket.getInputStream(), socket.getOutputStream(),
                    context, commandChooser);
        }

        @Override
        public void run() {
            try {
                final Interpreter interpreter = createInterpreter(clientSocket);
                interpreter.interpret();
            } catch (Exception e) {
                // actually impossible
                e.printStackTrace();
            } finally {
                shutdownGracefully();
            }
        }

        public synchronized void shutdownGracefully() {
            try {
                clientSocket.close();
            } catch (IOException e) {
                // nothing to do here
            }
        }
    }
}
