package com.example.megagram_market.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.megagram_market.R;
import com.example.megagram_market.model.SanPhamMoi;

import java.util.List;

public class SanPhamMoiAdapter  extends RecyclerView.Adapter<SanPhamMoiAdapter.MyViewHolder>{
    Context context;
    List<SanPhamMoi> array;

    public SanPhamMoiAdapter(Context context, List<SanPhamMoi> array) {
        this.context = context;
        this.array = array;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sp_moi,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        SanPhamMoi sanPhamMoi= array.get(position);
        holder.txtten.setText(sanPhamMoi.getTensp());
        // Gía
//        DecimalFormat decimalFormat= new DecimalFormat("###,###,###");
//        holder.txtgia.setText("Giá: "+decimalFormat.format(Double.parseDouble(sanPhamMoi.getGiasp()))+"Đ");
        holder.txtgia.setText(sanPhamMoi.getGiasp());
        Glide.with(context)
                .load(sanPhamMoi.getHinhanh()) // sanPhamMoi.getHinhanh() nên trả về một URL hoặc đường dẫn hình ảnh hợp lệ
                .into(holder.imghinhanh); // Đây là chỗ bạn sử dụng 'into' để đặt hình ảnh vào ImageView
//        holder.setItemClickListener(new ItemClickListener() {
//            @Override
//            public void onClick(View view, int pos, boolean isLongClick) {
//                if (!isLongClick){
//                    //Click
//                    Intent intent= new Intent(context, ChiTietActivity.class);
//                    intent.putExtra("chitiet",sanPhamMoi);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    context.startActivity(intent);
//                }
//            }
//        });


    }

    @Override
    public int getItemCount() {
        return array.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtgia,txtten;
        ImageView imghinhanh;
//        private ItemClickListener itemClickListener;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtgia=itemView.findViewById(R.id.itemsp_gia);
            txtten=itemView.findViewById(R.id.itemsp_ten);
            imghinhanh=itemView.findViewById(R.id.itemsp_image);
            itemView.setOnClickListener(this);
        }

//        public void setItemClickListener(ItemClickListener itemClickListener) {
//            this.itemClickListener = itemClickListener;
//        }

        @Override
        public void onClick(View view) {
//            itemClickListener.onClick(view, getAdapterPosition(),false);
        }
    }
}
