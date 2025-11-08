package bank.service;

import bank.dto.AnalyticsReport;

import java.time.LocalDate;

/**
 * Интерфейс сервиса для финансовой аналитики.
 */
public interface AnalyticsService {
    /**
     * Подсчет разницы доходов и расходов за выбранный период.
     */
    AnalyticsReport calculateAnalytics(LocalDate from, LocalDate to);


}