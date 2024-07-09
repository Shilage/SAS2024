package example.catering;

import example.catering.businesslogic.event.Service;
import example.catering.businesslogic.procedure.CookingProcedureManager;
import example.catering.businesslogic.shifts.Shift;
import example.catering.businesslogic.task.KitchenTaskManager;

public class TestPreparations {
    public static void main(String[] args) {
        KitchenTaskManager ktm = new KitchenTaskManager();
        System.out.println(ktm.getAllSummarySheets());
    }
}
