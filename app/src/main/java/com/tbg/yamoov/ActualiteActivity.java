package com.tbg.yamoov;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.tbg.yamoov.adapter.CardsAdapter;
import com.tbg.yamoov.model.CardModel;

public class ActualiteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualite);

        ListView lvCards = (ListView) findViewById(R.id.list_cards);
        CardsAdapter adapter = new CardsAdapter(this);

        lvCards.setAdapter(adapter);
        adapter.addAll(new CardModel(R.drawable.img, "R.string.venus", "R.string.mercury_sub"),
                new CardModel(R.drawable.img, "R.string.venus", "R.string.venus_sub"),
                new CardModel(R.drawable.img, "R.string.venus", "R.string.earth_sub"),
                new CardModel(R.drawable.img, "R.string.venus", "R.string.mars_sub"),
                new CardModel(R.drawable.img, "R.string.venus", "R.string.jupiter_sub"),
                new CardModel(R.drawable.img, "R.string.venus", "R.string.saturn_sub"),
                new CardModel(R.drawable.img, "R.string.venus", "R.string.uranus_sub"),
                new CardModel(R.drawable.img, "R.string.venus", "R.string.neptune_sub"),
                new CardModel(R.drawable.img, "R.string.venus", "R.string.pluto_sub"));
    }
}
