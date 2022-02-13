package com.hema.taqsawy.ui.favorite

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hema.taqsawy.R
import com.hema.taqsawy.adapter.FavoriteAdapter
import com.hema.taqsawy.data.db.favoritePlacesModel.FavoriteModel
import com.hema.taqsawy.databinding.FragmentFavoriteBinding
import com.hema.taqsawy.providers.SharedPreferencesProvider
import com.hema.taqsawy.ui.favorite.favoriteDetails.FavoriteDetailsActivity
import com.mapbox.api.geocoding.v5.models.CarmenFeature
import com.mapbox.mapboxsdk.plugins.places.autocomplete.ui.PlaceAutocompleteFragment
import com.mapbox.mapboxsdk.plugins.places.autocomplete.ui.PlaceSelectionListener
import java.math.BigDecimal
import java.math.RoundingMode


class FavoriteFragment : Fragment() {

    private lateinit var binding: FragmentFavoriteBinding
    private var placesList = mutableListOf<FavoriteModel>()
    private lateinit var favoriteAdapter: FavoriteAdapter
    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var intent: Intent
    private var latDecimal: BigDecimal? = null
    private var lonDecimal: BigDecimal? = null
    private lateinit var address: String
    private lateinit var sharedPref: SharedPreferencesProvider

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPref = SharedPreferencesProvider(requireContext())
        favoriteViewModel = ViewModelProvider(this)[FavoriteViewModel::class.java]
        binding.fabAddPlaces.setOnClickListener {
            showAutoCompleteBar()
            binding.tvAddLocations.visibility = View.GONE
        }

        //update RecyclerView
        favoriteViewModel.fetchFavorite().observe(viewLifecycleOwner) {
            placesList = it as MutableList<FavoriteModel>
            favoriteAdapter = FavoriteAdapter(placesList, favoriteViewModel)
            binding.recyclerView.adapter = favoriteAdapter
            binding.recyclerView.layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            binding.recyclerView.setHasFixedSize(true)
            favoriteAdapter?.notifyDataSetChanged()
            if (it.isEmpty()) {
                binding.tvAddLocations.visibility = View.VISIBLE
            }
        }

        // fetch weather data when click to search item
        favoriteViewModel.getWeatherFromFavorite(latDecimal.toString(), lonDecimal.toString())

        // intent to details activity when click to item
        intent = Intent(activity, FavoriteDetailsActivity::class.java)
        favoriteViewModel.getNavigation().observe(viewLifecycleOwner) {
            //it = placesList item clicked data --> [lat,lng] in favoriteAdapter
            if (it != null) {

                sharedPref.setLatLongFav(it[0], it[1])
                intent.putExtra("lat", it[0])
                intent.putExtra("lng", it[1])
                activity?.startActivity(intent)
            }
        }
    }

    private fun showAutoCompleteBar() {

        binding.placeAutoCompleteFrag.visibility = View.VISIBLE
        val autocompleteFragment =
            PlaceAutocompleteFragment.newInstance("pk.eyJ1IjoibW9oYW1lZHlvdXNzZWYxOTk5MiIsImEiOiJja2xsZGFxOTQzbDNwMnZzODVya3kyZWk3In0.u6G62r1JYlNDwCZxEdDrrA")
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.add(
            R.id.place_autoComplete_Frag,
            autocompleteFragment,
            "AUTOCOMPLETE_FRAGMENT_TAG"
        )
        transaction?.commit()

        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(carmenFeature: CarmenFeature) {


                val latitude = carmenFeature.center()?.latitude()
                val longitude = carmenFeature.center()?.longitude()

                latDecimal = latitude?.let { BigDecimal(it).setScale(4, RoundingMode.HALF_DOWN) }
                lonDecimal = longitude?.let { BigDecimal(it).setScale(4, RoundingMode.HALF_DOWN) }
                address = carmenFeature.text().toString()

                val favModel =
                    FavoriteModel(
                        address,
                        latDecimal.toString(),
                        lonDecimal.toString()
                    )
                //insert to fav places table
                favoriteViewModel.insertFavorite(favModel)
                // insert to weather data table
                favoriteViewModel.insertFavoriteToDataBase(
                    latDecimal.toString(),
                    lonDecimal.toString()
                )
                activity?.supportFragmentManager?.beginTransaction()?.remove(autocompleteFragment)
                    ?.commit()
                binding.placeAutoCompleteFrag.visibility = View.GONE
            }

            override fun onCancel() {
                activity?.supportFragmentManager?.beginTransaction()?.remove(autocompleteFragment)
                    ?.commit()
                binding.placeAutoCompleteFrag.visibility = View.GONE
            }
        })


    }

}


