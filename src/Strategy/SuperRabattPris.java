package Strategy;

import Concrete.Fordon;

public class SuperRabattPris implements StrategierPris { //Concrete klass för prisättning.
    @Override
    public double räknaUtPris(Fordon fordonParkerat, int parkeradeDagar){
        return parkeradeDagar * fordonParkerat.getPris() * 0.5;
    }
}
