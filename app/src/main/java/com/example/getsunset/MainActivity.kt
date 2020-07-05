package com.example.getsunset

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun GetSunSet(view: View)
    {
        val lat=enter_text.text.toString();
        val long=enter_text2.text.toString();
        val url="https://api.sunrise-sunset.org/json?lat="+lat+"&lng="+long+"&date=today";
        MyAsyncTask().execute(url);
    }

    inner class MyAsyncTask: AsyncTask<String, String, String>()
    {
        override fun onPreExecute()
        {
            //TODO
        }

        override fun doInBackground(vararg p0: String?): String
        {
            try {
                val url = URL(p0[0]);
                val urlConnect = url.openConnection() as HttpURLConnection;
                urlConnect.connectTimeout = 7000;

                var inString = ConvertStreamToString(urlConnect.inputStream);
                //Cannot be accessed by UI
                publishProgress(inString);
            }
            catch (ex:Exception)
            { }
            return " "
        }

        override fun onProgressUpdate(vararg values: String?) {
            try {
                //Converting file to JSON
                var json=JSONObject(values[0])
                val query=json.getJSONObject("results");
                val sunrise_data=query.getString("sunrise");
                val sunset_data=query.getString("sunset");

                sunrise.text="Sunrise at "+sunrise_data;
                sunset.text="Sunset at "+sunset_data;
            }
            catch(ex:Exception){}
        }

        override fun onPostExecute(result: String?) {
            //after task
        }

    }

    fun ConvertStreamToString(inputstream:InputStream):String
    {
        val bufferReader= BufferedReader(InputStreamReader(inputstream));
        var line:String;
        var AllString:String="";

        try
        {
            do {
                line=bufferReader.readLine()
                if (line!=null)
                {
                    AllString+=line
                }
            }
            while(line!=null)
            inputstream.close()
        }
        catch(ex:Exception){}
        return AllString;
    }
}