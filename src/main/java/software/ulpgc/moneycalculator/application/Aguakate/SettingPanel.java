package software.ulpgc.moneycalculator.application.Aguakate;

import software.ulpgc.moneycalculator.architecture.io.SettingsApplier;
import software.ulpgc.moneycalculator.architecture.io.SettingsGetter;

import javax.swing.*;
import java.awt.*;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.SOUTH;

public class SettingPanel extends JDialog {

    private final SettingsApplier settingsApplier;
    private final JToggleButton horizontalAxis;
    private final JToggleButton verticalAxis;
    private final Color ACTIVE_COLOR = new Color(255, 255, 255);
    private final Color INACTIVE_COLOR = new Color(108, 108, 108);
    private JButton applyButton;

    public SettingPanel(SettingsApplier settingsApplier, SettingsGetter settingsGetter, Desktop desktop) {
        super(desktop, "Setting Panel", true);
        this.settingsApplier = settingsApplier;
        this.horizontalAxis = createToggleSwitch("Axis Horizontal");
        this.verticalAxis = createToggleSwitch("Axis Vertical");
        this.getContentPane().setBackground(Color.BLACK);
        this.getContentPane().add(axisPanel(), CENTER);
        this.getContentPane().add(ApplyPanel(), SOUTH);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSettings(settingsGetter.get());
        this.setResizable(false);
        this.setSize(500, 500);
    }

    private void setSettings(Integer[] integers) {
        this.horizontalAxis.setSelected(integers[0] == 1);
        this.verticalAxis.setSelected(integers[1] == 1);
    }

    private Component ApplyPanel() {
        JPanel jPanel = new JPanel();
        jPanel.setBackground(null);
        applyButton = createButton("Apply");
        jPanel.add(applyButton);
        return jPanel;
    }

    private JButton createButton(String buttonName) {
        JButton button = new JButton(buttonName);
        button.setBackground(null);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setForeground(INACTIVE_COLOR);
        button.addActionListener(e -> {
            ApplyChanges();
            button.setForeground(INACTIVE_COLOR);
        });
        return button;
    }

    private void ApplyChanges() {
        this.settingsApplier.apply(getNewSettings());
    }

    private Integer[] getNewSettings() {
        return new Integer[]{
                horizontalAxis.isSelected() ? 1 : 0,
                verticalAxis.isSelected() ? 1 : 0
        };
    }

    private JPanel axisPanel() {
        JPanel jPanel = new JPanel();
        jPanel.setBackground(Color.BLACK);
        jPanel.add(this.verticalAxis);
        jPanel.add(this.horizontalAxis);
        return jPanel;
    }

    private JToggleButton createToggleSwitch(String label) {
        JToggleButton toggle = new JToggleButton(label);

        toggle.putClientProperty("JButton.buttonType", "switch");
        toggle.putClientProperty("JToggleButton.switch.showSymbol", true);

        toggle.setOpaque(false);
        toggle.setFocusPainted(false);
        toggle.addActionListener(e -> applyButton.setForeground(ACTIVE_COLOR));

        return toggle;
    }

}
