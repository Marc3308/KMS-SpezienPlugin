package me.marc3308.rassensystem.objekts;

import org.bukkit.Material;

import java.util.HashMap;

public class einstellungen {

    //waffen
    private int Schadenimkampf; //Schaden den man im kampf bekommt %
    private int Schadenohneausdauer; //Schaden wenn man keine ausdauer mehr hat %
    private int Regenerationimkampf; //Regeneration im kampf %
    private int kozeit; //zeit die der spieler ko ist+
    private int Kampfdauer; //die gesamtdauer des kampfes nach begin und lasthit +
    private int standartwaffenkosten; //die standart kosten die eine waffe hat wenn sie nicht in der liste ist +
    private HashMap<Material, Double> waffenschlagkosnte; //ausdauerkosten die die waffe beim angriff hat +

    //werte
    private double leben; //grund leben des spielers +
    private double lebenreg; //grund lebenreg des spielers %
    private double ausdauer; //grund ausdauer des spielers +
    private double ausreg; //grund ausdauerreg des spielers %
    private double mana; //grund mana des spielers +
    private double manareg; //grund mana des spielers %

    //waffenwerte
    private double waffenschaden; //grund waffenschaden des spielers %
    private double waffengeschwindigkeit; //grund waffengeschwindigkeit des spielers %
    private double waffencritdmg; //grund waffen crit dmg des spielers %
    private double waffencritchance; //grund waffen crit chance des spielers %

    //fähigkeitswerte
    private double fahigkeitsgeschwindigkeit; //grund fähigkeitsgeschwindigkeit des spielers %
    private double fahigkeitscritdmg; //grund fähigkeits crit dmg des spielers %
    private double fahigkeitscritchance; //grund fähigkeits crit chance des spielers %
    private double fahigkeitsdmg; //grund fight dmg des spielers %

    //rest
    private double bewegungsgeschwindigkeit; //grund bewegungsgeschwindigkeit des spielers %
    private int skillslots; //start skillslots des spielers +

    public einstellungen(int Schadenimkampf,int Schadenohneausdauer,int Regenerationimkampf,int kozeit,int Kampfdauer,int standartwaffenkosten,HashMap<Material, Double> waffenschlagkosnte
            ,double leben,double lebenreg,double ausdauer,double ausreg,double mana,double manareg
            ,double waffenschaden,double waffengeschwindigkeit,double waffencritdmg,double waffencritchance
            ,double fahigkeitsgeschwindigkeit,double fahigkeitscritdmg,double fahigkeitscritchance,double fahigkeitsdmg
            ,double bewegungsgeschwindigkeit,int skillslots){
        this.Schadenimkampf=Schadenimkampf;
        this.Schadenohneausdauer=Schadenohneausdauer;
        this.Regenerationimkampf=Regenerationimkampf;
        this.kozeit=kozeit;
        this.Kampfdauer=Kampfdauer;
        this.standartwaffenkosten=standartwaffenkosten;
        this.waffenschlagkosnte=waffenschlagkosnte;
        this.leben=leben;
        this.lebenreg=lebenreg;
        this.ausdauer=ausdauer;
        this.ausreg=ausreg;
        this.mana=mana;
        this.manareg=manareg;
        this.waffenschaden=waffenschaden;
        this.waffengeschwindigkeit=waffengeschwindigkeit;
        this.waffencritdmg=waffencritdmg;
        this.waffencritchance=waffencritchance;
        this.fahigkeitsgeschwindigkeit=fahigkeitsgeschwindigkeit;
        this.fahigkeitscritdmg=fahigkeitscritdmg;
        this.fahigkeitscritchance=fahigkeitscritchance;
        this.fahigkeitsdmg=fahigkeitsdmg;
        this.bewegungsgeschwindigkeit=bewegungsgeschwindigkeit;
        this.skillslots=skillslots;
    }

    public int getStandartwaffenkosten() {
        return standartwaffenkosten;
    }

    public void setStandartwaffenkosten(int standartwaffenkosten) {
        this.standartwaffenkosten = standartwaffenkosten;
    }

    public int getKampfdauer() {
        return Kampfdauer;
    }

    public double getAusdauer() {
        return ausdauer;
    }

