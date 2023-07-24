import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Scanner;

public class WeatherApp {
    private static final String API_KEY = "b6907d289e10d714a6e88b30761fae22";
    private static final String API_BASE_URL = "https://samples.openweathermap.org/data/2.5/forecast/hourly?q=London,us&appid=b6907d289e10d714a6e88b30761fae22";

    private static HashMap<String, HashMap<String, Double>> weatherData = new HashMap<>();

    private static void fetchWeatherData(String date) {
        try {
            String apiUrl = API_BASE_URL + "?q=" + date + "&appid=" + API_KEY;
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    response.append(line);
                }
                bufferedReader.close();

                // Parse the API response and extract the relevant weather data
                HashMap<String, Double> data = parseWeatherData(response.toString());
                weatherData.put(date, data);
            } else {
                System.out.println("Failed to fetch weather data. Error code: " + connection.getResponseCode());
            }
            connection.disconnect();
        } catch (IOException e) {
            System.out.println("Error occurred while fetching weather data: " + e.getMessage());
        }
    }

    private static HashMap<String, Double> parseWeatherData(String response) {
        // Implement your JSON parsing logic here to extract temperature, wind speed, and pressure
        // from the API response and return them as a HashMap
        // For simplicity, let's assume the weather data is returned in the following format:
        HashMap<String, Double> data = new HashMap<>();
        data.put("temp", 25.0);
        data.put("wind_speed", 10.0);
        data.put("pressure", 1015.0);
        return data;
    }

    private static int getUserInput() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("1. Get weather");
        System.out.println("2. Get Wind Speed");
        System.out.println("3. Get Pressure");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
        return scanner.nextInt();
    }

    private static String getDateInput() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the date (YYYY-MM-DD): ");
        return scanner.nextLine();
    }

    private static void printWeatherData(String date, int dataType) {
        if (!weatherData.containsKey(date)) {
            fetchWeatherData(date);
        }

        HashMap<String, Double> data = weatherData.get(date);
        if (data == null || data.isEmpty()) {
            System.out.println("Weather data not available for the input date.");
            return;
        }

        switch (dataType) {
            case 1:
                System.out.println("Temperature on " + date + ": " + data.getOrDefault("temp", -1.0) + " °C");
                break;
            case 2:
                System.out.println("Wind Speed on " + date + ": " + data.getOrDefault("wind_speed", -1.0) + " m/s");
                break;
            case 3:
                System.out.println("Pressure on " + date + ": " + data.getOrDefault("pressure", -1.0) + " hPa");
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    public static void main(String[] args) {
        while (true) {
            int choice = getUserInput();

            if (choice == 1) {
                String date = getDateInput();
                printWeatherData(date, 1);
            } else if (choice == 2) {
                String date = getDateInput();
                printWeatherData(date, 2);
            } else if (choice == 3) {
                String date = getDateInput();
                printWeatherData(date, 3);
            } else if (choice == 0) {
                System.out.println("Exiting the program.");
                break;
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
