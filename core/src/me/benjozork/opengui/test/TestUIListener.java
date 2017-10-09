package me.benjozork.opengui.test;

import me.benjozork.opengui.ui.Event;
import me.benjozork.opengui.ui.annotation.Listener;
import me.benjozork.opengui.ui.controls.RadioButtonGroup;

/**
 * @author Benjozork
 */
public class TestUIListener {

    @Listener(element = "radiogroup_0",
              event = Event.VALUE_CHANGED)
    public void radiobuttons_0_valueChanged(RadioButtonGroup buttonGroup) {
        System.out.println(buttonGroup.tickedButton.text.getText());
    }

}