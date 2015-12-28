package com.example.m2sar.rssreader;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class MainActivity extends ActionBarActivity  implements NavigationDrawerFragment.NavigationDrawerCallbacks {
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    AlertDialog.Builder builder;
    int Posistion_courant=1;
    int getPosition(){
        return  Posistion_courant;
    }
    PlaceholderFragment placeholderFragment;
    FragmentManager fragmentManager;
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private CharSequence mTitle;


    @Override
    public void onBackPressed() {
        builder = new AlertDialog.Builder(this);
        builder
                .setTitle("Attention")
                .setMessage("vous voulez vraiment quitter l'application ?")
                .setCancelable(false)
                .setPositiveButton("Oui", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {

                      //  super.onBackPressed();
                        finish();
                        dialog.cancel();

                    }
                })
                .setNegativeButton("Non",null);
        builder.show();



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }
    private boolean tester_Connexion() {
        ConnectivityManager Cmgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = Cmgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        fragmentManager = getSupportFragmentManager();
            if(placeholderFragment==null){
                placeholderFragment = new PlaceholderFragment();
            }

        fragmentManager.beginTransaction()
                .replace(R.id.container, placeholderFragment.newInstance(position + 1))
                        .commit();

    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = "Une";
                break;
            case 2:
                mTitle = "Internationnal";
                break;
            case 3:
                mTitle = "France";
                break;
            case 4:
                mTitle = "Santé";
                break;
            case 5:
                mTitle = "Science";
                break;
            case 6:
                mTitle = "High-Tech";
                break;
            case 7:
                mTitle = "Economie";
                break;
            case 8:
                mTitle = "Android";
                break;
            case 9:
                mTitle = "Culture";
                break;
            case 10:
                mTitle = "Energie";
                break;
            case 11:
                mTitle = "Sport";
                break;
            case 12:
                mTitle = "Gros-plan";
                break;
        }
    }


    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if( id==R.id.history){
            Intent intent = new Intent(getApplicationContext(),Archives.class);
            startActivity(intent);
        }else if (id==R.id.update){

            fragmentManager.beginTransaction()
                    .replace(R.id.container, placeholderFragment.newInstance(Posistion_courant+ 1))
                    .commit();
            Toast.makeText(getApplicationContext()," Rechargement de la page ...  ",Toast.LENGTH_SHORT).show();

        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public class PlaceholderFragment extends Fragment {
        SimpleAdapter adapter;
        String url;
        ListView list;
        View rootView;
        RetrieveFeedTask r;
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
        private   final String ARG_SECTION_NUMBER = "section_number";
        String[] selection_tableau = new String[]{" ", "w", "n", "m", "t", "t", "b", "t", "e", "e", "s", "w"
        };

        private boolean tester_Connexion() {
            ConnectivityManager Cmgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = Cmgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected())
                return true;
            else
                return false;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        public  PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            Posistion_courant = sectionNumber-1;
            return fragment;
        }

        public PlaceholderFragment() {
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.fragment_main, container, false);
            list = (ListView)rootView.findViewById(R.id.list);    // récupérer la référence de listeview
            url="http://news.google.com/?topic="+selection_tableau[Posistion_courant]+"&output=rss";
            r =new RetrieveFeedTask(this);



            r.execute();

                // creation un adaptator pour structurer mes données
            adapter = new SimpleAdapter(rootView.getContext(), data, R.layout.data_rss, new String[]{"title", "date"},
                    new int[]{R.id.title, R.id.subTitle});
            list.setAdapter(adapter);

                // créer un listner qui écoute sur les cellule de listewiew
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(tester_Connexion()){
                    String url =((HashMap<String,String>)adapter.getItem(position)).get("link");
                    String title = ((HashMap<String,String>)adapter.getItem(position)).get("title");
                    String date = ((HashMap<String,String>)adapter.getItem(position)).get("date");
                    Intent intent = new Intent(rootView.getContext(),web.class);
                    intent.putExtra("URL",url);
                    intent.putExtra("TITLE",title);
                    intent.putExtra("DATE",date);
                    startActivity(intent);}else{
                        Toast.makeText(getApplicationContext()," La connection est indisponible  ",Toast.LENGTH_LONG).show();
                    }
                }
            });
            return rootView;
        }




        // fonction responsable de remplire data
        private void addItem(String record_title, String record_date,String record_link) {
            HashMap<String, String> item = new HashMap <String, String>();
            item.put("title", record_title);
            item.put("date", record_date);
            item.put("link",record_link);
            data.add(item);
        }

        // mettre  à ajour l'affichage des cellules
        private Handler myHandler = new Handler() {
            @Override
            public void handleMessage(Message m) {
                adapter.notifyDataSetChanged();
            }
        };
        private Handler Handler2 = new Handler() {
            @Override
            public void handleMessage(Message m) {
                Toast.makeText(getApplicationContext()," La connection est indisponible  ",Toast.LENGTH_LONG).show();
            }
        };

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }
    static class RetrieveFeedTask extends AsyncTask<String, Integer, String> {
        PlaceholderFragment hold;
        String url;
        private Exception exception;

        RetrieveFeedTask(PlaceholderFragment hold){
           this.hold =hold;
        }
        RetrieveFeedTask(){

        }


        void Load_page(){
            try {
                try {
                    if(hold.tester_Connexion()) {

                        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                        Document doc = documentBuilder.parse(hold.url);
                        doc.getDocumentElement().normalize();
                        NodeList nodeList = doc.getElementsByTagName("item");

                        for (int itr = 0; itr < nodeList.getLength(); itr++) {
                            Node node = nodeList.item(itr);
                            if (node.getNodeType() == Node.ELEMENT_NODE) {
                                final String Title = ((Element) node).getElementsByTagName("title").item(0).getTextContent();
                                final String date = ((Element) node).getElementsByTagName("pubDate").item(0).getTextContent();
                                final String Link = ((Element) node).getElementsByTagName("link").item(0).getTextContent();
                                hold.addItem(Title, date, Link);
                                // mise à jour affichage
                                hold.myHandler.sendMessage(Message.obtain(hold.myHandler, 0));


                            }


                        }
                    }else{
                        hold.Handler2.sendMessage(Message.obtain(hold.Handler2, 0));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }



            } catch (Exception e) {
                this.exception = e;

            }
        }
        protected String doInBackground(String... urls) {

                Load_page();
            return " ";
        }

        protected void onPostExecute() {
            // TODO: check this.exception
            // TODO: do something with the feed
        }
    }

}
