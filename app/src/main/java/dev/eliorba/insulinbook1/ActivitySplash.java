package dev.eliorba.insulinbook1;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;

public class ActivitySplash extends AppCompatActivity {

    LottieAnimationView splashLottieAnimatioin;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        findView();








        showCubeMoving (splashLottieAnimatioin);
        splashLottieAnimatioin.animate().translationY(1400).setDuration(1000).setStartDelay(4000);
    }

    private void findView() {
        splashLottieAnimatioin = findViewById(R.id.lottie);
    }

    private void animationDone(){
        Intent intent = new Intent(this , MainActivity.class);
        startActivity(intent);
        finish();

    }

    private void showCubeMoving(final View v){
        splashLottieAnimatioin.animate().
                translationY(1400).
                setDuration(1000).
                setStartDelay(2000).
                setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        animationDone();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });

    }


}