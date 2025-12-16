package software.ulpgc.moneycalculator.application.Aguakate;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import software.ulpgc.moneycalculator.architecture.control.Command;
import software.ulpgc.moneycalculator.architecture.model.Chart;
import software.ulpgc.moneycalculator.architecture.model.Currency;
import software.ulpgc.moneycalculator.architecture.model.Money;
import software.ulpgc.moneycalculator.architecture.model.Point;
import software.ulpgc.moneycalculator.architecture.ui.ChartDisplay;
import software.ulpgc.moneycalculator.architecture.ui.CurrencyDialog;
import software.ulpgc.moneycalculator.architecture.ui.MoneyDialog;
import software.ulpgc.moneycalculator.architecture.ui.MoneyDisplay;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static java.awt.BorderLayout.*;
import static java.awt.FlowLayout.CENTER;

public class Desktop extends JFrame{
    private final Map<String, Command> commands;
    private final Currency[] currencies;
    private JTextField inputAmount;
    private JComboBox<Currency> inputCurrency;
    private JTextField outputAmount;
    private JComboBox<Currency> outputCurrency;
    private ChartPanel chartPanel;

    public Desktop(Stream<Currency> currencies) throws HeadlessException {
        this.commands = new HashMap<>();
        this.currencies = currencies.toArray(Currency[]::new);
        this.setTitle("Money Calculator");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800,600);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setLayout(new BorderLayout());
        this.getContentPane().add(panel(), NORTH);
        this.getContentPane().add(southpanel(), SOUTH);
        this.chartPanel = chartPanel();
        this.getContentPane().add(chartPanel, BorderLayout.CENTER);
    }

    public ChartPanel chartPanel() {
        return new ChartPanel(null);
    }


    private JPanel southpanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.add(createButton("Week", "week"));
        panel.add(createButton("Month", "month"));
        panel.add(createButton("6 Month", "6month"));
        panel.add(createButton("Year", "year"));
        panel.add(createButton("5 Year", "5year"));
        panel.add(createButton("Max", "max"));
        return panel;
    }

    private JPanel panel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(CENTER));
        panel.add(inputAmount = amountInput());
        panel.add(inputCurrency = currencySelector());
        panel.add(outputAmount = amountOutput());
        panel.add(outputCurrency = currencySelector());
        panel.add(createButton("Exchange", "exchange"));
        return panel;
    }

    private Component createButton(String buttonName, String commandName){
        JButton button = new JButton(buttonName);
        button.addActionListener(e -> {
            commands.get(commandName).execute();
        });
        return button;
    }

    //TODO eliminar esta linea
    private Component weekAgoButton() {
        JButton button = new JButton("week");
        button.addActionListener(e -> {
            commands.get("week").execute();
        });
        return button;
    }


    private JTextField amountInput() {
        return new JTextField(10);
    }

    private JTextField amountOutput() {
        JTextField textField = new JTextField(10);
        textField.setEditable(false);
        return textField;
    }

    private JComboBox<Currency> currencySelector() {
        return new JComboBox<>(currencies);
    }

    public void addCommand(String name, Command command) {
        this.commands.put(name, command);
    }

    public MoneyDialog moneyDialog() {
        return () -> new Money(inputAmount(), inputCurrency());
    }


    public CurrencyDialog currencyDialog() {
        return this::outputCurrency;
    }

    public CurrencyDialog inputCurrencyDialog() {
        return this::inputCurrency;
    }

    public MoneyDisplay moneyDisplay() {
        return money -> outputAmount.setText(money.amount() + "");
    }

    private double inputAmount() {
        return toDouble(inputAmount.getText());
    }

    private double toDouble(String text) {
        return Double.parseDouble(text);
    }

    private Currency inputCurrency() {
        return (Currency) inputCurrency.getSelectedItem();
    }

    private Currency outputCurrency() {
        return (Currency) outputCurrency.getSelectedItem();
    }

    public ChartDisplay chartDisplay() {
        return chart -> {
            JFreeChart lineChart = getXyLineChart(chart);
            chartPanel.setChart(decorate(lineChart, chart));
            chartPanel.repaint();
            chartPanel.revalidate();
        };
    }

    private JFreeChart decorate(JFreeChart lineChart, Chart chart) {
        decorate((XYPlot) lineChart.getPlot(), chart);
        return lineChart;
    }

    private void decorate(XYPlot plot, Chart chart) {
        plot.setBackgroundPaint(null);
        plot.addRangeMarker(getMarker(chart));
        paintLine(plot.getRenderer(), chart);
    }

    private static ValueMarker getMarker(Chart chart) {
        ValueMarker marker = new ValueMarker(chart.PointsList().getFirst().rate());
        marker.setStroke(new BasicStroke(
                1.5f,                   // Ancho (width)
                BasicStroke.CAP_BUTT,   // Estilo de tope de línea
                BasicStroke.JOIN_MITER, // Estilo de unión de línea
                10.0f,                  // Miterlimit
                new float[]{10.0f, 6.0f},            // El patrón discontinuo (dash array)
                0.0f                    // Fase inicial (dash phase)
        ));
        return marker;
    }

    private void paintLine(XYItemRenderer renderer,Chart chart) {
        renderer.setSeriesPaint(0, isRising(chart) ?   new Color(129, 210, 149) : new Color(242, 139, 130));
        renderer.setSeriesStroke(0, new BasicStroke(2.0f));
    }

    private static boolean isRising(Chart chart) {
        return chart.PointsList().getFirst().rate() < chart.PointsList().getLast().rate();
    }

    private static JFreeChart getXyLineChart(Chart chart) {
        return ChartFactory.createTimeSeriesChart(chart.title(), chart.x(), chart.legend(), dataOf(chart));
    }

    private static TimeSeriesCollection dataOf(Chart chart) {
        TimeSeries timeSeries = new TimeSeries("");

        chart.Points().forEach(point -> {
            timeSeries.add(getDay(point), point.rate());
        });
        return new TimeSeriesCollection(timeSeries);
    }

    private static Day getDay(Point point) {
        return new Day(
                point.date().getDayOfMonth(),
                point.date().getMonthValue(),
                point.date().getYear()
        );
    }
}
