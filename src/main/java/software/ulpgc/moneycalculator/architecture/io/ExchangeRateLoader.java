package software.ulpgc.moneycalculator.architecture.io;

import software.ulpgc.moneycalculator.architecture.model.Currency;
import software.ulpgc.moneycalculator.architecture.model.ExchangeRate;

public interface ExchangeRateLoader {

    ExchangeRate load(Currency from, Currency to);
}
