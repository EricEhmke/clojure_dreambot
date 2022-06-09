(ns dreambot-test.utils.inventory)

(import
 [org.dreambot.api.methods.container.impl Inventory])

(defn inventoryIsFull
  "Checks if player's inventory is full"
  []
  (Inventory/isFull))

(defn hasRequiredTools
  "Checks whether that player has the required tool"
  [toolsList]
  (Inventory/containsAll toolsList))

(defn hasInventorySpace
  "Makes sure there is space in the player's inventory"
  []
  (not (Inventory/isFull)))
