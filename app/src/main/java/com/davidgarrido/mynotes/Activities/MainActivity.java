package com.davidgarrido.mynotes.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.ContextMenu;

import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.davidgarrido.mynotes.Adapters.NoteAdapter;
import com.davidgarrido.mynotes.Models.Note;
import com.davidgarrido.mynotes.R;


import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private NoteAdapter noteAdapter;
    private int actionCode;
    private FloatingActionButton fabAdd;
    private Realm realm;
    private RealmResults<Note> noteRealmResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        realm = Realm.getDefaultInstance();
        noteRealmResults = realm.where(Note.class).findAll();
        noteRealmResults.addChangeListener(new RealmChangeListener<RealmResults<Note>>() {
            @Override
            public void onChange(RealmResults<Note> notes) {
                noteAdapter.notifyDataSetChanged();
            }
        });

        this.fabAdd = findViewById(R.id.fabAdd);

        this.listView = findViewById(R.id.listView);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_icon);

        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                actionCode = 2;
                Intent intentEdit = new Intent(MainActivity.this, AddEditActivity.class);
                intentEdit.putExtra("actionCode", actionCode);
                intentEdit.putExtra("id", noteRealmResults.get(position).getId());
                startActivity(intentEdit);
            }
        });

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTitleAD();

            }
        });

        this.noteAdapter = new NoteAdapter(this, R.layout.list_item, noteRealmResults);
        this.listView.setAdapter(noteAdapter);
        registerForContextMenu(listView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch ((item.getItemId())) {
            case (R.id.edit_Title):
                editTitleAD(this.noteRealmResults.get(info.position));
                return true;
            case (R.id.delete_note):
                realm.beginTransaction();
                this.noteRealmResults.get(info.position).deleteFromRealm();
                realm.commitTransaction();
                return true;
            default:
                return super.onContextItemSelected(item);
        }

    }

    public void editTitleAD(final Note note) {

        LayoutInflater inflater = this.getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog, null);
        final EditText editTextTitle = v.findViewById(R.id.editTextTitle);
        editTextTitle.setText(note.getTitle().toString());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.editTitle));
        builder.setView(v);

        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (!editTextTitle.getText().toString().isEmpty()) {
                    realm.beginTransaction();
                    note.setTitle(editTextTitle.getText().toString());
                    realm.copyToRealmOrUpdate(note);
                    realm.commitTransaction();
                } else {
                    Toast.makeText(MainActivity.this, getString(R.string.mustComplete), Toast.LENGTH_LONG).show();
                }
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();


    }

    public void setTitleAD() {
        final String[] title = new String[1];
        LayoutInflater inflater = this.getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.setTitle));
        builder.setView(v);
        final EditText editTextTitle = v.findViewById(R.id.editTextTitle);

        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (editTextTitle.getText().toString().length() != 0) {
                    title[0] = editTextTitle.getText().toString();
                    actionCode = 1;
                    Intent intentAdd = new Intent(MainActivity.this, AddEditActivity.class);
                    intentAdd.putExtra("noteTitle", title[0]);
                    intentAdd.putExtra("actionCode", actionCode);
                    startActivity(intentAdd);
                } else {
                    Toast.makeText(MainActivity.this, getString(R.string.mustComplete), Toast.LENGTH_LONG).show();
                }

            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }
}
