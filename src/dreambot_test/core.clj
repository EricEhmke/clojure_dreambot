(ns dreambot-test.core)

(import [org.dreambot.api.methods MethodProvider])

(defn onLoop
  []
  (MethodProvider/log "My test script")
  1000)

;; actions
;; wait times
;; click locaitons
;; click frequencies
