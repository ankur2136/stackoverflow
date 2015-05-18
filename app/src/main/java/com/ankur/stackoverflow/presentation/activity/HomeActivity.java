package com.ankur.stackoverflow.presentation.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.ankur.stackoverflow.R;
import com.ankur.stackoverflow.common.NavigationUtils;
import com.ankur.stackoverflow.common.ViewUtils;
import com.ankur.stackoverflow.domain.dto.QuestionItem;
import com.ankur.stackoverflow.presentation.fragment.AnswersListFragment;
import com.ankur.stackoverflow.presentation.fragment.UniSearchFragment;
import com.ankur.stackoverflow.presentation.view.BaseView;
import com.ankur.stackoverflow.utils.LogUtils;
import com.ankur.stackoverflow.utils.Utils;

public class HomeActivity extends BaseActivity implements BaseView.InteractionListener<Object> {

    private static final String LOG_TAG = "HOME_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // noinspection SimplifiableIfStatement
        switch (id) {
        case R.id.search:
            NavigationUtils.startFragment(this.getSupportFragmentManager(), R.id.fl_fragment_container,
                    UniSearchFragment.newInstance(), true, NavigationUtils.SLIDE_FROM_RIGHT);
            break;
        case R.id.action_dump:
            try {
                Utils.dbDump(getApplicationContext());
                ViewUtils.showShortToast(getApplicationContext(), "Dump Success");
            } catch (Exception ex) {
                LogUtils.errorLog(LOG_TAG, "Failed to take DB dump", ex);
                ViewUtils.showShortToast(getApplicationContext(), "Dump Error");
            }
            break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }

    @Override
    public void onItemClick(Object item) {
        if (item instanceof QuestionItem) {
            QuestionItem questionItem = (QuestionItem) item;
            LogUtils.debugLog(LOG_TAG, questionItem.mTitle);

            Bundle bundle = AnswersListFragment.getAnswerItemBundle(questionItem);
            NavigationUtils.startFragment(this.getSupportFragmentManager(), R.id.fl_fragment_container,
                    AnswersListFragment.newInstance(bundle), true, NavigationUtils.SLIDE_FROM_RIGHT);
        }
    }
}
