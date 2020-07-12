package com.example.dell.weather;

/**
 * Created by Dell on 31-05-2020.
 */

public class Model {

    private String cityname, temp, lowTemp, highTemp, windSpeed, humidity, sunrise, sunset, forcast;
//Model model = new Model(city, temp, forcast,tempMin, tempMax, windspeed, humidity, sunrisestring.sunsetstring);
    public Model(String cityname, String temp, String forcast , String lowTemp, String highTemp, String windSpeed, String humidity, String sunrise, String sunset) {
        this.cityname = cityname;
        this.temp = temp;
        this.forcast = forcast;
        this.lowTemp = lowTemp;
        this.highTemp = highTemp;
        this.windSpeed = windSpeed;
        this.humidity = humidity;
        this.sunrise = sunrise;
        this.sunset = sunset;
    }

    public String getForcast() {
        return forcast;
    }

    public void setForcast(String forcast) {
        this.forcast = forcast;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getLowTemp() {
        return lowTemp;
    }

    public void setLowTemp(String lowTemp) {
        this.lowTemp = lowTemp;
    }

    public String getHighTemp() {
        return highTemp;
    }

    public void setHighTemp(String highTemp) {
        this.highTemp = highTemp;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getSunrise() {
        return sunrise;
    }

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }
}
