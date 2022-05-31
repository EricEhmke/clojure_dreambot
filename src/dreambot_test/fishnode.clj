(ns dreambot-test.fishnode
  (:use clojure.core.matrix.random))
;; Is there a way to pass a reference to the Client, Area etc into this and have it run? These functions are not pure
(import
 [org.dreambot.api.methods.container.impl Inventory]
 [org.dreambot.api.methods.map Area]
 [org.dreambot.api Client]
 [org.dreambot.api.methods.interactive NPCs]
 [org.dreambot.api.methods MethodProvider]
 [org.dreambot.api.utilities.impl Condition])

(defn isAtFishingZone
  "Checks whether the player is in the fishing zone"
  []
  (.contains (new Area 2833 3436 2861 3421) (Client/getLocalPlayer)))

(defn hasInventorySpace
  "Makes sure there is space in the player's inventory"
  []
  (not (Inventory/isFull)))

(defn hasRequiredTool
  "Checks whether that player has the required tool"
  []
  (Inventory/contains "Lobster pot"))

(defn isAnimating
  "Returns a Condition which checks if the player is animating"
  []
  (reify Condition
    (verify [this]
      "Evaluates cond and returns a bool"
      (.isAnimating (Client/getLocalPlayer)))))

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

(def timeOutTime (int 330000)) ;; 5.5 minutes

(def pollingTime
  "Generates a normally distrubted polling time along a guasian distribution"
  (fn [] (let [pollTime (rand-gaussian 20000 10000)]
           (if (< 200 pollTime)
             pollTime
             (recur)))))

;; (def pollingTime (getPollingTime))

(defn goFishing
  "Fishs"
  []
  (let [fishingSpot  (NPCs/closest (lobsterFilter))]
    (MethodProvider/log (str "Fishing spot located..."))
    (if (.interact fishingSpot "Cage")
      (do
        (MethodProvider/log "Succesfully interacted with fishing spot...")
        (MethodProvider/sleep 1000) ;; Allows time for the client to register a character as moving
        (MethodProvider/log (str "Are we traveling: " (.isMoving (Client/getLocalPlayer))))
        ;; I think I want to avoid sleepUntil because this will sleep forever in the event of a misclick.
        (MethodProvider/sleepWhile (isTraveling) (isTraveling) timeOutTime (int (pollingTime)))
        (MethodProvider/log (str "Are we animating: " (.isAnimating (Client/getLocalPlayer))))
        ;;TODO: Some anti- logout stuff here
        ;;TODO: Explore checking if the fishing spot exists instead of animating. or both?
        (MethodProvider/sleepWhile (isAnimating) (isAnimating) timeOutTime (int (pollingTime))))

      (MethodProvider/log "Found a fishing spot but failed to interact with it.")))
  (MethodProvider/log "One loop completed sleeping for a bit")
  (MethodProvider/sleep (int (pollingTime))))

(defn FishNode
  []
  (proxy [org.dreambot.api.script.TaskNode] []
    (priority [] (int 1))
    ;; Will return 'true' when these conditions are met
    (accept [] (MethodProvider/log "Evaluating whether to fish")  (and (hasInventorySpace) (hasRequiredTool) (isAtFishingZone)))
    ;; What will execute when this node runs.
    (execute [] (goFishing) (int 3000))))
