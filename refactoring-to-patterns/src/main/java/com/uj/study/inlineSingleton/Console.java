package com.uj.study.inlineSingleton;

import java.io.BufferedReader;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/9 上午6:31
 * @description：
 * @modified By：
 * @version:
 */
public class Console {
    static private HitStayResponse hitStayResponse =
            new HitStayResponse();

    private Console() {
        super();
    }

    public static HitStayResponse obtainHitStayResponse(BufferedReader input) {
        hitStayResponse.readFrom(input);
        return hitStayResponse;
    }

    public static void setPlayerResponse(HitStayResponse newHitStayResponse) {
        hitStayResponse = newHitStayResponse;
    }
}
