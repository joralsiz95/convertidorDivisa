import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Cliente {
    public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String apiUrl = "https://v6.exchangerate-api.com/v6/d3b31b9960a0cd5570d878d2/latest/USD";
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                Gson gson = new Gson();
                JsonObject jsonResponse = gson.fromJson(response.toString(), JsonObject.class);

                JsonObject rates = jsonResponse.getAsJsonObject("conversion_rates");

                boolean exit = false;
                while (!exit) {
                    // Print menu options
                    System.out.println("\nMenu:");
                    System.out.println("1) Dólar -> Peso Argentino");
                    System.out.println("2) Peso Argentino -> Dólar");
                    System.out.println("3) Dólar -> Real Brasileño");
                    System.out.println("4) Real Brasileño -> Dólar");
                    System.out.println("5) Dólar -> Peso Colombiano");
                    System.out.println("6) Peso Colombiano -> Dólar");
                    System.out.println("7) Salir");
                    System.out.print("Seleccione una opción: ");

                    int option = Integer.parseInt(reader.readLine());

                    switch (option) {
                        case 1:
                            convertidor("USD", "ARS", rates);
                            break;
                        case 2:
                            convertidor("ARS", "USD", rates);
                            break;
                        case 3:
                            convertidor("USD", "BRL", rates);
                            break;
                        case 4:
                            convertidor("BRL", "USD", rates);
                            break;
                        case 5:
                            convertidor("USD", "COP", rates);
                            break;
                        case 6:
                            convertidor("COP", "USD", rates);
                            break;
                        case 7:
                            exit = true;
                            break;
                        default:
                            System.out.println("Opción no válida. Por favor, seleccione nuevamente.");
                    }
                }
            } else {
                System.out.println("GET request failed. Response code: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void convertidor(String from, String to, JsonObject rates) {
        double rate = rates.get(to).getAsDouble() / rates.get(from).getAsDouble();
        System.out.println("Ingrese la cantidad de " + from + " a convertir a " + to + ": ");
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            double amount = Double.parseDouble(reader.readLine());
            double result = amount * rate;
            System.out.println(amount + " " + from + " es equivalente a " + result + " " + to);
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }
}