/*
 * APIUserGenerator
 *
 * Ivan Zherdev, 2019
 */
package tech.zherdev.usertablesgenerator;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Класс APIUserGenerator предназначен для получения пользователей из API.
 * Производный от абстрактного класса AUserGenerator.
 *
 * @author Ivan Zherdev
 */
public class APIUserGenerator extends AUserGenerator {
    /* Используются библиотеки Gson и Apache HttpClient */

	private static final String url = "http://randomuser.ru/api.json";        /* API */
    private static final String country = "Россия";                           /* API генерит пользователей из России*/

    private static HttpClient client = HttpClientBuilder.create().build();

    private static Gson gson = new GsonBuilder()
            .registerTypeAdapter(User.class, new CustomDeserializer())
            .create();

    /**
     * Метод getResponseFromAPI(String url) возвращает ответ API на get запрос
     *
     * @param url адрес для get запроса
     * @return ответ API
     * @throws IOException в случае ошибки при обращении к API
     */
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

    /**
     * Метод responseBodyToString(...) извлекает тело ответа API
     *
     * @param response ответ API
     * @return строка с телом ответа
     * @throws IOException в случае ошибки при обработке ответа API
     */
    private String responseBodyToString(HttpResponse response)
            throws  IOException {
        try {
            return EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (IOException e) {
            String message = e.getMessage() +
                    " - Ошибка при обработке ответа API " + url;
            throw new IOException(message);
        }
    }

    /**
     * Метод generateUser() генерирует пользователя из данных, полученных от API
     *
     * @return пользователь
     * @throws IOException в случае ошибки при обращении к API
     */
    public User generateUser() throws IOException {
        String response = responseBodyToString(getResponseFromAPI(url));

        JsonElement jUser = new JsonParser()
                .parse(response.substring(1, response.length() - 1));   /* Избавляемся от внешних скобок [ ] массива */

        User user = gson.fromJson(jUser, User.class);

        user.setZip(random.nextInt(maxMailIndex - minMailIndex) + minMailIndex);
        user.setAppart(random.nextInt(maxAppartNum) + 1);
        user.setCountry(country);
        user.setInn(innGenerator.generateINN());

        return user;
    }

}
