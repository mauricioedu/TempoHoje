package mauricioeduardo.com.br.tempohoje;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by mauricioedu on 20/04/2015.
 */
public class TempoAtual {
    private String mIcon;
    private long mTime;
    private double mTemperatura;
    private double mHumidade;
    private double mPrecipitacao;
    private String mResume;
    String mTimeZone;

   public String getTimeZone() {
        return mTimeZone;
    }

    public void setTimeZone(String timeZone) {
        mTimeZone = timeZone;
    }



    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }

    public int getIconId() {
        int iconId = R.drawable.clear_day;

        if (mIcon.equals("clear-day")) {
            iconId = R.drawable.clear_day;
        } else if (mIcon.equals("clear-night")) {
            iconId = R.drawable.clear_night;
        } else if (mIcon.equals("rain")) {
            iconId = R.drawable.rain;
        } else if (mIcon.equals("snow")) {
            iconId = R.drawable.snow;
        } else if (mIcon.equals("sleet")) {
            iconId = R.drawable.sleet;
        } else if (mIcon.equals("wind")) {
            iconId = R.drawable.wind;
        } else if (mIcon.equals("fog")) {
            iconId = R.drawable.fog;
        } else if (mIcon.equals("cloudy")) {
            iconId = R.drawable.cloudy;
        } else if (mIcon.equals("partly-cloudy-day")) {
            iconId = R.drawable.partly_cloudy;
        } else if (mIcon.equals("partly-cloudy-night")) {
            iconId = R.drawable.cloudy_night;
        }
        return iconId;
    }
    public long getTime() {
        return mTime;
    }

    public String getFormatoTime(){
        SimpleDateFormat formato = new SimpleDateFormat("h:mm a");
        formato.setTimeZone(TimeZone.getTimeZone(getTimeZone()));
        Date dateTime = new Date(getTime() * 1000);
        String timeString = formato.format(dateTime);

        return timeString;
    }

    public void setTime(long time) {
        mTime = time;
    }

    public int getTemperatura() {
        mTemperatura = ((mTemperatura -32)*5)/9;
        return (int)Math.round(mTemperatura);
    }

    public void setTemperatura(double temperatura) {
        mTemperatura = temperatura;
    }

    public double getHumidade() {
        return mHumidade;
    }

    public void setHumidade(double humidade) {
        mHumidade = humidade;
    }

    public int getPrecipitacao() {
        Double prePrecip = mPrecipitacao *100;
        return (int)Math.round(prePrecip);
    }

    public void setPrecipitacao(double precipitacao) {
        mPrecipitacao = precipitacao;
    }

    public String getResume() {
        return mResume;
    }

    public void setResume(String resume) {
        mResume = resume;
    }


}
