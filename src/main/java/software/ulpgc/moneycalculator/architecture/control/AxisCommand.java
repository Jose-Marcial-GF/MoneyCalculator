package software.ulpgc.moneycalculator.architecture.control;

import software.ulpgc.moneycalculator.architecture.ui.AxisDisplay;

public class AxisCommand implements Command {
    private final AxisDisplay axis;

    public AxisCommand(AxisDisplay axis) {
        this.axis = axis;
    }

    @Override
    public void execute() {
        axis.show();
    }
}
