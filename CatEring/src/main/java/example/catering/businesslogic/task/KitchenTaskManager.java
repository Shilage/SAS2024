package example.catering.businesslogic.task;

import example.catering.businesslogic.CatERing;
import example.catering.businesslogic.errors.ItemNotFoundException;
import example.catering.businesslogic.errors.UnauthorizedException;
import example.catering.businesslogic.errors.UseCaseLogicException;
import example.catering.businesslogic.event.Service;
import example.catering.businesslogic.procedure.CookingProcedure;
import example.catering.businesslogic.procedure.OrderedProcedure;
import example.catering.businesslogic.shifts.KitchenShift;
import example.catering.businesslogic.shifts.Shift;
import example.catering.businesslogic.user.User;

import java.time.Duration;
import java.util.ArrayList;

public class KitchenTaskManager {
    private final ArrayList<TaskEventReceiver> eventReceivers;
    private SummarySheet currentSummarySheet;
    private Service currentService;
    private Task currentWorkingTask;

    public KitchenTaskManager() {
        SummarySheet.loadAllSummarySheets();
        eventReceivers = new ArrayList<>();
    }

    // todo quando generiamo un nuovo foglio riepilogativo bisogna controllare che quel servizio non abbia giò un foglio riepilogativo generato
    /**
     * DSD 1
     */
    public SummarySheet generateSummarySheet(Service service) throws UnauthorizedException, UseCaseLogicException {
        // preliminary checks
        checkUser();
        if (service.getUsedMenu() == null) throw new UseCaseLogicException("Specified service must have a menu");

        SummarySheet sheet = new SummarySheet(service.getUsedMenu().getAllRecipes());
        this.currentService = service;
        this.currentSummarySheet = sheet;

        this.notifySummarySheetCreated(sheet);

        return sheet;
    }

    /**
     * DSD 1a
     */
    public SummarySheet openSummarySheet(Service service) throws UnauthorizedException {
        checkUser();

        this.currentService = service;
        this.currentSummarySheet = service.getSummarySheet();

        return this.currentSummarySheet;
    }

    /**
     * DSD 2
     */
    public void addCookingProcedure(CookingProcedure procedure) throws UnauthorizedException, UseCaseLogicException {
        checkUser();

        if (this.currentSummarySheet == null) throw new UseCaseLogicException("No Summary Sheet specified");

        OrderedProcedure newProcedure = this.currentSummarySheet.addProcedure(procedure);

        this.notifyCookingProcedureAdded(newProcedure);
    }

    /**
     * DSD 3
     */
    public void orderSheet(CookingProcedure procedure, int position) throws UnauthorizedException, UseCaseLogicException, ItemNotFoundException {
        checkUser();

        if (this.currentSummarySheet == null) throw new UseCaseLogicException("No Summary Sheet specified");

        if (!this.currentSummarySheet.containsProcedure(procedure))
            throw new ItemNotFoundException("The selected procedure is not present in the " +
                    "selected summary sheet's listed procedures");

        OrderedProcedure newProcedure = this.currentSummarySheet.orderProcedure(procedure, position);
        this.notifyOrderedProcedureUpdated(newProcedure);
    }


    /**
     * DSD 4
     */
    public ArrayList<Shift> checkShiftBoard() throws UnauthorizedException {
        checkUser();

        return CatERing.getInstance().getShiftMgr().getShiftsFromService(this.currentService);
    }

    /**
     * DCD 5
     */
    public Task assignCookingProcedure(CookingProcedure procedure, KitchenShift shift, User cook) throws UnauthorizedException, UseCaseLogicException {
        checkUser();

        if (this.currentSummarySheet == null) throw new UseCaseLogicException("No Summary Sheet specified");

        if (cook != null) {
            boolean cookAvailable = CatERing.getInstance().getShiftMgr().isAvailable(cook, shift);
            if (!cookAvailable)
                throw new UseCaseLogicException(cook + " is not available");
            if (!cook.isCook())
                throw new UseCaseLogicException(cook + " is not actually a cook");
        }

        boolean isAssigned = this.currentSummarySheet.isAlreadyAssigned(procedure);
        if (isAssigned)
            throw new UseCaseLogicException(procedure + "is already assigned");

        this.currentWorkingTask = this.currentSummarySheet.addAssignment(procedure, shift, cook);

        this.notifyTaskCreated(this.currentWorkingTask);

        return this.currentWorkingTask;
    }

    /**
     * DSD 5a
     */
    public Task markCookingProcedureAsDone(Task task) throws UnauthorizedException, UseCaseLogicException, ItemNotFoundException{
        checkUser();

        if (this.currentSummarySheet == null) throw new UseCaseLogicException("No Summary Sheet specified");

        this.currentSummarySheet.markCookingProcedureAsDone(task);

        return task;
    }

    /**
     * DSD 5b
     */
    public Task modifyTask(Task task, CookingProcedure procedure, KitchenShift shift, User cook) throws UnauthorizedException, UseCaseLogicException, ItemNotFoundException {
        checkUser();

        if (this.currentSummarySheet == null) throw new UseCaseLogicException("No Summary Sheet specified");
        if (!this.currentSummarySheet.getTasks().contains(task)) throw new UseCaseLogicException("The specified task is not contained in the Summary Sheet you're working on");
        if (cook != null && !cook.isCook()) throw  new UseCaseLogicException("The specified user in not a cook");

        if (cook != null && shift != null){
            if(!CatERing.getInstance().getShiftMgr().isAvailable(cook, shift)) throw new UseCaseLogicException(cook + " is not available for the specified shift");
        }

        if (cook != null && shift == null){
            if(!CatERing.getInstance().getShiftMgr().isAvailable(cook, task.getShift())) throw new UseCaseLogicException(cook + " is not available for the specified shift");
        }

        if (procedure != null){
            if (this.currentSummarySheet.isAlreadyAssigned(procedure)) throw new UseCaseLogicException(procedure + " is already assigned in another task");
        }

        this.currentSummarySheet.modifyTask(task, procedure, shift, cook);

        notifyTaskUpdated(task);

        return task;
    }


