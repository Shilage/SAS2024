package example.catering.businesslogic.user;

import catering.persistence.PersistenceManager;
import catering.persistence.ResultHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class User {

    private static final Map<Integer, User> loadedUsers = new HashMap<Integer, User>();
    private final Set<Role> roles;
    private int id;
    private String username;

    public User() {
        id = 0;
        username = "";
        this.roles = new HashSet<>();
    }

    public static User loadUserById(int uid) {
        if (loadedUsers.containsKey(uid)) return loadedUsers.get(uid);

        User load = new User();
        String userQuery = "SELECT * FROM Users WHERE id='" + uid + "'";
        PersistenceManager.executeQuery(userQuery, rs -> {
            load.id = rs.getInt("id");
            load.username = rs.getString("username");
        });

        if (load.id > 0) {
            loadedUsers.put(load.id, load);
            String roleQuery = "SELECT * FROM UserRoles WHERE user_id=" + load.id;
            PersistenceManager.executeQuery(roleQuery, new ResultHandler() {
                @Override
                public void handle(ResultSet rs) throws SQLException {
                    String role = rs.getString("role_id");
                    switch (role.charAt(0)) {
                        case 'c':
                            load.roles.add(Role.COOK);
                            break;
                        case 'h':
                            load.roles.add(Role.CHEF);
                            break;
                        case 'o':
                            load.roles.add(Role.EVENT_PLANNER);
                            break;
                        case 's':
                            load.roles.add(Role.SERVIZIO);
                    }
                }
            });
        }
        return load;
    }

    public static User loadUser(String username) {
        User u = new User();
        String userQuery = "SELECT * FROM Users WHERE username='" + username + "'";

        PersistenceManager.executeQuery(userQuery, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                u.id = rs.getInt("id");
                u.username = rs.getString("username");
            }
        });
        if (u.id > 0) {
            loadedUsers.put(u.id, u);
            String roleQuery = "SELECT * FROM UserRoles WHERE user_id=" + u.id;
            PersistenceManager.executeQuery(roleQuery, new ResultHandler() {
                @Override
                public void handle(ResultSet rs) throws SQLException {
                    String role = rs.getString("role_id");
                    switch (role.charAt(0)) {
                        case 'c':
                            u.roles.add(Role.COOK);
                            break;
                        case 'h':
                            u.roles.add(Role.CHEF);
                            break;
                        case 'o':
                            u.roles.add(Role.EVENT_PLANNER);
                            break;
                        case 's':
                            u.roles.add(Role.SERVIZIO);
                    }
                }
            });
        }
        return u;
    }

    public boolean isChef() {
        return roles.contains(Role.CHEF);
    }

    public boolean isCook() {
        return roles.contains(Role.COOK);
    }

    public String getUserName() {
        return username;
    }

    public int getId() {
        return this.id;
    }

    // STATIC METHODS FOR PERSISTENCE

    public String toString() {
        String result = username;
        if (roles.size() > 0) {
            result += ": ";

            for (Role r : roles) {
                result += r.toString() + " ";
            }
        }
        return result;
    }

    public enum Role {
        SERVIZIO,
        COOK,
        CHEF,
        EVENT_PLANNER
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != user.id) return false;
        if (!username.equals(user.username)) return false;
        return roles.equals(user.roles);
    }
}
