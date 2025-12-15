package software.ulpgc.moneycalculator.architecture.model;

public record Currency(String code, String country) {
    @Override
    public String toString() {
        return code;
    }
}
