package com.nju.urbangreen.zhenjiangurbangreen.widget.ActionSheet;

import android.graphics.drawable.Drawable;

/**
 * Created by lxs on 17-8-17.
 */

public class ActionItem {
    public int actionID;
    public String title;
    public Drawable icon;

    public ActionItem(int id, String actionTitle, Drawable actionIcon) {
        actionID = id;
        title = actionTitle;
        icon = actionIcon;
    }
}
