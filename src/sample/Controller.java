package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import javax.xml.soap.Text;
import java.util.List;

public class Controller {
    List<Country> countries;

    @FXML // fx:id="list";
    ListView<String> list;
    @FXML // fx:id="city_cnt";
            Label city_cnt;

    String filter = "";

    public Controller(){
        countries = Country.getCountries();
    }


    public void loadList(){
        ObservableList<String> bridge_list = FXCollections.observableArrayList();
        for(Country c: countries){

            if (this.filter!=""){
                if(c.getName().toLowerCase().indexOf(this.filter.toLowerCase())<0){
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
            if (selected == c.getName()){
                String txt = Integer.toString(c.getCities());
                city_cnt.setText(txt);
            }
        }
    }
}
