package info.androidhive.loginandregistration.activity;

/**
 * Created by Hyunho on 9/7/17.
 */

public class Attendance {
    private Double target_lat = 37.77116;
    private Double target_long = 128.87024;

    private Double delta = 0.0005;


    public boolean check_attendance(String[] input_location){

        Double current_lat = Double.parseDouble(input_location[0]);
        Double current_long = Double.parseDouble(input_location[1]);

        if(Math.abs(target_lat-current_lat)>=delta & Math.abs(target_long-current_long)>=delta){
            return true;
        }
        return false;
    }

}

