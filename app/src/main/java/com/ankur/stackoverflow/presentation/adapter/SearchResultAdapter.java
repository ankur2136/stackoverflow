package com.ankur.stackoverflow.presentation.adapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ankur.stackoverflow.R;
import com.ankur.stackoverflow.common.ViewUtils;
import com.ankur.stackoverflow.domain.dto.QuestionItem;

public class SearchResultAdapter extends BaseAdapter {

    private String               LOG_TAG = "SEARCH_RESULT_ADAPTER";

    private final LayoutInflater mLayoutInflater;

    private List<QuestionItem>   mQuestions;

    private OnItemClickListener  mOnItemClickListener;

    private Context              mContext;

    public interface OnItemClickListener {
        void onItemClicked(View anchor, QuestionItem questionItem);
    }

    public SearchResultAdapter(Context context, Collection<QuestionItem> collection) {
        validateCollection(collection);
        mContext = context;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mQuestions = new ArrayList<>(collection);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    private void validateCollection(Collection<QuestionItem> questionItems) {
        if (questionItems == null) {
            throw new IllegalArgumentException("The list cannot be null");
        }
    }

    public void setQuestionsCollection(Collection<QuestionItem> questionItems) {
        validateUsersCollection(questionItems);
        mQuestions = new ArrayList<>(questionItems);
        notifyDataSetChanged();
    }

    private void validateUsersCollection(Collection<QuestionItem> questionItems) {
        if (questionItems == null) {
            throw new IllegalArgumentException("The list cannot be null");
        }
    }

    @Override
    public int getCount() {
        if (mQuestions == null)
            return 0;
        return mQuestions.size();
    }

    @Override
    public Object getItem(int position) {
        if (mQuestions == null)
            return null;
        return mQuestions.get(position);
    }

    @Override
    public long getItemId(int position) {
        if (mQuestions == null)
            return 0;
        return mQuestions.get(position).mQuestionId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        QuestionItem result = (QuestionItem) getItem(position);

        ViewHolder resultViewHolder;

        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.search_result_row_item, parent, false);
        }

        if (convertView.getTag() == null) {
            resultViewHolder = new ViewHolder();
            resultViewHolder.instantiate(convertView);
            convertView.setTag(resultViewHolder);
        } else {
            resultViewHolder = (ViewHolder) convertView.getTag();
        }

        final QuestionItem questionItem = mQuestions.get(position);
        convertView.findViewById(R.id.ll_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClicked(v, questionItem);
            }
        });

        resultViewHolder.bindViews(result);
        return convertView;
    }

    public static class ViewHolder {

        TextView    votes;
        TextView    answers;
        TextView    views;
        TextView    title;
        TextView    subtitle;
        int         position = -1;

        public void instantiate(View view) {
            votes = (TextView) view.findViewById(R.id.tv_votes);
            answers = (TextView) view.findViewById(R.id.tv_answers);
            views = (TextView) view.findViewById(R.id.tv_views);
            title = (TextView) view.findViewById(R.id.tv_title);
            subtitle = (TextView) view.findViewById(R.id.tv_subtitle);
        }

        public void bindViews(QuestionItem item) {
            ViewUtils.setupTextView(votes, item.mViewCount.toString());
            ViewUtils.setupTextView(answers, item.mAnswerCount.toString());
            ViewUtils.setupTextView(views, item.mViewCount.toString());
            ViewUtils.setupTextView(title, item.mTitle);
            ViewUtils.setupTextView(subtitle, item.mOwnerInfo.mDisplayName);
        }

        public void setPosition(int pos) {
            position = pos;
        }

    }

}
