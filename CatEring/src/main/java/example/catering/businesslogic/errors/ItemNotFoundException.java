package example.catering.businesslogic.errors;

public class ItemNotFoundException extends Exception {
    public ItemNotFoundException(){super();}
    public ItemNotFoundException(String message){
        super(message);
    }
}
