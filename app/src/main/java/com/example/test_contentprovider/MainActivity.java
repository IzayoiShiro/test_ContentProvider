package com.example.test_contentprovider;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ListView mLv;
    private List<Person> persons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        persons = new ArrayList<Person>();
//        File file = new File("/data/data/com.example.test_sqlite/databases/person.db");
//        System.out.println("文件是否存在"+file.exists());
//        SQLiteDatabase db = SQLiteDatabase.openDatabase("/data/data/com.example.test_sqlite/databases/person.db",null,SQLiteDatabase.OPEN_READONLY);
//        Cursor cursor = db.rawQuery("select * from person",null);
//        while (cursor.moveToNext()){
//            int id = cursor.getInt(cursor.getColumnIndex("id"));
//            String name = cursor.getString(cursor.getColumnIndex("name"));
//            String number = cursor.getString(cursor.getColumnIndex("number"));
//            System.out.println("ID："+id+"，姓名："+name+"，电话号码："+number);
//        }
//        cursor.close();
//        db.close();


//        Button button = (Button) findViewById(R.id.button);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String path = "content://com.example.mysqlite.personDB/query";
//                Uri uri = Uri.parse(path);
//                ContentResolver resolver = MainActivity.this.getContentResolver();
//                Cursor cursor = resolver.query(uri,null,null,null,null);
//                while (cursor.moveToNext()){
//                    int id = cursor.getInt(cursor.getColumnIndex("id"));
//                    String name = cursor.getString(1);
//                    String number = cursor.getString(2);
//                }
//                cursor.close();
//            }
//        });
        mLv = (ListView) findViewById(R.id.lv);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int item_id = item.getItemId();
        switch (item_id) {
            case R.id.add:
                insert();
                break;
            case R.id.delete:
                delete();
                break;
            case R.id.exit:
                MainActivity.this.finish();
                break;
        }
        return true;
    }

    private class Myadapter extends BaseAdapter {

        private LayoutInflater mInflater;

        public Myadapter() {
            mInflater = LayoutInflater.from(MainActivity.this);
        }

        @Override
        public int getCount() {
//            Log.d(TAG, "getCount: ........." + persons.size());
            return persons.size();
        }

        @Override
        public Object getItem(int i) {
            return persons.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            Person person = persons.get(i);
            ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.list_item, viewGroup, false);
                holder = new ViewHolder();
                holder.tv_id = (TextView) convertView.findViewById(R.id.tv_Id);
                holder.tv_name = (TextView) convertView.findViewById(R.id.tv_Name);
                holder.tv_number = (TextView) convertView.findViewById(R.id.tv_Number);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
//            View view = View.inflate(MainActivity.this, R.layout.list_item, null);
            /*View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item,
                    null);*/
//            TextView tv_id = (TextView) view.findViewById(R.id.tv_Id);
//            TextView tv_name = (TextView) view.findViewById(R.id.tv_Name);
//            TextView tv_number = (TextView) view.findViewById(R.id.tv_Number);
            holder.tv_id.setText(person.getId() + "");
            holder.tv_name.setText(person.getName());
            holder.tv_number.setText(person.getNumber());
            notifyDataSetChanged();
            return convertView;
        }
    }

    static class ViewHolder {
        TextView tv_id, tv_name, tv_number;
    }

    public void query() {
        ContentResolver resolver = getContentResolver();
        String path = "content://com.example.mysqlite.personDB/query";
        Uri uri = Uri.parse(path);
        Cursor cursor = resolver.query(uri, null, null, null, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String name = cursor.getString(1);
            String number = cursor.getString(2);
            Person person = new Person(id, name, number);
            persons.add(person);
            Log.i(TAG, "onOptionsItemSelected: id:" + id +"name:"+name+"number:"+number);
        }

        mLv.setAdapter(new Myadapter());
        cursor.close();
    }

    public void insert() {
        String path = "content://com.example.mysqlite.personDB/insert";
        Uri uri = Uri.parse(path);
        ContentResolver resolver = MainActivity.this.getContentResolver();
        for (int i = 1; i <= 10; i++) {
            ContentValues values = new ContentValues();
            values.put("name", "robot" + i);
            values.put("number", "110" + i);
            resolver.insert(uri, values);
        }
        query();
    }

    public void delete() {
        String path = "content://com.example.mysqlite.personDB/delete";
        Uri uri = Uri.parse(path);
        ContentResolver resolver = MainActivity.this.getContentResolver();
        resolver.delete(uri, null, null);
        persons.clear();
        query();
    }
}