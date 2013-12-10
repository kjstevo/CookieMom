package net.kjmaster.cookiemom.ui;

import java.util.HashMap;
import java.util.Observable;


/**
 * Created with IntelliJ IDEA.
 * User: KJ Stevo
 * Date: 12/6/13
 * Time: 9:18 PM
 */
@SuppressWarnings("UnusedDeclaration")
public class ObservableValues extends Observable {

    private final HashMap<String, String> valMap = new HashMap<String, String>();

    public HashMap<String, String> getValMap() {
        return valMap;
    }

    public void addValue(String key, String value) {
        valMap.put(key, value);
        this.setChanged();
        this.notifyObservers(valMap);

    }

    public void addAllValues(HashMap<String, String> hashMap) {
        valMap.putAll(hashMap);
    }

}
