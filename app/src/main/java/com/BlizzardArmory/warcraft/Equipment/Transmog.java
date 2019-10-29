
package com.BlizzardArmory.warcraft.Equipment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Transmog {

    @SerializedName("item")
    @Expose
    private Item item;
    @SerializedName("displaystring")
    @Expose
    private String displayString;
    @SerializedName("itemmodifiedappearanceid")
    @Expose
    private int itemModifiedAppearanceId;

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public String getDisplayString() {
        return displayString;
    }

    public void setDisplayString(String displayString) {
        this.displayString = displayString;
    }

    public int getItemModifiedAppearanceId() {
        return itemModifiedAppearanceId;
    }

    public void setItemModifiedAppearanceId(int itemModifiedAppearanceId) {
        this.itemModifiedAppearanceId = itemModifiedAppearanceId;
    }

}