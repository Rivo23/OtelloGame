package myapp.esps.uam.es.robpizarro.models;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by e268930 on 21/02/17.
 */

public interface RoundRepository {
    void open() throws Exception;
    void close();
    interface LoginRegisterCallback {
        void onLogin(String playerUuid);
        void onError(String error);
    }
    void login(String playername, String password, LoginRegisterCallback callback);
    void register(String playername, String password, LoginRegisterCallback callback);
    interface BooleanCallback {
        void onResponse(boolean ok);
    }
    void getRounds(String playeruuid, String orderByField, String group, RoundsCallback callback);
    void addRound(Round round, BooleanCallback callback);
    void updateRound(Round round, BooleanCallback callback);
    interface RoundsCallback {
        void onResponse(List<Round> rounds);
        void onError(String error);
    }

    void updateStats(String result[], String playeruuid, BooleanCallback callback);
    String[] getStats(String playeruuid);
}
