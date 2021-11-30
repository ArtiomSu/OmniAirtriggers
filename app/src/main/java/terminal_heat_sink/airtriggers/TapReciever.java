package terminal_heat_sink.airtriggers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class TapReciever extends BroadcastReceiver {
    final static String TAG = "TERMINALHEATSINK";

    @Override
    public void onReceive(Context paramContext, Intent paramIntent) {
        Log.d("TRIGGERS", " trigger on receive tap state ");
        if (paramIntent != null) {
            String str = paramIntent.getAction();
        } else {
            paramContext = null;
        }
        if (paramContext != null) {
            int i = paramContext.hashCode();
            if (i != 614923017) {
                if (i == 1781738321 && paramContext.equals("com.asus.airtriggers.NOTIFY_TAP_UI_STATE_CHANGE")) {
                    i = paramIntent.getIntExtra("pressure", 0);
                    int j = paramIntent.getIntExtra("side", 0);
                    Log.d(TAG, "pressure is" + i + " side is"+ j);
                }
            } else if (paramContext.equals("com.asus.airtriggers.NOTIFY_TAP_ANIMATE")) {
                i = paramIntent.getIntExtra("side", 0);
                Log.d(TAG, "side is " + i);
            }
        }
    }
}
