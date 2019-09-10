package com.wuxiaolong.androidmvpsample.testcard;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.squareup.picasso.Picasso;
import com.wuxiaolong.androidmvpsample.R;
import com.wuxiaolong.androidmvpsample.bean.TestBean;
import com.wuxiaolong.androidmvpsample.mvp.main.MainModel;
import com.wuxiaolong.androidmvpsample.mvp.main.MainPresenter;
import com.wuxiaolong.androidmvpsample.mvp.main.MainView;
import com.wuxiaolong.androidmvpsample.mvp.other.MvpActivity;
import com.wuxiaolong.androidmvpsample.retrofit.RetrofitCallback;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;

import static com.wuxiaolong.androidmvpsample.testcard.CardConfig.SWIPING_LEFT;
import static com.wuxiaolong.androidmvpsample.testcard.CardConfig.SWIPING_RIGHT;
import static java.security.AccessController.getContext;


public class MainActivity extends MvpActivity<MainPresenter> implements MainView {

    private static final String TAG = "MainActivity";
    static TestBean t1 = null;

    RecyclerView recyclerView;
    static boolean isSave = false;
    CardLayoutManager cardLayoutManager;
    static String s1;
    ImageView vb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main2);
        vb = findViewById(R.id.v_bg);

        loadData2();
    }

    @Override
    protected MainPresenter createPresenter() {
        return null;
    }


    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(new MyAdapter());
        CardItemTouchHelperCallback cardCallback = new CardItemTouchHelperCallback(recyclerView.getAdapter(), t1.getData());
        cardCallback.setOnSwipedListener(new OnSwipeListener<TestBean.ResultsBean>() {

            @Override
            public void onSwiping(RecyclerView.ViewHolder viewHolder, float ratio, int direction) {
                MyAdapter.MyViewHolder myHolder = (MyAdapter.MyViewHolder) viewHolder;
                viewHolder.itemView.setAlpha(1 - Math.abs(ratio) * 0.2f);
                if (direction == SWIPING_LEFT) {
                    myHolder.dislikeImageView.setAlpha(Math.abs(ratio));
                } else if (direction == SWIPING_RIGHT) {
                    myHolder.likeImageView.setAlpha(Math.abs(ratio));
                } else {
                    myHolder.dislikeImageView.setAlpha(0f);
                    myHolder.likeImageView.setAlpha(0f);
                }
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, TestBean.ResultsBean o, int direction) {
                MyAdapter.MyViewHolder myHolder = (MyAdapter.MyViewHolder) viewHolder;
                viewHolder.itemView.setAlpha(1f);
                myHolder.dislikeImageView.setAlpha(0f);
                myHolder.likeImageView.setAlpha(0f);
                isSave = direction != CardConfig.SWIPED_LEFT;

                if (isSave){
                    Glide.get(MainActivity.this).clearMemory();
                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.centerCrop();
                    Glide.with(MainActivity.this)
                            .load(s1)
                            .apply(requestOptions)
                            .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            saveImageToGallery(com.wuxiaolong.androidmvpsample.testcard.MainActivity.this, drawable2Bitmap(resource));
                        }
                    });
                }
//                Toast.makeText(MainActivity.this, direction == CardConfig.SWIPED_LEFT ? "swiped left" : "swiped right", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSwipedClear() {
                Toast.makeText(MainActivity.this, "data clear", Toast.LENGTH_SHORT).show();
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadData2();
                        recyclerView.getAdapter().notifyDataSetChanged();
                    }
                }, 3000L);
            }

        });
        final ItemTouchHelper touchHelper = new ItemTouchHelper(cardCallback);
        cardLayoutManager = new CardLayoutManager(recyclerView, touchHelper);
        recyclerView.setLayoutManager(cardLayoutManager);
        touchHelper.attachToRecyclerView(recyclerView);
    }

    private void loadData2() {
        showProgressDialog();
        Call<TestBean> call = apiStores().getImageList();
        call.enqueue(new RetrofitCallback<TestBean>() {
            @Override
            public void onSuccess(TestBean model) {
                dataSuccess(model);
            }

            @Override
            public void onFailure(int code, String msg) {
                toastShow(msg);
            }

            @Override
            public void onThrowable(Throwable t) {
                toastShow(t.getMessage());
            }

            @Override
            public void onFinish() {
                dismissProgressDialog();
            }
        });
        addCalls(call);
    }

    private void dataSuccess(TestBean model) {
        t1 = model;
        initView();
    }

    @Override
    public void getDataSuccess(MainModel model) {

    }

    @Override
    public void getDataSuccess(TestBean model) {

    }

    @Override
    public void getDataSuccess(List<TestBean.ResultsBean> model) {

    }

    @Override
    public void getDataFail(String msg) {

    }

    private class MyAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ImageView i1 = ((MyViewHolder) holder).avatarImageView;
            i1.bringToFront();
            Log.e(TAG, "onBindViewHolder: " +  position);
            getItemCount();
            Glide.with(MainActivity.this)
                    .load(t1.getData().get(position).getUrl())
                    .into(i1);

            s1 = t1.getData().get(position).getUrl();

            switch (position){
                case 0:




                    Log.e(TAG, "t1.getData: " + t1.getData().get(position).getUrl());


                    Glide.with(MainActivity.this)
                            .asBitmap()
                            .listener(new RequestListener<Bitmap>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                                    Bitmap blurBitmap = ImageFilter.blurBitmap(MainActivity.this, resource, 1);

//                                    BitmapFactory.Options options = new BitmapFactory.Options();
//                                    Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.bgpic,options);
//                                    Bitmap bgbm = BlurUtil.doBlur(bitmap1,10,5);
//                                    vb.setImageBitmap(bgbm);
                                    vb.setImageBitmap(blurBitmap);
                                    return false;
                                }
                            })
                            .load(t1.getData().get(position).getUrl())
                            .preload(300, 300);//设置长宽，原图就去掉参数



                    break;
            }
