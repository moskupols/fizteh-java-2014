package ru.fizteh.fivt.students.moskupols.telnet.commands.streamed_table;

import ru.fizteh.fivt.storage.structured.Storeable;
import ru.fizteh.fivt.storage.structured.Table;
import ru.fizteh.fivt.students.moskupols.cliutils2.commands.KnownArgsCountNameFirstCommand;
import ru.fizteh.fivt.students.moskupols.cliutils2.exceptions.CommandExecutionException;
import ru.fizteh.fivt.students.moskupols.telnet.commands.LocalContext;

/**
 * Created by moskupols on 09.12.14.
 */
public class Get extends KnownArgsCountNameFirstCommand {
    @Override
    public int expectedArgsCount() {
        return 2;
    }

    @Override
    public String name() {
        return "get";
    }

    @Override
    protected void performAction(Object context, String[] args)
            throws CommandExecutionException {
        final LocalContext cont = (LocalContext) context;
        final Table table = cont.getCurrentTable();
        if (table == null) {
            throw new CommandExecutionException(this, "no table");
        }
        Storeable ret = table.get(args[1]);
        String str;
        if (ret == null) {
            str = "not found";
        } else {
            str = cont.getProvider().serialize(table, ret);
        }
        ((LocalContext) context).getOutputPrinter().println(str);
    }
}
