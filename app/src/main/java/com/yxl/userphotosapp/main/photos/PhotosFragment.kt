package com.yxl.userphotosapp.main.photos

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.room.Room
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.yxl.userphotosapp.core.Utils
import com.yxl.userphotosapp.databinding.FragmentPhotosBinding
import com.yxl.userphotosapp.main.PhotoActivity
import com.yxl.userphotosapp.main.adapters.PhotosAdapter
import com.yxl.userphotosapp.main.data.PhotosRepositoryImpl
import com.yxl.userphotosapp.core.db.PhotoDatabase
import com.yxl.userphotosapp.main.model.ImageDtoIn
import com.yxl.userphotosapp.main.model.PhotoResponse
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Suppress("UNCHECKED_CAST")
class PhotosFragment : Fragment() {

    private lateinit var binding: FragmentPhotosBinding
    private var photoBitmap: Bitmap? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var lastKnownLocation: Location? = null
    private val viewModel by viewModels<PhotosViewModel>(factoryProducer = {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return PhotosViewModel(PhotosRepositoryImpl(db.photoDao())) as T
            }
        }
    },
        ownerProducer = { requireActivity() })
    private val db by lazy {
        Room.databaseBuilder(requireContext(), PhotoDatabase::class.java, "photos.db").build()
    }
    private lateinit var photosAdapter: PhotosAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPhotosBinding.inflate(inflater, container, false)
        return binding.root
    }

    @Suppress("DEPRECATION")
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            photoBitmap = imageBitmap
            if (lastKnownLocation != null) {
                val base64Image = Utils.encodeToBase64(photoBitmap!!)
                val lat = lastKnownLocation?.latitude!!
                val lng = lastKnownLocation?.longitude!!
                viewModel.postPhoto(
                    ImageDtoIn(
                        base64Image, Utils.currentTimestamp, lat, lng
                    )
                )

            }
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.updateToken((activity as? PhotoActivity)?.token.toString())
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        setUpRecycler()
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.photoListPager.collectLatest {
                    photosAdapter.submitData(it)
                }
            }
        }

        binding.fabTakePicture.setOnClickListener {
            getLocation()
            openCamera()
        }


    }

    private fun setUpRecycler() {
        photosAdapter = PhotosAdapter()
        photosAdapter.setOnClickListener(object : PhotosAdapter.OnClickListener {
            override fun onClick(position: Int, photo: PhotoResponse) {
                onPhotoClick(photo)
            }

            override fun onLongClick(position: Int, photo: PhotoResponse) {
                viewModel.setCurrentPhoto(photo)
                showDeleteDialog()
            }
        })
        binding.rvPhotos.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = photosAdapter
        }
    }

    private fun showDeleteDialog() {
        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle("Delete photo")
            .setMessage("Are u sure?")
            .setPositiveButton("Yes") { dialog, _ ->
                viewModel.deletePhoto()
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        alertDialog.show()
    }

    private fun onPhotoClick(photo: PhotoResponse) {
        viewModel.setCurrentPhoto(photo)
        viewModel.commentListPager
        (activity as? PhotoActivity)?.openFragment(PhotoItemFragment())
    }


    @Suppress("DEPRECATION")
    private fun openCamera() {

        if (checkCameraPermission()) {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } else {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
        }

    }

    @Suppress("DEPRECATION")
    private fun getLocation() {
        if (checkLocationPermission()) {
            fusedLocationClient.lastLocation.addOnSuccessListener {
                lastKnownLocation = it
            }
        } else {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun checkLocationPermission() = ContextCompat.checkSelfPermission(
        requireContext(),
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED


    private fun checkCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA,
        ) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 1
        private const val REQUEST_CAMERA_PERMISSION = 2
        private const val LOCATION_PERMISSION_REQUEST_CODE = 3
    }
}