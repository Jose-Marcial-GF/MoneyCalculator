package software.ulpgc.moneycalculator.application.Aguakate;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import software.ulpgc.moneycalculator.architecture.io.CurrencyLoader;
import software.ulpgc.moneycalculator.architecture.model.Currency;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.stream.Stream;

public class WebService implements CurrencyLoader {

    private static String api = "https://api.frankfurter.dev/v1/";



    @Override
    public Stream<Currency> loadAll() {
        try {
            return loadFrom(new URL(api+"currencies"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Stream<Currency> loadFrom(URL url) throws IOException {
        try (InputStream inputStream = url.openConnection().getInputStream()) {
            return loadFrom(new String(new BufferedInputStream(inputStream).readAllBytes()));
        }
    }

    private Stream<Currency> loadFrom(String json) {
        return getJsonObject(json).entrySet().stream().map(this::toCurrency);
    }

    private Currency toCurrency(Map.Entry<String, JsonElement> e) {
        return new Currency(e.getKey(), e.getValue().getAsString());
    }

    private JsonObject getJsonObject(String json) {
        return new Gson().fromJson(json, JsonObject.class);
    }
}
