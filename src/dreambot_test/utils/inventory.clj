(ns dreambot-test.utils.inventory)

(import
 [org.dreambot.api.methods.container.impl Inventory])

(defn inventoryIsFull
  "Checks if player's inventory is full"
  []
  (Inventory/isFull))

(defn hasRequiredItems
  "Checks whether that player has the required tool"
  [itemList]
  (Inventory/containsAll itemList))

(defn hasInventorySpace
  "Makes sure there is space in the player's inventory"
  []
  (not (Inventory/isFull)))
