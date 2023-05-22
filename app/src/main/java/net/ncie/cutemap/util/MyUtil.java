package net.ncie.cutemap.util;

import android.content.Context;
import android.widget.Toast;

public class MyUtil {
    public static void MyLog(Object s){
        System.out.println("----*--------*"+s);

    }

    public static void MyToast(Context context,String s){
        Toast toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        toast.setText(s);
        toast.show();
    }
}
