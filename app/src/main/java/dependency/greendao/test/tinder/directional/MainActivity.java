package dependency.greendao.test.tinder.directional;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.like.LikeButton;
import com.like.OnLikeListener;
import com.mindorks.placeholderview.SwipeDecor;
import com.mindorks.placeholderview.SwipeDirectionalView;
import com.mindorks.placeholderview.listeners.ItemRemovedListener;

import java.util.ArrayList;
import java.util.List;

import dependency.greendao.test.tinder.directional.network.retroFit.ApiClient;
import dependency.greendao.test.tinder.directional.network.retroFit.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements TinderCard.Callback {

    private SwipeDirectionalView mSwipeView;
    private Context mContext;
    private int mAnimationDuration = 300;
    private boolean isToUndo = false;
    private LikeButton likeButton;
    List<Profile> profileList = new ArrayList<>();
    Profile profilemodel = new Profile();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        reload();
        mSwipeView = findViewById(R.id.swipeView);
        mContext = getApplicationContext();
        likeButton = findViewById(R.id.likebutton);


        int bottomMargin = Utils.dpToPx(160);
        Point windowSize = Utils.getDisplaySize(getWindowManager());
        mSwipeView.getBuilder()
                .setDisplayViewCount(3)
                .setIsUndoEnabled(true)
                .setSwipeVerticalThreshold(Utils.dpToPx(50))
                .setSwipeHorizontalThreshold(Utils.dpToPx(50))
                .setHeightSwipeDistFactor(10)
                .setWidthSwipeDistFactor(5)
                .setSwipeDecor(new SwipeDecor()
                        .setViewWidth(windowSize.x)
                        .setViewHeight(windowSize.y - bottomMargin)
                        .setViewGravity(Gravity.TOP)
                        .setPaddingTop(20)
                        .setSwipeAnimTime(mAnimationDuration)
                        .setRelativeScale(0.01f)
                        .setSwipeInMsgLayoutId(R.layout.tinder_swipe_in_msg_view)
                        .setSwipeOutMsgLayoutId(R.layout.tinder_swipe_out_msg_view));


        Point cardViewHolderSize = new Point(windowSize.x, windowSize.y - bottomMargin);

//        for (Profile profile : profileList) {
//            mSwipeView.addView(new TinderCard(mContext, profile, cardViewHolderSize, this));
//        }

        findViewById(R.id.rejectBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeView.doSwipe(false);
            }
        });

        findViewById(R.id.acceptBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeView.doSwipe(true);
            }
        });

//        findViewById(R.id.undoBtn).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mSwipeView.undoLastSwipe();
//            }
//        });

        mSwipeView.addItemRemoveListener(new ItemRemovedListener() {
            @Override
            public void onItemRemoved(int count) {
                if (isToUndo) {
                    isToUndo = false;
                    mSwipeView.undoLastSwipe();
                }
            }
        });
        likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                likeButton.setLiked(true);
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                likeButton.setLiked(false);
            }
        });
    }

    @Override
    public void onSwipeUp() {
        Toast.makeText(this, "SUPER LIKE! Show any dialog here.", Toast.LENGTH_SHORT).show();
        isToUndo = true;
    }

    public void reload() {

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<Profile>> call = apiInterface.TINDER_CARD_OBSERVABLE();
        call.enqueue(new Callback<List<Profile>>() {
            @Override
            public void onResponse(Call<List<Profile>> call, Response<List<Profile>> response) {
                int bottomMargin = Utils.dpToPx(160);
                Point windowSize = Utils.getDisplaySize(getWindowManager());
                Point cardViewHolderSize = new Point(windowSize.x, windowSize.y - bottomMargin);
                for (Profile profile : response.body()) {
                    mSwipeView.addView(new TinderCard(mContext, profile, cardViewHolderSize, this));
                }

//
// for (Profile profile : response.body()) {
//                    profileList.add(profile);
//                }
                Log.d("profile", "onNext: " + response.body());
            }
            @Override
            public void onFailure(Call<List<Profile>> call, Throwable t) {
                Log.e("error", "onFailure: " + t.getMessage());
            }
        });
    }
}
