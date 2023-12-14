package com.example.suitcaseapplicationbyoutule.statusbar;

import android.app.Activity;
import android.view.Window;
import android.view.WindowManager;

public class StatusBarUtil {

    public static void setStatusColor(Activity activity, int color) {
        /*Below we have The Window object that represents the window that displays the contents of
          the activity. activity.getWindows() being assigned to the Window object gives the
          access to modify the current properties of the current window */
        Window window = activity.getWindow();

        /*Flags are indicators that draw control to various behaviours and below we just adding a
          flag which indicates that the window should draw the system bar backgrounds */
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(color);
    }

}
