/**
 * @author Hravchenko Vladyslav S15567
 */

package zad1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.URL;
import java.util.Currency;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class Service {
    String city, currencyCountry = "", currency = "";
    private String country;

    Service(String country) {
        this.country = country;
    }

    private String getContentFromUrl(URL content) throws IOException {
        StringBuilder sb = new StringBuilder();
        String wrt;
        BufferedReader in = new BufferedReader(new InputStreamReader(content.openStream()));

        while ((wrt = in.readLine()) != null) sb.append(wrt);
        in.close();

        return sb.toString();
    }

    public String getWeather(String city) throws IOException {
        this.city = city;
        String weatherResponse;

        weatherResponse = getContentFromUrl(new URL("http://api.openweathermap.org/data/2.5/weather?APPID=7574e4fff0f8ba7b1117fedad9967d67&q=" + city + "&units=metric"));

        return weatherResponse;
    }

    private String findCurrencyCountry() {
        Locale[] locale = Locale.getAvailableLocales();

        for (Locale l : locale)
            if (l.getDisplayCountry(Locale.ENGLISH).equals(country))
                return Currency.getInstance(l).getCurrencyCode();

        return "PLN";
    }

    public Double getNBPRate() throws IOException {
        Document docA = Jsoup.connect("http://www.nbp.pl/kursy/kursya.html").get();
        Document docB = Jsoup.connect("http://www.nbp.pl/kursy/kursyb.html").get();

        Elements elementsA = docA.getElementsMatchingText(currencyCountry);
        String currencyValueA = elementsA.get(elementsA.size() - 2).child(2).ownText().replace(',', '.');
        if (!currencyValueA.isEmpty()) return Double.parseDouble(currencyValueA);

        Elements elementsB = docB.getElementsMatchingText(currencyCountry);
        String currencyValueB = elementsB.get(elementsB.size() - 2).child(2).ownText().replace(',', '.');
        if (!currencyValueB.isEmpty()) return Double.parseDouble(currencyValueB);

        return 0.0;
    }

    public Double getRateFor(String currency) throws IOException, JSONException {
        String currencyResponse;
        currencyCountry = findCurrencyCountry();
        System.out.println("getRateFor: " + currencyCountry + " to: " + currency);
        JSONObject currencyJson, nestedJson;
        Double rate;

        currencyResponse = getContentFromUrl(new URL("http://api.fixer.io/latest?base=" + currencyCountry));

        currencyJson = new JSONObject(currencyResponse);
        nestedJson = currencyJson.getJSONObject("rates");
        rate = nestedJson.getDouble(currency);

        return rate;
    }
}
