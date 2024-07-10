package catering.task;

import example.catering.businesslogic.CatERing;
import example.catering.businesslogic.errors.ItemNotFoundException;
import example.catering.businesslogic.errors.UnauthorizedException;
import example.catering.businesslogic.errors.UseCaseLogicException;
import example.catering.businesslogic.event.Service;
import example.catering.businesslogic.task.SummarySheet;
import example.catering.businesslogic.task.Task;
import example.catering.businesslogic.user.User;
import example.catering.persistence.PersistenceManager;

import java.time.Duration;

public class TestCatERing7 {
    public static void main(String[] args) {
        try {
            PersistenceManager.executeSqlFile("catering_nofx/database/catering_db_init.sql");

            CatERing.getInstance().getUserManager().fakeLogin("Eva");
            System.out.println("Current user -> " + CatERing.getInstance().getUserManager().getCurrentUser());

            Service service = CatERing.getInstance().getEventManager().getServiceById(1);
            SummarySheet sheet =  CatERing.getInstance().getKitchenTaskMgr().openSummarySheet(service);

            System.out.println(sheet.getListedOrderedProcedures().get(1));

            // assigning a cooking procedure in order to create the task
            CatERing.getInstance().getKitchenTaskMgr().assignCookingProcedure(sheet.getListedOrderedProcedures().get(1), null, User.loadUser("Dana"));

            Task taskToBeInitialized = CatERing.getInstance().getKitchenTaskMgr().getCurrentWorkingTask();

            System.out.println(taskToBeInitialized.getTimeToComplete());
            System.out.println(taskToBeInitialized.getAmount());
            System.out.println(taskToBeInitialized.getDoses());

            System.out.println("Task before being modified -> " + taskToBeInitialized);

            CatERing.getInstance().getKitchenTaskMgr().addTaskInfo(Duration.ofHours(1), "200 grams", "2");

            System.out.println("Task after being modified -> " + taskToBeInitialized);

            System.out.println(taskToBeInitialized.getTimeToComplete());
            System.out.println(taskToBeInitialized.getAmount());
            System.out.println(taskToBeInitialized.getDoses());
        }
        catch (UnauthorizedException | UseCaseLogicException | ItemNotFoundException e){
            System.out.println("An exception occurred: " + e.getMessage());
        }
    }
}
