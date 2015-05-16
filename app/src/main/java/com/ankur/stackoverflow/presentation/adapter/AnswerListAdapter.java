package com.ankur.stackoverflow.presentation.adapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ankur.stackoverflow.R;
import com.ankur.stackoverflow.common.ViewUtils;
import com.ankur.stackoverflow.domain.dto.AnswerItem;

public class AnswerListAdapter extends BaseAdapter {

    private String               LOG_TAG = "ANSWER_RESULT_ADAPTER";

    private final LayoutInflater mLayoutInflater;

    private List<AnswerItem>     mQuestions;

    private OnItemClickListener  mOnItemClickListener;

    private Context              mContext;

    public interface OnItemClickListener {
        void onItemClicked(View anchor, AnswerItem questionItem);
    }

    public AnswerListAdapter(Context context, Collection<AnswerItem> collection) {
        validateCollection(collection);
        mContext = context;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mQuestions = new ArrayList<>(collection);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    private void validateCollection(Collection<AnswerItem> questionItems) {
        if (questionItems == null) {
            throw new IllegalArgumentException("The list cannot be null");
        }
    }

    public void setQuestionsCollection(Collection<AnswerItem> questionItems) {
        validateUsersCollection(questionItems);
        mQuestions = new ArrayList<>(questionItems);
        notifyDataSetChanged();
    }

    private void validateUsersCollection(Collection<AnswerItem> questionItems) {
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
        AnswerItem result = (AnswerItem) getItem(position);

        ViewHolder resultViewHolder;

        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.answer_row_item, parent, false);
        }

        if (convertView.getTag() == null) {
            resultViewHolder = new ViewHolder();
            resultViewHolder.instantiate(convertView);
            convertView.setTag(resultViewHolder);
        } else {
            resultViewHolder = (ViewHolder) convertView.getTag();
        }

        final AnswerItem questionItem = mQuestions.get(position);
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

        TextView upVotes;
        TextView downVotes;
        TextView isAccepted;
        TextView body;
        TextView author;
        int      position = -1;

        public void instantiate(View view) {
            upVotes = (TextView) view.findViewById(R.id.tv_upvotes);
            downVotes = (TextView) view.findViewById(R.id.tv_downvotes);
            isAccepted = (TextView) view.findViewById(R.id.tv_accepted);
            body = (TextView) view.findViewById(R.id.tv_body);
            author = (TextView) view.findViewById(R.id.tv_author);
        }

        public void bindViews(AnswerItem item) {
            ViewUtils.setupTextView(upVotes, item.mUpVote.toString());
            ViewUtils.setupTextView(downVotes, item.mDownVote.toString());

            if (item.mIsAccepted) {
                ViewUtils.setupTextView(isAccepted, "Accepted Answer");
            }

            body.setAutoLinkMask(0);
            Pattern pattern = Pattern.compile("@([A-Za-z0-9_-]+)");
            String scheme = "http://...";
            Linkify.addLinks(body, pattern, scheme);
            ViewUtils.setupTextView(body, Html.fromHtml(item.mBody));
            ViewUtils.setupTextView(author, item.mOwnerInfo.mDisplayName);
            body.setMovementMethod(LinkMovementMethod.getInstance());
        }

        public void setPosition(int pos) {
            position = pos;
        }

    }

}
