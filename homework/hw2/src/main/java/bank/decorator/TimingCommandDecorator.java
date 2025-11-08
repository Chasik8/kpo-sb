package bank.decorator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import bank.command.Command;

/**
 * Паттерн "Декоратор".
 * Оборачивает любую {@link Command} для измерения времени ее выполнения.
 * Соответствует принципу OCP.
 */
public class TimingCommandDecorator implements Command {

    private static final Logger logger = LoggerFactory.getLogger(TimingCommandDecorator.class);

    private final Command wrappedCommand;

    public TimingCommandDecorator(Command wrappedCommand) {
        this.wrappedCommand = wrappedCommand;
    }

    @Override
    public void execute() {
        long startTime = System.nanoTime();

        try {

            wrappedCommand.execute();
        } finally {
            long endTime = System.nanoTime();
            long durationMs = (endTime - startTime) / 1_000_000;

            String commandName = wrappedCommand.getClass().getSimpleName();


            logger.info("Scenario {} executed in {} ms", commandName, durationMs);
            System.out.println("(Debug: Scenario " + commandName + " executed in " + durationMs + " ms)");
        }
    }
}