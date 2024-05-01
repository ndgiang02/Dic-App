package Base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class TranslateAPI {

    public static void main(String[] args) throws IOException {
        // Văn bản cần dịch
        String text = "Văn bản cần dịch";
        System.out.println("Translated text: " + translate("", "en", text));
    }

    public static String translate(String langFrom, String langTo, String text) throws IOException {
        // URL của google app script
        String urlStr = "https://script.google.com/macros/s/AKfycbz4xJ9v8gx4PKk7-AA_bqJkTFhZdfjDnunrWXfMKpnW8t-CiQEynTDXsImOnxgRWXiX/exec" +
                "?q=" + URLEncoder.encode(text, "UTF-8") + "&target=" + langTo + "&source=" + langFrom;
        URL url = new URL(urlStr);

        //Lưu trữ phản hồi của server
        StringBuilder response = new StringBuilder();
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        //Giả mạo trình duyệt
        con.setRequestProperty("User-Agent", "Mozilla/5.0");

        //Input
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }

}