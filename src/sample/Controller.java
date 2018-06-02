package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import javax.xml.soap.Text;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    private List<Country> countries;

    @FXML // fx:id="list";
    ListView<String> list;
    @FXML // fx:id="city_cnt";
            Label city_cnt;

    @FXML // fx:id="line_chart";
    LineChart line_chart;

    private String filter = "";

    public Controller(){
        countries = Country.getCountries();
    }


    public void loadList(){
        ObservableList<String> bridge_list = FXCollections.observableArrayList();
        for(Country c: countries){

            if (!this.filter.equals("")){
                if(!c.getName().toLowerCase().contains(this.filter.toLowerCase())){
                    continue;
                }
            }
            bridge_list.add(c.getName());
        }
        list.setItems(bridge_list);

    }

    public void filter(Event e){
        TextField t = (TextField) e.getSource();
        this.filter = t.getText();
        this.loadList();

    }

    public void selected(){
        String selected = list.getSelectionModel().getSelectedItem();

        for(Country c: countries)
        {
            if (selected.equals(c.getName())){
                String txt = Integer.toString(c.getCities());
                this.drawChart(c.getCode());

                city_cnt.setText(txt);

                break;
            }
        }
    }

    public void drawChart(String city_code){
        ArrayList<Latest> latest_array = Latest.getLatest(city_code);

        // By the time we get to this point we have our data parsed and clean in an object
        // that can be accessed like obj.value and obj.lastUpdated

        //Here we will format the data to the way that the chart likes it
        String ylabel = "";
        String xlabel = "";
        XYChart.Series<String, Float> series = new XYChart.Series();
        for (Latest l:latest_array){
            series.getData().add(new XYChart.Data(l.lastUpdated.toString(), l.value));
            ylabel = l.parameter;
            xlabel = l.location;
        }

        line_chart.getData().clear();
        line_chart.getData().add(series);
        line_chart.getXAxis().setLabel("Time " + xlabel);
        line_chart.getYAxis().setLabel("Value " + ylabel);

    }
}
