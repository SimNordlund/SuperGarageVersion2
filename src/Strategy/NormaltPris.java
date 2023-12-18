package Strategy;

import Concrete.Fordon;

public class NormaltPris implements StrategierPris {
    @Override
    public double räknaUtPris(Fordon fordonParkerat, int parkeradeDagar) {
        return parkeradeDagar * fordonParkerat.getPris();
    }
}
