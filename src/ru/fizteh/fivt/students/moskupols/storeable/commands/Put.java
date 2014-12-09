package ru.fizteh.fivt.students.moskupols.storeable.commands;

import ru.fizteh.fivt.storage.structured.Storeable;
import ru.fizteh.fivt.storage.structured.Table;
import ru.fizteh.fivt.storage.structured.TableProvider;
import ru.fizteh.fivt.students.moskupols.cliutils2.commands.KnownArgsCountNameFirstCommand;
import ru.fizteh.fivt.students.moskupols.cliutils2.exceptions.CommandExecutionException;

import java.text.ParseException;

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
        final StoreableContext cont = (StoreableContext) context;
        Table table = cont.getCurrentTable();
        TableProvider provider = cont.getProvider();
        Storeable oldStoreable;
        try {
            oldStoreable = table.put(args[1], provider.deserialize(table, args[2]));
        } catch (ParseException e) {
            System.err.println("Value parse error: " + e.getMessage());
            return;
        }
        if (oldStoreable == null) {
            System.out.println("new");
        } else {
            System.out.println("overwrite");
            System.out.println(provider.serialize(table, oldStoreable));
        }
    }
}