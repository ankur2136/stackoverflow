package com.ankur.stackoverflow.presenter.fragment.view;

import android.content.Context;
import android.content.Intent;

public interface PlayerView<T> {

    Context getContext();

    void setCurrentMediaItem(T mediaItem);

    void updateUI(Intent intent);

    void updateAudioSessionId(Intent intent);

    void hidePlayer();

    void showPlayer();

}
