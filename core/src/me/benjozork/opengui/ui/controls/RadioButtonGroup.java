package me.benjozork.opengui.ui.controls;

import java.util.ArrayList;

import me.benjozork.opengui.render.object.Vector2;
import me.benjozork.opengui.ui.Element;
import me.benjozork.opengui.ui.Event;
import me.benjozork.opengui.ui.annotation.ChildrenClassFilter;

/**
 * @author Benjozork
 */
@ChildrenClassFilter(
        type = ChildrenClassFilter.FilterType.INCLUDE_ONLY,
        classes = {RadioButton.class}
)
public class RadioButtonGroup extends Element {

    public ArrayList<RadioButton> buttons = new ArrayList<RadioButton>();

    public RadioButton tickedButton;

    public void updateButtonStates(RadioButton clickedButton) {
        this.tickedButton = clickedButton;
        for (RadioButton button : buttons) {
            if (button != clickedButton) button.setTicked(false);
        }
        this.callEvent(Event.VALUE_CHANGED);
    }

    @Override
    public void init() {

    }

    @Override
    public boolean isBeingHovered() {
        for (RadioButton button : buttons) {
            if (button.isBeingHovered()) return true;
        }
        return false;
    }

    @Override
    public boolean onClick(Vector2 mousePos) {
        return false;
    }

}