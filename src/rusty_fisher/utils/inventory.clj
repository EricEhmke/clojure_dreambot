(ns rusty-fisher.utils.inventory)

(import
 [org.dreambot.api.methods.container.impl Inventory]
 [org.dreambot.api.methods MethodProvider])

(defn inventoryIsFull
  "Checks if player's inventory is full"
  []
  (Inventory/isFull))

(defn hasRequiredItems
  "Checks whether that player has the required tool"
  [itemList]
  (let [hasItems (Inventory/containsAll itemList)]
    (if hasItems
      true
      (do
        (MethodProvider/log "Required equipment not in inventory...")
        false))))

(defn hasInventorySpace
  "Makes sure there is space in the player's inventory"
  []
  (not (Inventory/isFull)))
