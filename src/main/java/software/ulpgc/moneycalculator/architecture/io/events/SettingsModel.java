package software.ulpgc.moneycalculator.architecture.io.events;

public class SettingsModel {

    // --- ESTADO CENTRAL DE LA APLICACIÓN ---
    private boolean verticalAxisVisible = true;
    private boolean horizontalAxisVisible = true;

    // Lista segura para hilos: Ideal para el patrón Observer.
    private final List<SettingsChangeListener> listeners = new CopyOnWriteArrayList<>();

    // --- MÉTODOS DEL OBSERVER (SUJETO) ---

    public void addListener(SettingsChangeListener listener) {
        listeners.add(listener);
    }

    private void notifyListeners() {
        // 1. CREAR el evento inmutable con el estado actual
        SettingsEvent event = new SettingsEvent(verticalAxisVisible, horizontalAxisVisible);

        // 2. Notificar a todos los oyentes con el evento inmutable
        for (SettingsChangeListener listener : listeners) {
            listener.settingsChanged(event);
        }
    }

    // --- GETTERS (Lectura para inicialización) ---

    public boolean isVerticalAxisVisible() { return verticalAxisVisible; }
    public boolean isHorizontalAxisVisible() { return horizontalAxisVisible; }

    // --- SETTERS (Escritura y Notificación) ---

    public void setVerticalAxisVisible(boolean visible) {
        if (this.verticalAxisVisible != visible) {
            this.verticalAxisVisible = visible;
            notifyListeners();
        }
    }

    public void setHorizontalAxisVisible(boolean visible) {
        if (this.isHorizontalAxisVisible != visible) {
            this.isHorizontalAxisVisible = visible;
            notifyListeners();
        }
    }
}