package test.com.jyl.bluetooth.holde;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import test.com.jyl.bluetooth.R;


/**
 * desc:
 * last modified time:2018/1/13 15:37
 *
 * @author yulin.jing
 * @since 2018/1/13
 */
public class ChatAdapte extends RecyclerView.Adapter {
    private Context mContext;
    public int TYPE;
    public final int TYPE_VOICE_ROBOT = 0;
    public final int TYPE_VOICE_USER = 1;
    private LayoutInflater mInflater;
    private List datas;

    public ChatAdapte(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        datas = new ArrayList();
    }

    public void addVoiceRobot(HoldeBeanServiceMsg beanRobotVoice){
        datas.add(beanRobotVoice);
        notifyDataSetChanged();
    }

    public void addVoiceUser(HoldeBeanUserMsg beanUserVoice){
        datas.add(beanUserVoice);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_VOICE_ROBOT:
                return new HolderServiceMsg(mInflater.inflate(R.layout.view_hole_robot_voice,parent,false));
            case TYPE_VOICE_USER:
                return new HolderUserMsg(mInflater.inflate(R.layout.view_hole_user_voice,parent,false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof HolderServiceMsg) {
            HolderServiceMsg robotVoice = (HolderServiceMsg)holder;
            robotVoice.bindData((HoldeBeanServiceMsg)datas.get(position));
        } else if(holder instanceof HolderUserMsg) {
            HolderUserMsg userVoice = (HolderUserMsg)holder;
            userVoice.bindData((HoldeBeanUserMsg)datas.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(datas.get(position) instanceof HoldeBeanServiceMsg) {
            return TYPE_VOICE_ROBOT;
        } else if(datas.get(position) instanceof HoldeBeanUserMsg) {
            return TYPE_VOICE_USER;
        }
        return super.getItemViewType(position);
    }
}
