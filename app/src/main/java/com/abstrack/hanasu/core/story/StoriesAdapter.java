package com.abstrack.hanasu.core.story;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.abstrack.hanasu.R;
import java.util.List;

public class StoriesAdapter extends RecyclerView.Adapter<StoriesAdapter.StoriesViewHolder> {

    // Store the stories
    private final List<Story> stories;
    private final Context context;
    private final RecyclerView storiesBar;

    public StoriesAdapter(List<Story> stories, RecyclerView storiesBar, Context context) {
        this.stories = stories;
        this.context = context;
        this.storiesBar = storiesBar;
    }


    @NonNull
    @Override
    public StoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.story_item, parent, false);

        return new StoriesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoriesViewHolder holder, int position) {
        holder.story.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (stories.get(holder.getAdapterPosition()).isSeen()) {
                    // Just launches the story
                    return;
                }
                // Remove the seen icon, TODO: check the amount of stories
                holder.story.findViewById(R.id.seenIcon).setVisibility(View.INVISIBLE);
                stories.get(holder.getAdapterPosition()).setSeen(true);
                moveStoryToLastPosition(stories.get(holder.getAdapterPosition()));
            }
        });
    }
    @Override
    public int getItemCount() {
        return stories.size();
    }

    public static class StoriesViewHolder extends RecyclerView.ViewHolder {
        private final LinearLayout story;

        public StoriesViewHolder(@NonNull View itemView) {
            super(itemView);
            story = itemView.findViewById(R.id.story);
        }
    }

    public void moveStoryToLastPosition(Story story) {
        // Sort the stories
        int lastIndexOfStory = stories.lastIndexOf(story);
        stories.remove(story);
        stories.add(story);
        notifyItemMoved(lastIndexOfStory, stories.indexOf(story));
        // Nullify the scroll animation
        storiesBar.scrollToPosition(lastIndexOfStory);
    }
}
