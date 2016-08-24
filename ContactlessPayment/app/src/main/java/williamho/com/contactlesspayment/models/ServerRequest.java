package williamho.com.contactlesspayment.models;

/**
 * Created by williamho on 12/08/16.
 */

public class ServerRequest {

    private String operation;
    private User user;

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public void setUser(User user) {
        this.user = user;
    }
}