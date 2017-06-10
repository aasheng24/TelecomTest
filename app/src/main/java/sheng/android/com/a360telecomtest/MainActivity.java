package sheng.android.com.a360telecomtest;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static String TAG = "CVE-2016-0847";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Button testButton = (Button) findViewById(R.id.testbutton);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCheck();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void startCheck() {
        try {
            if (check1("/system/priv-app/Telecom/oat/arm64/Telecom.odex", "enforcePhoneAccountIsRegisteredEnabled")) {
                Log.d(TAG, "find the method");
                return;
            }
            Log.d(TAG, "can't find the method");
        } catch (Exception e) {
            Log.d(TAG, "FileNotFoundException");
        }
    }

    private  boolean check1(String str, String str2) {
        List arrayList = new ArrayList();
        arrayList.add(str2.getBytes());
        return check2(str, arrayList);
    }

    private  boolean check2(String str, List list) {
        File file = new File(str);
        if (file.exists() && file.isFile()) {
            Log.d(TAG,"enter the file check");
            try {
                RandomAccessFile randomAccessFile = new RandomAccessFile(str, "r");
                byte[] bArr = new byte[((int) randomAccessFile.length())];
                randomAccessFile.read(bArr);
                Iterator iterator = list.iterator();
                while(iterator.hasNext()){
                    byte[] a = (byte[])iterator.next();
                    int ret = check4(bArr, a);
                    Log.d(TAG,"check 4 ret = "+ret);
                    if( ret != -1){
                        return true;
                    }
                }
                return false;
            } catch (Exception e) {
            }
        }
        return false;
    }

    private int[] check3(byte[] bArr){
        Log.d(TAG,"check3 with start = "+new String(bArr));
        int[] iArr = new int[bArr.length];
        int i = 0;
        int i2 = 1;
        while(i2 < bArr.length){
            while(i > 0 && bArr[i] != bArr[i2]){
                i = iArr[i-1];
            }
            if(bArr[i] == bArr[i2]){
                i++;
            }
            iArr[i2] = i;
            i2++;
        }
//        String ret = new String(iArr);
//        Log.d(TAG,"check3 with ret = "+ret);
        return iArr;
    }

    private int check4(byte[] bArr, byte[] bArr2){
        Log.d(TAG,"check4 with bArr start  = "+new String(bArr));
        int i = 0;
        int[] a = check3(bArr2);
        if(bArr.length == 0){
            return -1;
        }
        int i2 = 0;
        while(i < bArr.length){
            while(i2 > 0 && bArr2[i2] != bArr[i]){
                i2 = a[i2-1];
            }
            if(bArr2[i2] == bArr[i]){
                i2++;
            }
            if(i2 == bArr2.length){
                return(i - bArr2.length) + 1;
            }
            i++;
        }
        return -1;
    }
}
