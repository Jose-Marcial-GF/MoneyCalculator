package software.ulpgc.moneycalculator.architecture.io;

import software.ulpgc.moneycalculator.architecture.model.Currency;

import java.util.stream.Stream;

public interface CurrencyLoader {
    Stream<Currency> loadAll();
}