//            avatarImageView.setImageResource(list.get(position));
        }

        @Override
        public int getItemCount() {
            return t1.getData().size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            ImageView avatarImageView;
            ImageView likeImageView;
            ImageView dislikeImageView;

            MyViewHolder(View itemView) {
                super(itemView);
                avatarImageView = (ImageView) itemView.findViewById(R.id.iv_avatar);
                likeImageView = (ImageView) itemView.findViewById(R.id.iv_like);
                dislikeImageView = (ImageView) itemView.findViewById(R.id.iv_dislike);
            }

        }
    }

    /**
     * 获取权限 Permission
     */
    public void getImageCameraPermission() {
        //判断版本
        if (Build.VERSION.SDK_INT >= 23) {
            //检查权限是否被授予：
            int hasExternalPermission = ContextCompat.checkSelfPermission(com.wuxiaolong.androidmvpsample.testcard.MainActivity.this,  Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (hasExternalPermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(com.wuxiaolong.androidmvpsample.testcard.MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 11);
            }
        }
    }


    Bitmap drawable2Bitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof NinePatchDrawable) {
            Bitmap bitmap = Bitmap
                    .createBitmap(
                            drawable.getIntrinsicWidth(),
                            drawable.getIntrinsicHeight(),
                            drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                    : Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());
            drawable.draw(canvas);
            return bitmap;
        } else {
            return null;
        }
    }

    public static String ms2Date(long _ms){
        Date date = new Date(_ms);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.getDefault());
        return format.format(date);
    }

    public static String saveImageToGallery(Context context, Bitmap bmp) {
        Log.d(TAG, "saveImageToGallery: " + bmp);
        String imgpath = Environment.getExternalStorageDirectory().toString() + "/test-down/";
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory().toString(), "picDown");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = "Test:" + ms2Date(System.currentTimeMillis()) + ".jpg" ;
        imgpath += fileName;
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + imgpath)));
        Toast.makeText(context, "保存成功", Toast.LENGTH_SHORT).show();
        return imgpath;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            //就像onActivityResult一样这个地方就是判断你是从哪来的。
            case 12:
                boolean permissionsIsAgree = false;// 许可
                if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    permissionsIsAgree = true;
                }
                if ((grantResults[0] == PackageManager.PERMISSION_GRANTED) && permissionsIsAgree) {
                    getImageCameraPermission(); // 许可
                } else {
                    Toast.makeText(com.wuxiaolong.androidmvpsample.testcard.MainActivity.this, "很遗憾你把相机权限禁用了。", Toast.LENGTH_SHORT).show();
                }
                break;
            case 11:
            case 10:
                if ((grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    getImageCameraPermission();// 许可
                } else {
                    // Permission Denied
                    Toast.makeText(com.wuxiaolong.androidmvpsample.testcard.MainActivity.this, "很遗憾你把相机权限禁用了。", Toast.LENGTH_SHORT).show();
                }
                break;
            case 2:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getImageCameraPermission();
                } else {
                    // Permission Denied
                    Toast.makeText(com.wuxiaolong.androidmvpsample.testcard.MainActivity.this, "很遗憾你把相册权限禁用了", Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }



}
