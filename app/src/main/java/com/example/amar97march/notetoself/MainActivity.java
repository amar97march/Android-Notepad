package com.example.amar97march.notetoself;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Animation mAnimFlash;
    Animation mAnimFlashSlow;
    Animation mAnimFadeIn;
    int mIdBeep=-1;
    SoundPool mSp;
    private NoteAdapter mNoteAdapter;
    private boolean mSound;
    private int mAnimOption;
    private SharedPreferences mPrefs;
   // Note mTempNote = new Note();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Instantiate our sound Pool
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            AudioAttributes audioAttributes=new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION).setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build();
            mSp=new SoundPool.Builder().setMaxStreams(5).setAudioAttributes(audioAttributes).build();
        }else{
            mSp=new SoundPool(5, AudioManager.STREAM_MUSIC,0);
        }

        try{
            AssetManager assetManager= this.getAssets();
            AssetFileDescriptor descriptor;
            //Loading them
            descriptor=assetManager.openFd("beep.ogg");
            mIdBeep=mSp.load(descriptor,0);
        } catch (IOException e){
            Log.e("error","failed to load sound file");
        }

        mNoteAdapter=new NoteAdapter();
        ListView listNote=(ListView) findViewById(R.id.listView);
        listNote.setAdapter(mNoteAdapter);
        listNote.setLongClickable(true);
        listNote.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
           public boolean onItemLongClick(AdapterView<?> adapter,View view,int whichItem,long id){
               mNoteAdapter.deleteNote(whichItem);
               return true;
           }
        });
        listNote.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapter,View view,int whichItem,long id){
               if(mSound){
                   mSp.play(mIdBeep,1,1,0,0,1);
               }
                Note tempNote=mNoteAdapter.getItem(whichItem);
                DialogShowNote dialog=new DialogShowNote();
                dialog.sendNoteSelected(tempNote);
                dialog.show(getFragmentManager(),"");
            }
        });
    }
    @Override
    protected void onResume(){
        super.onResume();
        mPrefs=getSharedPreferences("Note to self",MODE_PRIVATE);
        mSound=mPrefs.getBoolean("sound",true);
        mAnimOption=mPrefs.getInt("anim option",settingsActivity.FAST);

        mAnimFadeIn= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
        mAnimFlash=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.flash);
        mAnimFlashSlow=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.flashslow);
        //set the rate of flash based on settings
        if(mAnimOption==settingsActivity.FAST) {
            mAnimFlash.setDuration(100);
            Log.i("anim= ", "" + mAnimOption);
            mAnimFlash.setDuration(1000);
        }/*else if(mAnimOption==settingsActivity.SLOW){
            Log.i("anim = ",""+mAnimOption);
            mAnimFlash.setDuration(1000);

        }*/
        mNoteAdapter.notifyDataSetChanged();
    }
    public void createNewNote(Note n) {
        mNoteAdapter.addNote(n);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent=new Intent(this,settingsActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_add) {
            DialogNewNote dialog = new DialogNewNote();
            dialog.show(getFragmentManager(), "");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public class NoteAdapter extends BaseAdapter {
        private JSONSerializer mSerializer;
        public NoteAdapter() {
            mSerializer = new JSONSerializer("NoteToSelf.json", MainActivity.this.getApplicationContext());
            try {
                noteList = mSerializer.load();
            }catch(Exception e){
                noteList=new ArrayList<Note>();
                Log.e("Error loading notes:","",e);
            }

        }
        List<Note> noteList=new ArrayList<Note>();
        @Override
        public int getCount() {
            return noteList.size();
        }
        @Override
        public Note getItem(int whichItem){
            return noteList.get(whichItem);
        }
        @Override
        public long getItemId(int whichItem){
            return whichItem;
        }
        @Override
        public View getView (int whichItem, View view,ViewGroup viewGroup){
            //implement this method next
            if(view==null){
                LayoutInflater inflater =(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.listitem,viewGroup,false);
            }
            TextView txtTitle=(TextView) view.findViewById(R.id.txtTitle1);
            TextView txtDescription =(TextView) view.findViewById(R.id.txtDescription);
            ImageView ivTodo=(ImageView) view.findViewById(R.id.imageViewTodo);
            ImageView ivIdea=(ImageView) view.findViewById(R.id.imageViewIdea);
            ImageView ivImportant=(ImageView)view.findViewById(R.id.imageViewImportant);
            Note tempNote =noteList.get(whichItem);
            if(tempNote.isImportant()&&mAnimOption!=settingsActivity.FAST){
                //ivImportant.setVisibility(View.GONE);
                view.setAnimation(mAnimFlash);
            }else if(tempNote.isImportant()&&mAnimOption!=settingsActivity.SLOW){
                view.setAnimation(mAnimFlashSlow);
            }
            else{
                view.setAnimation(mAnimFadeIn);
            }
            if(!tempNote.isIdea()){
                ivIdea.setVisibility(View.GONE);
            }
            if(!tempNote.isTodo()){
                ivTodo.setVisibility(View.GONE);
            }
            txtTitle.setText(tempNote.getTitle());
            txtDescription.setText(tempNote.getDescription());
            return view;
        }
        public void addNote (Note n){
            noteList.add(n);
            notifyDataSetChanged();
        }
        public void saveNotes(){
            try{
                mSerializer.save(noteList);
            }catch(Exception e){
                Log.e("Error Saving Notes","",e);
            }
        }
        public void deleteNote(int n){
            noteList.remove(n);
            notifyDataSetChanged();
        }
    }
    @Override
    protected void onPause(){
        super.onPause();
        mNoteAdapter.saveNotes();
    }
}
