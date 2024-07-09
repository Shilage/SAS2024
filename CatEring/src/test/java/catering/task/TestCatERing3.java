package catering.task;

import example.catering.businesslogic.CatERing;
import example.catering.businesslogic.errors.ItemNotFoundException;
import example.catering.businesslogic.errors.UnauthorizedException;
import example.catering.businesslogic.errors.UseCaseLogicException;
import example.catering.businesslogic.event.Service;
import example.catering.businesslogic.procedure.CookingProcedure;
import example.catering.businesslogic.procedure.OrderedProcedure;
import example.catering.businesslogic.task.KitchenTaskManager;
import example.catering.businesslogic.task.SummarySheet;
import example.catering.persistence.PersistenceManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TestCatERing3 {
    public static void main(String[] args) {
        try {
            PersistenceManager.executeSqlFile("catering_nofx/database/catering_db_init.sql");

            CatERing.getInstance().getUserManager().fakeLogin("Eva");
            Service service = CatERing.getInstance().getEventManager().getServiceById(1);

            // Opening the associated summary sheet
            SummarySheet summarySheet = CatERing.getInstance().getKitchenTaskMgr().openSummarySheet(service);
            // Fetching the current list of OrderedProcedures
            List<OrderedProcedure> orderedProcedures = summarySheet.getListedOrderedProcedures();

            System.out.println("Procedures before reordering:");
            for (OrderedProcedure proc : orderedProcedures) {
                System.out.println(proc.getBaseProcedure().getName() + " is now at position " + proc.getPosition());
            }

            // Implementing random reordering for testing
            Collections.shuffle(orderedProcedures);
            KitchenTaskManager taskManager = CatERing.getInstance().getKitchenTaskMgr();

            for (int i = 0; i < orderedProcedures.size(); i++) {
                // Ordering each procedure in the new shuffled order
                taskManager.orderSheet(orderedProcedures.get(i).getBaseProcedure(), i);
            }

            // verifying positions
            System.out.println("Reordered Procedures:");
            for (OrderedProcedure proc : orderedProcedures) {
                System.out.println(proc.getBaseProcedure().getName() + " is now at position " + proc.getPosition());
            }

        } catch (UnauthorizedException | UseCaseLogicException | ItemNotFoundException e) {
            System.out.println("An exception occurred: " + e.getMessage());
        }
    }
}
