package com.trentwdavies.daeloader;

import com.requiem.interfaces.Asset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Trent on 11/4/2014.
 */
public class Model implements Asset {
    public List<Geometry> geometryList;
    public Map<String, Effect> materialEffectMap;

    public Model() {
        geometryList = new ArrayList<Geometry>();
        materialEffectMap = new HashMap<String, Effect>();
    }

    public void addGeometries(Geometry... geometries) {
        for (Geometry g : geometries) {
            geometryList.add(g);
        }
    }

    public void setMaterialEffectMap(Map<String, Effect> materialEffectMap) {
        this.materialEffectMap = materialEffectMap;
    }
}
