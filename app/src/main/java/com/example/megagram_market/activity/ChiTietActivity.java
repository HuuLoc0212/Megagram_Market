package com.example.megagram_market.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.megagram_market.R;
import com.example.megagram_market.model.GioHang;
import com.example.megagram_market.model.SanPhamMoi;
import com.example.megagram_market.utils.Utils;
import com.nex3z.notificationbadge.NotificationBadge;

import java.text.DecimalFormat;

public class ChiTietActivity extends AppCompatActivity {

    TextView tensp, giasp, mota;
    Button btnThem;
    ImageView imghinhanh;
    Spinner spinner;
    Toolbar toolbar;
    SanPhamMoi sanPhamMoi;
    NotificationBadge badge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chi_tiet);
        initview();
        ActionToolbar();
        initData();
        initControl();
    }

    private void ActionToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initData() {
        sanPhamMoi = (SanPhamMoi) getIntent().getSerializableExtra("chitiet");
        tensp.setText(sanPhamMoi.getTensp());
        mota.setText(sanPhamMoi.getMota());
        Glide.with(getApplicationContext()).load(sanPhamMoi.getHinhanh()).into(imghinhanh);

        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");

        // Chọn số lượng sản phẩm trong spinner
        Integer[] so = new Integer[]{1, 2, 3, 4, 5};
        ArrayAdapter<Integer> adapterspin = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, so);
        spinner.setAdapter(adapterspin);
    }

    private void initControl() {
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                themGioHang();
            }
        });
    }

    private void themGioHang() {
        int soluong = Integer.parseInt(spinner.getSelectedItem().toString());

        // Xóa các dấu phẩy khỏi chuỗi giá trị giá sản phẩm
        String giaSanPham = sanPhamMoi.getGiasp().replace(",", "");
        long gia = Long.parseLong(giaSanPham) * soluong;

        boolean flag = false;

        // Kiểm tra xem sản phẩm có trong giỏ hàng hay không
        for (int i = 0; i < Utils.manggiohang.size(); i++) {
            if (Utils.manggiohang.get(i).getIdsp() == sanPhamMoi.getId()) {
                // Nếu sản phẩm đã tồn tại, cập nhật số lượng và giá
                Utils.manggiohang.get(i).setSoluong(soluong + Utils.manggiohang.get(i).getSoluong());
                long giaMoi = Long.parseLong(giaSanPham) * Utils.manggiohang.get(i).getSoluong();
                Utils.manggiohang.get(i).setGiasp(giaMoi);
                flag = true;
                break;
            }
        }

        // Nếu sản phẩm chưa tồn tại trong giỏ, thêm mới
        if (!flag) {
            GioHang gioHang = new GioHang();
            gioHang.setGiasp(gia);
            gioHang.setSoluong(soluong);
            gioHang.setIdsp(sanPhamMoi.getId());
            gioHang.setTensp(sanPhamMoi.getTensp());
            gioHang.setHinhsp(sanPhamMoi.getHinhanh());
            Utils.manggiohang.add(gioHang);
        }

        // Cập nhật badge số lượng sản phẩm trong giỏ
        badge.setText(String.valueOf(Utils.manggiohang.size()));
    }


    private void initview() {
        tensp = findViewById(R.id.txtTensp);
        giasp = findViewById(R.id.txtGiasp);
        mota = findViewById(R.id.txtmotachitiet);
        spinner = findViewById(R.id.spinner);
        imghinhanh = findViewById(R.id.imgchitiet);
        toolbar = findViewById(R.id.toolbar);
        badge = findViewById(R.id.menu_sl);
        btnThem = findViewById(R.id.btnThem);
        // Kiểm tra và cập nhật số lượng giỏ hàng
        if (Utils.manggiohang != null) {
            badge.setText(String.valueOf(Utils.manggiohang.size()));
        }
    }
}
