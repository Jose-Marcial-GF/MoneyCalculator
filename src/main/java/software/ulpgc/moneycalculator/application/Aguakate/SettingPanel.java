package software.ulpgc.moneycalculator.application.Aguakate;

import org.jfree.chart.axis.AxisState;
import software.ulpgc.moneycalculator.architecture.io.SettingApplier;

import javax.swing.*;
import java.awt.*;

import static java.awt.BorderLayout.CENTER;

public class SettingPanel extends JFrame {

    private Boolean[] axisStates;

    public SettingPanel(SettingApplier settingsRecord) {
        settingsRecord.apply();
        this.setTitle("Setting Panel");
        this.getContentPane().setBackground(Color.BLACK);
        this.getContentPane().add(axisPanel(), CENTER);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setResizable(false);
        this.setSize(500, 500);
    }

    private JPanel axisPanel() {
        JPanel jPanel = new JPanel();
        jPanel.setBackground(Color.BLACK);
        jPanel.add(createToggleSwitch("Axis Vertical", axisStates[0]));
        jPanel.add(createToggleSwitch("Axis Horizontal", axisStates[1]));
        return jPanel;
    }

    private JToggleButton createToggleSwitch(String label, boolean status) {
        JToggleButton toggle = new JToggleButton(label);
        toggle.putClientProperty("JToggleButton.buttonType", "toggleButton");
        toggle.setSelected(status);
        return toggle;
    }

}
