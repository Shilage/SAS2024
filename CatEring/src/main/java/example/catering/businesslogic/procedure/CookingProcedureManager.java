package example.catering.businesslogic.procedure;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class CookingProcedureManager {

    public CookingProcedureManager() {
        CookingProcedure.loadAllProcedures();
    }

    public ArrayList<CookingProcedure> getCookingProcedures() {
        return CookingProcedure.getAllProcedures();
    }

    public ArrayList<CookingProcedure> getProcedures() {
        return CookingProcedure.getAllProcedures();
    }

    public ArrayList<Recipe> getRecipes() {
        return CookingProcedure.getAllRecipes();
    }

    public ArrayList<Preparation> getPreparations() {
        return CookingProcedure.getAllPreparations();
    }
}
