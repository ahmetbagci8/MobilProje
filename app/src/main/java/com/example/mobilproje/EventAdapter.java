package com.example.mobilproje;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
    private Context mContext;
    private Cursor mCursor;
    public EventAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
    }
    public class EventViewHolder extends RecyclerView.ViewHolder{
        TextView nameText;
        TextView categoryText;
        TextView dateText;
        ImageView seeView;
        ImageView deleteView;
        public EventViewHolder(View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.name_Text);
            categoryText = itemView.findViewById(R.id.category_Text);
            dateText = itemView.findViewById(R.id.date_Text);
        }
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.card_item, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final EventViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)) {
            return;
        }
        String name = mCursor.getString(mCursor.getColumnIndex("name"));
        String category = mCursor.getString(mCursor.getColumnIndex("category"));
        String date = mCursor.getString(mCursor.getColumnIndex("date"));
        final long id = mCursor.getLong(mCursor.getColumnIndex("id"));
        holder.nameText.setText(name);
        holder.categoryText.setText(category);
        holder.dateText.setText(date);
        holder.itemView.setTag(id);

    }
    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }
    public void swapCursor(Cursor newCursor) {
        if (mCursor != null) {
            mCursor.close();
        }
        mCursor = newCursor;
        if (newCursor != null) {
            notifyDataSetChanged();
        }
    }
}
