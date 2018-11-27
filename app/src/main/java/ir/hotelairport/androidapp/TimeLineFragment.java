package ir.hotelairport.androidapp;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ir.hotelairport.androidapp.Adapters.TimeLineListAdapter;
import ir.hotelairport.androidapp.Models.TimeLine;
import ir.hotelairport.androidapp.SQLiteDB.DatabaseHandler;
import ir.hotelairport.androidapp.Server.RequestInterface;
import ir.hotelairport.androidapp.Server.ServerResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class TimeLineFragment extends Fragment {

    private ListView TimeLineList;
    private ArrayList<TimeLine> TimeLineContent;
    private ArrayList<TimeLine> TimeLineAllContent;
    private TimeLineListAdapter timeLineListAdapter;
    private SharedPreferences user_detail;
    private TextView time_line_empty_tv;
    private SwipeRefreshLayout swipeRefreshLayout;
    private DatabaseHandler db;
    ProgressDialog progress;

    public TimeLineFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_time_line, container, false);
        db = new DatabaseHandler(getActivity());
        user_detail = getActivity().getSharedPreferences(Constants.USER_DETAIL, Context.MODE_PRIVATE);
        TimeLineList = (ListView) view.findViewById(R.id.time_line_list);
        time_line_empty_tv = (TextView) view.findViewById(R.id.time_line_empty_tv);
        TimeLineContent = new ArrayList<TimeLine>();
        TimeLineAllContent = new ArrayList<TimeLine>();
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getTimeLine(true);
            }
        });
        getTimeLine(false);
        TimeLineList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if ((++firstVisibleItem) + visibleItemCount > totalItemCount) {
                    for (int i = 0; i < 10 && TimeLineAllContent.size() > 0; i++) {
                        TimeLineContent.add(TimeLineAllContent.get(0));
                        TimeLineAllContent.remove(0);
                        timeLineListAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
        return view;
    }

    private void getTimeLine(final boolean is_refreshing) {
        if (!is_refreshing) {
            progress = new ProgressDialog(getActivity());
            progress.setMessage(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID, 1), "connecting_to_server"));
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.setProgress(0);
            progress.show();
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestInterface requestInterface = retrofit.create(RequestInterface.class);
        Call<ServerResponse> response;
        String jwt = user_detail.getString(Constants.JWT, "");
        response = requestInterface.get_time_line(jwt, user_detail.getInt(Constants.LANGUAGE_ID, 1));
        RetrofitWithRetry.enqueueWithRetry(response, 3, new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
                if (!is_refreshing) {
                    progress.dismiss();
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                }
                ServerResponse resp = response.body();
                switch (response.code()) {
                    case 200:
                        if (resp != null) {
                            TimeLineContent = new ArrayList<TimeLine>();
                            TimeLineAllContent = new ArrayList<TimeLine>();
                            ArrayList<TimeLine> serverTimeLine = resp.getTimeLine();
                            if (serverTimeLine.size() == 0) {
                                TimeLineList.setVisibility(View.GONE);
                                time_line_empty_tv.setVisibility(View.VISIBLE);
                            } else {
                                TimeLineList.setVisibility(View.VISIBLE);
                                time_line_empty_tv.setVisibility(View.GONE);
                                for (int i = 0; i < serverTimeLine.size(); i++) {
                                    if (serverTimeLine.get(i).getResponses() != null) {
                                        ArrayList<TimeLine> responses = serverTimeLine.get(i).getResponses();
                                        for (int j = 0; j < responses.size(); j++) {
                                            TimeLine timeLine_response = responses.get(j);
                                            timeLine_response.setIs_response(true);
                                            if (i < 10)
                                                TimeLineContent.add(timeLine_response);
                                            else
                                                TimeLineAllContent.add(timeLine_response);
                                        }
                                    }
                                    TimeLine timeLine_item = serverTimeLine.get(i);
                                    timeLine_item.setIs_response(false);
                                    if (i < 10)
                                        TimeLineContent.add(timeLine_item);
                                    else
                                        TimeLineAllContent.add(timeLine_item);


                                }
                                Log.i("first_item", TimeLineContent.get(0).getContent());
                                timeLineListAdapter = new TimeLineListAdapter(TimeLineContent, getActivity());
                                TimeLineList.setAdapter(timeLineListAdapter);
                                timeLineListAdapter.notifyDataSetChanged();
                            }
                        }
                        break;
                    default:
                        if (resp != null) {
                            Toast.makeText(getActivity(), resp.getMessage(), Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(getActivity(), db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID, 1), "server_problem"), Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                if (!is_refreshing) {
                    progress.dismiss();
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                }
                Log.d("error:", t.getMessage());
            }
        });
    }
}
