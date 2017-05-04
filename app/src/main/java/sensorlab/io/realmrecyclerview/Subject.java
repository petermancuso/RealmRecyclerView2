package sensorlab.io.realmrecyclerview;

import android.location.Location;

/**
 * Created by pm on 12/21/2016.
 */

public interface Subject {
    public void registerObserver(Observer o);
    public void removeObserver(Observer o);
    public void notifyObservers(Location location);

}
