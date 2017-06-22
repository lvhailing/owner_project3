package com.haidehui.act;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.haidehui.R;
import com.haidehui.common.Urls;
import com.haidehui.network.http.AsyncHttpClient;
import com.haidehui.network.http.AsyncHttpResponseHandler;
import com.haidehui.network.http.RequestParams;
import com.haidehui.widget.CircularImage;
import com.haidehui.widget.SelectPhotoDialog;
import com.haidehui.widget.TitleBar;

import org.apache.http.Header;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 *  我的 --- 我的信息
 */
public class MyInfoActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout layout_photo;
    private CircularImage img_photo;
    private RelativeLayout layout_name;
    private TextView tv_name;
    private String realName;
    private String headPhoto;

    /**
     * 表示选择的是相册--2
     */
    private static int GALLERY_REQUEST_CODE = 2;
    /**
     * 表示选择的是裁剪--3
     */
    private static int CROP_REQUEST_CODE = 3;

    private Bitmap newZoomImage;
    private MyHandler mHandler;
    private Thread mthread;


    /**
     * 图片保存SD卡位置
     */
    private final static String IMG_PATH = Environment
            .getExternalStorageDirectory() + "/haidehui/imgs/";
    /**
     * 图片保存SD卡位置
     */
    private final static String IMG_PATH_TWO = Environment
            .getExternalStorageDirectory() + "/haidehui/imgs2/";

    /***
     * 使用照相机拍照获取图片
     */
    public static final int SELECT_PIC_BY_TACK_PHOTO = 1;
    /***
     * 使用相册中的图片
     */
    public static final int SELECT_PIC_BY_PICK_PHOTO = 2;

    /**获取到的图片路径*/
    private String picPath;

    private Uri photoUri;

    private static final String TAG = "MyInfoActivity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.ac_info);
        mHandler = new MyHandler();
        mthread = new Thread(myRunnable);
        initTopTitle();
        initView();
        initData();
    }

    private void initTopTitle() {
        TitleBar title = (TitleBar) findViewById(R.id.rl_title);
        title.showLeftImg(true);
        title.setTitle(getResources().getString(R.string.title_null))
                .setLogo(R.drawable.icons, false).setIndicator(R.drawable.back)
                .setCenterText(getResources().getString(R.string.title_my_info))
                .showMore(false).setOnActionListener(new TitleBar.OnActionListener() {

            @Override
            public void onMenu(int id) {
            }

            @Override
            public void onBack() {
                finish();
            }

            @Override
            public void onAction(int id) {

            }
        });
    }

    private void initView() {
        realName=getIntent().getStringExtra("realName");
        headPhoto=getIntent().getStringExtra("headPhoto");
        layout_photo= (RelativeLayout) findViewById(R.id.layout_photo);
        img_photo= (CircularImage) findViewById(R.id.img_photo);
        layout_name= (RelativeLayout) findViewById(R.id.layout_name);
        tv_name= (TextView) findViewById(R.id.tv_name);
        tv_name.setText(realName);
        if (!TextUtils.isEmpty(headPhoto)) {
            new ImageViewService().execute(headPhoto);
        } else {
            img_photo.setImageDrawable(getResources()
                    .getDrawable(R.mipmap.user_icon));
        }


    }
    /**
     * 获取网落图片资源
     *
     *
     * @return
     */
    class ImageViewService extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap data = getImageBitmap(params[0]);
            return data;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);

            if (result != null) {
                img_photo.setImageBitmap(result);
                saveBitmap2(result);
            } else {
                img_photo.setImageDrawable(getResources().getDrawable(
                        R.mipmap.user_icon));
            }
        }

    }
    private Bitmap getImageBitmap(String url) {
        URL imgUrl = null;
        Bitmap bitmap = null;
        try {
            imgUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) imgUrl
                    .openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
    private Uri saveBitmap2(Bitmap bm) {
        File tmpDir = new File(IMG_PATH_TWO);
        if (!tmpDir.exists()) {
            tmpDir.mkdirs();
        }
        File img = new File(IMG_PATH_TWO + "Test.png");
        try {
            FileOutputStream fos = new FileOutputStream(img);
            bm.compress(Bitmap.CompressFormat.PNG, 70, fos);
            fos.flush();
            fos.close();
            return Uri.fromFile(img);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    private void initData() {
        layout_photo.setOnClickListener(this);
        layout_name.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_photo:
                selectPhoto();
                break;
            case R.id.layout_name:
                Intent intent = new Intent(this, MyInfoForNameActivity.class);
                intent.putExtra("realName",realName);
                startActivityForResult(intent,1000);
                break;
        }
    }
    private void selectPhoto() {
        SelectPhotoDialog mDialog = new SelectPhotoDialog(this,new SelectPhotoDialog.OnSelectPhotoChanged() {
            @Override
            public void onAlbum() {//相册

                pickPhoto();
            }

            @Override
            public void onCamera() {//相机

                takePhoto();
            }

        });
        mDialog.show();
    }

    /**
     * 拍照获取图片
     */
    private void takePhoto() {
        //执行拍照前，应该先判断SD卡是否存在
        String sdState = Environment.getExternalStorageState();
        if(sdState.equals(Environment.MEDIA_MOUNTED))
        {

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//"android.media.action.IMAGE_CAPTURE"
            /***
             * 需要说明一下，以下操作使用照相机拍照，拍照后的图片会存放在相册中的
             * 这里使用的这种方式有一个好处就是获取的图片是拍照后的原图
             * 如果不实用ContentValues存放照片路径的话，拍照后获取的图片为缩略图不清晰
             */
			/*//设置图片的保存路径,作为全局变量
			String imageFilePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/filename.jpg";
			File temp = new File(imageFilePath);
			photoUri = Uri.fromFile(temp);//获取文件的Uri*/
            ContentValues values = new ContentValues();
            photoUri = this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoUri);
            /**-----------------*/
            startActivityForResult(intent, SELECT_PIC_BY_TACK_PHOTO);
        }else{
            Toast.makeText(this, "内存卡不存在", Toast.LENGTH_LONG).show();
        }
    }
    /***
     * 从相册中取图片
     */
    private void pickPhoto() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

    // 根据用户选择，返回图片资源
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//			doPhoto(requestCode, data);
        if (requestCode == SELECT_PIC_BY_TACK_PHOTO) {
            Bitmap photoBmp = null;

            if (photoUri != null) {
                try {
                    dialog.setmLoadingTip("正在上传照片，请稍后……");
                    startLoading();
                    photoBmp = getBitmapFormUri(MyInfoActivity.this, photoUri);
                    newZoomImage = photoBmp;
                    sendImage(photoBmp);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        } else if (requestCode == GALLERY_REQUEST_CODE) {
            if (data == null) {
                return;
            }
            dialog.setmLoadingTip("正在上传照片，请稍后……");
            startLoading();
            Uri mImageCaptureUri = data.getData();

            Bitmap photoBmp = null;

            if (mImageCaptureUri != null) {

                try {
                    photoBmp = getBitmapFormUri(MyInfoActivity.this, mImageCaptureUri);
                    newZoomImage = photoBmp;
                    sendImage(photoBmp);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        } else if (requestCode == CROP_REQUEST_CODE) {
            if (data == null) {
                return;
            }
            Bundle extras = data.getExtras();
            if (extras == null) {
                return;
            }
            Bitmap bm = extras.getParcelable("data");
            newZoomImage = zoomImage(bm, 600, 300);
//			sendImage(newZoomImage);
        }else if(requestCode==1000 && resultCode==2000){
           String nameData= data.getStringExtra("realName");
            tv_name.setText(nameData);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    /**
     * 通过uri获取图片并进行压缩
     *
     * @param uri
     */
    public static Bitmap getBitmapFormUri(Activity ac, Uri uri) throws FileNotFoundException, IOException {
        InputStream input = ac.getContentResolver().openInputStream(uri);
        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither = true;//optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();
        int originalWidth = onlyBoundsOptions.outWidth;
        int originalHeight = onlyBoundsOptions.outHeight;
        if ((originalWidth == -1) || (originalHeight == -1))
            return null;
        //图片分辨率以480x800为标准
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (originalWidth > originalHeight && originalWidth > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (originalWidth / ww);
        } else if (originalWidth < originalHeight && originalHeight > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (originalHeight / hh);
        }
        if (be <= 0)
            be = 1;
        //比例压缩
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = be;//设置缩放比例
        bitmapOptions.inDither = true;//optional
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        input = ac.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();

        return compressImage(bitmap);//再进行质量压缩
    }
    /**
     * 质量压缩方法
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            //第一个参数 ：图片格式 ，第二个参数： 图片质量，100为最高，0为最差  ，第三个参数：保存压缩后的数据的流
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }
    /**
     * 选择图片后，获取图片的路径
     * @param requestCode
     * @param data
     */
    private void doPhoto(int requestCode,Intent data)
    {
        if(requestCode == SELECT_PIC_BY_PICK_PHOTO )  //从相册取图片，有些手机有异常情况，请注意
        {
            if(data == null)
            {
                Toast.makeText(this, "选择图片文件出错", Toast.LENGTH_LONG).show();
                return;
            }
            photoUri = data.getData();
            if(photoUri == null )
            {
                Toast.makeText(this, "选择图片文件出错", Toast.LENGTH_LONG).show();
                return;
            }
        }
        String[] pojo = {MediaStore.Images.Media.DATA};
        ContentResolver cr = getContentResolver();
        Cursor cursor = cr.query(photoUri, pojo, null, null, null);
//		Cursor cursor = managedQuery(photoUri, pojo, null, null,null);
        if(cursor != null )
        {
            int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
            cursor.moveToFirst();
            picPath = cursor.getString(columnIndex);
            cursor.close();
        }
        Log.i(TAG, "imagePath = " + picPath);

        if(picPath != null && ( picPath.endsWith(".png") || picPath.endsWith(".PNG") ||picPath.endsWith(".jpg") ||picPath.endsWith(".JPG")  ))
        {

            Bitmap bm = BitmapFactory.decodeFile(picPath);
            newZoomImage = zoomImage(bm, 600, 300);

//			sendImage(image2byte(picPath));
        }else{
            Toast.makeText(this, "选择图片文件不正确", Toast.LENGTH_LONG).show();
        }

    }
    //图片到byte数组
    public byte[] image2byte(String path){
        byte[] data = null;
        FileInputStream input = null;
        try {
            input = new FileInputStream(new File(path));
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int numBytesRead = 0;
            while ((numBytesRead = input.read(buf)) != -1) {
                output.write(buf, 0, numBytesRead);
            }
            data = output.toByteArray();
            output.close();
            input.close();
        }
        catch (FileNotFoundException ex1) {
            ex1.printStackTrace();
        }
        catch (IOException ex1) {
            ex1.printStackTrace();
        }
        return data;
    }

    public static Bitmap zoomImage(Bitmap bgimage, double newWidth,
                                   double newHeight) {
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
                (int) height, matrix, true);
        return bitmap;
    }

    private void sendImage(Bitmap bm) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100, stream);
        byte[] bytes = stream.toByteArray();

        String img = new String(Base64.encodeToString(bytes, Base64.DEFAULT));

        try {
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.add("photo", img);
            params.add("name", "headPhoto.jpg");
            params.add("id", "17021511395798036131");
            params.add("photoType", "headPhoto");
            String url = Urls.URL_SUBMIT_PHOTO;
            client.post(url, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers,
                                      String content) {
                    super.onSuccess(statusCode, headers, content);
                    try {
                        mthread = new Thread(myRunnable);
                        mthread.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Throwable error, String content) {
                    super.onFailure(error, content);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            stopLoading();
            if (msg.what == 1) {
                   img_photo.setImageBitmap(newZoomImage);
                }
             else {
            }
        }

    }

    Runnable myRunnable = new Runnable() {
        @Override
        public void run() {
            Message msg = mHandler.obtainMessage();
            msg.what = 1;
            mHandler.sendMessage(msg);
        }
    };

    private Uri saveBitmap(Bitmap bm) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            File tmpDir = new File(IMG_PATH);
            if (!tmpDir.exists()) {
                tmpDir.mkdirs();
            }
            File img = new File(IMG_PATH + "Test.png");
            try {
                FileOutputStream fos = new FileOutputStream(img);
                bm.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.flush();
                fos.close();
                return Uri.fromFile(img);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }

    }



}
