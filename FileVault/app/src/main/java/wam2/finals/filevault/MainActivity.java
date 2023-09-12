package wam2.finals.filevault;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private LinearLayout codes, buttons;

    private String[] inputCodes = {"*", "*", "*", "*"};
    private int hasCodes = 0;

    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] perms = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.MANAGE_EXTERNAL_STORAGE", "android.permission.MANAGE_DOCUMENTS"};

        requestPermissions(perms, 200);

        if (checkSelfPermission(perms[0]) == PackageManager.PERMISSION_DENIED || checkSelfPermission(perms[1]) == PackageManager.PERMISSION_DENIED){
            requestPermissions(perms, 200);
        }

        codes = findViewById(R.id.codes);
        buttons = findViewById(R.id.buttons);
        db = new DatabaseHandler(MainActivity.this);

        if (!db.hasPinRegistered()){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Vault Pin Setup");
            final View dialogView = getLayoutInflater().inflate(R.layout.setup_pin, null);
            builder.setView(dialogView);

            builder.setPositiveButton("Submit", (dialog, which) -> {
                EditText pin = dialogView.findViewById(R.id.pin_setup);
                if (db.insertNewCode(pin.getText().toString())){
                    Toast.makeText(MainActivity.this, pin.getText().toString() + " has been registered as the vault pin", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(MainActivity.this, "Could not set the pin as the vault pin", Toast.LENGTH_LONG).show();
                }
            });
            builder.create().show();
        }


        for (int i = 0; i < buttons.getChildCount(); i++) {
            LinearLayout buttonContainers = (LinearLayout) buttons.getChildAt(i);
            for (int j = 0; j < buttonContainers.getChildCount(); j++) {
                try {
                    Button button = (Button) buttonContainers.getChildAt(j);
                    button.setOnClickListener(v -> {
                        registerInput(button.getText().toString());
                        updateDisplayCode();
                        checkVaultCode();
                    });
                }catch (ClassCastException e){
                    ImageButton button = (ImageButton) buttonContainers.getChildAt(j);
                    button.setOnClickListener(v -> {
                        eraseInput();
                        updateDisplayCode();
                    });
                }
            }
        }
    }



    private void checkVaultCode(){
        if (hasCodes == inputCodes.length){
            if (db.checkVaultCode(getPins())){
                startActivity(new Intent(MainActivity.this, PasswordListing.class));
            }else {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Access Denied!");
                builder.setMessage("Given Pin did not matched");
                builder.setNegativeButton("Close", (dialog, which) -> {
                    dialog.dismiss();
                });
                builder.create().show();
            }
        }
    }

    private String getPins(){
        StringBuilder pin = new StringBuilder();
        for (String inputCode : inputCodes) {
            pin.append(inputCode);
        }
        return pin.toString();
    }

    private void updateDisplayCode(){
        for (int i = 0; i < codes.getChildCount(); i++) {
            TextView codeDisplay = (TextView) codes.getChildAt(i);
            codeDisplay.setText(inputCodes[i]);
        }
    }

    private void eraseInput(){
        for (int i = (inputCodes.length - 1); i >= 0 ; i--) {
            if (!inputCodes[i].equals("*")){
                inputCodes[i] = "*";
                hasCodes--;
                break;
            }
        }
    }

    private boolean registerInput(String value){
        if (inputCodes[(inputCodes.length - 1)].equals("*")){
            for (int i = 0; i < inputCodes.length; i++) {
                if (inputCodes[i].equals("*")){
                    inputCodes[i] = value;
                    hasCodes++;
                    return true;
                }
            }
        }
        return false;
    }
}