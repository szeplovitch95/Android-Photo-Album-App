package com.example.shacharchrisphotoalbum.photoalbumandroid04;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

/**
 * @author Shachar Zeplovitch
 * @author Christopher Mcdonough
 */
public class AddEditTag extends AppCompatActivity {
    private String tagType;
    private String tagValue;
    private EditText tagTypeEText;
    private EditText tagValueEText;
    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_tag);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        
        tagTypeEText = (EditText)findViewById(R.id.tag_type);
        tagValueEText = (EditText)findViewById(R.id.tag_value);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            tagTypeEText.setText(bundle.getString("tagType"));
            tagValueEText.setText(bundle.getString("tagValue"));
            pos = bundle.getInt("pos");
        }
    }

    public void cancel(View view) {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    public void save(View view) {
        tagType = tagTypeEText.getText().toString();
        tagValue = tagValueEText.getText().toString();

        //checks if inputs are valid
        if (tagType == null || tagType.length() == 0 || tagValue == null || tagValue.length() == 0) {
            Bundle bundle = new Bundle();
            bundle.putString(AlbumDialogFragment.MESSAGE_KEY,"All values are required");
            DialogFragment newFragment = new AlbumDialogFragment();
            newFragment.setArguments(bundle);
            newFragment.show(getFragmentManager(), "missing fields");
            return;
        }

        if(tagType.trim().equalsIgnoreCase("person") || tagType.trim().equalsIgnoreCase("location")) {
            Bundle bundle = new Bundle();
            bundle.putString("tagType", tagTypeEText.getText().toString());
            bundle.putString("tagValue", tagValueEText.getText().toString());
            bundle.putInt("pos", pos);
            Intent intent = new Intent();
            intent.putExtras(bundle);
            setResult(RESULT_OK, intent);
            finish();
        }
        else {
            Bundle bundle = new Bundle();
            bundle.putString(AlbumDialogFragment.MESSAGE_KEY,"Tag Type must be either location or person");
            DialogFragment newFragment = new AlbumDialogFragment();
            newFragment.setArguments(bundle);
            newFragment.show(getFragmentManager(), "missing fields");
            return;
        }
    }
}