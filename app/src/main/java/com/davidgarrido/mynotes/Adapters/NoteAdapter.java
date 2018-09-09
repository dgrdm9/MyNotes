package com.davidgarrido.mynotes.Adapters;

        import android.content.Context;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.BaseAdapter;
        import android.widget.TextView;
        import com.davidgarrido.mynotes.R;

        import com.davidgarrido.mynotes.Models.Note;


        import java.util.List;

public class NoteAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private List<Note> noteList;

    public NoteAdapter(Context context, int layout, List<Note> noteList){
        this.context = context;
        this.layout = layout;
        this.noteList = noteList;
    }

    @Override
    public int getCount() {
        return this.noteList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.noteList.get(position);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if(convertView == null){
            LayoutInflater layoutInflater = LayoutInflater.from(this.context);
            convertView = layoutInflater.inflate(layout, null);

            holder = new ViewHolder();
            holder.textViewTitle = convertView.findViewById(R.id.textViewTitle);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        Note currentNote = noteList.get(position);
        holder.textViewTitle.setText(currentNote.getTitle());

        return convertView;
    }
    static class ViewHolder{
        private TextView textViewTitle;
    }

}
