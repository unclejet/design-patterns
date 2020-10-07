package com.uj.study.extractAdapter;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/7 上午7:42
 * @description：
 * @modified By：
 * @version:
 */
public class Client {
    private Query query;

    public void loginToDatabase(String db, String user, String password) {
        try   {
            if (usingSDVersion52()) {
                query = new QuerySD52(getSD52ConfigFileName());
            } else {
                query = new QuerySD51();
            }
            query.login(db, user, password); // Login to SD 5.1
//      ...
        } catch(QueryException qe) {

        }
    }

    private String getSD52ConfigFileName() {
        return null;
    }

    private boolean usingSDVersion52() {
        return false;
    }

}
