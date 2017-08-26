package br.com.alphadev.saudeconectadaapp.model.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.alphadev.saudeconectadaapp.R;
import br.com.alphadev.saudeconectadaapp.model.bean.Video;

public class AdapterItemVideo extends BaseAdapter {

    private final List<Video> videos;
    private final Activity act;

    public AdapterItemVideo(List<Video> videos, Activity act) {
        this.videos = videos;
        this.act = act;
    }

    @Override
    public int getCount() {
        return videos.size();
    }

    @Override
    public Object getItem(int position) {
        return videos.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = act.getLayoutInflater()
                .inflate(R.layout.adapter_list_videos, parent, false);

        Video video = videos.get(position);
        ImageView imgYT = (ImageView) view.findViewById(R.id.img_video_adapter);
        TextView textViewVideo = (TextView) view.findViewById(R.id.txt_video_adapter);

        String img_url = "http://img.youtube.com/vi/" + video.getId().toString() + "/0.jpg"; // this is link which will give u thumnail image of that video

        // picasso jar file download image for u and set image in imagview
        Picasso.with(view.getContext())
                .load(img_url)
                .placeholder(R.mipmap.ic_thumbnails)
                .into(imgYT);

        textViewVideo.setText(video.getTitulo());

        return view;
    }
}
