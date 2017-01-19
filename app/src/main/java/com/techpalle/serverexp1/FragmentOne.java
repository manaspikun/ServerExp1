package com.techpalle.serverexp1;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentOne extends Fragment {
    //8==initialize
    TextView textView;
    Button button;
    MyTask myTask;
    //9===
    public  boolean checkInternet(){
        //CHK FOR INTERNET CONNECTION
        //A.GET NETWORK MANAGER OBJECT
        ConnectivityManager manager=(ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        //b.from network manager,get activity information
        NetworkInfo networkInfo=manager.getActiveNetworkInfo();
        //c.chk if network is conncted or not
        if(networkInfo==null || networkInfo.isConnected() == false){
            return false;
        }
        return  true;
    }
    //7th=create async task
    public  class MyTask extends AsyncTask<String,Void,String>{
        //11step
        URL myUrl;
        HttpURLConnection connection;
        InputStream inputstream;
        InputStreamReader inputstreamreader;
        BufferedReader br;
        String line;
        StringBuilder result;


        @Override
        protected String doInBackground(String... params) {
            //12step just initialize url and alt enter
            try {
                myUrl = new URL(params[0]);
                //13 step
                connection= (HttpURLConnection) myUrl.openConnection();
                inputstream=connection.getInputStream();
                inputstreamreader=new InputStreamReader(inputstream);
                br=new BufferedReader(inputstreamreader);
                //14 we are noe read data from buffer reader
                result=new StringBuilder();
                line=br.readLine();
                while(line!=null){
                    result.append(line);
                    line=br.readLine();
                }
                //return the complete data tp onpause execute
                return result.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }/*catch (SecurityException e){
                e.printStackTrace();
                return "NO INTERNET PERMISSION";} */



            return null;//something went wrong on conncting to server
        }
        //onpost7.1
        @Override
        protected void onPostExecute(String s) {
            //15
            textView.setText(s);
            super.onPostExecute(s);
        }
    }


    public FragmentOne() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_fragment_one, container, false);
        textView= (TextView) view.findViewById(R.id.textview);
        button= (Button) view.findViewById(R.id.button);
        myTask=new MyTask();//dont exexute or don't start async task immediately
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //10==call above method to check if internet  is there or not
                if (checkInternet()) {
                    if (myTask.getStatus() == AsyncTask.Status.RUNNING || myTask.getStatus() == AsyncTask.Status.FINISHED){
                        Toast.makeText(getActivity(), "Already running plz wait", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    myTask.execute("http://skillgun.com");
                }else {
                    Toast.makeText(getActivity(), "network is not available", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

}

