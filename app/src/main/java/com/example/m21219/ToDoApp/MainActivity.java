package com.example.m21219.ToDoApp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/*Diese Klasse ist für die Loginfunktion zuständig, beinhaltet die User und Passwörter und vergleicht
sie mit den eingegebenen Werten*/
public class MainActivity extends AppCompatActivity {

    public static final String UserID = "";
    SQLiteDatabase myDB = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //SQLiteDatenbank anlegen und mit Usern und Tasks füllen
        try {
            myDB = this.openOrCreateDatabase("IUBHtoDoApp", MODE_PRIVATE, null);
            myDB.execSQL("CREATE TABLE IF NOT EXISTS Users (id INTEGER PRIMARY KEY,name VARCHAR, password VARCHAR)");
            myDB.execSQL("INSERT INTO Users (id, name, password) VALUES (NULL,'Matthias', 'test1')");
            myDB.execSQL("INSERT INTO Users (id, name, password) VALUES (NULL,'Hakan', 'test2')");
            myDB.execSQL("INSERT INTO Users (id, name, password) VALUES (NULL,'Lukas', 'test3')");
            myDB.execSQL("CREATE TABLE IF NOT EXISTS Tasks (id INTEGER PRIMARY KEY,name VARCHAR, description VARCHAR, completetiondate NUMERIC, completionstatus INTEGER, favorite INTEGER, userid INTEGER)");
            myDB.execSQL("INSERT INTO Tasks (id, name, description, userid) VALUES (NULL,'Task1', 'Das ist Task1',1)");
            myDB.execSQL("INSERT INTO Tasks (id, name, description, userid) VALUES (NULL,'Task2', 'Das ist Task2',1)");

        } catch (Exception e) {
            Log.e("Error", "Error", e);
        }

        final Button button = (Button) findViewById(R.id.btn_login);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                final EditText editTextUserName = (EditText) findViewById(R.id.txt_username);
                final EditText editTextPassword = (EditText) findViewById(R.id.txt_password);

                // get The User name and Password
                String userName = editTextUserName.getText().toString();
                String password = editTextPassword.getText().toString();

                // fetch the Password form database for respective user name
                Cursor cursor = myDB.rawQuery("SELECT id, password FROM Users where name='" + userName.trim() + "'", null);

                try {
                    if (cursor.getCount() < 1) // UserName Not Exist
                    {
                        cursor.close();
                        Toast toast = Toast.makeText(getApplicationContext(), "Unbekannter Username", Toast.LENGTH_LONG);
                        toast.show();
                    }

                    cursor.moveToFirst();
                    String strPassword = cursor.getString(cursor.getColumnIndex("password"));
                    String intUserID = cursor.getString(cursor.getColumnIndex("id"));
                    cursor.close();

                    // check if the Stored password matches with  Password entered by user
                    if (password.equals(strPassword)) {
                        Toast.makeText(getApplicationContext(), "Login Erfolgreich!", Toast.LENGTH_LONG).show();

                        //UserID Global setzen in Singleton Klasse
                        Globals.getInstance();
                        Globals.setUserID(intUserID);

                        Intent myIntent = new Intent(MainActivity.this, MainView.class);
                        myIntent.putExtra("UserName", userName);
                        MainActivity.this.startActivity(myIntent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Nutzername oder Passwort ist nicht korrekt!", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Es ist ein Fehler aufgetreten: " + e.getMessage());
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }


            }
        });


    }
}
