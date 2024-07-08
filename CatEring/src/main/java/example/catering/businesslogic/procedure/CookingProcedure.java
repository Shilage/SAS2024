package example.catering.businesslogic.procedure;

import example.catering.persistence.PersistenceManager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public abstract class CookingProcedure {
    private static final Map<Integer, CookingProcedure> allCookingProcedures = new HashMap<>();
    private static final Map<Integer, Preparation> allPreparations = new HashMap<>();
    private static final Map<Integer, Recipe> allRecipes = new HashMap<>();
    protected int id;
    protected int foreignKeyId;
    protected String name;
    protected ProcedureType type;

    public CookingProcedure() {
    }

    public CookingProcedure(int id, int foreignKeyId, String name, ProcedureType type) {
        this.id = id;
        this.foreignKeyId = foreignKeyId;
        this.name = name;
        this.type = type;
    }


    // STATIC FOR PERSISTENCE
    public static ArrayList<CookingProcedure> loadAllProcedures() {
        String query = "SELECT * FROM CookingProcedures";

        PersistenceManager.executeQuery(query, rs -> {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            String type = rs.getString("type");

            if (allCookingProcedures.containsKey(id)) {
                CookingProcedure procedure = allCookingProcedures.get(id);
                procedure.setName(rs.getString("name"));
            } else {
                CookingProcedure procedure;

                if (type.equals("preparation")) {
                    procedure = new Preparation(name, id, rs.getInt("fk_referenced_preparation"));
                    allPreparations.put(procedure.getForeignKeyId(), (Preparation) procedure);
                } else {
                    procedure = new Recipe(name, id, rs.getInt("fk_referenced_recipe"));
                    loadPreparationsForRecipe((Recipe) procedure);
                    allRecipes.put(procedure.getForeignKeyId(), (Recipe) procedure);
                }

                allCookingProcedures.put(procedure.getId(), procedure);
            }
        });

        ArrayList<CookingProcedure> proc = new ArrayList<>(allCookingProcedures.values());
        proc.sort(Comparator.comparing(CookingProcedure::getName));

        return proc;
    }

    private static void loadPreparationsForRecipe(Recipe recipe) {
        int recipeId = recipe.getId();

        String preparationQuery = "SELECT p.id, cp.name, cp.type, cp.fk_referenced_preparation " +
                "FROM Preparations p " +
                "JOIN RecipePreparations rp ON p.id = rp.preparation_id " +
                "JOIN CookingProcedures cp ON cp.id = p.id " +
                "WHERE rp.recipe_id = " + recipeId;

        PersistenceManager.executeQuery(preparationQuery, rs -> {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            int fkReferencedPreparation = rs.getInt("fk_referenced_preparation");

            // Check if the preparation already exists in the map and create a new one if it does not
            Preparation prep = allPreparations.containsKey(id) ?
                    allPreparations.get(id) :
                    new Preparation(name, id, fkReferencedPreparation);

            // Assuming a method to set or check other details if needed based on 'type' or other columns
            recipe.addPreparation(prep);
        });
    }

    public static CookingProcedure loadCookingProcedureById(int id) {
        if (allCookingProcedures.containsKey(id)) return allCookingProcedures.get(id);
        String query = "SELECT * FROM CookingProcedures WHERE id = " + id;
        PersistenceManager.executeQuery(query, rs -> {
            CookingProcedure proc;
            String name = rs.getString("name");
            if (rs.getString("type").equals("preparation"))
                proc = new Preparation(name, id, rs.getInt("fk_referenced_preparation"));
            else proc = new Recipe(name, id, rs.getInt("fk_referenced_preparation"));
            allCookingProcedures.put(proc.getId(), proc);
        });
        return allCookingProcedures.get(id);
    }

    public static Recipe loadRecipeById(int id) {
        if (allRecipes.containsKey(id)) return allRecipes.get(id);

        Recipe rec = new Recipe();
        // SQL query to load the recipe from the database
        String recipeQuery = "SELECT * FROM CookingProcedures WHERE id = " + id;
        PersistenceManager.executeQuery(recipeQuery, rs -> {
            rec.setId(rs.getInt("id"));
            rec.setName(rs.getString("name"));
            rec.setForeignKeyId(rs.getInt("fk_referenced_recipe"));
            allRecipes.put(rec.getForeignKeyId(), rec);
        });

        // SQL query to load the preparations associated with this recipe
        String preparationQuery = "SELECT p.* FROM Preparations p " +
                "JOIN RecipePreparations rp ON p.id = rp.preparation_id " +
                "WHERE rp.recipe_id = " + id;

        PersistenceManager.executeQuery(preparationQuery, rs -> {
            int prepId = rs.getInt("id");
            Preparation prep = allPreparations.get(prepId);
            rec.addPreparation(prep);
        });
        return rec;
    }

    public static Preparation loadPreparationById(int id) {
        if (allPreparations.containsKey(id)) return allPreparations.get(id);
        Preparation prep = new Preparation();
        String query = "SELECT * FROM Preparation WHERE id = " + id;
        PersistenceManager.executeQuery(query, rs -> {
            prep.setName(rs.getString("name"));
            prep.setForeignKeyId(id);
            allPreparations.put(prep.getForeignKeyId(), prep);
        });
        return prep;
    }

    public static ArrayList<CookingProcedure> getAllProcedures() {
        return new ArrayList<>(allCookingProcedures.values());
    }

    public static ArrayList<Preparation> getAllPreparations() {
        return new ArrayList<>(allPreparations.values());
    }

    public static ArrayList<Recipe> getAllRecipes() {
        return new ArrayList<>(allRecipes.values());
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int newId) {
        this.id = newId;
    }

    public int getForeignKeyId() {
        return this.foreignKeyId;
    }

    public void setForeignKeyId(int foreignKeyId) {
        this.foreignKeyId = foreignKeyId;
    }

    @Override
    public abstract boolean equals(Object obj);

    @Override
    public abstract String toString();
}
