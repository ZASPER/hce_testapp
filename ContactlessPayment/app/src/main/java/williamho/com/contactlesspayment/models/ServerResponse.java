package williamho.com.contactlesspayment.models;

/**
 * Created by williamho on 12/08/16.
 */

public class ServerResponse {

    private String result;
    private String message;
    private User user;

    public String getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }
}
