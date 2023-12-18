package Strategy;

import Concrete.Fordon;

public interface StrategierPris {
    double räknaUtPris(Fordon fordonParkerat, int parkeradeDagar);
}
