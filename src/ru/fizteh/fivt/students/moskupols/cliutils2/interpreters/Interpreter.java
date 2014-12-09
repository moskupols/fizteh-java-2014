package ru.fizteh.fivt.students.moskupols.cliutils2.interpreters;

import ru.fizteh.fivt.students.moskupols.cliutils.CommandExecutionException;
import ru.fizteh.fivt.students.moskupols.cliutils.StopProcessingException;
import ru.fizteh.fivt.students.moskupols.cliutils.UnknownCommandException;
import ru.fizteh.fivt.students.moskupols.cliutils2.CommandChooser;
import ru.fizteh.fivt.students.moskupols.cliutils2.commands.Command;
import ru.fizteh.fivt.students.moskupols.cliutils2.exceptions.InvalidArgsException;

/**
 * Created by moskupols on 02.12.14.
 */
public abstract class Interpreter {
    protected void runJob(Object context, CommandChooser chooser, String line)
            throws UnknownCommandException, CommandExecutionException, StopProcessingException, InvalidArgsException {
        String[] args = line.split("\\s+");
        Command cmd = chooser.commandForArgs(args);
        if (cmd == null) {
            throw new UnknownCommandException(line);
        }
        cmd.perform(context, args);
    }

    abstract void interpret() throws CommandExecutionException, UnknownCommandException, InvalidArgsException;
}