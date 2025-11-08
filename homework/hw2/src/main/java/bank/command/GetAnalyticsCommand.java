package bank.command;

import bank.dto.AnalyticsReport;
import bank.facade.FinanceFacade;

import java.time.LocalDate;

/**
 * Команда для получения аналитики.
 */
public class GetAnalyticsCommand implements Command {

    private final FinanceFacade financeFacade;
    private final LocalDate from;
    private final LocalDate to;

    public GetAnalyticsCommand(FinanceFacade financeFacade, LocalDate from, LocalDate to) {
        this.financeFacade = financeFacade;
        this.from = from;
        this.to = to;
    }

    @Override
    public void execute() {
        AnalyticsReport report = financeFacade.getAnalytics(from, to);
        System.out.println(report);
    }
}