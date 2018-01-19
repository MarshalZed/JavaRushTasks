package task2810;

import task2810.model.Model;
import task2810.model.Provider;
import task2810.vo.Vacancy;

import java.util.ArrayList;
import java.util.Arrays;

public class Controller {
    private Model model;

    public Controller(Model model) {
        if (model == null) throw new IllegalArgumentException();
        this.model = model;
    }

    public void onCitySelect(String cityName) {
        model.selectCity(cityName);
    }
}
