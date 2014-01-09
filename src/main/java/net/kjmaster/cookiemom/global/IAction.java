package net.kjmaster.cookiemom.global;


import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: KJ Stevo
 * Date: 11/14/13
 * Time: 6:48 PM
 */
public interface IAction {

    public Boolean isCardVisible();


    public List<?> getActionList();

    public String getActionTitle();

    public String getHeaderText();

}
