package test.com.jyl.bluetooth.holde;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import test.com.jyl.bluetooth.R;


/**
 * desc:
 * last modified time:2018/1/13 15:55
 *
 * @author yulin.jing
 * @since 2018/1/13
 */
public class HolderServiceMsg extends RecyclerView.ViewHolder {

    @ViewInject(R.id.tv_msg)
    TextView tvMsg;

    public HolderServiceMsg(View itemView) {
        super(itemView);
        x.view().inject(this,itemView);
    }

    public void bindData(HoldeBeanServiceMsg robotVoice) {
        tvMsg.setText(robotVoice.getMsg());
    }
}
