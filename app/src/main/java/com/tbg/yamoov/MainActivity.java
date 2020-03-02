package com.tbg.yamoov;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.Guideline;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.maps.UiSettings;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;
import com.nightonke.boommenu.BoomButtons.BoomButton;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.OnBoomListener;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;
import com.tbg.yamoov.adapter.SearchAdapter;
import com.tbg.yamoov.api.APIClient;
import com.tbg.yamoov.api.ApiInterface;
import com.tbg.yamoov.model.Nominatim;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;


import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.util.Log;

import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements
        OnMapReadyCallback, PermissionsListener, MapboxMap.OnMapClickListener, LocationListener {
    private final Handler handler = new Handler();
    @BindView(R.id.floatingActionButton1)
    FloatingActionButton fab1;
    @BindView(R.id.floatingActionButton)
    FloatingActionButton fab;
    @BindView(R.id.floatingActionButton2)
    FloatingActionButton fab2;
    @BindView(R.id.card)
    CardView card;
    @BindView(R.id.bmb)
    BoomMenuButton bmb;
    @BindView(R.id.guideline2)
    Guideline guideline2;
    int ss = 0;
    private MapView mapView;
    private MapboxMap mapboxMap;
    private PermissionsManager permissionsManager;
    private List<Nominatim> nomin;
    private SearchAdapter customSuggestionsAdapter;
    private SearchView search;
    private RecyclerView recyclerView;
    private String mQueryString;
    private SymbolManager symbolManager;
    private LocationComponent locationComponent;

    private DirectionsRoute currentRoute;
    private static final String TAG = "DirectionsActivity";
    private NavigationMapRoute navigationMapRoute;

    public LocationManager locationManager;
    public Criteria criteria;
    String bestProvider;
    LocationManager loc = null;
    private String fournisseur;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initialiserLocalisation();


        guideline2.setGuidelineBegin(getStatusBarHeight() + 10);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);


        nomin = new ArrayList<>();

        search = findViewById(R.id.search);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        search.setQueryHint("Rechercher");
        search.setActivated(false);
        search.onActionViewExpanded();
        search.setIconified(false);
        search.clearFocus();

        fab2.setVisibility(View.GONE);

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchNominatim(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() == 0 || newText.matches("")) {
                    recyclerView.setVisibility(View.GONE);
                } else {
                    textChanged(newText);
                }
                return true;
            }
        });

        fab.setOnClickListener(v -> {
            if (ss == 0) {
                newStyle(Style.SATELLITE);
                ss = 1;
            } else {
                newStyle(Style.MAPBOX_STREETS);
                ss = 0;
            }

        });

        fab1.setOnClickListener(v -> {

             locationComponent = mapboxMap.getLocationComponent();

            locationComponent.setCameraMode(CameraMode.TRACKING);
            locationComponent.setRenderMode(RenderMode.COMPASS);
            locationComponent.zoomWhileTracking(14);

        });



        assert bmb != null;
        bmb.setButtonEnum(ButtonEnum.Ham);
        bmb.setPiecePlaceEnum(PiecePlaceEnum.HAM_5);
        bmb.setButtonPlaceEnum(ButtonPlaceEnum.HAM_5);
        bmb.setOnBoomListener(new OnBoomListener() {
            @Override
            public void onClicked(int index, BoomButton boomButton) {
                if(index == 0) {
                    Intent intent = new Intent(MainActivity.this, AnecdoteActivity.class);
                    startActivity(intent);
                }
                Toast.makeText(MainActivity.this, "Clicked " + index, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onBackgroundClick() {

            }

            @Override
            public void onBoomWillHide() {

            }

            @Override
            public void onBoomDidHide() {

            }

            @Override
            public void onBoomWillShow() {

            }

            @Override
            public void onBoomDidShow() {

            }
        });
        bmb.addBuilder(BuilderManager.getHamButtonBuilder("Anecdotes", "Retrouvez des anecdotes quotidiennes"));
        bmb.addBuilder(BuilderManager.getHamButtonBuilder("Actualités", "Retrouvez votre actualité quotidienne"));
        bmb.addBuilder(BuilderManager.getHamButtonBuilder("Réservations", "Liste de vos réservations"));
        bmb.addBuilder(BuilderManager.getHamButtonBuilder("Profil", "Votre Profil"));
        bmb.addBuilder(BuilderManager.getHamButtonBuilder("Sauvegardes", "Vos lieux enregistrés"));


    }

    public void newStyle(String style) {
        mapboxMap.setStyle(style, style1 -> {

        });
        UiSettings uiSettings = mapboxMap.getUiSettings();
        uiSettings.setLogoEnabled(false);
        uiSettings.setAttributionEnabled(false);
        uiSettings.setCompassGravity(Gravity.CENTER_VERTICAL);


    }

    @SuppressWarnings({"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
// Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {

// Get an instance of the component
            LocationComponent locationComponent = mapboxMap.getLocationComponent();


// Activate with options
            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions.builder(this, loadedMapStyle).build());

// Enable to make component visible
            locationComponent.setLocationComponentEnabled(true);

// Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);

// Set the component's render mode
            locationComponent.setRenderMode(RenderMode.COMPASS);
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, "", Toast.LENGTH_LONG).show();
    }


    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            mapboxMap.getStyle(this::enableLocationComponent);
        } else {
            Toast.makeText(this, "", Toast.LENGTH_LONG).show();
            finish();
        }
    }


    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;

        mapboxMap.setStyle(Style.MAPBOX_STREETS, style -> {
            enableLocationComponent(style);

            addDestinationIconSymbolLayer(style);

            mapboxMap.addOnMapClickListener(MainActivity.this);



            fab2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean simulateRoute = true;
                    NavigationLauncherOptions options = NavigationLauncherOptions.builder()
                            .directionsRoute(currentRoute)
                            .shouldSimulateRoute(simulateRoute)
                            .build();
// Call this method with Context from within an Activity
                    NavigationLauncher.startNavigation(MainActivity.this, options);
                }
            });

            symbolManager = new SymbolManager(mapView, mapboxMap, style);
            symbolManager.setIconAllowOverlap(true);  //your choice t/f
            symbolManager.setTextAllowOverlap(false);

            symbolManager.addClickListener(symbol -> Toast.makeText(MainActivity.this, symbol.getTextField(), Toast.LENGTH_SHORT).show());
        });


        UiSettings uiSettings = mapboxMap.getUiSettings();
        uiSettings.setLogoEnabled(false);
        uiSettings.setAttributionEnabled(false);
        uiSettings.setCompassGravity(Gravity.CENTER_VERTICAL);


    }

    private void addDestinationIconSymbolLayer(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addImage("destination-icon-id",
                BitmapFactory.decodeResource(this.getResources(), R.drawable.mapbox_marker_icon_default));
        GeoJsonSource geoJsonSource = new GeoJsonSource("destination-source-id");
        loadedMapStyle.addSource(geoJsonSource);
        SymbolLayer destinationSymbolLayer = new SymbolLayer("destination-symbol-layer-id", "destination-source-id");
        destinationSymbolLayer.withProperties(
                iconImage("destination-icon-id"),
                iconAllowOverlap(true),
                iconIgnorePlacement(true)
        );
        loadedMapStyle.addLayer(destinationSymbolLayer);
    }

    @SuppressLint({"MissingPermission", "RestrictedApi"})
    private void initialiserLocalisation()
    {
        if(locationManager == null)
        {
            locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
            Criteria criteres = new Criteria();

            criteres.setAccuracy(Criteria.ACCURACY_FINE);

            criteres.setAltitudeRequired(true);

            criteres.setBearingRequired(true);

            criteres.setSpeedRequired(true);

            criteres.setCostAllowed(true);
            criteres.setPowerRequirement(Criteria.POWER_HIGH);

            fournisseur = locationManager.getBestProvider(criteres, true);
            Log.d("GPS", "fournisseur : " + fournisseur);
        }

        try {
            Location localisation = loc.getLastKnownLocation(fournisseur);

            String la = String.valueOf(localisation.getLatitude());
            String ln = String.valueOf(localisation.getLongitude());


        } catch (Exception e) {
            locationManager.requestLocationUpdates(fournisseur, 1000, 0,this);
        }





    }

    @SuppressLint("RestrictedApi")
    @SuppressWarnings( {"MissingPermission"})
    @Override
    public boolean onMapClick(@NonNull LatLng point) {

        LocationComponent locationComponent = mapboxMap.getLocationComponent();

        Point destinationPoint = Point.fromLngLat(point.getLongitude(), point.getLatitude());
        Point originPoint = Point.fromLngLat(locationComponent.getLastKnownLocation().getLongitude(),
                locationComponent.getLastKnownLocation().getLatitude());

        GeoJsonSource source = mapboxMap.getStyle().getSourceAs("destination-source-id");
        if (source != null) {
            source.setGeoJson(Feature.fromGeometry(destinationPoint));
        }

        getRoute(originPoint, destinationPoint);
        fab2.setVisibility(View.VISIBLE);
      /*  button.setEnabled(true);
        button.setBackgroundResource(R.color.mapboxBlue);*/
        return true;
    }

    private void getRoute(Point origin, Point destination) {
        NavigationRoute.builder(this)
                .accessToken(Mapbox.getAccessToken())
                .origin(origin)
                .destination(destination)
                .build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
// You can get the generic HTTP info about the response
                        Log.d(TAG, "Response code: " + response.code());
                        if (response.body() == null) {
                            Log.e(TAG, "No routes found, make sure you set the right user and access token.");
                            return;
                        } else if (response.body().routes().size() < 1) {
                            Log.e(TAG, "No routes found");
                            return;
                        }

                        currentRoute = response.body().routes().get(0);

// Draw the route on the map
                        if (navigationMapRoute != null) {
                            navigationMapRoute.removeRoute();
                        } else {
                            navigationMapRoute = new NavigationMapRoute(null, mapView, mapboxMap, R.style.NavigationMapRoute);
                        }
                        navigationMapRoute.addRoute(currentRoute);
                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                        Log.e(TAG, "Error: " + throwable.getMessage());
                    }
                });
    }


    private void searchNominatim(String query) {
        if (query.matches("")) {
            Context context = getApplicationContext();
            CharSequence text1 = "Aucune entrée";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text1, duration);
            toast.show();
        } else {
            if (Function.isNetworkAvailable(getApplicationContext())) {
                ApiInterface apiService = APIClient.getClient().create(ApiInterface.class);
                Call<List<Nominatim>> call = apiService.nominatim(query, "json", 1, "cm");
                call.enqueue(new Callback<List<Nominatim>>() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onResponse(@NonNull Call<List<Nominatim>> call, @NonNull Response<List<Nominatim>> response) {
                        assert response.body() != null;
                        if (response.body().isEmpty()) {
                            Context context = getApplicationContext();
                            CharSequence text1 = "Aucun résultat";
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(context, text1, duration);
                            toast.show();
                        } else {
                            nomin = response.body();
                            if (nomin.size() > 1) {
                                Context context = getApplicationContext();
                                CharSequence text1 = "Soyez plus précis";
                                int duration = Toast.LENGTH_SHORT;

                                Toast toast = Toast.makeText(context, text1, duration);
                                toast.show();
                            } else {
                                double lat = Double.parseDouble(nomin.get(0).getLat());
                                double lng = Double.parseDouble(nomin.get(0).getLon());
                                String display_name = nomin.get(0).getDisplayName();

                                CameraPosition cam = new CameraPosition.Builder()
                                        .target(new LatLng(lat, lng))
                                        .zoom(17)
                                        .build();

                                symbolManager.create(new SymbolOptions()
                                        .withLatLng(new LatLng(lat, lng))
                                        .withIconImage("marker")
                                        .withIconSize(1.5f)
                                        .withIconOffset(new Float[]{0f, -1.5f})
                                       // .withZIndex(10)
                                        .withTextField(display_name)
                                        .withTextHaloColor("rgba(255, 255, 255, 100)")
                                        .withTextHaloWidth(5.0f)
                                        .withTextAnchor("top")
                                        .withTextOffset(new Float[]{0f, 1.5f})
                                      //  .setDraggable(false)
                                );

                                mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(cam), 5000);
                                search.setQuery("", false);
                                search.clearFocus();
                            }

                        }


                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Nominatim>> call, @NonNull Throwable t) {
                        Log.e("main3", t.toString());
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void textChanged(String text) {
        recyclerView.setVisibility(View.VISIBLE);
        mQueryString = text;
        handler.removeCallbacksAndMessages(null);
        handler.postDelayed(() -> {
            if (Function.isNetworkAvailable(getApplicationContext())) {
                ApiInterface apiService = APIClient.getClient().create(ApiInterface.class);
                Call<List<Nominatim>> call = apiService.nominatim(mQueryString, "json", 1, "cm");
                call.enqueue(new Callback<List<Nominatim>>() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onResponse(@NonNull Call<List<Nominatim>> call, @NonNull Response<List<Nominatim>> response) {

                        nomin = response.body();
                        customSuggestionsAdapter = new SearchAdapter(nomin, R.layout.item_custom_suggestion, getApplicationContext(), mapboxMap, MainActivity.this, recyclerView, search, mapView, symbolManager);
                        recyclerView.setAdapter(customSuggestionsAdapter);
                        customSuggestionsAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Nominatim>> call, @NonNull Throwable t) {
                        Log.e("main3", t.toString());
                    }
                });

            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }, 300);


    }

    @Override
    public void onLocationChanged(Location location) {
        locationManager.removeUpdates(this);

        String la = String.valueOf(location.getLatitude());
        String ln = String.valueOf(location.getLongitude());
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
