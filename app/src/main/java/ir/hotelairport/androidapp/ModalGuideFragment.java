package ir.hotelairport.androidapp;


import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ModalGuideFragment extends DialogFragment {


    private String title,content;
    TextView titleTv;
    TextView contentTv;
    Button closeBtn ;
    TextView moreInfoTitle;
    public ModalGuideFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title = getArguments().getString("title");
        content=getArguments().getString("content");
    }

    @Override
    public void onResume() {
        super.onResume();
        try{
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            int width = metrics.widthPixels;
            int height = metrics.heightPixels;
            getDialog().getWindow().setLayout((6 * width)/7, (4 * height)/5);
        }
        catch (NullPointerException e){

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_modal_guide, container, false);
        titleTv = view.findViewById(R.id.titleTv);
        contentTv = view.findViewById(R.id.contentTv);
        closeBtn = view.findViewById(R.id.close_btn);
        moreInfoTitle = view.findViewById(R.id.moreInfoTitle);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        try {
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        catch (NullPointerException e)
        {
        }
        titleTv.setText(title);
        contentTv.setText(content);
        moreInfoTitle.setText(title);
        return  view;
    }
    static ModalGuideFragment newInstance(String title,String content) {
        ModalGuideFragment f = new ModalGuideFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("content",content);
        f.setArguments(args);

        return f;
    }
}
