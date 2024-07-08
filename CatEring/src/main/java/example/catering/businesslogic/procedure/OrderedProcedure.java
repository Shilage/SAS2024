package example.catering.businesslogic.procedure;

public class OrderedProcedure extends CookingProcedure {
    private final CookingProcedure baseProcedure;
    private int position;

    public OrderedProcedure(CookingProcedure baseProcedure, int position) {
        this.baseProcedure = baseProcedure;
        this.position = position;
    }

    public CookingProcedure getBaseProcedure() {
        return baseProcedure;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public String getName() {
        return baseProcedure.getName();
    }

    @Override
    public void setName(String name) {
        baseProcedure.setName(name);
    }

    @Override
    public int getId() {
        return baseProcedure.getId();
    }

    @Override
    public void setId(int newId) {
        baseProcedure.setId(newId);
    }

    @Override
    public int getForeignKeyId() {
        return baseProcedure.getForeignKeyId();
    }

    @Override
    public void setForeignKeyId(int foreignKeyId) {
        baseProcedure.setForeignKeyId(foreignKeyId);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof OrderedProcedure) {
            OrderedProcedure other = (OrderedProcedure) obj;
            return baseProcedure.equals(other.baseProcedure) && this.position == other.position;
        }
        return false;
    }

    @Override
    public String toString() {
        return "[" + baseProcedure.toString() + ", Position: " + position + "]";
    }
}