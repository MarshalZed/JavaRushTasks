package task2810;

import task2810.model.HHStrategy;
import task2810.model.Model;
import task2810.model.MoikrugStrategy;
import task2810.model.Provider;
import task2810.view.HtmlView;

public class Aggregator {
    public static void main(String[] args) {
        HtmlView view = new HtmlView();
        Model model = new Model(view, new Provider(new HHStrategy()), new Provider(new MoikrugStrategy()));
        Controller controller = new Controller(model);
        view.setController(controller);
        view.userCitySelectEmulationMethod();
    }
}
