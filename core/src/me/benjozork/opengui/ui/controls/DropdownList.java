package me.benjozork.opengui.ui.controls;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import me.benjozork.opengui.render.object.TextComponent;
import me.benjozork.opengui.render.object.Vector2;
import me.benjozork.opengui.render.object.resource.NinePatch;
import me.benjozork.opengui.render.object.resource.Texture;
import me.benjozork.opengui.ui.Style;
import me.benjozork.opengui.ui.annotation.CallOnUnrelatedClick;
import me.benjozork.opengui.ui.annotation.DefaultStyle;
import me.benjozork.opengui.ui.annotation.FallbackStyle;
import me.benjozork.opengui.ui.annotation.IgnoreParentClassStyles;
import me.benjozork.opengui.ui.annotation.PartiallySkinnableProperty;
import me.benjozork.opengui.ui.annotation.SkinnableProperty;
import me.benjozork.opengui.ui.annotation.ZIndex;

/**
 * @author Benjozork
 */
@IgnoreParentClassStyles
public class DropdownList extends Button {

    @DefaultStyle
    public Style normal;

    @FallbackStyle(fallback = "normal")
    public Style highlighted_info;

    @FallbackStyle(fallback = "highlighted_info")
    public Style highlighted_warning;

    @FallbackStyle(fallback = "highlighted_info")
    public Style highlighted_error;

    @PartiallySkinnableProperty (field = "font", alias = "font")
    public TextComponent text;

    /**
     * The {@link NinePatch} which is drawn when the button is at a collapsed state.
     */
    public NinePatch button_expanded;

    /**
     * The {@link NinePatch} which is drawn when the button is at a collapsed state and is hovered.
     */
    public NinePatch button_expanded_hovered;

    /**
     * The {@link NinePatch} which is drawn for the topmost item of the list when it is in an idle state.
     */
    public NinePatch dropdown_menu_top;

    /**
     * The {@link NinePatch} which is drawn for the topmost item of the list when it is hovered.
     */
    public NinePatch dropdown_menu_top_hovered;

    /**
     * The {@link NinePatch} which is drawn for the topmost item of the list when it is in an idle state.
     */
    public NinePatch dropdown_menu_top_inverted;

    /**
     * The {@link NinePatch} which is drawn for the topmost item of the list when it is hovered.
     */
    public NinePatch dropdown_menu_top_inverted_hovered;

    /**
     * The {@link NinePatch} which is drawn for any intermediate item of the list when it is in an idle state.
     */
    public NinePatch dropdown_menu_middle;

    /**
     * The {@link NinePatch} which is drawn for any intermediate item of the list when it is hovered.
     */
    public NinePatch dropdown_menu_middle_hovered;

    /**
     * The {@link NinePatch} which is drawn for the bottommost item of the list when it is in an idle state.
     */
    public NinePatch dropdown_menu_bottom;

    /**
     * The {@link NinePatch} which is drawn for the bottommost item of the list when it is hovered.
     */
    public NinePatch dropdown_menu_bottom_hovered;

    /**
     * The {@link Texture} used to represent the dropdown arrow when the menu is collapsed.
     */
    public Texture arrow_collapsed;

    /**
     * The {@link Texture} used to represent the dropdown arrow when the menu is expanded.
     */
    public Texture arrow_expanded;

    /**
     * The width of the arrow.
     */
    @SkinnableProperty
    public int arrow_width;

    /**
     * The height of the arrow.
     */
    @SkinnableProperty
    public int arrow_height;

    /**
     * Whether the text is centered or not.
     */
    @SkinnableProperty
    public boolean center_text = true;

    /**
     * The distance between the text and the arrow.
     */
    @SkinnableProperty
    public int text_arrow_offset = 0;

    /**
     * The distance between the border and the arrow.
     */
    @SkinnableProperty
    public int border_arrow_offset = 5;

    /**
     * Whether the arrow is drawn on the left or not.
     */
    @SkinnableProperty
    public boolean draw_arrow_left = true;

    /**
     * Whether the button's width automatically adjusts to the list element with the biggest width.
     */
    @SkinnableProperty
    public boolean auto_adjust = false;

    /**
     * The width of an item on the list.
     * Any item whose width exceeds this limit will be cut.
     */
    @SkinnableProperty
    public int item_width = 60;

