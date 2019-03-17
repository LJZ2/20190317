package month.bawei.com.recyclerview;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {


    private RecyclerView mrecyclerView;
    private MyAdapter myAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mrecyclerView = (RecyclerView) findViewById(R.id.recycler);
//        添加布局管理器
//        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
//        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        mrecyclerView.setLayoutManager(linearLayoutManager);
//        网格布局管理器
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,3);
        mrecyclerView.setLayoutManager(gridLayoutManager);
        myAdapter = new MyAdapter();
//        瀑布布局管理器
        StaggeredGridLayoutManager staggeredGridLayoutManager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        mrecyclerView.setLayoutManager(staggeredGridLayoutManager);
//        适配器
        mrecyclerView.setAdapter(myAdapter);
        doHttp();

    }
//    请求网络数据
    private void doHttp() {
        String url="http://code.aliyun.com/598254259/FristProject/raw/master/bw_test.txt";
        OkHttpClient okHttpClient=new OkHttpClient();
        Request request=new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Message message=Message.obtain();
                message.obj=response.body().string();
                handler.sendMessage(message);
            }
        });
    }
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String data= (String) msg.obj;
            Bean bean=new Gson().fromJson(data,Bean.class);
            List<Bean.ItemsBean> items = bean.getItems();
            myAdapter.setData(items);

        }
    };

    private class MyAdapter extends RecyclerView.Adapter<MyViewHolder>{
        private List<Bean.ItemsBean> data=new ArrayList<>();

        //        初始化ViewHolder
        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = View.inflate(MainActivity.this, R.layout.recycler_item, null);
            MyViewHolder myViewHolder=new MyViewHolder(view);
            return myViewHolder;
        }
//        绑定数据
        @Override
        public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
            Bean.ItemsBean bean=data.get(i);
            myViewHolder.mTitle.setText(bean.getTitle());
            myViewHolder.mDesc.setText(bean.getDesc());
            Picasso.with(MainActivity.this).load(bean.getImage()).into(myViewHolder.mImage);



        }
//        返回
        @Override
        public int getItemCount() {
            return data.size();
        }

        public void setData(List<Bean.ItemsBean> data) {
            this.data = data;
            notifyDataSetChanged();
        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView mImage;
        TextView mDesc;
        TextView mTitle;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mImage = (ImageView)itemView.findViewById(R.id.image);
            mTitle = (TextView)itemView.findViewById(R.id.tv_title);
            mDesc =(TextView) itemView.findViewById(R.id.tv_desc);

        }
    }
}
