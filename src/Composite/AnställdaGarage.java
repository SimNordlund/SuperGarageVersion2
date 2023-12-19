package Composite;

import java.util.List;

public class AnställdaGarage {
    private String förnamn;
    private String efternamn;
    private String yrke;
    private String telefonnummer;
    private List<AnställdaGarage> listaAnställda;

    public AnställdaGarage (String förnamn, String efternamn, String yrke, String telefonnummer) {
        this.förnamn = förnamn;
        this.efternamn = efternamn;
        this.yrke = yrke;
        this.telefonnummer = telefonnummer;
    }
    public void läggTillAnställd(AnställdaGarage anställd) {
        listaAnställda.add(anställd);
    }
    public void taBortAnställd(AnställdaGarage anställd) {
        listaAnställda.remove(anställd);
    }
    public List<AnställdaGarage> getListaAnställda() {
        return listaAnställda;
    }
}
