package example;

import iBoxDB.LocalServer.*;
import static iBoxDB.LocalServer.Ason.*;
import static iBoxDB.LocalServer.IFunction.*;
import static example.AsonExample.Names.*;
import java.util.Map;

public class AsonExample {

    public static void main(String[] mainargs) {
        DB.root("../");
        //BoxSystem.DBDebug.DeleteDBFiles(1);
        var db = new DB(1);

        db.getConfig()
                .ensureTable(new Ason(Id, 0L), Table1);

        db.getConfig()
                .ensureTable(new Ason(Id, 0L), Table2)
                .ensureIndex(new Ason(JoinTable1, 0L), Table2);

        db.getConfig()
                .ensureTable(new Ason(Id, 0L), Table3)
                .ensureIndex(new Ason(JoinTable2, 0L), Table3);

        var auto = db.open();

        long id1start;

        try (var box = auto.cube()) {
            id1start = box.newId(1, 0);
            for (int t1 = 0; t1 < 100; t1++) {
                long id1 = box.newId(1, 1);
                var obj1 = new Ason(Id, id1, Value1, "T1-" + id1);
                box.d(Table1).insert(obj1);

                for (int t2 = 0; t2 < 3; t2++) {
                    long id2 = box.newId(2, 1);
                    var obj2 = new Ason(Id, id2, JoinTable1, id1, Value2, "T2-" + id2);
                    box.d(Table2).insert(obj2);

                    for (int t3 = 0; t3 < 2; t3++) {
                        long id3 = box.newId(3, 1);
                        var obj3 = new Ason(Id, id3, JoinTable2, id2, Value3, "T3-" + id3);
                        box.d(Table3).insert(obj3);

                    }

                }
            }
            CommitResult cr = box.commit();
        }

        var joinList = new JoinList();
        try (var box = auto.cube()) {
            box.selectCount("*from Table1 where Id >= ? & Id <=? & [*]", id1start, id1start + 5,
                    func((_a, args1) -> {
                        var map1 = (Map<String, Object>) args1[0];
                        var id1 = map1.get(Id);

                        box.selectCount("*from Table2 where JoinTable1 == ? & [*]", id1,
                                func((_b, args2) -> {
                                    var map2 = (Map<String, Object>) args2[0];
                                    var id2 = map2.get(Id);

                                    box.selectCount("*from Table3 where JoinTable2 == ? & [*]", id2,
                                            func((_c, args3) -> {
                                                var map3 = (Map<String, Object>) args3[0];
                                                joinList.newRow(map1).join(map2).join(map3);
                                                return true;
                                            }));

                                    return true;
                                }));

                        return true;
                    })
            );
        }

        for (var row : joinList) {
            System.out.println(row.toString());
        }
        auto.getDatabase().close();
    }

    public static class Names {

        public static String Id = "Id";

        public static String JoinTable1 = "JoinTable1";
        public static String JoinTable2 = "JoinTable2";

        public static String Table1 = "Table1";
        public static String Table2 = "Table2";
        public static String Table3 = "Table3";

        public static String Value1 = "Value1";
        public static String Value2 = "Value2";
        public static String Value3 = "Value3";
    }
}
