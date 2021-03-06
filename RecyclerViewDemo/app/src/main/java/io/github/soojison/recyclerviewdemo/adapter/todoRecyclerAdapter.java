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
import java.util.UUID;

import io.github.soojison.recyclerviewdemo.MainActivity;
import io.github.soojison.recyclerviewdemo.R;
import io.github.soojison.recyclerviewdemo.data.Todo;
import io.github.soojison.recyclerviewdemo.touch.TodoTouchHelperAdapter;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

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

    public TodoRecyclerAdapter(Context context, Realm realm) {
        this.context = context;

        // init realm
        realmTodo = realm;

        // get all the data from the database
        RealmResults<Todo> todoResult = realmTodo.where(Todo.class).findAll()
                .sort("todoText", Sort.ASCENDING);

        todoList = new ArrayList<Todo>();

        // load objects from the fetched data
        for (int i = 0; i < todoResult.size(); i++) {
            todoList.add(todoResult.get(i));
        }
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
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.tvTodo.setText(todoList.get(position).getTodoText());
        holder.cbDone.setChecked(todoList.get(position).isDone());

        holder.cbDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realmTodo.beginTransaction();
                todoList.get(holder.getAdapterPosition()).setDone(holder.cbDone.isChecked());
                realmTodo.commitTransaction();
            }
        });

        // whole view of the item = card view in our case
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // you can't open the activity here,
                // you have to ask the mainactivity to open a new activity
                // position param = visual position,
                // relative position in the recyclerview which doesn't show all the items
                // getAdapterPosition = global position
                ((MainActivity) context).showEdit(holder.getAdapterPosition(),
                        todoList.get(holder.getAdapterPosition()).getTodoID());
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

        // remove it from the database before removing it from the list
        realmTodo.beginTransaction();
        todoList.get(position).deleteFromRealm();
        realmTodo.commitTransaction();

        // delete from the list
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

    public void addTodo(String todoTitle) {
        // save to realm before adding to db
        //start transaction
        realmTodo.beginTransaction();
        // create object from class
        Todo newTodo = realmTodo.createObject(Todo.class, UUID.randomUUID().toString());
        // set fields
        newTodo.setTodoText(todoTitle);
        newTodo.setDone(false);
        // close transaction
        realmTodo.commitTransaction();

        // add object
        todoList.add(0, newTodo);
        notifyItemInserted(0);

    }

    public void updateTodo(String todoID, int positionToEdit) {
        Todo todo = realmTodo.where(Todo.class)
                .equalTo(Todo.COL_TODO_ID, todoID)
                .findFirst();
        todoList.set(positionToEdit, todo);
        notifyItemChanged(positionToEdit);
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
