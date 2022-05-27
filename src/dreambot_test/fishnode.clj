(ns dreambot-test.fishnode)

(import [org.dreambot.api.methods.container.impl Inventory])

(defn FishNode
  []
  (proxy [org.dreambot.api.script.TaskNode] []
    (priority [] (int 1))
    ;; Will return 'true' when these conditions are met
    (accept [] (every? true? [isAtFishingZone hasInventorySpace hasRequiredTool]))
    ;; What will execute when this node runs.
    (execute [] (goFishing) (int 3000))))

(defn isAtFishingZone
  []
  false
  ;;TODO: Check we're at fishing zone
  )

(defn hasInventorySpace
  []
  false
  ;;TODO: Check that player has free inventory space
  )

(defn hasRequiredTool
  []
  false
  ;; Check that the player has the right tool for the chosen fish
  )

(defn goFishing
  []
  false
  ;;TODO: Create the logic for fishing
  )
