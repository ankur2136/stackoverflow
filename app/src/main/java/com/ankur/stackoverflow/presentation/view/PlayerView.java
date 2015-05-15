package com.ankur.stackoverflow.presentation.view;

import android.content.Context;
import android.content.Intent;

public interface PlayerView<T> {

    Context getContext();

    void setCurrentItem(T mediaItem);

    void updateUI(Intent intent);

    void updateAudioSessionId(Intent intent);

    void hidePlayer();

    void showPlayer();

}
