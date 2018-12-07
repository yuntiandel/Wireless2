package myadapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.wireless.R;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

import bean.MyEventShow;
import bean.ShiJianXinXiGet;
import util.SharedPreferencesUtils;


public class MyShiJianAdapter extends ArrayAdapter<ShiJianXinXiGet> {
    private int resourceId;
    public MyShiJianAdapter(Context context, int textviewresourceId, List<ShiJianXinXiGet> list){
        super(context,textviewresourceId,list);
        resourceId = textviewresourceId;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ShiJianXinXiGet shi = getItem(position);
        View view;
        MyViewHolder viewHolder;
        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHolder = new MyViewHolder();
            viewHolder.shijian = (TextView)view.findViewById(R.id.myfashengtime);
            viewHolder.shuoming = (TextView)view.findViewById(R.id.myshijianshuoming);
            viewHolder.zhuangtai = (TextView)view.findViewById(R.id.mychulizhuangtai);
            view.setTag(viewHolder);
        }else {
            view = convertView;
            viewHolder = (MyViewHolder)view.getTag();
        }
            String timechange = changetime(shi.getShangBaoShiJian().toString());
        int a;
        if (timechange.length()>10){
            a = 10;
        }else {
            a = timechange.length();
        }
            viewHolder.shijian.setText(timechange.substring(0,a));
            String shu = shi.getShiJianShuoMing().trim()+"　　　　　　　　";
            viewHolder.shuoming.setText(shu.substring(0,7));
            String zhuang = shi.getChuZhiGuoCheng().trim()+"　　　　　　　　";
            viewHolder.zhuangtai.setText(zhuang.substring(0,7));
        return view;
    }
    //时间转换：从数据库中读取到的是（Date(00000)）类型的数据，在向view投影前转换成正确的数据
    private String changetime(String time){
        if (time.equals(""+"null")){
            return "";
        }else {
            try{
                Log.d("12345654321", "时间格式time:" + time);
                SimpleDateFormat fff = new SimpleDateFormat("yyyy-MM-dd");
                String ss = time.substring(6,time.length()-2);
                Log.d("12345654321", "时间格式ss:" + ss);
                long sa = Long.parseLong(ss);
                String x = fff.format(sa);
                Log.d("12345654321", "时间格式x:" + x);
                return x;
            }catch (Exception e){
                e.printStackTrace();
                Log.d("12345654321", "出错：" + e);
                return "未知时间";
            }
        }

    }
}
class MyViewHolder{
    TextView shijian;
    TextView shuoming;
    TextView zhuangtai;
}
