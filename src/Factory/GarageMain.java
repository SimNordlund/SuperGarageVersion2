package Factory;

import Concrete.Fordon;
import Strategy.NormaltPris;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class GarageMain {
    private final Databas databas = new Databas();
    private final Scanner scan = new Scanner(System.in);
    private final LocalDate parkeringsDatum = LocalDate.now();
    private final Garage garage = Garage.getGarageInstans();

    //Lösenord til den administrativa delen av programmet är Garage123

    public GarageMain() {
        garage.setPrisStrategi(new NormaltPris()); //Sätter initialt pris utan rabatter. Justeras eventuellt senare i programmet.
        try {
            läsInFordon();
            välkommenOchInfo();
            garage.antalPlatserLediga();
            while (true) {
                String s = kundEllerAnställd();
                if (s.equals("1")) {
                    kund();
                } else if (s.equals("2")) {
                    anställd();
                } else {
                    System.out.println("Om du inte är anställd eller kund, vänligen lämna området.");
                    System.exit(0);
                }
                databas.sparaFordon(garage.getParkeradeFordon());
            }
        } catch (Exception e) {
            databas.sparaFordon(garage.getParkeradeFordon());
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        GarageMain garageMain = new GarageMain();
    }

    //Frågor om användare är anställd eller kund.
    public String kundEllerAnställd() {
        System.out.println("Är du anställd eller kund? \nSkriv:  \n- 1 för kund. \n- 2 för anställd.");
        return scan.nextLine().trim().toLowerCase();
    }

    //Hanterar funktioner i programmet för kund.
    public void kund() {
        System.out.println("Vill du checka in eller checka ut ett fordon? \nAnge: \n- 1 för att checka in \n- 2 för att checka ut" +
                "\n- 3 för att stänga ner programmet.");
        String inEllerUtFråga = scan.nextLine().trim().toLowerCase();
        if (inEllerUtFråga.equals("1")) {
            if (garage.kontrolleraPlats()) {

                System.out.println("Vad har du för registreringsnummer?");
                String regNr = scan.nextLine();
                System.out.println("Vad har du för fordonstyp?");
                String fordonsTyp = scan.nextLine();

                if (garage.checkaInFordon(fordonsTyp, regNr, parkeringsDatum) == null) {
                    System.out.println("Fordonet får inte parkera här.");
                }
            }
            else {
                System.out.println("Garaget är fullt, vänligen återkom i ett senare skede.");
            }
        } else if (inEllerUtFråga.equals("2")) {
            int temp = 0;
            System.out.println("Vad har du för registreringsnummer?");
            String regNr = scan.nextLine();
            temp = garage.checkaUtFordon(regNr);
            if (temp == 0) {
                garage.skickaFaktura();
            }
        } else if (inEllerUtFråga.equals("3")) {
            System.out.println("Adjöken!");
            databas.sparaFordon(garage.getParkeradeFordon());
            System.exit(0);
        }
    }

    //Metod som ber användaren om lösenord för att komma åt den administrativa delen av programmet
    public void kontrollOmAnställd () {
        System.out.println("Ange lösenordet");
        int försökLösenord = 0;
        while (true) {
            String indataAnställd = scan.nextLine();
            if (indataAnställd.equals("Garage123")) {
                System.out.println("Korrekt lösenord");
                break;
            }
            System.out.println("Fel lösenord");
            försökLösenord++;
            if (försökLösenord == 3) {
                System.out.println("Programmet avslutas. Du har försökt för många gånger.");
                databas.sparaFordon(garage.getParkeradeFordon());
                System.exit(0);
            }
        }
    }

    //Hanterar funktioner i programmet för anställda.
    public void anställd() {
        kontrollOmAnställd();
        System.out.println("Vad vill du göra? Ange: \n- 1 för att söka i databas eller\n- 2 för Checka in eller checka ut en kund" +
                "\n- 3 för att skriva ut alla fordon" + " \n- 4 för att kontrollera parkeringstid."
                + " \n- 5 för att stänga ner programmet.");
        String indataAnställd = scan.nextLine().trim().toLowerCase();

        if (indataAnställd.equals("1")) {
            System.out.println("Vilket reg nr?");
            String regNr = scan.nextLine().trim().toUpperCase();
            int parkeringsPlats = garage.hittaFordon(regNr);
            if (parkeringsPlats == -1) {
                System.out.println("Bilen är inte parkerad här!");
            } else {
                System.out.println(garage.getParkeradeFordon().get(parkeringsPlats).toString());
            }
        } else if (indataAnställd.equals("2")) {
            kund();
        } else if (indataAnställd.equals("3")) {
            garage.skrivUtIncheckadeBilar();
        } else if (indataAnställd.equals("4")) {
            garage.kontrollerBegränsningParkeradeDagar();
        } else if (indataAnställd.equals("5")) {
            System.out.println("Adjöken!");
            databas.sparaFordon(garage.getParkeradeFordon());
            System.exit(0);
        } else {
            System.out.println("Adjöken");
        }
    }

    //Skriver ut information i samband med programmets start.
    public void välkommenOchInfo() {
        System.out.println("Välkommen till Super Garaget!");
        System.out.println("Om något inte fungerar kontakta oss på Supergaraget AB: 076 123 45 67" + "\n" +
                "\n Garaget är öppet: Måndag-Söndag 00:00 - 23:59." +
                "\n - Obemannat 16:00 - 23:59.");
    }

    //Sätter lagrade fordon ifrån textfilen i ParkeradeFordon. Sätter även antalet parkerade fordon.
    public void läsInFordon() {
        List<Fordon> tempHållare = databas.läsInFordon();
        garage.setParkeradeFordon(tempHållare);
        garage.setAntalParkeradeFordon(tempHållare.size());
    }
}
