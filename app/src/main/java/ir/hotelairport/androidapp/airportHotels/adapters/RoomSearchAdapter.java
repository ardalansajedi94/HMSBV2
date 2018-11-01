package ir.hotelairport.androidapp.airportHotels.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import ir.hotelairport.androidapp.R;


public class RoomSearchAdapter extends BaseAdapter {

    TextView adultText;
    private ArrayList<ArrayList<Integer>> occupants;
    private ArrayList<Integer> adtList = new ArrayList<>();
//  private ArrayList<Integer> cldList = new ArrayList<>();

    public RoomSearchAdapter(ArrayList<ArrayList<Integer>> occupants){

        this.occupants = occupants;


        for(int i=1; i < 3; i++){
            adtList.add(i);
        }
//
//        for(int i=0; i < 5; i++){
//            cldList.add(i);
//        }

    }

    @Override
    public int getCount() {
        return this.occupants.size();
    }

    @Override
    public Object getItem(int i) {

        if(this.occupants.size() - 1 > i){
            return this.occupants.get(i);
        }

        ArrayList<Integer> arr = new ArrayList<>();

        arr.add(1);
        arr.add(0);

        return arr;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        final int j = i;

        if (view == null) {

            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate( R.layout.room_occupants, viewGroup, false);

            Spinner adults = view.findViewById(R.id.adult_count);
            adultText = view.findViewById(R.id.textView6);
          //  Spinner children = view.findViewById(R.idString.child_count);

            adults.setAdapter(new ArrayAdapter<>(viewGroup.getContext(),
                    android.R.layout.simple_spinner_item, this.adtList));


//            children.setAdapter(new ArrayAdapter<>(viewGroup.getContext(),
//                    android.R.layout.simple_spinner_item, this.cldList));

            adults.setSelection(this.occupants.get(j).get(0)-1);

           // children.setSelection(this.occupants.get(j).get(1));

            adults.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view,
                                           int position, long id) {

                    RoomSearchAdapter.this.occupants.get(j).set(0, RoomSearchAdapter.this.adtList.get(position));

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {


                }
            });

//            children.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//
//                @Override
//                public void onItemSelected(AdapterView<?> adapterView, View view,
//                                           int position, long idString) {
//
//                 //   RoomSearchAdapter.this.occupants.get(j).set(1, RoomSearchAdapter.this.cldList.get(position));
//
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> adapterView) {
//
//                }
//            });
        }

        return view;
    }



    public ArrayList<ArrayList<Integer>> getOccupants(){
        return this.occupants;
    }
}
