package wam2.finals.filevault;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private LinearLayout codes, buttons;

    private String[] inputCodes = {"*", "*", "*", "*"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        codes = findViewById(R.id.codes);
        buttons = findViewById(R.id.buttons);

        for (int i = 0; i < buttons.getChildCount(); i++) {
            LinearLayout buttonContainers = (LinearLayout) buttons.getChildAt(i);
            for (int j = 0; j < buttonContainers.getChildCount(); j++) {
                try {
                    Button button = (Button) buttonContainers.getChildAt(j);
                    button.setOnClickListener(v -> {
                        registerInput(button.getText().toString());
                        updateDisplayCode();
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
                break;
            }
        }
    }

    private boolean registerInput(String value){
        if (inputCodes[3].equals("*")){
            for (int i = 0; i < inputCodes.length; i++) {
                if (inputCodes[i].equals("*")){
                    inputCodes[i] = value;
                    return true;
                }
            }
        }
        return false;
    }
}