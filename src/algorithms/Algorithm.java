package algorithms;

import framework.MapPanel;

public abstract class Algorithm implements Runnable{

    protected MapPanel panel;

    private boolean isInterrupted;

    public Algorithm(MapPanel panel){
        this.panel = panel;

        isInterrupted = false;
    }

    public void interrupt(){
        isInterrupted = true;
    }

    public boolean isInterrupted(){
        return isInterrupted;
    }
}