    /**
     * DSD 5c
     */
    public void deleteTask(Task task) throws UnauthorizedException, UseCaseLogicException, ItemNotFoundException {
        checkUser();

        if (this.currentSummarySheet == null) throw new UseCaseLogicException("No Summary Sheet specified");
        if (!this.currentSummarySheet.getTasks().contains(task)) throw new UseCaseLogicException("The specified task is not contained in the Summary Sheet you're working on");

        this.currentSummarySheet.deleteTask(task);

        this.notifyTaskDeleted(task);
    }

    /**
     * DSD 5d
     */
    public Task modifyEstimatedTime(Task task, Duration newEstimate) throws UnauthorizedException, UseCaseLogicException, ItemNotFoundException {
        checkUser();

        if (this.currentSummarySheet == null) throw new UseCaseLogicException("No Summary Sheet specified");

        this.currentSummarySheet.modifyEstimatedTime(task, newEstimate);

        this.notifyTaskUpdated(task);

        return task;
    }

    /**
     * DSD 5e
     */
    public Task modifyQuantities(Task task, String newAmount, String newDoses) throws UnauthorizedException, UseCaseLogicException, ItemNotFoundException {
        checkUser();

        if (this.currentSummarySheet == null) throw new UseCaseLogicException("No Summary Sheet specified");

        this.currentSummarySheet.modifyQuantities(task, newAmount, newDoses);

        this.notifyTaskUpdated(task);

        return task;
    }

    /**
     * DSD 6
     */
    public Task markAsContinuation(Task initialTask) throws UnauthorizedException, UseCaseLogicException, ItemNotFoundException {
        checkUser();

        if (this.currentSummarySheet == null) throw new UseCaseLogicException("No Summary Sheet specified");
        if (this.currentWorkingTask == null) throw new UseCaseLogicException("No task is under assignment");

        this.currentSummarySheet.markAsContinuation(this.currentWorkingTask, initialTask);

        this.notifyTaskUpdated(this.currentWorkingTask);

        return this.currentWorkingTask;
    }

    /**
     * DSD 7
     */
    public Task addTaskInfo(Duration estimate, String amount, String doses) throws UnauthorizedException, UseCaseLogicException, ItemNotFoundException {
        checkUser();

        if (this.currentSummarySheet == null) throw new UseCaseLogicException("No Summary Sheet specified");
        if (this.currentWorkingTask == null) throw new UseCaseLogicException("No task is under assignment");

        this.currentSummarySheet.addTaskInfo(this.currentWorkingTask, estimate, amount, doses);

        return this.currentWorkingTask;
    }


    private void checkUser() throws UnauthorizedException {
        User u = CatERing.getInstance().getUserManager().getCurrentUser();
        if (u == null || !u.isChef()) throw new UnauthorizedException("User must be authenticated as Chef");
        if (this.currentService != null && !this.currentService.isChefAssigned(u))
            throw new UnauthorizedException("The selected service is not assigned to: " + u + ", therefore it is impossible to proceed");
    }

    public void addEventReceiver(TaskEventReceiver receiver) {
        eventReceivers.add(receiver);
    }

    public void removeEventReceiver(TaskEventReceiver receiver) {
        eventReceivers.remove(receiver);
    }

    public ArrayList<SummarySheet> getAllSummarySheets() {
        return SummarySheet.getAllSummarySheets();
    }

    public SummarySheet getCurrentSummarySheet() {
        return currentSummarySheet;
    }

    public Service getCurrentService() {
        return currentService;
    }

    public Task getCurrentWorkingTask() {
        return this.currentWorkingTask;
    }

    private void notifySummarySheetCreated(SummarySheet sheet) {
        for (TaskEventReceiver er : this.eventReceivers) {
            er.updateSummarySheetCreated(sheet);
        }
    }

    private void notifyCookingProcedureAdded(OrderedProcedure procedure) {
        for (TaskEventReceiver er : this.eventReceivers) {
            er.updateCookingProcedureAdded(currentSummarySheet, procedure);
        }
    }

    private void notifyOrderedProcedureUpdated(OrderedProcedure procedure) {
        for (TaskEventReceiver receiver : this.eventReceivers) {
            receiver.updateOrderedProcedurePosition(currentSummarySheet, procedure);
        }
    }

    private void notifyTaskCreated(Task task) {
        for (TaskEventReceiver er : this.eventReceivers) {
            er.updateTaskCreated(currentSummarySheet, task);
        }
    }

    private void notifyTaskUpdated(Task task) {
        for (TaskEventReceiver er : this.eventReceivers) {
            er.updateTaskUpdated(currentSummarySheet, task);
        }
    }

    private void notifyTaskDeleted(Task task) {
        for (TaskEventReceiver er : this.eventReceivers) {
            er.updateTaskDeleted(currentSummarySheet, task);
        }
    }
}
