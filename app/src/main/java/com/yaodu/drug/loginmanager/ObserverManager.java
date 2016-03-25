package com.yaodu.drug.loginmanager;

import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by bobomee on 2016/3/25.
 */
public class ObserverManager {
    private HashMap<String, ArrayList<MRObserver>> _mapping;
    private Object _lockObj = null;
    private static ObserverManager _instance = null;

    private ObserverManager() {
        _mapping = new HashMap<String, ArrayList<MRObserver>>();
        _lockObj = new Object();
    }

    public synchronized static ObserverManager getInstance() {
        if (_instance == null)
            _instance = new ObserverManager();
        return _instance;
    }

    public void addObserver(String name, MRObserver observer) {
        synchronized (_lockObj) {
            ArrayList<MRObserver> observers;
            if (!_mapping.containsKey(name)) {
                observers = new ArrayList<MRObserver>();
                _mapping.put(name, observers);
            } else {
                observers = _mapping.get(name);
            }
            if (!observers.contains(observer))
                observers.add(observer);
        }
    }

    public void removeObserver(MRObserver observer) {
        synchronized (_lockObj) {
            for (String key : _mapping.keySet()) {
                ArrayList<MRObserver> observers = _mapping.get(key);
                observers.remove(observer);
            }
        }
    }

    public void removeObserver(String name, MRObserver observer) {
        synchronized (_lockObj) {
            if (_mapping.containsKey(name)) {
                ArrayList<MRObserver> observers = _mapping.get(name);
                observers.remove(observer);
            }
        }
    }

    public void removeObserver(String name) {
        synchronized (_lockObj) {
            _mapping.remove(name);
        }
    }

    public ArrayList<MRObserver> getObserver(String name) {
        ArrayList<MRObserver> observers = null;
        synchronized (_lockObj) {
            if (_mapping.containsKey(name)) {
                observers = _mapping.get(name);
            }
        }
        return observers;
    }

    public void notify(String name, Object sender, Object data) {
        synchronized (_lockObj) {
            if (_mapping.containsKey(name)) {
                ArrayList<MRObserver> observers = _mapping.get(name);
                for (MRObserver o : observers) {
                    o.notify(name, sender, data);
                }
            }
        }
    }

    public void notifyAsync(final String name, final Object sender, final Object data) {
        synchronized (_lockObj) {
            if (_mapping.containsKey(name)) {
                Handler handler = new Handler(Looper.getMainLooper());
                final ArrayList<MRObserver> observers = (ArrayList<MRObserver>) _mapping.get(name).clone();
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        for (MRObserver o : observers) {
                            o.notify(name, sender, data);
                        }
                    }
                });

            }
        }
    }

    public void destory() {
        synchronized (_lockObj) {
            _mapping.clear();
        }
    }

    public interface MRObserver {
        void notify(String name, Object sender, Object data);
    }
}
