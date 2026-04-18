package gui.panel.buttons;

import java.util.ArrayList;
import java.util.List;

public class GroupControllerAdapter implements IRadioButtonController {

    private final List<IRadioButtonController> controllers = new ArrayList<>();

    @Override
    public void updateSelectedItem(String option) {
        for (IRadioButtonController c : controllers) {
            c.updateSelectedItem(option);
        }
    }

    @Override
    public void hitOptionalButton() {
        for (IRadioButtonController c : controllers) {
            c.hitOptionalButton();
        }
    }

    public void addController(IRadioButtonController c) {
        this.controllers.add(c);
    }
}
