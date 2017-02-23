package myapp.esps.uam.es.robpizarro.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import myapp.esps.uam.es.robpizarro.R;
import myapp.esps.uam.es.robpizarro.models.Round;

/**
 * Created by e268930 on 21/02/17.
 */

public class RoundAdapter extends RecyclerView.Adapter<RoundAdapter.RoundHolder> {
    private ArrayList<Round> rounds;

    public class RoundHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView idTextView;
        private TextView boardTextView;
        private TextView dateTextView;
        private Round round;

        public RoundHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            idTextView = (TextView) itemView.findViewById(R.id.list_item_id);
            boardTextView = (TextView) itemView.findViewById(R.id.list_item_board);
            dateTextView = (TextView) itemView.findViewById(R.id.list_item_date);
        }
        public void bindRound(Round round){
            this.round = round;
            idTextView.setText(round.getTitle());
            boardTextView.setText(round.getBoard().toString());
            dateTextView.setText(String.valueOf(round.getDate()).substring(0,19));
        }

        @Override
        public void onClick(View view) {
            Context context = view.getContext();
            Intent intent = MainActivity.newIntent(context, round.getId());
            context.startActivity(intent);
        }
    }

    /*TODO generar la interfaz con Java.*/

    public RoundAdapter(ArrayList<Round> rounds){
        this.rounds = rounds;
    }

    @Override
    public RoundAdapter.RoundHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_item_round, parent, false);
        return new RoundHolder(view);
    }

    @Override
    public void onBindViewHolder(RoundAdapter.RoundHolder holder, int position) {
        Round round = rounds.get(position);
        holder.bindRound(round);
    }

    @Override
    public int getItemCount() {
        return rounds.size();
    }
}
