package catering.task;

import example.catering.businesslogic.CatERing;
import example.catering.businesslogic.errors.ItemNotFoundException;
import example.catering.businesslogic.errors.UnauthorizedException;
import example.catering.businesslogic.errors.UseCaseLogicException;
import example.catering.businesslogic.event.Service;
import example.catering.businesslogic.task.SummarySheet;
import example.catering.businesslogic.task.Task;
import example.catering.persistence.PersistenceManager;

import java.time.Duration;

public class TestCatERing5e {
    public static void main(String[] args) {
        try {
            PersistenceManager.executeSqlFile("catering_nofx/database/catering_db_init.sql");

            CatERing.getInstance().getUserManager().fakeLogin("Eva");
            System.out.println("Current user -> " + CatERing.getInstance().getUserManager().getCurrentUser());

            Service service = CatERing.getInstance().getEventManager().getServiceById(1);
            SummarySheet sheet =  CatERing.getInstance().getKitchenTaskMgr().openSummarySheet(service);

            // assigning a cooking procedure in order to create the task
            CatERing.getInstance().getKitchenTaskMgr().assignCookingProcedure(sheet.getListedOrderedProcedures().get(0), null, null);

            Task taskToBeModified = sheet.getTasks().get(0);

            System.out.println(taskToBeModified.getAmount());
            System.out.println(taskToBeModified.getDoses());

            System.out.println("Task before being modified -> " + taskToBeModified);

            CatERing.getInstance().getKitchenTaskMgr().modifyQuantities(taskToBeModified, "200 grams", "2");

            System.out.println("Task after being modified -> " + taskToBeModified);

            System.out.println(taskToBeModified.getAmount());
            System.out.println(taskToBeModified.getDoses());
        }
        catch (UnauthorizedException | UseCaseLogicException | ItemNotFoundException  e){
            System.out.println("An exception occurred: " + e.getMessage());
        }
    }
}
