package example.catering.businesslogic.task;

import catering.businesslogic.procedure.OrderedProcedure;

public interface TaskEventReceiver {
    void updateSummarySheetCreated(SummarySheet sheet);

    void updateCookingProcedureAdded(SummarySheet sheet, OrderedProcedure p);

    void updateOrderedProcedurePosition(SummarySheet sheet, OrderedProcedure p);

    void updateTaskCreated(SummarySheet sheet, Task t);

    void updateTaskUpdated(SummarySheet sheet, Task t);

    void updateTaskDeleted(SummarySheet sheet, Task t);
}