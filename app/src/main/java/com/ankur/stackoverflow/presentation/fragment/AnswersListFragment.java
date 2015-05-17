package com.ankur.stackoverflow.presentation.fragment;

import java.util.ArrayList;
import java.util.Collection;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ankur.stackoverflow.R;
import com.ankur.stackoverflow.common.QueryParams;
import com.ankur.stackoverflow.domain.dto.AnswerItem;
import com.ankur.stackoverflow.domain.dto.QuestionItem;
import com.ankur.stackoverflow.domain.usecase.UseCaseFactory;
import com.ankur.stackoverflow.presentation.adapter.AnswerListAdapter;
import com.ankur.stackoverflow.presentation.presenter.ItemPresenter;
import com.ankur.stackoverflow.presentation.view.CollectionView;
import com.ankur.stackoverflow.utils.LogUtils;

public class AnswersListFragment extends PresenterFragment<ItemPresenter<AnswerItem>> implements
        CollectionView<AnswerItem>, AnswerListAdapter.OnItemClickListener {

    private static final String             FRAGMENT_TAG         = AnswersListFragment.class.getName();

    private String                          LOG_TAG              = "ANSWER_LIST_FRAGMENT";

    private static final String             SCROLL_POSITION      = "scroll_position";

    private static final String             KEY_QUESTION_ITEM    = "question_item";

    private int                             mScrollPosition      = 0;

    AnswerListAdapter                       mQuestionListAdapter;

    LinearLayoutManager                     mLinearLayoutManager;

    ListView                                mListView;

    QuestionItem                            mQuestionItem;

    RelativeLayout                          mProgressView;

    RelativeLayout                          mRetryView;

    Button                                  mRetry;

    private InteractionListener<AnswerItem> mListener;

    /**
     * Should not be called from outside this fragment.
     */
    public AnswersListFragment() {
    }

    public static Bundle getAnswerItemBundle(QuestionItem questionItem) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_QUESTION_ITEM, questionItem);
        return bundle;
    }

    public static AnswersListFragment newInstance() {
        return newInstance(null);
    }

    public static AnswersListFragment newInstance(Bundle bundle) {
        AnswersListFragment fragment = new AnswersListFragment();
        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Override
    public String getFragmentTag() {
        return FRAGMENT_TAG;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (InteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onNewBundle(getArguments());
    }

    @Override
    public void onNewBundle(Bundle bundle) {
        super.onNewBundle(bundle);
        QuestionItem questionItem = (QuestionItem) bundle.getSerializable(KEY_QUESTION_ITEM);
        if (questionItem != null) {
            mQuestionItem = questionItem;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menu.clear();
    }

    @Override
    protected ItemPresenter<AnswerItem> createPresenter() {
        return new ItemPresenter<AnswerItem>(UseCaseFactory.newGetAnswersItemUseCaseInstance());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.fragment_media_list, container, false);
        findViews(v);
        bindViews();
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadMediaList();
        if (savedInstanceState != null) {
            int posArray = savedInstanceState.getInt(SCROLL_POSITION);
            if (posArray > 0) {
                mScrollPosition = posArray;
            }
        }
    }

    private void loadMediaList() {

        if (mQuestionItem.mAnswerCount > 0) {
            QueryParams queryParams = QueryParams.getNewInstance();
            queryParams.setId(mQuestionItem.mQuestionId);
            presenter.init(queryParams);
        } else {
            Toast.makeText(getContext(), "No answers found for this Question", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        presenter.resume();
        mActivity.supportInvalidateOptionsMenu();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mListView != null && mListView.getAdapter() != null) {
            outState.putInt(SCROLL_POSITION, mScrollPosition);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        LogUtils.debugLog(LOG_TAG, item.getItemId() + " ");
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.pause();
    }

    private void findViews(View view) {
        mListView = (ListView) view.findViewById(R.id.lv_list_view);
        mProgressView = (RelativeLayout) view.findViewById(R.id.rl_progress);
        mRetryView = (RelativeLayout) view.findViewById(R.id.rl_retry);
        mRetry = (Button) view.findViewById(R.id.bt_retry);
        getmActivity().getSupportActionBar().hide();
    }

    private void bindViews() {
        mLinearLayoutManager = new LinearLayoutManager(this.getActivity());
        mQuestionListAdapter = new AnswerListAdapter(mActivity, new ArrayList<AnswerItem>());
        mListView.setAdapter(mQuestionListAdapter);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void renderCollection(Collection<AnswerItem> questionItems) {
        if (questionItems != null && mQuestionListAdapter != null) {
            mQuestionListAdapter.setQuestionsCollection(questionItems);
            mQuestionListAdapter.notifyDataSetChanged();
            mQuestionListAdapter.setOnItemClickListener(this);
        }
    }

    @Override
    public void renderItem(AnswerItem questionItem) {
        LogUtils.debugLog(LOG_TAG, "HERE");
    }

    @Override
    public void viewItem(AnswerItem questionItem) {
        mListener.onItemClick(questionItem);
    }

    @Override
    public void deleteItem(AnswerItem item) {

    }

    @Override
    public void viewPopupWindow(View anchor, AnswerItem item) {
    }

    @Override
    public void showLoading() {
        this.mProgressView.setVisibility(View.VISIBLE);
        if (mActivity != null) {
            mActivity.setProgressBarIndeterminateVisibility(true);
        }
    }

    @Override
    public void hideLoading() {
        this.mProgressView.setVisibility(View.GONE);
        if (mActivity != null) {
            mActivity.setProgressBarIndeterminateVisibility(false);
        }
    }

    @Override
    public void showRetry() {
        mQuestionListAdapter.setQuestionsCollection(new ArrayList<AnswerItem>());
        mRetryView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideRetry() {
        mRetryView.setVisibility(View.GONE);
    }

    @Override
    public void showError(String message) {
        LogUtils.errorLog(LOG_TAG, message);
    }

    @Override
    public Context getContext() {
        return mApplication;
    }

    @Override
    public void onItemClicked(View v, AnswerItem questionItem) {
        if (presenter != null && questionItem != null) {
            presenter.onItemClicked(questionItem);
        }
    }
}
