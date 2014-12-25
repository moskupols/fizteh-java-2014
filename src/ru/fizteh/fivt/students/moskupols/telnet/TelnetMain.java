package ru.fizteh.fivt.students.moskupols.telnet;

import ru.fizteh.fivt.storage.structured.TableProvider;
import ru.fizteh.fivt.students.moskupols.proxy.AutoCloseableCachingTableProvider;
import ru.fizteh.fivt.students.moskupols.proxy.AutoCloseableTableProviderFactoryImpl;
import ru.fizteh.fivt.students.moskupols.proxy.LoggingProxyFactoryImpl;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Scanner;

/**
 * Created by moskupols on 25.12.14.
 */
public class TelnetMain {
    public static final String DB_DIR_PROPERTY = "fizteh.db.dir";

    public static void main(String[] args) {
        String dbPath = System.getProperty(DB_DIR_PROPERTY);
        if (dbPath == null) {
            System.err.format("Specify database file in property %s.%n", DB_DIR_PROPERTY);
            System.exit(1);
        }
        final TableProvider provider;
        try {
            provider = new AutoCloseableTableProviderFactoryImpl(
                    new LoggingProxyFactoryImpl(new OutputStreamWriter(System.out))).
                    create(dbPath);

            TelnetServer server;
            server = new TelnetServer((AutoCloseableCachingTableProvider) provider);
            try {
                server.start(10001);
                new Scanner(System.in).nextLine();
            } finally {
                server.stop();
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
