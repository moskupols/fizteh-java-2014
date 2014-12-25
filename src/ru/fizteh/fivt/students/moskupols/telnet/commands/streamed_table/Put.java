package ru.fizteh.fivt.students.moskupols.telnet.commands.streamed_table;

import ru.fizteh.fivt.storage.structured.Storeable;
import ru.fizteh.fivt.storage.structured.Table;
import ru.fizteh.fivt.storage.structured.TableProvider;
import ru.fizteh.fivt.students.moskupols.cliutils2.commands.KnownArgsCountNameFirstCommand;
import ru.fizteh.fivt.students.moskupols.cliutils2.exceptions.CommandExecutionException;
import ru.fizteh.fivt.students.moskupols.telnet.commands.LocalContext;

/**
 * Created by moskupols on 09.12.14.
 */
public class Put extends KnownArgsCountNameFirstCommand {
    @Override
    public String name() {
        return "put";
    }

    @Override
    public int expectedArgsCount() {
        return 3;
    }

    @Override
    protected void performAction(Object context, String[] args) throws CommandExecutionException {
        final LocalContext cont = (LocalContext) context;
        Table table = cont.getCurrentTable();
        if (table == null) {
            throw new CommandExecutionException(this, "no table");
        }
        TableProvider provider = cont.getProvider();
        Storeable oldStoreable;
        try {
            oldStoreable = table.put(args[1], provider.deserialize(table, args[2]));
        } catch (Exception e) {
            throw new CommandExecutionException(this, "Value parse error: " + e.getMessage(), e);
        }
        if (oldStoreable == null) {
            ((LocalContext) context).getOutputPrinter().println("new");
        } else {
            ((LocalContext) context).getOutputPrinter().println("overwrite");
            ((LocalContext) context).getOutputPrinter().println(provider.serialize(table, oldStoreable));
        }
    }
}
