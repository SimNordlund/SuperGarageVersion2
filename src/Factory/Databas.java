package Factory;
import Concrete.*;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Databas {
    private final List<Fordon> parkeradeBilar = new ArrayList<>();

    public void sparaFordon(List<Fordon> parkeradeF) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/Factory/fordon.txt"))) {
            for (Fordon fordon : parkeradeF) {
                writer.write(fordon.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Fel uppstod vid skrivning till fil.");
        }
    }

    public List<Fordon> läsInFordon() {

        try (BufferedReader reader = new BufferedReader(new FileReader("src/Factory/fordon.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {

                String[] tempDelar = line.split(", ");

                String fordonsTyp = tempDelar[0];
                String regNummer = tempDelar[1];
                LocalDate parkeringsDatum = LocalDate.parse(tempDelar[2]);

                if (fordonsTyp.equals("Bil")) {
                    parkeradeBilar.add(new Bil(regNummer, parkeringsDatum));
                }
                if (fordonsTyp.equals("Båt")) {
                    parkeradeBilar.add(new Bat(regNummer, parkeringsDatum));
                }
                if (fordonsTyp.equals("Moped")) {
                    parkeradeBilar.add(new Moped(regNummer, parkeringsDatum));
                }
                if (fordonsTyp.equals("Motorcykel")) {
                    parkeradeBilar.add(new Motorcykel(regNummer, parkeringsDatum));
                }

            }
        } catch (IOException e) {
            System.out.println("Fel inträffade vid läsning från fil.");
        }

        return parkeradeBilar;
    }
}


