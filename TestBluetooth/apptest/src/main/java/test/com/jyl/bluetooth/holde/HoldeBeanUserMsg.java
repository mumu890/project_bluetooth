package test.com.jyl.bluetooth.holde;

/**
 * desc:
 * last modified time:2018/1/13 15:57
 *
 * @author yulin.jing
 * @since 2018/1/13
 */
public class HoldeBeanUserMsg {
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("HoldeBeanUserVoice{");
        sb.append("msg='").append(msg).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
