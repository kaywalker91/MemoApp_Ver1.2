package com.kaywalker.memoapp_proto;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder>
{
    private ArrayList<Todoitem> mTodoitems;
    private Context mcontext;
    private DBHelper mDBHelper;

    public CustomAdapter(ArrayList<Todoitem> mTodoitems, Context mcontext) {
        this.mTodoitems = mTodoitems;
        this.mcontext = mcontext;
        mDBHelper = new DBHelper(mcontext);
    }

    @NonNull
    @Override
    public CustomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View holder = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new ViewHolder(holder);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.ViewHolder holder, int position)
    {
        holder.tv_title.setText(mTodoitems.get(position).getTitle());
        holder.tv_content.setText(mTodoitems.get(position).getContent());
        holder.tv_writeDate.setText(mTodoitems.get(position).getWriteDate());

    }

    @Override
    public int getItemCount()
    {
        return mTodoitems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView tv_title;
        private TextView tv_content;
        private TextView tv_writeDate;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            tv_title = itemView.findViewById(R.id.tv_title);
            tv_content = itemView.findViewById(R.id.tv_content);
            tv_writeDate = itemView.findViewById(R.id.tv_writeDate);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    int curPos = getAdapterPosition(); //현재 리스트 아이템 위치
                    Todoitem todoitem = mTodoitems.get(curPos);

                    String[] strChoiceItems = {"수정하기", "삭제하기"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
                    builder.setTitle("원하는 작업을 선택해주세요");
                    builder.setItems(strChoiceItems, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int position)
                        {
                            if(position == 0)//수정
                            {
                                //팝업창 띄우기
                                Dialog dialog = new Dialog(mcontext, android.R.style.Theme_Material_Light_Dialog);
                                dialog.setContentView(R.layout.dialog_edit);
                                EditText et_title = dialog.findViewById(R.id.et_title);
                                EditText et_content = dialog.findViewById(R.id.et_content);
                                Button btn_ok = dialog.findViewById(R.id.btn_ok);
                                btn_ok.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view)
                                    {
                                        //Update Database
                                        String title = et_title.getText().toString();
                                        String content = et_content.getText().toString();
                                        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                                        String beforeTime = todoitem.getWriteDate();
                                        mDBHelper.UpdateTodo(title, content, currentTime, beforeTime);

                                        //Update UI
                                        todoitem.setTitle(title);
                                        todoitem.setContent(content);
                                        todoitem.setWriteDate(currentTime);
                                        notifyItemChanged(curPos, todoitem);
                                        dialog.dismiss();
                                        Toast.makeText(mcontext, "메모가 수정되었습니다!", Toast.LENGTH_SHORT).show();
                                    }
                                });

                                dialog.show();

                            }
                            else if(position == 1)//삭제
                            {
                                String beforeTime = todoitem.getWriteDate();
                                mDBHelper.DeleteTodo(beforeTime);

                                mTodoitems.remove(curPos);
                                notifyItemRemoved(curPos);
                                Toast.makeText(mcontext, "메모가 삭제되었습니다!", Toast.LENGTH_SHORT).show();

                            }

                        }
                   });

                    builder.show();
                }
            });

        }
    }

    public void addItem(Todoitem _item){
        mTodoitems.add(0, _item);
        notifyItemInserted(0);
    }
}
