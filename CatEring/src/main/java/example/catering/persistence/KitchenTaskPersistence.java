package example.catering.persistence;

import example.catering.businesslogic.procedure.OrderedProcedure;
import example.catering.businesslogic.task.SummarySheet;
import example.catering.businesslogic.task.Task;
import example.catering.businesslogic.task.TaskEventReceiver;

public class KitchenTaskPersistence implements TaskEventReceiver {
    @Override
    public void updateSummarySheetCreated(SummarySheet sheet) {
        for (OrderedProcedure procedure : sheet.getListedOrderedProcedures()) {
            String procedureQuery = "INSERT INTO ListedProcedures (summary_sheet_id, procedure_id, position) VALUES ("
                    + sheet.getId() + ", "
                    + procedure.getId() + ", "
                    + procedure.getPosition() + ")";
            PersistenceManager.executeUpdate(procedureQuery);
        }
    }

    @Override
    public void updateCookingProcedureAdded(SummarySheet sheet, OrderedProcedure p) {
        String procedureQuery = "INSERT INTO ListedProcedures (summary_sheet_id, procedure_id, position) VALUES ("
                + sheet.getId() + ", "
                + p.getId() + ", "
                + p.getPosition() + ")";

        PersistenceManager.executeUpdate(procedureQuery);
    }

    @Override
    public void updateOrderedProcedurePosition(SummarySheet sheet, OrderedProcedure p) {
        String procedureQuery = "UPDATE ListedProcedures SET position = " + p.getPosition() +
                " WHERE summary_sheet_id = " + sheet.getId() +
                " AND procedure_id = " + p.getId();
        PersistenceManager.executeUpdate(procedureQuery);
    }

    @Override
    public void updateTaskCreated(SummarySheet sheet, Task t) {
        String insertQuery = "INSERT INTO Tasks (cooking_procedure_id, cook_id, shift_id, initial_task, time_to_complete, completed, amount, doses, to_prepare) VALUES ("
                + t.getProcedure().getId() + ", "
                + (t.getCook() != null ? t.getCook().getId() : "NULL") + ", "
                + (t.getShift() != null ? t.getShift().getId() : "NULL") + ", "
                + (t.getInitialTask() != null ? t.getInitialTask().getId() : "NULL") + ", "
                + (t.getTimeToComplete() != null ? "'" + t.getTimeToComplete().toString() + "'" : "NULL") + ", "
                + t.isCompleted() + ", "
                + (t.getAmount() != null ? "'" + t.getAmount() + "'" : "NULL") + ", "
                + (t.getDoses() != null ? "'" + t.getDoses() + "'" : "NULL") + ", "
                + t.getToPrepare()
                + ")";
        PersistenceManager.executeUpdate(insertQuery);
    }

    @Override
    public void updateTaskUpdated(SummarySheet sheet, Task t) {
        String updateQuery = "UPDATE Tasks SET "
                + "cooking_procedure_id = " + t.getProcedure().getId() + ", "
                + "cook_id = " + (t.getCook() != null ? t.getCook().getId() : "NULL") + ", "
                + "shift_id = " + (t.getShift() != null ? t.getShift().getId() : "NULL") + ", "
                + "initial_task = " + (t.getInitialTask() != null ? t.getInitialTask().getId() : "NULL") + ", "
                + "time_to_complete = " + (t.getTimeToComplete() != null ? "'" + t.getTimeToComplete().toString() + "'" : "NULL") + ", "
                + "completed = " + t.isCompleted() + ", "
                + "amount = " + (t.getAmount() != null ? "'" + t.getAmount() + "'" : "NULL") + ", "
                + "doses = " + (t.getDoses() != null ? "'" + t.getDoses() + "'" : "NULL") + ", "
                + "to_prepare = " + t.getToPrepare()
                + " WHERE id = " + t.getId();
        PersistenceManager.executeUpdate(updateQuery);
    }

    @Override
    public void updateTaskDeleted(SummarySheet sheet, Task t) {
        String deleteQuery = "DELETE FROM Tasks WHERE id = " + t.getId();
        PersistenceManager.executeUpdate(deleteQuery);
    }
}
