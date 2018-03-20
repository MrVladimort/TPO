package zad1;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import javax.swing.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

class GUI extends JFrame {
    private JTextField inputCountry = new JTextField("", 1), inputCity = new JTextField("", 2), inputRate = new JTextField("", 3);
    private String rateNBPS, rateForS, weather;
    private Service service;

    GUI() {
        super("Simple Example");
        this.setBounds(500, 500, 500, 500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container container = this.getContentPane();
        container.setLayout(new GridLayout(5, 0, 0, 0));

        JLabel labelCountry = new JLabel("Country: ");
        container.add(labelCountry);
        container.add(inputCountry);

        JLabel labelCity = new JLabel("City: ");
        container.add(labelCity);
        container.add(inputCity);

        JLabel labelRate = new JLabel("Rate: ");
        container.add(labelRate);
        container.add(inputRate);


        JButton button = new JButton("Go !");
        button.addActionListener(new ButtonEventListener());
        container.add(button);
    }

    private void startService() {
        service = new Service(inputCountry.getText());
        String weatherJson = service.getWeather(inputCity.getText());
        getWeatherData(weatherJson);

        try {
            rateForS = "" + service.getRateFor(inputRate.getText());
            rateNBPS = "" + service.getNBPRate();
        } catch (IOException e) {
            e.printStackTrace();
        }

        startWiki(service.city);
    }

    private void getWeatherData(String weatherJson) {
        JSONObject obj, objTemp;
        JSONArray arrayWeather;
        String weatherDescription = "";
        double weatherTemperature = 0.0;

        try {
            obj = new JSONObject(weatherJson);
            objTemp = obj.getJSONObject("main");
            arrayWeather = obj.getJSONArray("weather");
            weatherTemperature = objTemp.getDouble("temp");

            for (int i = 0; i < arrayWeather.length(); i++)
                weatherDescription = arrayWeather.getJSONObject(i).getString("description");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        weather = weatherDescription + ", " + weatherTemperature;
    }

    private void startWiki(String setCity) {
        JFXPanel panel = new JFXPanel();
        JFrame frame = new JFrame();
        Platform.runLater(() -> {
            WebView myWebView = new WebView();
            WebEngine engine = myWebView.getEngine();
            engine.load("https://uk.wikipedia.org/wiki/" + setCity);

            VBox root = new VBox();
            root.getChildren().addAll(myWebView);

            Scene scene = new Scene(root, 800, 500);

            panel.setScene(scene);
            frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
            SwingUtilities.invokeLater(() -> {
                frame.add(panel);
                frame.pack();
                frame.setVisible(true);
            });
        });
    }

    class ButtonEventListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            startService();
            String message = "";
            message += "Hi, it'service " + service.city + "\n";
            message += "Weather: " + weather + "\n";
            message += "Get Rate For " + service.currencyCountry + " to " + service.currency + ": " + rateForS + "\n";
            message += "Get PLN rate to " + service.currencyCountry + ": " + rateNBPS + "\n";

            JOptionPane.showMessageDialog(null,
                    message,
                    "Output",
                    JOptionPane.PLAIN_MESSAGE);
        }
    }
}