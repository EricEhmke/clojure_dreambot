(ns dreambot-test.fishnode
  (:require [dreambot-test.utils.utilities :as utils]))
;; Is there a way to pass a reference to the Client, Area etc into this and have it run? These functions are not pure
(import
 [org.dreambot.api.methods.container.impl Inventory]
 [org.dreambot.api.methods.map Area]
 [org.dreambot.api Client]
 [org.dreambot.api.input Mouse]
 [org.dreambot.api.methods.interactive NPCs]
 [org.dreambot.api.methods MethodProvider]
 [org.dreambot.api.utilities.impl Condition]
 [org.dreambot.api.methods.dialogues Dialogues])

(defn isAtFishingZone
  "Checks whether the player is in the fishing zone"
  [fishingZone]
  (let [playerInZone (.contains fishingZone (Client/getLocalPlayer))]
    (if (playerInZone)
      (playerInZone)
      (utils/travelTo fishingZone))))
;; (new Area 2833 3436 2861 3421) CATHERBY

(defn isAtDestination
  "Checks whether the player is in the fishing zone"
  [destination]
  (.contains destination (Client/getLocalPlayer)))

(defn travelFallback
  [destination]
  (or (isAtDestination destination) (utils/walkNext destination)))

(defn hasInventorySpace
  "Makes sure there is space in the player's inventory"
  []
  (not (Inventory/isFull)))

(defn hasRequiredTools
  "Checks whether that player has the required tool"
  [tools]
  (Inventory/contains tools)) ;; TODO Check all tools

(defn isFishing
  "Returns a Condition which checks if the player is animating"
  [fishingSpot]
  (reify Condition
    (verify [_]
      "Evaluates cond and returns a bool"
      (if (< 40 (rand-int 100)) ;; 40% of the time we notice if the fishing spot has moved.
        (.isAnimating (Client/getLocalPlayer))
        (.exists fishingSpot)))))

(defn isTraveling
  "Returns a Condition which checks if the player is traveling"
  []
  (reify Condition
    (verify [this]
      "Evaluates cond and returns a bool"
      (.isMoving (Client/getLocalPlayer)))))

(defn lobsterFilter
  "Filters lobster fishing spots"
  []
  (proxy [org.dreambot.api.methods.filter.Filter] []
    (match [npc]
      (if (nil? npc)
        false
        (.hasAction npc (into-array ["Cage"]))))))

(defn goFishing
  "Fishs"
  []
  (let [fishingSpot  (NPCs/closest (lobsterFilter))
        isFishing (isFishing fishingSpot)]
    (MethodProvider/log (str "Fishing spot located..."))
    ;; TODO: Handle cases where there are no suitable fishing spots
    (if (and (not (nil? fishingSpot)) (.interact fishingSpot "Cage"))
      (do
        (MethodProvider/log "Interacted with fishing spot...")
        (when (> 70 (rand-int 100)) (utils/moveMouseOutOfScreen))
        (MethodProvider/sleep 1000) ;; Allows time for the client to register a character as moving
        (MethodProvider/sleepWhile (isTraveling) utils/timeOutTime (utils/pollingTime))
        (while (.verify isFishing)
          (when (> (Client/getIdleTime) (utils/pollingTime 300000 200000))
            (utils/antiLogout))
          (MethodProvider/sleep (utils/pollingTime 16000 5000))))

      (MethodProvider/log "Found a fishing spot but failed to interact with it.")))

  (when (and (Dialogues/inDialogue) (Dialogues/canContinue))
    (Dialogues/continueDialogue)
    (MethodProvider/sleep (utils/pollingTime)))

  (MethodProvider/log "Completed one fishing loop..."))

(defn fishSequence
  [tools fishingZone]
  (and (hasRequiredTools tools) (hasInventorySpace) (travelFallback fishingZone) (goFishing)))

;; (defn FishNode
;;   []
;;   (proxy [org.dreambot.api.script.TaskNode] []
;;     (priority [] (int 1))
;;     ;; Will return 'true' when these conditions are met
;;     (accept [] (MethodProvider/log "Evaluating whether to fish...")  (and (hasInventorySpace) (hasRequiredTool)))
;;     ;; What will execute when this node runs.
;;     (execute [] (goFishing (new Area 2833 3436 2861 3421)) (utils/pollingTime))))
