(ns dreambot-test.utils.fishing
  (:require [dreambot-test.utils.walking :as walk]
            [dreambot-test.utils.antiban :as antiban]
            [dreambot-test.utils.utilities :as utils]))

(import
 [org.dreambot.api Client]
 [org.dreambot.api.methods.interactive NPCs]
 [org.dreambot.api.methods MethodProvider]
 [org.dreambot.api.utilities.impl Condition])

(defn isFishing
  "Returns a Condition which checks if the player is animating"
  [fishingSpot]
  (reify Condition
    (verify [_]
      "Evaluates cond and returns a bool"
      (if (< 40 (rand-int 100)) ;; 40% of the time we notice if the fishing spot has moved.
        (.isAnimating (Client/getLocalPlayer))
        (.exists fishingSpot)))))

(def fishTypeInteract
  {:Lobster "Cage"})

(defn lobsterFilter
  "Filters lobster fishing spots"
  []
  (proxy [org.dreambot.api.methods.filter.Filter] []
    (match [npc]
      (if (nil? npc)
        false
        (.hasAction npc (into-array ["Cage"]))))))

(def fishingSpotFilterMap
  {:Lobster lobsterFilter})

(defn goFishing
  "Fishs"
  [fishType]
  (let [fishingSpot  (NPCs/closest (fishingSpotFilterMap fishType))
        isFishing (isFishing fishingSpot)]
    (MethodProvider/log (str "Fishing spot located..."))
    ;; TODO: Handle cases where there are no suitable fishing spots
    (if (and (not (nil? fishingSpot)) (.interact fishingSpot (fishTypeInteract fishType)))
      (do
        (MethodProvider/log "Interacted with fishing spot...")
        (when (> 70 (rand-int 100)) (antiban/moveMouseOutOfScreen))
        (MethodProvider/sleep 1000) ;; Allows time for the client to register a character as moving
        (MethodProvider/sleepWhile (walk/isTraveling) utils/timeOutTime (antiban/pollingTime))
        (while (.verify isFishing)
          (when (> (Client/getIdleTime) (antiban/pollingTime 300000 200000))
            (antiban/antiLogout))
          (MethodProvider/sleep (antiban/pollingTime 16000 5000))))

      (MethodProvider/log "Could not find a suitable fishing spot...")))

  (utils/clearDialogue) ;; Clear level up or inventory full dialogues

  (MethodProvider/log "Completed one fishing loop..."))
