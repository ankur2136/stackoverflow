package com.ankur.stackoverflow.presentation.fragment;

import java.util.ArrayList;
import java.util.Collection;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
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
import com.ankur.stackoverflow.domain.dto.QuestionItem;
import com.ankur.stackoverflow.domain.usecase.UseCaseFactory;
import com.ankur.stackoverflow.presentation.adapter.SearchResultAdapter;
import com.ankur.stackoverflow.presentation.presenter.ItemPresenter;
import com.ankur.stackoverflow.presentation.view.CollectionView;
import com.ankur.stackoverflow.utils.LogUtils;

public class UniSearchFragment extends PresenterFragment<ItemPresenter<QuestionItem>> implements
        CollectionView<QuestionItem>, SearchResultAdapter.OnItemClickListener {

    private static final String FRAGMENT_TAG = UniSearchFragment.class.getName();

    private String              LOG_TAG      = "UNI_SEARCH_FRAGMENT";

    public void setFragmentTagSuffix(String fragmentTagSuffix) {
        mFragmentTagSuffix = fragmentTagSuffix;
    }

    private String                            mFragmentTagSuffix   = "";

    private static final String               SCROLL_POSITION      = "scroll_position";

    private static final String               SEARCH_QUERY         = "query";

    private static final String               KEY_QUESTION_ITEM_ID = "question_item_id";

    private int                               mScrollPosition      = 0;

    private SearchView                        mSearchView;

    SearchResultAdapter                       mQuestionListAdapter;

    String                                    mQuery;

    LinearLayoutManager                       mLinearLayoutManager;

    ListView                                  mListView;

    RelativeLayout                            mProgressView;

    RelativeLayout                            mRetryView;

    Button                                    mRetry;

    private InteractionListener<QuestionItem> mListener;

    /**
     * Should not be called from outside this fragment.
     */
    public UniSearchFragment() {
    }

    public static UniSearchFragment newInstance() {
        return newInstance(null);
    }

    public static UniSearchFragment newInstance(Bundle bundle) {
        UniSearchFragment fragment = new UniSearchFragment();
        if (bundle != null) {
            fragment.setArguments(bundle);
            fragment.setFragmentTagSuffix(bundle.getString(KEY_QUESTION_ITEM_ID));

        }
        return fragment;
    }

    @Override
    public String getFragmentTag() {
        return FRAGMENT_TAG + mFragmentTagSuffix;
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menu.clear();
        LogUtils.debugLog(LOG_TAG, "onCreateOptionsMenu");
        menuInflater.inflate(R.menu.uni_search, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(item);
        setupSearchView(mSearchView);

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                PreferenceManager.getDefaultSharedPreferences(getContext()).edit().putString("QUERY", query).commit();
                findResults(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                return true;
            }
        });

        item.expandActionView();
        setupKeyboard();
    }

    private void findResults(String query) {
        if (query == null || query.equals("")) {
            return;
        }

        // Clear the previous results
        mQuestionListAdapter.setQuestionsCollection(new ArrayList<QuestionItem>());

        mQuery = query;
        QueryParams queryParams = QueryParams.getNewInstance();
        queryParams.setText(query);
        presenter.init(queryParams);
        LogUtils.infoLog(LOG_TAG, "Query complete: " + query);
    }

    private void setupKeyboard() {
        if (mSearchView == null)
            return;

        if (LogUtils.isDebugLogEnabled())
            LogUtils.debugLog(LOG_TAG, "Bringing up keyboard");
        mSearchView.setIconified(false);
    }

    private void setupSearchView(SearchView searchView) {
        searchView.setQueryHint(getActivity().getString(R.string.search_hint));
        searchView.setSearchableInfo(((SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE))
                .getSearchableInfo(getActivity().getComponentName()));

        searchView.setIconifiedByDefault(false);
        searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);

        android.support.v7.widget.SearchView.SearchAutoComplete searchAutoComplete = (android.support.v7.widget.SearchView.SearchAutoComplete) searchView
                .findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchAutoComplete.setHintTextColor(Color.LTGRAY);
        searchAutoComplete.setTextColor(Color.WHITE);

    }

    @Override
    protected ItemPresenter<QuestionItem> createPresenter() {
        return new ItemPresenter<QuestionItem>(UseCaseFactory.newGetQuestionsItemUseCaseInstance());
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
        if (savedInstanceState != null) {
            int posArray = savedInstanceState.getInt(SCROLL_POSITION);
            String query = savedInstanceState.getString(SEARCH_QUERY);
            if (posArray > 0) {
                mScrollPosition = posArray;
            }

            if (query != null && !query.equals("")) {
                mQuery = query;
                findResults(mQuery);
            }
        } else {
            mQuery = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("QUERY", null);
            findResults(mQuery);
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
            outState.putString(SEARCH_QUERY, mQuery);
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
        getmActivity().getSupportActionBar().show();
    }

    private void bindViews() {
        mLinearLayoutManager = new LinearLayoutManager(this.getActivity());
        mQuestionListAdapter = new SearchResultAdapter(mActivity, new ArrayList<QuestionItem>());
        mListView.setAdapter(mQuestionListAdapter);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void renderCollection(Collection<QuestionItem> questionItems) {
        if (questionItems != null && mQuestionListAdapter != null) {
            if (questionItems.size() == 0) {
                PreferenceManager.getDefaultSharedPreferences(getContext()).edit().remove("QUERY").commit();
                Toast.makeText(getContext(), "Sorry No results found", Toast.LENGTH_SHORT).show();
                return;
            }
            mQuestionListAdapter.setQuestionsCollection(questionItems);
            mQuestionListAdapter.notifyDataSetChanged();
            mQuestionListAdapter.setOnItemClickListener(this);
        } else {
            PreferenceManager.getDefaultSharedPreferences(getContext()).edit().remove("QUERY").commit();
            Toast.makeText(getContext(), "Oops Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void viewItem(QuestionItem questionItem) {
        mListener.onItemClick(questionItem);
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
        mQuestionListAdapter.setQuestionsCollection(new ArrayList<QuestionItem>());
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
    public void onItemClicked(View v, QuestionItem questionItem) {
        if (presenter != null && questionItem != null) {
            presenter.onItemClicked(questionItem);
        }
    }
}
