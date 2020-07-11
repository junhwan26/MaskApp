package com.junhwan.maskapp

import android.graphics.Color
import android.graphics.PointF
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Transformations.map
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons
import java.util.concurrent.Executor


class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    var array : ArrayList<Pharmacy>?=null
    private lateinit var locationSource: FusedLocationSource
    var latitude:Double?=null
    var longitude:Double?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)


        locationSource =
            FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)

        val intent = intent
        array =intent.getParcelableArrayListExtra<Pharmacy>("list")
        latitude = intent.getDoubleExtra("latitude",0.0)
        longitude = intent.getDoubleExtra("longitude",0.0)

        NaverMapSdk.getInstance(this).client = NaverMapSdk.NaverCloudPlatformClient("@strings/naver_client_id")

        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map_fragment) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map_fragment, it).commit()
            }

        mapFragment.getMapAsync(this)



    }

    override fun onMapReady(naverMap: NaverMap) {

        val cameraUpdate = CameraUpdate.scrollTo(LatLng(latitude!!, longitude!!)).animate(CameraAnimation.Fly)
        naverMap.moveCamera(cameraUpdate)

        val infoWindow = InfoWindow()
        infoWindow.adapter = object : InfoWindow.DefaultTextAdapter(this) {
            override fun getText(infoWindow: InfoWindow): CharSequence {
                // 정보 창이 열린 마커의 tag를 텍스트로 노출하도록 반환
                return infoWindow.marker?.tag as CharSequence? ?: ""
            }
        }

        val markers = mutableListOf<Marker>()
        array!!.forEach {
            markers+=Marker().apply {

                position=LatLng(it.latitude,it.longitude)
                icon = MarkerIcons.BLACK
                if(it.remain_stat=="plenty")
                    iconTintColor= Color.GREEN
                if(it.remain_stat=="some")
                    iconTintColor=Color.YELLOW
                if(it.remain_stat=="few")
                    iconTintColor=Color.RED
                if(it.remain_stat=="empty")
                    iconTintColor= Color.GRAY
                width=Marker.SIZE_AUTO
                height=Marker.SIZE_AUTO

                var tmp : String?=null
                if(it.type=="01")
                    tmp="약국"
                if(it.type=="02")
                    tmp="우체국"
                if(it.type=="03")
                    tmp="농협"

                if(it.remain_stat == "null")
                    tag=it.name+" ($tmp)"+"\n정보 없음"
                else
                    tag=it.name+" ($tmp)"+"\n입고 시간 : "+it.stock_at

                setOnClickListener {
                    infoWindow.open(this)
                    true
                }
            }
        }

        markers.forEach { marker ->
            marker.map = naverMap
        }


    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }
}
