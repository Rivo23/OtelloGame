package myapp.esps.uam.es.robpizarro.database;

/**
 * Created by localuser01 on 31/03/17.
 */

public class RoundDataBaseSchema {
    public static final class UserTable {
        public static final String NAME = "users";
        public static final class Cols {
            public static final String PLAYERUUID = "playeruuid1";
            public static final String PLAYERNAME = "playername";
            public static final String PLAYERPASSWORD = "playerpassword";
        }
    }
    public static final class RoundTable {
        public static final String NAME = "rounds";
        public static final class Cols {
            public static final String PLAYERUUID = "playeruuid2";
            public static final String ROUNDUUID = "rounduuid";
            public static final String DATE = "date";
            public static final String TITLE = "title";
            public static final String SIZE = "size";
            public static final String BOARD = "board";
        }
    }
    public static final class StatsTable{
        public static final String NAME = "stats";
        public static final class Cols{
            public static final String PLAYERUUID = "playeruuid3";
            public static final String WINS = "wins";
            public static final String DRAWS = "draws";
            public static final String LOST = "losts";
        }
    }
}
