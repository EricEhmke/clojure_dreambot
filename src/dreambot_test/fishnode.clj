(ns dreambot-test.fishnode)
;; Is there a way to pass a reference to the Client, Area etc into this and have it run? These functions are not pure
(import
 [org.dreambot.api.methods.container.impl Inventory]
 [org.dreambot.api.methods.map Area]
 [org.dreambot.api Client]
 [org.dreambot.api.methods.interactive NPCs]
 [org.dreambot.api.wrappers.interactive NPC]
 [org.dreambot.api.methods MethodProvider]
 [org.dreambot.api.methods.filter Filter])

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

(defn goFishing
  "Fishs"
  []
  ;; (let [fishingSpot (#(Client/getNPCs) (filter (fn [spot] (.hasAction spot "Cage"))))]
  ;;   (if-not (nil? fishingSpot)
  ;;     (.interact fishingSpot "Cage")
  ;;     (MethodProvider/log "There was a problem interacting with the fishing spot")))
  ;; (let [fishingSpot  #(NPCs/closest (fn [x] (.hasAction x "Cage")))]
  ;;   (MethodProvider/log (str "Found fishing spot " fishingSpot))
  ;;   (if-not (nil? fishingSpot)
  ;;     (.interact #(fishingSpot) (:action "Cage"))
  ;; (.interact #(NPCs/closest (fn [x] (.hasAction x "Cage"))))
  (let [fishingSpot  (NPCs/closest (into-array ["Fishing spot"]))]
  ;; (let [fishingSpot  (.closest (Client/getNPCs) (fn [x] (.hasAction x "Cage")))]
    (MethodProvider/log (str "Found fishing spot " fishingSpot))
    (if (.interact fishingSpot "Cage")
      (do
        (MethodProvider/log "Fishing now")
        (MethodProvider/sleepUntil (not (.isAnimating (Client/getLocalPlayer))) 50000))
      (MethodProvider/log "Not fishing for some reason"))
    ;; (if-not (nil? fishingSpot)
    ;;   (.interact fishingSpot (:action "Cage"))
    ;;   (MethodProvider/log "There was a problem interacting with the fishing spot"))
    )
  (MethodProvider/sleep 5000))

(defn FishNode
  []
  (proxy [org.dreambot.api.script.TaskNode] []
    (priority [] (int 1))
    ;; Will return 'true' when these conditions are met
    (accept [] (MethodProvider/log "Evaluating whether to fish")  (and (hasInventorySpace) (hasRequiredTool) (isAtFishingZone)))
    ;; What will execute when this node runs.
    (execute [] (goFishing) (int 3000))))
