package com.example.roomdatabasetutorial;

import android.app.Dialog;
import android.app.WallpaperInfo;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DatabaseAdapter extends RecyclerView.Adapter<DatabaseAdapter.ViewHolder> {

    private List<MainData> dataList;
    private Context context;
    private RoomDB roomDatabase;

    public DatabaseAdapter(List<MainData> dataList, Context context, RoomDB roomDatabase) {
        this.dataList = dataList;
        this.context = context;
        this.roomDatabase = roomDatabase;
    }

    @NonNull
    @Override
    public DatabaseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_row_main, parent, false);

        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final DatabaseAdapter.ViewHolder holder, int position) {
        final MainData data = dataList.get(position);

        roomDatabase = RoomDB.getInstance(context);

        holder.textView.setText(data.getText());

        holder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainData mainData = dataList.get(holder.getAdapterPosition());

                final int sID = mainData.getID();
                String sText = mainData.getText();

                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_update);

                int width = WindowManager.LayoutParams.MATCH_PARENT;
                int height = WindowManager.LayoutParams.WRAP_CONTENT;

                dialog.getWindow().setLayout(width, height);
                dialog.show();

                final EditText editText = dialog.findViewById(R.id.edit_text);
                Button button = dialog.findViewById(R.id.button_update);

                editText.setText(sText);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                        //get updated string
                        String uText = editText.getText().toString().trim();

                        //update string in database
                        roomDatabase.MainDao().update(sID, uText);

                        //notify when data is updated
                        dataList.clear();
                        dataList.addAll(roomDatabase.MainDao().getAll());
                        notifyDataSetChanged();

                        Toast.makeText(context, "Data Updated Successfully!!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get maindata object
                MainData mainData = dataList.get(holder.getAdapterPosition());

                //remove maindata from database
                roomDatabase.MainDao().delete(mainData);

                //notify when data is deleted
                int position = holder.getAdapterPosition();
                dataList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, dataList.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;
        private ImageView btn_edit, btn_delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.text_view);
            btn_edit = itemView.findViewById(R.id.btn_edit);
            btn_delete = itemView.findViewById(R.id.btn_delete);
        }
    }
}
