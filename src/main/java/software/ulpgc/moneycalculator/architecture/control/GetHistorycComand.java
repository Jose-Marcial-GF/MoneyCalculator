package software.ulpgc.moneycalculator.architecture.control;

import software.ulpgc.moneycalculator.application.Aguakate.ExchangeRateLoader;
import software.ulpgc.moneycalculator.architecture.io.ChartBuilder;
import software.ulpgc.moneycalculator.architecture.model.Chart;
import software.ulpgc.moneycalculator.architecture.model.Currency;
import software.ulpgc.moneycalculator.architecture.model.ExchangeRate;
import software.ulpgc.moneycalculator.architecture.ui.ChartDisplay;
import software.ulpgc.moneycalculator.architecture.ui.CurrencyDialog;

import java.time.LocalDate;
import java.util.stream.Stream;

public class GetHistorycComand implements Command {

    private final CurrencyDialog fromCurrencyDialog;
    private final CurrencyDialog tocurrencyDialog;
    private final ExchangeRateLoader exchangeRateLoader;
    private final ChartDisplay chartDisplay;
    private final LocalDate elapstime;

    public GetHistorycComand(CurrencyDialog fromCurrencyDialog, CurrencyDialog tocurrencyDialog, ExchangeRateLoader exchangeRateLoader, ChartDisplay chartDisplay, LocalDate elapstime) {
        this.fromCurrencyDialog = fromCurrencyDialog;
        this.tocurrencyDialog = tocurrencyDialog;
        this.exchangeRateLoader = exchangeRateLoader;
        this.chartDisplay = chartDisplay;
        this.elapstime = elapstime;
    }


    @Override
    public void execute() {
        Currency currencyFrom = fromCurrencyDialog.get();
        Currency currencyTo = tocurrencyDialog.get();
        Stream<ExchangeRate> exchangeRate = exchangeRateLoader.load(currencyFrom, currencyTo, elapstime, LocalDate.now());
        Chart chart = ChartBuilder.with(exchangeRate).title("Exchage Rate: " + currencyFrom.code()+ " -> " + currencyTo.code()).x("date").legend("cover").build();
        chartDisplay.show(chart);
    }

}
