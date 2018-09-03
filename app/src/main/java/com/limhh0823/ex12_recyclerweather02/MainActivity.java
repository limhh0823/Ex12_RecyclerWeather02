package com.limhh0823.ex12_recyclerweather02;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.limhh0823.ex12_recyclerweather02.Utils.ApiService;
import com.limhh0823.ex12_recyclerweather02.Utils.PermitGps;
import com.limhh0823.ex12_recyclerweather02.Utils.SetRetrofit;
import com.limhh0823.ex12_recyclerweather02.WeeklyData.WeeklyWeather;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements LocationListener{

    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    private ArrayList<WeeklyModel> weeklyModels = new ArrayList<>();
    private LocationManager lm;
    private Gson gson = new Gson();
    private Call<JsonObject> call;
    private WeeklyWeather data;
    private double lat = 0.0;
    private double lon = 0.0;
    public static final String ICON_BASEURL = "http://motive.co.kr/images/weather/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.rv_wrap);

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(lm != null) {
            PermitGps.check(this,lm, 1000, 5);
        }
    }

    private void initData() {
        call = SetRetrofit
                .retrofit().create(ApiService.class).getWeeklyWeather(ApiService.APPID, lat, lon, ApiService.UNIT);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()) {
                    data = gson.fromJson(response.body(), WeeklyWeather.class);
                    for(int i=0; i<data.getCnt(); i++) {
                        String icon = ICON_BASEURL+data.getList().get(i).getWeather().get(0).getIcon() + ".png";
                        String dt = data.getList().get(i).getDtTxt();
                        int time = Integer.parseInt(dt.substring(11, 13)) + 9;
                        if(time >= 24) {
                            time -= 24;
                        }//time = time - 24;
                        String date = Integer.parseInt(dt.substring(5,7))+"월 ";
                        date += Integer.parseInt(dt.substring(8, 10))+"일 ";
                        date += time+"시";
                        String temp = data.getList().get(i).getMain().getTemp()+"도";
                        String tempMax = data.getList().get(i).getMain().getTempMax()+"도";
                        String tempMin = data.getList().get(i).getMain().getTempMin()+"도";
                        String deg = Integer.parseInt(String.valueOf(Math.round(data.getList().get(i).getWind().getDeg()))) + "deg";
                        String speed = data.getList().get(i).getWind().getSpeed() + "ms";
                        String detail = deg + " / " + speed;
                        String summary = data.getList().get(i).getWeather().get(0).getMain();
                        summary += " / " + data.getList().get(i).getWeather().get(0).getDescription();
                        weeklyModels.add(new WeeklyModel(icon, temp, tempMin, tempMax, date, detail, summary));
                    }
                    initAdapter();
                }
                else {
                    Toast.makeText(getApplicationContext(), "데이터 이상", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "통신 실패", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initAdapter() {
        adapter = new RecyclerAdapter(this, weeklyModels);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        lon = location.getLongitude();
        initData();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}



        /*for(int i=0, j=1; i<100; i++, j++) {
            if(j%4 == 1) j = 1;
            String icon = ICON_BASEURL+"0"+j+"d.png";
            String temp = (25+j)+"도";
            String date = "8월 22일 15:00";
            String tempMin = "최저기온: 22도";
            String tempMax = "최고기온: 30도";
            String detail = "바람: 36도 / 11.3ms";
            String summary = "Clear Sky / Light Rain";
            WeeklyModel item = new WeeklyModel(icon, temp, tempMin, tempMax, date, detail, summary);
            weeklyModels.add(item);
        }*/