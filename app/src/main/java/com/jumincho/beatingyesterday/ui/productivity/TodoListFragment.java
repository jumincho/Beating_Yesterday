package com.jumincho.beatingyesterday.ui.productivity;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.jumincho.beatingyesterday.R;
import com.jumincho.beatingyesterday.data.Note;
import com.jumincho.beatingyesterday.data.NoteAdapter;
import com.jumincho.beatingyesterday.data.NoteDatabase;

import java.util.ArrayList;

public class TodoListFragment extends Fragment {
    private static final String TAG = "TodoListFragment";

    RecyclerView recyclerView;
    NoteAdapter adapter;

    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_todo_list, container, false);

        initUI(rootView);

        loadNoteListData();

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadNoteListData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return rootView;
    }

    private void initUI(ViewGroup rootView) {
        recyclerView = rootView.findViewById(R.id.recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new NoteAdapter(getContext().getApplicationContext());
        recyclerView.setAdapter(adapter);
    }

    public int loadNoteListData() {
        String loadSql = "select _id, TODO from " + NoteDatabase.TABLE_NOTE + " order by _id desc";

        int recordCount = -1;
        Context ctx = getContext();
        if (ctx == null) {
            return recordCount;
        }
        NoteDatabase database = NoteDatabase.getInstance(ctx.getApplicationContext());

        if (database != null) {
            // Ensure the underlying SQLiteDatabase is open before querying.
            try {
                database.open();
            } catch (Exception ignored) {
            }
            Cursor outCursor = database.rawQuery(loadSql);
            if (outCursor == null) {
                return recordCount;
            }

            recordCount = outCursor.getCount();

            ArrayList<Note> items = new ArrayList<>();

            for (int i = 0; i < recordCount; i++) {
                outCursor.moveToNext();

                int _id = outCursor.getInt(0);
                String todo = outCursor.getString(1);
                items.add(new Note(_id, todo));
            }
            outCursor.close();

            adapter.setItems(items);
            adapter.notifyDataSetChanged();
        }

        return recordCount;
    }
}
