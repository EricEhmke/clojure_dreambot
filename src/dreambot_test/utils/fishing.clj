(ns dreambot-test.utils.fishing
  (:require [dreambot-test.utils.walking :as walk]
            [dreambot-test.utils.antiban :as antiban]
            [dreambot-test.utils.utilities :as utils]))

(import
 [org.dreambot.api Client]
 [org.dreambot.api.methods.interactive NPCs]
 [org.dreambot.api.methods MethodProvider]
 [org.dreambot.api.utilities.impl Condition])

(def fishTypeInteract
  "Maps the fish type to its correct right-click action.
  Used to identify if you can fish the desired fish at a spot"
  {:Lobster "Cage"})

(defn isFishing
  "Returns a Condition which checks if the player is animating"
  [fishingSpot]
  (reify Condition
    (verify [_]
      "Evaluates cond and returns a bool"
      (if (< 40 (rand-int 100)) ;; 40% of the time we notice if the fishing spot has moved.
        (.isAnimating (Client/getLocalPlayer))
        (.exists fishingSpot)))))

(defn lobsterFilter
  "Filters lobster fishing spots"
  []
  (proxy [org.dreambot.api.methods.filter.Filter] []
    (match [npc]
      (if (nil? npc)
        false
        (.hasAction npc (into-array ["Cage"]))))))

(def fishingSpotFilterMap
  "Maps a fish type (key) to the correct fishing spot filter (value)"
  {:Lobster (lobsterFilter)})

(defn goFishing
  "Fishs"
  [fishType]
  (let [fishingSpot  (NPCs/closest (fishingSpotFilterMap (keyword fishType)))
        isFishing (isFishing fishingSpot)]
    (if (and (not (nil? fishingSpot)) (.interact fishingSpot (fishTypeInteract (keyword fishType))))
      (do
        (MethodProvider/log "Interacted with fishing spot...")
        (when (> 70 (rand-int 100)) (antiban/moveMouseOutOfScreen))
        (MethodProvider/sleep 1000) ;; Allows time for the client to register a character as moving
        (MethodProvider/sleepWhile (walk/isTraveling) utils/timeOutTime (antiban/pollingTime))
        (while (.verify isFishing)
          (when (> (Client/getIdleTime) (antiban/pollingTime 300000 200000))
            (antiban/antiLogout))
          (MethodProvider/sleep (antiban/pollingTime 16000 5000)))
        true)

      (do
        (MethodProvider/log "Could not find a suitable fishing spot in area...")
        false)))

  (utils/clearDialogue) ;; Clear level up or inventory full dialogues

  (MethodProvider/log "Completed one fishing loop..."))
