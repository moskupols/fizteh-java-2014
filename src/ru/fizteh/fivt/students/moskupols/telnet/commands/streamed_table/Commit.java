package ru.fizteh.fivt.students.moskupols.telnet.commands.streamed_table;

import ru.fizteh.fivt.storage.structured.Table;
import ru.fizteh.fivt.students.moskupols.cliutils2.commands.KnownArgsCountNameFirstCommand;
import ru.fizteh.fivt.students.moskupols.cliutils2.exceptions.CommandExecutionException;
import ru.fizteh.fivt.students.moskupols.telnet.commands.LocalContext;

import java.io.IOException;

/**
 * Created by moskupols on 09.12.14.
 */
public class Commit extends KnownArgsCountNameFirstCommand {
    @Override
    public int expectedArgsCount() {
        return 1;
    }

    @Override
    public String name() {
        return "commit";
    }

    @Override
    protected void performAction(Object context, String[] args) throws CommandExecutionException {
        final Table currentTable = ((LocalContext) context).getCurrentTable();
        if (currentTable == null) {
            throw new CommandExecutionException(this, "no table");
        }
        try {
            ((LocalContext) context).getOutputPrinter().println(currentTable.commit());
        } catch (IOException e) {
            throw new CommandExecutionException(this, e.getMessage(), e);
        }
    }
}