    /**
     * The minimum height of an item on the list.
     */
    @SkinnableProperty
    public int item_height = 20;

    private boolean isExpanded;

    private TextComponent[] items = new TextComponent[]{};

    private transient List<TextComponent> itemsList = new ArrayList<TextComponent>();

    @Override
    public void init() {
        super.init();
        if (this.text == null) this.text = new TextComponent(this.getContext());
    }

    @Override
    public void update() {
        if (itemsList.isEmpty()) itemsList = Arrays.asList(items);
        super.update();
    }

    @Override
    @ZIndex(index = 100)
    public void draw() {
        super.draw();

        // Draw arrow and text

        if (draw_arrow_left) {

            int arrowX = this.getAbsX() + border_arrow_offset;
            int arrowY = this.getAbsY() + this.getHeight() / 2 - arrow_height / 2;

            if (isExpanded) {
                this.getBackend().texture(arrow_expanded, arrowX, arrowY, arrow_width, arrow_height);
            } else {
                this.getBackend().texture(arrow_collapsed, arrowX, arrowY, arrow_width, arrow_height);
            }

            int textX;

            if (center_text) {
                textX = arrowX + arrow_width + (this.getWidth() - arrow_width - border_arrow_offset - text_arrow_offset) / 2 - text.getWidth() / 2;
            } else {
                textX = arrowX + arrow_width + text_arrow_offset;
            }
            int textY = this.getAbsY() + this.getHeight() / 2 - text.getHeight() / 2;

            this.getBackend().text(text, textX, textY);

        } else {

            int arrowX = this.getAbsX() + getWidth() - border_arrow_offset - arrow_width;
            int arrowY = this.getAbsY() + this.getHeight() / 2 - arrow_height / 2;

            if (isExpanded) {
                this.getBackend().texture(arrow_expanded, arrowX, arrowY, arrow_width, arrow_height);
            } else {
                this.getBackend().texture(arrow_collapsed, arrowX, arrowY, arrow_width, arrow_height);
            }

            int textX;

            if (center_text) {
                textX = getAbsX() + ((getWidth() - arrow_width - text_arrow_offset - border_arrow_offset) / 2) - text.getWidth() / 2;
            } else {
                textX = getAbsX();
            }
            int textY = this.getAbsY() + this.getHeight() / 2 - text.getHeight() / 2;

            this.getBackend().text(text, textX, textY);

        }

        // Draw items if expanded

        if (isExpanded) {

            // Draw item backgrounds

            if (isMenuGoingUp()) {

                // Draw menu upwards

                for (int i = 0; i < itemsList.size(); i++) {

                    NinePatch itemPatch = null;

                    if (i == 0) itemPatch = dropdown_menu_top;
                    if (i == 0 && isItemBeingHovered(i)) itemPatch = dropdown_menu_top_hovered;

                    if (i == itemsList.size() - 1) itemPatch = dropdown_menu_bottom;
                    if (i == itemsList.size() - 1 && isItemBeingHovered(i)) itemPatch = dropdown_menu_bottom_hovered;

                    if (i != 0 && i != itemsList.size() - 1) itemPatch = dropdown_menu_middle;
                    if (i != 0 && i != itemsList.size() - 1 && isItemBeingHovered(i)) itemPatch = dropdown_menu_middle_hovered;

                    if (itemPatch == dropdown_menu_bottom || itemPatch == dropdown_menu_bottom_hovered) {
                        if (isItemBeingHovered(i)) itemPatch = dropdown_menu_top_inverted_hovered;
                        else itemPatch = dropdown_menu_top_inverted;
                    }

                    this.getBackend().ninepatch(itemPatch, getAbsX(), getAbsY() + getHeight() + (i * item_height), getWidth(), item_height);
                }

            } else {

                // Draw menu downwards

                for (int i = 0; i < itemsList.size(); i++) {

                    NinePatch itemPatch = null;

                    if (i == 0) itemPatch = dropdown_menu_top;
                    if (i == 0 && isItemBeingHovered(i)) itemPatch = dropdown_menu_top_hovered;

                    if (i == itemsList.size() - 1) itemPatch = dropdown_menu_bottom;
                    if (i == itemsList.size() - 1 && isItemBeingHovered(i)) itemPatch = dropdown_menu_bottom_hovered;

                    if (i != 0 && i != itemsList.size() - 1) itemPatch = dropdown_menu_middle;
                    if (i != 0 && i != itemsList.size() - 1 && isItemBeingHovered(i)) itemPatch = dropdown_menu_middle_hovered;

                    this.getBackend().ninepatch(itemPatch, getAbsX(), getAbsY() - item_height - (i * item_height), getWidth(), item_height);
                }

            }

            // Draw item text

            if (isMenuGoingUp()) {
                for (int i = 0; i < itemsList.size(); i++) {
                    int textX = getAbsX() + getWidth() / 2 - itemsList.get(i).getWidth() / 2;
                    int textY = getAbsY() + getHeight() + item_height + (i * item_height) - (item_height / 2) - itemsList.get(i).getHeight() / 2;

                    this.getBackend().text(itemsList.get(i), textX, textY);
                }
            } else {
                for (int i = 0; i < itemsList.size(); i++) {
                    int textX = getAbsX() + getWidth() / 2 - itemsList.get(i).getWidth() / 2;
                    int textY = getAbsY() - (i * item_height) - (item_height / 2) - itemsList.get(i).getHeight() / 2;

                    this.getBackend().text(itemsList.get(i), textX, textY);
                }
            }

        }

    }

