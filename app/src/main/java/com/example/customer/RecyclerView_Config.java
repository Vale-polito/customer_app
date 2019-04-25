package com.example.customer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerView_Config {

    private Context mContext;
    private FoodsAdapter mFoodsAdapter;

    public void setConfig (RecyclerView recyclerView, Context context,List<DailyOffer> dailyOffers, List <String> keys){
        mContext=context;
        mFoodsAdapter = new FoodsAdapter(dailyOffers,keys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mFoodsAdapter);
    }

    class DailyOfferItemView extends RecyclerView.ViewHolder {
        private TextView mFoodname;
        private TextView mPrice;
        private TextView mDiscount;
        private TextView mShortdescription;
        private ImageView mImgfood;
        private String key;


        public DailyOfferItemView(ViewGroup parent){
            super(LayoutInflater.from(mContext).
                    inflate(R.layout.food_list_item,parent,false));
            mFoodname=  itemView.findViewById(R.id.txt_FoodName);
            mPrice=  itemView.findViewById(R.id.txt_Price);
            mDiscount=  itemView.findViewById(R.id.txt_Discount);
            mShortdescription=  itemView.findViewById(R.id.txt_ShortDescription);
            mImgfood= itemView.findViewById(R.id.img_Food);

        }
        public void bind(DailyOffer dailyOffer, String key) {

            mFoodname.setText(dailyOffer.getName());
            mPrice.setText(dailyOffer.getPrice()+" € •");
            mDiscount.setText(dailyOffer.getDiscount()+"% (Off) • ");
            mShortdescription.setText(dailyOffer.getShortdescription());

            Picasso.get()
                    .load(dailyOffer.getImageUrl())
                    .placeholder(R.drawable.default_food)
                    .fit()
                    .centerCrop()
                    .into(mImgfood);
            this.key = key;
        }
    }
    class FoodsAdapter extends RecyclerView.Adapter<DailyOfferItemView>{
        private List<DailyOffer> mDailyOfferList;
        private List <String> mKeys;

        public FoodsAdapter(List <DailyOffer> mDailyOfferList , List <String> mKeys){
            this.mDailyOfferList = mDailyOfferList;
            this.mKeys = mKeys;
        }


        @Override
        public DailyOfferItemView onCreateViewHolder(ViewGroup parent, int viewType) {
            return new DailyOfferItemView(parent);
        }

        @Override
        public void onBindViewHolder(DailyOfferItemView holder, int position) {
            holder.bind(mDailyOfferList.get(position),mKeys.get(position));
        }

        @Override
        public int getItemCount() {
            return mDailyOfferList.size();
        }
    }

}