    public double getAusreg() {
        return ausreg;
    }

    public double getLeben() {
        return leben;
    }

    public double getLebenreg() {
        return lebenreg;
    }

    public double getMana() {
        return mana;
    }

    public HashMap<Material, Double> getWaffenschlagkosnte() {
        return waffenschlagkosnte;
    }

    public double getManareg() {
        return manareg;
    }

    public double getWaffengeschwindigkeit() {
        return waffengeschwindigkeit;
    }

    public double getWaffencritdmg() {
        return waffencritdmg;
    }

    public int getRegenerationimkampf() {
        return Regenerationimkampf;
    }

    public int getKozeit() {
        return kozeit;
    }

    public void setKozeit(int kozeit) {
        this.kozeit = kozeit;
    }

    public double getWaffenschaden() {
        return waffenschaden;
    }

    public int getSchadenimkampf() {
        return Schadenimkampf;
    }

    public int getSchadenohneausdauer() {
        return Schadenohneausdauer;
    }

    public double getWaffencritchance() {
        return waffencritchance;
    }

    public double getBewegungsgeschwindigkeit() {
        return bewegungsgeschwindigkeit;
    }

    public double getFahigkeitscritchance() {
        return fahigkeitscritchance;
    }

    public double getFahigkeitscritdmg() {
        return fahigkeitscritdmg;
    }

    public double getFahigkeitsdmg() {
        return fahigkeitsdmg;
    }

    public double getFahigkeitsgeschwindigkeit() {
        return fahigkeitsgeschwindigkeit;
    }

    public int getSkillslots() {
        return skillslots;
    }

    public void setAusdauer(double ausdauer) {
        this.ausdauer = ausdauer;
    }

    public void setSchadenimkampf(int schadenimkampf) {
        Schadenimkampf = schadenimkampf;
    }

    public void setAusreg(double ausreg) {
        this.ausreg = ausreg;
    }

    public void setKampfdauer(int kampfdauer) {
        Kampfdauer = kampfdauer;
    }

    public void setFahigkeitscritdmg(double fahigkeitscritdmg) {
        this.fahigkeitscritdmg = fahigkeitscritdmg;
    }

    public void setLeben(double leben) {
        this.leben = leben;
    }

    public void setLebenreg(double lebenreg) {
        this.lebenreg = lebenreg;
    }

    public void setMana(double mana) {
        this.mana = mana;
    }

    public void setFahigkeitsgeschwindigkeit(double fahigkeitsgeschwindigkeit) {
        this.fahigkeitsgeschwindigkeit = fahigkeitsgeschwindigkeit;
    }

    public void setManareg(double manareg) {
        this.manareg = manareg;
    }

    public void setRegenerationimkampf(int regenerationimkampf) {
        Regenerationimkampf = regenerationimkampf;
    }

    public void setBewegungsgeschwindigkeit(double bewegungsgeschwindigkeit) {
        this.bewegungsgeschwindigkeit = bewegungsgeschwindigkeit;
    }

    public void setFahigkeitscritchance(double fahigkeitscritchance) {
        this.fahigkeitscritchance = fahigkeitscritchance;
    }

    public void setSchadenohneausdauer(int schadenohneausdauer) {
        Schadenohneausdauer = schadenohneausdauer;
    }

    public void setWaffencritchance(double waffencritchance) {
        this.waffencritchance = waffencritchance;
    }

    public void setFahigkeitsdmg(double fahigkeitsdmg) {
        this.fahigkeitsdmg = fahigkeitsdmg;
    }

    public void setWaffencritdmg(double waffencritdmg) {
        this.waffencritdmg = waffencritdmg;
    }

    public void setWaffenschaden(double waffenschaden) {
        this.waffenschaden = waffenschaden;
    }

    public void setWaffengeschwindigkeit(double waffengeschwindigkeit) {
        this.waffengeschwindigkeit = waffengeschwindigkeit;
    }

    public void setWaffenschlagkosnte(HashMap<Material, Double> waffenschlagkosnte) {
        this.waffenschlagkosnte = waffenschlagkosnte;
    }

    public void setSkillslots(int skillslots) {
        this.skillslots = skillslots;
    }
}

