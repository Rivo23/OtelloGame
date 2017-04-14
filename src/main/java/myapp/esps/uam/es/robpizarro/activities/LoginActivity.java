package myapp.esps.uam.es.robpizarro.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import myapp.esps.uam.es.robpizarro.R;
import myapp.esps.uam.es.robpizarro.models.RoundRepository;
import myapp.esps.uam.es.robpizarro.models.RoundRepositoryFactory;

/**
 * Created by localuser01 on 31/03/17.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener{
    private RoundRepository repository;
    private EditText usernameEditText;
    private EditText passwordEditText;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final String username=PreferenceActivity.getPlayerName(this);
        final String pass=PreferenceActivity.getPassword(this);
        RoundRepository.LoginRegisterCallback loginRegisterCallback =
                new RoundRepository.LoginRegisterCallback() {
                    @Override
                    public void onLogin(String playerId) {
                        PreferenceActivity.setPlayerUUID(LoginActivity.this, playerId);
                        PreferenceActivity.setPlayerName(LoginActivity.this, username);
                        PreferenceActivity.setPassword(LoginActivity.this, pass);
                        startActivity(new Intent(LoginActivity.this, RoundListActivity.class));
                        finish();
                    }
                    @Override
                    public void onError(String error) {

                    }
                };

        repository = RoundRepositoryFactory.createRepository(LoginActivity.this);
        if (repository == null)
            Toast.makeText(LoginActivity.this, R.string.repository_opening_error, Toast.LENGTH_SHORT).show();

        repository.login(username, pass, loginRegisterCallback);

        usernameEditText = (EditText) findViewById(R.id.login_username);
        passwordEditText = (EditText) findViewById(R.id.login_password);
        Button loginButton = (Button) findViewById(R.id.login_button);
        Button registerButton = (Button) findViewById(R.id.new_user_button);
        Button helpButton = (Button) findViewById(R.id.help_button);
        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
        helpButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        final String playername = usernameEditText.getText().toString();
        final String password = passwordEditText.getText().toString();
        if ((playername.isEmpty()||password.isEmpty()) && v.getId()!=R.id.help_button){
            Toast.makeText(LoginActivity.this, R.string.error_username, Toast.LENGTH_SHORT).show();
            return;
        }
        RoundRepository.LoginRegisterCallback loginRegisterCallback = new RoundRepository.LoginRegisterCallback() {
            @Override
            public void onLogin(String playerId) {
                PreferenceActivity.setPlayerUUID(LoginActivity.this, playerId);
                PreferenceActivity.setPlayerName(LoginActivity.this, playername);
                PreferenceActivity.setPassword(LoginActivity.this, password);
                startActivity(new Intent(LoginActivity.this, RoundListActivity.class));
                finish();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(LoginActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        };
        switch (v.getId()) {
            case R.id.login_button:
                repository.login(playername, password, loginRegisterCallback);
                break;
            case R.id.help_button:
                AlertDialogFragment alert = AlertDialogFragment.newInstance(getResources().getString(R.string.ayuda), AlertDialogFragment.STARTROUND, getResources().getString(R.string.ayuda_login));
                alert.show(this.getFragmentManager(), "ALERT DIALOG");
                break;
            case R.id.new_user_button:
                repository.register(playername, password, loginRegisterCallback);
                break;
        }
    }

    public void onDestroy(){
        super.onDestroy();
        repository.close();
    }
}
