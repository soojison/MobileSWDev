package io.github.soojison.recyclerviewdemo.data;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Contains info about the to-do item
 */

// system will know to create db from this class
public class Todo extends RealmObject {

    public static String COL_TODO_ID = "todoID";
    // this will help realm that this is the primary key in the table
    // the key should be unique
    // GUID = globally unique identifier
    // in Java, UUID = universally unique identifier
    @PrimaryKey
    private String todoID;
    private String todoText;
    private boolean done;

    public Todo(String todoText, boolean done) {
        this.todoText = todoText;
        this.done = done;
    }

    public Todo() {
        // technical thing ... that's good to have....
    }

    public String getTodoID() {
        return todoID;
    }

    public String getTodoText() {
        return todoText;
    }

    public void setTodoText(String todoText) {
        this.todoText = todoText;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

}
