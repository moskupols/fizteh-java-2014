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
    public static void main(String[] args) throws IOException {
        final TableProvider provider = new AutoCloseableTableProviderFactoryImpl(
                new LoggingProxyFactoryImpl(new OutputStreamWriter(System.out))).create("");
        TelnetServer server = new TelnetServer((AutoCloseableCachingTableProvider) provider);
        server.start(10001);
        new Scanner(System.in).nextLine();
        server.stop();
    }
}
