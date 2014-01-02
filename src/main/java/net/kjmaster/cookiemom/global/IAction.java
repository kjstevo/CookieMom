package net.kjmaster.cookiemom.global;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: KJ Stevo
 * Date: 11/14/13
 * Time: 6:48 PM
 */
public interface IAction {
    @NotNull
    public Boolean isCardVisible();

    @NotNull
    public List<?> getActionList();

    public String getActionTitle();

    public String getHeaderText();

}
