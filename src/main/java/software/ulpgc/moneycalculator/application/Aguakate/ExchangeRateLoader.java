package software.ulpgc.moneycalculator.application.Aguakate;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import software.ulpgc.moneycalculator.architecture.model.Currency;
import software.ulpgc.moneycalculator.architecture.model.ExchangeRate;

import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Stream;

public class ExchangeRateLoader implements software.ulpgc.moneycalculator.architecture.io.ExchangeRateLoader, software.ulpgc.moneycalculator.architecture.io.ExchangeRateLoaderHistoric {

    private static String api = "https://api.frankfurter.dev/v1/";
    private Currency base;
    private Currency symbol;

    @Override
    public ExchangeRate load(Currency from, Currency to) {
        this.base = from;
        this.symbol = to;
        try {
            return loadFrom(new URL( api + "latest?base=" + from.code() + "&symbols=" + to.code()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ExchangeRate loadFrom(URL url) throws IOException {
        try (InputStream inputStream = url.openConnection().getInputStream()) {
            return loadFrom(new String(new BufferedInputStream(inputStream).readAllBytes()));
        }
    }

    private ExchangeRate loadFrom(String s) {
        JsonObject jsonObject = new Gson().fromJson(s, JsonObject.class);
        return new ExchangeRate(getDate(jsonObject), base, symbol, getRate(jsonObject));
    }

    private LocalDate getDate(JsonObject jsonObject) {
        return toLocalDate(jsonObject.get("date").getAsString());
    }

    private static LocalDate toLocalDate(String date) {
        return LocalDate.parse(date);
    }

    private double getRate(JsonObject jsonObject) {
        return new Gson().fromJson(jsonObject.get("rates"), JsonObject.class).get(symbol.code()).getAsDouble();
    }

    @Override
    public Stream<ExchangeRate> load(Currency base, Currency symbol, LocalDate from, LocalDate to) {
        this.base = base;
        this.symbol = symbol;
        try {
            return loadHistoricFrom(new URL(api +  from + ".." + to + "?base=" + base.code() + "&symbols=" + symbol.code() ));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Stream<ExchangeRate> loadHistoricFrom(URL url) throws IOException {
        try (InputStream inputStream = url.openConnection().getInputStream()) {
            return loadHistoricFrom(new String(new BufferedInputStream(inputStream).readAllBytes()));
        }
    }

    private Stream<ExchangeRate> loadHistoricFrom(String jsonString) {
        JsonObject jsonObject = new Gson().fromJson(jsonString, JsonObject.class);
        JsonObject rates = new Gson().fromJson(jsonObject.get("rates"), JsonObject.class);
        return loadHistoricFrom(rates);
    }

    private Stream<ExchangeRate> loadHistoricFrom(JsonObject rates) {
        return rates.entrySet().stream().map(this::toExchangeRate);
    }

    private ExchangeRate toExchangeRate(Map.Entry<String, JsonElement> entry) {
        return new ExchangeRate(toLocalDate(entry.getKey()), symbol, base, entry.getValue().getAsJsonObject().get(symbol.code()).getAsDouble());
    }
}
