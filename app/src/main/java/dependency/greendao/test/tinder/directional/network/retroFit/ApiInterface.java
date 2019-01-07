package dependency.greendao.test.tinder.directional.network.retroFit;


import org.json.JSONArray;

import java.util.List;

import dependency.greendao.test.tinder.directional.Profile;
import dependency.greendao.test.tinder.directional.TinderCard;
import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.*;

public interface ApiInterface {

    @GET("/api/images")
    Call<List<Profile>> TINDER_CARD_OBSERVABLE();

}
