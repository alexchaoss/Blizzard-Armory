
package com.BlizzardArmory.overwatch.Competitive;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Average_____ {

    @SerializedName("allDamageDoneAvgPer10Min")
    @Expose
    private int allDamageDoneAvgPer10Min;
    @SerializedName("criticalHitsAvgPer10Min")
    @Expose
    private float criticalHitsAvgPer10Min;
    @SerializedName("deathsAvgPer10Min")
    @Expose
    private float deathsAvgPer10Min;
    @SerializedName("eliminationsAvgPer10Min")
    @Expose
    private float eliminationsAvgPer10Min;
    @SerializedName("eliminationsPerLife")
    @Expose
    private int eliminationsPerLife;
    @SerializedName("finalBlowsAvgPer10Min")
    @Expose
    private float finalBlowsAvgPer10Min;
    @SerializedName("objectiveKillsAvgPer10Min")
    @Expose
    private float objectiveKillsAvgPer10Min;
    @SerializedName("objectiveTimeAvgPer10Min")
    @Expose
    private String objectiveTimeAvgPer10Min;

    public int getAllDamageDoneAvgPer10Min() {
        return allDamageDoneAvgPer10Min;
    }

    public void setAllDamageDoneAvgPer10Min(int allDamageDoneAvgPer10Min) {
        this.allDamageDoneAvgPer10Min = allDamageDoneAvgPer10Min;
    }

    public float getCriticalHitsAvgPer10Min() {
        return criticalHitsAvgPer10Min;
    }

    public void setCriticalHitsAvgPer10Min(float criticalHitsAvgPer10Min) {
        this.criticalHitsAvgPer10Min = criticalHitsAvgPer10Min;
    }

    public float getDeathsAvgPer10Min() {
        return deathsAvgPer10Min;
    }

    public void setDeathsAvgPer10Min(float deathsAvgPer10Min) {
        this.deathsAvgPer10Min = deathsAvgPer10Min;
    }

    public float getEliminationsAvgPer10Min() {
        return eliminationsAvgPer10Min;
    }

    public void setEliminationsAvgPer10Min(float eliminationsAvgPer10Min) {
        this.eliminationsAvgPer10Min = eliminationsAvgPer10Min;
    }

    public int getEliminationsPerLife() {
        return eliminationsPerLife;
    }

    public void setEliminationsPerLife(int eliminationsPerLife) {
        this.eliminationsPerLife = eliminationsPerLife;
    }

    public float getFinalBlowsAvgPer10Min() {
        return finalBlowsAvgPer10Min;
    }

    public void setFinalBlowsAvgPer10Min(float finalBlowsAvgPer10Min) {
        this.finalBlowsAvgPer10Min = finalBlowsAvgPer10Min;
    }

    public float getObjectiveKillsAvgPer10Min() {
        return objectiveKillsAvgPer10Min;
    }

    public void setObjectiveKillsAvgPer10Min(float objectiveKillsAvgPer10Min) {
        this.objectiveKillsAvgPer10Min = objectiveKillsAvgPer10Min;
    }

    public String getObjectiveTimeAvgPer10Min() {
        return objectiveTimeAvgPer10Min;
    }

    public void setObjectiveTimeAvgPer10Min(String objectiveTimeAvgPer10Min) {
        this.objectiveTimeAvgPer10Min = objectiveTimeAvgPer10Min;
    }

}
