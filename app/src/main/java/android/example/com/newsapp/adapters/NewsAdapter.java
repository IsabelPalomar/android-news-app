package android.example.com.newsapp.adapters;

import android.content.Context;
import android.example.com.newsapp.R;
import android.example.com.newsapp.models.News;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class NewsAdapter extends ArrayAdapter<News> {
    private static class ViewHolder {
        public ImageView ivImage;
        public TextView tvTitle;
        public TextView tvContent;
    }

    public NewsAdapter(Context context, ArrayList<News> arrNews) {
        super(context, 0, arrNews);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        final News news = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.news_row, parent, false);
            viewHolder.ivImage = (ImageView) convertView.findViewById(R.id.ivNewsImage);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            viewHolder.tvContent = (TextView) convertView.findViewById(R.id.tvContentSnippet);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Populate the data into the template view using the data object
        viewHolder.tvTitle.setText(news.getTitle());
        viewHolder.tvContent.setText(news.getContentSnippet());
        //Picasso.with(getContext()).load(Uri.parse(news.getImageUrl())).error(R.drawable.app_img_default).into(viewHolder.ivImage);
        return convertView;
    }

}
