package catering.task;

import example.catering.businesslogic.CatERing;
import example.catering.businesslogic.errors.UnauthorizedException;
import example.catering.businesslogic.errors.UseCaseLogicException;
import example.catering.businesslogic.event.EventInfo;
import example.catering.businesslogic.event.Service;
import example.catering.businesslogic.procedure.CookingProcedure;
import example.catering.businesslogic.procedure.Recipe;
import example.catering.businesslogic.task.SummarySheet;
import example.catering.persistence.PersistenceManager;

import java.util.ArrayList;

public class TestCatERing1 {
    public static void main(String[] args) {
        try {
            PersistenceManager.executeSqlFile("catering_nofx/database/catering_db_init.sql");

            CatERing.getInstance().getUserManager().fakeLogin("Eva");
            System.out.println("Current user -> " + CatERing.getInstance().getUserManager().getCurrentUser());

            Service service = CatERing.getInstance().getEventManager().getServiceById(1);


            ArrayList<CookingProcedure> expectedProcedure = new ArrayList<>(service.getUsedMenu().getAllRecipes());

            System.out.println("Generating new summary sheet starting from -> " + service);
            CatERing.getInstance().getKitchenTaskMgr().generateSummarySheet(service);
            SummarySheet sheet = CatERing.getInstance().getKitchenTaskMgr().getCurrentSummarySheet();
            ArrayList<CookingProcedure> generatedProcedures = sheet.getListedProcedures();

            // testing all recipes in the menu are added correctly to the summary sheet
            for (CookingProcedure procedure : expectedProcedure) {
                System.out.println(generatedProcedures.contains(procedure));
            }
        } catch (UnauthorizedException | UseCaseLogicException e) {
            System.out.println("An exception occurred: " + e.getMessage());
        }
    }
}
