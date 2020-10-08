package com.uj.study.inlineSingleton;

import java.io.BufferedReader;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/9 上午6:40
 * @description：
 * @modified By：
 * @version:
 */
public class Blackjack {
    private Player player;
    private Dealer dealer;
    private BufferedReader input;

    public Blackjack(int[] deck) {
        player = new Player();
        dealer = new Dealer();
    }

    public void play() {
        deal();
        writeln(player.getHandAsString());
        writeln(dealer.getHandAsStringWithFirstCardDown());
        HitStayResponse hitStayResponse;
        do {
            write("H)it or S)tay: ");
            hitStayResponse = obtainHitStayResponse(input);
            write(hitStayResponse.toString());
            if (hitStayResponse.shouldHit()) {
                dealCardTo(player);
                writeln(player.getHandAsString());
            }
        }
        while (canPlayerHit(hitStayResponse));
        // ...
    }

    public
    HitStayResponse obtainHitStayResponse(BufferedReader input) {

        return Console.obtainHitStayResponse(input);

    }

    public
    void setPlayerResponse(HitStayResponse newHitStayResponse) {

        Console.setPlayerResponse(newHitStayResponse);

    }

    private boolean canPlayerHit(HitStayResponse hitStayResponse) {
        return false;
    }

    private void dealCardTo(Player player) {

    }

    private void write(String s) {

    }

    private void writeln(String handAsString) {

    }

    private void deal() {

    }

    public boolean didDealerWin() {
        return false;
    }

    public boolean didPlayerWin() {
        return false;
    }

    public int getDealerTotal() {
        return 0;
    }

    public int getPlayerTotal() {
        return 0;
    }
}
