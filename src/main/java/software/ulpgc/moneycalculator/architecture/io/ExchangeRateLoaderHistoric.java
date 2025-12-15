package software.ulpgc.moneycalculator.architecture.io;

import software.ulpgc.moneycalculator.architecture.model.Currency;
import software.ulpgc.moneycalculator.architecture.model.ExchangeRate;

import java.time.LocalDate;
import java.util.stream.Stream;

public interface ExchangeRateLoaderHistoric {
    Stream<ExchangeRate> load(Currency from, Currency to, LocalDate From, LocalDate To);
}
