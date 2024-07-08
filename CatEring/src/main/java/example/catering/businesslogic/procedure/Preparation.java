package example.catering.businesslogic.procedure;


public class Preparation extends CookingProcedure {

    public Preparation() {
    }

    public Preparation(String name, int cookingProcedureId, int preparationId) {
        super(cookingProcedureId, preparationId, name, ProcedureType.PREPARATION);
    }


    @Override
    public boolean equals(Object obj) {
        return obj instanceof Preparation &&
                ((Preparation) obj).id == id &&
                ((Preparation) obj).foreignKeyId == this.foreignKeyId;
    }

    public String toString() {
        return String.format("{Preparation, %s, %d, %d}", name, id, foreignKeyId);
    }

}
