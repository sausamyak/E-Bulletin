package com.example.samyaksau.e_bulletin.ebulletin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.samyaksau.e_bulletin.R;
import com.example.samyaksau.e_bulletin.details.NoticeDetail;
import com.example.samyaksau.e_bulletin.helper.AppConstants;
import com.example.samyaksau.e_bulletin.httputil.Action;
import com.example.samyaksau.e_bulletin.httputil.OnHttpTaskListener;
import com.example.samyaksau.e_bulletin.httputil.Request;
import com.example.samyaksau.e_bulletin.httputil.RequestMethod;
import com.example.samyaksau.e_bulletin.httputil.Response;
import com.example.samyaksau.e_bulletin.result.GetAllNoticesResult;
import com.example.samyaksau.e_bulletin.result.GetAllNoticesResult.NoticeList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * @Author   Samyak Sau
 * @College Jaipur National University, Jaipur
 */
public class NoticeActivity extends BaseActivity implements OnItemClickListener, OnHttpTaskListener {

    private ListView listNotice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice_list);

        setHeader(getString(R.string.header_view_notice_list));
        initView();
    }

    private void initView() {
        listNotice = (ListView)findViewById(R.id.listview_notice);

        listNotice.setOnItemClickListener(this);

        sendHttpRequest(Action.GET_ALL_NOTICES, null);
    }

    private class NoticeAdapter extends BaseAdapter {

        private ArrayList<NoticeDetail> list;

        public NoticeAdapter(ArrayList<NoticeDetail> list) {
            super();
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.inflater_notice_list, null);
            TextView tvTitle = (TextView)view.findViewById(R.id.textview_notice_title);
            TextView tvDescription = (TextView)view.findViewById(R.id.textview_notice_description);
            TextView tvTime = (TextView)view.findViewById(R.id.textview_notice_detail_time_and_date);

            tvTitle.setText(list.get(position).getTitle());
            tvDescription.setText(list.get(position).getDescription());
            tvTime.setText(list.get(position).getDate() + " " + list.get(position).getTime());

            view.setTag(list.get(position));
            return view;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        NoticeDetail tag = (NoticeDetail)arg1.getTag();
        if (tag != null) {
            startActivity(new Intent(getApplicationContext(), ViewNotice.class).putExtra(AppConstants.KEY_NOTICE, tag));
        }
    }

    @Override
    public void sendHttpRequest(Action ac, String... param) {
        showProgressBar();
        Request request = new Request(ac, this);
        request.execute(RequestMethod.GET, ac.toString());
    }

    @Override
    public void OnReceiveResponse(Action ac, Response response) {

        if (response.isSuccess()) {
            try {
                Gson gson = new Gson();
                Type type = new TypeToken<GetAllNoticesResult>() {}.getType();

                GetAllNoticesResult postNoticeResult = gson.fromJson(response.getResult(), type);

                if (postNoticeResult.success.trim().equalsIgnoreCase("true")) {
                    ArrayList<NoticeList>noticeList = postNoticeResult.notices;
                    if (!noticeList.isEmpty()) {
                        ArrayList<NoticeDetail> mList = new ArrayList<NoticeDetail>();
                        NoticeDetail noticeDetail = null;
                        for (int i = (noticeList.size()-1); i >= 0; i--) {

                            noticeDetail = new NoticeDetail();

                            noticeDetail.setNoticeId(noticeList.get(i).noticeId);

                            noticeDetail.setDescription(noticeList.get(i).description);

                            noticeDetail.setDate(noticeList.get(i).date);

                            noticeDetail.setTitle(noticeList.get(i).time);

                            mList.add(noticeDetail);
                        }
                        NoticeAdapter noticeAdapter = new NoticeAdapter(mList);

                        listNotice.setAdapter(noticeAdapter);
                    } else  {
                        showToast(getString(R.string.notice_not_found));
                    }
                } else {
                    showToast((postNoticeResult.message));
                }
            } catch (Exception e) {
                e.printStackTrace();
                showToast(e.getMessage());
            }
        } else {
            showToast(response.getErromMessage());
        }
        dismissProgressBar();
    }
}
