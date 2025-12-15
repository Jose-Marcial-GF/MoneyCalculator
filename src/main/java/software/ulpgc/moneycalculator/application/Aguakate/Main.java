package software.ulpgc.moneycalculator.application.Aguakate;


import software.ulpgc.moneycalculator.architecture.control.ExchangeMoneyCommand;
import software.ulpgc.moneycalculator.architecture.control.GetHistorycComand;
import software.ulpgc.moneycalculator.architecture.io.ChartBuilder;
import software.ulpgc.moneycalculator.architecture.model.Chart;
import software.ulpgc.moneycalculator.architecture.model.Currency;
import software.ulpgc.moneycalculator.architecture.model.ExchangeRate;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

public class Main {

/*
    public static void main(String[] args) {
        Stream<Currency> webService = new WebService().loadAll();
        ExchangeRateLoader exchangeRateLoader = new ExchangeRateLoader();
        List<Currency> limit = webService.limit(2).toList();
        Stream<ExchangeRate> load = exchangeRateLoader.load(limit.get(0), limit.get(1), LocalDate.of(2025, 12, 1), LocalDate.now());
        Chart chart = ChartBuilder.with(load).title("titulo").legend("legend").x("x").build();
        new ChartPanelDisplay(chart).show(chart);
    }
}
*/

    public static void main(String[] args) {
        Desktop desktop = new Desktop(new WebService().loadAll());
        desktop.addCommand("weekago", new GetHistorycComand(
                desktop.inputCurrencyDialog(),
                desktop.currencyDialog(),
                new ExchangeRateLoader(),
                desktop.chartDisplay()
        ));
        desktop.addCommand("exchange", new ExchangeMoneyCommand(
                desktop.moneyDialog(),
                desktop.currencyDialog(),
                new ExchangeRateLoader(),
                desktop.moneyDisplay()
        ));
        desktop.setVisible(true);

    }
}
