package com.example.abc123.my12306.Ticket;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abc123.my12306.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class CityActivity extends AppCompatActivity {

    private ListView mCityLit;
    private TextView overlay;
    private QuicLocationBar mQuicLocationBar;
    private HashMap<String, Integer> alphaIndexer;
    private ArrayList<CityBean> mCityNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);

        mQuicLocationBar = (QuicLocationBar) findViewById(R.id.city_loactionbar);
        mQuicLocationBar
                .setOnTouchLitterChangedListener(new LetterListViewListener());
        overlay = (TextView) findViewById(R.id.city_dialog);
        mCityLit = (ListView) findViewById(R.id.city_list);
        mQuicLocationBar.setTextDialog(overlay);
        try {
            initList();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void initList() throws IOException {
        mCityNames = getCityNames();
        CityAdapter adapter = new CityAdapter(CityActivity.this, mCityNames);
        mCityLit.setAdapter(adapter);
        alphaIndexer = adapter.getCityMap();
        mCityLit.setOnItemClickListener(new CityListOnItemClick());
    }

    private ArrayList<CityBean> getCityNames() throws IOException {
        InputStream jsoninputStream = getResources().openRawResource(R.raw.city);
        byte[] buffer=new byte[jsoninputStream.available()];
        jsoninputStream.read(buffer);
        String json=new String(buffer,"UTF-8");
        ArrayList<CityBean> names = new ArrayList<CityBean>();
        JSONObject jsonA= null;
        try {
            jsonA = new JSONObject(json);
            JSONArray city=jsonA.getJSONArray("city");
            for (int i=0;i<city.length();i++){
                JSONObject obj=city.getJSONObject(i);
                JSONArray list=obj.getJSONArray("list");
                Log.d("initial", obj.getString("initial"));
                for (int j=0;j<list.length();j++){
                    CityBean cityModel = new CityBean();
                    JSONObject obj2=list.getJSONObject(j);
                    cityModel.setNameSort(obj.getString("initial"));
                    cityModel.setCityName(obj2.getString("name"));
                    names.add(cityModel);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }finally {
            jsoninputStream.close();
        }
        return names;
    }

    private class CityListOnItemClick implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
                                long arg3) {
            CityBean cityModel = (CityBean) mCityLit.getAdapter()
                    .getItem(pos);
            Toast.makeText(CityActivity.this, cityModel.getCityName(),
                    Toast.LENGTH_SHORT).show();
        }

    }

    private class LetterListViewListener implements
            QuicLocationBar.OnTouchLetterChangedListener {

        @Override
        public void touchLetterChanged(String s) {
            // TODO Auto-generated method stub
            if (alphaIndexer.get(s) != null) {
                int position = alphaIndexer.get(s);
                mCityLit.setSelection(position);
            }
        }

    }

}
