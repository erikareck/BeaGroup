package com.example.sharon.beagroup;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class findMerchandise_furniture extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_find_merchandise);

        setLayout();
        findView();
        setView();
    }

    private  void setLayout(){
        RelativeLayout relativeLayout = new RelativeLayout(this);
        listView = new ListView(this);
        RelativeLayout.LayoutParams layoutParams = new
        RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        listView.setLayoutParams(layoutParams);
        relativeLayout.addView(listView);
        setContentView(relativeLayout);
    }

    private void findView(){

    }

    private void setView(){

        showMerchandise();
    }

    private void showMerchandise(){
        final Context context = this;
        merchandise furniture_1= new merchandise(R.drawable.tarnedo_desk, "TÄRENDÖ 桌子", "$1290", "美耐皿桌面，可防潮、抗污，容易保持乾淨", "https://www.ikea.com/tw/zh/catalog/products/90255898/");
        merchandise furniture_2= new merchandise(R.drawable.crainde_mark, "FÄRGRIK 馬克杯", "$15", "底部排水孔可排出水分，防止杯子倒放在洗碗機時導致積水", "https://www.ikea.com/tw/zh/catalog/products/80234806/");
        merchandise furniture_3= new merchandise(R.drawable.djeka_mark, "HÄNGIVEN 馬克杯", "$149", "半瓷，未添加鎘或鉛且適用於洗碗機清潔", "https://www.ikea.com/tw/zh/catalog/products/20353864/");
        merchandise furniture_4= new merchandise(R.drawable.lieab_lamp, "LAMPAN 桌燈", "$279", "獨特設計，容易放置，使家中的黑暗角落更溫暖、舒適", "https://www.ikea.com/tw/zh/catalog/products/90399062/");
        merchandise furniture_5= new merchandise(R.drawable.plueeg_box, "SORTERA 附蓋垃圾分類箱", "$349", "附折疊式蓋子，堆疊時容易拿取收納物", "https://www.ikea.com/tw/zh/catalog/products/90255898/");
        merchandise furniture_6= new merchandise(R.drawable.sknodd_trash_bin, "STRAPATS 腳踏式垃圾桶", "$389", "後面有把手，方便移動 附可拆式內桶，容易清空垃圾或清潔 可放在家中任何地方，也適用於廚房和浴室等潮濕地方", "https://www.ikea.com/tw/zh/catalog/products/60245410/");

        final ArrayList<merchandise> merchandises = new ArrayList<>();
        merchandises.add(furniture_1);
        merchandises.add(furniture_2);
        merchandises.add(furniture_3);
        merchandises.add(furniture_4);
        merchandises.add(furniture_5);
        merchandises.add(furniture_6);


        BaseAdapter baseAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return merchandises.size();
            }

            @Override
            public Object getItem(int position) {
                return merchandises.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {


                if (convertView == null){
                    convertView = LayoutInflater.from(context).inflate(R.layout.list_item_merchandise_furniture, null);
                }

                merchandise m = (merchandise)getItem(position);

                ImageView photoImage = (ImageView) convertView.findViewById(R.id.photo_merchandise);
                int photoResid = m.photoID;
                photoImage.setImageResource(photoResid);

                TextView nameText = (TextView) convertView.findViewById(R.id.name_merchandise);
                String itemName = m.name;
                nameText.setText(itemName);

                TextView discountPriceText = (TextView) convertView.findViewById(R.id.discount_price);
                String itemDiscountPrice = m.discount_price;
                discountPriceText.setText(itemDiscountPrice);

                TextView originalPriceText = (TextView) convertView.findViewById(R.id.original_price);
                //originalPriceText.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                String itemDescription = m.description;
                originalPriceText.setText(itemDescription);

                String itemUrl = m.url;


                Button webButton = (Button)convertView.findViewById(R.id.search_doc);
                webButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openWeb(itemUrl);
                    }
                });


                return convertView;
            }
        };

        listView.setAdapter(baseAdapter);
    }

    public void openWeb(String url){
        Intent intent = new Intent(this, webBrowsing.class);
        intent.putExtra("url", url);
        startActivity(intent);
    }
}
