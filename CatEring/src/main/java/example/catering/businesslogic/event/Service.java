package example.catering.businesslogic.event;

import example.catering.businesslogic.menu.Menu;
import example.catering.businesslogic.task.SummarySheet;
import example.catering.businesslogic.user.User;
import example.catering.persistence.PersistenceManager;
import example.catering.persistence.ResultHandler;

import java.util.ArrayList;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;

public class Service {
    private String name;
    private int id;
    private Date date;
    private Time timeStart;
    private Time timeEnd;
    private int participants;
    private Menu usedMenu;
    private User chef;
    private SummarySheet summarySheet;

    public Service(String name) {
        this.name = name;
    }

    public Service() {
    }

    public static ArrayList<Service> loadServiceInfoForEvent(int event_id) {
        ArrayList<Service> result = new ArrayList<>();
        String query = "SELECT * " +
                "FROM Services WHERE event_id = " + event_id;
        PersistenceManager.executeQuery(query, rs -> {
            String s = rs.getString("name");
            Service serv = new Service(s);
            setUpService(serv, rs);
            result.add(serv);
        });

        return result;
    }

    public static Service loadById(int id) {
        String query = "SELECT * " +
                "FROM Services WHERE id = " + id;
        Service serv = new Service();
        PersistenceManager.executeQuery(query, rs -> {
            String s = rs.getString("name");
            serv.name = s;
            setUpService(serv, rs);
        });

        return serv;
    }

    private static void setUpService(Service serv, ResultSet rs) throws SQLException {
        serv.id = rs.getInt("id");
        serv.date = rs.getDate("service_date");
        serv.timeStart = rs.getTime("time_start");
        serv.timeEnd = rs.getTime("time_end");
        serv.participants = rs.getInt("expected_participants");
        serv.chef = User.loadUserById(rs.getInt("chef_id"));
        serv.usedMenu = Menu.loadById(rs.getInt("used_menu_id"));
        serv.summarySheet = SummarySheet.loadById(rs.getInt("summary_sheet_id"));
    }

    public boolean isChefAssigned(User chef) {
        return this.chef.equals(chef);
    }

    public User getChef() {
        return chef;
    }

    public void setChef(User chef) {
        this.chef = chef;
    }

    public Menu getUsedMenu() {
        return usedMenu;
    }

    public void setUsedMenu(Menu usedMenu) {
        this.usedMenu = usedMenu;
    }

    public int getId() {
        return id;
    }

    public SummarySheet getSummarySheet() {
        return summarySheet;
    }

    public void setSummarySheet(SummarySheet summarySheet) {
        this.summarySheet = summarySheet;
    }

    public String toString() {
        return name + ": " + date + " (" + timeStart + "-" + timeEnd + "), " + participants + " pp.";
    }
}
