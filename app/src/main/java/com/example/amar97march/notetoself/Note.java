package com.example.amar97march.notetoself;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by amar97march on 02-07-2017.
 */

public class Note {
    private String mTitle;
    private String  mDescription;
    private boolean mIdea;
    private boolean mTodo;
    private boolean mImportant;
    private static final String JSON_TITLE="title";
    private static  final String JSON_DESCRIPTION="descripbtion";
    private static final String JSON_IDEA="idea";
    private static final String JSON_TODO="todo";
    private static final String JSON_IMPORTANT="important";

    public Note(JSONObject jo) throws JSONException{
        mTitle=jo.getString(JSON_TITLE);
        mDescription=jo.getString(JSON_DESCRIPTION);
        mIdea=jo.getBoolean(JSON_IDEA);
        mTodo=jo.getBoolean(JSON_TODO);
        mImportant=jo.getBoolean(JSON_IMPORTANT);
    }
    public Note() {

    }
    public JSONObject convertToJSON() throws JSONException{
        JSONObject jo=new JSONObject();
        jo.put(JSON_TITLE,mTitle);
        jo.put(JSON_DESCRIPTION,mDescription);
        jo.put(JSON_IDEA,mIdea);
        jo.put(JSON_IMPORTANT,mImportant);
        jo.put(JSON_TODO,mTodo);
        return jo;
    }


    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String nTitle) {
        this.mTitle = nTitle;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public boolean isTodo() {
        return mTodo;
    }

    public void setTodo(boolean mTodo) {
        this.mTodo = mTodo;
    }

    public boolean isIdea() {
        return mIdea;
    }

    public void setIdea(boolean mIdea) {
        this.mIdea = mIdea;
    }

    public boolean isImportant() {
        return mImportant;
    }

    public void setImportant(boolean mImportant) {
        this.mImportant = mImportant;
    }
}
