package terminal_heat_sink.airtriggers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    final static String TAG = "TERMINALHEATSINK";
    final static String BROADCAST = "com.asus.airtriggers.NOTIFY_TAP_UI_STATE_CHANGE";
    final static String BROADCAST2 = "com.asus.airtriggers.NOTIFY_TAP_ANIMATE";
    final static String BROADCAST3 = "com.asus.airtriggers.SYSTEMUI_AIR_TRIGGER_ON";
    final static String BROADCAST4 ="com.asus.airtriggers.SYSTEMUI_AIR_TRIGGER_OFF";
    final static String BROADCAST5 ="com.asus.airtriggers.NOTIFY_TAP_SIDE";
    final static String BROADCAST6 ="com.asus.airtriggers.NOTIFY_TAP_TEST_PAGE";
    private TapReciever myReceiver;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        context = this;
        myReceiver = new TapReciever();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BROADCAST);
        intentFilter.addAction(BROADCAST2);
        intentFilter.addAction(BROADCAST3);
        intentFilter.addAction(BROADCAST4);
        intentFilter.addAction(BROADCAST5);
        intentFilter.addAction(BROADCAST6);
        registerReceiver( myReceiver , intentFilter);

        if(check_if_system_app(context).equals("terminal_heat_sink.airtriggers\n")){
            Log.i(TAG, " running in magisk mode");
        }else{
            PackageManager pm = context.getPackageManager();
            String path = "";
            for (ApplicationInfo app : pm.getInstalledApplications(0)) {
                if(app.packageName.equals("terminal_heat_sink.airtriggers")){
                    path = app.sourceDir;
                }
            }
            Log.d(TAG, "path " + path);
            //create_magisk_module(context,path); //doesn't really help at the moment
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myReceiver);
    }

    public static void create_magisk_module(Context context, String path_to_apk){
        write_to_sys("mkdir -p /data/adb/modules/terminalairtriggers/system/priv-app/terminal_heat_sink.airtriggers && " +
                "cp "+path_to_apk+" /data/adb/modules/terminalairtriggers/system/priv-app/terminal_heat_sink.airtriggers/airtriggers.apk && " +
                "echo id=asusrogphone2rgb > /data/adb/modules/terminalairtriggers/module.prop && " +
                "echo name=Airtriggers >> /data/adb/modules/terminalairtriggers/module.prop && " +
                "echo version=v1 >> /data/adb/modules/terminalairtriggers/module.prop && " +
                "echo versionCode=1 >> /data/adb/modules/terminalairtriggers/module.prop && " +
                "echo author=Terminal_Heat_Sink >> /data/adb/modules/terminalairtriggers/module.prop && " +
                "echo description=Terminal Heat Sink Airtriggers >> /data/adb/modules/terminalairtriggers/module.prop && " +
                "pm uninstall terminal_heat_sink.airtriggers\n",context);
    }

    public static String check_if_system_app(Context context){
        return read_from_sys("ls /system/priv-app/ | grep terminal_heat_sink.airtriggers \n", context);
    }

    private static void write_to_sys(String command, Context context){
        Process p;
        try {
            // Preform su to get root privledges
            p = Runtime.getRuntime().exec("su");

            DataOutputStream os = new DataOutputStream(p.getOutputStream());
            os.writeBytes(command);
            // Close the terminal
            os.writeBytes("exit\n");
            os.flush();
            try {
                p.waitFor();
                if (p.exitValue() != 255) {

                    if(p.exitValue() == 0){
                        Log.i("SystemWriter","wrote successfully "+command);

                    }else{
                        Log.i("SystemWriter","failed to write");
                        Toast toast = Toast.makeText(context, "Could not write please allow AsusRogPhone2RGB root access in magisk", Toast.LENGTH_LONG);
                        toast.show();
                    }



                }
                else {
                    Log.i("SystemWriter","not rooted 1");
                    Toast toast = Toast.makeText(context, "root required please root your phone", Toast.LENGTH_SHORT);
                    toast.show();
                }
            } catch (InterruptedException e) {
                Log.i("SystemWriter","not rooted 2");
                Toast toast = Toast.makeText(context, "root required please root your phone", Toast.LENGTH_SHORT);
                toast.show();
            }
        } catch (IOException e) {
            Log.i("SystemWriter","not rooted 3");
            Toast toast = Toast.makeText(context, "root required please root your phone", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private static String read_from_sys(String command, Context context){
        Process p;
        String result = "";
        try {
            // Preform su to get root privledges
            p = Runtime.getRuntime().exec("su");

            DataOutputStream os = new DataOutputStream(p.getOutputStream());
            DataInputStream in = new DataInputStream(p.getInputStream());
            os.writeBytes(command);
            // Close the terminal
            os.writeBytes("exit\n");
            os.flush();
            try {
                p.waitFor();
                if (p.exitValue() != 255) {

                    if(p.exitValue() == 0){
                        Log.i("SystemWriter","read successfully");
                        int i;
                        String output = "";
                        char c;
                        while((i = in.read())!=-1) {
                            c = (char)i;
                            output +=c;
                        }
                        result = output.toString();
                    }else{
                        //Log.i("SystemWriter","failed to read");
                        //Toast toast = Toast.makeText(context, "Could not read files please allow AsusRogPhone2RGB root access in magisk", Toast.LENGTH_LONG);
                        //toast.show();
                    }

                }
                else {
                    Log.i("SystemWriter","not rooted 1");
                    Toast toast = Toast.makeText(context, "root required please root your phone", Toast.LENGTH_SHORT);
                    toast.show();
                }
            } catch (InterruptedException e) {
                Log.i("SystemWriter","not rooted 2");
                Toast toast = Toast.makeText(context, "root required please root your phone", Toast.LENGTH_SHORT);
                toast.show();
            }
        } catch (IOException e) {
            Log.i("SystemWriter","not rooted 3");
            Toast toast = Toast.makeText(context, "root required please root your phone", Toast.LENGTH_SHORT);
            toast.show();
        }
        return result;
    }
}