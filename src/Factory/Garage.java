package Factory;

import Concrete.*;
import Strategy.RabattPris;
import Strategy.StrategierPris;
import Strategy.SuperRabattPris;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Garage {

    private static final int antalParkeringsplatser = 20;
    private static final Garage garageInstans = new Garage(); //OBS! STATIK VARIABEL!
    private final int maxTidParkering = 365;
    private int antalParkeradeFordon = 0;
    private double totalPris = 0;
    private List<Fordon> parkeradeFordon = new ArrayList<>();
    private StrategierPris prisStrategi; //För strategi

    private Garage() { //Tom konstruktor pga Singleton
    }

    public static Garage getGarageInstans() { //Statik för singelton, en instans.
        return garageInstans;
    }

    public Fordon checkaInFordon(String typ, String regNr, LocalDate parkeringsDatum) {

        Scanner scan = new Scanner(System.in);

        Fordon f = null;
        regNr = regNr.toUpperCase().trim();
        if (typ.equalsIgnoreCase("Bil")) {
            f = new Bil(regNr, parkeringsDatum);
        }
        if (typ.equalsIgnoreCase("Båt")) {
            f = new Bat(regNr, parkeringsDatum);
        }
        if (typ.equalsIgnoreCase("Moped")) {
            f = new Moped(regNr, parkeringsDatum);
        }
        if (typ.equalsIgnoreCase("Motorcykel")) {
            f = new Motorcykel(regNr, parkeringsDatum);
        }

        if (f != null) {
            System.out.println("Pris: " + f.getPris() + " kr per dag.\n Skriv nej för avbryt");
            String inputUser = scan.nextLine().trim().toLowerCase();

            if (inputUser.equals("nej")) {
                System.out.println("Adjö");
            } else {
                System.out.println("Välkommen in och parkera!");
            }
            parkeradeFordon.add(f);
            antalParkeradeFordon++;
        }
        return f;
    }

    public int checkaUtFordon(String regNr) {
        int bilPaPlats = hittaFordon(regNr);
        if (bilPaPlats == -1) {
            System.out.println("Bilen är inte parkerad här");
            return -1;
        } else if (kontrolleraParkeringstid(parkeradeFordon.get(bilPaPlats)) > 5) {
            this.prisStrategi = new RabattPris();
            totalPris = beräknaPrisVersion2(parkeradeFordon.get(bilPaPlats), kontrolleraParkeringstid(parkeradeFordon.get(bilPaPlats)));
            parkeradeFordon.remove(bilPaPlats);
        } else if (kontrolleraParkeringstid(parkeradeFordon.get(bilPaPlats)) > 10) {
            this.prisStrategi = new SuperRabattPris();
            totalPris = beräknaPrisVersion2(parkeradeFordon.get(bilPaPlats), kontrolleraParkeringstid(parkeradeFordon.get(bilPaPlats)));
            parkeradeFordon.remove(bilPaPlats);
        } else {
            totalPris = beräknaPrisVersion2(parkeradeFordon.get(bilPaPlats), kontrolleraParkeringstid(parkeradeFordon.get(bilPaPlats)));
            parkeradeFordon.remove(bilPaPlats);
        }
        return 0;
    }


    public int hittaFordon(String regNr) {
        int counter = 0;
        for (Fordon f : parkeradeFordon) {
            if (f.getRegNr().equals(regNr.toUpperCase())) {
                return counter;
            }
            counter++;
        }
        return -1;
    }

    public int kontrolleraParkeringstid(Fordon f) {
        LocalDate lD = LocalDate.now();
        long days = ChronoUnit.DAYS.between(f.getIncheckningstid(), lD);
        return (int) days;
    }

    public void setPrisStrategi(StrategierPris prisStrategi) { //Sätter strategi
        this.prisStrategi = prisStrategi;
    }

    public double beräknaPrisVersion2(Fordon f, int antalDagar) {
        return prisStrategi.räknaUtPris(f, antalDagar);
    }

    public void skickaFaktura() {
        DecimalFormat formateringAvPris = new DecimalFormat("0.00");
        String formateratPris = formateringAvPris.format(totalPris);
        System.out.println("Ditt totalpris blir: " + formateratPris + " kr.");
        System.out.println("Fakturan skickas till fordonsägarens hemadress, välkommen åter.");
    }

    public void skrivUtIncheckadeBilar() {
        for (Fordon f : parkeradeFordon) {
            System.out.println(f.toString());
        }
    }

    public boolean kontrolleraPlats() {
        return antalParkeradeFordon < antalParkeringsplatser;
    }

    public void antalPlatserLediga() {
        int ledigaPlatser = antalParkeringsplatser - antalParkeradeFordon;
        System.out.println("Antal lediga platser i Garaget: " + ledigaPlatser);
        System.out.println(" ");
    }

    public List<Fordon> getParkeradeFordon() {
        return parkeradeFordon;
    }

    public void setParkeradeFordon(List<Fordon> parkeradeFordon) {
        this.parkeradeFordon = parkeradeFordon;
    }

    public int getMaxTidParkering() {
        return maxTidParkering;
    }

    public void kontrollerBegränsningParkeradeDagar() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Vilken fordon vill du kontrollera? (Skriv in registreringsnummer)");
        String svar = scan.nextLine();
        int i = hittaFordon(svar);
        if (i != -1) {
            int f = kontrolleraParkeringstid(getParkeradeFordon().get(i));
            System.out.println("Kunden har parkerat: " + f + " dagar.");
            System.out.println("Kunden får stå parkerad totalt: " + (getMaxTidParkering() - f) + " dagar till.");
        }
    }
}
