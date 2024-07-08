package example.catering.businesslogic.procedure;

import example.catering.persistence.PersistenceManager;
import example.catering.persistence.ResultHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Recipe extends CookingProcedure {

    private ArrayList<Preparation> preparations;

    public Recipe() {
        preparations = new ArrayList<>();
    }

    public Recipe(String name, int cookingProcedureId, int recipeId) {
        super(cookingProcedureId, recipeId, name, ProcedureType.RECIPE);
        this.preparations = new ArrayList<>();
    }

    public void addPreparation (Preparation preparation) {
        this.preparations.add(preparation);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Recipe &&
                ((Recipe) obj).id == this.id &&
                ((Recipe) obj).foreignKeyId == this.foreignKeyId;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("{Recipe, %s, %d, %d, Preparations: [", name, id, foreignKeyId));
        for (Preparation preparation : preparations) {
            sb.append(preparation.toString());
            sb.append(", "); // Add comma to separate each preparation
        }
        if (!preparations.isEmpty()) {
            sb.setLength(sb.length() - 2); // Remove the last comma and space
        }
        sb.append("]}");
        return sb.toString();
    }
}
