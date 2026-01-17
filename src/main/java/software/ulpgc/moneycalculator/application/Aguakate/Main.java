package software.ulpgc.moneycalculator.application.Aguakate;


import javax.swing.*;
import com.formdev.flatlaf.*;

public class Main {

    public static void main(String[] args) {
        FlatLaf.registerCustomDefaultsSource("com/formdev/flatlaf/extras");
        UIManager.put("FlatLaf.useModernUI", true);
        FlatDarkLaf.setup();

        new Desktop(new WebService().loadAll()).setVisible(true);

    }
}
