package example.catering.businesslogic.errors;

public class UseCaseLogicException extends Exception {

    public UseCaseLogicException(){
        super();
    }
    public UseCaseLogicException(String message) {
        super(message);
    }
}
