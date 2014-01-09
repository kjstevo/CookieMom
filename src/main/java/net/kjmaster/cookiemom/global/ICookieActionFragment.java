package net.kjmaster.cookiemom.global;


import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: KJ Stevo
 * Date: 11/25/13
 * Time: 1:53 AM
 */
public interface ICookieActionFragment {


    public HashMap<String, String> valuesMap();

    public void refreshView();

    public boolean isRefresh();

}
