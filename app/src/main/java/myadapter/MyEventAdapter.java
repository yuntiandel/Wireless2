package myadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wireless.R;

import java.util.ArrayList;
import java.util.List;

public class MyEventAdapter extends BaseAdapter{
    private Context mcontext;
    private ArrayList<String> mSortList = new ArrayList<String>();
    private ArrayList<String> mLocationList;
    private ArrayList<String> mTimeList;
    private static final String TAG = "TAG";

    public MyEventAdapter(Context mcontext, ArrayList<String> mSortList, ArrayList<String> mLocationList, ArrayList<String> mTimeList) {
        this.mcontext = mcontext;
        this.mSortList = mSortList;
        this.mLocationList = mLocationList;
        this.mTimeList = mTimeList;
    }

    @Override
    public int getCount() {
        //返回数据有多少
        return mSortList.size();
    }
    @Override
    public Object getItem(int i) {
        //返回item的数据
        return mSortList.get(i);
    }

    @Override
    public long getItemId(int i) {
        //
        return i;
    }

    //负责每个item的数据设置
    //第一个参数 当前绘制的item的序号
    //第二个参数：
    //的第三个参数：item所在的容器控件
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {


        LayoutInflater layoutInflater =LayoutInflater.from(mcontext);
        //
        View linearLayout = view;
        TextView sort;
        TextView location;
        TextView time;
//        Button button;
        ViewHoler holer;
        if (view == null)
        {

            //布局建造器

            //第一个参数：单个item布局的id
            //第二个参数：null
            linearLayout =  layoutInflater.inflate(R.layout.item_myevent,null);
            // 单独找出Item内部的控件
            sort = (TextView) linearLayout.findViewById(R.id.sort);
            location = (TextView) linearLayout.findViewById(R.id.location);
            time = (TextView) linearLayout.findViewById(R.id.time);
            //把控件对象装载到holder一个对象中
            holer = new ViewHoler(sort,location,time);
            linearLayout.setTag(holer);
        }else {
            //布局重用时
            //先从布局对象中取出Holder
            holer = (ViewHoler) linearLayout.getTag();
            //取出之前存进去的对象
            sort = holer.wSort;
            location = holer.wLocation;
            time = holer.wTime;
        }

        sort.setText(mSortList.get(i));
        location.setText(mLocationList.get(i));
        time.setText(mTimeList.get(i));
        return linearLayout;
    }

    class ViewHoler{
        //存储多个控件，并提供get多个控件的方法
        private TextView wSort;
        private TextView wLocation;
        private TextView wTime;
        public ViewHoler(TextView textView1,TextView textView2,TextView textView3) {
            wSort = textView1;
            wLocation = textView2;
            wTime = textView3;
        }
    }

}
