package ru.fizteh.fivt.students.moskupols.telnet.commands;

import ru.fizteh.fivt.students.moskupols.proxy.AutoCloseableCachingTableProvider;
import ru.fizteh.fivt.students.moskupols.proxy.AutoCloseableTable;
import ru.fizteh.fivt.students.moskupols.storeable.commands.StoreableContext;

import java.io.PrintWriter;

/**
 * Created by moskupols on 12.12.14.
 */
public interface LocalContext extends StoreableContext {
    AutoCloseableCachingTableProvider getProvider();

    AutoCloseableTable getCurrentTable();

    void setCurrentTable(AutoCloseableTable currentTable);

    PrintWriter getOutputPrinter();
}
