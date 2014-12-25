package ru.fizteh.fivt.students.moskupols.telnet;

import ru.fizteh.fivt.students.moskupols.cliutils.StopProcessingException;
import ru.fizteh.fivt.students.moskupols.cliutils.UnknownCommandException;
import ru.fizteh.fivt.students.moskupols.cliutils2.CommandChooser;
import ru.fizteh.fivt.students.moskupols.cliutils2.exceptions.CommandExecutionException;
import ru.fizteh.fivt.students.moskupols.cliutils2.interpreters.ShellInterpreter;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Created by moskupols on 25.12.14.
 */
public class StreamCommandInterpreter extends ShellInterpreter {
    private final Scanner inputScanner;
    private final PrintWriter errorPrinter;
    private final Object context;
    private final CommandChooser chooser;

    public StreamCommandInterpreter(
            InputStream inputStream, OutputStream errorStream, Object context, CommandChooser chooser) {
        inputScanner = new Scanner(inputStream);
        errorPrinter = new PrintWriter(errorStream);
        this.context = context;
        this.chooser = chooser;
    }

    @Override
    public void interpret() {
        boolean exitOccurred = false;
        do {
            if (!inputScanner.hasNextLine()) {
                break;
            }

            for (String s : inputScanner.nextLine().split(";")) {
                try {
                    runJob(context, chooser, s);
                } catch (StopProcessingException e) {
                    exitOccurred = true;
                } catch (UnknownCommandException | CommandExecutionException e) {
                    errorPrinter.println(e.getMessage());
                    errorPrinter.flush();
                }
            }
        } while (!exitOccurred);
    }
}
