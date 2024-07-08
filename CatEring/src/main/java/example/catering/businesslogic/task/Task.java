package example.catering.businesslogic.task;

import example.catering.businesslogic.procedure.CookingProcedure;
import example.catering.businesslogic.procedure.Preparation;
import example.catering.businesslogic.procedure.Recipe;
import example.catering.businesslogic.shifts.KitchenShift;
import example.catering.businesslogic.user.User;
import example.catering.persistence.PersistenceManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import java.time.Duration;


public class Task {

    private static Map<Integer, Task> allTasks = new HashMap<>();
    int id;
    private Duration timeToComplete;
    private boolean completed;
    private Boolean toPrepare;
    private String amount;
    private String doses;
    private CookingProcedure procedure;
    private User cook;
    private KitchenShift shift;

    private Task initialTask;

    public Task(CookingProcedure procedure, KitchenShift shift, User cook) {
        this.id = 0;
        this.procedure = procedure;
        this.timeToComplete = Duration.ZERO;
        this.shift = shift;
        this.cook = cook;
        this.completed = false;
        this.toPrepare = true;
    }

    public Task(CookingProcedure procedure, KitchenShift shift) {
        this.id = 0;
        this.procedure = procedure;
        this.shift = shift;
    }

    // Getters and Setters


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Duration getTimeToComplete() {
        return timeToComplete;
    }

    public void setTimeToComplete(Duration timeToComplete) {
        this.timeToComplete = timeToComplete;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
        this.toPrepare = !completed;
    }

    public Boolean getToPrepare() {
        return toPrepare;
    }

    public void setToPrepare(Boolean toPrepare) {
        this.toPrepare = toPrepare;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDoses() {
        return doses;
    }

    public void setDoses(String doses) {
        this.doses = doses;
    }

    public CookingProcedure getProcedure() {
        return procedure;
    }

    public void setProcedure(CookingProcedure procedure) {
        this.procedure = procedure;
    }

    public User getCook() {
        return cook;
    }

    public void setCook(User cook) {
        this.cook = cook;
    }

    public KitchenShift getShift() {
        return shift;
    }

    public void setShift(KitchenShift shift) {
        this.shift = shift;
    }

    public Task getInitialTask() {
        return initialTask;
    }

    public void setInitialTask(Task initialTask) {
        this.initialTask = initialTask;
    }

    // PERSISTENCE PART

    public static ArrayList<Task> loadAllTasks(){
        String query = "SELECT * FROM Tasks";

        PersistenceManager.executeQuery(query, rs -> {
            int id = rs.getInt("id");
            if (!allTasks.containsKey(id)){
                taskSetUp(id, rs);
            }
        });

        ArrayList<Task> tasks = new ArrayList<>(allTasks.values());
        tasks.sort(Comparator.comparing(Task::getTimeToComplete));

        return tasks;
    }

    public static Task loadTaskById(int id){
        if (!allTasks.containsKey(id)) {
            String query = "SELECT * FROM Tasks WHERE id = " + id;
            PersistenceManager.executeQuery(query, rs -> {
                taskSetUp(id, rs);
            });
        }
        return allTasks.get(id);
    }

    private static void taskSetUp(int id, ResultSet rs) throws SQLException {
        CookingProcedure proc = CookingProcedure.loadCookingProcedureById(rs.getInt("cooking_procedure_id"));
        User user = User.loadUserById(rs.getInt("cook_id"));
        Task task = new Task(proc, null, user);
        task.setCompleted(rs.getBoolean("completed"));

        String time = rs.getString("time_to_complete");
        if (time != null)
            task.setTimeToComplete(Duration.parse(time));
        String amount = rs.getString("amount");
        if (time != null)
            task.setAmount(amount);
        String doses = rs.getString("doses");
        if (time != null)
            task.setDoses(doses);

        allTasks.put(id, task);
    }

    @Override
    public String toString() {
        return "Task{\n" +
                "\tid=" + id +
                "\t, timeToComplete=" + timeToComplete + "\n" +
                "\t, completed=" + completed + "\n" +
                "\t, toPrepare=" + toPrepare + "\n" +
                "\t, amount='" + amount + '\'' + "\n" +
                "\t, doses='" + doses + '\'' + "\n" +
                "\t, procedure=" + procedure + "\n" +
                "\t, cook=" + cook + "\n" +
                "\t, shift=" + shift + "\n" +
                "\t, initialTask=" + initialTask + "\n" +
                '}';
    }
}
