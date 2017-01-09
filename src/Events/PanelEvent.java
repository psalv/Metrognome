package Events;

import java.util.EventObject;

public class PanelEvent<E> extends EventObject{

    private int panelNum;
    private int curNum;
    private int delay;
    private int inc;

    public PanelEvent(Object source, int panelNum, int curNum){
        super(source);
        this.panelNum = panelNum;
        this.curNum = curNum;
    }

    public PanelEvent(Object source, int panelNum, int curNum, int delay, int inc){
        super(source);
        this.panelNum = panelNum;
        this.curNum = curNum;
        this.delay = delay;
        this.inc = inc;
    }

    public int getDelay() {
        return delay;
    }

    public int getInc() {
        return inc;
    }

    public int getPanelNum() {
        return panelNum;
    }

    public int getCurNum() {
        return curNum;
    }
}
