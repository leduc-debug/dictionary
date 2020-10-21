package com.lduwcs.dictionary.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.lduwcs.dictionary.DefinitionActivity;
import com.lduwcs.dictionary.R;
import com.lduwcs.dictionary.database.DatabaseAccess2;

import java.util.List;

public class VietnameseToEnglish extends Fragment {

    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_vietnamese_to_english, container, false);

        this.listView = (ListView) root.findViewById(R.id.listView2);
        DatabaseAccess2 databaseAccess = DatabaseAccess2.getInstance(getContext(),"viet_anh");
        databaseAccess.open();
        List<String> vietAnh = databaseAccess.getWords("a");
        databaseAccess.close();
        Log.d("TAG", "onCreateView: " +vietAnh);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, vietAnh);
        this.listView.setAdapter(adapter);

        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(),DefinitionActivity.class);

                intent.putExtra("WORD",vietAnh.get(position));
                intent.putExtra("TYPE",1);

                getActivity().startActivity(intent);
            }
        });

        return root;
    }
}