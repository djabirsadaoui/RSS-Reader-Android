package com.example.m2sar.rssreader;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import static com.example.m2sar.rssreader.R.layout.activity_list_favoris;


public class Archives extends ActionBarActivity {
    ArrayList<HashMap<String, String>> dataFavoris = new ArrayList<HashMap<String, String>>();
    SimpleAdapter adapter;
    ListView list;
    AlertDialog alert;
    AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_list_favoris);
        list= (ListView) findViewById(R.id.Archives);    // récupérer la référence de listeview
        readeCellule();
        adapter = new SimpleAdapter(getApplicationContext(), dataFavoris, R.layout.data_rss, new String[]{"title", "date"},
                new int[]{R.id.title, R.id.subTitle});
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(tester_Connexion()){
                    String url =((HashMap<String,String>)adapter.getItem(position)).get("link");
                    String title = ((HashMap<String,String>)adapter.getItem(position)).get("title");
                    String date = ((HashMap<String,String>)adapter.getItem(position)).get("date");
                    Intent intent = new Intent(getApplicationContext(),web.class);
                    intent.putExtra("URL",url);
                    intent.putExtra("TITLE",title);
                    intent.putExtra("DATE",date);
                    startActivity(intent);}else{
                    Toast.makeText(getApplicationContext(), " La connection est indisponible  ", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private boolean tester_Connexion() {
        ConnectivityManager Cmgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = Cmgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }
    private void addItem(String record_title, String record_date,String record_link) {
        HashMap<String, String> item = new HashMap <String, String>();
        System.out.println("title : "+record_title+" date : "+record_date+" url : "+record_link);
        item.put("title", record_title);
        item.put("date", record_date);
        item.put("link",record_link);
        dataFavoris.add(item);
    }
    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message m) {
            adapter.notifyDataSetChanged();
        }
    };
    void readeCellule(){

        try {
            InputStream input =openFileInput("MyFile");
            if(input!=null){
                InputStreamReader inputStreamReader = new InputStreamReader(input);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString ="";
                StringBuilder stringBuilder = new StringBuilder();
                while ((receiveString = bufferedReader.readLine())!=null){
                   addItem(receiveString,bufferedReader.readLine(),bufferedReader.readLine());
                }
                input.close();
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_favoris, menu);
        return true;
    }
   void refresh(){
        setContentView(activity_list_favoris);
        readeCellule();
        list.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if(id==R.id.delete){

            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("vous voulez vraiment supprimer votre sauvegardes?");
            builder1.setCancelable(true);
            builder1.setPositiveButton("Oui",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            FileOutputStream fos;
                            try {

                                fos = openFileOutput("MyFile",getApplicationContext().MODE_PRIVATE);
                                fos.write((" ").getBytes());
                                fos.close();
                                refresh();

                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            dialog.cancel();
                        }
                    });
            builder1.setNegativeButton("Non",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();




        }

        return super.onOptionsItemSelected(item);
    }
}
