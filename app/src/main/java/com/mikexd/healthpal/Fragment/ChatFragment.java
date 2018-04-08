package com.mikexd.healthpal.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mikexd.healthpal.Activity.ChatActivity;
import com.mikexd.healthpal.Activity.ExplorerActivity;
import com.mikexd.healthpal.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {

    private Button chat;
    private Button explorer;

    public ChatFragment()  {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_chat, container, false);

        explorer = v.findViewById(R.id.explore);
        explorer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ExplorerActivity.class);
                startActivity(i);
            }
        });

        chat = v.findViewById(R.id.chat);
        chat.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ChatActivity.class);
                startActivity(i);
            }
        });

        return v;
    }

}
