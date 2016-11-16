package com.nju.urbangreen.zhenjiangurbangreen.map;

/**
 * Created by HCQIN on 2016/9/26.
 */
public interface ILayerSwitchListener {

    boolean[] getLayerState();
    void changeLayerState(boolean[] layerState);
}
