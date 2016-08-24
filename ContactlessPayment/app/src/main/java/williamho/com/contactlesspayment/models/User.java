package williamho.com.contactlesspayment.models;

/**
 * Created by williamho on 12/08/16.
 */

public class User {

    private String name;
    private String email;
    private String card1;
    private String card1_balance;
    private String unique_id;
    private String password;
    private String old_password;
    private String new_password;


    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getCard1() {
        return card1;
    }

    public String getCard1_balance() {
        return card1_balance;
    }

    public String getUnique_id() {
        return unique_id;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCard1(String card1) {
        this.card1 = card1;
    }

    public void setCard1_balance(String card1_balance) {
        this.card1_balance = card1_balance;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setOld_password(String old_password) {
        this.old_password = old_password;
    }

    public void setNew_password(String new_password) {
        this.new_password = new_password;
    }

}
