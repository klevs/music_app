package com.example.kbarbosa.myapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kbarbosa.myapp.API.EchonestAPI;
import com.example.kbarbosa.myapp.model.Artist;
import com.example.kbarbosa.myapp.model.ArtistImage;
import com.example.kbarbosa.myapp.model.ArtistResponse;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;


public class MainActivity extends AppCompatActivity {
    @Bind(R.id.artist)
    EditText editText;
    @Bind(R.id.queryButton)
    Button searchButton;
    @Bind(R.id.results_container)
    View resultsContainer;
    @Bind(R.id.responseView)
    TextView resultView;
    @Bind(R.id.songButton)
    Button searchSong;
    @Bind(R.id.imageButton)
    Button searchImage;
    @Bind(R.id.imageView)
    ImageView iresultView;
    @Bind(R.id.p_button)
    Button previous_button;
    @Bind(R.id.n_button)
    Button next_button;



    // This method creates the screen

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        previous_button.setVisibility(View.GONE);
        next_button.setVisibility(View.GONE);
    }

    // This is the method that is called when the search button is clicked
    @OnClick(R.id.queryButton)
    void searchArtist(View view) {

        final String artist = editText.getText().toString();

        //Retrofit session starts here...
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(EchonestAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Create an adapter for retrofit with base url
        EchonestAPI api = retrofit.create(EchonestAPI.class);

        String bucket = "";
        int results = 15;

        switch (bucket){
            case "biography":
                bucket = "biographies";
                results = 1;
                break;

            case "image":
                bucket = "images";
                break;

            case "song":
                bucket = "songs";
                break;

            default:
                break;
        }

        Call<ArtistResponse> call = api.searchArtist(EchonestAPI.api_key, "json", artist, "biographies", results);
        call.enqueue(new Callback<ArtistResponse>() {
            @Override
            public void onResponse(Response<ArtistResponse> response) {
                if (response.isSuccess()) {
                    Log.d("MESSAGE", "SUCCESS!");
                    ArtistResponse artistResponse = response.body();
                    com.example.kbarbosa.myapp.model.Response myResponse =
                            artistResponse.getResponse();

                    Log.i("INFO", artist);

                    List<String> artists = new ArrayList<>(myResponse.getArtists().length);
                    for (Artist artist : myResponse.getArtists()) {
                        artists.add(artist.getName());
                    }

                    //Shows the biography results
                    resultsContainer.setVisibility(View.VISIBLE);
                    iresultView.setVisibility(View.GONE);
                    previous_button.setVisibility(View.GONE);
                    next_button.setVisibility(View.GONE);
                    resultView.setText("Artist: " + TextUtils.join("\n", artists)
                            + "\nStatus: " + myResponse.getStatus().getMessage()
                            + "\nCode: " + myResponse.getStatus().getCode()
                            + "\nVersion: " + myResponse.getStatus().getVersion()
                            + "\nBiography: " + myResponse.getArtists()[0].getBiographies()[0].getText());
                } else {
                    Log.d("fail", "FAIL!");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @OnClick(R.id.songButton)
    public void searchSong(View view){
        final String artist = editText.getText().toString();

        //Retrofit session starts here...
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(EchonestAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Create an adapter for retrofit with base url
        EchonestAPI api = retrofit.create(EchonestAPI.class);

        Call<ArtistResponse> call = api.searchArtist(EchonestAPI.api_key, "json", artist, "songs", 15);
        call.enqueue(new Callback<ArtistResponse>() {
            @Override
            public void onResponse(Response<ArtistResponse> response) {
                if (response.isSuccess()) {
                    Log.d("MESSAGE", "SUCCESS!");
                    ArtistResponse artistResponse = response.body();
                    com.example.kbarbosa.myapp.model.Response myResponse =
                            artistResponse.getResponse();

                    //Show the songs results
                    Artist[] artists = myResponse.getArtists();
                    if (artists.length == 0){
                        return;
                    }
                    String song_list = "Most Famous Songs: \n";

                    int i=0;
                    while (i<15){
                        song_list = song_list.concat(myResponse.getArtists()[0].getSongs()[i].getTitle()+"\n");
                        i++;
                    }

                    resultsContainer.setVisibility(View.VISIBLE);
                    iresultView.setVisibility(View.GONE);
                    previous_button.setVisibility(View.GONE);
                    next_button.setVisibility(View.GONE);
                    resultView.setText(song_list);
                }
                else {
                    Log.d("fail", "FAIL!");
                }
            }
            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        });
    }

    int i=0;
    String url;

    @OnClick(R.id.p_button)
    public void previousbutton(View view){
        i--;

    }

    @OnClick(R.id.n_button)
    public void nextbutton(View view){
        i++;


    }

    @OnClick(R.id.imageButton)
    public void searchImage(final View view){
        final String artist = editText.getText().toString();

        //Retrofit session starts here...
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(EchonestAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Create an adapter for retrofit with base url
        EchonestAPI api = retrofit.create(EchonestAPI.class);

        Call<ArtistResponse> call = api.searchArtist(EchonestAPI.api_key, "json", artist, "images", 15);
        call.enqueue(new Callback<ArtistResponse>() {
            @Override
            public void onResponse(Response<ArtistResponse> response) {
                if (response.isSuccess()) {
                    Log.d("MESSAGE", "SUCCESS!");
                    ArtistResponse artistResponse = response.body();
                    com.example.kbarbosa.myapp.model.Response myResponse =
                            artistResponse.getResponse();


                    // Image View
                    Artist[] artists = myResponse.getArtists();
                    if (artists.length == 0){
                        return;
                    }

                    Artist a = artists[0];

                    ArtistImage[] images = a.getImages();
                    if(images.length==0){
                        return;
                    }

                    //int i = 0;

                        ArtistImage image = images[i];
                        url = image.getUrl();

                        //ION Image
                        Ion.with(iresultView)
                                .load(url);

                    nextbutton(view);

                    //previousClicked();
                   // nextClicked();

                    url = image.getUrl();

                    //ION Image
                    Ion.with(iresultView)
                            .load(url);


                    // Controls the Visibility of Previous and Next Buttons
                    if (i<1){
                        previous_button.setVisibility(View.GONE);
                        next_button.setVisibility(View.VISIBLE);
                    } else if (i>13){
                        previous_button.setVisibility(View.VISIBLE);
                        next_button.setVisibility(View.GONE);
                    } else {
                        previous_button.setVisibility(View.VISIBLE);
                        next_button.setVisibility(View.VISIBLE);
                    }

                    // Visibility of Scroll and ImageView
                    resultsContainer.setVisibility(View.GONE);
                    iresultView.setVisibility(View.VISIBLE);

                }
                else {
                    Log.d("fail", "FAIL!");
                }
            }
            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        });
    }

}