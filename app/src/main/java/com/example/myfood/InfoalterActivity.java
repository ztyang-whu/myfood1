package com.example.myfood;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.method.LoadImage;
import com.example.utils.SystemDBManager;
import com.example.utils.UsersDBManager;
import com.example.utils.myapplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InfoalterActivity extends Activity {

    private myapplication myapplication1;
    private UsersDBManager UsersDBManager1;
    private SystemDBManager systemDBManager;
    private ProgressDialog ProgressDialog1; // 加载对话框
    private ImageView StarView;

    private TextView TextView1;  //头像标签
    private ImageView personImage;  //头像

    private TextView TextView2;  //昵称标签
    private EditText nameEditText;  //昵称编辑

    private TextView TextView3;  //账号标签
    private TextView idTextView;  //账号编辑

    private TextView TextView4;  //性别标签
    private EditText sexEditText;  //性别编辑

    private Spinner mSpinner;
    private String address;
    private Button infoalter_submit_button;

    private CharSequence[] sel={"拍照","从相册中选择"};
    public static final int TAKE_PHOTO=1;
    public static final int CROP_PHOTO=2;
    public static final int SELECT_PIC=0;
    private Uri imageUri;
    private String filename;
    private Bitmap photo;

    private Bundle bundle;

    private static Handler handler=new Handler();

    public static void enableStrictMode(InfoalterActivity infoalterActivity) {
        StrictMode.setThreadPolicy(
                new StrictMode.ThreadPolicy.Builder()
                        .detectDiskReads()
                        .detectDiskWrites()
                        .detectNetwork()
                        .penaltyLog()
                        .build());
        StrictMode.setVmPolicy(
                new StrictMode.VmPolicy.Builder()
                        .detectLeakedSqlLiteObjects()
                        .penaltyLog()
                        .build());
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        enableStrictMode(InfoalterActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infoalter);
        myapplication1 = (myapplication) getApplication();
        myapplication1.getInstance().addActivity(this);
        bundle=this.getIntent().getExtras();
        UsersDBManager1 = new UsersDBManager(this);
        systemDBManager=new SystemDBManager(this);

        TextView1 = (TextView) findViewById(R.id.infoalter_photo_title);
        personImage = (ImageView) findViewById(R.id.infoalter_photo_image);
        TextView2 = (TextView) findViewById(R.id.infoalter_name_title);
        nameEditText = (EditText) findViewById(R.id.infoalter_name_text);
        TextView3 = (TextView) findViewById(R.id.infoalter_id_title);
        idTextView = (TextView) findViewById(R.id.infoalter_id_text);
        TextView4 = (TextView) findViewById(R.id.infoalter_sex_title);
        sexEditText = (EditText) findViewById(R.id.infoalter_sex_text);


        mSpinner = (Spinner) findViewById(R.id.infoalter_department_text);
        String[] items=getResources().getStringArray(R.array.userinfo_department);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,items);
        mSpinner.setAdapter(adapter);

        infoalter_submit_button=(Button)findViewById(R.id.infoalter_submit_button);

        idTextView.setText(myapplication1.getusername());
        nameEditText.setText(bundle.getString("userName"));
        sexEditText.setText(bundle.getString("userSex"));

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                address=adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        personImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new AlertDialog.Builder(InfoalterActivity.this).setTitle("更换头像")
                        .setItems(sel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent=new Intent();
                                switch(which)
                                {
                                    case 0:
                                        //ActivityCompat.requestPermissions(InfoalterActivity.this, new String[]{android.Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                                        SimpleDateFormat format=
                                                new SimpleDateFormat("yyyyMMddHHmmss");
                                        Date date=new Date(System.currentTimeMillis());
                                        filename=format.format(date);
                                        File path=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                                        File outputImage=new File(path, filename+".jpg");
                                        try{
                                            if(outputImage.exists())
                                            {
                                                outputImage.delete();
                                            }
                                            outputImage.createNewFile();
                                        }catch(IOException e){
                                            e.printStackTrace();
                                        }
                                        imageUri=Uri.fromFile(outputImage);
                                        Intent tIntent=new Intent("android.media.action.IMAGE_CAPTURE");
                                        tIntent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                                        startActivityForResult(tIntent,TAKE_PHOTO);
                                        break;
                                    case 1:
                                        intent.setAction(Intent.ACTION_GET_CONTENT);
                                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                                        intent.setType("image/*");
                                        intent.putExtra("return-data",true);
                                        intent.putExtra("crop","true");

                                        intent.putExtra("aspectX",1);
                                        intent.putExtra("aspaceY",1);

                                        intent.putExtra("outputX",450);
                                        intent.putExtra("outputY",450);
                                        startActivityForResult(intent,SELECT_PIC);
                                        break;
                                }
                            }
                        }).create().show();
            }
        });
        infoalter_submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(InfoalterActivity.this,"修改信息开始",Toast.LENGTH_SHORT);
                    }
                });
                Thread t=new Thread(new Runnable() {
                    @Override
                    public void run() {

                        String result=UserToJson();
                        if(result==""){
                            return;
                        }
                        else{
                            HttpURLConnection conn=null;
                            try{
                                String path="http://"+getString(R.string.ip)+"/test/setUserServlet";
                                conn=(HttpURLConnection) new URL(path).openConnection();
                                conn.setConnectTimeout(3000);
                                conn.setDoInput(true);
                                conn.setDoOutput(true);
                                conn.setRequestMethod("POST");
                                conn.setUseCaches(false);
                                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                                //设置请求体的长度
                                conn.setRequestProperty("Content-Length", String.valueOf(result.getBytes().length));
                                //获得输出流，向服务器写入数据
                                OutputStream outputStream = conn.getOutputStream();
                                outputStream.write(result.getBytes());
                                int response = conn.getResponseCode();            //获得服务器的响应码
                                if(response == HttpURLConnection.HTTP_OK) {
                                    InputStream inputStream = conn.getInputStream();
                                    String judge=new String(read(inputStream),"UTF-8");
                                    JSONObject result1= new JSONObject(judge.substring(judge.indexOf("{"), judge.lastIndexOf("}") + 1));
                                    int r=result1.getInt("result");
                                    if(r==1){
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                systemDBManager.updateaddress(address);
                                                Toast.makeText(InfoalterActivity.this,"修改信息成功",Toast.LENGTH_SHORT);
                                            }
                                        });
                                        Intent intent = new Intent();
                                        intent.setClass(InfoalterActivity.this, UserInfoActivity.class);
                                        startActivity(intent);
                                        overridePendingTransition(android.R.anim.slide_in_left,
                                                android.R.anim.slide_out_right);
                                        finish();
                                    }
                                    else if(r==0){
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(InfoalterActivity.this,"修改信息失败",Toast.LENGTH_SHORT);
                                            }
                                        });
                                    }
                                }
                            }catch(Exception e){
                                e.printStackTrace();
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(InfoalterActivity.this,"修改信息失败",Toast.LENGTH_SHORT);
                                    }
                                });
                            }finally {
                                if(conn!=null){
                                    conn.disconnect();
                                }
                            }
                        }
                    }
                });
                t.start();
            }
        });
        load();

    }

    private String UserToJson(){
        String jsonresult="";
        JSONObject object=new JSONObject();
        JSONArray jsonArray=new JSONArray();
        JSONObject jsonObj=new JSONObject();
        try{
            jsonObj.put("User_ID",idTextView.getText());
            jsonObj.put("User_name",nameEditText.getText());
            jsonObj.put("User_head",convertIconToString(photo));
            jsonObj.put("User_sex",sexEditText.getText());
            //jsonObj.put("User_star",);
            jsonObj.put("User_addr",address);
            jsonArray.put(jsonObj);
            object.put("user",jsonArray);
            jsonresult=object.toString();
            return jsonresult;
        }catch(JSONException e){
            e.printStackTrace();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(InfoalterActivity.this,"JSON封装出错",Toast.LENGTH_SHORT);
                }
            });
            jsonresult="";
        }
        return jsonresult;
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK)
            return;
        switch (requestCode)
        {
            case SELECT_PIC://相册
                ParcelFileDescriptor parcelFD = null;
                try {
                    parcelFD = getContentResolver().openFileDescriptor(data.getData(), "r");
                    FileDescriptor imageSource = parcelFD.getFileDescriptor();

                    // Decode image size
                    BitmapFactory.Options o = new BitmapFactory.Options();
                    o.inJustDecodeBounds = true;
                    BitmapFactory.decodeFileDescriptor(imageSource, null, o);

                    // the new size we want to scale to
                    final int REQUIRED_SIZE = 1024;

                    // Find the correct scale value. It should be the power of 2.
                    int width_tmp = o.outWidth, height_tmp = o.outHeight;
                    int scale = 1;
                    while (true) {
                        if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE) {
                            break;
                        }
                        width_tmp /= 2;
                        height_tmp /= 2;
                        scale *= 2;
                    }
                    BitmapFactory.Options o2 = new BitmapFactory.Options();
                    o2.inSampleSize = scale;
                    photo = BitmapFactory.decodeFileDescriptor(imageSource, null, o2);

                    personImage.setImageBitmap(photo);

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (parcelFD != null)
                        try {
                            parcelFD.close();
                        } catch (IOException e) {
                            // ignored
                        }
                }
                break;
            case TAKE_PHOTO://相机
                try {
                    Intent intent = new Intent("com.android.camera.action.CROP"); //剪裁
                    intent.setDataAndType(imageUri, "image/*");
                    intent.putExtra("scale", true);
                    //设置宽高比例
                    intent.putExtra("aspectX", 1);
                    intent.putExtra("aspectY", 1);
                    //设置裁剪图片宽高
                    intent.putExtra("outputX", 450);
                    intent.putExtra("outputY", 450);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    Toast.makeText(InfoalterActivity.this, "剪裁图片", Toast.LENGTH_SHORT).show();
                    //广播刷新相册
                    Intent intentBc = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    intentBc.setData(imageUri);
                    this.sendBroadcast(intentBc);
                    startActivityForResult(intent, CROP_PHOTO); //设置裁剪参数显示图片至ImageView
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case CROP_PHOTO:
                try {
                    //图片解析成Bitmap对象
                    photo = BitmapFactory.decodeStream(
                            getContentResolver().openInputStream(imageUri));

                    personImage.setImageBitmap(photo);
                } catch(FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    public void load(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    photo=LoadImage.loadimage("http://192.168.43.150:8080/test/img/user/defaultHead.png");
                    personImage.setImageBitmap(photo);
                }catch (Exception e){

                }
            }
        }).start();
    }

    public static String convertIconToString(Bitmap bitmap)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] appicon = baos.toByteArray();// 转为byte数组
        return Base64.encodeToString(appicon, Base64.DEFAULT);
    }

    private static byte[] read(InputStream inStream) throws Exception{
        ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
        byte[] buffer=new byte[1024];
        int len=0;
        while((len=inStream.read(buffer))!=-1){
            outputStream.write(buffer,0,len);
        }
        inStream.close();
        return outputStream.toByteArray();
    }
}
