package org.zella.game.fromandroid;

import com.google.gson.JsonObject;

/**
 * Created by dru on 13.01.16.
 */
public class Winner  {

   private final String playerId;

   private final int plusScore;

   private Winner(String playerId, int plusScore) {
      this.playerId = playerId;
      this.plusScore = plusScore;
   }

   public String getPlayerId() {
      return playerId;
   }

   public int getPlusScore() {
      return plusScore;
   }

   public static Winner fromJson(JsonObject json) {
      return new Winner(
           json.get("playerId").getAsString(),
           json.get("plusScore").getAsInt()
      );
   }

   @Override
   public String toString() {
      return "Winner{" +
           "playerId='" + playerId + '\'' +
           ", plusScore=" + plusScore +
           '}';
   }


}
