(ns dreambot-test.fishnode)
;; Is there a way to pass a reference to the Client, Area etc into this and have it run? These functions are not pure
(import
 [org.dreambot.api.methods.container.impl Inventory]
 [org.dreambot.api.methods.map Area]
 [org.dreambot.api Client]
 [org.dreambot.api.methods MethodProvider])

(defn isAtFishingZone
  "Checks whether the player is in the fishing zone"
  []
  ;; Catherby
  ;; (def catherbyFishing (new Area 2833 3436 2861 3421))
  ;; (def inArea (.contains catherbyFishing (.getLocalPlayer Client)))
  ;; TODO: Move the area & defintion to top level and pass in if this works OK
  ;; (let [atLocation (.contains (new Area 2833 3436 2861 3421) #(Client/getLocalPlayer))]
  ;;   (MethodProvider/log (str "at location " atLocation))
  ;;   atLocation)
  true)

(defn hasInventorySpace
  "Checks whether the players inventory is full"
  []
  ;; (let [inventoryFull #(Inventory/isFull)]
  ;;   (MethodProvider/log (str "Is inventory full" inventoryFull))
  ;;   inventoryFull)
  (boolean true))

(defn hasRequiredTool
  "Checks whether that player has the required tool"
  []
  ;; (let [hasTool (#(Inventory/contains "Lobster pot"))]
  ;;   (MethodProvider/log (str "Has tool " hasTool))
  ;;   hasTool)
  (boolean true))

(defn goFishing
  "Fishs"
  []
  (let [fishingSpot (.. #(Client/getNPCs) .closest (filter (fn [spot] (.hasAction spot "Cage"))))]
    (if-not (nil? fishingSpot)
      (.interact fishingSpot "Cage")
      (MethodProvider/log "There was a problem interacting with the fishing spot")))
  (MethodProvider/sleepUntil (not (.isAnimating #(Client/getLocalPlayer))) 50000)
  (MethodProvider/sleep 5000))

(defn FishNode
  []
  (proxy [org.dreambot.api.script.TaskNode] []
    (priority [] (int 1))
    ;; Will return 'true' when these conditions are met
    ;; (accept [] (MethodProvider/log "Eval Fishnode")  (MethodProvider/log (str "results " hasRequiredTool)) (boolean hasRequiredTool)) ;;TODO: Figure out why this is evaluating to false
    (accept [] (MethodProvider/log "Eval Fishnode") (let [evalResults (every? true? [hasInventorySpace hasRequiredTool])] (MethodProvider/log (str "results " evalResults)) (boolean hasRequiredTool))) ;;TODO: Figure out why this is evaluating to false
    ;; What will execute when this node runs.
    (execute [] (goFishing) (int 3000))))
