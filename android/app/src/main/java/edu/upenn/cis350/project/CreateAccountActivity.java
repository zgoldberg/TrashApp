package edu.upenn.cis350.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.graphics.Color;
import android.widget.TextView;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import android.util.Log;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;


public class CreateAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        // set username to whatever was typed in last activity
        GlobalInfo info = GlobalInfo.getInstance();

        if (info.getEditAccountMode()) { // if the flag is true, populate the fields and make a new account

            info.setEditAccountMode(false);

            EditText usernameField = (EditText) findViewById(R.id.username_field);
            EditText fullNameField = (EditText) findViewById(R.id.full_name_field);
            EditText zipCodeField = (EditText) findViewById(R.id.zip_code_field);
            EditText passwordField = (EditText) findViewById(R.id.password_field);
            EditText reEnterPasswordField = (EditText) findViewById(R.id.reenter_password_field);

            usernameField.setText(info.getLoggedInUser().getUsername());
            fullNameField.setText(info.getLoggedInUser().getFullName());
            zipCodeField.setText(info.getLoggedInUser().getZip());
            passwordField.setVisibility(View.GONE);
            reEnterPasswordField.setVisibility(View.GONE);

            Button createButton = (Button) findViewById(R.id.create_button);
            createButton.setText("Edit Account");

        }

        makeButtons();

    }

    private void makeButtons() {

        Button createButton = (Button) findViewById(R.id.create_button);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccountPress();
            }
        });

    }

    private boolean usernameTaken(String username) {
        // TODO check in database for same username
        return false;
    }

    private void setErrorMessage(int messageID) {
        TextView errorMessage = (TextView) findViewById(R.id.error_message);
        errorMessage.setText(getString(messageID));
        errorMessage.setVisibility(View.VISIBLE);
    }

    private void createAccountPress() {

        EditText usernameField = (EditText) findViewById(R.id.username_field);
        EditText fullNameField = (EditText) findViewById(R.id.full_name_field);
        EditText zipCodeField = (EditText) findViewById(R.id.zip_code_field);
        EditText passwordField = (EditText) findViewById(R.id.password_field);
        EditText reEnterPasswordField = (EditText) findViewById(R.id.reenter_password_field);

        String username = usernameField.getText().toString();
        String fullName = fullNameField.getText().toString();
        String zipCode = zipCodeField.getText().toString();
        String password1 = passwordField.getText().toString();
        String password2 = reEnterPasswordField.getText().toString();

        EditText[] fields = {usernameField, fullNameField, zipCodeField, passwordField, reEnterPasswordField};

        // Jason added this b/c there seemed to be an error with updating data in GlobalInfo
        GlobalInfo.getInstance().setCreateAccountUsername(username);

        // reset errors

        TextView errorMessage = (TextView) findViewById(R.id.error_message);
        errorMessage.setVisibility(View.INVISIBLE);

        for (EditText field : fields) {
            field.setHintTextColor(Color.GRAY);
        }

        boolean emptyFlag = false;

        for (EditText field : fields) {
            String contents = field.getText().toString();
            if (contents.isEmpty()) {
                field.setHintTextColor(Color.RED);
                emptyFlag = true;
            }
        }

        if (emptyFlag) {
            setErrorMessage(R.string.CREATEACCOUNT_missing_message);
            return;
        }

        if (!password1.equals(password2)) {
            reEnterPasswordField.setText("");
            reEnterPasswordField.setHintTextColor(Color.RED);
            setErrorMessage(R.string.CREATEACCOUNT_matching_message);
            return;
        }

        if (usernameTaken(username)) {
            setErrorMessage(R.string.CREATEACCOUNT_username_message);
            return;
        }

        int zipCodeNum;

        try {
            zipCodeNum = Integer.parseInt(zipCode);
        } catch (NumberFormatException e) {
            zipCodeNum = -1; // means not valid number
        }

        if (zipCodeNum == -1 || zipCode.length() != 5) {
            setErrorMessage(R.string.CREATEACCOUNT_zip_message);
            return;
        }

        // getting here means no errors
        User newUser = new User(username, password1, fullName, zipCodeNum);

        GlobalInfo info = GlobalInfo.getInstance();
        info.setCreateAccountUsername(newUser.username);
        info.setLoggedInUser(newUser);
        if (addUserToDataBase(newUser)) {

            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        } else {
            setErrorMessage(R.string.CREATEACCOUNT_database_message);
        }

    }
    User user;
    private boolean addUserToDataBase(User newUser) {
        user = newUser;
        class AccessWebTask extends AsyncTask<URL, String, String> {
            protected String doInBackground(URL... urls) {
                try {
                    URL url = urls[0];
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Content-Type", "application/json");
                    //conn.setRequestProperty("Accept", "application/json");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);


                    //String json = "\"name\":" + "\"" + user.username + "\"" +  ",\"" + "password\":" + "\"" + user.password +"\",\"fullname\":" +  "\"" +user.fullName + "\",\"zipcode\":" + user.zipCode  +  "";
                    JSONObject js = new JSONObject();
                    js.put("name", user.username);
                    js.put("password", user.password);
                    js.put("fullname", user.fullName);
                    js.put("zipcode", user.zipCode);
                    Log.i("ss","Sending 'POST' request to URL : " + url);

                    OutputStream os = conn.getOutputStream();
                    os.write(js.toJSONString().getBytes());
                    os.flush();
                    os.close();
                    int responseCode = conn.getResponseCode();
                    Log.i("ss","Sending 'POST' request to URL : " + url);
                    Log.i("dfs","Response Code : " + js.toJSONString());
                    Log.i("dfs","Response Code : " + responseCode);
                    conn.disconnect();
                    return "good";
                }
                catch (Exception e) {
                    Log.i("dfs",e.toString());
                    return e.toString();}
            }
        }
        try{
            String json = "?name=" + user.username + "&password=" + user.password +"&fullname=" + user.fullName + "&zipcode=" + user.zipCode  +  "";
            URL url = new URL("http://10.0.2.2:3000/createUser");
            AccessWebTask task = new AccessWebTask();
            task.execute(url);
            task.get();
            return true;
        } catch (Exception e){
            return  false;
        }
    }

}
