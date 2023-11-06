package lk.nibm.furious5.scorpio.transit.Activities;

import static com.android.volley.VolleyLog.TAG;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import lk.nibm.furious5.scorpio.transit.Model.User;
import lk.nibm.furious5.scorpio.transit.R;
import lk.nibm.furious5.scorpio.transit.Utilities.PreferenceManager;
import lk.nibm.furious5.scorpio.transit.databinding.ActivitySignupBinding;

public class SignupActivity extends AppCompatActivity {

    private ActivitySignupBinding binding;
    private String encodedImage;

    private static final String TAG = "VolleyExample";
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        PreferenceManager preferenceManager = new PreferenceManager(getApplicationContext());

        setListners();

    }

    private void setListners()
    {

        binding.txtSignIn.setOnClickListener(v -> onBackPressed());
        binding.btnSignUp.setOnClickListener(v -> {
            if (isValidSignUpDetails())
            {
//                User user = new User(
//                        binding.inputName.getText().toString(),
//                        binding.inputLastName.getText().toString(),
//                        binding.inputEmail.getText().toString(),
//                        binding.inputMobile.getText().toString(),
//                        binding.inputPassword.getText().toString(),
//                        binding.confirmPassword.getText().toString()
//                );
                postData();
            }
        });

        binding.layoutImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImage.launch(intent);
        });

    }

    private void showToast(String Message)
    {

        Toast.makeText(getApplicationContext(), Message, Toast.LENGTH_SHORT).show();

    }

    private String encodeImage(Bitmap bitmap)
    {

        int previewWidth = 150;
        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);

    }

    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode() == RESULT_OK){
                    if(result.getData()!=null){
                        Uri imageUri = result.getData().getData();
                        try{
                            InputStream inputStream = getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            binding.imgProPic.setImageBitmap(bitmap);
                            binding.txtAddImage.setVisibility(View.GONE);
                            encodedImage = encodeImage(bitmap);
                        }catch(FileNotFoundException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

    private Boolean isValidSignUpDetails()
    {

        if (encodedImage == null)
        {
            showToast("Select Profile Image");
            return false;
        }else if(binding.inputName.getText().toString().trim().isEmpty())
        {
            showToast("Enter First Name");
            return false;
        } else if (binding.inputLastName.getText().toString().trim().isEmpty()) {

            showToast("Enter Last Name");
            return false;
        } else if(binding.inputEmail.getText().toString().trim().isEmpty())
        {
            showToast("Enter an Email");
            return false;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(binding.inputEmail.getText().toString()).matches())
        {
            showToast("Enter a valid Email");
            return false;
        } else if (binding.inputMobile.getText().toString().trim().isEmpty()) {

            showToast("Enter Mobile Number");
            return false;

        } else if(binding.inputPassword.getText().toString().trim().isEmpty())
        {
            showToast("Enter Password");
            return false;
        } else if (binding.inputPassword.getText().toString().trim().length() < 6) {
            showToast("Enter at least six characters password.");
            return false;
        } else if(binding.confirmPassword.getText().toString().trim().isEmpty())
        {
            showToast("Confirm Your Password");
            return false;
        }else if(!binding.inputPassword.getText().toString().equals(binding.confirmPassword.getText().toString()))
        {
            showToast("Password and Confirm Password must be matched.");
            return false;
        }else
        {
            return true;
        }

    }

    private void loading(Boolean isLoading)
    {
        if(isLoading)
        {
            binding.btnSignUp.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        }else
        {
            binding.progressBar.setVisibility(View.INVISIBLE);
            binding.btnSignUp.setVisibility(View.VISIBLE);
        }
    }

    private void postData() {

        loading(true);
        // Initialize the RequestQueue
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        // API endpoint URL
        String apiUrl = "https://transit.alexlanka.com/api/v1/user/store";

        String firstName = binding.inputName.getText().toString();
        String lastName = binding.inputLastName.getText().toString();
        String email = binding.inputEmail.getText().toString();
        String mobile = binding.inputMobile.getText().toString();
        String password = binding.inputPassword.getText().toString();
        String confirm = binding.confirmPassword.getText().toString();

        // Create a JSONObject for the POST request
        JSONObject postData = new JSONObject();
        try {
            postData.put("first_name", firstName);
            postData.put("last_name", lastName);
            postData.put("email", email);
            postData.put("mobile", mobile);
            postData.put("password", password);
            postData.put("password_confirmation", confirm);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Make a POST request using Volley
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, apiUrl, postData,
                response -> {
                    loading(false);
                    binding.inputName.setText("");
                    binding.inputLastName.setText("");
                    binding.inputEmail.setText("");
                    binding.inputMobile.setText("");
                    binding.inputPassword.setText("");
                    binding.confirmPassword.setText("");
                    try {
                        String message = response.toString();
                        Log.d(TAG, "Response: " + response.toString());
                        showToast("User Registered Successful");
                        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                        startActivity(intent);
                        if (message.equals("User Registered Successful")) {
                            // Handle success, navigate to the next activity
//                            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
//                            startActivity(intent);
                        }else {
                            showToast("Registration failed: " + response.getString("errorMessage"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    if (error instanceof NetworkError) {
                        Log.e(TAG, "Network Error");
                    } else if (error instanceof ServerError) {
                        Log.e(TAG, "Server Error");
                    } else if (error instanceof AuthFailureError) {
                        Log.e(TAG, "Authentication Failure Error");
                    } else if (error instanceof ParseError) {
                        Log.e(TAG, "Parse Error");
                    } else if (error instanceof NoConnectionError) {
                        Log.e(TAG, "No Connection Error");
                    } else if (error instanceof TimeoutError) {
                        Log.e(TAG, "Timeout Error");
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        // Add the request to the RequestQueue
        requestQueue.add(request);
    }

}