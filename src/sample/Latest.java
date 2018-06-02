package sample;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Latest {
    private static final String url_base = "https://api.openaq.org/v1/latest";

    public float value;
    public String parameter;
    public Date lastUpdated;
    public String location;

    public Latest(String param, float value, Date lastUpdated, String city){
        this.value = value;
        this.parameter = param;
        this.lastUpdated = lastUpdated;
        this.location = city;
    }

    public static ArrayList<Latest> getLatest(String country_code) {
        String url = url_base + "?country=" + country_code;
        String api_data = readFromAPI(url);

        Gson gson = new Gson();
        JsonObject parsed = gson.fromJson(api_data, JsonObject.class);
        JsonArray items = parsed.getAsJsonArray("results");

        ArrayList<Latest> return_value = new ArrayList<Latest>();
        String first_city = "";
        String first_parameter = "";

        for (JsonElement p : items){
            JsonObject item = p.getAsJsonObject();
            String current_city = item.get("city").getAsString();
            if (first_city.equals("")){
                first_city = current_city;
            }

            if (!current_city.equals(first_city)){
                continue;
            }

            JsonArray measurements = item.getAsJsonArray("measurements");

            for (JsonElement m: measurements){
                JsonObject measurement = m.getAsJsonObject();

                String current_parameter = measurement.get("parameter").getAsString();
                if (first_parameter.equals("")){
                    first_parameter = current_parameter;
                }

                if (!current_parameter.equals(first_parameter)){
                    continue;
                }

                float value = measurement.get("value").getAsFloat();
                String lastupdated = measurement.get("lastUpdated").getAsString();
                DateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'.000Z'");
                try{
                    Date date = f.parse(lastupdated);
                    Latest l = new Latest(current_parameter, value, date, current_city);
                    return_value.add(l);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }

        }
        return return_value;

    }

    public static String readFromAPI(String endpoint){
        String contents = "";
        try{
            URL address = new URL(endpoint);
            InputStreamReader reader = new InputStreamReader(address.openStream());
            BufferedReader buffer = new BufferedReader(reader);

            String line;
            while((line = buffer.readLine()) != null){
                if (line.isEmpty()) {
                    break;
                }

                contents += line;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return contents;
    }

}