    @Override
    public boolean isBeingHovered() {
        if (isExpanded) {
            if (isMenuGoingUp()) {
                return (this.getBackend().getMousePos().x > getAbsX()
                        && this.getBackend().getMousePos().x < getAbsX() + getWidth()
                        && this.getBackend().getMousePos().y > getAbsY()
                        && this.getBackend().getMousePos().y < getAbsY() + getHeight() + (itemsList.size() * item_height));
            } else {
                return (this.getBackend().getMousePos().x > getAbsX()
                        && this.getBackend().getMousePos().x < getAbsX() + getWidth()
                        && this.getBackend().getMousePos().y < getAbsY() + getHeight()
                        && this.getBackend().getMousePos().y > getAbsY() - (itemsList.size() * item_height));
            }
        } else {
            return super.isBeingHovered();
        }
    }

    @Override
    public boolean onClick(Vector2 mousePos) {

        if (isExpanded) {

            // Check if click is in main button

            if (mousePos.y > getAbsY() && mousePos.y < getAbsY() + getHeight()) {
                this.setExpanded(false);
                return true;
            }

            else {
                if (isMenuGoingUp()) {

                    int mouseDeltaY = (int) (mousePos.y - getAbsY() - getHeight());

                    int itemIdx = mouseDeltaY / item_height;

                    text = itemsList.get(itemIdx);

                    return true;


                } else {

                    int mouseDeltaY = (int) (getAbsY() - mousePos.y);

                    int itemIdx = mouseDeltaY / item_height;

                    text = itemsList.get(itemIdx);

                    return true;

                }
            }

        } else {
            this.setExpanded(true);
            return true;
        }

    }

    @CallOnUnrelatedClick
    public void closeOnUnrelatedClick() {
        this.setExpanded(false);
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public List<TextComponent> getItems() {
        return Collections.unmodifiableList(this.itemsList);
    }

    public TextComponent getItem(int idx) {
        return this.itemsList.get(idx);
    }

    public void addItem(TextComponent item) {
        this.itemsList.add(item);
    }

    public void removeItem(String item) {
        this.itemsList.remove(item);
    }

    private boolean isItemBeingHovered(int itemIdx) {

        if (! (this.getBackend().getMousePos().x > this.getAbsX() && this.getBackend().getMousePos().x < this.getAbsX() + this.getWidth())) return false;

        if (isMenuGoingUp()) {

            // Menu goes upwards

            int itemMinPosDelta = getAbsY() + item_height + (itemIdx * item_height);

            return this.getBackend().getMousePos().y > itemMinPosDelta && this.getBackend().getMousePos().y < itemMinPosDelta + item_height;

        } else {


            // Menu goes upwards

            int itemMinPosDelta = getAbsY() - (itemIdx * item_height);

            return this.getBackend().getMousePos().y < itemMinPosDelta && this.getBackend().getMousePos().y > itemMinPosDelta - item_height;

        }
    }

    private boolean isMenuGoingUp() {
        return getAbsY() - itemsList.size() * item_height < 0;
    }

}