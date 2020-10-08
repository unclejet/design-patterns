package com.uj.study.inlineSingleton;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author ：unclejet
 * @date ：Created in 2020/10/9 上午6:40
 * @description：
 * @modified By：
 * @version:
 */
class ScenarioTest {
    @Test
    public void testDealerStandsWhenPlayerBusts() {
        int[] deck = { 10, 9, 7, 2, 6 };
        Blackjack blackjack = new Blackjack(deck);
        blackjack.setPlayerResponse(new TestAlwaysHitResponse());
        blackjack.play();
//        assertTrue(blackjack.didDealerWin(), "dealer wins");
//        assertTrue(!blackjack.didPlayerWin(), "player loses");
//        assertEquals(11, blackjack.getDealerTotal(), "dealer total");
//        assertEquals(23, blackjack.getPlayerTotal(), "player total");
    }
}