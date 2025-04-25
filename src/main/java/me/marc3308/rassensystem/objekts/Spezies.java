package me.marc3308.rassensystem.objekts;

import java.util.ArrayList;

public class Spezies {

    //ticker
    private String ticker;
    private String erkennung;

    //werte
    private double leben; //grund leben des spielers %
    private double lebenreg; //grund lebenreg des spielers %
    private double ausdauer; //grund ausdauer des spielers %
    private double ausreg; //grund ausdauerreg des spielers %
    private double mana; //grund mana des spielers %
    private double manareg; //grund mana des spielers %

    //passiven
    private ArrayList<String> passiven;

    public Spezies(String ticker, String erkennung, double leben, double lebenreg, double ausdauer, double ausreg, double mana, double manareg, ArrayList<String> passiven) {
        this.ticker = ticker;
        this.erkennung = erkennung;
        this.leben = leben;
        this.lebenreg = lebenreg;
        this.ausdauer = ausdauer;
        this.ausreg = ausreg;
        this.mana = mana;
        this.manareg = manareg;
        this.passiven = passiven;
    }

    public String getErkennung() {
        return erkennung;
    }

    public String getTicker() {
        return ticker;
    }
    public void setTicker(String ticker) {
        this.ticker = ticker;
    }
    public double getLeben() {
        return leben;
    }
    public void setLeben(double leben) {
        this.leben = leben;
    }
    public double getLebenreg() {
        return lebenreg;
    }
    public void setLebenreg(double lebenreg) {
        this.lebenreg = lebenreg;
    }
    public double getAusdauer() {
        return ausdauer;
    }
    public void setAusdauer(double ausdauer) {
        this.ausdauer = ausdauer;
    }
    public double getAusreg() {
        return ausreg;
    }
    public void setAusreg(double ausreg) {
        this.ausreg = ausreg;
    }
    public double getMana() {
        return mana;
    }
    public void setMana(double mana) {
        this.mana = mana;
    }
    public double getManareg() {
        return manareg;
    }

    public void setManareg(double manareg) {
        this.manareg = manareg;
    }

    public ArrayList<String> getPassiven() {
        return passiven;
    }

    public void setPassiven(ArrayList<String> passiven) {
        this.passiven = passiven;
    }
}
