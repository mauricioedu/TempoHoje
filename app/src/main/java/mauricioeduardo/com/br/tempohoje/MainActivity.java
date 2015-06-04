package mauricioeduardo.com.br.tempohoje;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends ActionBarActivity {


    public static final String TAG = MainActivity.class.getSimpleName();

    private TempoAtual mTempoAtual;

    @InjectView(R.id.timeLabel) TextView mTimeLabel;
    @InjectView(R.id.temperaturaLabel) TextView mTemperaturaLabel;
    @InjectView(R.id.chuvaValor) TextView mChuvaValor;
    @InjectView(R.id.umidadeValor) TextView mUmidadeValor;
    @InjectView(R.id.resumoLabel) TextView mResumoLabel;
    @InjectView(R.id.iconImageView) ImageView mIconImageView;
    @InjectView(R.id.refreshTempo) ImageView mRefreshTempo;
    @InjectView(R.id.progressBar) ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        mProgressBar.setVisibility(View.INVISIBLE);

        final double latitude=-5.098079800000000000;
        final double longitude=-42.833301099999970000;

        mRefreshTempo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getForecast(latitude,longitude);
            }
        });

        getForecast(latitude,longitude);
        Log.d(TAG, "Meu codigo está rodando");

    }

    private void getForecast(double latitude, double longitude) {
        String apiKey = "78cff78ff2fc1e08480a2f14caf6a7a9";


        String ForecastUrl = "https://api.forecast.io/forecast/"
                + apiKey + "/" + latitude + "," + longitude;

        if (isNetworkAvaliable()) {
            resfreshLoading();

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(ForecastUrl)
                    .build();

            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            resfreshLoading();
                        }
                    });
                    alertUserAboutError();
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            resfreshLoading();
                        }
                    });
                    try {
                        String jsonData = response.body().string();
                        Log.v(TAG, jsonData);
                        if (response.isSuccessful()) {
                               mTempoAtual = getTempoAtualDetalhes(jsonData);
                               runOnUiThread(new Runnable() {
                                   @Override
                                   public void run() {
                                       updateDisplay();
                                   }
                               });

                        } else {
                            alertUserAboutError();
                        }
                    }
                    catch (IOException e) {
                        Log.e(TAG, "Causa Da Exceção é: ", e);
                    }
                    catch (JSONException e){
                        Log.e(TAG, "Causa Da Exceção é: ", e);
                    }
                }


            });
        }
        else {
            Toast.makeText(this, getString(R.string.internet_desabilitada_message)
                    , Toast.LENGTH_LONG).show();
        }
    }

    private void resfreshLoading() {
        if (mProgressBar.getVisibility() == View.INVISIBLE){
        mProgressBar.setVisibility(View.VISIBLE);
        mRefreshTempo.setVisibility(View.INVISIBLE);
        }else{
            mProgressBar.setVisibility(View.INVISIBLE);
            mRefreshTempo.setVisibility(View.VISIBLE);
        }
    }

    private void updateDisplay() {
        mTemperaturaLabel.setText(mTempoAtual.getTemperatura() + "");
        mTimeLabel.setText("São " + mTempoAtual.getFormatoTime());
        mUmidadeValor.setText(mTempoAtual.getHumidade() + "");
        mChuvaValor.setText(mTempoAtual.getPrecipitacao() + "%");
        mResumoLabel.setText(mTempoAtual.getResume() + "");

        Drawable drawable = getResources().getDrawable(mTempoAtual.getIconId());
        mIconImageView.setImageDrawable(drawable);
    }

    private TempoAtual getTempoAtualDetalhes(String jsonData) throws JSONException{
       JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");
        Log.i(TAG, "From JSON: " + timezone);

        JSONObject atualmente = forecast.getJSONObject("currently");
        TempoAtual tempoAtual = new TempoAtual();
        tempoAtual.setHumidade(atualmente.getDouble("humidity"));
        tempoAtual.setTime(atualmente.getLong("time"));
        tempoAtual.setIcon(atualmente.getString("icon"));
        tempoAtual.setPrecipitacao(atualmente.getDouble("precipProbability"));
        tempoAtual.setResume(atualmente.getString("summary"));
        tempoAtual.setTemperatura(atualmente.getDouble("temperature"));
        tempoAtual.setTimeZone(timezone);

       Log.d(TAG, tempoAtual.getFormatoTime());

        return tempoAtual;
    }

    private boolean isNetworkAvaliable() {
        ConnectivityManager manager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvaliable=false;
        if (networkInfo != null && networkInfo.isConnected()){
            isAvaliable = true;
        }
        return isAvaliable;
    }

    private void alertUserAboutError() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(), "error_dialog");
    }

}
