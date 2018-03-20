/**
 * @author Hravchenko Vladyslav S15567
 */

package zad1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class Service {
    String country, city, currencyCountry = "", currency = "", abbreviationCountry;

    Service(String country) {
        this.country = country;
    }

    public String getWeather(String city) {
        this.city = city;
        JSONObject weatherJson;
        JSONObject nestedJson;
        String weatherResponse = "";

        try {
            weatherResponse = getContentFromUrl(new URL("http://api.openweathermap.org/data/2.5/weather?APPID=7574e4fff0f8ba7b1117fedad9967d67&q=" + city + "&units=metric"));
            weatherJson = new JSONObject(weatherResponse);
            nestedJson = weatherJson.getJSONObject("sys");
            abbreviationCountry = nestedJson.getString("country");
        } catch (IOException | JSONException ex) {
            ex.printStackTrace();
        }

        return weatherResponse;
    }

    private String findCurrencyCountry(String abbreviationCountry) throws IOException {
        JSONObject currencyJson;
        String currency = "";

        try {
            String getCurrency = getContentFromUrl(new URL("http://country.io/currency.json"));
            currencyJson = new JSONObject(getCurrency);
            currency = currencyJson.getString(abbreviationCountry);
        } catch (MalformedURLException | JSONException e) {
            e.printStackTrace();
        }

        return currency;
    }

    public Double getNBPRate() {
        try {
            Document docA = Jsoup.connect("http://www.nbp.pl/kursy/kursya.html").get();
            Document docB = Jsoup.connect("http://www.nbp.pl/kursy/kursyb.html").get();

            Elements elementsA = docA.getElementsMatchingText(currencyCountry);
            String currencyValueA = elementsA.get(elementsA.size() - 2).child(2).ownText().replace(',', '.');
            if (!currencyValueA.isEmpty()) return Double.parseDouble(currencyValueA);

            Elements elementsB = docB.getElementsMatchingText(currencyCountry);
            String currencyValueB = elementsB.get(elementsB.size() - 2).child(2).ownText().replace(',', '.');
            if (!currencyValueB.isEmpty()) return Double.parseDouble(currencyValueB);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public Double getRateFor(String currency) throws IOException {
        String currencyResponse;
        this.currencyCountry = findCurrencyCountry(abbreviationCountry);
        System.out.println("getRateFor: " + currencyCountry + " to: " + currency);
        JSONObject currencyJson, nestedJson;
        Double rate = 0.0;

        try {
            currencyResponse = getContentFromUrl(new URL("http://api.fixer.io/latest?base=" + currencyCountry));

            currencyJson = new JSONObject(currencyResponse);
            nestedJson = currencyJson.getJSONObject("rates");
            rate = nestedJson.getDouble(currency);
        } catch (JSONException | MalformedURLException e) {
            e.printStackTrace();
        }

        return rate;
    }

    private String getContentFromUrl(URL content) throws IOException {
        StringBuilder sb = new StringBuilder();
        String wrt;
        BufferedReader in = new BufferedReader(new InputStreamReader(content.openStream()));

        while ((wrt = in.readLine()) != null) sb.append(wrt);
        in.close();

        return sb.toString();
    }
}  
