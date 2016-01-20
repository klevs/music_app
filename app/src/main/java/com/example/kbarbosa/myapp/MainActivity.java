package com.example.kbarbosa.myapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kbarbosa.myapp.API.EchonestAPI;
import com.example.kbarbosa.myapp.model.Artist;
import com.example.kbarbosa.myapp.model.ArtistImage;
import com.example.kbarbosa.myapp.model.ArtistResponse;
import com.example.kbarbosa.myapp.model.Bucket;
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


    //Variables on handleImage method
    private int imageNumber;
    private String[] urls;

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
    void searchArtist() {

        final String artist = editText.getText().toString();
        makeApiCall(Bucket.BIOGRAPHY, artist, 1);
    }


    // This is the method that is called when the song button is clicked
    @OnClick(R.id.songButton)
    public void searchSong(View view) {
        final String artist = editText.getText().toString();
        makeApiCall(Bucket.SONG, artist, 15);

    }


    //Previous Button
    @OnClick(R.id.p_button)
    void previousbutton() {
        imageNumber--;
        String url = urls[imageNumber];
        Ion.with(iresultView)
                .load(url);

        if (imageNumber == 0) {
            previous_button.setVisibility(View.GONE);
        }

        if (imageNumber < urls.length - 1) {
            next_button.setVisibility(View.VISIBLE);
        }
    }

    //Next Button
    @OnClick(R.id.n_button)
    void nextbutton() {

        imageNumber++;
        String url = urls[imageNumber];
        Ion.with(iresultView)
                .load(url);

        if (imageNumber > 0) {
            previous_button.setVisibility(View.VISIBLE);
        }

        if (imageNumber == urls.length - 1) {
            next_button.setVisibility(View.GONE);
        }

    }


    // This is the method that is called when the image button is clicked
    @OnClick(R.id.imageButton)
    public void searchImage(final View view) {
        final String artist = editText.getText().toString();
        makeApiCall(Bucket.IMAGE, artist, 15);

    }


    private void makeApiCall(final Bucket bucket, String searchTerm, int maxResults) {

        //Retrofit session starts here...
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(EchonestAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Create an adapter for retrofit with base url
        EchonestAPI api = retrofit.create(EchonestAPI.class);
        Call<ArtistResponse> call = api.searchArtist(EchonestAPI.api_key, "json", searchTerm, bucket.text, maxResults);

        call.enqueue(new Callback<ArtistResponse>() {
            @Override
            public void onResponse(Response<ArtistResponse> response) {

                if (response.isSuccess()) {
                    switch (bucket) {
                        case BIOGRAPHY:
                            handleBio(response.body());
                            break;
                        case IMAGE:
                            handleImages(response.body());
                            break;
                        case SONG:
                            handleSongs(response.body());
                            break;
                    }
                } else {

                    // Shows a Toast Message warning the user about some error
                    Context context = getApplicationContext();
                    CharSequence text = "SEARCH FAILED!";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        });
    }


    private void handleBio(ArtistResponse artistResponse) {

        List<String> artists = new ArrayList<>(artistResponse.getResponse().getArtists().length);
        for (Artist artist : artistResponse.getResponse().getArtists()) {
            artists.add(artist.getName());
        }

        //Shows the biography results
        resultsContainer.setVisibility(View.VISIBLE);
        iresultView.setVisibility(View.GONE);
        previous_button.setVisibility(View.GONE);
        next_button.setVisibility(View.GONE);
        resultView.setText("Artist: " + TextUtils.join("\n", artists)
                + "\nStatus: " + artistResponse.getResponse().getStatus().getMessage()
                + "\nCode: " + artistResponse.getResponse().getStatus().getCode()
                + "\nVersion: " + artistResponse.getResponse().getStatus().getVersion()
                + "\nBiography: " + artistResponse.getResponse().getArtists()[0].getBiographies()[0].getText());

    }

    private void handleImages(ArtistResponse artistResponse) {

        // Image View
        Artist[] artists = artistResponse.getResponse().getArtists();
        if (artists.length == 0) {
            urls = new String[0];
            imageNumber = 0;
            previous_button.setVisibility(View.GONE);
            next_button.setVisibility(View.GONE);
            return;
        }

        Artist a = artists[0];

        ArtistImage[] images = a.getImages();
        urls = new String[images.length];
        imageNumber = 0;
        previous_button.setVisibility(View.GONE);


        if (images.length == 0) {
            next_button.setVisibility(View.GONE);
            return;
        }

        if (images.length == 1) {
            next_button.setVisibility(View.GONE);
        } else {
            next_button.setVisibility(View.VISIBLE);
        }

        for (int i = 0; i < images.length; i++) {
            urls[i] = images[i].getUrl();

        }
        
        ArtistImage image = images[0];
        String url = image.getUrl();

        //ION Image
        Ion.with(iresultView)
                .load(url);

        // Visibility of Scroll and ImageView
        resultsContainer.setVisibility(View.GONE);
        iresultView.setVisibility(View.VISIBLE);

    }

    private void handleSongs(ArtistResponse artistResponse) {

        //Show the songs results
        Artist[] artists = artistResponse.getResponse().getArtists();
        if (artists.length == 0) {
            return;
        }
        String song_list = "Most Famous Songs: \n";

        int i = 0;
        while (i < 15) {
            song_list = song_list.concat(artistResponse.getResponse().getArtists()[0].getSongs()[i].getTitle() + "\n");
            i++;
        }

        resultsContainer.setVisibility(View.VISIBLE);
        iresultView.setVisibility(View.GONE);
        previous_button.setVisibility(View.GONE);
        next_button.setVisibility(View.GONE);
        resultView.setText(song_list);

    }

}