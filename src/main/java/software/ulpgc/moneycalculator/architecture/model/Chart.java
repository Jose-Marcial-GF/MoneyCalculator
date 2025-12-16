package software.ulpgc.moneycalculator.architecture.model;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class Chart {

    private final List<Point> points;
    private final Map<String, String> labels;
    public Chart(Stream<ExchangeRate> exchangeRates, Map<String, String> labels) {
        this.points = getPoints(exchangeRates);
        this.labels = labels;
    }

    private static List<Point> getPoints(Stream<ExchangeRate> exchangeRates) {
        return exchangeRates.map(Chart::toPoint).toList();
    }

    public Stream<Point> Points() {
        return points.stream();
    }

    public List<Point> PointsList() {
        return this.points;
    }

    public String title() {
        return labels.getOrDefault("title", "");
    }

    public String legend() {
        return labels.getOrDefault("legend", "");
    }

    public String x() {
        return labels.getOrDefault("x", "");
    }

    private static Point toPoint(ExchangeRate exchangeRate) {
        return new Point(exchangeRate.date(), exchangeRate.rate());
    }


}
