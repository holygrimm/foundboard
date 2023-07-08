package com.holygrimm.foundboard;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class MainActivity extends Activity {

    private static final int PICK_AUDIO_REQUEST = 1;
    private LinearLayout buttonContainer;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonContainer = findViewById(R.id.buttonContainer);
        Button addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptAudioFileSelection();
            }
        });
    }

    private void promptAudioFileSelection() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("audio/*");
        startActivityForResult(intent, PICK_AUDIO_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_AUDIO_REQUEST && resultCode == RESULT_OK) {
            final Uri audioUri = data.getData();
            promptButtonName(new ButtonNameCallback() {
                @Override
                public void onButtonNameEntered(String buttonName) {
                    createButton(buttonName, audioUri);
                }
            });
        }
    }

    private void promptButtonName(final ButtonNameCallback callback) {
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);

        new android.app.AlertDialog.Builder(this)
                .setTitle("Enter Button Name")
                .setView(input)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String buttonName = input.getText().toString().trim();
                        callback.onButtonNameEntered(buttonName);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private interface ButtonNameCallback {
        void onButtonNameEntered(String buttonName);
    }

    private void createButton(final String buttonName, final Uri audioUri) {
        Button newButton = new Button(this);
        newButton.setText(buttonName);
        buttonContainer.addView(newButton);

        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playAudioFile(audioUri);
            }
        });
    }

    private void playAudioFile(Uri audioUri) {
        MediaPlayer mediaPlayer = MediaPlayer.create(this, audioUri);
        mediaPlayer.start();
    }
}
