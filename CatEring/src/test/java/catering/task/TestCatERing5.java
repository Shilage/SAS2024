package catering.task;

import example.catering.businesslogic.CatERing;
import example.catering.businesslogic.errors.UnauthorizedException;
import example.catering.businesslogic.errors.UseCaseLogicException;
import example.catering.businesslogic.event.Service;
import example.catering.businesslogic.procedure.OrderedProcedure;
import example.catering.businesslogic.task.SummarySheet;
import example.catering.persistence.PersistenceManager;

import java.util.ArrayList;

public class TestCatERing5 {
    public static void main(String[] args){
        try {
            PersistenceManager.executeSqlFile("catering_nofx/database/catering_db_init.sql");

            CatERing.getInstance().getUserManager().fakeLogin("Eva");
            System.out.println("Current user -> " + CatERing.getInstance().getUserManager().getCurrentUser());

            Service service = CatERing.getInstance().getEventManager().getServiceById(1);
            SummarySheet sheet =  CatERing.getInstance().getKitchenTaskMgr().openSummarySheet(service);

            ArrayList<OrderedProcedure> unassignedProcedures = sheet.getListedOrderedProcedures();

            System.out.println("Tasks before -> " + sheet.getTasks());

            CatERing.getInstance().getKitchenTaskMgr().assignCookingProcedure(unassignedProcedures.get(0), null, null);

            System.out.println("Tasks after -> " + sheet.getTasks());
            System.out.println(sheet.isAlreadyAssigned(unassignedProcedures.get(0)));
            System.out.println(sheet.getTasks().get(0).getProcedure().equals(unassignedProcedures.get(0)));
        }
        catch (UnauthorizedException | UseCaseLogicException e){
            System.out.println("An exception occurred: " + e.getMessage());
        }
    }
}
