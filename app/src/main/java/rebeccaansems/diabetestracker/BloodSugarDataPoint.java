package rebeccaansems.diabetestracker;

import com.orm.SugarRecord;

import java.util.Date;

/**
 * Created by Rebecca Ansems on 2017-09-23.
 */

public class BloodSugarDataPoint extends SugarRecord<BloodSugarDataPoint> {

    int bloodSugarValue;
    Date dateTime;
    boolean didExercise, isSick, isHoromones;

    public BloodSugarDataPoint(){
    }

    public BloodSugarDataPoint(int bloodSugarValue, Date dateTime){
        this.bloodSugarValue = bloodSugarValue;
        this.dateTime = dateTime;
    }

    public BloodSugarDataPoint(int bloodSugarValue, Date dateTime,
                               boolean didExercise, boolean isSick, boolean isHoromones){
        this.bloodSugarValue = bloodSugarValue;
        this.dateTime = dateTime;
        this.isHoromones = isHoromones;
        this.isSick = isSick;
        this.didExercise = didExercise;
    }




}
