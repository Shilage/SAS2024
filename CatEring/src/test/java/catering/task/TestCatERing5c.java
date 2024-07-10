package catering.task;

import example.catering.businesslogic.CatERing;
import example.catering.businesslogic.errors.ItemNotFoundException;
import example.catering.businesslogic.errors.UnauthorizedException;
import example.catering.businesslogic.errors.UseCaseLogicException;
import example.catering.businesslogic.event.Service;
import example.catering.businesslogic.procedure.CookingProcedure;
import example.catering.businesslogic.procedure.OrderedProcedure;
import example.catering.businesslogic.task.SummarySheet;
import example.catering.businesslogic.task.Task;
import example.catering.persistence.PersistenceManager;

import java.util.ArrayList;
import java.util.Random;

public class TestCatERing5c {
    public static void main(String[] args) {
        try {
            PersistenceManager.executeSqlFile("catering_nofx/database/catering_db_init.sql");

            CatERing.getInstance().getUserManager().fakeLogin("Eva");
            System.out.println("Current user -> " + CatERing.getInstance().getUserManager().getCurrentUser());

            Service service = CatERing.getInstance().getEventManager().getServiceById(1);
            SummarySheet sheet =  CatERing.getInstance().getKitchenTaskMgr().openSummarySheet(service);

            ArrayList<OrderedProcedure> unassignedProcedures = sheet.getListedOrderedProcedures();

            System.out.println("Tasks before adding a new procedure -> " + sheet.getTasks());

            Task task = CatERing.getInstance().getKitchenTaskMgr().assignCookingProcedure(unassignedProcedures.get(0), null, null);

            System.out.println("Tasks after adding a new procedure -> " + sheet.getTasks());
            System.out.println(sheet.isAlreadyAssigned(unassignedProcedures.get(0)));

            CatERing.getInstance().getKitchenTaskMgr().deleteTask(task);

            System.out.println("Tasks after removing the latest task -> " + sheet.getTasks());
        }
        catch (UnauthorizedException | UseCaseLogicException | ItemNotFoundException e){
            System.out.println("An exception occurred: " + e.getMessage());
        }
    }
}
