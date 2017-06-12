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
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
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
import com.haidehui.dialog.SelectAddressDialog;
import com.haidehui.network.http.AsyncHttpClient;
import com.haidehui.network.http.AsyncHttpResponseHandler;
import com.haidehui.network.http.RequestParams;
import com.haidehui.uitls.AlipayBase64;
import com.haidehui.uitls.DESUtil;
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


/**
 *  事业合伙人认证
 */
public class PartnerIdentifyActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_name;
    private EditText edt_phone;
    private TextView tv_address_left;
    private RelativeLayout layout_address;
    private TextView tv_address;
    private EditText edt_location;
    private EditText edt_email;
    private RelativeLayout layout_identity_card;
    private ImageView img_identity_card;
    private RelativeLayout layout_card;
    private ImageView img_card;
    private Button btn_state;
    private TextView tv_hint;
    private RelativeLayout layout_delete;
    private ImageView img_delete;

    private int photoType = 1;
    private boolean isID,isCard;
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

    private static final String TAG = "PartnerIdentifyActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.ac_partner_identify);
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
        tv_name= (TextView) findViewById(R.id.tv_name);
        edt_phone= (EditText) findViewById(R.id.edt_phone);
        tv_address_left= (TextView) findViewById(R.id.tv_address_left);
        layout_address= (RelativeLayout) findViewById(R.id.layout_address);
        tv_address= (TextView) findViewById(R.id.tv_address);
        edt_location= (EditText) findViewById(R.id.edt_location);
        edt_email= (EditText) findViewById(R.id.edt_email);
        layout_identity_card= (RelativeLayout) findViewById(R.id.layout_identity_card);
        img_identity_card= (ImageView) findViewById(R.id.img_identity_card);
        layout_card= (RelativeLayout) findViewById(R.id.layout_card);
        img_card= (ImageView) findViewById(R.id.img_card);
        btn_state= (Button) findViewById(R.id.btn_state);
        tv_hint= (TextView) findViewById(R.id.tv_hint);
        layout_delete=(RelativeLayout) findViewById(R.id.layout_delete);
        img_delete= (ImageView) findViewById(R.id.img_delete);


    }
    private void initData() {
        layout_address.setOnClickListener(this);
        layout_identity_card.setOnClickListener(this);
        layout_card.setOnClickListener(this);
        btn_state.setOnClickListener(this);
        img_delete.setOnClickListener(this);
    }
/*
    private void requestList() {

        try {
            userId = DESUtil.decrypt(PreferenceUtil.getUserId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        HtmlRequest.getAddressList(AddressManageActivity.this, userId, token,
                new BaseRequester.OnRequestListener() {
                    @Override
                    public void onRequestFinished(BaseParams params) {
                        if (params.result != null) {
                            ResultAddressManageContentBean data = (ResultAddressManageContentBean) params.result;
                            if (data.getAddressList().size() == 0) {
                                vs_inviterecord_switch.setDisplayedChild(1);
                            } else {
                                addressList.clear();
                                addressList.addAll(data.getAddressList());
                                adapter.notifyDataSetChanged();
                                lv_address_manage.getRefreshableView()
                                        .smoothScrollToPositionFromTop(0,
                                                80, 100);
                                lv_address_manage.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        lv_address_manage.onRefreshComplete();
                                    }
                                }, 1000);
                            }
                        } else {
                            vs_inviterecord_switch.setDisplayedChild(1);
                            Toast.makeText(AddressManageActivity.this, "加载失败，请确认网络通畅",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }*/
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_address:
                SelectAddressDialog dialog=new SelectAddressDialog(this, new SelectAddressDialog.OnExitChanged() {
                    @Override
                    public void onConfim(String selectText) {
                        tv_address.setText(selectText);
      //                  selectInfo=selectText.split("-");
                    }

                    @Override
                    public void onCancel() {

                    }
                });
                dialog.show();
                break;
            case R.id.layout_identity_card:
                photoType = 1;
                setPhoto();
                break;
            case R.id.layout_card:
                photoType = 2;
                setPhoto();
                break;
            case R.id.btn_state:

                break;
            case R.id.img_delete:
                layout_delete.setVisibility(View.GONE);
                break;
        }
    }
    private void setPhoto() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择图片");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setPositiveButton("相机", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
				/*Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
				startActivityForResult(intent, CAMERA_REQUEST_CODE);*/

                takePhoto();
            }
        });
        builder.setNeutralButton("相册", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
				/*Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
				intent.setType("image*//*");
				startActivityForResult(intent, GALLERY_REQUEST_CODE);*/

                pickPhoto();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();

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
                    photoBmp = getBitmapFormUri(PartnerIdentifyActivity.this, photoUri);
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
                    photoBmp = getBitmapFormUri(PartnerIdentifyActivity.this, mImageCaptureUri);
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
            String uploadType = "";
            if (photoType == 1) {
                uploadType = "identityFile";
            } else if (photoType == 2) {
                uploadType = "identityFile";

            } else if (photoType == 3) {
                uploadType = "identityFile";
            } else if (photoType == 4) {
                uploadType = "otherFile";
            }
            String fileName = "";
            String fileType = "";
            if (photoType == 1) {
                fileName = "sfzzm.jpg";
                fileType="idCardF1";
            } else if (photoType == 2) {
                fileName = "sfzfm.jpg";
                fileType="idCardF2";
            } else if (photoType == 3) {
                fileName = "withIdCard.jpg";
                fileType="withIdCard";
            } else if (photoType == 4) {
                fileName = "diploma.jpg";
                fileType="diploma";
            }
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.add("photo", img);
            params.add("uploadType", uploadType);
            params.add("fileName", fileName);
            params.add("fileType", fileType);
//          String url = ApplicationConsts.URL_ELITE_UPLOAD+"?token="+ AlipayBase64.encode(token.getBytes("utf-8"));
            String url=null;
            client.post(url, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers,
                                      String content) {
                    super.onSuccess(statusCode, headers, content);
                    try {
                        String data = DESUtil.decrypt(content);
					/*	ResultUploadListBean bean = json.fromJson(data,
								ResultUploadListBean.class);
						ResultUploadListContentBean contentBean = bean
								.getData();
						if (photoType == 1) {
							sfzzmFileName = contentBean.getTmpFileName();
						} else if (photoType == 2) {
							sfzfmFileName = contentBean.getTmpFileName();
						} else if (photoType == 3) {
							cardFileName = contentBean.getTmpFileName();
						} else if (photoType == 4) {
							dkptFileName = contentBean.getTmpFileName();
						} else if (photoType == 5) {
							qzyFileName = contentBean.getTmpFileName();
						}*/

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mthread = new Thread(myRunnable);
                    mthread.start();

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
