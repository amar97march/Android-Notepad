package com.example.amar97march.notetoself;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class DialogNewNote extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());

        LayoutInflater inflater= getActivity().getLayoutInflater();
        View dialogView =inflater.inflate(R.layout.dialog_new_note,null);

        final TextView editTitle=(TextView) dialogView.findViewById(R.id.editTitle);
        final TextView editDescription=(TextView) dialogView.findViewById(R.id.editDescription);
        final CheckBox checkBoxIdea=(CheckBox) dialogView.findViewById(R.id.checkBoxIdea);
        final CheckBox checkBoxTodo=(CheckBox) dialogView.findViewById(R.id.checkBoxTodo);
        final CheckBox checkBoxImportant=(CheckBox) dialogView.findViewById(R.id.checkBoxImportant);
        Button btnCancel=(Button) dialogView.findViewById(R.id.btnCancel);
        Button btnOK=(Button) dialogView.findViewById(R.id.btnOk);

        builder.setView(dialogView).setMessage("Add a new note.");

                btnCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View v){
                dismiss();
            }
        });

                btnOK.setOnClickListener(new View.OnClickListener(){
            @Override

            public  void onClick(View v){
                Note newNote= new Note();
                //set the  variables
                newNote.setTitle(editTitle.getText().toString());
                newNote.setDescription(editDescription.getText().toString());
                newNote.setIdea(checkBoxIdea.isChecked());
                newNote.setImportant(checkBoxImportant.isChecked());
                newNote.setTodo(checkBoxTodo.isChecked());
                MainActivity callingActivity=(MainActivity) getActivity();
                callingActivity.createNewNote(newNote);
                dismiss();
            }
        });
     return builder.create();
    }
}
