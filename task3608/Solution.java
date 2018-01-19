package task3608;

import task3608.controller.Controller;
import task3608.model.FakeModel;
import task3608.model.MainModel;
import task3608.model.Model;
import task3608.view.EditUserView;
import task3608.view.UsersView;

public class Solution {
    public static void main(String[] args) {
        Model model = new MainModel();
        UsersView usersView = new UsersView();
        EditUserView editUserView = new EditUserView();
        Controller controller = new Controller();

        usersView.setController(controller);
        editUserView.setController(controller);
        controller.setModel(model);
        controller.setUsersView(usersView);
        controller.setEditUserView(editUserView);

        usersView.fireEventShowAllUsers();
        usersView.fireEventOpenUserEditForm(126L);
        editUserView.fireEventUserDeleted(124L);
        editUserView.fireEventUserChanged("Saulov",126L,2);
        usersView.fireEventShowDeletedUsers();
    }
}