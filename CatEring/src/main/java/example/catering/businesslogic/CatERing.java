package example.catering.businesslogic;

import catering.businesslogic.event.EventManager;
import catering.businesslogic.menu.MenuManager;
import catering.businesslogic.procedure.CookingProcedureManager;
import catering.businesslogic.shifts.ShiftManager;
import catering.businesslogic.task.KitchenTaskManager;
import catering.businesslogic.user.UserManager;
import catering.persistence.KitchenTaskPersistence;
import catering.persistence.MenuPersistence;

public class CatERing {
    private static CatERing singleInstance;
    private final MenuManager menuMgr;
    private final CookingProcedureManager procedureMgr;
    private final UserManager userMgr;
    private final EventManager eventMgr;
    private final KitchenTaskManager kitchenTaskMgr;
    private final ShiftManager shiftMgr;
    private final MenuPersistence menuPersistence;
    private final KitchenTaskPersistence taskPersistence;

    private CatERing() {

        menuMgr = new MenuManager();
        procedureMgr = new CookingProcedureManager();
        userMgr = new UserManager();
        eventMgr = new EventManager();
        kitchenTaskMgr = new KitchenTaskManager();
        shiftMgr = new ShiftManager();



        menuPersistence = new MenuPersistence();
        menuMgr.addEventReceiver(menuPersistence);
        taskPersistence = new KitchenTaskPersistence();
        kitchenTaskMgr.addEventReceiver(taskPersistence);
    }

    public static CatERing getInstance() {
        if (singleInstance == null) {
            singleInstance = new CatERing();
        }
        return singleInstance;
    }

    public MenuManager getMenuManager() {
        return menuMgr;
    }

    public CookingProcedureManager getProcedureManager() {
        return procedureMgr;
    }

    public UserManager getUserManager() {
        return userMgr;
    }

    public EventManager getEventManager() {
        return eventMgr;
    }

    public KitchenTaskManager getKitchenTaskMgr() {
        return kitchenTaskMgr;
    }

    public ShiftManager getShiftMgr() {
        return shiftMgr;
    }
}
