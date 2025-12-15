package software.ulpgc.moneycalculator.architecture.model;

import java.time.LocalDate;

public record ExchangeRate(LocalDate date, Currency from, Currency to, double rate) {
}
