package com.example.todolist;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;


public class Sqldatabase extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String NAME = "ListDatabase";
    private static final String TABLE_N = "TASKLIST";
    private static final String ID = "id";
    private static final String TASK = "task";
    private static final String STATUS = "status";
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_N + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TASK + " TEXT, "
            + STATUS + " INTEGER)";

    private SQLiteDatabase db;

    public Sqldatabase(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

            db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_N);
        onCreate(db);
    }

    public void openDatabase() {
        db = this.getWritableDatabase();
    }

    public void insTsk(TaskObj task){
        ContentValues cv = new ContentValues();
        cv.put(TASK, task.getTsk());
        cv.put(STATUS, 0);
        db.insert(TABLE_N, null, cv);
    }

    public List<TaskObj> getAllTasks(){
        List<TaskObj> taskList = new ArrayList<>();
        Cursor cr = null;
        db.beginTransaction();
        try{
            cr = db.query(TABLE_N, null, null, null, null, null, null, null);
            if(cr != null){
                if(cr.moveToFirst()){
                    do{
                        TaskObj task = new TaskObj();
                        task.setId(cr.getInt(cr.getColumnIndex(ID)));
                        task.setTsk(cr.getString(cr.getColumnIndex(TASK)));
                        task.setStatus(cr.getInt(cr.getColumnIndex(STATUS)));
                        taskList.add(task);
                    }
                    while(cr.moveToNext());
                }
            }
        }
        finally {
            db.endTransaction();
            assert cr != null;
            cr.close();
        }
        return taskList;
    }

    public void updSt(int id, int status){
        ContentValues cv = new ContentValues();
        cv.put(STATUS, status);
        db.update(TABLE_N, cv, ID + "= ?", new String[] {String.valueOf(id)});
    }

    public void updTsk(int id, String task) {
        ContentValues cv = new ContentValues();
        cv.put(TASK, task);
        db.update(TABLE_N, cv, ID + "= ?", new String[] {String.valueOf(id)});
    }


    public void delTsk(int id){
        db.delete(TABLE_N, ID + "= ?", new String[] {String.valueOf(id)});
    }
}







