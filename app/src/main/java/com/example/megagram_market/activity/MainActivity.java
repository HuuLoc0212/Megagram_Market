package com.example.megagram_market.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.megagram_market.R;
import com.example.megagram_market.adapter.LoaiSpAdapter;
import com.example.megagram_market.adapter.SanPhamMoiAdapter;
import com.example.megagram_market.model.LoaiSp;
import com.example.megagram_market.model.SanPhamMoi;
import com.example.megagram_market.model.SanPhamMoiModel;
import com.example.megagram_market.retrofit.ApiBanHang;
import com.example.megagram_market.retrofit.RetrofitClient;
import com.example.megagram_market.utils.Utils;
import com.google.android.material.navigation.NavigationView;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    ViewFlipper viewFlipper;
    RecyclerView recyleViewManhinhchinh;
    NavigationView navigationView;
    ListView listviewManhinhchinh;
    DrawerLayout drawerLayout;
    LoaiSpAdapter loaiSpAdapter;
    List<LoaiSp> mangloaisp;

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    List<SanPhamMoi> mangSpMoi;
    SanPhamMoiModel spAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        apiBanHang= RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        AnhXa();
        ActionBar();
        ActionViewFlipper();
        if(iConnected(this)){

            ActionViewFlipper();
            Toast.makeText(getApplicationContext(), "Bạn đã kết nối Internet", Toast.LENGTH_LONG).show();
            getLoaiSanPham();
            getSpMoi();
            getEventClick();
        }
        else {
            Toast.makeText(getApplicationContext(), "Bạn đã mất kết nối Internet", Toast.LENGTH_LONG).show();
        }
    }

    private void getSpMoi() {
        compositeDisposable.add(apiBanHang.getSpMoi()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamMoiModel -> {
                            if(sanPhamMoiModel.isSuccess()){
                                mangSpMoi=sanPhamMoiModel.getResult();
                                SanPhamMoiAdapter spAdapter;

                                // Trong phương thức getSpMoi(), khởi tạo adapter với kiểu đúng
                                spAdapter = new SanPhamMoiAdapter(getApplicationContext(), mangSpMoi);
                                recyleViewManhinhchinh.setAdapter(spAdapter);
                            }

                        },throwable -> {
                            Toast.makeText(getApplicationContext(),"Không kết nối được với sever"+throwable.getMessage(),Toast.LENGTH_LONG).show();
                        }

                ));
    }

    private void getLoaiSanPham() {
        compositeDisposable.add(apiBanHang.getLoaiSp()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        loaiSpModel -> {
                            if (loaiSpModel.isSuccess()) {
                                mangloaisp = loaiSpModel.getResult();
//                                Log.d("loggg", loaiSpModel.getResult().get(0).getSanpham()+"");
                                // khoi tao adapter
                                loaiSpAdapter =new LoaiSpAdapter(getApplicationContext(),mangloaisp);
                                listviewManhinhchinh.setAdapter((loaiSpAdapter));
                                loaiSpAdapter.notifyDataSetChanged();
                            }

                        } // hàm xử lý onError găn ứng dụng của mình bị crash khi gặp lỗi,
                        // đồng thời cung cấp phản hồi rõ ràng cho người dùng
                        ,throwable -> {
//                            Log.d("loggg", throwable.getMessage());
                        }
                ));
    }
    private void getEventClick() {
        listviewManhinhchinh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        Intent trangchu= new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(trangchu);
                        break;
                    case 1:
                        Intent dienthoai= new Intent(getApplicationContext(),DienThoaiActivity.class);
                        dienthoai.putExtra("loai",1);
                        startActivity(dienthoai);
                        break;
                    case 2:
                        Intent laptop= new Intent(getApplicationContext(),DienThoaiActivity.class);
                        laptop.putExtra("loai",2);
                        startActivity(laptop);
                        break;
                }
            }
        });
    }

    private void ActionViewFlipper() {
        List<String> mangquangcao = new ArrayList<>();
        mangquangcao.add("https://zshop.vn/images/companies/1/khuyen-mai/2023/7/975x360-Macbook-16%27%27.png?1701054310720");
        mangquangcao.add("https://cdn11.dienmaycholon.vn/filewebdmclnew/DMCL21/Picture/News/News_expe_12507/12507.png?version=232235");
        mangquangcao.add("https://cdnv2.tgdd.vn/mwg-static/common/Campaign/d8/83/d8838f950e99f75f56d0bd7eea3849ed.jpg");
        for (int i = 0; i < mangquangcao.size(); i++) {
            ImageView imageView = new ImageView(getApplicationContext());
            Glide.with(getApplicationContext()).load(mangquangcao.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);
        }
        viewFlipper.setFlipInterval(3000);
        viewFlipper.setAutoStart(true);
        Animation slide_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_right);
        Animation slide_out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_right);
        viewFlipper.setInAnimation(slide_in);
        viewFlipper.setOutAnimation(slide_out);
    }


    private void ActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    private void AnhXa() {
        recyleViewManhinhchinh = findViewById(R.id.recyleViewManhinhchinh);
        navigationView = findViewById(R.id.navigationView);
        toolbar = findViewById(R.id.toolbarmanhinhchinh);
        viewFlipper = findViewById(R.id.viewflipper);
        drawerLayout = findViewById(R.id.drawablelayout);

        RecyclerView.LayoutManager layoutManager= new GridLayoutManager(this, 2);
        recyleViewManhinhchinh.setLayoutManager(layoutManager);
        recyleViewManhinhchinh.setHasFixedSize(true);

        // Initialize list
        recyleViewManhinhchinh = findViewById(R.id.recyleViewManhinhchinh);
        mangloaisp = new ArrayList<>();
        mangSpMoi = new ArrayList<>();
        // Initialize adapter
        navigationView = findViewById(R.id.navigationView);
        loaiSpAdapter = new LoaiSpAdapter(getApplicationContext(), mangloaisp);
        listviewManhinhchinh = findViewById(R.id.listviewManhinhchinh);
        if (Utils.manggiohang == null){
            Utils.manggiohang = new ArrayList<>();
        }


    }
    private boolean iConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // API 23 trở lên
                Network network = connectivityManager.getActiveNetwork();
                if (network != null) {
                    NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
                    return networkCapabilities != null &&
                            (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR));
                }
            } else {
                NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                return (wifi != null && wifi.isConnected()) ||
                        (mobile != null && mobile.isConnected());
            }
        }
        return false;
    }
}