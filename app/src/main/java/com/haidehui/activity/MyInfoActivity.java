package com.haidehui.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.haidehui.R;
import com.haidehui.common.Urls;
import com.haidehui.network.http.AsyncHttpClient;
import com.haidehui.network.http.AsyncHttpResponseHandler;
import com.haidehui.network.http.RequestParams;
import com.haidehui.photo_preview.PhotoPreviewAcForOne;
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
import java.util.ArrayList;


/**
 * 我的 --- 我的信息
 */
public class MyInfoActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout rl_photo;
    private RelativeLayout rl_name; // 姓名
    private RelativeLayout rl_work_unit; // 工作单位
        private RelativeLayout rl_weChat_code; // 微信二维码
    private RelativeLayout rl_introduce_myself; // 自我介绍

    private TextView tv_name;
    private TextView tv_work_unit;
    private TextView tv_introduce_myself;
    private CircularImage img_photo; // 用户头像
    private ImageView iv_weChat_code_photo;  // 微信二维码 图片
    private ImageView img_arrow4;
    private int photoType = 1; // 1是头像，2是微信二维码图片

    private String realName;
    private String headPhoto;
    private String workUnit;
    private String weChatPhoto;
    private String introduceMyself;

    private static int GALLERY_REQUEST_CODE = 2; // 表示选择的是相册--2
    private static int CROP_REQUEST_CODE = 3; // 表示选择的是裁剪--3

    private Bitmap newZoomImage;
    private MyHandler mHandler;
    private Thread mthread;

    private final static String IMG_PATH = Environment.getExternalStorageDirectory() + "/haidehui/imgs/"; // 图片保存SD卡位置
    private final static String IMG_PATH_TWO = Environment.getExternalStorageDirectory() + "/haidehui/imgs2/";

    public static final int SELECT_PIC_BY_TACK_PHOTO = 1; // 使用照相机拍照获取图片
    public static final int SELECT_PIC_BY_PICK_PHOTO = 2; // 使用相册中的图片

    private String picPath; // 获取到的图片路径

    private Uri photoUri;

    private static final String TAG = "MyInfoActivity";
    private boolean isUserPhoto;
    private boolean isWechatCodePhoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.activity_info);

        mHandler = new MyHandler();
        mthread = new Thread(myRunnable);

        initTopTitle();
        initView();
        initData();
    }

    private void initTopTitle() {
        TitleBar title = (TitleBar) findViewById(R.id.rl_title);
        title.showLeftImg(true);
        title.setTitle(getResources().getString(R.string.title_null)).setLogo(R.drawable.icons, false).setIndicator(R.drawable.back)
             .setCenterText(getResources().getString(R.string.title_my_info)).showMore(false).setOnActionListener(new TitleBar.OnActionListener() {

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
        img_photo = (CircularImage) findViewById(R.id.iv_photo);

        rl_photo = (RelativeLayout) findViewById(R.id.rl_photo);
        rl_name = (RelativeLayout) findViewById(R.id.rl_name);
        rl_work_unit = (RelativeLayout) findViewById(R.id.rl_work_unit);
//        rl_weChat_code = (RelativeLayout) findViewById(R.id.rl_weChat_code);
        rl_introduce_myself = (RelativeLayout) findViewById(R.id.rl_introduce_myself);

        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_work_unit = (TextView) findViewById(R.id.tv_work_unit);
        tv_introduce_myself = (TextView) findViewById(R.id.tv_introduce_myself);

        img_arrow4 = (ImageView) findViewById(R.id.img_arrow4);
        iv_weChat_code_photo = (ImageView) findViewById(R.id.iv_weChat_code_photo);

        rl_photo.setOnClickListener(this);
        rl_name.setOnClickListener(this);
        rl_work_unit.setOnClickListener(this);
        img_arrow4.setOnClickListener(this);
        iv_weChat_code_photo.setOnClickListener(this);
        rl_introduce_myself.setOnClickListener(this);
    }

    private void initData() {
        realName = getIntent().getStringExtra("realName");
        headPhoto = getIntent().getStringExtra("headPhoto");
        workUnit = getIntent().getStringExtra("workUnit");
        weChatPhoto = getIntent().getStringExtra("weChatPhoto");
        introduceMyself = getIntent().getStringExtra("selfInfo");

        if (!TextUtils.isEmpty(headPhoto)) {
            File file = new File(IMG_PATH);
            if (file.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(IMG_PATH + "Test.png");
                if (bitmap != null) {
                    img_photo.setImageBitmap(bitmap);
                }else {
                    new ImageViewService1().execute(headPhoto);
                }
            } else {
                new ImageViewService1().execute(headPhoto);
            }
        } else {
            img_photo.setImageDrawable(getResources().getDrawable(R.mipmap.user_icon));
        }
        if (!TextUtils.isEmpty(weChatPhoto)) {
            File file = new File(IMG_PATH_TWO);
            if (file.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(IMG_PATH_TWO + "Test2.png");
                if (bitmap != null) {
                    iv_weChat_code_photo.setImageBitmap(bitmap);
                } else {
                    new ImageViewService2().execute(weChatPhoto);
                }
            } else {
                new ImageViewService2().execute(weChatPhoto);
            }
        } else {
            iv_weChat_code_photo.setImageDrawable(getResources().getDrawable(R.mipmap.user_icon));
        }

        tv_name.setText(realName);
        tv_work_unit.setText(workUnit);
        tv_introduce_myself.setText(introduceMyself);
    }

    /**
     * 获取网落图片资源(头像)
     * @return
     */
    class ImageViewService1 extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap data = getHeadPhotoBitmap(params[0]);
            return data;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);

            if (result != null) {
                img_photo.setImageBitmap(result);
                saveBitmap2(result, 1);
            } else {
                img_photo.setImageDrawable(getResources().getDrawable(R.mipmap.user_icon));
            }
        }
    }

    private Bitmap getHeadPhotoBitmap(String url) {
        URL imgUrl = null;
        Bitmap bitmap = null;
        try {
            imgUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) imgUrl.openConnection();
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

    /**
     * 获取网落图片资源(微信二维码图片)
     *
     * @return
     */
    class ImageViewService2 extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap data = getWechatPhotoBitmap(params[0]);
            return data;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);

            if (result != null) {
                iv_weChat_code_photo.setImageBitmap(result);
                saveBitmap2(result, 2);
            } else {
                iv_weChat_code_photo.setImageDrawable(getResources().getDrawable(R.mipmap.user_icon));
            }
        }
    }

    private Bitmap getWechatPhotoBitmap(String url) {
        URL imgUrl = null;
        Bitmap bitmap = null;
        try {
            imgUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) imgUrl.openConnection();
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

    private Uri saveBitmap2(Bitmap bm, int type) {
        File img = null;
        File tmpDir = new File(IMG_PATH);
        if (!tmpDir.exists()) {
            tmpDir.mkdirs();
        }
        if (type == 1) {
            img = new File(IMG_PATH + "headPhoto.png");
        } else if (type == 2) {
            img = new File(IMG_PATH + "weChatPhoto.png");
        }
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

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.rl_photo: // 头像布局
                photoType = 1;
                selectPhoto();
                break;
            case R.id.rl_name: // 用户姓名
                intent = new Intent(this, MyInfoForNameActivity.class);
                intent.putExtra("realName", realName);
                startActivityForResult(intent, 1000);
                break;
            case R.id.rl_work_unit: // 工作单位
                intent = new Intent(this, MyInfoForWorkUnitActivity.class);
                intent.putExtra("workUnit", workUnit);
                startActivityForResult(intent, 2000);
                break;
            case R.id.rl_weChat_code: // 微信二维码
                photoType = 2;
                selectPhoto();
                break;
            case R.id.iv_weChat_code_photo: // 微信二维码图片
//                ArrayList list = new ArrayList<>();
//                list.add(weChatPhoto);
//                intent = new Intent(mContext, PhotoPreviewAcForOne.class);
//                intent.putStringArrayListExtra("urls", list);
//                intent.putExtra("currentPos", 0);
//                startActivity(intent);
                break;
            case R.id.rl_introduce_myself: // 自我介绍
                intent = new Intent(this, MyInfoForIntroduceMyselfActivity.class);
                intent.putExtra("selfInfo", introduceMyself);
                startActivityForResult(intent, 3000);
                break;
        }
    }

    private void selectPhoto() {
        SelectPhotoDialog mDialog = new SelectPhotoDialog(this, new SelectPhotoDialog.OnSelectPhotoChanged() {
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
        if (sdState.equals(Environment.MEDIA_MOUNTED)) {
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
            startActivityForResult(intent, SELECT_PIC_BY_TACK_PHOTO);
        } else {
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
                    photoBmp = getBitmapFormUri(MyInfoActivity.this, photoUri);
                    if (photoBmp != null) {
                        dialog.setmLoadingTip("正在上传照片，请稍后……");
                        startLoading();
                    }
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
        } else if (requestCode == 1000 && resultCode == 1001) {
            String nameData = data.getStringExtra("realName");
            realName = nameData;
            tv_name.setText(nameData);

        } else if (requestCode == 2000 && resultCode == 2001) {
            String workUnitData = data.getStringExtra("workUnit");
            workUnit = workUnitData;
            tv_work_unit.setText(workUnit);

        } else if (requestCode == 3000 && resultCode == 3001) {
            String introduceMyselfData = data.getStringExtra("introduceMyself");
            introduceMyself = introduceMyselfData;
            tv_introduce_myself.setText(introduceMyself);

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
        if ((originalWidth == -1) || (originalHeight == -1)) return null;
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
        if (be <= 0) be = 1;
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
     *
     * @param requestCode
     * @param data
     */
    private void doPhoto(int requestCode, Intent data) {
        if (requestCode == SELECT_PIC_BY_PICK_PHOTO)  //从相册取图片，有些手机有异常情况，请注意
        {
            if (data == null) {
                Toast.makeText(this, "选择图片文件出错", Toast.LENGTH_LONG).show();
                return;
            }
            photoUri = data.getData();
            if (photoUri == null) {
                Toast.makeText(this, "选择图片文件出错", Toast.LENGTH_LONG).show();
                return;
            }
        }
        String[] pojo = {MediaStore.Images.Media.DATA};
        ContentResolver cr = getContentResolver();
        Cursor cursor = cr.query(photoUri, pojo, null, null, null);
//		Cursor cursor = managedQuery(photoUri, pojo, null, null,null);
        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
            cursor.moveToFirst();
            picPath = cursor.getString(columnIndex);
            cursor.close();
        }
        Log.i(TAG, "imagePath = " + picPath);

        if (picPath != null && (picPath.endsWith(".png") || picPath.endsWith(".PNG") || picPath.endsWith(".jpg") || picPath.endsWith(".JPG"))) {

            Bitmap bm = BitmapFactory.decodeFile(picPath);
            newZoomImage = zoomImage(bm, 600, 300);

//			sendImage(image2byte(picPath));
        } else {
            Toast.makeText(this, "选择图片文件不正确", Toast.LENGTH_LONG).show();
        }

    }

    //图片到byte数组
    public byte[] image2byte(String path) {
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
        } catch (FileNotFoundException ex1) {
            ex1.printStackTrace();
        } catch (IOException ex1) {
            ex1.printStackTrace();
        }
        return data;
    }

    public static Bitmap zoomImage(Bitmap bgimage, double newWidth, double newHeight) {
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
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width, (int) height, matrix, true);
        return bitmap;
    }

    /**
     * 调接口 传图片到服务器
     *
     * @param bm
     */
    private void sendImage(Bitmap bm) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bytes = stream.toByteArray();

        String img = new String(Base64.encodeToString(bytes, Base64.DEFAULT));
        try {
            String userPhoto = "";
            String weChatCodePhoto = "";
            String photoTypeStr = "";
            if (photoType == 1) {
                userPhoto = "userPhoto.jpg";
                photoTypeStr = "headPhoto";
            } else if (photoType == 2) {
                weChatCodePhoto = "weChatCodePhoto.jpg";
                photoTypeStr = "wechatPhoto";
            }
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            if (photoType == 1) {
                params.add("photo", img);
                params.add("name", userPhoto);
            } else if (photoType == 2) {
                params.add("photo", img);
                params.add("name", weChatCodePhoto);
            }
            params.add("id", userId);
            params.add("photoType", photoTypeStr);
            String url = Urls.URL_SUBMIT_PHOTO;
            client.post(url, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, String content) {
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
                if (photoType == 1) {
                    img_photo.setImageBitmap(newZoomImage);
                    isUserPhoto = true;
                } else if (photoType == 2) {
                    iv_weChat_code_photo.setImageBitmap(newZoomImage);
                    isWechatCodePhoto = true;
                }
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
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
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
