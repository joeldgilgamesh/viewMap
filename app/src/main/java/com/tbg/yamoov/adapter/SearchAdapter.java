package com.tbg.yamoov.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;
import com.tbg.yamoov.MainActivity;
import com.tbg.yamoov.R;
import com.tbg.yamoov.model.Nominatim;

import java.util.List;
import java.util.Objects;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    private static final String MAKI_ICON_HARBOR = "harbor-15";
    private final List<Nominatim> nomin;
    private final int rowLayout;
    private final Context context;
    private final MapboxMap mapboxMap;
    private final MainActivity activity;
    private final RecyclerView recyclerView;
    private final SearchView search;
    private final MapView mapView;
    private final SymbolManager symbolManager;

    //ImageView image;


    public SearchAdapter(List<Nominatim> nomin, int rowLayout, Context context, MapboxMap mapboxMap, MainActivity activity, RecyclerView recyclerView, SearchView search, MapView mapView, SymbolManager symbolManager) {
        this.nomin = nomin;
        this.rowLayout = rowLayout;
        this.context = context;
        this.mapboxMap = mapboxMap;
        this.activity = activity;
        this.search = search;
        this.recyclerView = recyclerView;
        this.mapView = mapView;
        this.symbolManager = symbolManager;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchViewHolder holder, final int position) {


        holder.title.setText(nomin.get(position).getDisplayName());

        holder.title.setOnClickListener(v -> clickItemNomin(position));
    }

    private void clickItemNomin(int i) {

        if (symbolManager.getAnnotations().size() == 0) {
            double lat = Double.parseDouble(nomin.get(i).getLat());
            double lng = Double.parseDouble(nomin.get(i).getLon());
            String display_name = nomin.get(i).getDisplayName();


            CameraPosition cam = new CameraPosition.Builder()
                    .target(new LatLng(lat, lng))
                    .zoom(14)
                    .build();


            symbolManager.create(new SymbolOptions()
                    .withLatLng(new LatLng(lat, lng))
                    .withIconImage(MAKI_ICON_HARBOR)
                    .withIconSize(1.5f)
                    .withIconOffset(new Float[]{0f, -1.5f})
                    //.withZIndex(20)
                    .withTextField(display_name)
                    .withTextHaloColor("rgba(255, 255, 255, 100)")
                    .withTextHaloWidth(5.0f)
                    .withTextAnchor("top")
                    .withTextOffset(new Float[]{0f, 1.5f})
                   // .setDraggable(false)
            );

            mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(cam), 5000);
            recyclerView.setVisibility(View.GONE);
            search.clearFocus();
        } else {
            for (int j = 0; j < symbolManager.getAnnotations().size(); j++) {
                try {
                    symbolManager.delete(Objects.requireNonNull(symbolManager.getAnnotations().get(i)));
                } catch (Exception ignored) {
                    Log.e("symbolError",ignored.toString());
                }

            }

            double lat = Double.parseDouble(nomin.get(i).getLat());
            double lng = Double.parseDouble(nomin.get(i).getLon());
            String display_name = nomin.get(i).getDisplayName();


            CameraPosition cam = new CameraPosition.Builder()
                    .target(new LatLng(lat, lng))
                    .zoom(14)
                    .build();


            symbolManager.create(new SymbolOptions()
                    .withLatLng(new LatLng(lat, lng))
                    .withIconImage(MAKI_ICON_HARBOR)
                    .withIconSize(1.5f)
                    .withIconOffset(new Float[]{0f, -1.5f})
                  //  .withZIndex(20)
                    .withTextField(display_name)
                    .withTextHaloColor("rgba(255, 255, 255, 100)")
                    .withTextHaloWidth(5.0f)
                    .withTextAnchor("top")
                    .withTextOffset(new Float[]{0f, 1.5f})
                   // .setDraggable(false)
            );

            mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(cam), 5000);
            recyclerView.setVisibility(View.GONE);
            search.clearFocus();
        }


    }

    @Override
    public int getItemCount() {
        return nomin.size();
    }

    public static class SearchViewHolder extends RecyclerView.ViewHolder {
        final TextView title;
        final ImageView image;


        SearchViewHolder(View v) {
            super(v);
            title = v.findViewById(R.id.title);
            image = v.findViewById(R.id.imageView2);
        }
    }


}






