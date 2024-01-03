package com.yxl.userphotosapp.main.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.yxl.userphotosapp.core.Utils
import com.yxl.userphotosapp.databinding.FragmentMapBinding
import com.yxl.userphotosapp.main.photos.PhotosViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private lateinit var binding: FragmentMapBinding
    private val viewModel by viewModels<PhotosViewModel>(ownerProducer = { requireActivity() })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapBinding.inflate(inflater, container, false)
        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync(this)
        return binding.root
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        val llBounds = LatLngBounds.builder()
        lifecycleScope.launch {
            viewModel.photosFromDb.collect {
                for (p in it) {
                    googleMap.addMarker(
                        MarkerOptions()
                            .position(LatLng(p.lat, p.lng)).title(Utils.convertToDateTime(p.date, true))
                    )
                    llBounds.include(LatLng(p.lat, p.lng))
                }
            }
        }
        val llBoundsBuilded = llBounds.build()
        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(llBoundsBuilded, 10))
    }

}