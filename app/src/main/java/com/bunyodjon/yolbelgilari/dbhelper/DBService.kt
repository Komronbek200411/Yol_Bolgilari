package com.bunyodjon.yolbelgilari.dbhelper

import android.content.Context

interface DBService {
    fun addRoadSign(roadSign: RoadSign): Boolean
    fun showRoadSign(): ArrayList<RoadSign>
    fun updateRoadSign(roadSign: RoadSign): Boolean
    fun deleteRoadSign(roadSign: RoadSign): Boolean
}