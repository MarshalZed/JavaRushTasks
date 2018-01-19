package task3608.view;

import task3608.controller.Controller;
import task3608.model.ModelData;

public interface View {
    void refresh(ModelData modelData);
    void setController(Controller controller);
}
