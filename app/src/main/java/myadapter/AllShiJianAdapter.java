package myadapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.wireless.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import bean.AllEventShow;
import bean.ShiJianXinXiGet;


public class AllShiJianAdapter extends ArrayAdapter<ShiJianXinXiGet> {
    private int resourceId;
    public AllShiJianAdapter(Context context, int textviewresourceId, List<ShiJianXinXiGet> list){
        super(context,textviewresourceId,list);
        resourceId = textviewresourceId;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ShiJianXinXiGet shi = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.shijian = (TextView)view.findViewById(R.id.allfashengtime);
            viewHolder.didian = (TextView)view.findViewById(R.id.allshijiandidian);
            viewHolder.shuoming = (TextView)view.findViewById(R.id.allshijianshuoming);
            view.setTag(viewHolder);
        }else {
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();//重新获取
        }
        String shijianchange = changetime(""+shi.getShangBaoShiJian());
        int as;
        if (shijianchange.length()>10){
            as = 10;
        }else {
            as = shijianchange.length();
        }
        viewHolder.shijian.setText(shijianchange.substring(0,as));
        viewHolder.didian.setText((shi.getShiJianDiDian().trim()+"　　　　　　　　").substring(0,7));
        viewHolder.shuoming.setText((shi.getShiJianShuoMing().trim()+"　　　　　　　　").substring(0,7));
        return view;
    }
    //时间转换：从数据库中读取到的是（Date(00000)）类型的数据，在向view投影前转换成正确的数据
    private String changetime(String time){
        if (time.equals(""+"null")){
            return "";
        }else {
            try{
//           Log.d("12345654321", "时间格式time:" + time);
                SimpleDateFormat fff = new SimpleDateFormat("yyyy-MM-dd");
                String ss = time.substring(6,time.length()-2);
                // Log.d("12345654321", "时间格式ss:" + ss);
                long sa = Long.parseLong(ss);
                String x = fff.format(sa);
//           Log.d("12345654321", "时间格式x:" + x);
                return x;
            }catch (Exception e){
                e.printStackTrace();
                Log.d("12345654321", "出错：" + e);
                return "未知时间";
            }
        }

    }
}
class ViewHolder{
    TextView shijian;
    TextView didian;
    TextView shuoming;
}
