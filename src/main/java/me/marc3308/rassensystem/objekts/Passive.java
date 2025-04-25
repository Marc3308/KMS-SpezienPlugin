package me.marc3308.rassensystem.objekts;

import java.util.HashMap;

public class Passive {

    private String ticker;
    private String erkennung;

    private Boolean toggle;
    private HashMap<String,Integer> passive;

    public Passive(String ticker, String erkennung, Boolean toggle, HashMap<String,Integer> passive) {
        this.ticker = ticker;
        this.erkennung = erkennung;
        this.toggle = toggle;
        this.passive = passive;
    }

    public String getErkennung() {
        return erkennung;
    }

    public Boolean getToggle() {
        return toggle;
    }

    public HashMap<String, Integer> getPassive() {
        return passive;
    }

    public String getTicker() {
        return ticker;
    }

    public void setPassive(HashMap<String, Integer> passive) {
        this.passive = passive;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public void setToggle(Boolean toggle) {
        this.toggle = toggle;
    }
}
