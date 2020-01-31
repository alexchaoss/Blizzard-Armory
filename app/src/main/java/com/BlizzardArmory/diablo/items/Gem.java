package com.BlizzardArmory.diablo.items;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

public class Gem {

    @SerializedName("item")
    @Expose
    private GemInfo item;
    @SerializedName("attributes")
    @Expose
    private List<String> attributes = null;
    @SerializedName("isGem")
    @Expose
    private Boolean isGem;
    @SerializedName("isJewel")
    @Expose
    private Boolean isJewel;

    public GemInfo getItem() {
        return item;
    }

    public void setItem(GemInfo item) {
        this.item = item;
    }

    public List<String> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<String> attributes) {
        this.attributes = attributes;
    }

    public Boolean getIsGem() {
        return isGem;
    }

    public void setIsGem(Boolean isGem) {
        this.isGem = isGem;
    }

    public Boolean getIsJewel() {
        return isJewel;
    }

    public void setIsJewel(Boolean isJewel) {
        this.isJewel = isJewel;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("item", item).append("attributes", attributes).append("isGem", isGem).append("isJewel", isJewel).toString();
    }

}