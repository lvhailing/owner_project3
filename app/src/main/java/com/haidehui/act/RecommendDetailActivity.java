package com.haidehui.act;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Paint;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.haidehui.R;
import com.haidehui.common.Urls;
import com.haidehui.dialog.SelectAddressDialog;
import com.haidehui.model.PartnerIdentify2B;
import com.haidehui.model.SubmitPartnerIdentify2B;
import com.haidehui.network.BaseParams;
import com.haidehui.network.BaseRequester;
import com.haidehui.network.HtmlRequest;
import com.haidehui.network.http.AsyncHttpClient;
import com.haidehui.network.http.AsyncHttpResponseHandler;
import com.haidehui.network.http.RequestParams;
import com.haidehui.photo_preview.PhotoPreviewAcForOne;
import com.haidehui.uitls.DESUtil;
import com.haidehui.uitls.PreferenceUtil;
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
import java.util.LinkedHashMap;

/**
 *  事业合伙人推荐详情
 */
public class RecommendDetailActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_realName;
    private TextView tv_mobile;
    private RelativeLayout layout_address;
    private TextView tv_workProvince;
    private EditText edt_workUnit;
    private EditText edt_email;
    private EditText edt_idNo;
    private RelativeLayout layout_identity_card;
    private ImageView img_identity_card;
    private RelativeLayout layout_card;
    private ImageView img_card;
    private TextView tv_remark;
    private RelativeLayout layout_delete;
    private ImageView img_delete;

    private int photoType = 1;
    private boolean isID;
    private boolean isCard;
    private static int GALLERY_REQUEST_CODE = 2; // 表示选择的是相册--2
    private static int CROP_REQUEST_CODE = 3; // 表示选择的是裁剪--3

    private Bitmap newZoomImage;
    private MyHandler mHandler;
    private Thread mthread;
    private final static String IMG_PATH = Environment.getExternalStorageDirectory() + "/haidehui/imgs/";  // 图片保存SD卡位置
    public static final int SELECT_PIC_BY_TACK_PHOTO = 1; // 使用照相机拍照获取图片
    public static final int SELECT_PIC_BY_PICK_PHOTO = 2; // 使用相册中的图片

    private String picPath; // 获取到的图片路径
    private Uri photoUri;
    private static final String TAG = "PartnerIdentifyActivity";

    private ArrayList<String> list;
    private PartnerIdentify2B data;
    private TextView tv_customer_service_telephone;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.activity_recommend_detail);

        mHandler = new MyHandler();
        mthread = new Thread(myRunnable);
        initTopTitle();
        initView();
        requestData();
    }

    private void initTopTitle() {
        TitleBar title = (TitleBar) findViewById(R.id.rl_title);
        title.showLeftImg(true);
        title.setTitle(getResources().getString(R.string.title_null))
                .setLogo(R.drawable.icons, false).setIndicator(R.drawable.back)
                .setCenterText(getResources().getString(R.string.title_partner_identify))
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
        userId = getIntent().getStringExtra(userId);

        tv_realName= (TextView) findViewById(R.id.tv_real_name);
        tv_mobile= (TextView) findViewById(R.id.tv_mobile);
        layout_address= (RelativeLayout) findViewById(R.id.layout_address);
        tv_workProvince= (TextView) findViewById(R.id.tv_workProvince);
        edt_workUnit= (EditText) findViewById(R.id.edt_workUnit);
        edt_email= (EditText) findViewById(R.id.et_email);
        edt_idNo=(EditText) findViewById(R.id.edt_idNo);
        layout_identity_card= (RelativeLayout) findViewById(R.id.layout_identity_card);
        img_identity_card= (ImageView) findViewById(R.id.img_identity_card);
        layout_card= (RelativeLayout) findViewById(R.id.layout_card);
        img_card= (ImageView) findViewById(R.id.img_card);
        tv_remark= (TextView) findViewById(R.id.tv_remark);
        layout_delete=(RelativeLayout) findViewById(R.id.layout_delete);
        img_delete= (ImageView) findViewById(R.id.img_delete);

        layout_address.setOnClickListener(this);
        layout_identity_card.setOnClickListener(this);
        layout_card.setOnClickListener(this);
        img_delete.setOnClickListener(this);
        img_identity_card.setOnClickListener(this);
        img_card.setOnClickListener(this);
    }

    /**
     * 获取之前的数据
     */
    private void requestData() {
        LinkedHashMap<String, Object> param = new LinkedHashMap<>();
        param.put("userId", userId);
        HtmlRequest.getPartnerIdentify(this, param, new BaseRequester.OnRequestListener() {
                    @Override
                    public void onRequestFinished(BaseParams params) {
                        if (params.result == null) {
                            Toast.makeText(mContext, "加载失败，请确认网络通畅", Toast.LENGTH_LONG).show();
                            return;
                        }
                        data = (PartnerIdentify2B) params.result;
                        setData(data);
                    }

                }
        );
    }

    private void setData(PartnerIdentify2B data) {
        tv_realName.setText(data.getRealName());
        tv_mobile.setText(data.getMobile());

        if (!TextUtils.isEmpty(data.getWorkProvince())){
            tv_workProvince.setText(data.getWorkProvince());
        }
        if (!TextUtils.isEmpty(data.getWorkProvince())){
            tv_workProvince.setText(data.getWorkProvince());
        }
        if (!TextUtils.isEmpty(data.getWorkUnit())){
            edt_workUnit.setText(data.getWorkUnit());
        }
        if (!TextUtils.isEmpty(data.getEmail())){
            edt_email.setText(data.getEmail());
        }
        if (!TextUtils.isEmpty(data.getIdNo())){
            edt_idNo.setText(data.getIdNo());
        }

        String idCardUrl = data.getIdcardFront();
        String cardUrl = data.getBusiCardPhoto();
        if (!TextUtils.isEmpty(idCardUrl)){
            isID=true;
            new ImageViewService().execute(idCardUrl);
        }
        if (!TextUtils.isEmpty(cardUrl)){
            isCard=true;
            new ImageViewServiceCard().execute(cardUrl);
        }

        if (!TextUtils.isEmpty(data.getCheckStatus())){
            if ("init".equals(data.getCheckStatus())){

                img_identity_card.setClickable(false);
                img_card.setClickable(false);

            }else if ("submit".equals(data.getCheckStatus())){//待认证(提交认证信息待审核)  submit状态 所填内容不能编辑
                layout_delete.setVisibility(View.VISIBLE);
                tv_remark.setText("认证审核中，请您耐心等待我们的反馈！");

                layout_address.setClickable(false);
                edt_workUnit.setFocusable(false);
                edt_email.setFocusable(false);
                edt_idNo.setFocusable(false);
                layout_identity_card.setClickable(false);
                layout_card.setClickable(false);
                img_identity_card.setClickable(true);
                img_card.setClickable(true);
            }else if ("success".equals(data.getCheckStatus())){//success状态  已认证置灰  不能编辑  身份证照片可以看
                layout_address.setClickable(false);
                edt_workUnit.setFocusable(false);
                edt_email.setFocusable(false);
                edt_idNo.setFocusable(false);
                layout_identity_card.setClickable(false);
                layout_card.setClickable(false);
                img_identity_card.setClickable(true);
                img_card.setClickable(true);

                if(!TextUtils.isEmpty(data.getIdNo())) {
                    try {
                        PreferenceUtil.setIdNo(DESUtil.encrypt(data.getIdNo()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                }else if ("fail".equals(data.getCheckStatus())){//内容可以编辑  身份证照片变成 可照相状态 提交认证可以点
                layout_delete.setVisibility(View.VISIBLE);
                tv_remark.setText("认证失败，请提交您的真实信息！");
                edt_workUnit.requestFocusFromTouch();
                edt_email.requestFocusFromTouch();
                edt_idNo.requestFocusFromTouch();
                layout_identity_card.setClickable(true);
                layout_card.setClickable(true);
                img_identity_card.setClickable(false);
                img_card.setClickable(false);
            }
        }
    }
    /**
     * 获取网落图片资源
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
                img_identity_card.setImageBitmap(result);
                saveBitmap(result);
            } else {
                img_identity_card.setImageDrawable(getResources().getDrawable(R.mipmap.img_default_picture));
            }
        }

    }
    /**
     * 获取网落图片资源
     *
     * @return
     */
    class ImageViewServiceCard extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap data = getImageBitmap(params[0]);
            return data;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);

            if (result != null) {
                img_card.setImageBitmap(result);
                saveBitmap(result);
            } else {
                img_card.setImageDrawable(getResources().getDrawable(R.mipmap.img_default_picture));
            }
        }

    }
    private Bitmap getImageBitmap(String url) {
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
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_address: // 选择所在地
                SelectAddressDialog dialog=new SelectAddressDialog(this, new SelectAddressDialog.OnExitChanged() {
                    @Override
                    public void onConfim(String selectText) {
                        tv_workProvince.setText(selectText);
                    }

                    @Override
                    public void onCancel() {

                    }
                });
                dialog.show();
                break;
            case R.id.layout_identity_card:
                photoType = 1;
                selectPhoto();
                break;
            case R.id.layout_card:
                photoType = 2;
                selectPhoto();
                break;
            case R.id.img_identity_card:
                list = new ArrayList<>();
                list.add(data.getIdcardFront());
                Intent i_identify = new Intent(mContext, PhotoPreviewAcForOne.class);
                i_identify.putStringArrayListExtra("urls", list);
                i_identify.putExtra("currentPos", 0);
                startActivity(i_identify);

                break;
            case R.id.img_card:
                list = new ArrayList<>();
                list.add(data.getBusiCardPhoto());
                Intent i_card = new Intent(mContext, PhotoPreviewAcForOne.class);
                i_card.putStringArrayListExtra("urls", list);
                i_card.putExtra("currentPos", 0);
                startActivity(i_card);
                break;
            case R.id.btn_submit: // 提交认证
                String workProvince=tv_workProvince.getText().toString();
                String workUnit=edt_workUnit.getText().toString();
                String email=edt_email.getText().toString();
                String idNo=edt_idNo.getText().toString();
//                if (!TextUtils.isEmpty(workProvince)){
//                    if (!TextUtils.isEmpty(workUnit)){
//                        if (!TextUtils.isEmpty(email)){
//                            if (StringUtil.isEmail(email)) {
//                                if (!TextUtils.isEmpty(idNo)) {
//                                    if (IdCardCheckUtils.isIdCard((idNo.toUpperCase()))) {
//                                        if (isID == false) {
//                                            Toast.makeText(PartnerIdentifyActivity.this, "请上传身份证正面", Toast.LENGTH_SHORT).show();
//                                            return;
//                                        }
////                                        if (isCard == false) {
////                                            Toast.makeText(PartnerIdentifyActivity.this, "请上传名片", Toast.LENGTH_SHORT).show();
////                                            return;
////                                        }
//
                                        requestSubmitData(email, idNo, userId, workProvince, workUnit);
//                                    } else {
//                                        Toast.makeText(mContext, "请输入正确的身份证号", Toast.LENGTH_LONG).show();
//                                        edt_idNo.requestFocusFromTouch();
//                                    }
//
//                                } else {
//                                    Toast.makeText(mContext, "请输入您的身份证号", Toast.LENGTH_LONG).show();
//                                    edt_idNo.requestFocusFromTouch();
//                                }
//                            }else{
//                                Toast.makeText(mContext, "请输入正确的邮箱", Toast.LENGTH_LONG).show();
//                                edt_email.requestFocusFromTouch();
//                            }
//
//                        }else{
//                            Toast.makeText(mContext, "请输入您的邮箱", Toast.LENGTH_LONG).show();
//                            edt_email.requestFocusFromTouch();
//                        }
//
//                    }else{
//                        Toast.makeText(mContext, "请输入工作单位", Toast.LENGTH_LONG).show();
//                        edt_workUnit.requestFocusFromTouch();
//                    }
//
//                }else{
//                    Toast.makeText(mContext, "请选择所在地", Toast.LENGTH_LONG).show();
//                }

                break;
            case R.id.img_delete:
                layout_delete.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * 提交认证数据
     */
    private void requestSubmitData(String email,String idNo,String userId,String workProvince,String workUnit) {
        LinkedHashMap<String, Object> param = new LinkedHashMap<>();
        param.put("email", email);
        param.put("idNo", idNo);
        param.put("userId", userId);
        param.put("workProvince", workProvince);
        param.put("workUnit", workUnit);

        HtmlRequest.submitPartnerIdentify(this, param, new BaseRequester.OnRequestListener() {
                    @Override
                    public void onRequestFinished(BaseParams params) {
                        if (params.result == null) {
                            Toast.makeText(mContext, "加载失败，请确认网络通畅", Toast.LENGTH_LONG).show();
                            return;
                        }
                        SubmitPartnerIdentify2B data = (SubmitPartnerIdentify2B) params.result;
                            if ("true".equals(data.getFlag())){
                                Toast.makeText(mContext, data.getMessage(), Toast.LENGTH_LONG).show();
                                layout_delete.setVisibility(View.VISIBLE);
                                tv_remark.setText("认证审核中，请您耐心等待我们的反馈！");

                                layout_address.setClickable(false);
                                edt_workUnit.setFocusable(false);
                                edt_email.setFocusable(false);
                                edt_idNo.setFocusable(false);
                                layout_identity_card.setClickable(false);
                                layout_card.setClickable(false);
                                img_identity_card.setClickable(true);
                                img_card.setClickable(true);
                            }else{
                                Toast.makeText(mContext, data.getMessage(), Toast.LENGTH_LONG).show();
                            }
                    }
                }
        );
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
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
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
                    photoBmp = getBitmapFormUri(RecommendDetailActivity.this, photoUri);
                    if (photoBmp!=null){
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
                    photoBmp = getBitmapFormUri(RecommendDetailActivity.this, mImageCaptureUri);
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
            String idNoName = "";
            String busiName = "";
            String photoTypeStr = "";
            if (photoType == 1) {
                idNoName = "iscardFrount.jpg";
                photoTypeStr="idNoPhoto";
            } else if (photoType == 2) {
                busiName = "busiCardPhoto.jpg";
                photoTypeStr="cardPhoto";
            }
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            if (photoType==1){
                params.add("photo", img);
                params.add("name", idNoName);
            } else if (photoType == 2) {
                params.add("photo", img);
                params.add("name", busiName);
            }
            params.add("id", userId);
            params.add("photoType", photoTypeStr);
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
                if (photoType == 1) {
                    img_identity_card.setImageBitmap(newZoomImage);
                    isID = true;
                } else if (photoType == 2) {
                    img_card.setImageBitmap(newZoomImage);
                    isCard = true;
                }
            } else {
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
