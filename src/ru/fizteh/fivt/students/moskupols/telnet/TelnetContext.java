package ru.fizteh.fivt.students.moskupols.telnet;

import ru.fizteh.fivt.students.moskupols.proxy.AutoCloseableCachingTableProvider;
import ru.fizteh.fivt.students.moskupols.proxy.AutoCloseableTable;

import java.io.OutputStream;

/**
 * Created by moskupols on 25.12.14.
 */
public class TelnetContext {
    private TelnetContext(
            InteractionMode mode, OutputStream outputStream,
            AutoCloseableTable currentTable, AutoCloseableCachingTableProvider currentProvider,
            AutoCloseableCachingTableProvider localProvider,
            TelnetServer masterServer, TelnetClient slaveClient) {
        this.mode = mode;
        this.outputStream = outputStream;
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

    public enum InteractionMode {
        LOCAL,
        REMOTE_MASTER,
        REMOTE_SLAVE;

        private InteractionMode() {
        }
    }

    private InteractionMode mode;

    private final OutputStream outputStream;

    private AutoCloseableTable currentTable;
    private AutoCloseableCachingTableProvider currentProvider;

    private AutoCloseableCachingTableProvider localProvider;

    private final TelnetServer masterServer;
    private final TelnetClient slaveClient;
}
