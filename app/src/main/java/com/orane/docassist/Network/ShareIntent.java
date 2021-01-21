package com.orane.docassist.Network;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import com.orane.docassist.Model.Model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

public class ShareIntent {

    public String sAux;

    public void ShareApp(Activity activity, String actname) throws IOException {

        try {

            if (actname.equals("MainActivity")) {
                sAux = "I am " + (Model.name) + ". Now available for online consultation at iCliniq. It is #1 online consultation platform. Visit icliniq.com or just download the mobile app on\n\n";
                sAux = sAux + Uri.parse("https://play.google.com/store/apps/details?id=com.orane.docassist");
            }

            if (actname.equals("QasesHome")) {
                sAux = "Discuss medical cases and clinical scenarios with medical doctors of iCliniq. All within Doctor-2-Doctor iCliniq network \n\n";
                sAux = sAux + Uri.parse("https://www.icliniq.com/discuss-case");
            }


            Log.e("actname",actname+" ");
            Log.e("sAux",sAux+" ");
            Date now = new Date();
            long milliseconds = now.getTime();
            String mPath = Environment.getExternalStorageDirectory() + "/" + milliseconds + ".jpg";
            Log.e("mPath",mPath+" ");
            View v1 = activity.getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);
            Log.e("imageFile",imageFile+" ");
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();


            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            Uri uri = Uri.fromFile(imageFile);

            Log.e("uri",uri+" ");

            shareIntent.putExtra(Intent.EXTRA_TEXT, sAux);
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            shareIntent.setType("image/jpeg");
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            activity.startActivity(Intent.createChooser(shareIntent, "send"));


        } catch (Throwable e) {
            //e.printStackTrace();
            System.out.println("Exception from Taking Screenshot---" + e.toString());
            System.out.println("Exception Screenshot---" + e.toString());

            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, sAux);
            activity.startActivity(Intent.createChooser(sharingIntent, "Share using"));
        }
    }




}