package com.example.nitai.client_nitai;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class WikiFragment extends android.support.v4.app.Fragment {

    private WikiObject wikiObject;
    private TextView title;
    private TextView summery;
    private ImageView image;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wiki, container, false);
        Bundle bundle = getArguments();
        wikiObject = (WikiObject) bundle.getSerializable("wikiObject");
        title = view.findViewById(R.id.titleView);
        summery = view.findViewById(R.id.summery);
        image = view.findViewById(R.id.imageView);
        ImageView xButton = view.findViewById(R.id.xButton);
        xButton.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View v) {
                MainActivity.backClick();
            }
        });
        ImageView saveButton = view.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View v) {
                MainActivity.backClick();
            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        title.setText(wikiObject.getTitle());
        summery.setText(wikiObject.getSummary());
        String img = wikiObject.getImage();
        if (!img.equals("")) {
            Picasso.get().load(img).error(android.R.color.background_light).into(image);
        }
    }

}
