package bookseller.hackjunction.com.bookselling;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static junit.framework.Assert.assertTrue;


public class searchActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "RecyclerViewActivity";
    String condition, state, subject;
    Context cont;
    ArrayList<String> items;
    ArrayList<book> books;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cont = this;
        books = new ArrayList<>();
        items =new ArrayList<>();
        Bundle bundle = getIntent().getExtras();
         state = bundle.getString("state");
         condition = bundle.getString("condition");
         subject = bundle.getString("subject");
        AsyncTask h = new LongOperation().execute();

        setContentView(R.layout.search_activity);
        ImageView f = findViewById(R.id.imageView3);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new adapter(books);
        mRecyclerView.setAdapter(mAdapter);

        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);


    }

    @Override
    protected void onResume() {
        super.onResume();
        ((adapter) mAdapter).setOnItemClickListener(new
                                                                          adapter.MyClickListener() {
                                                                              @Override
                                                                              public void onItemClick(int position, View v) {
                                                                                  Log.i(LOG_TAG, " Clicked on Item " + position);


                                                                              }
                                                                          });
    }



        void afterExecute(){
        }

    private class LongOperation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            SharedPreferences pres = PreferenceManager.getDefaultSharedPreferences(cont);
            final OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("action", "sell")
                    .add("condition",condition)
                    .add("area",state)
                    .add("subject",subject)
                    .add("token",pres.getString("token", ""))
                    .build();

            Request request = new Request.Builder().url("http://192.168.100.50/kommunisti/search.php")
                    .post(formBody)
                    .build();

            Response response = null;
            try {
                response = client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }

            assertTrue(response.isSuccessful());
            try {
                return response.body().string().toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            items = new ArrayList<String>(Arrays.asList(pilko(result, '\n')));
            items.remove(items.size()-1);
            ArrayList<book> e = new ArrayList<>();
            for(String f : items){
               ArrayList<String> t= new ArrayList<>(Arrays.asList(pilko(f,',')));
               //2 nimi, 3 seller, hinta, kunto, subject, state
                System.out.println(t);
               String name=t.get(1);
               String price=t.get(3);
               String state=t.get(6);
               String kuntot=t.get(4);
               String subject=t.get(5);
                e.add(new book(name, state, kuntot, subject, price));
            }

            books.clear();
            books.addAll(e);
            System.out.println(e);
            mAdapter.notifyDataSetChanged();
            afterExecute();
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
    public static String[] pilko(String in, char cut)
    {
        int subs = 1;
        for(int i = 0; i < in.length(); i++)
        {
            if(in.charAt(i) == cut)
                subs++;
        }

        String[] ret = new String[subs];
        int sub = 0;

        for(int i = 0; i < ret.length; i++)
            ret[i] = "";

        for(int i = 0; i < in.length(); i++)
        {
            if(in.charAt(i) == cut)
                sub++;
            else
                ret[sub] += in.charAt(i);
        }
        return ret;
    }


}