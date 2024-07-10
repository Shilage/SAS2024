package catering.task;

import example.catering.businesslogic.CatERing;
import example.catering.businesslogic.errors.UnauthorizedException;
import example.catering.businesslogic.errors.UseCaseLogicException;
import example.catering.businesslogic.errors.ItemNotFoundException;
import example.catering.businesslogic.event.Service;
import example.catering.businesslogic.task.SummarySheet;
import example.catering.businesslogic.task.Task;
import example.catering.persistence.PersistenceManager;

public class TestCatERing5a {
    public static void main(String[] args) {
        try {
            PersistenceManager.executeSqlFile("catering_nofx/database/catering_db_init.sql");

            CatERing.getInstance().getUserManager().fakeLogin("Eva");
            System.out.println("Current user -> " + CatERing.getInstance().getUserManager().getCurrentUser());

            Service service = CatERing.getInstance().getEventManager().getServiceById(1);
            SummarySheet sheet =  CatERing.getInstance().getKitchenTaskMgr().openSummarySheet(service);

            // assigning a cooking procedure in order to create the task
            CatERing.getInstance().getKitchenTaskMgr().assignCookingProcedure(sheet.getListedOrderedProcedures().get(0), null, null);

            Task taskToMarkAsDone = sheet.getTasks().get(0);

            System.out.println("toPrepare -> " + taskToMarkAsDone.getToPrepare());
            System.out.println("isCompleted -> " + taskToMarkAsDone.isCompleted());

            System.out.println("Task before being marked as done -> " + taskToMarkAsDone);
            CatERing.getInstance().getKitchenTaskMgr().markCookingProcedureAsDone(taskToMarkAsDone);
            System.out.println("Task after being marked as done -> " + taskToMarkAsDone);

            System.out.println("toPrepare -> " + taskToMarkAsDone.getToPrepare());
            System.out.println("isCompleted -> " + taskToMarkAsDone.isCompleted());
        }
        catch (UnauthorizedException | UseCaseLogicException | ItemNotFoundException e){
            System.out.println("An exception occurred: " + e.getMessage());
        }
    }
}
