package com.davidgarrido.mynotes.Activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.davidgarrido.mynotes.Models.Note;
import com.davidgarrido.mynotes.R;

import io.realm.Realm;

public class AddEditActivity extends AppCompatActivity {

    private EditText editTextDescription;
    private FloatingActionButton fabSave;
    private final int addCode = 1;
    private final int editCode = 2;
    private int noteId;
    private Realm realm;
    private Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        fabSave = findViewById(R.id.fabSave);
        editTextDescription = findViewById(R.id.editTextDescription);

        realm = Realm.getDefaultInstance();

        Bundle bundle = getIntent().getExtras();
        int actionCode = bundle.getInt("actionCode");

        if (actionCode == addCode) {
            final String noteTitle = bundle.getString("noteTitle");
            AddEditActivity.this.setTitle(noteTitle);
            fabSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (noteTitle.length() > 0 && editTextDescription.getText().toString().length() > 0) {
                        Intent intentSave = new Intent(AddEditActivity.this, MainActivity.class);
                        createNote(noteTitle, editTextDescription.getText().toString());
                        startActivity(intentSave);
                    } else {
                        Toast.makeText(AddEditActivity.this, getString(R.string.mustComplete), Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else if (actionCode == editCode) {
            noteId = getIntent().getExtras().getInt("id");

            note = realm.where(Note.class).equalTo("id", noteId).findFirst();
            AddEditActivity.this.setTitle(note.getTitle());
            editTextDescription.setText(note.getDescription());
            fabSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (note.getTitle().length() > 0 && editTextDescription.getText().toString().length() > 0) {
                        editNote(editTextDescription.getText().toString(), note);
                        Intent intentSave = new Intent(AddEditActivity.this, MainActivity.class);
                        startActivity(intentSave);
                    } else {
                        Toast.makeText(AddEditActivity.this, getString(R.string.mustComplete), Toast.LENGTH_LONG).show();
                    }

                }
            });
        }
    }

    private void createNote(String title, String description) {
        realm.beginTransaction();
        Note note = new Note(title, description);
        realm.copyToRealm(note);
        realm.commitTransaction();
    }

    private void editNote(String description, Note note) {
        realm.beginTransaction();
        note.setDescription(description);
        realm.copyToRealmOrUpdate(note);
        realm.commitTransaction();
    }
}
