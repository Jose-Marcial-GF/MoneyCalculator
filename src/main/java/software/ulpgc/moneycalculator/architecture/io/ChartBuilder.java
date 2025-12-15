package software.ulpgc.moneycalculator.architecture.io;

import software.ulpgc.moneycalculator.architecture.model.Chart;
import software.ulpgc.moneycalculator.architecture.model.ExchangeRate;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class ChartBuilder {

    private final Stream<ExchangeRate> exchangeRates;
    private final Map<String, String > labels;

    private ChartBuilder(Stream<ExchangeRate> exchangeRates) {
        this.exchangeRates = exchangeRates;
        this.labels = new HashMap<>();
    }

    public ChartBuilder title(String title) {
        labels.put("title", title);
        return this;
    }

    public ChartBuilder legend(String legend) {
        labels.put("legend", legend);
        return this;
    }

    public ChartBuilder x(String x) {
        labels.put("x", x);
        return this;
    }

    public static ChartBuilder with(Stream<ExchangeRate> exchangeRates){
        return new ChartBuilder(exchangeRates);
    }

    public Chart  build () {
        return new Chart(exchangeRates, labels);
    }
}
