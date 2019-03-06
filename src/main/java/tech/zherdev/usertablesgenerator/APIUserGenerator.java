package tech.zherdev.usertablesgenerator;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class APIUserGenerator extends AUserGenerator {

    private static final String country = "Russia";
    private static final String url = "http://randomuser.ru/api.json";

    private static HttpClient client = HttpClientBuilder.create().build();

    private static Gson gson = new GsonBuilder()
            .registerTypeAdapter(User.class, new CustomDeserializer())
            .create();

    private HttpResponse getResponseFromAPI(String url) throws IOException {
        HttpResponse response;
        HttpGet request = new HttpGet(url);

        // Выполняем запрос
        try {
           response = client.execute(request);
            // Получение кода состояния
            int responseCode = response.getStatusLine().getStatusCode();
            if (responseCode < 200 || responseCode > 299) {
                throw new IOException("Неудача. Код состояния " + responseCode);
            }
        } catch (IOException e) {
            String message = e.getMessage() + " - Ошибка обращении к API " +
                    url + ", проверьте соединение с Интернетом.";
            throw new IOException(message);
        }

        return response;
    }

    private String responseBodyToString(HttpResponse response)
            throws  IOException {
        StringBuffer result;

        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));
            String line = "";
            result = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
        } catch (IOException e) {
            String message = e.getMessage() + " - Ошибка при обработке ответа API " +
                    url + ", проверьте соединение с Интернетом.";
            throw new IOException(message);
        }
        return result.toString();
    }

    public User generateUser() throws IOException {
        String response = responseBodyToString(getResponseFromAPI(url));

        User user = gson.fromJson(response, User.class);

        user.setAppart(random.nextInt(maxAppartNum) + 1);
        user.setCountry(country);
        user.setInn(innGenerator.generateINN());

        return user;
    }

}
