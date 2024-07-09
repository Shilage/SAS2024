package catering.task;

import example.catering.businesslogic.CatERing;
import example.catering.businesslogic.errors.UnauthorizedException;
import example.catering.businesslogic.errors.UseCaseLogicException;
import example.catering.businesslogic.event.Service;
import example.catering.businesslogic.procedure.CookingProcedure;
import example.catering.businesslogic.task.SummarySheet;
import example.catering.persistence.PersistenceManager;

import java.util.ArrayList;

public class TestCatERing1a {
    public static void main(String[] args) {
        try {
            PersistenceManager.executeSqlFile("catering_nofx/database/catering_db_init.sql");

            CatERing.getInstance().getUserManager().fakeLogin("Eva");
            System.out.println("Current user -> " + CatERing.getInstance().getUserManager().getCurrentUser());

            Service service = CatERing.getInstance().getEventManager().getServiceById(1);

            CatERing.getInstance().getKitchenTaskMgr().openSummarySheet(service);

            SummarySheet summarySheet = SummarySheet.loadById(1);

            System.out.println(summarySheet.equals(CatERing.getInstance().getKitchenTaskMgr().getCurrentSummarySheet()));
        } catch (UnauthorizedException e) {
            System.out.println("An exception occurred: " + e.getMessage());
        }
    }
}
