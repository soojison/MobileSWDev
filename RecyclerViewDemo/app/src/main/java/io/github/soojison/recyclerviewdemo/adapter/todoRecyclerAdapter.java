package io.github.soojison.recyclerviewdemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.github.soojison.recyclerviewdemo.R;
import io.github.soojison.recyclerviewdemo.data.Todo;
import io.github.soojison.recyclerviewdemo.touch.TodoTouchHelperAdapter;
import io.realm.Realm;

/**
 * The most important class in our code.
 * Please get familiar with this stuff!
 */

public class TodoRecyclerAdapter
        extends RecyclerView.Adapter<TodoRecyclerAdapter.ViewHolder>
        implements TodoTouchHelperAdapter {
    // Adapter template of the type TodoRecyclerAdapter.ViewHolder

    // You have to make sure you initialize the list! don't let it be null
    private List<Todo> todoList;
    private Context context; // kind of the activity reference

    // another field for realm db
    private Realm realmTodo;

    public TodoRecyclerAdapter(Context context) {
        this.context = context;
        todoList = new ArrayList<Todo>();

        // init realm
        realmTodo = Realm.getDefaultInstance();
    }

    @Override
    // this ViewHolder is the internal ViewHolder class we created
    // this is why we had to fill in the <> with RecyclerView.ViewHolder
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // you could use my context too, it's the same in this situation
        // we inflated the view to pass into the constructor
        View rowView = LayoutInflater.from(parent.getContext()).inflate(
                // cmd + p shows you the parameters
                R.layout.todo_row,
                parent, // tells you where to insert the thing
                false
        );
        return new ViewHolder(rowView);
    }

    @Override
    // called for each line in the recyclerView
    // won't call 200 times in the beginning --> lazy load as you scroll
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.tvTodo.setText(todoList.get(position).getTodoText());
        holder.cbDone.setChecked(todoList.get(position).isDone());

        holder.cbDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                todoList.get(holder.getAdapterPosition()).setDone(holder.cbDone.isChecked());
            }
        });
    }


    @Override
    // don't forget to override this method or else the view won't show anything
    public int getItemCount() {
        return todoList.size();
    }

    @Override
    public void onItemDismiss(int position) {
        todoList.remove(position);
        // we have to tell the recyclerview that data set has changed
        // notifyDataSetChanged(); refreshes the whole list, inefficient
        // so use this, which is more efficient, just refreshes that position
        notifyItemRemoved(position);
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        // this won't let you swap and scroll
        todoList.add(toPosition, todoList.get(fromPosition));
        todoList.remove(fromPosition);

        notifyItemMoved(fromPosition, toPosition);
    }

    public void addTodo(Todo todo) {
        todoList.add(0, todo);
        notifyItemInserted(0);

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CheckBox cbDone;
        private TextView tvTodo;

        // itemView is the individual to-do item
        public ViewHolder(View itemView) {
            super(itemView);
            // you have to import the R class because it's in a separate package
            cbDone = (CheckBox) itemView.findViewById(R.id.cbDone);
            tvTodo = (TextView) itemView.findViewById(R.id.tvTodo);
        }
    }

}
