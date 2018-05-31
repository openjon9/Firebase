package com.firebase.phone.fiebasetelecommunicationssolutions;
//作品 firebase 即時更新
//作者:楊鎮聯,康庭毓
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    TextView textView;
    Spinner spinner;
    String str;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("time");
    ArrayAdapter<String> adapter;
    List<String> list=new ArrayList();  //放 url  用的集合
    List<String> list2=new ArrayList(); //放spinner內容用的集合
    ArrayAdapter adapter2;
    AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-7117776194690995~5902929264");

        //廣告
        adView=(AdView)findViewById(R.id.adView);
       // AdRequest adRequest=new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        AdRequest adRequest=new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        spinner=(Spinner)findViewById(R.id.spinner);
        textView=(TextView)findViewById(R.id.textView);
        listView = (ListView) findViewById(R.id.listview);

                //將雲端上的欄位顯示在Listview 上
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);
              //Listview 點一下發生的事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                           //設置適配器得到的物件位置
            String str2 =MainActivity.this.adapter.getItem(position);
                          //判斷得到的位置沒有網址的情況除錯 並設成空白
                      try{
                          str =list.get(position);
                      }catch (Exception e){
                          str="";
                          Toast.makeText(MainActivity.this,"目前沒有連結",Toast.LENGTH_SHORT).show();
                      }
                          //將得到的Listview  跟 集合得到的網址 傳給意圖  並帶過去第2頁
                Intent intent = new Intent(MainActivity.this,WebviewMain2Activity.class);
                intent.putExtra("name",str2);
                intent.putExtra("url",str);
                startActivity(intent);
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                                                                         //取得雲端的資料夾名稱 company
                DatabaseReference reference_contacts2 = FirebaseDatabase.getInstance().getReference("company");
                reference_contacts2.addValueEventListener(new ValueEventListener() {
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // 取得spinner點選的位置 =   子資料夾 company+(位置+1)
                        DataSnapshot ds=dataSnapshot.child("company"+(position+1));
                        //清除適配器  跟 集合  得到的內容,  若無這個,   當發生改變的時候    會一直重疊下去
                        adapter.clear();
                        list.clear();
                        //用點選的位置來取得裡面的子資料夾的 name 跟  url
                        for (DataSnapshot ds2 : ds.getChildren() ){
                            //若雲端只設  name  沒有url 的話會出錯  所以這邊捕捉
                            try {
                                adapter.add(ds2.child("name").getValue().toString());
                                list.add(ds2.child("url").getValue().toString());
                            }catch (Exception e){
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

 // 雲端設spinner的內容
        DatabaseReference reference_contacts = FirebaseDatabase.getInstance().getReference("sp");
        reference_contacts.addValueEventListener(new ValueEventListener() {

            public void onDataChange(DataSnapshot dataSnapshot) {
                //每一次改變都會清除下拉選單得到的集合內容  並抓取新的內容
                    list2.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren() ){
                    list2.add(ds.getValue().toString());
                }
                //將抓到的內容放進適配器裡  要放在這個位置才會即時改變
                adapter2 = new ArrayAdapter(MainActivity.this,android.R.layout.simple_spinner_item,list2);
                spinner.setAdapter(adapter2);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        //時間
        fun1();
    }
    public void fun1(){
        //從雲端抓東西下來的方法  這裡抓的是  設置的時間
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                textView.setText(value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                // Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }
}
