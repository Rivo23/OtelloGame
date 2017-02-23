package myapp.esps.uam.es.robpizarro.models;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by e268930 on 21/02/17.
 */

public class RoundRepository {
    public static final int SIZE = 4;
    private static RoundRepository repository;
    private ArrayList<Round> rounds;

    public static RoundRepository get(Context context) {
        if (repository == null) {
            repository = new RoundRepository(context);
        }
        return repository;
    }

    public void addRound(Round round) { rounds.add(round); }

    private RoundRepository(Context context) {
        rounds = new ArrayList<Round>();
        for (int i = 0; i < 50; i++) {
            Round round = new Round(SIZE);
            rounds.add(round);
        }
    }
    public ArrayList<Round> getRounds() {
        return rounds;
    }
    public Round getRound(String id) {
        for (Round round : rounds) {
            if (round.getId().equals(id))
                return round;
        }
        return null;
    }
}
