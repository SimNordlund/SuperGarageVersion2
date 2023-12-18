package Concrete;

import java.time.LocalDate;

public class Bat implements Fordon {

    private final String regNr;
    private final double pris = 100;

    private final LocalDate incheckningstid;

    public Bat(String regNr, LocalDate incheckningstid) {
        this.regNr = regNr;
        this.incheckningstid = incheckningstid;
    }

    @Override
    public String toString() {
        return ("Båt, " + this.regNr + ", " + incheckningstid);
    }

    @Override
    public LocalDate getIncheckningstid() {
        return incheckningstid;
    }

    public String getRegNr() {
        return this.regNr;
    }

    public double getPris() {
        return this.pris;
    }
}
