package software.ulpgc.moneycalculator.application.Aguakate;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import software.ulpgc.moneycalculator.architecture.control.Command;
import software.ulpgc.moneycalculator.architecture.io.SettingsApplier;
import software.ulpgc.moneycalculator.architecture.io.SettingsGetter;
import software.ulpgc.moneycalculator.architecture.model.Chart;
import software.ulpgc.moneycalculator.architecture.model.Currency;
import software.ulpgc.moneycalculator.architecture.model.Money;
import software.ulpgc.moneycalculator.architecture.model.Point;
import software.ulpgc.moneycalculator.architecture.ui.*;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Stream;

import static java.awt.BorderLayout.*;
import static java.awt.FlowLayout.CENTER;

public class Desktop extends JFrame {
    private final Map<String, Command> commands;
    private final Currency[] currencies;
    private JTextField inputAmount;
    private JComboBox<Currency> inputCurrency;
    private JTextField outputAmount;
    private JComboBox<Currency> outputCurrency;
    private ChartPanel chartPanel;

    private final List<JButton> periodButtons;
    private final Color ACTIVE_COLOR = new Color(255, 255, 255);
    private final Color INACTIVE_COLOR = new Color(108, 108, 108);
    private Integer[] axisStatus;

    public Desktop(Stream<Currency> currencies) throws HeadlessException {
        this.commands = new HashMap<>();
        this.periodButtons = new ArrayList<>();
        apply(new Integer[]{0, 0});
        this.currencies = currencies.toArray(Currency[]::new);
        this.setTitle("Money Calculator");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 600);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setLayout(new BorderLayout());
        this.getContentPane().setBackground(Color.BLACK);
        this.getContentPane().add(northPanel(), NORTH);
        this.getContentPane().add(southPanel(), SOUTH);
        this.chartPanel = chartPanel();
        this.getContentPane().add(chartPanel, BorderLayout.CENTER);
    }

    private void deactivateAllPeriodButtons(List<JButton> jButtonList) {
        jButtonList.forEach(this::unSelected);
    }

    private void unSelected(JButton button) {
        button.setForeground(INACTIVE_COLOR);
        button.setSelected(false);
    }

    public ChartPanel chartPanel() {
        ChartPanel panel = new ChartPanel(null);
        panel.setBackground(null);
        return panel;
    }


    private JPanel southPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(null);
        panel.setLayout(new FlowLayout());
        periodButtons.add(createButton("Week", "week", periodButtons));
        periodButtons.add(createButton("Month", "month", periodButtons));
        periodButtons.add(createButton("6 Month", "6month", periodButtons));
        periodButtons.add(createButton("Year", "year", periodButtons));
        periodButtons.add(createButton("5 Year", "5year", periodButtons));
        periodButtons.add(createButton("Max", "max", periodButtons));
        periodButtons.forEach(panel::add);
        return panel;
    }

    private JPanel northPanel() {
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.setBackground(null);
        JPanel panel = new JPanel();
        panel.setBackground(null);
        panel.setLayout(new FlowLayout(CENTER));
        panel.add(inputAmount = amountInput());
        panel.add(inputCurrency = currencySelector());
        panel.add(outputAmount = amountOutput());
        panel.add(outputCurrency = currencySelector());
        panel.add(createButton("Exchange", "exchange"));
        northPanel.add(panel, BorderLayout.CENTER);
        northPanel.add(settingPanel(), BorderLayout.EAST);
        return northPanel;
    }

    private Component settingPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(null);
        JButton setting = createButton("\u2699", "setting");
        setting.setFont(new Font("Segoe UI Symbol", Font.BOLD, 25));
        panel.add(setting);
        return panel;
    }

    private JButton createButton(String buttonName, String commandName, List<JButton> buttonList) {
        JButton button = new JButton(buttonName);
        button.setBackground(null);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setForeground(INACTIVE_COLOR);
        button.addActionListener(e -> {
            commands.get(commandName).execute();
            deactivateAllPeriodButtons(buttonList);
            button.setForeground(ACTIVE_COLOR);
        });
        return button;
    }


    private JButton createButton(String buttonName, String commandName) {
        JButton button = new JButton(buttonName);
        button.setBackground(null);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setForeground(INACTIVE_COLOR);
        button.addActionListener(e -> {
            commands.get(commandName).execute();
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
        lineChart.setBackgroundPaint(null);
        decorate((XYPlot) lineChart.getPlot(), chart);
        return lineChart;
    }

    private void decorate(XYPlot plot, Chart chart) {
        plot.setBackgroundPaint(null);
        plot.setRangeGridlinePaint(new Color(255, 255, 255, axisStatus[0]*255));
        plot.setDomainGridlinePaint(new Color(255, 255, 255,axisStatus[1]*255));
        plot.addRangeMarker(getMarker(chart));
        paintLine(plot.getRenderer(), chart);
    }

    private int drawAxis(JButton jButton) {
        int i = jButton.isSelected() ? 255 : 0;
        System.out.println("drawAxis = " + i);

        return i;
    }

    private static ValueMarker getMarker(Chart chart) {
        ValueMarker marker = new ValueMarker(chart.PointsList().getFirst().rate());
        marker.setStroke(new BasicStroke(
                1.5f,
                BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER,
                10.0f,
                new float[]{10.0f, 6.0f},
                0.0f
        ));
        return marker;
    }

    private void paintLine(XYItemRenderer renderer, Chart chart) {
        renderer.setSeriesPaint(0, isRising(chart) ? new Color(129, 210, 149) : new Color(242, 139, 130));
        renderer.setSeriesStroke(0, new BasicStroke(2.0f));
    }

    private static boolean isRising(Chart chart) {
        return chart.PointsList().getFirst().rate() < chart.PointsList().getLast().rate();
    }

    private static JFreeChart getXyLineChart(Chart chart) {
        return ChartFactory.createTimeSeriesChart(chart.title(), chart.x(), chart.legend(), dataOf(chart), false, true, false);
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

    public Integer[] axisStatus() {
        return axisStatus;
    }

    public SettingsApplier settingsApplier() {
        return this::apply;
    }

    private void apply(Integer[] state) {
        this.axisStatus = state;
    }

    public SettingsGetter settingsGetter() {
        return this::axisStatus;
    }
}
