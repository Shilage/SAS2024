package example.catering.businesslogic.shifts;

import example.catering.businesslogic.event.Service;
import example.catering.businesslogic.user.User;

import java.util.ArrayList;

public class ShiftManager {

    public ShiftManager(){

    }

    public boolean isAvailable (User cook, KitchenShift shift){
        // visto che il caso d'uso non è da gestire usppongo che il cuoco sia sempre disponibile
        return true;
    }

    public ArrayList<Shift> getShiftsFromService (Service service){
        return new ArrayList<>();
    }
}
