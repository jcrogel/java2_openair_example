package sample;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Country {
    private String name;
    private String code;
    private int cities;

    public Country(String _name, String _code, int _cities){
        this.name = _name;
        this.code = _code;
        this.cities = _cities;
    }

    public String getName(){
        return name;
    }

    public String getCode(){
        return code;
    }

    public int getCities(){
        return cities;
    }

    public static String readFromAPI(){
        String contents = "";
        try{
            URL address = new URL("https://api.openaq.org/v1/countries");
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

    public static List<Country> getCountries(){
        String data = readFromAPI();

        Gson gson = new Gson();
        JsonObject parsed = gson.fromJson(data, JsonObject.class);
        JsonArray countries = parsed.getAsJsonArray("results");

        ArrayList<Country> result = new ArrayList<Country>();

        for(JsonElement i:countries){

            JsonObject item = i.getAsJsonObject();
            String name = item.getAsJsonPrimitive("name").getAsString();
            String code = item.getAsJsonPrimitive("code").getAsString();
            int count = item.getAsJsonPrimitive("cities").getAsInt();

            Country obj = new Country(name, code, count);
            result.add(obj);
        }
        return result;
    }

}
