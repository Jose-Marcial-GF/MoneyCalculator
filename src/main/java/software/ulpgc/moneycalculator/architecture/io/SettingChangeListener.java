package software.ulpgc.moneycalculator.architecture.io;

import software.ulpgc.moneycalculator.architecture.io.events.SettingsEvent;

public interface SettingChangeListener {
    void settingsChanged(SettingsEvent event);
}
