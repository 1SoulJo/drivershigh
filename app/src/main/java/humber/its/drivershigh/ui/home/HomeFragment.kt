package humber.its.drivershigh.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import humber.its.drivershigh.R
import humber.its.drivershigh.data.localdata.Route
import humber.its.drivershigh.databinding.FragmentHomeBinding
import humber.its.drivershigh.ui.BaseFragment
import kotlin.math.max
import kotlin.math.min


class HomeFragment : BaseFragment(), OnMapReadyCallback, LocationListener {
    companion object {
        const val locationResultCode = 10231
    }

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    private lateinit var googleMap: GoogleMap
    private lateinit var locationManager: LocationManager
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var selectedRoute: Route? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        initMap()
        initList()
        initFab()

        return binding.root
    }

    override fun onMapReady(map: GoogleMap?) {
        googleMap = map!!
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED) {
            googleMap.isMyLocationEnabled = true
            updateLocation()
        } else {
            requestPermissions(
                listOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION).toTypedArray(),
                locationResultCode)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onLocationChanged(loc: Location) {
        val current: CameraPosition = CameraPosition.Builder()
                .target(LatLng(loc.latitude, loc.longitude))
                .zoom(15.5f)
                .bearing(300f)
                .tilt(50f)
                .build()
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(current))

        locationManager.removeUpdates(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == locationResultCode && resultCode == PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, this)
            }
        }
    }

    override fun onBackPressed(): Boolean {
        if (_binding?.featuredRoutes?.isVisible == false) {
            googleMap.clear()
            _binding?.featuredRoutes?.visibility = View.VISIBLE
            return true
        }

        return false
    }

    @SuppressLint("MissingPermission")
    private fun updateLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity as Activity)
        fusedLocationClient.lastLocation
            .addOnSuccessListener { loc: Location? ->
                if (loc != null) {
                    val lastLocation: CameraPosition = CameraPosition.Builder()
                        .target(LatLng(loc.latitude, loc.longitude))
                        .zoom(15.5f)
                        .bearing(300f)
                        .tilt(50f)
                        .build()
                    googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(lastLocation))
                }
            }
    }

    private fun initList() {
        _binding?.routeList?.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        _binding?.routeList?.adapter = RoutesAdapter {
            homeViewModel.fetchRouteInfo(it)
            selectedRoute = it
        }

        // list observer
        val listOb = Observer<List<Route>> {
            if (it != null && it.isNotEmpty()) {
                if (_binding?.routeList?.adapter != null) {
                    (_binding?.routeList?.adapter as RoutesAdapter).setData(it)
                }
            }
        }
        homeViewModel.routes.observe(requireActivity(), listOb)

        // route points observer
        val pointsOb = Observer<MutableList<LatLng>> {
            if (it == null) {
                // clear map
                googleMap.clear()
            } else {
                if (it.size == 0) {
                    return@Observer
                }

                // update views
                _binding?.featuredRoutes?.visibility = View.GONE
                _binding?.fab?.visibility = View.VISIBLE

                // draw
                val start = LatLng(it[0].latitude, it[0].longitude)
                googleMap.addMarker(
                    MarkerOptions().position(start).title("Start")
                        .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_AZURE))).showInfoWindow()

                val end = LatLng(it[it.size - 1].latitude, it[it.size - 1].longitude)
                googleMap.addMarker(MarkerOptions().position(end).title("End"))
                googleMap.addPolyline(
                    PolylineOptions().clickable(true).addAll(it).color(R.color.purple_200)
                )

                var minLat: Double = Double.POSITIVE_INFINITY
                var minLng: Double = Double.POSITIVE_INFINITY
                var maxLat: Double = Double.NEGATIVE_INFINITY
                var maxLng: Double = Double.NEGATIVE_INFINITY
                for (i in it) {
                    minLat = min(minLat, i.latitude)
                    minLng = min(minLng, i.longitude)
                    maxLat = max(maxLat, i.latitude)
                    maxLng = max(maxLng, i.longitude)
                }
                val swBound = LatLng(minLat, minLng)
                val neBound = LatLng(maxLat, maxLng)
                val boundary = LatLngBounds(swBound, neBound)
                googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(boundary, 150))
            }
        }
        homeViewModel.routePoints.observe(requireActivity(), pointsOb)
    }

    private fun initMap() {
        val mf = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mf.getMapAsync(this)
    }

    private fun initFab() {
        _binding?.fab?.setOnClickListener {
            if (selectedRoute != null) {
                homeViewModel.startDriving(selectedRoute!!)
            }
        }
    }
}