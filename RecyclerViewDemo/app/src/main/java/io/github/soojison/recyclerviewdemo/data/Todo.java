package io.github.soojison.recyclerviewdemo.data;

import io.realm.RealmObject;

/**
 * Contains info about the to-do item
 */

// system will know to create db from this class
public class Todo extends RealmObject {

    private String todoText;
    private boolean done;

    public Todo(String todoText, boolean done) {
        this.todoText = todoText;
        this.done = done;
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
