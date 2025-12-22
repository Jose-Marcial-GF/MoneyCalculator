package software.ulpgc.moneycalculator.application.Aguakate;


import software.ulpgc.moneycalculator.architecture.control.ExchangeMoneyCommand;
import software.ulpgc.moneycalculator.architecture.control.GetHistorycComand;

import javax.swing.*;
import com.formdev.flatlaf.*;
import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {
        FlatLaf.registerCustomDefaultsSource("com/formdev/flatlaf/extras");
        UIManager.put("FlatLaf.useModernUI", true);
        FlatDarkLaf.setup();

        Desktop desktop = new Desktop(new WebService().loadAll());
        desktop.addCommand("week", new GetHistorycComand(
                desktop.inputCurrencyDialog(),
                desktop.currencyDialog(),
                new ExchangeRateLoader(),
                desktop.chartDisplay(),
                getLastWeek()
        ));
        desktop.addCommand("month", new GetHistorycComand(
                desktop.inputCurrencyDialog(),
                desktop.currencyDialog(),
                new ExchangeRateLoader(),
                desktop.chartDisplay(),
                getLastMoth(1)
        ));
        desktop.addCommand("6month", new GetHistorycComand(
                desktop.inputCurrencyDialog(),
                desktop.currencyDialog(),
                new ExchangeRateLoader(),
                desktop.chartDisplay(),
                getLastMoth(6)
        ));
        desktop.addCommand("year", new GetHistorycComand(
                desktop.inputCurrencyDialog(),
                desktop.currencyDialog(),
                new ExchangeRateLoader(),
                desktop.chartDisplay(),
                getLastYear(1)
        ));
        desktop.addCommand("5year", new GetHistorycComand(
                desktop.inputCurrencyDialog(),
                desktop.currencyDialog(),
                new ExchangeRateLoader(),
                desktop.chartDisplay(),
                getLastYear(5)
        ));
        desktop.addCommand("max", new GetHistorycComand(
                desktop.inputCurrencyDialog(),
                desktop.currencyDialog(),
                new ExchangeRateLoader(),
                desktop.chartDisplay(),
                getLastYear(200)
        ));
        desktop.addCommand("exchange", new ExchangeMoneyCommand(
                desktop.moneyDialog(),
                desktop.currencyDialog(),
                new ExchangeRateLoader(),
                desktop.moneyDisplay()
        ));


        desktop.addCommand("setting",
        () -> new SettingPanel(desktop.settingsApplier(), desktop.settingsGetter(), desktop).setVisible(true));
        desktop.setVisible(true);

    }

    private static LocalDate getLastYear(int yearsAgo) {
        return LocalDate.now().minusYears(yearsAgo);
    }

    private static LocalDate getLastMoth(int mothsAgo) {
        return LocalDate.now().minusMonths(mothsAgo);
    }


    private static LocalDate getLastWeek() {
        return LocalDate.now().minusWeeks(1);
    }
}
