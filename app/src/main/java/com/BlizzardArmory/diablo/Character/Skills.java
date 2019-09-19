package com.BlizzardArmory.diablo.Character;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Skills {

    @SerializedName("active")
    @Expose
    private List<Active> active = null;
    @SerializedName("passive")
    @Expose
    private List<Passive> passive = null;

    public List<Active> getActive() {
        return active;
    }

    public void setActive(List<Active> active) {
        this.active = active;
    }

    public List<Passive> getPassive() {
        return passive;
    }

    public void setPassive(List<Passive> passive) {
        this.passive = passive;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("active", active).append("passive", passive).toString();
    }

}