package catering.task;

import example.catering.businesslogic.CatERing;
import example.catering.businesslogic.errors.UnauthorizedException;
import example.catering.businesslogic.errors.UseCaseLogicException;
import example.catering.businesslogic.event.Service;
import example.catering.businesslogic.procedure.CookingProcedure;
import example.catering.persistence.PersistenceManager;

import java.util.ArrayList;
import java.util.Random;

public class TestCatERing2 {

    // Testing addCookingProcedure function
    public static void main(String[] args) {
        try {
            PersistenceManager.executeSqlFile("catering_nofx/database/catering_db_init.sql");

            CatERing.getInstance().getUserManager().fakeLogin("Eva");
            System.out.println("Current user -> " + CatERing.getInstance().getUserManager().getCurrentUser());

            Service service = CatERing.getInstance().getEventManager().getServiceById(1);

            System.out.println("Opening an already generated summary sheet starting from -> " + service);
            CatERing.getInstance().getKitchenTaskMgr().openSummarySheet(service);

            System.out.println(CatERing.getInstance().getKitchenTaskMgr().getCurrentSummarySheet());

            ArrayList<CookingProcedure> procedures = CatERing.getInstance().getProcedureManager().getProcedures();
            Random random = new Random();
            CookingProcedure randomProcedure = procedures.get(random.nextInt(procedures.size()));
            CatERing.getInstance().getKitchenTaskMgr().addCookingProcedure(randomProcedure);

            System.out.println(CatERing.getInstance().getKitchenTaskMgr().getCurrentSummarySheet());
        }
        catch (UnauthorizedException | UseCaseLogicException e){
            System.out.println("An exception occurred: " + e.getMessage());
        }
    }
}