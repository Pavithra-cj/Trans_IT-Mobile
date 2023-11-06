package lk.nibm.furious5.scorpio.transit.Model;

import org.json.JSONException;
import org.json.JSONObject;

public class User {

    private String firstName;
    private String lastName;
    private String email;
    private String mobile;
    private String password;
    private String confirm_password;

    public User(String firstName, String lastName, String email, String mobile, String password, String confirm_password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.mobile = mobile;
        this.password = password;
        this.confirm_password = confirm_password;
    }

    public JSONObject toJsonObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("first_name", firstName);
            jsonObject.put("last_name", lastName);
            jsonObject.put("email", email);
            jsonObject.put("mobile", mobile);
            jsonObject.put("password", password);
            jsonObject.put("password_confirmation", confirm_password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

}
