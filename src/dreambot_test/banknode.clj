(ns dreambot-test.banknode
  (:require [dreambot-test.utils.utilities :as utils]))
;; TODO: In the future I want to pass a callback or something to this function. There will be too much duplciation doing it this way
(import
 [org.dreambot.api.methods.container.impl Inventory]
 [org.dreambot.api.methods MethodProvider]
 [org.dreambot.api Client]
 [org.dreambot.api.methods.container.impl.bank Bank]
 [org.dreambot.api.utilities.impl Condition])

(defn inventoryIsFull
  []
  "Checks if player's inventory is full"
  (Inventory/isFull))

(defn goBank
  []
  (let [currentLocation (fn [] (.getTile (Client/getLocalPlayer)))
        travelFunc (fn [lastLoc]
                     (if (Bank/openClosest)
                       (do
                         (MethodProvider/log "Arrived at bank...")
                         (MethodProvider/sleep (utils/pollingTime))
                         (Bank/depositAll "Raw Lobster")
                         (MethodProvider/sleep (utils/pollingTime))
                         (Bank/close))
                       (do
                         (MethodProvider/log "Not at bank sleeping for sec")
                         (MethodProvider/sleep (utils/pollingTime 1200 200))
                         (if (= lastLoc (currentLocation))
                           (MethodProvider/log "Aborting: character potentially stuck...")
                           (recur (.getTile (Client/getLocalPlayer)))))))]
    (travelFunc (currentLocation))))

(def noCookables
  "Checks whether that player has items they can cook"
  ())

(defn deposit
  []
  (when (Bank/open)
    (MethodProvider/log "Bank Opened...")
    (MethodProvider/sleep (utils/pollingTime))
    (Bank/depositAll "Raw Lobster")
    (MethodProvider/sleep (utils/pollingTime))
    (Bank/close)))

(defn bankSequence
  []
  (and (inventoryIsFull) (utils/travelFallback (Bank/getClosestBankLocation)) (deposit)))

;; (defn BankNode
;;   []
;;   ;; TODO: Be able to pass in a function that walks to/destination and criteria for evaluate
;;   (proxy [org.dreambot.api.script.TaskNode] []
;;     (priority [] (int 3))
;;     ;; Will return 'true' when these conditions are met
;;     (accept [] (MethodProvider/log "Evaluating whether to bank...") (and (inventoryIsFull)))
;;     ;; What will execute when this node runs.
;;     (execute [] (goBank) (int 5000))))
