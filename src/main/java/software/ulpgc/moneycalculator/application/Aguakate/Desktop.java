package software.ulpgc.moneycalculator.application.Aguakate;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import software.ulpgc.moneycalculator.architecture.control.Command;
import software.ulpgc.moneycalculator.architecture.io.ChartBuilder;
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
        panel.add(weekAgoButton());
        return panel;
    }

    private JPanel panel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(CENTER));
        panel.add(inputAmount = amountInput());
        panel.add(inputCurrency = currencySelector());
        panel.add(outputAmount = amountOutput());
        panel.add(outputCurrency = currencySelector());
        panel.add(calculateButton());
        return panel;
    }

    private Component weekAgoButton() {
        JButton button = new JButton("week ago");
        button.addActionListener(e -> {
            commands.get("weekago").execute();
        });
        return button;
    }

    private Component calculateButton() {
        JButton button = new JButton("Exchange");
        button.addActionListener(e -> commands.get("exchange").execute());
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

            chartPanel.setChart(decorate(lineChart));
            chartPanel.repaint();
            chartPanel.revalidate();
        };
    }

    private JFreeChart decorate(JFreeChart lineChart) {
        return lineChart;
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
