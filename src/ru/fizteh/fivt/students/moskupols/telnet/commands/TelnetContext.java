package ru.fizteh.fivt.students.moskupols.telnet.commands;

import ru.fizteh.fivt.storage.structured.Table;
import ru.fizteh.fivt.students.moskupols.proxy.AutoCloseableCachingTableProvider;
import ru.fizteh.fivt.students.moskupols.proxy.AutoCloseableTable;
import ru.fizteh.fivt.students.moskupols.telnet.TelnetClient;
import ru.fizteh.fivt.students.moskupols.telnet.TelnetServer;

import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * Created by moskupols on 25.12.14.
 */
public class TelnetContext implements LocalContext {
    private TelnetContext(
            InteractionMode mode, OutputStream outputStream,
            AutoCloseableTable currentTable, AutoCloseableCachingTableProvider currentProvider,
            AutoCloseableCachingTableProvider localProvider,
            TelnetServer masterServer, TelnetClient slaveClient) {
        this.mode = mode;
        this.outputPrinter = new PrintWriter(outputStream);
        this.currentTable = currentTable;
        this.currentProvider = currentProvider;
        this.localProvider = localProvider;
        this.masterServer = masterServer;
        this.slaveClient = slaveClient;
    }

    public static TelnetContext createLocal(AutoCloseableCachingTableProvider provider) {
        return new TelnetContext(
                InteractionMode.LOCAL,
                System.out,
                null,
                provider, provider,
                new TelnetServer(provider), null
        );
    }

    public static TelnetContext createRemoteMaster(
            AutoCloseableCachingTableProvider provider,
            TelnetServer masterServer,
            OutputStream outputStream) {
        return new TelnetContext(
                InteractionMode.REMOTE_MASTER,
                outputStream,
                null,
                provider, provider,
                masterServer, null);
    }

    @Override
    public AutoCloseableCachingTableProvider getProvider() {
        return currentProvider;
    }

    @Override
    public AutoCloseableTable getCurrentTable() {
        return currentTable;
    }

    @Override
    public void setCurrentTable(Table currentTable) {
        setCurrentTable((AutoCloseableTable) currentTable);
    }

    @Override
    public void setCurrentTable(AutoCloseableTable currentTable) {
        this.currentTable = currentTable;
    }

    @Override
    public PrintWriter getOutputPrinter() {
        return outputPrinter;
    }

    public enum InteractionMode {
        LOCAL,
        REMOTE_MASTER,
        REMOTE_SLAVE;

        private InteractionMode() {
        }
    }

    private InteractionMode mode;

    private final PrintWriter outputPrinter;

    private AutoCloseableTable currentTable;
    private AutoCloseableCachingTableProvider currentProvider;

    private AutoCloseableCachingTableProvider localProvider;

    private final TelnetServer masterServer;
    private final TelnetClient slaveClient;
}
