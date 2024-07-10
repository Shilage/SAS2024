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
import example.catering.businesslogic.user.User;
import example.catering.persistence.PersistenceManager;

import java.util.ArrayList;
import java.util.Random;

public class TestCatERing5b {
    public static void main(String[] args){
        try {
            PersistenceManager.executeSqlFile("catering_nofx/database/catering_db_init.sql");

            CatERing.getInstance().getUserManager().fakeLogin("Eva");
            System.out.println("Current user -> " + CatERing.getInstance().getUserManager().getCurrentUser());

            Service service = CatERing.getInstance().getEventManager().getServiceById(1);
            SummarySheet sheet =  CatERing.getInstance().getKitchenTaskMgr().openSummarySheet(service);

            // assigning a cooking procedure in order to create the task
            CatERing.getInstance().getKitchenTaskMgr().assignCookingProcedure(sheet.getListedOrderedProcedures().get(0), null, User.loadUser("Dana"));

            Task taskToBeModified = sheet.getTasks().get(0);

            System.out.println(taskToBeModified.getProcedure());
            System.out.println(taskToBeModified.getCook());

            System.out.println("Task before being modified -> " + taskToBeModified);

            ArrayList<CookingProcedure> procedures = CatERing.getInstance().getProcedureManager().getProcedures();
            Random random = new Random();
            CookingProcedure randomProcedure = procedures.get(random.nextInt(procedures.size()));
            CatERing.getInstance().getKitchenTaskMgr().modifyTask(taskToBeModified, randomProcedure, null, null);

            System.out.println("Task after being modified -> " + taskToBeModified);

            System.out.println(taskToBeModified.getProcedure());
            System.out.println(taskToBeModified.getCook());
        }
        catch (UnauthorizedException | UseCaseLogicException | ItemNotFoundException e){
            System.out.println("An exception occurred: " + e.getMessage());
        }
    }
}
